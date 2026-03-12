package com.goodbird.npcgecko.api;

/**
 * Interface for querying and modifying bone transforms on a GeckoLib model at runtime.
 * Bones represent individual parts of a model (e.g. "head", "left_arm", "body").
 *
 * <p>Position values are relative offsets from the bone's rest position.
 * Rotation values are in radians.
 * Scale values are multipliers where 1.0 is the default size.</p>
 *
 * @see IGeckoModel#getBone
 */
public interface IGeckoBone {

    /**
     * Get the name of this bone as defined in the model file.
     *
     * @return the bone name
     */
    String getName();

    /**
     * Get the X position offset from the bone's rest position.
     *
     * @return the X position offset
     */
    float getPositionX();

    /**
     * Get the Y position offset from the bone's rest position.
     *
     * @return the Y position offset
     */
    float getPositionY();

    /**
     * Get the Z position offset from the bone's rest position.
     *
     * @return the Z position offset
     */
    float getPositionZ();

    /**
     * Set the X position offset from the bone's rest position.
     *
     * @param value the X position offset
     */
    void setPositionX(float value);

    /**
     * Set the Y position offset from the bone's rest position.
     *
     * @param value the Y position offset
     */
    void setPositionY(float value);

    /**
     * Set the Z position offset from the bone's rest position.
     *
     * @param value the Z position offset
     */
    void setPositionZ(float value);

    /**
     * Get the X rotation in radians.
     *
     * @return the X rotation in radians
     */
    float getRotationX();

    /**
     * Get the Y rotation in radians.
     *
     * @return the Y rotation in radians
     */
    float getRotationY();

    /**
     * Get the Z rotation in radians.
     *
     * @return the Z rotation in radians
     */
    float getRotationZ();

    /**
     * Set the X rotation in radians.
     *
     * @param value the X rotation in radians
     */
    void setRotationX(float value);

    /**
     * Set the Y rotation in radians.
     *
     * @param value the Y rotation in radians
     */
    void setRotationY(float value);

    /**
     * Set the Z rotation in radians.
     *
     * @param value the Z rotation in radians
     */
    void setRotationZ(float value);

    /**
     * Get the X scale multiplier.
     *
     * @return the X scale multiplier (1.0 = default size)
     */
    float getScaleX();

    /**
     * Get the Y scale multiplier.
     *
     * @return the Y scale multiplier (1.0 = default size)
     */
    float getScaleY();

    /**
     * Get the Z scale multiplier.
     *
     * @return the Z scale multiplier (1.0 = default size)
     */
    float getScaleZ();

    /**
     * Set the X scale multiplier.
     *
     * @param value the X scale multiplier (1.0 = default size)
     */
    void setScaleX(float value);

    /**
     * Set the Y scale multiplier.
     *
     * @param value the Y scale multiplier (1.0 = default size)
     */
    void setScaleY(float value);

    /**
     * Set the Z scale multiplier.
     *
     * @param value the Z scale multiplier (1.0 = default size)
     */
    void setScaleZ(float value);

    /**
     * Get the X pivot point of this bone.
     *
     * @return the X pivot point
     */
    float getPivotX();

    /**
     * Get the Y pivot point of this bone.
     *
     * @return the Y pivot point
     */
    float getPivotY();

    /**
     * Get the Z pivot point of this bone.
     *
     * @return the Z pivot point
     */
    float getPivotZ();

    /**
     * Set the X pivot point of this bone.
     *
     * @param value the X pivot point
     */
    void setPivotX(float value);

    /**
     * Set the Y pivot point of this bone.
     *
     * @param value the Y pivot point
     */
    void setPivotY(float value);

    /**
     * Set the Z pivot point of this bone.
     *
     * @param value the Z pivot point
     */
    void setPivotZ(float value);

    /**
     * Check whether this bone is hidden from rendering.
     *
     * @return true if this bone is currently hidden
     */
    boolean isHidden();

    /**
     * Set whether this bone is hidden from rendering.
     * Hidden bones and their children are not drawn.
     *
     * @param hidden true to hide this bone, false to show it
     */
    void setHidden(boolean hidden);

    /**
     * Get the parent bone in the bone hierarchy.
     *
     * @return the parent bone, or null if this is a root bone
     */
    IGeckoBone getParent();

    /**
     * Get the direct child bones of this bone.
     *
     * @return array of child bones, or an empty array if this bone has no children
     */
    IGeckoBone[] getChildBones();
}
