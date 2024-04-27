package com.goodbird.npcgecko.client.renderer;

import com.goodbird.npcgecko.client.model.TileModelCustom;
import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import software.bernie.example.client.model.tile.BotariumModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class RenderTileCustomModel extends GeoBlockRenderer<TileEntityCustomModel> {
    public RenderTileCustomModel() {
        super(new TileModelCustom());
    }
}

