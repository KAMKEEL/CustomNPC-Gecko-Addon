package com.goodbird.npcgecko.client;

import com.goodbird.npcgecko.client.model.ItemModelCustom;
import com.goodbird.npcgecko.data.CustomItemModelData;
import com.goodbird.npcgecko.data.ItemDisplayData;
import com.goodbird.npcgecko.data.ItemDisplayTransform;
import com.goodbird.npcgecko.mixin.IScriptCustomItem;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import org.lwjgl.opengl.GL11;
import scala.tools.nsc.doc.base.comment.Inline;
import software.bernie.geckolib3.item.AnimatableStackWrapper;
import software.bernie.geckolib3.renderers.geo.GeoItemStackRenderer;

public class ModelItemRenderUtil {
    private static final GeoItemStackRenderer RENDERER = new GeoItemStackRenderer(new ItemModelCustom());

    /**
     * Quick check if an ItemStack has a gecko model attached.
     * Used by the shouldUseRenderHelper mixin to route gecko items
     * through the EQUIPPED_BLOCK path in ForgeHooksClient.
     */
    public static boolean hasGeckoModel(ItemStack itemStack) {
        IItemStack iItemStack = NpcAPI.Instance().getIItemStack(itemStack);
        if (!(iItemStack instanceof IItemCustomizable)) return false;
        if (!(iItemStack instanceof IScriptCustomItem)) return false;
        return ((IScriptCustomItem) iItemStack).hasCustomModel();
    }

    /**
     * Attempts to render a gecko model for the given item.
     * Returns true if rendering was handled, false if the item has no gecko model.
     *
     * With shouldUseRenderHelper returning true for EQUIPPED_BLOCK,
     * ForgeHooksClient only applies translate(-0.5, -0.5, -0.5) before calling us.
     * Our per-type defaults below match GeoItemRenderer.renderItem() transforms.
     * GeoItemStackRenderer.render() then applies the standard GeckoLib transforms
     * (translate 0.5,0.5,0.5 + rotate 90 Y) matching GeoItemRenderer.render().
     */
    public static boolean tryRender(ItemRenderType type, ItemStack itemStack) {
        IItemStack iItemStack = NpcAPI.Instance().getIItemStack(itemStack);
        if (!(iItemStack instanceof IItemCustomizable)) return false;
        IItemCustomizable scriptCustomItem = (IItemCustomizable) iItemStack;
        if (!(scriptCustomItem instanceof IScriptCustomItem)) return false;
        IScriptCustomItem geckoItem = (IScriptCustomItem) scriptCustomItem;
        if (!geckoItem.hasCustomModel()) return false;

        CustomItemModelData modelData = geckoItem.getCustomModelData();
        AnimatableStackWrapper wrapper = AnimatableStackWrapper.of(itemStack).withUserData(modelData);
        ItemDisplayTransform transform = resolveTransform(modelData, scriptCustomItem, type);

        // Reset Resetting to the Upper Stack
        GL11.glPushMatrix();
        applyTranslate(transform, type);
        applyRotate(transform, type);
        applyScale(transform, type);

        if(type == ItemRenderType.INVENTORY)
            RenderHelper.disableStandardItemLighting();
        RENDERER.render(wrapper, itemStack);

        if(type == ItemRenderType.INVENTORY)
            RenderHelper.enableStandardItemLighting();


        GL11.glPopMatrix();
        return true;
    }

    public static void applyTranslate(ItemDisplayTransform t, ItemRenderType type) {
        // Inline per-context defaults
        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0.5F, 0.55F, 0.5F);
                break;
            case EQUIPPED:
                GL11.glTranslatef(0F, -0.7F, 0F);
                break;
            case INVENTORY:
                GL11.glTranslatef(0F, 0.05F, 0F);
                break;
            case ENTITY:
                GL11.glTranslatef(0F, 0.0F, 0F);
                break;
            default:
                break;
        }

        if (t != null && t.hasTranslation()) {
            GL11.glTranslatef(t.getTranslateX(), t.getTranslateY(), t.getTranslateZ());
        }
    }

    public static void applyRotate(ItemDisplayTransform t, ItemRenderType type) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                GL11.glRotatef(-45F, 0.0F, 1.0F, 0.0F);
                break;
            case EQUIPPED:
                break;
            case INVENTORY:
                GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(30F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(175F, 0.0F, 1.0F, 0.0F);
                break;
            case ENTITY:
                break;
            default:
                break;
        }

        if (t != null && t.hasRotation()) {
            GL11.glRotatef(t.getRotationY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(t.getRotationX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(t.getRotationZ(), 0.0F, 0.0F, 1.0F);
        }
    }

    public static void applyScale(ItemDisplayTransform t, ItemRenderType type) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                float scale = 2.36f;
                GL11.glScalef(scale, scale, scale);
                break;
            case EQUIPPED:
                //GL11.glScalef(0F, -0.5F, 0F);
                break;
            case INVENTORY:
                scale = 1.65f;
                GL11.glScalef(scale, scale, scale);
                break;
            case ENTITY:
                //GL11.glScalef(0F, -0.5F, 0F);
                break;
            default:
                break;
        }
        if (t != null && t.hasScale()) {
            GL11.glScalef(t.getScaleX(), t.getScaleY(), t.getScaleZ());
        }
        // No per-context default scale — GeoItemRenderer doesn't scale per type
    }

    /**
     * Resolves the display transform for a given render context.
     * Priority:
     *   1) Per-item NBT override (set via setItemDisplay)
     *   2) Display JSON file (set via setDisplayJSON)
     *   3) null — let the apply methods use their inline defaults
     */
    public static ItemDisplayTransform resolveTransform(
            CustomItemModelData modelData, IItemCustomizable item, ItemRenderType type) {

        // 1) Per-item NBT override
        ItemDisplayTransform nbtTransform = null;
        switch (type) {
            case EQUIPPED_FIRST_PERSON: nbtTransform = modelData.getFirstPerson(); break;
            case EQUIPPED:              nbtTransform = modelData.getThirdPerson(); break;
            case INVENTORY:             nbtTransform = modelData.getInventory(); break;
            case ENTITY:                nbtTransform = modelData.getGround(); break;
            default: break;
        }
        if (nbtTransform != null) return nbtTransform;

        // 2) Display JSON file (explicitly assigned via setDisplayJSON)
        String displayFile = modelData.getDisplayFile();
        if (displayFile != null && !displayFile.isEmpty()) {
            ItemDisplayData fileData = ItemDisplayLoader.getInstance().getDisplayData(displayFile);
            if (fileData != null) {
                switch (type) {
                    case EQUIPPED_FIRST_PERSON: return fileData.getFirstPerson();
                    case EQUIPPED:              return fileData.getThirdPerson();
                    case INVENTORY:             return fileData.getInventory();
                    case ENTITY:                return fileData.getGround();
                    default: break;
                }
            }
        }

        // 3) No transform found — apply methods will use inline defaults
        return null;
    }
}
