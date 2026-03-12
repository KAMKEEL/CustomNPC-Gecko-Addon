package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.nbt.NBTTagCompound;
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
        }
    }

    @Inject(method = "setItemNBT", at = @At("RETURN"), remap = false)
    public void readGeckoData(NBTTagCompound compound, CallbackInfo ci) {
        if (compound.hasKey("GeckoModelData")) {
            customNPC_Gecko_Addon$customModelData = new CustomItemModelData();
            customNPC_Gecko_Addon$customModelData.readFromNBT(compound.getCompoundTag("GeckoModelData"));
        }
    }

    @Unique
    public CustomItemModelData getCustomModelData() {
        return customNPC_Gecko_Addon$customModelData;
    }

    @Override
    public void setCustomModelData(CustomItemModelData data) {
        customNPC_Gecko_Addon$customModelData = data;
    }

    @Unique
    public boolean hasCustomModel() {
        return customNPC_Gecko_Addon$customModelData != null;
    }
}
