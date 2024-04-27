package com.goodbird.npcgecko.api;

import com.goodbird.npcgecko.client.renderer.RenderTileCustomModel;
import com.goodbird.npcgecko.mixin.IDataDisplay;
import com.goodbird.npcgecko.network.CPacketSyncManualAnim;
import com.goodbird.npcgecko.network.CPacketSyncTileManualAnim;
import com.goodbird.npcgecko.network.NetworkHandler;
import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.scripted.BlockScriptedWrapper;
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
    public void setModel(ICustomNpc<EntityNPCInterface> npc, String model) {
        ((IDataDisplay)npc.getMCEntity().display).getCustomModelData().setModel(model);
        npc.updateClient();
    }

    @Override
    public void setTexture(ICustomNpc<EntityNPCInterface> npc, String texture) {
        npc.getMCEntity().display.texture = texture;
        npc.updateClient();
    }

    @Override
    public void setAnimationFile(ICustomNpc<EntityNPCInterface> npc, String animation) {
        ((IDataDisplay)npc.getMCEntity().display).getCustomModelData().setAnimFile(animation);
        npc.updateClient();
    }

    @Override
    public void setIdleAnimation(ICustomNpc<EntityNPCInterface> npc, String animation) {
        ((IDataDisplay)npc.getMCEntity().display).getCustomModelData().setIdleAnim(animation);
        npc.updateClient();
    }

    @Override
    public void syncAnimForPlayer(ICustomNpc<EntityNPCInterface> npc, AnimationBuilder builder, IPlayer<EntityPlayerMP> player) {
        NetworkHandler.sendToPlayer(new CPacketSyncManualAnim(npc.getMCEntity(),builder), player.getMCEntity());
    }

    @Override
    public void syncAnimForAll(ICustomNpc<EntityNPCInterface> npc, AnimationBuilder builder) {
        NetworkHandler.sendToAll(new CPacketSyncManualAnim(npc.getMCEntity(),builder));
    }



    private TileEntityCustomModel getOrCreateTECM(IBlockScripted scriptedBlock){
        TileScripted tile = (TileScripted) scriptedBlock.getMCTileEntity();
        if(!(tile.renderTile instanceof TileEntityCustomModel)){
            tile.renderTile = new TileEntityCustomModel();
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
    public void syncAnimForPlayer(IBlockScripted scriptedBlock, AnimationBuilder builder, IPlayer<EntityPlayerMP> player) {
        NetworkHandler.sendToPlayer(new CPacketSyncTileManualAnim(scriptedBlock.getMCTileEntity(), builder), player.getMCEntity());
    }

    @Override
    public void syncAnimForAll(IBlockScripted scriptedBlock, AnimationBuilder builder) {
        NetworkHandler.sendToAll(new CPacketSyncTileManualAnim(scriptedBlock.getMCTileEntity(), builder));
    }
}
