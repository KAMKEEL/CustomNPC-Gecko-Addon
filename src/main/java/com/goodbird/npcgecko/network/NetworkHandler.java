package com.goodbird.npcgecko.network;


import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public final class NetworkHandler {
    private static final SimpleNetworkWrapper wrapper = new SimpleNetworkWrapper("npcgecko");

    public static void init() {
        wrapper.registerMessage(CPacketSyncAutoAnim.class, CPacketSyncAutoAnim.class, 0, Side.CLIENT);
    }

    public static void sendToPlayer(IMessage message, EntityPlayer player) {
        wrapper.sendTo(message, (EntityPlayerMP) player);
    }

    public static void sendToAll(IMessage message) {
        wrapper.sendToAll(message);
    }

    public static void sendToServer(IMessage message) {
        wrapper.sendToServer(message);
    }
}
