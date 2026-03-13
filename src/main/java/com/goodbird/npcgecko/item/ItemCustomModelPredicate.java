package com.goodbird.npcgecko.item;

import com.goodbird.npcgecko.client.ItemAnimationStateTracker;
import com.goodbird.npcgecko.client.ItemRenderContext;
import com.goodbird.npcgecko.constants.EnumItemAnimType;
import com.goodbird.npcgecko.data.CustomItemModelData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.item.AnimatableStackWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Animation predicate for gecko item models (equipped only).
 * Uses a priority chain: swing > use > idle.
 *
 * All tracking maps are keyed by animId (entityId << 4 | slot), NOT geckoLibId.
 * This ensures copies of the same item on different entities animate independently.
 *
 * INVENTORY/ENTITY rendering skips animation entirely (bones are reset to bind
 * pose in ModelItemRenderUtil), so this predicate is never called for those types.
 */
public class ItemCustomModelPredicate {

    private static final Map<Integer, Integer> lastProcessedSwingSeq = new HashMap<>();
    private static final Map<Integer, Boolean> fpSwingActive = new HashMap<>();

    public static <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        P animatable = event.getAnimatable();
        if (!(animatable instanceof AnimatableStackWrapper)) return PlayState.STOP;
        AnimatableStackWrapper wrapper = (AnimatableStackWrapper) animatable;

        Object userData = wrapper.getUserData();
        if (!(userData instanceof ItemRenderContext)) return PlayState.STOP;
        ItemRenderContext ctx = (ItemRenderContext) userData;
        CustomItemModelData data = ctx.modelData;
        if (data == null) return PlayState.STOP;

        AnimationController<?> controller = event.getController();
        ItemRenderType renderType = ctx.renderType;
        int animId = (ctx.entityId << 4) | (ctx.slotIndex & 0xF);

        boolean isFirstPerson = (renderType == ItemRenderType.EQUIPPED_FIRST_PERSON);

        // Keep transition length in sync with model data (script may change it at runtime)
        controller.transitionLengthTicks = data.getTransitionLengthTicks();

        // Select perspective-specific animations
        String swingAnim = isFirstPerson ? data.getSwingAnimFP() : data.getSwingAnimTP();
        String useAnim = isFirstPerson ? data.getUseAnimFP() : data.getUseAnimTP();
        String idleAnim = isFirstPerson ? data.getIdleAnimFP() : data.getIdleAnimTP();

        if (isFirstPerson) {
            // ================================================================
            // FIRST PERSON: local player, read state directly from client
            // ================================================================
            EntityPlayer localPlayer = Minecraft.getMinecraft().thePlayer;
            if (localPlayer == null) return PlayState.STOP;

            Boolean fpActive = fpSwingActive.get(animId);
            boolean swingActive = fpActive != null && fpActive;

            // --- Swing (allows interruption: new clicks restart the animation) ---
            boolean isSwinging = localPlayer.isSwingInProgress;
            if (isSwinging && swingAnim != null && !swingAnim.isEmpty()) {
                // Start OR restart the swing animation on every new click
                fpSwingActive.put(animId, true);
                localPlayer.isSwingInProgress = false;
                localPlayer.swingProgressInt = 0;
                controller.markNeedsReload();
                controller.setAnimation(new AnimationBuilder().playOnce(swingAnim));
                return PlayState.CONTINUE;
            }

            if (swingActive) {
                if (controller.getAnimationState() == AnimationState.Stopped) {
                    fpSwingActive.put(animId, false);
                    // Fall through to use/idle
                } else {
                    // Swing still playing, no new click — suppress vanilla swing
                    localPlayer.isSwingInProgress = false;
                    localPlayer.swingProgressInt = 0;
                    return PlayState.CONTINUE;
                }
            }

            // --- Use ---
            if (localPlayer.isUsingItem() && useAnim != null && !useAnim.isEmpty()) {
                controller.setAnimation(new AnimationBuilder().loop(useAnim));
                return PlayState.CONTINUE;
            }

        } else {
            // ================================================================
            // THIRD PERSON: read from ItemAnimationStateTracker (keyed by animId)
            // ================================================================

            // Clear any stale FP swing flag — prevents getting stuck when
            // toggling F5 (FP→TP→FP) while a swing was active
            fpSwingActive.remove(animId);

            ItemAnimationStateTracker.AnimState trackerState = ItemAnimationStateTracker.getAnimation(animId);

            // --- Swing (sequence-based detection) ---
            if (trackerState != null && trackerState.type == EnumItemAnimType.SWING
                    && swingAnim != null && !swingAnim.isEmpty()) {
                Integer lastSeq = lastProcessedSwingSeq.get(animId);
                if (lastSeq == null || lastSeq != trackerState.sequence) {
                    // New swing detected
                    lastProcessedSwingSeq.put(animId, trackerState.sequence);
                    controller.markNeedsReload();
                    controller.setAnimation(new AnimationBuilder().playOnce(swingAnim));
                    return PlayState.CONTINUE;
                }
                // Same swing, still playing
                if (controller.getAnimationState() != AnimationState.Stopped) {
                    return PlayState.CONTINUE;
                }
                // Swing finished, fall through to idle
            }

            // --- Use ---
            if (trackerState != null && trackerState.type == EnumItemAnimType.USE_START
                    && useAnim != null && !useAnim.isEmpty()) {
                controller.setAnimation(new AnimationBuilder().loop(useAnim));
                return PlayState.CONTINUE;
            }
        }

        // --- Idle (lowest priority, both perspectives) ---
        if (idleAnim != null && !idleAnim.isEmpty()) {
            controller.setAnimation(new AnimationBuilder().loop(idleAnim));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }
}
