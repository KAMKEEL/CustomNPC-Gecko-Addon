package com.goodbird.cnpcgeckoaddon;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "cnpcgeckoaddon",
    name = "CustomNPC Gecko Addon",
    version = "1.0",
    dependencies = "required-after:customnpcs;required-after:geckolib3")
public class CNPCGeckoAddon {

    @SidedProxy(clientSide = "com.goodbird.cnpcgeckoaddon.client.ClientProxy", serverSide = "com.goodbird.cnpcgeckoaddon.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static CNPCGeckoAddon instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        proxy.preInit(ev);
    }
}
