package com.goodbird.npcgecko.data;

import net.minecraft.nbt.NBTTagCompound;

public class CustomModelData {
    private String model = "geckolib3:geo/npc.geo.json";
    private String animFile = "custom:geo_npc.animation.json";
    private String idleAnim = "";
    private String walkAnim = "";
    private String meleeAttackAnim = "";
    private String hurtAnim = "";
    private String rangedAttackAnim = "";

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Model", model);
        nbttagcompound.setString("AnimFile", animFile);
        nbttagcompound.setString("IdleAnim", idleAnim);
        nbttagcompound.setString("WalkAnim", walkAnim);
        nbttagcompound.setString("MeleeAttackAnim", meleeAttackAnim);
        nbttagcompound.setString("RangedAttackAnim", rangedAttackAnim);
        nbttagcompound.setString("HurtAnim", hurtAnim);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        if(nbttagcompound.hasKey("Model")) {
            model = nbttagcompound.getString("Model");
            animFile = nbttagcompound.getString("AnimFile");
            idleAnim = nbttagcompound.getString("IdleAnim");
            walkAnim = nbttagcompound.getString("WalkAnim");
            hurtAnim = nbttagcompound.getString("HurtAnim");
            meleeAttackAnim = nbttagcompound.getString("MeleeAttackAnim");
            rangedAttackAnim = nbttagcompound.getString("RangedAttackAnim");
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

    public String getWalkAnim() {
        return walkAnim;
    }

    public void setWalkAnim(String walkAnim) {
        this.walkAnim = walkAnim;
    }

    public String getMeleeAttackAnim() {
        return meleeAttackAnim;
    }

    public void setMeleeAttackAnim(String meleeAttackAnim) {
        this.meleeAttackAnim = meleeAttackAnim;
    }

    public String getHurtAnim() {
        return hurtAnim;
    }

    public void setHurtAnim(String hurtAnim) {
        this.hurtAnim = hurtAnim;
    }

    public String getRangedAttackAnim() {
        return rangedAttackAnim;
    }

    public void setRangedAttackAnim(String rangedAttackAnim) {
        this.rangedAttackAnim = rangedAttackAnim;
    }
}
