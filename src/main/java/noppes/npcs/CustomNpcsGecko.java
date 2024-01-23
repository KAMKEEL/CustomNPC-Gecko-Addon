package noppes.npcs;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import noppes.npcs.entity.*;

@Mod(modid = "customnpcsgecko",
        name = "CustomNPC+ Gecko",
        version = "1.0",
        dependencies = "required-after:customnpcs;")
public class CustomNpcsGecko {

    @SidedProxy(clientSide = "noppes.npcs.client.ClientGeckoProxy", serverSide = "noppes.npcs.CommonGeckoProxy")
    public static CommonGeckoProxy proxy;

    private static int NewEntityStartId = 0;

    public CustomNpcsGecko() {}

    @EventHandler
    public void load(FMLPreInitializationEvent ev) {
        proxy.load();

        registerCustomModel(EntityCustomModel.class, "CustomModel");
    }

    public void registerCustomModel(Class<? extends Entity> cl, String name) {
        EntityRegistry.registerModEntity(cl, name, NewEntityStartId++, this, 64, 10, false);
        EntityList.stringToClassMapping.put(name, cl);
    }
}