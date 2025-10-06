package com.goodbird.npcgecko.item;

import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.item.ScriptCustomItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class ItemCustomModelPredicate {
    public static <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        ItemStack stack = (ItemStack) event.getExtraData().get(0);
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        if(!(istack instanceof ScriptCustomItem)) return PlayState.STOP;
        IScriptCustomItem item = (IScriptCustomItem)istack;
        event.getController().setAnimation(new AnimationBuilder().loop(item.getCustomModelData().getIdleAnim()));
        return PlayState.CONTINUE;
    }
}
