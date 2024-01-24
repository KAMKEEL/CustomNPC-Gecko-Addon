package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.client.gui.GuiStringSelection;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.model.GuiCreationScreen;
import noppes.npcs.client.gui.util.GuiModelInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.Vector;

@Mixin(GuiCreationScreen.class)
public class MixinGuiCreationScreen extends GuiModelInterface {

    public MixinGuiCreationScreen() {
        super(null);
    }

    @Inject(method = "showEntityButtons", at = @At("TAIL"), remap = false)
    private void showEntityButtons(EntityLivingBase entity, CallbackInfo ci) {
        if(EntityList.getEntityString(entity).equals("cnpcgeckoaddon.CustomModelEntity") || EntityList.getEntityString(entity).equals("Geckolib Model")){
            this.addButton(new GuiNpcButton(202, guiLeft-60, this.guiTop+40, 180, 20, ((IDataDisplay)npc.display).getCustomModelData().getModel()));
        }
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"), remap = false)
    protected void actionPerformed(GuiButton btn, CallbackInfo ci) {
        GuiNpcButton button = (GuiNpcButton) btn;
        if(button.id == 202){
            Vector<String> list = new Vector<>();
            for(ResourceLocation resLoc : GeckoLibCache.getInstance().getGeoModels().keySet()){
                list.add(resLoc.toString());
            }
            setSubGui(new GuiStringSelection(this,"Selecting geckolib model:", list, name -> {
                ((IDataDisplay)npc.display).getCustomModelData().setModel(name);
                getButton(202).setDisplayText(name);
            }));
        }
    }
}
