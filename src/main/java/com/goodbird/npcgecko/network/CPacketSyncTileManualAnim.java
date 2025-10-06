package com.goodbird.npcgecko.network;

import com.goodbird.npcgecko.entity.EntityCustomModel;
import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.util.ByteBufUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.Server;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.builder.RawAnimation;

import java.io.IOException;

public class CPacketSyncTileManualAnim implements IMessage, IMessageHandler<CPacketSyncTileManualAnim, IMessage> {
    public AnimationBuilder builder;
    public int x, y, z;
    public CPacketSyncTileManualAnim(){

    }
    public CPacketSyncTileManualAnim(TileEntity tile, AnimationBuilder builder) {
        this.builder = builder;
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            writeAnimBuilder(buf, builder);
        }catch (Exception ignored){ }
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            builder = readAnimBuilder(buf);
        }catch (Exception ignored){ }
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();

    }

    public static void writeAnimBuilder(ByteBuf buffer, AnimationBuilder builder) throws IOException {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList animList = new NBTTagList();
        for(RawAnimation anim: builder.getRawAnimationList()){
            NBTTagCompound animTag = new NBTTagCompound();
            animTag.setString("name", anim.animationName);
            if(anim.loopType!=null) {
                animTag.setInteger("loop", ((ILoopType.EDefaultLoopTypes) anim.loopType).ordinal());
            }else{
                animTag.setInteger("loop",1);
            }
            animList.appendTag(animTag);
        }
        compound.setTag("anims",animList);
        ByteBufUtils.writeNBT(buffer,compound);
    }

    public static AnimationBuilder readAnimBuilder(ByteBuf buffer) throws IOException {
        AnimationBuilder builder = new AnimationBuilder();
        NBTTagCompound compound = ByteBufUtils.readNBT(buffer);
        NBTTagList animList = compound.getTagList("anims",10);
        for(int i=0;i<animList.tagCount();i++){
            NBTTagCompound animTag = animList.getCompoundTagAt(i);
            builder.addAnimation(animTag.getString("name"),
                ILoopType.EDefaultLoopTypes.values()[animTag.getInteger("loop")]);
        }
        return builder;
    }

    @Override
    public IMessage onMessage(CPacketSyncTileManualAnim message, MessageContext ctx) {
        TileEntity entity = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
        if(!(entity instanceof TileScripted)) return null;
        TileScripted tile = (TileScripted) entity;
        if(tile.renderTile==null){
            tile.renderTile = new TileEntityCustomModel();
        }
        TileEntityCustomModel geckoTile = (TileEntityCustomModel) tile.renderTile;
        geckoTile.manualAnim = message.builder;
        return null;
    }
}
