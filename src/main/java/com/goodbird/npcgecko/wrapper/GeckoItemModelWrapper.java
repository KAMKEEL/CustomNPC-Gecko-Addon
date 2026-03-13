package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IItemAnimation;
import com.goodbird.npcgecko.api.IGeckoItemModel;
import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.data.ItemDisplayTransform;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.scripted.item.ScriptCustomizableItem;

/**
 * Implementation of {@link IGeckoItemModel} that wraps a customizable item's gecko model data.
 * Handles persistence to both the in-memory wrapper field and the ItemStack's top-level NBT.
 */
public class GeckoItemModelWrapper implements IGeckoItemModel {
    private final IItemCustomizable item;
    private final IScriptCustomItem scriptItem;

    public GeckoItemModelWrapper(IItemCustomizable item) {
        this.item = item;
        this.scriptItem = (IScriptCustomItem) item;
    }

    private CustomItemModelData getOrCreateData() {
        if (!scriptItem.hasCustomModel()) {
            scriptItem.setCustomModelData(new CustomItemModelData());
        }
        return scriptItem.getCustomModelData();
    }

    private void persist() {
        ((ScriptCustomizableItem) item).saveItemData();
        persistToNBT();
    }

    private void persistToNBT() {
        CustomItemModelData data = scriptItem.getCustomModelData();
        ItemStack stack = ((ScriptCustomizableItem) item).getMCItemStack();
        if (data != null && stack != null) {
            NBTTagCompound root = stack.getTagCompound();
            if (root == null) {
                root = new NBTTagCompound();
                stack.setTagCompound(root);
            }
            NBTTagCompound tag = new NBTTagCompound();
            data.writeToNBT(tag);
            root.setTag("GeckoModelData", tag);
        }
    }

    @Override
    public boolean hasModel() {
        return scriptItem.hasCustomModel();
    }

    @Override
    public String getModel() {
        CustomItemModelData data = scriptItem.getCustomModelData();
        return data != null ? data.getModel() : "";
    }

    @Override
    public void setModel(String model) {
        getOrCreateData().setModel(model);
        persist();
    }

    @Override
    public String getTexture() {
        CustomItemModelData data = scriptItem.getCustomModelData();
        return data != null ? data.getTexture() : "";
    }

    @Override
    public void setTexture(String texture) {
        getOrCreateData().setTexture(texture);
        persist();
    }

    @Override
    public String getAnimationFile() {
        CustomItemModelData data = scriptItem.getCustomModelData();
        return data != null ? data.getAnimFile() : "";
    }

    @Override
    public void setAnimationFile(String animationFile) {
        getOrCreateData().setAnimFile(animationFile);
        persist();
    }

    @Override
    public IItemAnimation getItemAnimation() {
        return new ItemAnimationWrapper(item);
    }

    @Override
    public int getTransitionLengthTicks() {
        CustomItemModelData data = scriptItem.getCustomModelData();
        return data != null ? data.getTransitionLengthTicks() : 10;
    }

    @Override
    public void setTransitionLengthTicks(int ticks) {
        getOrCreateData().setTransitionLengthTicks(ticks);
        persist();
    }

    @Override
    public String getDisplayJSON() {
        CustomItemModelData data = scriptItem.getCustomModelData();
        return data != null ? data.getDisplayFile() : "";
    }

    @Override
    public void setDisplayJSON(String displayFile) {
        getOrCreateData().setDisplayFile(displayFile);
        persist();
    }

    @Override
    public void setDisplay(String context,
                           float tx, float ty, float tz,
                           float rx, float ry, float rz,
                           float sx, float sy, float sz) {
        CustomItemModelData data = getOrCreateData();
        ItemDisplayTransform transform = new ItemDisplayTransform(tx, ty, tz, rx, ry, rz, sx, sy, sz);
        switch (context.toLowerCase()) {
            case "first_person": data.setFirstPerson(transform); break;
            case "third_person": data.setThirdPerson(transform); break;
            case "inventory":    data.setInventory(transform); break;
            case "ground":       data.setGround(transform); break;
            default:
                throw new IllegalArgumentException("Unknown display context: " + context
                    + ". Use: first_person, third_person, inventory, ground");
        }
        persist();
    }

    @Override
    public void clear() {
        scriptItem.setCustomModelData(null);
        ItemStack stack = ((ScriptCustomizableItem) item).getMCItemStack();
        if (stack != null && stack.hasTagCompound()) {
            stack.getTagCompound().removeTag("GeckoModelData");
        }
        ((ScriptCustomizableItem) item).saveItemData();
    }
}
