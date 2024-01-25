package com.goodbird.npcgecko;

import com.goodbird.npcgecko.entity.EntityCustomModel;
import com.goodbird.npcgecko.handler.EventHandler;
import com.goodbird.npcgecko.network.NetworkHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import noppes.npcs.scripted.NpcAPI;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent ev){
        EntityRegistry.registerModEntity(EntityCustomModel.class, "CustomModelEntity", 0, CustomNpcPlusGecko.instance, 64, 10, false);
        NpcAPI.EVENT_BUS.register(new EventHandler());
    }

    public void init(FMLInitializationEvent ev){
        NetworkHandler.init();
    }
}
