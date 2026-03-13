package com.goodbird.npcgecko.api;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.entity.EntityNPCInterface;

/**
 * Main entry point for the GeckoLib Addon API.
 * Provides methods for controlling GeckoLib 3D models, animations, textures,
 * and display settings on CustomNPC+ entities, scripted blocks, and items.
 *
 * <p>Access the API singleton via {@code AbstractGeckoAPI.Instance()}.
 * Check availability first with {@code AbstractGeckoAPI.IsAvailable()}.</p>
 *
 * <p>Script usage example:</p>
 * <pre>
 * var gecko = Java.type("com.goodbird.npcgecko.api.AbstractGeckoAPI").Instance();
 * if (gecko != null) {
 *     gecko.setModel(npc, "geckolib3:geo/custom_npc.geo.json");
 *     gecko.setTexture(npc, "geckolib3:textures/model/custom_npc.png");
 * }
 * </pre>
 *
 * @see IGeckoAnimatable
 * @see IGeckoItemModel
 * @see IGeckoAnimationBuilder
 */
public abstract class AbstractGeckoAPI {
    private static AbstractGeckoAPI instance = null;

    /**
     * Check whether the GeckoLib mod is loaded and the API is available.
     *
     * @return true if GeckoLib is loaded
     */
    public static boolean IsAvailable() {
        return Loader.isModLoaded("geckolib3");
    }

