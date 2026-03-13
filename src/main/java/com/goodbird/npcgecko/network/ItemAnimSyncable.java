package com.goodbird.npcgecko.network;

import com.goodbird.npcgecko.client.ItemAnimationStateTracker;
import com.goodbird.npcgecko.constants.EnumItemAnimType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.manager.SingletonAnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;

/**
 * Singleton ISyncable that handles animation sync for ALL dynamic/scripted items.
 * Owns the shared SingletonAnimationFactory used by all AnimatableStackWrappers.
 *
 * State encoding: (sequence << 8) | (type.ordinal() & 0xFF)
 * This allows the client to detect new swings by comparing sequence numbers.
 */
public class ItemAnimSyncable implements ISyncable {
    public static final ItemAnimSyncable INSTANCE = new ItemAnimSyncable();
    private static final String SYNC_KEY = "npcgecko:item_anim";

    private final SingletonAnimationFactory factory;

    private ItemAnimSyncable() {
        // Dummy animatable for the factory constructor — actual controllers
        // are registered per-ID by the AnimatableStackWrapper via the
        // two-argument getOrCreateAnimationData(id, wrapper) at render time.
        IAnimatable dummy = new IAnimatable() {
            @Override
            public void registerControllers(AnimationData data) {
                data.addAnimationController(
                    new AnimationController<>(this, "item_controller", 0, e -> PlayState.STOP));
            }
            @Override
            public AnimationFactory getFactory() { return factory; }
        };
        this.factory = new SingletonAnimationFactory(dummy);
    }

    public void register() {
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public String getSyncKey() {
        return SYNC_KEY;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        int animTypeOrdinal = state & 0xFF;
        int sequence = state >>> 8;

        EnumItemAnimType[] values = EnumItemAnimType.values();
        if (animTypeOrdinal < 0 || animTypeOrdinal >= values.length) return;
        EnumItemAnimType type = values[animTypeOrdinal];

        if (type == EnumItemAnimType.USE_STOP) {
            ItemAnimationStateTracker.clearAnimation(id);
        } else {
            ItemAnimationStateTracker.setAnimation(id, type, sequence);
        }
    }

    public AnimationFactory getSharedFactory() {
        return factory;
    }

    /**
     * Encodes an EnumItemAnimType and sequence counter into a single int
     * for the ISyncable state parameter.
     */
    public static int encodeState(EnumItemAnimType type, int sequence) {
        return (sequence << 8) | (type.ordinal() & 0xFF);
    }
}
