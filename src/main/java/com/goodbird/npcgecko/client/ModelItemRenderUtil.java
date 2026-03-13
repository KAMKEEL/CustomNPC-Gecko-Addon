package com.goodbird.npcgecko.client;

import com.goodbird.npcgecko.client.model.ItemModelCustom;
import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.data.ItemDisplayData;
import com.goodbird.npcgecko.data.ItemDisplayTransform;
import com.goodbird.npcgecko.item.ItemCustomModelPredicate;
import com.goodbird.npcgecko.network.ItemAnimSyncable;
import net.geckominecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.manager.SingletonAnimationFactory;
import software.bernie.geckolib3.core.snapshot.BoneSnapshot;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.item.AnimatableStackWrapper;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.example.config.ConfigHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Central rendering orchestrator for gecko item models.
 * Implements IGeoRenderer directly.
 *
 * Equipped items (FP/TP) use a shared SingletonAnimationFactory keyed by
 * entity-slot composite ID, so different entities holding copies of the same
 * item get independent animation state.
 *
 * INVENTORY/ENTITY renders skip animation entirely and reset bones to bind
 * pose, preventing the shared mutable GeoModel from leaking equipped
 * animation transforms into display renders.
 */
public class ModelItemRenderUtil implements IGeoRenderer<AnimatableStackWrapper> {

    private static final ModelItemRenderUtil INSTANCE = new ModelItemRenderUtil();
    private static final AnimatedGeoModel<AnimatableStackWrapper> MODEL_PROVIDER = new ItemModelCustom();

    /**
     * Separate factory for INVENTORY/ENTITY rendering.
     * Prevents display-only renders from interfering with equipped animation state
     * (they share no AnimationData since they use different factory instances).
     */
    private static final SingletonAnimationFactory displayFactory;

    static {
        // Model fetcher for AnimatableStackWrapper
        AnimationController.addModelFetcher((IAnimatable object) -> {
            if (object instanceof AnimatableStackWrapper) {
                return (IAnimatableModel) MODEL_PROVIDER;
            }
            return null;
        });

        // Display factory — isolated from the equipped/sync factory
        IAnimatable displayDummy = new IAnimatable() {
            @Override
            public void registerControllers(AnimationData data) {
                data.addAnimationController(
                    new AnimationController<>(this, "item_controller", 0, e -> PlayState.STOP));
            }
            @Override
            public AnimationFactory getFactory() { return displayFactory; }
        };
        displayFactory = new SingletonAnimationFactory(displayDummy);
    }

    private static final WeakHashMap<ItemStack, CustomItemModelData> modelDataCache = new WeakHashMap<>();

    /**
     * EQUIPPED cache: keyed by animId (entityId << 4 | slot).
     * Each entity-slot combination gets its own persistent animation state,
     * so copies of the same item on different entities animate independently.
     */
    private static final Map<Integer, AnimatableStackWrapper> equippedWrapperCache = new HashMap<>();

    /**
     * INVENTORY/ENTITY cache: keyed by ItemStack identity.
     * These wrappers are only used for model/texture lookup — no animation.
     */
    private static final WeakHashMap<ItemStack, AnimatableStackWrapper> nonEquippedWrapperCache = new WeakHashMap<>();

    // Current render state (set per-render, single-threaded)
    private AnimatableStackWrapper currentWrapper;
    private ItemStack currentItemStack;
    private int currentAnimId;
    private Color currentItemColor = Color.WHITE;

    /** Frame counter for rotation rate, incremented each render call (like original's item3dRenderTicks). */
    private static int renderTickCounter = 0;

    /** CustomNPC+'s default item color — treated as "no tint" for 3D models. */
    private static final int DEFAULT_NPC_ITEM_COLOR = 0x8B4513;

