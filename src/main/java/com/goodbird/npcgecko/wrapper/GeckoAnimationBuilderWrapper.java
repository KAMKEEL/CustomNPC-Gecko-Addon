package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IGeckoAnimationBuilder;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

/**
 * Wrapper around GeckoLib's {@link AnimationBuilder} that implements {@link IGeckoAnimationBuilder}.
 * This allows the scripting API to work with animation builders without exposing GeckoLib internals.
 */
public class GeckoAnimationBuilderWrapper implements IGeckoAnimationBuilder {
    private final AnimationBuilder builder;

    public GeckoAnimationBuilderWrapper(AnimationBuilder builder) {
        this.builder = builder;
    }

    @Override
    public IGeckoAnimationBuilder playOnce(String animationName) {
        builder.playOnce(animationName);
        return this;
    }

    @Override
    public IGeckoAnimationBuilder loop(String animationName) {
        builder.loop(animationName);
        return this;
    }

    @Override
    public IGeckoAnimationBuilder playAndHold(String animationName) {
        builder.playAndHold(animationName);
        return this;
    }

    @Override
    public IGeckoAnimationBuilder addAnimation(String animationName) {
        builder.addAnimation(animationName);
        return this;
    }

    @Override
    public IGeckoAnimationBuilder addRepeatingAnimation(String animationName, int timesToRepeat) {
        builder.addRepeatingAnimation(animationName, timesToRepeat);
        return this;
    }

    @Override
    public IGeckoAnimationBuilder clearAnimations() {
        builder.clearAnimations();
        return this;
    }

    /**
     * Get the underlying GeckoLib AnimationBuilder.
     * Used internally for network sync and animation application.
     *
     * @return The wrapped AnimationBuilder
     */
    public AnimationBuilder getBuilder() {
        return builder;
    }
}
