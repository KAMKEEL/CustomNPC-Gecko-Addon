package com.goodbird.npcgecko.api;

/**
 * Builder interface for constructing animation sequences.
 * Follows the builder pattern where all methods return this builder for chaining.
 *
 * <p>Create an instance via {@link AbstractGeckoAPI#createAnimationBuilder()}.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * var builder = gecko.createAnimationBuilder();
 * builder.playOnce("animation.npc.attack").loop("animation.npc.idle");
 * gecko.syncAnimForAll(npc, builder);
 * </pre>
 *
 * @see AbstractGeckoAPI#createAnimationBuilder()
 * @see AbstractGeckoAPI#syncAnimForAll
 * @see AbstractGeckoAPI#syncAnimForPlayer
 */
public interface IGeckoAnimationBuilder {

    /**
     * Add an animation that plays once, then advances to the next in the queue.
     *
     * @param animationName the animation name as defined in the Blockbench animation file
     * @return this builder for chaining
     */
    IGeckoAnimationBuilder playOnce(String animationName);

    /**
     * Add an animation that loops indefinitely until replaced by another animation.
     *
     * @param animationName the animation name as defined in the Blockbench animation file
     * @return this builder for chaining
     */
    IGeckoAnimationBuilder loop(String animationName);

    /**
     * Add an animation that plays once and holds on the last frame.
     *
     * @param animationName the animation name as defined in the Blockbench animation file
     * @return this builder for chaining
     */
    IGeckoAnimationBuilder playAndHold(String animationName);

    /**
     * Add an animation to the queue using the default loop behavior
     * defined in the animation file.
     *
     * @param animationName the animation name as defined in the Blockbench animation file
     * @return this builder for chaining
     */
    IGeckoAnimationBuilder addAnimation(String animationName);

    /**
     * Add an animation to the queue that repeats a specified number of times.
     *
     * @param animationName the animation name as defined in the Blockbench animation file
     * @param timesToRepeat how many times to repeat the animation
     * @return this builder for chaining
     */
    IGeckoAnimationBuilder addRepeatingAnimation(String animationName, int timesToRepeat);

    /**
     * Clear all animations currently queued in this builder.
     *
     * @return this builder for chaining
     */
    IGeckoAnimationBuilder clearAnimations();
}
