package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IGeckoAnimatable;
import com.goodbird.npcgecko.data.CustomModelData;
import com.goodbird.npcgecko.mixin.IDataDisplay;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

/**
 * Implementation of {@link IGeckoAnimatable} that wraps an NPC's {@link CustomModelData}.
 * Changes made through this interface are automatically synced to clients.
 */
public class GeckoNpcAnimatable implements IGeckoAnimatable {
    private final ICustomNpc<EntityNPCInterface> npc;
    private final CustomModelData data;

    public GeckoNpcAnimatable(ICustomNpc<EntityNPCInterface> npc) {
        this.npc = npc;
        this.data = ((IDataDisplay) npc.getMCEntity().display).getCustomModelData();
    }

    @Override
    public String getModel() {
        return data.getModel();
    }

    @Override
    public void setModel(String model) {
        data.setModel(model);
        npc.updateClient();
    }

    @Override
    public String getTexture() {
        return npc.getMCEntity().display.texture;
    }

    @Override
    public void setTexture(String texture) {
        npc.getMCEntity().display.texture = texture;
        npc.updateClient();
    }

    @Override
    public String getAnimationFile() {
        return data.getAnimFile();
    }

    @Override
    public void setAnimationFile(String animationFile) {
        data.setAnimFile(animationFile);
        npc.updateClient();
    }

    @Override
    public String getIdleAnimation() {
        return data.getIdleAnim();
    }

    @Override
    public void setIdleAnimation(String animation) {
        data.setIdleAnim(animation);
        npc.updateClient();
    }

    @Override
    public String getWalkAnimation() {
        return data.getWalkAnim();
    }

    @Override
    public void setWalkAnimation(String animation) {
        data.setWalkAnim(animation);
        npc.updateClient();
    }

    @Override
    public String getHurtAnimation() {
        return data.getHurtAnim();
    }

    @Override
    public void setHurtAnimation(String animation) {
        data.setHurtAnim(animation);
        npc.updateClient();
    }

    @Override
    public String getMeleeAttackAnimation() {
        return data.getMeleeAttackAnim();
    }

    @Override
    public void setMeleeAttackAnimation(String animation) {
        data.setMeleeAttackAnim(animation);
        npc.updateClient();
    }

    @Override
    public String getRangedAttackAnimation() {
        return data.getRangedAttackAnim();
    }

    @Override
    public void setRangedAttackAnimation(String animation) {
        data.setRangedAttackAnim(animation);
        npc.updateClient();
    }

    @Override
    public String getHeadBoneName() {
        return data.getHeadBoneName();
    }

    @Override
    public void setHeadBoneName(String boneName) {
        data.setHeadBoneName(boneName);
        npc.updateClient();
    }

    @Override
    public int getTransitionLengthTicks() {
        return data.getTransitionLengthTicks();
    }

    @Override
    public void setTransitionLengthTicks(int ticks) {
        data.setTransitionLengthTicks(ticks);
        npc.updateClient();
    }
}
