package com.goodbird.npcgecko.handler;

import com.goodbird.npcgecko.constants.EnumSyncAutoAnim;
import com.goodbird.npcgecko.network.CPacketSyncAutoAnim;
import com.goodbird.npcgecko.network.NetworkHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.scripted.event.NpcEvent;

public class EventHandler {
    @SubscribeEvent
    public void onNpcMeleeAttack(NpcEvent.MeleeAttackEvent event){
        NetworkHandler.sendToAll(new CPacketSyncAutoAnim((EntityNPCInterface) event.npc.getMCEntity(), EnumSyncAutoAnim.MELEE_ATTACK));
    }

    @SubscribeEvent
    public void onNpcRanged(NpcEvent.RangedLaunchedEvent event){
        NetworkHandler.sendToAll(new CPacketSyncAutoAnim((EntityNPCInterface) event.npc.getMCEntity(), EnumSyncAutoAnim.RANGED_ATTACK));
    }

    @SubscribeEvent
    public void onNpcDamaged(NpcEvent.DamagedEvent event){
        NetworkHandler.sendToAll(new CPacketSyncAutoAnim((EntityNPCInterface) event.npc.getMCEntity(), EnumSyncAutoAnim.HURT));
    }
}
