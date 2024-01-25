package com.goodbird.cnpcplusgecko.client;

import com.goodbird.cnpcplusgecko.CommonProxy;
import com.goodbird.cnpcplusgecko.client.renderer.RenderCustomModel;
import com.goodbird.cnpcplusgecko.entity.EntityCustomModel;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {
        super.preInit(ev);
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomModel.class, new RenderCustomModel());
    }

}
