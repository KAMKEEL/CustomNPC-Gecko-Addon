package com.goodbird.npcgecko.client;

import com.goodbird.npcgecko.constants.EnumItemAnimType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client-side tracker for item animation states.
 * Keyed by GeckoLibID (the stable unique ID assigned to each ItemStack).
 * Tracks both the animation type and a sequence counter for swing re-triggering.
 */
public class ItemAnimationStateTracker {

    public static class AnimState {
        public final EnumItemAnimType type;
        public final int sequence;

        public AnimState(EnumItemAnimType type, int sequence) {
            this.type = type;
            this.sequence = sequence;
        }
    }

    private static final Map<Integer, AnimState> activeAnimations = new ConcurrentHashMap<>();

    public static void setAnimation(int geckoLibId, EnumItemAnimType type, int sequence) {
        activeAnimations.put(geckoLibId, new AnimState(type, sequence));
    }

    public static void clearAnimation(int geckoLibId) {
        activeAnimations.remove(geckoLibId);
    }

    public static AnimState getAnimation(int geckoLibId) {
        return activeAnimations.get(geckoLibId);
    }

    public static void clear() {
        activeAnimations.clear();
    }
}
