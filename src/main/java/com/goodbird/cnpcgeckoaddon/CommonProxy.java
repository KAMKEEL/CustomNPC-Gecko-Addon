package com.goodbird.cnpcgeckoaddon;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent ev){
        EntityRegistry.registerModEntity(EntityCustomModel.class, "CustomModelEntity", 0, CNPCGeckoAddon.instance, 64, 10, false);
    }
}
