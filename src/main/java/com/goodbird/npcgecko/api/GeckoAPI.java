package com.goodbird.npcgecko.api;

import com.goodbird.npcgecko.network.CPacketSyncManualAnim;
import com.goodbird.npcgecko.network.NetworkHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

public class GeckoAPI extends AbstractGeckoAPI {
    private static AbstractGeckoAPI Instance;

    private GeckoAPI() {}

    public static AbstractGeckoAPI Instance() {
        if (GeckoAPI.Instance == null) {
            Instance = new GeckoAPI();
        }
        return Instance;
    }

    @Override
    public AnimationBuilder createAnimationBuilder() {
        return new AnimationBuilder();
    }

    @Override
    public void syncAnimForPlayer(ICustomNpc<EntityNPCInterface> npc, AnimationBuilder builder, IPlayer<EntityPlayerMP> player) {
        NetworkHandler.sendToPlayer(new CPacketSyncManualAnim(npc.getMCEntity(),builder), player.getMCEntity());
    }

    @Override
    public void syncAnimForAll(ICustomNpc<EntityNPCInterface> npc, AnimationBuilder builder) {
        NetworkHandler.sendToAll(new CPacketSyncManualAnim(npc.getMCEntity(),builder));
    }
}