    /**
     * Get the API singleton instance.
     * Returns null if GeckoLib is not loaded.
     *
     * @return the API instance, or null if GeckoLib is unavailable
     */
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
     * Alias for {@link #createAnimationBuilder()}.
     *
     * @return a new animation builder instance
     */
    public abstract IGeckoAnimationBuilder createAnimBuilder();

    /**
     * Create a new animation builder for constructing animation sequences.
     *
     * <p>Usage example:</p>
     * <pre>
     * var builder = gecko.createAnimationBuilder();
     * builder.playOnce("animation.model.attack").loop("animation.model.idle");
     * gecko.syncAnimForAll(npc, builder);
     * </pre>
     *
     * @return a new animation builder instance
     */
    public abstract IGeckoAnimationBuilder createAnimationBuilder();

    // ========================================================================
    // Animatable Access
    // ========================================================================

    /**
     * Get the gecko model configuration interface for an NPC.
     * The returned object allows reading and writing model, texture,
     * animation file, and animation state settings.
     *
     * @param npc the NPC to configure
     * @return the animatable interface for this NPC
     * @see IGeckoAnimatable
     */
    public abstract IGeckoAnimatable getAnimatable(ICustomNpc<EntityNPCInterface> npc);

    /**
     * Get the gecko model configuration interface for a scripted block.
     * The returned object allows reading and writing model, texture,
     * animation file, and animation state settings.
     *
     * @param scriptedBlock the scripted block to configure
     * @return the animatable interface for this block
     * @see IGeckoAnimatable
     */
    public abstract IGeckoAnimatable getAnimatable(IBlockScripted scriptedBlock);

    // ========================================================================
    // Animation Queries
    // ========================================================================

    /**
     * Get a list of all animation names defined in an animation file.
     *
     * @param animationFile the animation file resource location
     *        (e.g. "geckolib3:animations/model.animation.json")
     * @return array of animation names, or an empty array if the file was not found
     */
    public abstract String[] getAnimationList(String animationFile);

    /**
     * Get a list of all loaded animation file resource locations.
     *
     * @return array of animation file resource location strings
     */
    public abstract String[] getAnimationFileList();

    /**
     * Get animation metadata from a loaded animation file.
     *
     * @param animationFile the animation file resource location
     * @param animationName the animation name within the file
     * @return the animation metadata, or null if not found
     * @see IGeckoAnimation
     */
    public abstract IGeckoAnimation getAnimation(String animationFile, String animationName);

    // ========================================================================
    // Color Utilities
    // ========================================================================

    /**
     * Create a color from RGB components.
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @return a new color instance
     * @see IGeckoColor
     */
    public abstract IGeckoColor colorOfRGB(int r, int g, int b);

    /**
     * Create a color from RGBA components.
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @param a alpha component (0-255, where 255 is fully opaque)
     * @return a new color instance
     * @see IGeckoColor
     */
    public abstract IGeckoColor colorOfRGBA(int r, int g, int b, int a);

    // ========================================================================
    // NPC Methods
    // ========================================================================

    /**
     * Set the GeckoLib geo model resource location for an NPC.
     *
     * @param npc the target NPC
     * @param model the model resource location (e.g. "geckolib3:geo/npc.geo.json")
     */
    public abstract void setModel(ICustomNpc<EntityNPCInterface> npc, String model);

    /**
     * Set the texture resource location for an NPC.
     *
     * @param npc the target NPC
     * @param texture the texture resource location (e.g. "geckolib3:textures/model/npc.png")
     */
    public abstract void setTexture(ICustomNpc<EntityNPCInterface> npc, String texture);

    /**
     * Set the animation file resource location for an NPC.
     *
     * @param npc the target NPC
     * @param animation the animation file resource location
     *        (e.g. "geckolib3:animations/npc.animation.json")
     */
    public abstract void setAnimationFile(ICustomNpc<EntityNPCInterface> npc, String animation);

    /**
     * Set the idle animation name for an NPC.
     * The animation must exist in the currently assigned animation file.
     *
     * @param npc the target NPC
     * @param animation the animation name (e.g. "animation.npc.idle")
     */
    public abstract void setIdleAnimation(ICustomNpc<EntityNPCInterface> npc, String animation);

    /**
     * Sync a queued animation sequence to a specific player for an NPC.
     * Only the specified player will see the animation.
     *
     * @param npc the NPC to play the animation on
     * @param builder the animation builder containing the queued animation sequence
     * @param player the player to send the animation to
     */
    public abstract void syncAnimForPlayer(ICustomNpc<EntityNPCInterface> npc, IGeckoAnimationBuilder builder, IPlayer<EntityPlayerMP> player);

    /**
     * Sync a queued animation sequence to all connected players for an NPC.
     *
     * @param npc the NPC to play the animation on
     * @param builder the animation builder containing the queued animation sequence
     */
    public abstract void syncAnimForAll(ICustomNpc<EntityNPCInterface> npc, IGeckoAnimationBuilder builder);

    // ========================================================================
    // Block Methods
    // ========================================================================

    /**
     * Set the GeckoLib geo model resource location for a scripted block.
     *
     * @param scriptedBlock the target scripted block
     * @param model the model resource location (e.g. "geckolib3:geo/block.geo.json")
     */
    public abstract void setModel(IBlockScripted scriptedBlock, String model);

    /**
     * Set the texture resource location for a scripted block.
     *
     * @param scriptedBlock the target scripted block
     * @param texture the texture resource location (e.g. "geckolib3:textures/model/block.png")
     */
    public abstract void setTexture(IBlockScripted scriptedBlock, String texture);

    /**
     * Set the animation file resource location for a scripted block.
     *
     * @param scriptedBlock the target scripted block
     * @param animation the animation file resource location
     *        (e.g. "geckolib3:animations/block.animation.json")
     */
    public abstract void setAnimationFile(IBlockScripted scriptedBlock, String animation);

    /**
     * Set the idle animation name for a scripted block.
     * The animation must exist in the currently assigned animation file.
     *
     * @param scriptedBlock the target scripted block
     * @param animation the animation name (e.g. "animation.block.idle")
     */
    public abstract void setIdleAnimation(IBlockScripted scriptedBlock, String animation);

    /**
     * Sync a queued animation sequence to a specific player for a scripted block.
     * Only the specified player will see the animation.
     *
     * @param scriptedBlock the scripted block to play the animation on
     * @param builder the animation builder containing the queued animation sequence
     * @param player the player to send the animation to
     */
    public abstract void syncAnimForPlayer(IBlockScripted scriptedBlock, IGeckoAnimationBuilder builder, IPlayer<EntityPlayerMP> player);

    /**
     * Sync a queued animation sequence to all connected players for a scripted block.
     *
     * @param scriptedBlock the scripted block to play the animation on
     * @param builder the animation builder containing the queued animation sequence
     */
    public abstract void syncAnimForAll(IBlockScripted scriptedBlock, IGeckoAnimationBuilder builder);

    // ========================================================================
    // Item Methods
    // ========================================================================

    /**
     * Get the gecko model interface for a customizable item.
     * Works with both Scripted Items and Linked Items.
     * The returned object allows reading and writing model, texture,
     * animation, and display transform settings.
     *
     * <p>Usage example:</p>
     * <pre>
     * var itemGecko = gecko.getItemGecko(event.item);
     * itemGecko.setModel("geckolib3:geo/sword.geo.json");
     * itemGecko.setTexture("geckolib3:textures/model/sword.png");
     * itemGecko.setAnimationFile("geckolib3:animations/sword.animation.json");
     *
     * var anim = itemGecko.getItemAnimation();
     * anim.setIdleAnimation("animation.sword.idle");
     * anim.setAttackAnimation("animation.sword.attack");
     * </pre>
     *
     * @param item the customizable item (IItemCustom or IItemLinked)
     * @return the gecko model interface for this item
     * @throws IllegalArgumentException if the item is not a Scripted or Linked item
     * @see IGeckoItemModel
     */
    public abstract IGeckoItemModel getItemGecko(IItemCustomizable item);

    /**
     * Get a list of all loaded display file names from item_displays/ resource folders.
     * Display files control how 3D item models are positioned, rotated, and scaled
     * in each render context (first person, third person, inventory, ground).
     *
     * @return array of display file names (e.g. "sword.json", "tool.json")
     */
    public abstract String[] getDisplayFileList();
}
