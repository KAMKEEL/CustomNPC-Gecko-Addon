package com.goodbird.cnpcplusgecko;

import com.goodbird.cnpcplusgecko.entity.EntityCustomModel;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent ev){
        EntityRegistry.registerModEntity(EntityCustomModel.class, "CustomModelEntity", 0, CustomNpcPlusGecko.instance, 64, 10, false);
    }
}
