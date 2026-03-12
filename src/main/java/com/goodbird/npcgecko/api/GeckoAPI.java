package com.goodbird.npcgecko.api;

import software.bernie.geckolib3.resource.ItemDisplayLibrary;
import com.goodbird.npcgecko.wrapper.*;
import com.goodbird.npcgecko.mixin.IDataDisplay;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import com.goodbird.npcgecko.network.CPacketSyncManualAnim;
import com.goodbird.npcgecko.network.CPacketSyncTileManualAnim;
import com.goodbird.npcgecko.network.NetworkHandler;
import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.resource.GeckoLibCache;

/**
 * Implementation of {@link AbstractGeckoAPI} that provides the concrete GeckoLib integration.
 * This class is loaded reflectively when GeckoLib is present and should not be
 * referenced directly. Use {@link AbstractGeckoAPI#Instance()} to obtain the API.
 */
public class GeckoAPI extends AbstractGeckoAPI {
    private static AbstractGeckoAPI Instance;

    private GeckoAPI() {
    }

    public static AbstractGeckoAPI Instance() {
        if (GeckoAPI.Instance == null) {
            Instance = new GeckoAPI();
        }
        return Instance;
    }

    // ========================================================================
    // Internal helper - unwrap IGeckoAnimationBuilder to GeckoLib AnimationBuilder
    // ========================================================================

    private static AnimationBuilder unwrap(IGeckoAnimationBuilder builder) {
        return ((GeckoAnimationBuilderWrapper) builder).getBuilder();
    }

    // ========================================================================
    // Animation Builder
    // ========================================================================

    @Override
    public IGeckoAnimationBuilder createAnimBuilder() {
        return new GeckoAnimationBuilderWrapper(new AnimationBuilder());
    }

    @Override
    public IGeckoAnimationBuilder createAnimationBuilder() {
        return createAnimBuilder();
    }

    // ========================================================================
    // Animatable Access
    // ========================================================================

    @Override
    public IGeckoAnimatable getAnimatable(ICustomNpc<EntityNPCInterface> npc) {
        return new GeckoNpcAnimatable(npc);
    }

    @Override
    public IGeckoAnimatable getAnimatable(IBlockScripted scriptedBlock) {
        TileEntityCustomModel tile = getOrCreateTECM(scriptedBlock);
        return new GeckoBlockAnimatable(scriptedBlock, tile);
    }

    // ========================================================================
    // Animation Queries
    // ========================================================================

    @Override
    public String[] getAnimationList(String animationFile) {
        AnimationFile file = GeckoLibCache.getInstance().getAnimations().get(new ResourceLocation(animationFile));
        if (file == null) {
            return new String[0];
        }
        java.util.Collection<Animation> animations = file.getAllAnimations();
        String[] names = new String[animations.size()];
        int i = 0;
        for (Animation anim : animations) {
            names[i++] = anim.animationName;
        }
        return names;
    }

    @Override
    public String[] getAnimationFileList() {
        java.util.Set<ResourceLocation> keys = GeckoLibCache.getInstance().getAnimations().keySet();
        String[] result = new String[keys.size()];
        int i = 0;
        for (ResourceLocation resLoc : keys) {
            result[i++] = resLoc.toString();
        }
        return result;
    }

    @Override
    public IGeckoAnimation getAnimation(String animationFile, String animationName) {
        AnimationFile file = GeckoLibCache.getInstance().getAnimations().get(new ResourceLocation(animationFile));
        if (file == null) {
            return null;
        }
        for (Animation anim : file.getAllAnimations()) {
            if (anim.animationName.equals(animationName)) {
                return new GeckoAnimationWrapper(anim);
            }
        }
        return null;
    }

    // ========================================================================
    // Color Utilities
    // ========================================================================

    @Override
    public IGeckoColor colorOfRGB(int r, int g, int b) {
        return new GeckoColorWrapper(Color.ofRGB(r, g, b));
    }

    @Override
    public IGeckoColor colorOfRGBA(int r, int g, int b, int a) {
        return new GeckoColorWrapper(Color.ofRGBA(r, g, b, a));
    }

    // ========================================================================
    // NPC Methods
    // ========================================================================

    @Override
    public void setModel(ICustomNpc<EntityNPCInterface> npc, String model) {
        ((IDataDisplay) npc.getMCEntity().display).getCustomModelData().setModel(model);
        npc.updateClient();
    }

