package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IGeckoAnimation;
import software.bernie.geckolib3.core.builder.Animation;

/**
 * Wrapper around GeckoLib's {@link Animation} that implements {@link IGeckoAnimation}.
 */
public class GeckoAnimationWrapper implements IGeckoAnimation {
    private final Animation animation;

    public GeckoAnimationWrapper(Animation animation) {
        this.animation = animation;
    }

    @Override
    public String getName() {
        return animation.animationName;
    }

    @Override
    public double getLength() {
        return animation.animationLength != null ? animation.animationLength : 0.0;
    }

    @Override
    public boolean isLooping() {
        return animation.loop != null && animation.loop.isRepeatingAfterEnd();
    }

    @Override
    public String[] getAnimatedBoneNames() {
        if (animation.boneAnimations == null) {
            return new String[0];
        }
        String[] names = new String[animation.boneAnimations.size()];
        for (int i = 0; i < animation.boneAnimations.size(); i++) {
            names[i] = animation.boneAnimations.get(i).boneName;
        }
        return names;
    }

    @Override
    public int getSoundKeyframeCount() {
        return animation.soundKeyFrames != null ? animation.soundKeyFrames.size() : 0;
    }

    @Override
    public int getParticleKeyframeCount() {
        return animation.particleKeyFrames != null ? animation.particleKeyFrames.size() : 0;
    }

    @Override
    public int getCustomInstructionKeyframeCount() {
        return animation.customInstructionKeyframes != null ? animation.customInstructionKeyframes.size() : 0;
    }
}
