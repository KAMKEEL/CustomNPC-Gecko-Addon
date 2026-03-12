package com.goodbird.npcgecko.api;

/**
 * Read-only interface for querying animation metadata from a loaded animation file.
 * Provides access to animation properties as defined in Blockbench.
 *
 * <p>Obtain an instance via {@link AbstractGeckoAPI#getAnimation}.</p>
 *
 * @see AbstractGeckoAPI#getAnimation
 * @see AbstractGeckoAPI#getAnimationList
 */
public interface IGeckoAnimation {

    /**
     * Get the animation name as defined in the animation file.
     *
     * @return the animation name
     */
    String getName();

    /**
     * Get the total duration of this animation in seconds.
     *
     * @return the animation length in seconds
     */
    double getLength();

    /**
     * Check whether this animation loops by default as defined in the animation file.
     *
     * @return true if the animation is set to loop
     */
    boolean isLooping();

    /**
     * Get the names of all bones that have keyframes in this animation.
     *
     * @return array of bone names that are animated
     */
    String[] getAnimatedBoneNames();

    /**
     * Get the number of sound effect keyframes in this animation.
     *
     * @return the sound keyframe count
     */
    int getSoundKeyframeCount();

    /**
     * Get the number of particle effect keyframes in this animation.
     *
     * @return the particle keyframe count
     */
    int getParticleKeyframeCount();

    /**
     * Get the number of custom instruction keyframes in this animation.
     *
     * @return the custom instruction keyframe count
     */
    int getCustomInstructionKeyframeCount();
}
