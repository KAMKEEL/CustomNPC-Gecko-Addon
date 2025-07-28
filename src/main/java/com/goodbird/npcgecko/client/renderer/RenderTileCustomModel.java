package com.goodbird.npcgecko.client.renderer;

import com.goodbird.npcgecko.client.model.TileModelCustom;
import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import net.minecraft.tileentity.TileEntity;
import software.bernie.example.client.model.tile.BotariumModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class RenderTileCustomModel extends GeoBlockRenderer<TileEntityCustomModel> {
    public RenderTileCustomModel() {
        super(new TileModelCustom());
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
        // TODO: FIX RenderTileEntityAt
    }
}

