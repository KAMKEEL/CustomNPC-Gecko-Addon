package com.goodbird.npcgecko.data;

import net.minecraft.nbt.NBTTagCompound;

public class CustomItemModelData {
    private String model = "geckolib3:geo/sword.geo.json";
    private String animFile = "geckolib3:animations/sword.animation.json";
    private String texture = "geckolib3:textures/model/sword.png";
    private String idleAnim = "animation.sword.idle";
    private int transitionLengthTicks = 10;

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
        nbttagcompound.setString("IdleAnim", idleAnim);
        nbttagcompound.setString("Texture", texture);
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("Model")) {
            model = nbttagcompound.getString("Model");
            animFile = nbttagcompound.getString("AnimFile");
            idleAnim = nbttagcompound.getString("IdleAnim");
            texture = nbttagcompound.getString("Texture");
            if (nbttagcompound.hasKey("TransitionLengthTicks")) {
                transitionLengthTicks = nbttagcompound.getInteger("TransitionLengthTicks");
            }
            if (nbttagcompound.hasKey("DisplayFile")) {
                displayFile = nbttagcompound.getString("DisplayFile");
            }
        }

        if (nbttagcompound.hasKey("Display")) {
            NBTTagCompound displayTag = nbttagcompound.getCompoundTag("Display");
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

    // --- Existing getters/setters ---

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getAnimFile() { return animFile; }
    public void setAnimFile(String animFile) { this.animFile = animFile; }

    public String getIdleAnim() { return idleAnim; }
    public void setIdleAnim(String idleAnim) { this.idleAnim = idleAnim; }

    public int getTransitionLengthTicks() { return transitionLengthTicks; }
    public void setTransitionLengthTicks(int transitionLengthTicks) { this.transitionLengthTicks = transitionLengthTicks; }

    public String getTexture() { return texture; }
    public void setTexture(String texture) { this.texture = texture; }
}
