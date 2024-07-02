package com.goodbird.npcgecko.client.model;

import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.item.ScriptCustomItem;
import software.bernie.geckolib3.item.AnimatableStackWrapper;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class ItemModelCustom extends AnimatedGeoModel<AnimatableStackWrapper> {

    private CustomItemModelData getModelData(AnimatableStackWrapper animatable){
        IItemStack istack = NpcAPI.Instance().getIItemStack(animatable.getStack());
        if(!(istack instanceof ScriptCustomItem)) return null;
        return ((IScriptCustomItem)istack).getCustomModelData();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AnimatableStackWrapper animatable) {
        CustomItemModelData data = getModelData(animatable);
        if(data==null || !GeckoLibCache.getInstance().getAnimations().containsKey(new ResourceLocation(data.getAnimFile()))){
            return new ResourceLocation("npcgecko","animations/none.animations.json");
        }
        return new ResourceLocation(data.getAnimFile());
    }

    @Override
    public ResourceLocation getModelLocation(AnimatableStackWrapper animatable) {
        CustomItemModelData data = getModelData(animatable);
        if(data==null || !GeckoLibCache.getInstance().getGeoModels().containsKey(new ResourceLocation(data.getModel()))){
            return new ResourceLocation("npcgecko","geo/modelnotfound.geo.json");
        }
        if(!GeckoLibCache.getInstance().getAnimations().containsKey(new ResourceLocation(data.getAnimFile()))){
            return new ResourceLocation("npcgecko","geo/animfilenotfound.geo.json");
        }
        return new ResourceLocation(data.getModel());
    }

    @Override
    public ResourceLocation getTextureLocation(AnimatableStackWrapper animatable) {
        CustomItemModelData data = getModelData(animatable);
        if(data==null || !GeckoLibCache.getInstance().getGeoModels().containsKey(new ResourceLocation(data.getModel()))){
            return new ResourceLocation("npcgecko","textures/model/alphabet.png");
        }
        if(!GeckoLibCache.getInstance().getAnimations().containsKey(new ResourceLocation(data.getAnimFile()))){
            return new ResourceLocation("npcgecko","textures/model/alphabet.png");
        }
        return new ResourceLocation(data.getTexture());
    }
}
