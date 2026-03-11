package com.goodbird.npcgecko.api;

/**
 * Interface for querying animation metadata from a loaded animation file.
 * Provides read-only access to animation properties defined in Blockbench.
 *
 * <p>Obtain via {@link AbstractGeckoAPI#getAnimation(String, String)}.</p>
 */
public interface IGeckoAnimation {

    /**
     * Get the animation name as defined in the animation file.
     *
     * @return The animation name
     */
    String getName();

    /**
     * Get the duration of this animation in seconds.
     *
     * @return The animation length in seconds
     */
    double getLength();

    /**
     * Whether this animation loops by default.
     *
     * @return True if the animation loops
     */
    boolean isLooping();

    /**
     * Get the names of all bones that have keyframes in this animation.
     *
     * @return Array of bone names with animation data
     */
    String[] getAnimatedBoneNames();

    /**
     * Get the number of sound keyframes in this animation.
     *
     * @return The sound keyframe count
     */
    int getSoundKeyframeCount();

    /**
     * Get the number of particle keyframes in this animation.
     *
     * @return The particle keyframe count
     */
    int getParticleKeyframeCount();

    /**
     * Get the number of custom instruction keyframes in this animation.
     *
     * @return The custom instruction keyframe count
     */
    int getCustomInstructionKeyframeCount();
}
