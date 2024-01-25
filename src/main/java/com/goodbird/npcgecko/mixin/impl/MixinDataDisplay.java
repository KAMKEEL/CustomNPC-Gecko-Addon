package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.data.CustomModelData;
import com.goodbird.npcgecko.entity.EntityCustomModel;
import com.goodbird.npcgecko.mixin.IDataDisplay;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.DataDisplay;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataDisplay.class)
public class MixinDataDisplay implements IDataDisplay {

    @Shadow(remap = false) public EntityNPCInterface npc;
    @Unique
    private final CustomModelData customNPC_Gecko_Addon$customModelData = new CustomModelData();

    @Inject(method = "writeToNBT", at = @At("HEAD"), remap = false)
    public void writeToNBT(NBTTagCompound nbttagcompound, CallbackInfoReturnable<NBTTagCompound> cir) {
        if(hasCustomModel())
            customNPC_Gecko_Addon$customModelData.writeToNBT(nbttagcompound);
    }

    @Inject(method = "readToNBT", at = @At("HEAD"), remap = false)
    public void readFromNBT(NBTTagCompound nbttagcompound, CallbackInfo ci){
        customNPC_Gecko_Addon$customModelData.readFromNBT(nbttagcompound);
    }

    @Unique
    public CustomModelData getCustomModelData(){
        return customNPC_Gecko_Addon$customModelData;
    }

    @Unique
    public boolean hasCustomModel() {
        return (npc instanceof EntityCustomNpc) && ((EntityCustomNpc) npc).modelData.getEntity(npc) != null &&
            ((EntityCustomNpc) npc).modelData.getEntity(npc) instanceof EntityCustomModel;
    }
}
