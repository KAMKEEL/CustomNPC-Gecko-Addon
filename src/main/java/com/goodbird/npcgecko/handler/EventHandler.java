package com.goodbird.npcgecko.handler;

import com.goodbird.npcgecko.constants.EnumItemAnimType;
import com.goodbird.npcgecko.constants.EnumSyncAutoAnim;
import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import com.goodbird.npcgecko.network.CPacketSyncAutoAnim;
import com.goodbird.npcgecko.network.ItemAnimSyncable;
import com.goodbird.npcgecko.network.NetworkHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.scripted.event.ItemEvent;
import noppes.npcs.scripted.event.NpcEvent;
import software.bernie.geckolib3.network.GeckoLibNetwork;

import java.util.HashMap;
import java.util.Map;

public class EventHandler {

    /**
     * Server-side swing sequence counter per syncId (entityId << 4 | slot).
     * Incremented on each swing so the client can detect new swings
     * even when the tracker already has SWING state.
     */
    private static final Map<Integer, Integer> swingSequences = new HashMap<>();

    private static int nextSwingSequence(int syncId) {
        int seq = swingSequences.getOrDefault(syncId, 0) + 1;
        swingSequences.put(syncId, seq);
        return seq;
    }

    // ========================================================================
    // NPC Events
    // ========================================================================

    @SubscribeEvent
    public void onNpcMeleeAttack(NpcEvent.SwingEvent event) {
        if (event == null || event.npc == null || event.npc.getMCEntity() == null) return;
        EntityNPCInterface npc = (EntityNPCInterface) event.npc.getMCEntity();
        NetworkHandler.sendToAll(new CPacketSyncAutoAnim(npc, EnumSyncAutoAnim.MELEE_ATTACK));
        // Also sync NPC's held item swing animation (if it has a gecko model)
        syncHeldItemAnim(npc, EnumItemAnimType.SWING);
    }

    @SubscribeEvent
    public void onNpcRanged(NpcEvent.RangedLaunchedEvent event) {
        if (event != null && event.npc != null && event.npc.getMCEntity() != null)
            NetworkHandler.sendToAll(new CPacketSyncAutoAnim((EntityNPCInterface) event.npc.getMCEntity(), EnumSyncAutoAnim.RANGED_ATTACK));
    }

    @SubscribeEvent
    public void onNpcDamaged(NpcEvent.DamagedEvent event) {
        if (event != null && event.npc != null && event.npc.getMCEntity() != null)
            NetworkHandler.sendToAll(new CPacketSyncAutoAnim((EntityNPCInterface) event.npc.getMCEntity(), EnumSyncAutoAnim.HURT));
    }

    // ========================================================================
    // Item Events — uses GeckoLib ISyncable/GeckoLibNetwork
    // ========================================================================

    @SubscribeEvent
    public void onItemAttack(ItemEvent.AttackEvent event) {
        if (event == null || event.item == null) return;
        if (!(event.item instanceof IScriptCustomItem)) return;
        CustomItemModelData data = ((IScriptCustomItem) event.item).getCustomModelData();
        if (data == null) return;

        String swingFP = data.getSwingAnimFP();
        String swingTP = data.getSwingAnimTP();
        boolean hasSwing = (swingFP != null && !swingFP.isEmpty()) || (swingTP != null && !swingTP.isEmpty());
        if (hasSwing) {
            syncItemAnim(event.swingingEntity, EnumItemAnimType.SWING);
        }
    }

    @SubscribeEvent
    public void onItemStartUsing(ItemEvent.StartUsingItem event) {
        if (event == null || event.item == null || event.player == null) return;
        if (!(event.item instanceof IScriptCustomItem)) return;
        CustomItemModelData data = ((IScriptCustomItem) event.item).getCustomModelData();
        if (data == null) return;

        String useFP = data.getUseAnimFP();
        String useTP = data.getUseAnimTP();
        boolean hasUse = (useFP != null && !useFP.isEmpty()) || (useTP != null && !useTP.isEmpty());
        if (hasUse) {
            syncItemAnimForPlayer(event.player, EnumItemAnimType.USE_START);
        }
    }

    @SubscribeEvent
    public void onItemStopUsing(ItemEvent.StopUsingItem event) {
        if (event == null || event.player == null) return;
        syncItemAnimForPlayer(event.player, EnumItemAnimType.USE_STOP);
    }

    @SubscribeEvent
    public void onItemFinishUsing(ItemEvent.FinishUsingItem event) {
        if (event == null || event.player == null) return;
        syncItemAnimForPlayer(event.player, EnumItemAnimType.USE_STOP);
    }

    // ========================================================================
    // Helpers — use GeckoLibNetwork.syncAnimationToAll via ItemAnimSyncable
    // ========================================================================

    /**
     * Syncs item animation for a scripted item event entity (player or NPC).
     */
    private void syncItemAnim(noppes.npcs.api.entity.IEntity entity, EnumItemAnimType type) {
        if (entity == null) return;
        net.minecraft.entity.Entity mcEntity = entity.getMCEntity();
        if (mcEntity instanceof EntityPlayerMP) {
            doSyncPlayer((EntityPlayerMP) mcEntity, type);
        } else if (mcEntity instanceof EntityLivingBase) {
            syncHeldItemAnim((EntityLivingBase) mcEntity, type);
        }
    }

    @SuppressWarnings("unchecked")
    private void syncItemAnimForPlayer(IPlayer<?> iPlayer, EnumItemAnimType type) {
        if (iPlayer == null) return;
        EntityPlayer mcPlayer = (EntityPlayer) iPlayer.getMCEntity();
        if (!(mcPlayer instanceof EntityPlayerMP)) return;
        doSyncPlayer((EntityPlayerMP) mcPlayer, type);
    }

    /**
     * Syncs a player's held item animation using their current hotbar slot.
     */
    private void doSyncPlayer(EntityPlayerMP player, EnumItemAnimType type) {
        int slot = player.inventory.currentItem;
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack == null) return;

        int syncId = (player.getEntityId() << 4) | (slot & 0xF);
        int sequence = (type == EnumItemAnimType.SWING) ? nextSwingSequence(syncId) : 0;
        int state = ItemAnimSyncable.encodeState(type, sequence);

        GeckoLibNetwork.syncAnimationToAll(ItemAnimSyncable.INSTANCE, syncId, state);
    }

    /**
     * Syncs a non-player entity's held item animation (e.g., NPC).
     * Uses slot 0 since non-player entities don't have hotbar slots.
     */
    private void syncHeldItemAnim(EntityLivingBase entity, EnumItemAnimType type) {
        ItemStack heldItem = entity.getHeldItem();
        if (heldItem == null || !heldItem.hasTagCompound()) return;
        if (!heldItem.getTagCompound().hasKey("GeckoModelData")) return;

        int syncId = (entity.getEntityId() << 4) | 0;
        int sequence = (type == EnumItemAnimType.SWING) ? nextSwingSequence(syncId) : 0;
        int state = ItemAnimSyncable.encodeState(type, sequence);

        GeckoLibNetwork.syncAnimationToAll(ItemAnimSyncable.INSTANCE, syncId, state);
    }
}
