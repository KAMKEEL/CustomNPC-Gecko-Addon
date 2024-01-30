package com.goodbird.npcgecko.api;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

public abstract class AbstractGeckoAPI {
    private static AbstractGeckoAPI instance = null;

    public static boolean IsAvailable() {
        return Loader.isModLoaded("geckolib3");
    }

    public static AbstractGeckoAPI Instance() {
        if (instance != null) {
            return instance;
        } else if (!IsAvailable()) {
            return null;
        } else {
            try {
                Class<?> c = Class.forName("com.goodbird.npcgecko.api.GeckoAPI");
                instance = (AbstractGeckoAPI) c.getMethod("Instance").invoke(null);
            } catch (Exception ignored) {

            }
            return instance;
        }
    }

    public abstract AnimationBuilder createAnimationBuilder();

    public abstract void syncAnimForPlayer(ICustomNpc<EntityNPCInterface> npc, AnimationBuilder builder, IPlayer<EntityPlayerMP> player);
    public abstract void syncAnimForAll(ICustomNpc<EntityNPCInterface> npc, AnimationBuilder builder);
}
