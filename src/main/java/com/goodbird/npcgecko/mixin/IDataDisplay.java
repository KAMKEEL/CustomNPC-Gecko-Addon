package com.goodbird.npcgecko.mixin;

import com.goodbird.npcgecko.data.CustomModelData;

public interface IDataDisplay {
    CustomModelData getCustomModelData();

    boolean hasCustomModel();
}
