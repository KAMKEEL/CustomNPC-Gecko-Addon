package com.goodbird.npcgecko.client;

import com.goodbird.npcgecko.CommonProxy;
import com.goodbird.npcgecko.client.renderer.RenderCustomModel;
import com.goodbird.npcgecko.client.renderer.RenderTileCustomModel;
import com.goodbird.npcgecko.entity.EntityCustomModel;
import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;


public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {
        super.preInit(ev);
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomModel.class, new RenderCustomModel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCustomModel.class, new RenderTileCustomModel());
    }

    public World getWorldById(int id){
        return Minecraft.getMinecraft().theWorld;
    }
}