    /**
     * Quick check if an ItemStack has a gecko model attached.
     */
    public static boolean hasGeckoModel(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasTagCompound()) return false;
        return itemStack.getTagCompound().hasKey("GeckoModelData");
    }

    /**
     * Undoes ForgeHooksClient's sprite path transforms and applies the simpler
     * EQUIPPED_BLOCK translate instead.
     */
    private static void normalizeEquipped() {
        GL11.glTranslatef(0.9375F, 0.0625F, 0.0F);
        GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(1.0F / 1.5F, 1.0F / 1.5F, 1.0F / 1.5F);
        GL11.glTranslatef(0.0F, 0.3F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    }

    /**
     * Computes a unique animation ID from entity ID and slot index.
     * Different entities holding copies of the same item get different IDs.
     */
    private static int computeAnimId(int entityId, int slotIndex) {
        return (entityId << 4) | (slotIndex & 0xF);
    }

    /**
     * Resets all bones in the model to their initial bind pose.
     * Prevents animation transforms from the equipped render bleeding
     * into INVENTORY/ENTITY renders via the shared cached GeoModel.
     */
    private static void resetBonesToBindPose(GeoModel model) {
        for (GeoBone bone : model.topLevelBones) {
            resetBoneRecursive(bone);
        }
    }

    private static void resetBoneRecursive(GeoBone bone) {
        BoneSnapshot initial = bone.getInitialSnapshot();
        if (initial != null) {
            bone.setPositionX(initial.positionOffsetX);
            bone.setPositionY(initial.positionOffsetY);
            bone.setPositionZ(initial.positionOffsetZ);
            bone.setRotationX(initial.rotationValueX);
            bone.setRotationY(initial.rotationValueY);
            bone.setRotationZ(initial.rotationValueZ);
            bone.setScaleX(initial.scaleValueX);
            bone.setScaleY(initial.scaleValueY);
            bone.setScaleZ(initial.scaleValueZ);
        }
        for (GeoBone child : bone.childBones) {
            resetBoneRecursive(child);
        }
    }

    /**
     * Attempts to render a gecko model for the given item.
     */
    public static boolean tryRender(ItemRenderType type, ItemStack itemStack, Object[] data) {
        if (itemStack == null || !itemStack.hasTagCompound()) return false;
        NBTTagCompound root = itemStack.getTagCompound();
        if (!root.hasKey("GeckoModelData")) return false;

        // Always re-read from NBT so script changes (animations, transitions, etc.)
        // take effect immediately without needing an ItemStack reference change.
        CustomItemModelData modelData = modelDataCache.get(itemStack);
        if (modelData == null) {
            modelData = new CustomItemModelData();
            modelDataCache.put(itemStack, modelData);
        }
        modelData.readFromNBT(root.getCompoundTag("GeckoModelData"));

        // Extract entity for EQUIPPED types
        EntityLivingBase entity = null;
        int entityId = 0;
        int slotIndex = -1;
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            // Try to find entity in the data array (CustomNPC+ may not follow
            // Forge's standard data[0] = entity convention)
            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (data[i] instanceof EntityLivingBase) {
                        entity = (EntityLivingBase) data[i];
                        break;
                    }
                }
            }
            // Fallback: for first-person, we know it's the local player
            if (entity == null && type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                entity = Minecraft.getMinecraft().thePlayer;
            }
            if (entity != null) {
                entityId = entity.getEntityId();
                if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
                    slotIndex = ((net.minecraft.entity.player.EntityPlayer) entity).inventory.currentItem;
                } else {
                    slotIndex = 0;
                }
            }
        }

        // Look up or create wrapper
        AnimatableStackWrapper wrapper;
        boolean isEquipped = (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON);
        int animId = 0;

        if (isEquipped) {
            animId = computeAnimId(entityId, slotIndex);
            AnimationFactory sharedFactory = ItemAnimSyncable.INSTANCE.getSharedFactory();
            wrapper = equippedWrapperCache.get(animId);
            if (wrapper == null) {
                int transitionTicks = modelData.getTransitionLengthTicks();
                wrapper = AnimatableStackWrapper.of(itemStack, sharedFactory)
                        .withControllerRegistrar((w, d) -> {
                            d.addAnimationController(new AnimationController<>(w, "item_controller", transitionTicks, ItemCustomModelPredicate::predicate));
                        });
                equippedWrapperCache.put(animId, wrapper);
            } else if (wrapper.getStack() != itemStack) {
                wrapper.setStack(itemStack);
            }
            wrapper.withUserData(new ItemRenderContext(modelData, type, entity, entityId, slotIndex));
        } else {
            // INVENTORY/ENTITY: no animation, just model/texture lookup
            wrapper = nonEquippedWrapperCache.get(itemStack);
            if (wrapper == null) {
                wrapper = AnimatableStackWrapper.of(itemStack, displayFactory);
                nonEquippedWrapperCache.put(itemStack, wrapper);
            }
            // userData is still needed for model/texture resolution
            wrapper.withUserData(new ItemRenderContext(modelData, type, null, 0, -1));
        }

        ItemDisplayTransform transform = resolveTransform(modelData, type);

        GL11.glPushMatrix();

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            normalizeEquipped();
        }

        applyTranslate(transform, type);
        applyRotate(transform, type);
        applyScale(transform, type);

        // Apply CustomNPC+ scripted item display properties (translate, rotation,
        // rotation rate, scale, color) from the ItemData NBT tag.
        applyCustomNpcItemData(root);
        renderTickCounter++;

        if (type == ItemRenderType.INVENTORY)
            RenderHelper.disableStandardItemLighting();

        INSTANCE.renderItem(wrapper, itemStack, animId, isEquipped);

        if (type == ItemRenderType.INVENTORY)
            RenderHelper.enableStandardItemLighting();

        GL11.glPopMatrix();
        return true;
    }

    /**
     * Drives GeckoLib model loading, animation ticking, and rendering.
     *
     * @param animate true for equipped items (tick animation), false for
     *                INVENTORY/ENTITY (reset to bind pose, no animation).
     */
    private void renderItem(AnimatableStackWrapper wrapper, ItemStack itemStack, int animId, boolean animate) {
        this.currentWrapper = wrapper;
        this.currentItemStack = itemStack;
        this.currentAnimId = animId;

        GeoModel model;
        try {
            model = MODEL_PROVIDER.getModel(MODEL_PROVIDER.getModelLocation(wrapper));
        } catch (Exception e) {
            if (ConfigHandler.debugPrintStacktraces) {
                e.printStackTrace();
            }
            return;
        }

        if (animate) {
            AnimationEvent<AnimatableStackWrapper> itemEvent = new AnimationEvent<>(wrapper, 0, 0,
                    Minecraft.getMinecraft().timer.renderPartialTicks, false,
                    Collections.singletonList(itemStack));
            wrapper.getFactory().getOrCreateAnimationData(animId, wrapper);
            MODEL_PROVIDER.setLivingAnimations(wrapper, animId, itemEvent);
        } else {
            // Non-equipped (INVENTORY/ENTITY): no animation runs, so manually
            // reset bones to bind pose to prevent stale transforms from the
            // last equipped render bleeding into display renders.
            resetBonesToBindPose(model);
        }

        GlStateManager.pushMatrix();
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(getTextureLocation(wrapper));
            Color renderColor = getRenderColor(wrapper, 0f);
            render(model, wrapper, 0, (float) renderColor.getRed() / 255f,
                    (float) renderColor.getGreen() / 255f,
                    (float) renderColor.getBlue() / 255f,
                    (float) renderColor.getAlpha() / 255f);
        } catch (Exception e) {
            if (ConfigHandler.debugPrintStacktraces) {
                e.printStackTrace();
            }
        } finally {
            GlStateManager.popMatrix();
        }
    }

    @Override
    public AnimatedGeoModel<AnimatableStackWrapper> getGeoModelProvider() {
        return MODEL_PROVIDER;
    }

    @Override
    public ResourceLocation getTextureLocation(AnimatableStackWrapper instance) {
        return MODEL_PROVIDER.getTextureLocation(instance);
    }

    @Override
    public Integer getUniqueID(AnimatableStackWrapper animatable) {
        // Return entity-slot animId, NOT the GeckoLibID from NBT.
        // Duplicated stacks share the same GeckoLibID, so using it here
        // would cause GeckoLib internals to cross-contaminate between entities.
        return currentAnimId;
    }

    @Override
    public Color getRenderColor(AnimatableStackWrapper animatable, float partialTicks) {
        return currentItemColor;
    }

    // ========================================================================
    // Transform helpers
    // ========================================================================

    public static void applyTranslate(ItemDisplayTransform t, ItemRenderType type) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0.5F, 0.55F, 0.5F);
                break;
            case EQUIPPED:
                GL11.glTranslatef(0.75F, 0.4F, 0F);
                break;
            case INVENTORY:
                GL11.glTranslatef(0F, -0.1F, 0F);
                break;
            case ENTITY:
                GL11.glTranslatef(0F, 0.0F, 0F);
                break;
            default:
                break;
        }
        if (t != null && t.hasTranslation()) {
            GL11.glTranslatef(t.getTranslateX(), t.getTranslateY(), t.getTranslateZ());
        }
    }

    public static void applyRotate(ItemDisplayTransform t, ItemRenderType type) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                GL11.glRotatef(-45F, 0.0F, 1.0F, 0.0F);
                break;
            case EQUIPPED:
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(33F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-5F, 0.0F, 1.0F, 0.0F);
                break;
            case INVENTORY:
                GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(30F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(175F, 0.0F, 1.0F, 0.0F);
                break;
            case ENTITY:
                break;
            default:
                break;
        }
        if (t != null && t.hasRotation()) {
            GL11.glRotatef(t.getRotationY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(t.getRotationX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(t.getRotationZ(), 0.0F, 0.0F, 1.0F);
        }
    }

    public static void applyScale(ItemDisplayTransform t, ItemRenderType type) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                float scale = 2.36f;
                GL11.glScalef(scale, scale, scale);
                break;
            case EQUIPPED:
                scale = 1.0f;
                GL11.glScalef(scale, scale, scale);
                break;
            case INVENTORY:
                scale = 1.5f;
                GL11.glScalef(scale, scale, scale);
                break;
            case ENTITY:
                break;
            default:
                break;
        }
        if (t != null && t.hasScale()) {
            GL11.glScalef(t.getScaleX(), t.getScaleY(), t.getScaleZ());
        }
    }

    /**
     * Reads CustomNPC+'s ItemData NBT and applies the scripted item display
     * properties (translate, rotation, rotation rate, scale) as GL transforms,
     * and sets the color for getRenderColor().
     *
     * These are the properties set via scripting API: setScale(), setRotation(),
     * setTranslate(), setColor(), setRotationRate().
     */
    private static void applyCustomNpcItemData(NBTTagCompound root) {
        if (!root.hasKey("ItemData")) {
            INSTANCE.currentItemColor = Color.WHITE;
            return;
        }
        NBTTagCompound itemData = root.getCompoundTag("ItemData");

        // --- Color ---
        // The default CustomNPC+ item color (0x8B4513 / saddle brown) is a sprite
        // tint that doesn't make sense for 3D models. Treat it as "no tint".
        if (itemData.hasKey("ItemColor")) {
            int color = itemData.getInteger("ItemColor");
            if (color != DEFAULT_NPC_ITEM_COLOR) {
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;
                INSTANCE.currentItemColor = Color.ofRGBA(r, g, b, 255);
            } else {
                INSTANCE.currentItemColor = Color.WHITE;
            }
        } else {
            INSTANCE.currentItemColor = Color.WHITE;
        }

        // --- Translation ---
        float tx = itemData.hasKey("TranslateX") ? itemData.getFloat("TranslateX") : 0;
        float ty = itemData.hasKey("TranslateY") ? itemData.getFloat("TranslateY") : 0;
        float tz = itemData.hasKey("TranslateZ") ? itemData.getFloat("TranslateZ") : 0;
        if (tx != 0 || ty != 0 || tz != 0) {
            GL11.glTranslatef(tx, ty, tz);
        }

        // --- Rotation (static) ---
        float rx = itemData.hasKey("RotationX") ? itemData.getFloat("RotationX") : 0;
        float ry = itemData.hasKey("RotationY") ? itemData.getFloat("RotationY") : 0;
        float rz = itemData.hasKey("RotationZ") ? itemData.getFloat("RotationZ") : 0;
        if (rx != 0) GL11.glRotatef(rx, 1, 0, 0);
        if (ry != 0) GL11.glRotatef(ry, 0, 1, 0);
        if (rz != 0) GL11.glRotatef(rz, 0, 0, 1);

        // --- Rotation rate (continuous spinning) ---
        float rxr = itemData.hasKey("RotationXRate") ? itemData.getFloat("RotationXRate") : 0;
        float ryr = itemData.hasKey("RotationYRate") ? itemData.getFloat("RotationYRate") : 0;
        float rzr = itemData.hasKey("RotationZRate") ? itemData.getFloat("RotationZRate") : 0;
        if (rxr != 0 || ryr != 0 || rzr != 0) {
            if (rxr != 0) GL11.glRotatef(rxr * renderTickCounter % 360, 1, 0, 0);
            if (ryr != 0) GL11.glRotatef(ryr * renderTickCounter % 360, 0, 1, 0);
            if (rzr != 0) GL11.glRotatef(rzr * renderTickCounter % 360, 0, 0, 1);
        }

        // --- Scale ---
        float sx = itemData.hasKey("ScaleX") ? itemData.getFloat("ScaleX") : 1;
        float sy = itemData.hasKey("ScaleY") ? itemData.getFloat("ScaleY") : 1;
        float sz = itemData.hasKey("ScaleZ") ? itemData.getFloat("ScaleZ") : 1;
        if (sx != 1 || sy != 1 || sz != 1) {
            GL11.glScalef(sx, sy, sz);
        }
    }

    public static ItemDisplayTransform resolveTransform(
            CustomItemModelData modelData, ItemRenderType type) {
        ItemDisplayTransform nbtTransform = null;
        switch (type) {
            case EQUIPPED_FIRST_PERSON: nbtTransform = modelData.getFirstPerson(); break;
            case EQUIPPED:              nbtTransform = modelData.getThirdPerson(); break;
            case INVENTORY:             nbtTransform = modelData.getInventory(); break;
            case ENTITY:                nbtTransform = modelData.getGround(); break;
            default: break;
        }
        if (nbtTransform != null) return nbtTransform;

        String displayFile = modelData.getDisplayFile();
        if (displayFile != null && !displayFile.isEmpty()) {
            ItemDisplayData fileData = ItemDisplayLoader.getInstance().getDisplayData(displayFile);
            if (fileData != null) {
                switch (type) {
                    case EQUIPPED_FIRST_PERSON: return fileData.getFirstPerson();
                    case EQUIPPED:              return fileData.getThirdPerson();
                    case INVENTORY:             return fileData.getInventory();
                    case ENTITY:                return fileData.getGround();
                    default: break;
                }
            }
        }
        return null;
    }
}
