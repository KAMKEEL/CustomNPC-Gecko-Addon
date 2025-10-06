// TODO: Fix ScriptCustomItem

//package com.goodbird.npcgecko.mixin.impl;
//
//import com.goodbird.npcgecko.client.model.ItemModelCustom;
//import com.goodbird.npcgecko.mixin.IScriptCustomItem;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.item.ItemStack;
//import noppes.npcs.client.renderer.items.CustomItemRenderer;
//import noppes.npcs.scripted.item.ScriptCustomItem;
//import org.lwjgl.opengl.GL11;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import software.bernie.geckolib3.item.AnimatableStackWrapper;
//import software.bernie.geckolib3.renderers.geo.GeoItemStackRenderer;
//
//@Mixin(CustomItemRenderer.class)
//public class MixinCustomItemRenderer {
//    @Unique
//    private static GeoItemStackRenderer renderer = new GeoItemStackRenderer(new ItemModelCustom());
//
//    @Inject(method = "renderEntityCustomItem", cancellable = true, at=@At("HEAD"), remap = false)
//    public void renderEntityCustomItem(ScriptCustomItem scriptCustomItem, ItemStack itemStack, EntityItem entityItem, CallbackInfo ci) {
//        if(((IScriptCustomItem)scriptCustomItem).hasCustomModel()){
//            renderer.render(AnimatableStackWrapper.wrapItemStack(itemStack), itemStack);
//            ci.cancel();
//        }
//    }
//
//    @Inject(method = "renderInventoryCustomItem", cancellable = true, at=@At("HEAD"), remap = false)
//    public void renderInventoryCustomItem(ScriptCustomItem scriptCustomItem, CallbackInfo ci){
//        if(((IScriptCustomItem)scriptCustomItem).hasCustomModel()){
//            GL11.glPushMatrix();
//            GL11.glTranslated(-1.0, -1.0, 0.0);
//            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
//            renderer.render(AnimatableStackWrapper.wrapItemStack(scriptCustomItem.item), scriptCustomItem.item);
//            GL11.glPopMatrix();
//            ci.cancel();
//        }
//    }
//
//    @Inject(method = "renderItem3d", cancellable = true, at=@At("HEAD"), remap = false)
//    public void renderItem3d(ScriptCustomItem scriptCustomItem, EntityLivingBase entityLivingBase, ItemStack itemStack, CallbackInfo ci) {
//        if(((IScriptCustomItem)scriptCustomItem).hasCustomModel()){
//            renderer.render(AnimatableStackWrapper.wrapItemStack(itemStack), itemStack);
//            ci.cancel();
//        }
//    }
//
//}
