package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IGeckoAnimatable;
import com.goodbird.npcgecko.tile.TileEntityCustomModel;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.blocks.tiles.TileScripted;

/**
 * Implementation of {@link IGeckoAnimatable} that wraps a scripted block's {@link TileEntityCustomModel}.
 * Changes made through this interface are automatically synced to clients.
 *
 * <p>Note: Scripted blocks support a subset of animation features compared to NPCs.
 * Walk, hurt, melee attack, ranged attack animations, head bone name, and transition length
 * are not applicable to blocks and will return default/empty values.</p>
 */
public class GeckoBlockAnimatable implements IGeckoAnimatable {
    private final IBlockScripted block;
    private final TileEntityCustomModel tile;

    public GeckoBlockAnimatable(IBlockScripted block, TileEntityCustomModel tile) {
        this.block = block;
        this.tile = tile;
    }

    private void markDirty() {
        ((TileScripted) block.getMCTileEntity()).needsClientUpdate = true;
    }

    @Override
    public String getModel() {
        return tile.modelResLoc.toString();
    }

    @Override
    public void setModel(String model) {
        tile.modelResLoc = new ResourceLocation(model);
        markDirty();
    }

    @Override
    public String getTexture() {
        return tile.textureResLoc.toString();
    }

    @Override
    public void setTexture(String texture) {
        tile.textureResLoc = new ResourceLocation(texture);
        markDirty();
    }

    @Override
    public String getAnimationFile() {
        return tile.animResLoc.toString();
    }

    @Override
    public void setAnimationFile(String animationFile) {
        tile.animResLoc = new ResourceLocation(animationFile);
        markDirty();
    }

    @Override
    public String getIdleAnimation() {
        return tile.idleAnimName;
    }

    @Override
    public void setIdleAnimation(String animation) {
        tile.idleAnimName = animation;
        markDirty();
    }

    @Override
    public String getWalkAnimation() {
        return "";
    }

    @Override
    public void setWalkAnimation(String animation) {
        // Not applicable to blocks
    }

    @Override
    public String getHurtAnimation() {
        return "";
    }

    @Override
    public void setHurtAnimation(String animation) {
        // Not applicable to blocks
    }

    @Override
    public String getMeleeAttackAnimation() {
        return "";
    }

    @Override
    public void setMeleeAttackAnimation(String animation) {
        // Not applicable to blocks
    }

    @Override
    public String getRangedAttackAnimation() {
        return "";
    }

    @Override
    public void setRangedAttackAnimation(String animation) {
        // Not applicable to blocks
    }

    @Override
    public String getHeadBoneName() {
        return "";
    }

    @Override
    public void setHeadBoneName(String boneName) {
        // Not applicable to blocks
    }

    @Override
    public int getTransitionLengthTicks() {
        return 0;
    }

    @Override
    public void setTransitionLengthTicks(int ticks) {
        // Not applicable to blocks
    }
}
