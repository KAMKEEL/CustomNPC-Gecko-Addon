package com.goodbird.npcgecko.api;

/**
 * Interface for accessing and modifying GeckoLib 3D model configuration on items.
 * Provides control over model, texture, animation files, display transforms,
 * and display JSON settings for Scripted and Linked items.
 *
 * <p>Changes made through this interface are automatically persisted to the
 * ItemStack's NBT and synced to clients via container mechanics.</p>
 *
 * <p>Obtain an instance via {@link AbstractGeckoAPI#getItemGecko}.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * var itemGecko = gecko.getItemGecko(event.item);
 * itemGecko.setModel("geckolib3:geo/sword.geo.json");
 * itemGecko.setTexture("geckolib3:textures/model/sword.png");
 * itemGecko.setAnimationFile("geckolib3:animations/sword.animation.json");
 *
 * var anim = itemGecko.getItemAnimation();
 * anim.setIdleAnimation("animation.sword.idle");
 * anim.setSwingAnimation("animation.sword.swing");
 * anim.setAttackAnimation("animation.sword.attack");
 * </pre>
 *
 * @see AbstractGeckoAPI#getItemGecko
 */
public interface IGeckoItemModel {

    /**
     * Check if this item currently has a gecko model assigned.
     *
     * @return true if the item has a GeckoLib 3D model
     */
    boolean hasModel();

    /**
     * Get the geo model resource location.
     *
     * @return the model resource location string, or an empty string if no model is set
     */
    String getModel();

    /**
     * Set the GeckoLib geo model resource location.
     * The model file should be a Blockbench geo JSON exported for GeckoLib.
     *
     * @param model the model resource location (e.g. "geckolib3:geo/sword.geo.json")
     */
    void setModel(String model);

    /**
     * Get the texture resource location.
     *
     * @return the texture resource location string, or an empty string if no texture is set
     */
    String getTexture();

    /**
     * Set the texture resource location used when rendering this item's 3D model.
     *
     * @param texture the texture resource location (e.g. "geckolib3:textures/model/sword.png")
     */
    void setTexture(String texture);

    /**
     * Get the animation file resource location.
     *
     * @return the animation file resource location string, or an empty string if none is set
     */
    String getAnimationFile();

    /**
     * Set the animation file resource location.
     * The file should be a Blockbench animation JSON exported for GeckoLib.
     *
     * @param animationFile the animation file resource location
     *        (e.g. "geckolib3:animations/sword.animation.json")
     */
    void setAnimationFile(String animationFile);

    /**
     * Get the item animation configuration interface.
     * Provides access to getters and setters for all animation types
     * (idle, swing, attack, use).
     *
     * @return the item animation interface
     * @see IItemAnimation
     */
    IItemAnimation getItemAnimation();

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

    /**
     * Get the display JSON filename currently assigned to this item.
     *
     * @return the display JSON filename (e.g. "sword.json"), or an empty string if none is set
     */
    String getDisplayJSON();

    /**
     * Set the display JSON file for controlling how the 3D model is positioned,
     * rotated, and scaled in each render context (first person, third person,
     * inventory, and ground).
     *
     * <p>Display files are loaded from {@code assets/{domain}/item_displays/} resource folders.
     * Use {@link AbstractGeckoAPI#getDisplayFileList()} to list available files.</p>
     *
     * @param displayFile the display JSON filename (e.g. "sword.json")
     * @see AbstractGeckoAPI#getDisplayFileList()
     */
    void setDisplayJSON(String displayFile);

    /**
     * Set the display transform for a specific render context.
     * This overrides any display JSON settings for the specified context and
     * controls how the 3D model is positioned, rotated, and scaled.
     *
     * <p>Valid context values:</p>
     * <ul>
     *   <li>{@code "first_person"} - held item in first-person view</li>
     *   <li>{@code "third_person"} - held item in third-person view</li>
     *   <li>{@code "inventory"} - item icon in inventory GUI</li>
     *   <li>{@code "ground"} - dropped item entity on the ground</li>
     * </ul>
     *
     * @param context the render context name
     * @param tx translation X offset
     * @param ty translation Y offset
     * @param tz translation Z offset
     * @param rx rotation X in degrees
     * @param ry rotation Y in degrees
     * @param rz rotation Z in degrees
     * @param sx scale X multiplier
     * @param sy scale Y multiplier
     * @param sz scale Z multiplier
     * @throws IllegalArgumentException if the context name is not recognized
     */
    void setDisplay(String context,
                    float tx, float ty, float tz,
                    float rx, float ry, float rz,
                    float sx, float sy, float sz);

    /**
     * Remove the gecko model from this item, reverting it to normal 2D rendering.
     * Clears all model, texture, animation, and display settings.
     */
    void clear();
}
