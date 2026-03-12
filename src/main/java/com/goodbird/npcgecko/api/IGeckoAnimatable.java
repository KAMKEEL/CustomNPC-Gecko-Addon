package com.goodbird.npcgecko.api;

/**
 * Interface for accessing and modifying GeckoLib model configuration on NPCs and scripted blocks.
 * Provides control over model, texture, animation file, and named animation states.
 *
 * <p>Changes made through this interface are automatically synced to clients.</p>
 *
 * <p>Obtain an instance via {@link AbstractGeckoAPI#getAnimatable}.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * var animatable = gecko.getAnimatable(npc);
 * animatable.setModel("geckolib3:geo/custom_npc.geo.json");
 * animatable.setTexture("geckolib3:textures/model/custom_npc.png");
 * animatable.setAnimationFile("geckolib3:animations/custom_npc.animation.json");
 * animatable.setIdleAnimation("animation.custom_npc.idle");
 * </pre>
 *
 * @see AbstractGeckoAPI#getAnimatable
 */
public interface IGeckoAnimatable {

    /**
     * Get the geo model resource location.
     *
     * @return the model resource location string
     */
    String getModel();

    /**
     * Set the GeckoLib geo model resource location.
     *
     * @param model the model resource location (e.g. "geckolib3:geo/npc.geo.json")
     */
    void setModel(String model);

    /**
     * Get the texture resource location.
     *
     * @return the texture resource location string
     */
    String getTexture();

    /**
     * Set the texture resource location.
     *
     * @param texture the texture resource location (e.g. "geckolib3:textures/model/npc.png")
     */
    void setTexture(String texture);

    /**
     * Get the animation file resource location.
     *
     * @return the animation file resource location string
     */
    String getAnimationFile();

    /**
     * Set the animation file resource location.
     *
     * @param animationFile the animation file resource location
     *        (e.g. "geckolib3:animations/npc.animation.json")
     */
    void setAnimationFile(String animationFile);

    /**
     * Get the idle animation name.
     *
     * @return the idle animation name, or an empty string if none is set
     */
    String getIdleAnimation();

    /**
     * Set the idle animation name.
     * This animation plays continuously while the entity is standing still.
     *
     * @param animation the animation name (e.g. "animation.npc.idle")
     */
    void setIdleAnimation(String animation);

    /**
     * Get the walk animation name.
     *
     * @return the walk animation name, or an empty string if none is set
     */
    String getWalkAnimation();

    /**
     * Set the walk animation name.
     * This animation plays while the entity is moving.
     *
     * @param animation the animation name (e.g. "animation.npc.walk")
     */
    void setWalkAnimation(String animation);

    /**
     * Get the hurt animation name.
     *
     * @return the hurt animation name, or an empty string if none is set
     */
    String getHurtAnimation();

    /**
     * Set the hurt animation name.
     * This animation plays when the entity takes damage.
     *
     * @param animation the animation name (e.g. "animation.npc.hurt")
     */
    void setHurtAnimation(String animation);

    /**
     * Get the melee attack animation name.
     *
     * @return the melee attack animation name, or an empty string if none is set
     */
    String getMeleeAttackAnimation();

    /**
     * Set the melee attack animation name.
     * This animation plays when the entity performs a melee attack.
     *
     * @param animation the animation name (e.g. "animation.npc.melee")
     */
    void setMeleeAttackAnimation(String animation);

    /**
     * Get the ranged attack animation name.
     *
     * @return the ranged attack animation name, or an empty string if none is set
     */
    String getRangedAttackAnimation();

    /**
     * Set the ranged attack animation name.
     * This animation plays when the entity performs a ranged attack.
     *
     * @param animation the animation name (e.g. "animation.npc.ranged")
     */
    void setRangedAttackAnimation(String animation);

    /**
     * Get the bone name used for head tracking and rotation.
     *
     * @return the head bone name (default: "head")
     */
    String getHeadBoneName();

    /**
     * Set the bone name used for head tracking and rotation.
     * The bone must exist in the assigned model file.
     *
     * @param boneName the bone name as defined in the model
     */
    void setHeadBoneName(String boneName);

    /**
     * Get the transition duration between animations in ticks.
     *
     * @return the transition length in ticks (default: 10)
     */
    int getTransitionLengthTicks();

    /**
     * Set the transition duration between animations in ticks.
     * Controls how smoothly one animation blends into the next.
     *
     * @param ticks the transition length in ticks (20 ticks = 1 second)
     */
    void setTransitionLengthTicks(int ticks);
}
