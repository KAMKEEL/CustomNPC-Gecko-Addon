package com.goodbird.npcgecko.mixin;

import com.goodbird.npcgecko.data.CustomItemModelData;

public interface IScriptCustomItem {
    CustomItemModelData getCustomModelData();

    void setCustomModelData(CustomItemModelData data);

    boolean hasCustomModel();
}