    @Override
    public void setTexture(ICustomNpc<EntityNPCInterface> npc, String texture) {
        npc.getMCEntity().display.texture = texture;
        npc.updateClient();
    }

    @Override
    public void setAnimationFile(ICustomNpc<EntityNPCInterface> npc, String animation) {
        ((IDataDisplay) npc.getMCEntity().display).getCustomModelData().setAnimFile(animation);
        npc.updateClient();
    }

    @Override
    public void setIdleAnimation(ICustomNpc<EntityNPCInterface> npc, String animation) {
        ((IDataDisplay) npc.getMCEntity().display).getCustomModelData().setIdleAnim(animation);
        npc.updateClient();
    }

    @Override
    public void syncAnimForPlayer(ICustomNpc<EntityNPCInterface> npc, IGeckoAnimationBuilder builder, IPlayer<EntityPlayerMP> player) {
        NetworkHandler.sendToPlayer(new CPacketSyncManualAnim(npc.getMCEntity(), unwrap(builder)), player.getMCEntity());
    }

    @Override
    public void syncAnimForAll(ICustomNpc<EntityNPCInterface> npc, IGeckoAnimationBuilder builder) {
        NetworkHandler.sendToAll(new CPacketSyncManualAnim(npc.getMCEntity(), unwrap(builder)));
    }

    // ========================================================================
    // Block Methods
    // ========================================================================

    private TileEntityCustomModel getOrCreateTECM(IBlockScripted scriptedBlock) {
        TileScripted tile = (TileScripted) scriptedBlock.getMCTileEntity();
        if (!(tile.renderTile instanceof TileEntityCustomModel)) {
            tile.renderTile = new TileEntityCustomModel(tile);
        }
        return (TileEntityCustomModel) tile.renderTile;
    }

    @Override
    public void setModel(IBlockScripted scriptedBlock, String model) {
        TileEntityCustomModel geckoTile = getOrCreateTECM(scriptedBlock);
        geckoTile.modelResLoc = new ResourceLocation(model);
        ((TileScripted) scriptedBlock.getMCTileEntity()).needsClientUpdate = true;
    }

    @Override
    public void setTexture(IBlockScripted scriptedBlock, String texture) {
        TileEntityCustomModel geckoTile = getOrCreateTECM(scriptedBlock);
        geckoTile.textureResLoc = new ResourceLocation(texture);
        ((TileScripted) scriptedBlock.getMCTileEntity()).needsClientUpdate = true;
    }

    @Override
    public void setAnimationFile(IBlockScripted scriptedBlock, String animation) {
        TileEntityCustomModel geckoTile = getOrCreateTECM(scriptedBlock);
        geckoTile.animResLoc = new ResourceLocation(animation);
        ((TileScripted) scriptedBlock.getMCTileEntity()).needsClientUpdate = true;
    }

    @Override
    public void setIdleAnimation(IBlockScripted scriptedBlock, String animation) {
        TileEntityCustomModel geckoTile = getOrCreateTECM(scriptedBlock);
        geckoTile.idleAnimName = animation;
        ((TileScripted) scriptedBlock.getMCTileEntity()).needsClientUpdate = true;
    }

    @Override
    public void syncAnimForPlayer(IBlockScripted scriptedBlock, IGeckoAnimationBuilder builder, IPlayer<EntityPlayerMP> player) {
        NetworkHandler.sendToPlayer(new CPacketSyncTileManualAnim(scriptedBlock.getMCTileEntity(), unwrap(builder)), player.getMCEntity());
    }

    @Override
    public void syncAnimForAll(IBlockScripted scriptedBlock, IGeckoAnimationBuilder builder) {
        NetworkHandler.sendToAll(new CPacketSyncTileManualAnim(scriptedBlock.getMCTileEntity(), unwrap(builder)));
    }

    // ========================================================================
    // Item Methods
    // ========================================================================

    @Override
    public IGeckoItemModel getItemGecko(IItemCustomizable item) {
        if (!(item instanceof IScriptCustomItem)) {
            throw new IllegalArgumentException("Item does not support gecko models. Must be a Scripted or Linked item.");
        }
        return new GeckoItemModelWrapper(item);
    }

    @Override
    public String[] getDisplayFileList() {
        return ItemDisplayLibrary.instance.getFileNames();
    }
}
