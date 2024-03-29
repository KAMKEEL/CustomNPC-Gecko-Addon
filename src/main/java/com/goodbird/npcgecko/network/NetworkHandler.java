package com.goodbird.npcgecko.network;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public final class NetworkHandler {
    private static final SimpleNetworkWrapper wrapper = new SimpleNetworkWrapper("npcgecko");
    private static Side side = FMLCommonHandler.instance().getSide();

    public static void init() {
        if(side.isServer())
            return;
        wrapper.registerMessage(CPacketSyncAutoAnim.class, CPacketSyncAutoAnim.class, 0, Side.CLIENT);
        wrapper.registerMessage(CPacketSyncManualAnim.class, CPacketSyncManualAnim.class, 1, Side.CLIENT);
    }

    public static void sendToPlayer(IMessage message, EntityPlayer player) {
        if(side.isClient())
            return;
        wrapper.sendTo(message, (EntityPlayerMP) player);
    }

    public static void sendToAll(IMessage message) {
        if(side.isClient())
            return;
        wrapper.sendToAll(message);
    }

    public static void sendToServer(IMessage message) {
        if(side.isServer())
            return;
        wrapper.sendToServer(message);
    }
}
