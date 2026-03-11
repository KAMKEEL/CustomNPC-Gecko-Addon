package com.goodbird.npcgecko.api;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.entity.EntityNPCInterface;

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

    // ========================================================================
    // Animation Builder
    // ========================================================================

    /**
     * Create a new animation builder for constructing animation sequences.
     *
     * @return A new animation builder
     */
    public abstract IGeckoAnimationBuilder createAnimBuilder();

    /**
     * Create a new animation builder for constructing animation sequences.
     *
     * @return A new animation builder
     */
    public abstract IGeckoAnimationBuilder createAnimationBuilder();

    // ========================================================================
    // Animatable Access
    // ========================================================================

    /**
     * Get the animation configuration interface for an NPC.
     *
     * @param npc The NPC to get animation config for
     * @return The animatable interface for this NPC
     */
    public abstract IGeckoAnimatable getAnimatable(ICustomNpc<EntityNPCInterface> npc);

    /**
     * Get the animation configuration interface for a scripted block.
     *
     * @param scriptedBlock The block to get animation config for
     * @return The animatable interface for this block
     */
    public abstract IGeckoAnimatable getAnimatable(IBlockScripted scriptedBlock);

    // ========================================================================
    // Animation Queries
    // ========================================================================

    /**
     * Get a list of all animation names in an animation file.
     *
     * @param animationFile The animation file resource location (e.g. "npcgecko:animations/geo_npc.animation.json")
     * @return Array of animation names, or empty array if file not found
     */
    public abstract String[] getAnimationList(String animationFile);

    /**
     * Get a list of all loaded animation file resource locations.
     *
     * @return Array of animation file paths
     */
    public abstract String[] getAnimationFileList();

    /**
     * Get animation metadata from a loaded animation file.
     *
     * @param animationFile The animation file resource location
     * @param animationName The animation name within the file
     * @return The animation metadata, or null if not found
     */
    public abstract IGeckoAnimation getAnimation(String animationFile, String animationName);

    // ========================================================================
    // Color Utilities
    // ========================================================================

    /**
     * Create a color from RGB components.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return A new color
     */
    public abstract IGeckoColor colorOfRGB(int r, int g, int b);

    /**
     * Create a color from RGBA components.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @param a Alpha component (0-255)
     * @return A new color
     */
    public abstract IGeckoColor colorOfRGBA(int r, int g, int b, int a);

    // ========================================================================
    // NPC Methods
    // ========================================================================

    /**
     * Set the geo model resource location for an NPC.
     *
     * @param npc The target NPC
     * @param model The model path (e.g. "geckolib3:geo/npc.geo.json")
     */
    public abstract void setModel(ICustomNpc<EntityNPCInterface> npc, String model);

    /**
     * Set the texture resource location for an NPC.
     *
     * @param npc The target NPC
     * @param texture The texture path
     */
    public abstract void setTexture(ICustomNpc<EntityNPCInterface> npc, String texture);

    /**
     * Set the animation file resource location for an NPC.
     *
     * @param npc The target NPC
     * @param animation The animation file path
     */
    public abstract void setAnimationFile(ICustomNpc<EntityNPCInterface> npc, String animation);

    /**
     * Set the idle animation name for an NPC.
     *
     * @param npc The target NPC
     * @param animation The animation name from the animation file
     */
    public abstract void setIdleAnimation(ICustomNpc<EntityNPCInterface> npc, String animation);

    /**
     * Sync an animation to a specific player for an NPC.
     *
     * @param npc The NPC to play the animation on
     * @param builder The animation builder with queued animations
     * @param player The player to send the animation to
     */
    public abstract void syncAnimForPlayer(ICustomNpc<EntityNPCInterface> npc, IGeckoAnimationBuilder builder, IPlayer<EntityPlayerMP> player);

    /**
     * Sync an animation to all players for an NPC.
     *
     * @param npc The NPC to play the animation on
     * @param builder The animation builder with queued animations
     */
    public abstract void syncAnimForAll(ICustomNpc<EntityNPCInterface> npc, IGeckoAnimationBuilder builder);

    // ========================================================================
    // Block Methods
    // ========================================================================

    /**
     * Set the geo model resource location for a scripted block.
     *
     * @param scriptedBlock The target block
     * @param model The model path
     */
    public abstract void setModel(IBlockScripted scriptedBlock, String model);

    /**
     * Set the texture resource location for a scripted block.
     *
     * @param scriptedBlock The target block
     * @param texture The texture path
     */
    public abstract void setTexture(IBlockScripted scriptedBlock, String texture);

    /**
     * Set the animation file resource location for a scripted block.
     *
     * @param scriptedBlock The target block
     * @param animation The animation file path
     */
    public abstract void setAnimationFile(IBlockScripted scriptedBlock, String animation);

    /**
     * Set the idle animation name for a scripted block.
     *
     * @param scriptedBlock The target block
     * @param animation The animation name from the animation file
     */
    public abstract void setIdleAnimation(IBlockScripted scriptedBlock, String animation);

    /**
     * Sync an animation to a specific player for a scripted block.
     *
     * @param scriptedBlock The block to play the animation on
     * @param builder The animation builder with queued animations
     * @param player The player to send the animation to
     */
    public abstract void syncAnimForPlayer(IBlockScripted scriptedBlock, IGeckoAnimationBuilder builder, IPlayer<EntityPlayerMP> player);

    /**
     * Sync an animation to all players for a scripted block.
     *
     * @param scriptedBlock The block to play the animation on
     * @param builder The animation builder with queued animations
     */
    public abstract void syncAnimForAll(IBlockScripted scriptedBlock, IGeckoAnimationBuilder builder);

    // TODO: Fix ScriptCustomItem

//    public abstract void setModel(ScriptCustomItem item, String model);
//    public abstract void setTexture(ScriptCustomItem item, String texture);
//    public abstract void setAnimationFile(ScriptCustomItem item, String animation);
//    public abstract void setIdleAnimation(ScriptCustomItem item, String animation);
}
