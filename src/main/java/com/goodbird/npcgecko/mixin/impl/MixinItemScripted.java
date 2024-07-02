package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.item.ItemCustomModelPredicate;
import net.minecraft.item.Item;
import noppes.npcs.items.ItemScripted;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@Mixin(ItemScripted.class)
@Implements(value = @Interface(iface = IAnimatable.class, prefix = "customNPC_Gecko_Addon$"))
public abstract class MixinItemScripted extends Item implements IAnimatable {
    @Unique
    public AnimationFactory customNPC_Gecko_Addon$factory = new AnimationFactory(this);

    @Unique
    public void customNPC_Gecko_Addon$registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, ItemCustomModelPredicate::predicate));
    }

    @Unique
    public AnimationFactory customNPC_Gecko_Addon$getFactory() {
        return this.customNPC_Gecko_Addon$factory;
    }
}
