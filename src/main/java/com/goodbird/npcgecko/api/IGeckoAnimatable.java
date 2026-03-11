package com.goodbird.npcgecko.api;

/**
 * Interface for accessing and modifying Gecko model configuration on NPCs and scripted blocks.
 * Provides control over model, texture, animation files, and animation names for each state.
 *
 * <p>Obtain via {@link AbstractGeckoAPI#getAnimatable(noppes.npcs.api.entity.ICustomNpc)}
 * or {@link AbstractGeckoAPI#getAnimatable(noppes.npcs.api.block.IBlockScripted)}.</p>
 */
public interface IGeckoAnimatable {

    /**
     * Get the geo model resource location.
     *
     * @return The model path (e.g. "geckolib3:geo/npc.geo.json")
     */
    String getModel();

    /**
     * Set the geo model resource location.
     *
     * @param model The model path (e.g. "geckolib3:geo/npc.geo.json")
     */
    void setModel(String model);

    /**
     * Get the texture resource location.
     *
     * @return The texture path
     */
    String getTexture();

    /**
     * Set the texture resource location.
     *
     * @param texture The texture path
     */
    void setTexture(String texture);

    /**
     * Get the animation file resource location.
     *
     * @return The animation file path (e.g. "npcgecko:animations/geo_npc.animation.json")
     */
    String getAnimationFile();

    /**
     * Set the animation file resource location.
     *
     * @param animationFile The animation file path
     */
    void setAnimationFile(String animationFile);

    /**
     * Get the idle animation name.
     *
     * @return The idle animation name, or empty string if none
     */
    String getIdleAnimation();

    /**
     * Set the idle animation name.
     *
     * @param animation The animation name from the animation file
     */
    void setIdleAnimation(String animation);

    /**
     * Get the walk animation name.
     *
     * @return The walk animation name, or empty string if none
     */
    String getWalkAnimation();

    /**
     * Set the walk animation name.
     *
     * @param animation The animation name from the animation file
     */
    void setWalkAnimation(String animation);

    /**
     * Get the hurt animation name.
     *
     * @return The hurt animation name, or empty string if none
     */
    String getHurtAnimation();

    /**
     * Set the hurt animation name.
     *
     * @param animation The animation name from the animation file
     */
    void setHurtAnimation(String animation);

    /**
     * Get the melee attack animation name.
     *
     * @return The melee attack animation name, or empty string if none
     */
    String getMeleeAttackAnimation();

    /**
     * Set the melee attack animation name.
     *
     * @param animation The animation name from the animation file
     */
    void setMeleeAttackAnimation(String animation);

    /**
     * Get the ranged attack animation name.
     *
     * @return The ranged attack animation name, or empty string if none
     */
    String getRangedAttackAnimation();

    /**
     * Set the ranged attack animation name.
     *
     * @param animation The animation name from the animation file
     */
    void setRangedAttackAnimation(String animation);

    /**
     * Get the bone name used for head tracking/rotation.
     *
     * @return The head bone name (default: "head")
     */
    String getHeadBoneName();

    /**
     * Set the bone name used for head tracking/rotation.
     *
     * @param boneName The bone name in the model
     */
    void setHeadBoneName(String boneName);

    /**
     * Get the transition duration between animations in ticks.
     *
     * @return Transition length in ticks (default: 10)
     */
    int getTransitionLengthTicks();

    /**
     * Set the transition duration between animations in ticks.
     *
     * @param ticks Transition length in ticks
     */
    void setTransitionLengthTicks(int ticks);
}
