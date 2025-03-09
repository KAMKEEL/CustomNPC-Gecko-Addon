package com.goodbird.npcgecko.mixin.impl;

import com.goodbird.npcgecko.client.gui.GuiStringSelection;
import com.goodbird.npcgecko.client.gui.SubGuiModelAnimation;
import com.goodbird.npcgecko.entity.EntityCustomModel;
import com.goodbird.npcgecko.mixin.IDataDisplay;
import kamkeel.npcs.addon.client.GeckoAddonClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.model.GuiCreationScreen;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.model.ModelMPM;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.Vector;

@Mixin(GeckoAddonClient.class)
public class MixinGeckoAddonClient {

    @Shadow(remap = false) public boolean supportEnabled;

    /**
     * @author Goodbird
     * @reason Show Custom Model Selection button for Custom Entities
     */
    @Overwrite(remap = false)
    public void showGeckoButtons(GuiCreationScreen creationScreen, EntityLivingBase entity){
        if(!supportEnabled)
            return;

        if(entity instanceof EntityCustomModel){
            creationScreen.addButton(new GuiNpcButton(202, creationScreen.guiLeft-60, creationScreen.guiTop+40, 180, 20, ((IDataDisplay)creationScreen.npc.display).getCustomModelData().getModel()));
        }
    }

    /**
     * @author Goodbird
     * @reason Add support for new showGeckoButtons() in GuiCreationScreen
     */
    @Overwrite(remap = false)
    public void geckoGuiCreationScreenActionPerformed(GuiCreationScreen creationScreen, GuiNpcButton button){
        if(!supportEnabled)
            return;

        if(button.id == 202){
            Vector<String> list = new Vector<>();
            for(ResourceLocation resLoc : GeckoLibCache.getInstance().getGeoModels().keySet()){
                list.add(resLoc.toString());
            }
            creationScreen.setSubGui(new GuiStringSelection(creationScreen,"Selecting GeckoLib Model:", list, name -> {
                ((IDataDisplay)creationScreen.npc.display).getCustomModelData().setModel(name);
                creationScreen.getButton(202).setDisplayText(name);
            }));
        }
    }

    /**
     * @author Goodbird
     * @reason Init Model Animation Button for NPCs
     */
    @Overwrite(remap = false)
    public void geckoNpcDisplayInitGui(GuiNPCInterface2 gui){
        if(!supportEnabled)
            return;

        int y = gui.guiTop + 188;
        gui.addLabel(new GuiNpcLabel(212,"Model Animation", gui.guiLeft + 185, y + 5));
        gui.addButton(new GuiNpcButton(212, gui.guiLeft + 300, y, 100, 20, "selectServer.edit"));
        if (!((IDataDisplay)gui.npc.display).hasCustomModel()) {
            gui.getLabel(212).enabled=false;
            gui.getButton(212).setVisible(false);
            gui.getButton(212).setEnabled(false);
        }
    }

    /**
     * @author Goodbird
     * @reason Add Support for Model Animation button on Npc Display
     */
    @Overwrite(remap = false)
    public void geckoNpcDisplayActionPerformed(GuiNPCInterface2 gui, GuiNpcButton btn){
        if(!supportEnabled)
            return;

        if(btn.id == 212){
            gui.setSubGui(new SubGuiModelAnimation(gui.npc));
        }
    }

    /**
     * @author KAMKEEEL
     * @reason Check if Entity is a Gecko Model
     */
    @Overwrite(remap = false)
    public boolean isGeckoModel(ModelBase mainModel){
        if(!supportEnabled)
            return false;

        return (mainModel instanceof ModelMPM && ((ModelMPM) mainModel).entity instanceof IAnimatable);
    }

    /**
     * @author Goodbird
     * @reason Render Gecko Model Support
     */
    @Overwrite(remap = false)
    public void geckoRenderModel(ModelMPM mainModel, EntityNPCInterface npc, float rot, float partial){
        GL11.glRotatef(180, 1,0,0);
        GL11.glTranslated(0, -1.5,0);

        (mainModel).entity.renderYawOffset = (mainModel).entity.prevRenderYawOffset = 0;
        if (!npc.isInvisible())
        {
            if(mainModel.entity instanceof EntityCustomModel){
                ((EntityCustomModel)mainModel.entity).isSemiVisible = false;
            }
            RenderManager.instance.renderEntityWithPosYaw(mainModel.entity, 0,0,0,rot,partial);
        }
        else if (!npc.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
        {
            if(mainModel.entity instanceof EntityCustomModel){
                ((EntityCustomModel)mainModel.entity).isSemiVisible = true;
                RenderManager.instance.renderEntityWithPosYaw(mainModel.entity, 0,0,0,rot,partial);
            }else {
                GL11.glPushMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
                RenderManager.instance.renderEntityWithPosYaw(mainModel.entity, 0, 0, 0, rot, partial);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                GL11.glPopMatrix();
                GL11.glDepthMask(true);
            }
        }
    }
}
