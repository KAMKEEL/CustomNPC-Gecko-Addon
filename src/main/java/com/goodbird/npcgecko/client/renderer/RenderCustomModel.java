package com.goodbird.npcgecko.client.renderer;

import com.goodbird.npcgecko.client.model.ModelCustom;
import com.goodbird.npcgecko.entity.EntityCustomModel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.ArrayList;

public class RenderCustomModel extends GeoEntityRenderer<EntityCustomModel> {

    public RenderCustomModel() {
        super(new ModelCustom());
    }
    public void renderAfter(GeoModel model, EntityCustomModel animatable, float ticks, float red, float green, float blue, float alpha) {
        super.renderAfter(model, animatable, ticks, red, green, blue, alpha);
        if (model.getBone("held_item").isPresent() && animatable.getHeldItem() != null) {
            GeoBone bone = model.getBone("held_item").get();
            this.renderItem(animatable, animatable.getHeldItem(), bone);
        }
        if (model.getBone("left_held_item").isPresent() && animatable.leftHeldItem != null) {
            GeoBone bone = model.getBone("left_held_item").get();
            this.renderLeftHandItem(animatable, animatable.leftHeldItem, bone);
        }
    }

    public GeoBone[] getPathFromRoot(GeoBone bone) {
        ArrayList<GeoBone> bones;
        for(bones = new ArrayList<>(); bone != null; bone = bone.parent) {
            bones.add(0, bone);
        }

        return bones.toArray(new GeoBone[0]);
    }

    public void renderItem(EntityCustomModel animatable, ItemStack stack, GeoBone locator) {
        GL11.glPushMatrix();
        float scale = 0.5F;
        GL11.glScaled(scale, scale, scale);
        GeoBone[] bonePath = this.getPathFromRoot(locator);

        for (GeoBone b : bonePath) {
            GL11.glTranslatef(b.getPositionX() / (16.0F * scale), b.getPositionY() / (16.0F * scale), b.getPositionZ() / (16.0F * scale));
            GL11.glTranslatef(b.getPivotX() / (16.0F * scale), b.getPivotY() / (16.0F * scale), b.getPivotZ() / (16.0F * scale));
            GL11.glRotated((double) b.getRotationZ() / Math.PI * 180.0, 0.0, 0.0, 1.0);
            GL11.glRotated((double) b.getRotationY() / Math.PI * 180.0, 0.0, 1.0, 0.0);
            GL11.glRotated((double) b.getRotationX() / Math.PI * 180.0, 1.0, 0.0, 0.0);
            GL11.glScalef(b.getScaleX(), b.getScaleY(), b.getScaleZ());
            GL11.glTranslatef(-b.getPivotX() / (16.0F * scale), -b.getPivotY() / (16.0F * scale), -b.getPivotZ() / (16.0F * scale));
        }
        GL11.glRotatef(250.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(40.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.4F, -0.55F, 1.7F);
        RenderManager.instance.itemRenderer.renderItem(animatable, stack, 0, IItemRenderer.ItemRenderType.INVENTORY);
        GL11.glPopMatrix();
    }

    public void renderLeftHandItem(EntityCustomModel animatable, ItemStack stack, GeoBone locator) {
        GL11.glPushMatrix();
        float scale = 0.5F;
        GL11.glScaled(scale, scale, scale);
        GeoBone[] bonePath = this.getPathFromRoot(locator);

        for (GeoBone b : bonePath) {
            GL11.glTranslatef(b.getPositionX() / (16.0F * scale), b.getPositionY() / (16.0F * scale), b.getPositionZ() / (16.0F * scale));
            GL11.glTranslatef(b.getPivotX() / (16.0F * scale), b.getPivotY() / (16.0F * scale), b.getPivotZ() / (16.0F * scale));
            GL11.glRotated((double) b.getRotationZ() / Math.PI * 180.0, 0.0, 0.0, 1.0);
            GL11.glRotated((double) b.getRotationY() / Math.PI * 180.0, 0.0, 1.0, 0.0);
            GL11.glRotated((double) b.getRotationX() / Math.PI * 180.0, 1.0, 0.0, 0.0);
            GL11.glScalef(b.getScaleX(), b.getScaleY(), b.getScaleZ());
            GL11.glTranslatef(-b.getPivotX() / (16.0F * scale), -b.getPivotY() / (16.0F * scale), -b.getPivotZ() / (16.0F * scale));
        }
        GL11.glRotatef(250.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(40.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-1.51F, -0.55F, 0.7F);
        RenderManager.instance.itemRenderer.renderItem(animatable, stack, 0, IItemRenderer.ItemRenderType.INVENTORY);
        GL11.glPopMatrix();
    }

    public Color getRenderColor(EntityCustomModel animatable, float partialTicks) {
        if(animatable.hurtTime>0 || animatable.deathTime > 0){
            return Color.ofRGBA(255, 30, 30, 255);
        }else{
            return Color.ofRGBA(255, 255, 255, 255);
        }
    }

    public boolean isBoneRenderOverriden(EntityCustomModel entity, GeoBone bone) {
        return bone.name.equals("held_item") || bone.name.equals("left_held_item");
    }
}
