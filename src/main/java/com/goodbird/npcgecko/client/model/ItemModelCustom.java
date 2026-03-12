package com.goodbird.npcgecko.client.model;

import com.goodbird.npcgecko.data.CustomItemModelData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.item.AnimatableStackWrapper;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class ItemModelCustom extends AnimatedGeoModel<AnimatableStackWrapper> {

    private CustomItemModelData getModelData(AnimatableStackWrapper animatable) {
        // Primary: read from userData attached by the mixin renderer
        Object userData = animatable.getUserData();
        if (userData instanceof CustomItemModelData) {
            return (CustomItemModelData) userData;
        }
        // Fallback: read directly from ItemStack NBT
        if (animatable.getStack() != null && animatable.getStack().hasTagCompound()) {
            NBTTagCompound root = animatable.getStack().getTagCompound();
            if (root.hasKey("ItemData")) {
                NBTTagCompound itemData = root.getCompoundTag("ItemData");
                if (itemData.hasKey("GeckoModelData")) {
                    CustomItemModelData data = new CustomItemModelData();
                    data.readFromNBT(itemData.getCompoundTag("GeckoModelData"));
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AnimatableStackWrapper animatable) {
        CustomItemModelData data = getModelData(animatable);
        if (data == null || data.getAnimFile() == null || data.getAnimFile().isEmpty()
                || !GeckoLibCache.getInstance().getAnimations().containsKey(new ResourceLocation(data.getAnimFile()))) {
            return new ResourceLocation("npcgecko", "animations/none.animations.json");
        }
        return new ResourceLocation(data.getAnimFile());
    }

    @Override
    public ResourceLocation getModelLocation(AnimatableStackWrapper animatable) {
        CustomItemModelData data = getModelData(animatable);
        if (data == null || data.getModel() == null || data.getModel().isEmpty()
                || !GeckoLibCache.getInstance().getGeoModels().containsKey(new ResourceLocation(data.getModel()))) {
            return new ResourceLocation("npcgecko", "geo/modelnotfound.geo.json");
        }
        if (data.getAnimFile() == null || data.getAnimFile().isEmpty()
                || !GeckoLibCache.getInstance().getAnimations().containsKey(new ResourceLocation(data.getAnimFile()))) {
            return new ResourceLocation("npcgecko", "geo/animfilenotfound.geo.json");
        }
        return new ResourceLocation(data.getModel());
    }

    @Override
    public ResourceLocation getTextureLocation(AnimatableStackWrapper animatable) {
        CustomItemModelData data = getModelData(animatable);
        if (data == null || data.getModel() == null || data.getModel().isEmpty()
                || !GeckoLibCache.getInstance().getGeoModels().containsKey(new ResourceLocation(data.getModel()))) {
            return new ResourceLocation("npcgecko", "textures/model/alphabet.png");
        }
        if (data.getAnimFile() == null || data.getAnimFile().isEmpty()
                || !GeckoLibCache.getInstance().getAnimations().containsKey(new ResourceLocation(data.getAnimFile()))) {
            return new ResourceLocation("npcgecko", "textures/model/alphabet.png");
        }
        return new ResourceLocation(data.getTexture());
    }
}
