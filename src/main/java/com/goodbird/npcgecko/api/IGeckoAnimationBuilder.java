package com.goodbird.npcgecko.api;

/**
 * Builder interface for constructing animation sequences.
 * Follows the builder pattern - all methods return the builder for chaining.
 *
 * <p>Example usage in scripts:</p>
 * <pre>
 * var builder = GeckoAPI.Instance().createAnimationBuilder();
 * builder.playOnce("attack").loop("idle");
 * GeckoAPI.Instance().syncAnimForAll(npc, builder);
 * </pre>
 */
public interface IGeckoAnimationBuilder {

    /**
     * Add an animation that plays once then moves to the next in queue.
     *
     * @param animationName The animation name matching the Blockbench file
     * @return This builder for chaining
     */
    IGeckoAnimationBuilder playOnce(String animationName);

    /**
     * Add an animation that loops indefinitely until replaced.
     *
     * @param animationName The animation name matching the Blockbench file
     * @return This builder for chaining
     */
    IGeckoAnimationBuilder loop(String animationName);

    /**
     * Add an animation that plays once and holds on the last frame.
     *
     * @param animationName The animation name matching the Blockbench file
     * @return This builder for chaining
     */
    IGeckoAnimationBuilder playAndHold(String animationName);

    /**
     * Add an animation to the queue with default loop behavior from the animation file.
     *
     * @param animationName The animation name matching the Blockbench file
     * @return This builder for chaining
     */
    IGeckoAnimationBuilder addAnimation(String animationName);

    /**
     * Add an animation to the queue repeated multiple times.
     *
     * @param animationName The animation name matching the Blockbench file
     * @param timesToRepeat How many times to repeat the animation
     * @return This builder for chaining
     */
    IGeckoAnimationBuilder addRepeatingAnimation(String animationName, int timesToRepeat);

    /**
     * Clear all animations currently in the builder.
     *
     * @return This builder for chaining
     */
    IGeckoAnimationBuilder clearAnimations();
}
