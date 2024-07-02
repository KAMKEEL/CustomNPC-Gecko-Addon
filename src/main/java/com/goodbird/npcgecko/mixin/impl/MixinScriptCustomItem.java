package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.scripted.item.ScriptCustomItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScriptCustomItem.class)
public class MixinScriptCustomItem implements IScriptCustomItem {
    @Unique
    private CustomItemModelData customNPC_Gecko_Addon$customModelData;

    @Inject(method = "getItemNBT", at = @At("HEAD"), remap = false)
    public void writeToNBT(NBTTagCompound nbttagcompound, CallbackInfoReturnable<NBTTagCompound> cir) {
        if(customNPC_Gecko_Addon$customModelData!=null){
            NBTTagCompound modelDataTag = new NBTTagCompound();
            customNPC_Gecko_Addon$customModelData.writeToNBT(modelDataTag);
            nbttagcompound.setTag("GeckoModelData", modelDataTag);
        }
    }

    @Inject(method = "setItemNBT", at = @At("HEAD"), remap = false)
    public void readFromNBT(NBTTagCompound nbttagcompound, CallbackInfo ci){
        if(nbttagcompound.hasKey("GeckoModelData")){
            customNPC_Gecko_Addon$customModelData = new CustomItemModelData();
            customNPC_Gecko_Addon$customModelData.readFromNBT(nbttagcompound.getCompoundTag("GeckoModelData"));
        }
    }

    @Unique
    public CustomItemModelData getCustomModelData(){
        return customNPC_Gecko_Addon$customModelData;
    }

    @Override
    public void setCustomModelData(CustomItemModelData data) {
        customNPC_Gecko_Addon$customModelData = data;
    }

    @Unique
    public boolean hasCustomModel() {
        return customNPC_Gecko_Addon$customModelData!=null;
    }
}
