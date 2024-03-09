package com.goodbird.npcgecko.entity;

import com.goodbird.npcgecko.constants.EnumSyncAutoAnim;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityCustomModel extends EntityCreature implements IAnimatable, IAnimationTickable {
    private final AnimationFactory factory = new AnimationFactory(this);
    public ResourceLocation modelResLoc = new ResourceLocation("geckolib3", "geo/npc.geo.json");
    public ResourceLocation animResLoc = new ResourceLocation("custom", "geo_npc.animation.json");
    public ResourceLocation textureResLoc = new ResourceLocation("geckolib3", "textures/model/entity/geo_npc.png");
    public String idleAnimName = "";
    public String walkAnimName = "";
    public String hurtAnimName = "";
    public String meleeAttackAnimName = "";
    public String rangedAttackAnimName = "";
    public AnimationBuilder dialogAnim = null;
    public AnimationBuilder manualAnim = null;
    public AnimationBuilder attackAnim = null;
    public AnimationBuilder hurtAnim = null;
    public ItemStack leftHeldItem;
    public boolean isSemiVisible = false;

    private <E extends IAnimatable> PlayState predicateMovement(AnimationEvent<E> event) {
        if (manualAnim != null) {
            if (event.getController().getAnimationState() == AnimationState.Stopped) {
                manualAnim = null;
            } else {
                if (event.getController().currentAnimationBuilder != manualAnim) {
                    event.getController().markNeedsReload();
                }
                event.getController().setAnimation(manualAnim);
                return PlayState.CONTINUE;
            }
        }
        if (dialogAnim != null) {
            if (event.getController().getAnimationState() == AnimationState.Stopped) {
                dialogAnim = null;
            } else {
                if (event.getController().currentAnimationBuilder != dialogAnim) {
                    event.getController().markNeedsReload();
                }
                event.getController().setAnimation(dialogAnim);
                return PlayState.CONTINUE;
            }
        }
        if (attackAnim != null) {
            if (event.getController().getAnimationState() == AnimationState.Stopped) {
                attackAnim = null;
            } else {
                if (event.getController().currentAnimationBuilder != attackAnim) {
                    event.getController().markNeedsReload();
                }
                event.getController().setAnimation(attackAnim);
                return PlayState.CONTINUE;
            }
        }
        if (hurtAnim != null) {
            if (event.getController().getAnimationState() == AnimationState.Stopped) {
                hurtAnim = null;
            } else {
                if (event.getController().currentAnimationBuilder != hurtAnim) {
                    event.getController().markNeedsReload();
                }
                event.getController().setAnimation(hurtAnim);
                return PlayState.CONTINUE;
            }
        }
        if (!event.isMoving() || walkAnimName.isEmpty()) {
            if (!idleAnimName.isEmpty()) {
                event.getController().setAnimation(new AnimationBuilder().loop(idleAnimName));
            } else {
                return PlayState.STOP;
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop(walkAnimName));
        }
        return PlayState.CONTINUE;
    }

    public void setDialogAnim(String name) {
        dialogAnim = new AnimationBuilder().playOnce(name);
    }
    public void activateReceivedAnim(EnumSyncAutoAnim type) {
        switch (type) {
            case HURT -> {
                if(!hurtAnimName.isEmpty())
                    hurtAnim = new AnimationBuilder().playOnce(hurtAnimName);
            }
            case MELEE_ATTACK -> {
                if(!meleeAttackAnimName.isEmpty())
                    attackAnim = new AnimationBuilder().playOnce(meleeAttackAnimName);
            }
            case RANGED_ATTACK -> {
                if(!rangedAttackAnimName.isEmpty())
                    attackAnim = new AnimationBuilder().playOnce(rangedAttackAnimName);
            }
        }
    }

    public EntityCustomModel(World worldIn) {
        super(worldIn);
        this.ignoreFrustumCheck = true;
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.setSize(0.7F, 2F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "movement", 10, this::predicateMovement));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return ticksExisted;
    }

    @Override
    public void tick() {
        super.onUpdate();
    }
}
