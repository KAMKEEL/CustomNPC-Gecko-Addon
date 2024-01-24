package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import com.goodbird.cnpcgeckoaddon.utils.NpcTextureUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityUtil.class)
public class MixinEntityUtil {

    @Inject(method = "Copy", at = @At("TAIL"), remap = false)
    private static void copy(EntityLivingBase copied, EntityLivingBase entity, CallbackInfo ci) {
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
