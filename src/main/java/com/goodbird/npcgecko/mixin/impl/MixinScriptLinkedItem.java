package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.scripted.item.ScriptItemStack;
import noppes.npcs.scripted.item.ScriptLinkedItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScriptLinkedItem.class)
public class MixinScriptLinkedItem implements IScriptCustomItem {
    @Unique
    private CustomItemModelData customNPC_Gecko_Addon$customModelData;

    @Inject(method = "getItemNBT", at = @At("RETURN"), remap = false)
    public void writeGeckoData(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> cir) {
        if (customNPC_Gecko_Addon$customModelData != null) {
            NBTTagCompound modelDataTag = new NBTTagCompound();
            customNPC_Gecko_Addon$customModelData.writeToNBT(modelDataTag);
            compound.setTag("GeckoModelData", modelDataTag);
            // Also persist to top-level key on the ItemStack for direct NBT reads
            ItemStack stack = ((ScriptItemStack) (Object) this).getMCItemStack();
            if (stack != null) {
                NBTTagCompound root = stack.getTagCompound();
                if (root == null) {
                    root = new NBTTagCompound();
                    stack.setTagCompound(root);
                }
                NBTTagCompound topLevelTag = new NBTTagCompound();
                customNPC_Gecko_Addon$customModelData.writeToNBT(topLevelTag);
                root.setTag("GeckoModelData", topLevelTag);
            }
        }
    }

    @Inject(method = "setItemNBT", at = @At("RETURN"), remap = false)
    public void readGeckoData(NBTTagCompound compound, CallbackInfo ci) {
        // Try reading from the ItemData sub-compound (legacy path)
        if (compound.hasKey("GeckoModelData")) {
            customNPC_Gecko_Addon$customModelData = new CustomItemModelData();
            customNPC_Gecko_Addon$customModelData.readFromNBT(compound.getCompoundTag("GeckoModelData"));
            return;
        }
        // Also try the top-level key on the ItemStack (primary path)
        ItemStack stack = ((ScriptItemStack) (Object) this).getMCItemStack();
        if (stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("GeckoModelData")) {
            customNPC_Gecko_Addon$customModelData = new CustomItemModelData();
            customNPC_Gecko_Addon$customModelData.readFromNBT(stack.getTagCompound().getCompoundTag("GeckoModelData"));
        }
    }

    @Unique
    public CustomItemModelData getCustomModelData() {
        if (customNPC_Gecko_Addon$customModelData != null) return customNPC_Gecko_Addon$customModelData;
        // Fallback: load from ItemStack NBT when wrapper was recreated by cache miss
        customNPC_Gecko_Addon$customModelData = customNPC_Gecko_Addon$loadFromNBT();
        return customNPC_Gecko_Addon$customModelData;
    }

    @Override
    public void setCustomModelData(CustomItemModelData data) {
        customNPC_Gecko_Addon$customModelData = data;
    }

    @Unique
    public boolean hasCustomModel() {
        return getCustomModelData() != null;
    }

    @Unique
    private CustomItemModelData customNPC_Gecko_Addon$loadFromNBT() {
        ItemStack stack = ((ScriptItemStack) (Object) this).getMCItemStack();
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound root = stack.getTagCompound();
            // Primary: top-level key (written by GeckoAPI.persistGeckoData)
            if (root.hasKey("GeckoModelData")) {
                CustomItemModelData data = new CustomItemModelData();
                data.readFromNBT(root.getCompoundTag("GeckoModelData"));
                return data;
            }
        }
        return null;
    }
}
