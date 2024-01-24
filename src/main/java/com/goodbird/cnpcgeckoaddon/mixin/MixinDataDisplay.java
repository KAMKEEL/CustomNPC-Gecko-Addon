package com.goodbird.cnpcgeckoaddon.mixin;

import com.goodbird.cnpcgeckoaddon.data.CustomModelData;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
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
public class MixinDataDisplay {

    @Shadow public EntityNPCInterface npc;
    @Unique
    private final CustomModelData customNPC_Gecko_Addon$customModelData = new CustomModelData();

    @Inject(method = "writeToNBT", at = @At("HEAD"))
    public void writeToNBT(NBTTagCompound nbttagcompound, CallbackInfoReturnable<NBTTagCompound> cir) {
        if(customNPC_Gecko_Addon$hasCustomModel())
            customNPC_Gecko_Addon$customModelData.writeToNBT(nbttagcompound);
    }

    @Inject(method = "readToNBT", at = @At("HEAD"))
    public void readFromNBT(NBTTagCompound nbttagcompound, CallbackInfo ci){
        customNPC_Gecko_Addon$customModelData.readFromNBT(nbttagcompound);
    }

    @Unique
    public boolean customNPC_Gecko_Addon$hasCustomModel() {
        return (npc instanceof EntityCustomNpc) && ((EntityCustomNpc) npc).modelData.getEntity(npc) != null &&
            ((EntityCustomNpc) npc).modelData.getEntity(npc) instanceof EntityCustomModel;
    }
}
