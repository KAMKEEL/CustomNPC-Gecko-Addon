package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.client.ModelItemRenderUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import noppes.npcs.client.renderer.items.ItemCustomRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemCustomRenderer.class)
public class MixinCustomItemRenderer {

    @Inject(
        method = "shouldUseRenderHelper",
        cancellable = true,
        at = @At("HEAD"),
        remap = false
    )
    public void shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper, CallbackInfoReturnable<Boolean> cir) {
        if (ModelItemRenderUtil.hasGeckoModel(item)) {
            // Return true for all helpers, matching GeoItemRenderer behavior.
            // EQUIPPED_BLOCK → simple translate(-0.5) path in ForgeHooksClient
            // INVENTORY_BLOCK → 3D isometric inventory rendering
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "renderItem(Lnet/minecraftforge/client/IItemRenderer$ItemRenderType;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V",
        cancellable = true,
        at = @At("HEAD"),
        remap = false
    )
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object[] data, CallbackInfo ci) {
        if (ModelItemRenderUtil.tryRender(type, itemStack)) {
            ci.cancel();
        }
    }
}
