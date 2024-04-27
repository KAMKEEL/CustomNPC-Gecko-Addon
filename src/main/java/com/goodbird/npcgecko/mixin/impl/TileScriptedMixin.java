package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.blocks.tiles.TileScripted;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileScripted.class)
public class TileScriptedMixin {

    @Shadow(remap = false)
    public TileEntity renderTile;

    @Inject(method = "setDisplayNBT", at = @At("TAIL"), remap = false)
    public void setDisplayNBT(NBTTagCompound compound, CallbackInfo ci) {
        if(compound.hasKey("renderTileTag")){
            renderTile = new TileEntityCustomModel();
            NBTTagCompound saveTag = compound.getCompoundTag("renderTileTag");
            if(MinecraftServer.getServer()!=null && saveTag.hasKey("dimID")){
                World world = MinecraftServer.getServer().worldServers[saveTag.getInteger("dimID")];
                renderTile.setWorldObj(world);
            }
            renderTile.readFromNBT(saveTag);
        }
    }

    @Inject(method = "getDisplayNBT", at = @At("TAIL"), remap = false)
    public void getDisplayNBT(NBTTagCompound compound, CallbackInfo ci) {
        if(renderTile!=null) {
            NBTTagCompound saveTag = new NBTTagCompound();
            renderTile.writeToNBT(saveTag);
            if(renderTile.getWorldObj()!=null && renderTile.getWorldObj().provider!=null)
                saveTag.setInteger("dimID", renderTile.getWorldObj().provider.dimensionId);
            compound.setTag("renderTileTag", saveTag);
        }
    }
}
