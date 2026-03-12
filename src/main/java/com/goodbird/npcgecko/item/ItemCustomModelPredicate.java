package com.goodbird.npcgecko.item;

import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class ItemCustomModelPredicate {
    public static <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        ItemStack stack = (ItemStack) event.getExtraData().get(0);
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        if (!(istack instanceof IItemCustomizable)) return PlayState.STOP;
        if (!(istack instanceof IScriptCustomItem)) return PlayState.STOP;
        IScriptCustomItem item = (IScriptCustomItem) istack;
        CustomItemModelData data = item.getCustomModelData();
        if (data == null) return PlayState.STOP;
        String idleAnim = data.getIdleAnim();
        if (idleAnim == null || idleAnim.isEmpty()) return PlayState.STOP;
        event.getController().setAnimation(new AnimationBuilder().loop(idleAnim));
        return PlayState.CONTINUE;
    }
}
