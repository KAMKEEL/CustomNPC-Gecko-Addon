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
            // Only override INVENTORY_BLOCK for 3D isometric inventory rendering.
            // For EQUIPPED, keep the default sprite path — it handles all arm
            // orientations correctly (player, zombie, NPC models).
            if (helper == ItemRendererHelper.INVENTORY_BLOCK) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(
        method = "renderItem(Lnet/minecraftforge/client/IItemRenderer$ItemRenderType;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V",
        cancellable = true,
        at = @At("HEAD"),
        remap = false
    )
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object[] data, CallbackInfo ci) {
        if (ModelItemRenderUtil.tryRender(type, itemStack, data)) {
            ci.cancel();
        }
    }
}
