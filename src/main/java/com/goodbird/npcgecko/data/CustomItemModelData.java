package com.goodbird.npcgecko.data;

import net.minecraft.nbt.NBTTagCompound;

public class CustomItemModelData {
    private String model = "geckolib3:geo/npc.geo.json";
    private String animFile = "custom:geo_npc.animation.json";
    private String texture = "geckolib3:textures/item/jack.png";
    private String idleAnim = "";
    private int transitionLengthTicks = 10;

    public CustomItemModelData() {
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Model", model);
        nbttagcompound.setString("AnimFile", animFile);
        nbttagcompound.setString("IdleAnim", idleAnim);
        nbttagcompound.setString("Texture", texture);
        nbttagcompound.setInteger("TransitionLengthTicks", transitionLengthTicks);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        if(nbttagcompound.hasKey("Model")) {
            model = nbttagcompound.getString("Model");
            animFile = nbttagcompound.getString("AnimFile");
            idleAnim = nbttagcompound.getString("IdleAnim");
            texture = nbttagcompound.getString("Texture");
            if(nbttagcompound.hasKey("TransitionLengthTicks")){
                transitionLengthTicks = nbttagcompound.getInteger("TransitionLengthTicks");
            }
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAnimFile() {
        return animFile;
    }

    public void setAnimFile(String animFile) {
        this.animFile = animFile;
    }

    public String getIdleAnim() {
        return idleAnim;
    }

    public void setIdleAnim(String idleAnim) {
        this.idleAnim = idleAnim;
    }

    public int getTransitionLengthTicks() {
        return transitionLengthTicks;
    }

    public void setTransitionLengthTicks(int transitionLengthTicks) {
        this.transitionLengthTicks = transitionLengthTicks;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
