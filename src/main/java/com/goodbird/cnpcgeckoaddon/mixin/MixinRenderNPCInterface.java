package com.goodbird.cnpcgeckoaddon.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.client.model.ModelMPM;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.IAnimatable;

@Mixin(RenderNPCInterface.class)
public abstract class MixinRenderNPCInterface extends RendererLivingEntity {

    public MixinRenderNPCInterface() {
        super(null,0);
    }

    @Inject(method = "renderModel",at=@At("HEAD"), cancellable = true)
    protected void renderModel(EntityLivingBase entityliving, float par2, float par3, float par4, float par5, float par6, float par7, CallbackInfo ci) {
        EntityNPCInterface npc = (EntityNPCInterface)entityliving;
        if(mainModel instanceof ModelMPM && ((ModelMPM) mainModel).entity instanceof IAnimatable){
            GL11.glRotatef(180, 1,0,0);
            GL11.glTranslated(0, -1.5,0);
            customNPC_Gecko_Addon$renderGeoModel(npc, npc.rotationYaw, Minecraft.getMinecraft().timer.renderPartialTicks);
            ci.cancel();
        }
    }

    @Unique
    private void customNPC_Gecko_Addon$renderGeoModel(EntityNPCInterface npc, float rot, float partial)
    {
        ((ModelMPM) mainModel).entity.renderYawOffset = ((ModelMPM) mainModel).entity.prevRenderYawOffset = 0;
        if (!npc.isInvisible())
        {
            RenderManager.instance.renderEntityWithPosYaw(((ModelMPM) mainModel).entity, 0,0,0,rot,partial);
        }
        else if (!npc.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            RenderManager.instance.renderEntityWithPosYaw(((ModelMPM) mainModel).entity, 0,0,0,rot,partial);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
        }
    }
}
