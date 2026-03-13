package com.goodbird.npcgecko.client;

import com.goodbird.npcgecko.data.CustomItemModelData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

/**
 * Carries render context through the animation pipeline via wrapper userData.
 * The predicate reads this to determine perspective (FP vs TP) and entity identity.
 */
public class ItemRenderContext {
    public final CustomItemModelData modelData;
    public final ItemRenderType renderType;
    public final EntityLivingBase entity;
    public final int entityId;
    public final int slotIndex;

    public ItemRenderContext(CustomItemModelData modelData, ItemRenderType renderType,
                            EntityLivingBase entity, int entityId, int slotIndex) {
        this.modelData = modelData;
        this.renderType = renderType;
        this.entity = entity;
        this.entityId = entityId;
        this.slotIndex = slotIndex;
    }
}
