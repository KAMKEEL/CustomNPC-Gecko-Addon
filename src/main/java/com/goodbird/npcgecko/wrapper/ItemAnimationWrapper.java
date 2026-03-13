package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IItemAnimation;
import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.scripted.item.ScriptCustomizableItem;

public class ItemAnimationWrapper implements IItemAnimation {
    private final IItemCustomizable item;
    private final IScriptCustomItem scriptItem;

    public ItemAnimationWrapper(IItemCustomizable item) {
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
        CustomItemModelData data = scriptItem.getCustomModelData();
        ItemStack stack = ((ScriptCustomizableItem) item).getMCItemStack();
        if (data != null && stack != null) {
            NBTTagCompound root = stack.getTagCompound();
            if (root == null) { root = new NBTTagCompound(); stack.setTagCompound(root); }
            NBTTagCompound tag = new NBTTagCompound();
            data.writeToNBT(tag);
            root.setTag("GeckoModelData", tag);
        }
    }

    // --- Idle ---

    @Override
    public String getIdleAnimation() { return getIdleAnimation(0); }

    @Override
    public String getIdleAnimation(int perspective) {
        CustomItemModelData data = scriptItem.getCustomModelData();
        if (data == null) return "";
        return perspective == 0 ? data.getIdleAnimFP() : data.getIdleAnimTP();
    }

    @Override
    public void setIdleAnimation(String animation) {
        CustomItemModelData data = getOrCreateData();
        String v = animation != null ? animation : "";
        data.setIdleAnimFP(v);
        data.setIdleAnimTP(v);
        persist();
    }

    @Override
    public void setIdleAnimation(String animation, int perspective) {
        CustomItemModelData data = getOrCreateData();
        String v = animation != null ? animation : "";
        if (perspective == 0) data.setIdleAnimFP(v);
        else data.setIdleAnimTP(v);
        persist();
    }

    // --- Swing ---

    @Override
    public String getSwingAnimation() { return getSwingAnimation(0); }

    @Override
    public String getSwingAnimation(int perspective) {
        CustomItemModelData data = scriptItem.getCustomModelData();
        if (data == null) return "";
        return perspective == 0 ? data.getSwingAnimFP() : data.getSwingAnimTP();
    }

    @Override
    public void setSwingAnimation(String animation) {
        CustomItemModelData data = getOrCreateData();
        String v = animation != null ? animation : "";
        data.setSwingAnimFP(v);
        data.setSwingAnimTP(v);
        persist();
    }

    @Override
    public void setSwingAnimation(String animation, int perspective) {
        CustomItemModelData data = getOrCreateData();
        String v = animation != null ? animation : "";
        if (perspective == 0) data.setSwingAnimFP(v);
        else data.setSwingAnimTP(v);
        persist();
    }

    // --- Use ---

    @Override
    public String getUseAnimation() { return getUseAnimation(0); }

    @Override
    public String getUseAnimation(int perspective) {
        CustomItemModelData data = scriptItem.getCustomModelData();
        if (data == null) return "";
        return perspective == 0 ? data.getUseAnimFP() : data.getUseAnimTP();
    }

    @Override
    public void setUseAnimation(String animation) {
        CustomItemModelData data = getOrCreateData();
        String v = animation != null ? animation : "";
        data.setUseAnimFP(v);
        data.setUseAnimTP(v);
        persist();
    }

    @Override
    public void setUseAnimation(String animation, int perspective) {
        CustomItemModelData data = getOrCreateData();
        String v = animation != null ? animation : "";
        if (perspective == 0) data.setUseAnimFP(v);
        else data.setUseAnimTP(v);
        persist();
    }
}
