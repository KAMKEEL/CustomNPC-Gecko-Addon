package com.goodbird.npcgecko.client.model;

import com.goodbird.npcgecko.entity.EntityCustomModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelCustom extends AnimatedGeoModel<EntityCustomModel> {
    @Override
    public ResourceLocation getAnimationFileLocation(EntityCustomModel entity) {
        return entity.animResLoc;
    }

    @Override
    public ResourceLocation getModelLocation(EntityCustomModel entity) {
        return entity.modelResLoc;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCustomModel entity) {
        return entity.textureResLoc;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setLivingAnimations(EntityCustomModel entity, Integer uniqueID, AnimationEvent animationEvent) {
        super.setLivingAnimations(entity, uniqueID, animationEvent); //We call the super-function
        IBone head = this.getAnimationProcessor().getBone(entity.headBoneName); //Then we take the head bone
        if(head!=null) {
            //We get the model data for an entity
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
            //And we set the head bone rotation to the interpolated pitch and yaw rotations of an entity
            head.setRotationY((extraData.netHeadYaw) * ((float) Math.PI / 180F));
            head.setRotationX((extraData.headPitch) * ((float) Math.PI / 180F));
        }
    }
}
