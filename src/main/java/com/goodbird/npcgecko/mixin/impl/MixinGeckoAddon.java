package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.entity.EntityCustomModel;
import com.goodbird.npcgecko.mixin.IDataDisplay;
import com.goodbird.npcgecko.utils.NpcTextureUtils;
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
            modelEntity.idleAnimName = display.getCustomModelData().getIdleAnim();
            modelEntity.walkAnimName = display.getCustomModelData().getWalkAnim();
            modelEntity.meleeAttackAnimName = display.getCustomModelData().getMeleeAttackAnim();
            modelEntity.rangedAttackAnimName = display.getCustomModelData().getRangedAttackAnim();
            modelEntity.hurtAnimName = display.getCustomModelData().getHurtAnim();
            modelEntity.leftHeldItem = npc.inventory.getOffHand();
            modelEntity.hurtTime = npc.hurtTime;
            modelEntity.deathTime = npc.deathTime;
            modelEntity.tintData = npc.display.tintData;
        }
    }
}
