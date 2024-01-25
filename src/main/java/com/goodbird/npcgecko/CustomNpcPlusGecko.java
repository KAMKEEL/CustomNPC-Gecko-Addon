package com.goodbird.npcgecko;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "npcgecko",
    name = "CustomNPC+ Gecko Addon",
    version = "1.0",
    dependencies = "required-after:customnpcs;required-after:geckolib3")
public class CustomNpcPlusGecko {

    @SidedProxy(clientSide = "com.goodbird.npcgecko.client.ClientProxy", serverSide = "com.goodbird.npcgecko.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static CustomNpcPlusGecko instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        proxy.preInit(ev);
    }
}
