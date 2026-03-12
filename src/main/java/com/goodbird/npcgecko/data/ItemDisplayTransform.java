package com.goodbird.npcgecko.data;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Per-context display transform for gecko item models.
 * Stores translation, rotation, and scale for a single render context
 * (first_person, third_person, inventory, ground).
 */
public class ItemDisplayTransform {
    private float translateX, translateY, translateZ;
    private float rotationX, rotationY, rotationZ;
    private float scaleX = 1.0F, scaleY = 1.0F, scaleZ = 1.0F;
    private boolean hasTranslation;
    private boolean hasRotation;
    private boolean hasScale;

    public ItemDisplayTransform() {
    }

    public ItemDisplayTransform(float tx, float ty, float tz,
                                float rx, float ry, float rz,
                                float sx, float sy, float sz) {
        this.translateX = tx; this.translateY = ty; this.translateZ = tz;
        this.rotationX = rx; this.rotationY = ry; this.rotationZ = rz;
        this.scaleX = sx; this.scaleY = sy; this.scaleZ = sz;
        this.hasTranslation = true;
        this.hasRotation = true;
        this.hasScale = true;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setFloat("TX", translateX);
        tag.setFloat("TY", translateY);
        tag.setFloat("TZ", translateZ);
        tag.setFloat("RX", rotationX);
        tag.setFloat("RY", rotationY);
        tag.setFloat("RZ", rotationZ);
        tag.setFloat("SX", scaleX);
        tag.setFloat("SY", scaleY);
        tag.setFloat("SZ", scaleZ);
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        translateX = tag.getFloat("TX");
        translateY = tag.getFloat("TY");
        translateZ = tag.getFloat("TZ");
        rotationX = tag.getFloat("RX");
        rotationY = tag.getFloat("RY");
        rotationZ = tag.getFloat("RZ");
        scaleX = tag.getFloat("SX");
        scaleY = tag.getFloat("SY");
        scaleZ = tag.getFloat("SZ");
    }

    public float getTranslateX() { return translateX; }
    public float getTranslateY() { return translateY; }
    public float getTranslateZ() { return translateZ; }
    public float getRotationX() { return rotationX; }
    public float getRotationY() { return rotationY; }
    public float getRotationZ() { return rotationZ; }
    public float getScaleX() { return scaleX; }
    public float getScaleY() { return scaleY; }
    public float getScaleZ() { return scaleZ; }

    public void setTranslation(float x, float y, float z) {
        this.translateX = x; this.translateY = y; this.translateZ = z;
        this.hasTranslation = true;
    }

    public void setRotation(float x, float y, float z) {
        this.rotationX = x; this.rotationY = y; this.rotationZ = z;
        this.hasRotation = true;
    }

    public void setScale(float x, float y, float z) {
        this.scaleX = x; this.scaleY = y; this.scaleZ = z;
        this.hasScale = true;
    }

    public boolean hasTranslation() { return hasTranslation; }
    public boolean hasRotation() { return hasRotation; }
    public boolean hasScale() { return hasScale; }
}
