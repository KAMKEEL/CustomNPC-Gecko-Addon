package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IGeckoBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import java.util.List;

/**
 * Wrapper around GeckoLib's {@link GeoBone} that implements {@link IGeckoBone}.
 */
public class GeckoBoneWrapper implements IGeckoBone {
    private final GeoBone bone;

    public GeckoBoneWrapper(GeoBone bone) {
        this.bone = bone;
    }

    @Override
    public String getName() {
        return bone.name;
    }

    @Override
    public float getPositionX() {
        return bone.getPositionX();
    }

    @Override
    public float getPositionY() {
        return bone.getPositionY();
    }

    @Override
    public float getPositionZ() {
        return bone.getPositionZ();
    }

    @Override
    public void setPositionX(float value) {
        bone.setPositionX(value);
    }

    @Override
    public void setPositionY(float value) {
        bone.setPositionY(value);
    }

    @Override
    public void setPositionZ(float value) {
        bone.setPositionZ(value);
    }

    @Override
    public float getRotationX() {
        return bone.getRotationX();
    }

    @Override
    public float getRotationY() {
        return bone.getRotationY();
    }

    @Override
    public float getRotationZ() {
        return bone.getRotationZ();
    }

    @Override
    public void setRotationX(float value) {
        bone.setRotationX(value);
    }

    @Override
    public void setRotationY(float value) {
        bone.setRotationY(value);
    }

    @Override
    public void setRotationZ(float value) {
        bone.setRotationZ(value);
    }

    @Override
    public float getScaleX() {
        return bone.getScaleX();
    }

    @Override
    public float getScaleY() {
        return bone.getScaleY();
    }

    @Override
    public float getScaleZ() {
        return bone.getScaleZ();
    }

    @Override
    public void setScaleX(float value) {
        bone.setScaleX(value);
    }

    @Override
    public void setScaleY(float value) {
        bone.setScaleY(value);
    }

    @Override
    public void setScaleZ(float value) {
        bone.setScaleZ(value);
    }

    @Override
    public float getPivotX() {
        return bone.getPivotX();
    }

    @Override
    public float getPivotY() {
        return bone.getPivotY();
    }

    @Override
    public float getPivotZ() {
        return bone.getPivotZ();
    }

    @Override
    public void setPivotX(float value) {
        bone.setPivotX(value);
    }

    @Override
    public void setPivotY(float value) {
        bone.setPivotY(value);
    }

    @Override
    public void setPivotZ(float value) {
        bone.setPivotZ(value);
    }

    @Override
    public boolean isHidden() {
        return bone.isHidden();
    }

    @Override
    public void setHidden(boolean hidden) {
        bone.setHidden(hidden);
    }

    @Override
    public IGeckoBone getParent() {
        if (bone.parent == null) {
            return null;
        }
        return new GeckoBoneWrapper(bone.parent);
    }

    @Override
    public IGeckoBone[] getChildBones() {
        List<GeoBone> children = bone.childBones;
        IGeckoBone[] result = new IGeckoBone[children.size()];
        for (int i = 0; i < children.size(); i++) {
            result[i] = new GeckoBoneWrapper(children.get(i));
        }
        return result;
    }
}
