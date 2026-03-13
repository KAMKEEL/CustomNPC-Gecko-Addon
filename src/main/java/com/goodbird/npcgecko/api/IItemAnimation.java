package com.goodbird.npcgecko.api;

/**
 * Interface for configuring per-perspective animation names on a GeckoLib item.
 * Each animation type has a first-person and third-person variant.
 *
 * <p>First-person animations play for the local player's held item view.
 * Third-person animations play when other players see the item being held.</p>
 *
 * <p>Animations are played based on a priority system:</p>
 * <ol>
 *   <li>Swing - plays when the player swings or attacks with the item</li>
 *   <li>Use - plays while the player is using the item (holding right-click)</li>
 *   <li>Idle - loops continuously when no other animation is active</li>
 * </ol>
 *
 * <p>Obtain an instance via {@link IGeckoItemModel#getItemAnimation()}.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * var anim = itemGecko.getItemAnimation();
 * // Set for both perspectives
 * anim.setIdleAnimation("animation.sword.idle");
 * anim.setSwingAnimation("animation.sword.swing");
 * // Set per-perspective (0 = first person, 1 = third person)
 * anim.setSwingAnimation("animation.sword.swing_fp", 0);
 * anim.setSwingAnimation("animation.sword.swing_tp", 1);
 * </pre>
 *
 * @see IGeckoItemModel#getItemAnimation()
 */
public interface IItemAnimation {

    /**
     * Get the idle animation name for first person.
     *
     * @return the idle animation name, or an empty string if none is set
     */
    String getIdleAnimation();

    /**
     * Get the idle animation name for a specific perspective.
     *
     * @param perspective 0 for first person, 1 for third person
     * @return the idle animation name, or an empty string if none is set
     */
    String getIdleAnimation(int perspective);

    /**
     * Set the idle animation name for both perspectives.
     *
     * @param animation the animation name, or an empty string to clear
     */
    void setIdleAnimation(String animation);

    /**
     * Set the idle animation name for a specific perspective.
     *
     * @param animation the animation name, or an empty string to clear
     * @param perspective 0 for first person, 1 for third person
     */
    void setIdleAnimation(String animation, int perspective);

    /**
     * Get the swing animation name for first person.
     *
     * @return the swing animation name, or an empty string if none is set
     */
    String getSwingAnimation();

    /**
     * Get the swing animation name for a specific perspective.
     *
     * @param perspective 0 for first person, 1 for third person
     * @return the swing animation name, or an empty string if none is set
     */
    String getSwingAnimation(int perspective);

    /**
     * Set the swing animation name for both perspectives.
     * This animation plays once when the player swings or attacks with the item.
     *
     * @param animation the animation name, or an empty string to clear
     */
    void setSwingAnimation(String animation);

    /**
     * Set the swing animation name for a specific perspective.
     *
     * @param animation the animation name, or an empty string to clear
     * @param perspective 0 for first person, 1 for third person
     */
    void setSwingAnimation(String animation, int perspective);

    /**
     * Get the use animation name for first person.
     *
     * @return the use animation name, or an empty string if none is set
     */
    String getUseAnimation();

    /**
     * Get the use animation name for a specific perspective.
     *
     * @param perspective 0 for first person, 1 for third person
     * @return the use animation name, or an empty string if none is set
     */
    String getUseAnimation(int perspective);

    /**
     * Set the use animation name for both perspectives.
     * This animation plays while the player is actively using the item.
     *
     * @param animation the animation name, or an empty string to clear
     */
    void setUseAnimation(String animation);

    /**
     * Set the use animation name for a specific perspective.
     *
     * @param animation the animation name, or an empty string to clear
     * @param perspective 0 for first person, 1 for third person
     */
    void setUseAnimation(String animation, int perspective);
}
