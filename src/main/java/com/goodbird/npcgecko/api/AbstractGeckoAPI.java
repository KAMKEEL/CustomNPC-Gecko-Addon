package com.goodbird.npcgecko.api;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.block.IBlockScripted;
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

    public abstract void setModel(ICustomNpc<EntityNPCInterface> npc, String model);
    public abstract void setTexture(ICustomNpc<EntityNPCInterface> npc, String texture);
    public abstract void setAnimationFile(ICustomNpc<EntityNPCInterface> npc, String animation);
    public abstract void setIdleAnimation(ICustomNpc<EntityNPCInterface> npc, String animation);
    public abstract void syncAnimForPlayer(ICustomNpc<EntityNPCInterface> npc, AnimationBuilder builder, IPlayer<EntityPlayerMP> player);
    public abstract void syncAnimForAll(ICustomNpc<EntityNPCInterface> npc, AnimationBuilder builder);

    public abstract void setModel(IBlockScripted scriptedBlock, String model);
    public abstract void setTexture(IBlockScripted scriptedBlock, String texture);
    public abstract void setAnimationFile(IBlockScripted scriptedBlock, String animation);
    public abstract void setIdleAnimation(IBlockScripted scriptedBlock, String animation);
    public abstract void syncAnimForPlayer(IBlockScripted scriptedBlock, AnimationBuilder builder, IPlayer<EntityPlayerMP> player);
    public abstract void syncAnimForAll(IBlockScripted scriptedBlock, AnimationBuilder builder);
}
