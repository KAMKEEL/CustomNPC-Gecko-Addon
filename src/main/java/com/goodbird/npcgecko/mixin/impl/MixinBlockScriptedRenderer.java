package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.client.renderer.blocks.BlockScriptedRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.renderers.geo.RenderBlockItem;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.ENTITY;

@Mixin(BlockScriptedRenderer.class)
public abstract class MixinBlockScriptedRenderer {

    @Inject(method = "renderTileEntityAt", at = @At(value = "INVOKE", target = "Lnoppes/npcs/client/renderer/blocks/BlockScriptedRenderer;renderItem(Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/item/ItemStack;)V", remap = false))
    public void geckoModelRendering(TileEntity te, double x, double y, double z, float partialTicks, CallbackInfo ci) {
        if(!(te instanceof TileScripted)) return;
        TileScripted tileScripted = (TileScripted) te;
        if(tileScripted.itemModel == null) return;
        if(!(MinecraftForgeClient.getItemRenderer(tileScripted.itemModel, ENTITY) instanceof RenderBlockItem) || BlockScriptedRenderer.overrideModel()) return;
        GL11.glTranslated(0, -0.5, 0);
        GL11.glScaled(2, 2, 2);
    }

    @Inject(method = "renderTileEntityAt", at = @At(value = "HEAD"), cancellable = true)
    public void customGeckoModelRendering(TileEntity te, double x, double y, double z, float partialTicks, CallbackInfo ci) {
        if(BlockScriptedRenderer.overrideModel()) return;
        if(!(te instanceof TileScripted)) return;
        TileScripted tileScripted = (TileScripted) te;
        if(!(tileScripted.renderTile instanceof TileEntityCustomModel)) return;
        GL11.glPushMatrix();
        GL11.glTranslated(x,y,z);
        GL11.glTranslated(0.5,0, 0.5);
        GL11.glRotatef((float)tileScripted.rotationY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef((float)tileScripted.rotationX, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef((float)tileScripted.rotationZ, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(tileScripted.scaleX, tileScripted.scaleY, tileScripted.scaleZ);
        GL11.glTranslated(-0.5,0, -0.5);
        GL11.glTranslated(-x,-y,-z);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(tileScripted.renderTile, x, y, z, partialTicks);
        GL11.glPopMatrix();
        ci.cancel();
    }
}
