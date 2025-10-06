package com.goodbird.npcgecko;

import com.goodbird.npcgecko.api.AbstractGeckoAPI;
import com.goodbird.npcgecko.entity.EntityCustomModel;
import com.goodbird.npcgecko.handler.EventHandler;
import com.goodbird.npcgecko.network.NetworkHandler;
import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import noppes.npcs.scripted.NpcAPI;
import software.bernie.example.block.tile.BotariumTileEntity;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent ev){
        EntityRegistry.registerModEntity(EntityCustomModel.class, "CustomModelEntity", 0, CustomNpcPlusGecko.instance, 64, 10, false);
        NpcAPI.EVENT_BUS.register(new EventHandler());
    }

    public void init(FMLInitializationEvent ev){
        NetworkHandler.init();
        GameRegistry.registerTileEntity(TileEntityCustomModel.class, "custommodeltile");
        NpcAPI.Instance().addGlobalObject("GeckoAPI", AbstractGeckoAPI.Instance());
    }

    public World getWorldById(int id){
        return MinecraftServer.getServer().worldServers[id];
    }
}
