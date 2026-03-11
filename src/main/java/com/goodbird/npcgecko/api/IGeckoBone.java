package com.goodbird.npcgecko.api;

/**
 * Interface for querying and modifying bone transforms on a Gecko model at runtime.
 * Bones represent individual parts of a model (e.g. "head", "left_arm", "held_item").
 *
 * <p>Position values are relative offsets from the bone's rest position.
 * Rotation values are in radians. Scale values are multipliers (1.0 = default).</p>
 */
public interface IGeckoBone {

    /**
     * Get the name of this bone as defined in the model file.
     *
     * @return The bone name
     */
    String getName();

    /**
     * @return The X position offset
     */
    float getPositionX();

    /**
     * @return The Y position offset
     */
    float getPositionY();

    /**
     * @return The Z position offset
     */
    float getPositionZ();

    /**
     * Set the X position offset.
     *
     * @param value The X position offset
     */
    void setPositionX(float value);

    /**
     * Set the Y position offset.
     *
     * @param value The Y position offset
     */
    void setPositionY(float value);

    /**
     * Set the Z position offset.
     *
     * @param value The Z position offset
     */
    void setPositionZ(float value);

    /**
     * @return The X rotation in radians
     */
    float getRotationX();

    /**
     * @return The Y rotation in radians
     */
    float getRotationY();

    /**
     * @return The Z rotation in radians
     */
    float getRotationZ();

    /**
     * Set the X rotation in radians.
     *
     * @param value The X rotation in radians
     */
    void setRotationX(float value);

    /**
     * Set the Y rotation in radians.
     *
     * @param value The Y rotation in radians
     */
    void setRotationY(float value);

    /**
     * Set the Z rotation in radians.
     *
     * @param value The Z rotation in radians
     */
    void setRotationZ(float value);

    /**
     * @return The X scale multiplier
     */
    float getScaleX();

    /**
     * @return The Y scale multiplier
     */
    float getScaleY();

    /**
     * @return The Z scale multiplier
     */
    float getScaleZ();

    /**
     * Set the X scale multiplier.
     *
     * @param value The X scale multiplier (1.0 = default)
     */
    void setScaleX(float value);

    /**
     * Set the Y scale multiplier.
     *
     * @param value The Y scale multiplier (1.0 = default)
     */
    void setScaleY(float value);

    /**
     * Set the Z scale multiplier.
     *
     * @param value The Z scale multiplier (1.0 = default)
     */
    void setScaleZ(float value);

    /**
     * @return The X pivot point
     */
    float getPivotX();

    /**
     * @return The Y pivot point
     */
    float getPivotY();

    /**
     * @return The Z pivot point
     */
    float getPivotZ();

    /**
     * Set the X pivot point.
     *
     * @param value The X pivot point
     */
    void setPivotX(float value);

    /**
     * Set the Y pivot point.
     *
     * @param value The Y pivot point
     */
    void setPivotY(float value);

    /**
     * Set the Z pivot point.
     *
     * @param value The Z pivot point
     */
    void setPivotZ(float value);

    /**
     * @return Whether this bone is hidden from rendering
     */
    boolean isHidden();

    /**
     * Set whether this bone is hidden from rendering.
     *
     * @param hidden True to hide this bone
     */
    void setHidden(boolean hidden);

    /**
     * Get the parent bone, or null if this is a root bone.
     *
     * @return The parent bone, or null
     */
    IGeckoBone getParent();

    /**
     * Get the child bones of this bone.
     *
     * @return Array of child bones
     */
    IGeckoBone[] getChildBones();
}
