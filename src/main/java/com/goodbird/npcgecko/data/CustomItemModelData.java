package com.goodbird.npcgecko.data;

import net.minecraft.nbt.NBTTagCompound;

public class CustomItemModelData {
    private String model = "geckolib3:geo/sword.geo.json";
    private String animFile = "geckolib3:animations/sword.animation.json";
    private String texture = "geckolib3:textures/model/sword.png";

    // Per-perspective animation names
    private String idleAnimFP = "animation.sword.idle";
    private String idleAnimTP = "animation.sword.idle";
    private String swingAnimFP = "";
    private String swingAnimTP = "";
    private String useAnimFP = "";
    private String useAnimTP = "";

    private int transitionLengthTicks = 0;

    // Display JSON filename from item_displays/ (e.g. "sword.json"). Null = auto-match by model.
    private String displayFile = "";

    // Per-context display transforms (null = use defaults)
    private ItemDisplayTransform firstPerson;
    private ItemDisplayTransform thirdPerson;
    private ItemDisplayTransform inventory;
    private ItemDisplayTransform ground;

    public CustomItemModelData() {
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Model", model);
        nbttagcompound.setString("AnimFile", animFile);
        nbttagcompound.setString("Texture", texture);
        nbttagcompound.setString("IdleAnimFP", idleAnimFP);
        nbttagcompound.setString("IdleAnimTP", idleAnimTP);
        nbttagcompound.setString("SwingAnimFP", swingAnimFP);
        nbttagcompound.setString("SwingAnimTP", swingAnimTP);
        nbttagcompound.setString("UseAnimFP", useAnimFP);
        nbttagcompound.setString("UseAnimTP", useAnimTP);
        nbttagcompound.setInteger("TransitionLengthTicks", transitionLengthTicks);
        if (displayFile != null && !displayFile.isEmpty()) {
            nbttagcompound.setString("DisplayFile", displayFile);
        }

        NBTTagCompound displayTag = new NBTTagCompound();
        if (firstPerson != null) displayTag.setTag("FirstPerson", firstPerson.writeToNBT(new NBTTagCompound()));
        if (thirdPerson != null) displayTag.setTag("ThirdPerson", thirdPerson.writeToNBT(new NBTTagCompound()));
        if (inventory != null) displayTag.setTag("Inventory", inventory.writeToNBT(new NBTTagCompound()));
        if (ground != null) displayTag.setTag("Ground", ground.writeToNBT(new NBTTagCompound()));
        if (!displayTag.hasNoTags()) {
            nbttagcompound.setTag("Display", displayTag);
        }

        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Model")) {
            model = nbt.getString("Model");
            animFile = nbt.getString("AnimFile");
            texture = nbt.getString("Texture");

            // New format: per-perspective keys
            if (nbt.hasKey("IdleAnimFP")) {
                idleAnimFP = nbt.getString("IdleAnimFP");
                idleAnimTP = nbt.getString("IdleAnimTP");
                swingAnimFP = nbt.getString("SwingAnimFP");
                swingAnimTP = nbt.getString("SwingAnimTP");
                useAnimFP = nbt.getString("UseAnimFP");
                useAnimTP = nbt.getString("UseAnimTP");
            } else if (nbt.hasKey("IdleAnim")) {
                // Legacy format: single animation per type -> both perspectives
                String idle = nbt.getString("IdleAnim");
                idleAnimFP = idle;
                idleAnimTP = idle;
                String swing = nbt.hasKey("SwingAnim") ? nbt.getString("SwingAnim") : "";
                swingAnimFP = swing;
                swingAnimTP = swing;
                // Legacy attack maps to swing
                if (swingAnimFP.isEmpty() && nbt.hasKey("AttackAnim")) {
                    String attack = nbt.getString("AttackAnim");
                    swingAnimFP = attack;
                    swingAnimTP = attack;
                }
                String use = nbt.hasKey("UseAnim") ? nbt.getString("UseAnim") : "";
                useAnimFP = use;
                useAnimTP = use;
            }

            if (nbt.hasKey("TransitionLengthTicks")) {
                transitionLengthTicks = nbt.getInteger("TransitionLengthTicks");
            }
            if (nbt.hasKey("DisplayFile")) {
                displayFile = nbt.getString("DisplayFile");
            }
        }

        if (nbt.hasKey("Display")) {
            NBTTagCompound displayTag = nbt.getCompoundTag("Display");
            if (displayTag.hasKey("FirstPerson")) {
                firstPerson = new ItemDisplayTransform();
                firstPerson.readFromNBT(displayTag.getCompoundTag("FirstPerson"));
            }
            if (displayTag.hasKey("ThirdPerson")) {
                thirdPerson = new ItemDisplayTransform();
                thirdPerson.readFromNBT(displayTag.getCompoundTag("ThirdPerson"));
            }
            if (displayTag.hasKey("Inventory")) {
                inventory = new ItemDisplayTransform();
                inventory.readFromNBT(displayTag.getCompoundTag("Inventory"));
            }
            if (displayTag.hasKey("Ground")) {
                ground = new ItemDisplayTransform();
                ground.readFromNBT(displayTag.getCompoundTag("Ground"));
            }
        }
    }

    // --- Display transforms ---

    public ItemDisplayTransform getFirstPerson() { return firstPerson; }
    public void setFirstPerson(ItemDisplayTransform t) { this.firstPerson = t; }

    public ItemDisplayTransform getThirdPerson() { return thirdPerson; }
    public void setThirdPerson(ItemDisplayTransform t) { this.thirdPerson = t; }

    public ItemDisplayTransform getInventory() { return inventory; }
    public void setInventory(ItemDisplayTransform t) { this.inventory = t; }

    public ItemDisplayTransform getGround() { return ground; }
    public void setGround(ItemDisplayTransform t) { this.ground = t; }

    // --- Display file ---

    public String getDisplayFile() { return displayFile; }
    public void setDisplayFile(String displayFile) { this.displayFile = displayFile != null ? displayFile : ""; }

    // --- Model/Anim/Texture ---

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getAnimFile() { return animFile; }
    public void setAnimFile(String animFile) { this.animFile = animFile; }

    public String getTexture() { return texture; }
    public void setTexture(String texture) { this.texture = texture; }

    public int getTransitionLengthTicks() { return transitionLengthTicks; }
    public void setTransitionLengthTicks(int transitionLengthTicks) { this.transitionLengthTicks = transitionLengthTicks; }

    // --- Per-perspective animation getters/setters ---

    public String getIdleAnimFP() { return idleAnimFP; }
    public void setIdleAnimFP(String v) { this.idleAnimFP = v != null ? v : ""; }

    public String getIdleAnimTP() { return idleAnimTP; }
    public void setIdleAnimTP(String v) { this.idleAnimTP = v != null ? v : ""; }

    public String getSwingAnimFP() { return swingAnimFP; }
    public void setSwingAnimFP(String v) { this.swingAnimFP = v != null ? v : ""; }

    public String getSwingAnimTP() { return swingAnimTP; }
    public void setSwingAnimTP(String v) { this.swingAnimTP = v != null ? v : ""; }

    public String getUseAnimFP() { return useAnimFP; }
    public void setUseAnimFP(String v) { this.useAnimFP = v != null ? v : ""; }

    public String getUseAnimTP() { return useAnimTP; }
    public void setUseAnimTP(String v) { this.useAnimTP = v != null ? v : ""; }
}
