package noppes.npcs.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import noppes.npcs.CommonGeckoProxy;
import noppes.npcs.client.renderer.RenderCustomModel;
import noppes.npcs.entity.EntityCustomModel;

public class ClientGeckoProxy extends CommonGeckoProxy {

    @Override
    public void load() {
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomModel.class, new RenderCustomModel());
    }

}
