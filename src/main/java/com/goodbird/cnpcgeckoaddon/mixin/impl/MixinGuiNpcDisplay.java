package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.client.gui.SubGuiModelAnimation;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.mainmenu.GuiNpcDisplay;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNpcDisplay.class)
public abstract class MixinGuiNpcDisplay extends GuiNPCInterface2 {

    public MixinGuiNpcDisplay() {
        super(null);
    }

    @Inject(method = "initGui", at = @At("TAIL"))
    public void initGui(CallbackInfo ci){
        int y = 319;
        addLabel(new GuiNpcLabel(212,"Model Animation", guiLeft + 185, y + 5));
        this.addButton(new GuiNpcButton(212, guiLeft + 300, y, 100, 20, "selectServer.edit"));
        if (!((IDataDisplay)npc.display).hasCustomModel()) {
            this.getLabel(212).enabled=false;
            this.getButton(212).setVisible(false);
            this.getButton(212).setEnabled(false);
        }
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    public void actionPerformed(GuiButton guibutton, CallbackInfo ci){
        GuiNpcButton button = (GuiNpcButton) guibutton;
        if(button.id == 212){
            setSubGui(new SubGuiModelAnimation(npc));
        }
    }
}
