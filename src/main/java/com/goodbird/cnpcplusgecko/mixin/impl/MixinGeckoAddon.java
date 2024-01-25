package com.goodbird.cnpcplusgecko.mixin.impl;

import com.goodbird.cnpcplusgecko.entity.EntityCustomModel;
import com.goodbird.cnpcplusgecko.mixin.IDataDisplay;
import com.goodbird.cnpcplusgecko.utils.NpcTextureUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import kamkeel.addon.GeckoAddon;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GeckoAddon.class)
public class MixinGeckoAddon {

    @Shadow(remap = false) public boolean supportEnabled;

    /**
     * @author Goodbird
     * @reason Allow for Copying of Data for Custom Model information
     */
    @Overwrite(remap = false)
    public void geckoCopyData(EntityLivingBase copied, EntityLivingBase entity) {
        if(!supportEnabled)
            return;

        if (entity instanceof EntityCustomModel modelEntity && copied instanceof EntityNPCInterface npc) {
            IDataDisplay display = (IDataDisplay) npc.display;
            modelEntity.textureResLoc = NpcTextureUtils.getNpcTexture((EntityNPCInterface) copied);
            modelEntity.modelResLoc = new ResourceLocation(display.getCustomModelData().getModel());
            modelEntity.animResLoc = new ResourceLocation(display.getCustomModelData().getAnimFile());
            modelEntity.idleAnim = display.getCustomModelData().getIdleAnim();
            modelEntity.walkAnim = display.getCustomModelData().getWalkAnim();
            modelEntity.attackAnim = display.getCustomModelData().getAttackAnim();
            modelEntity.hurtAnim = display.getCustomModelData().getHurtAnim();
            modelEntity.leftHeldItem = npc.inventory.getOffHand();
        }
    }
}
