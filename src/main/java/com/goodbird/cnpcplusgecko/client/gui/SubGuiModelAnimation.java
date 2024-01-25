package com.goodbird.cnpcplusgecko.client.gui;

import com.goodbird.cnpcplusgecko.data.CustomModelData;
import com.goodbird.cnpcplusgecko.mixin.IDataDisplay;
import com.goodbird.cnpcplusgecko.utils.AnimationFileUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class SubGuiModelAnimation extends SubGuiInterface implements ITextfieldListener, ISubGuiListener {
    public SubGuiModelAnimation(EntityNPCInterface npc){
        this.npc = npc;
        closeOnEsc = true;
    }

    @Override
    public void initGui() {
        super.initGui();
        int y = guiTop + 44;
        addSelectionBlock(1,y,"Animation File:",getModelData(npc).getAnimFile());
        addSelectionBlock(2,y+=23,"Idle:",getModelData(npc).getIdleAnim());
        addSelectionBlock(3,y+=23,"Walk:",getModelData(npc).getWalkAnim());
        addSelectionBlock(4,y+=23,"Attack:",getModelData(npc).getAttackAnim());
        addSelectionBlock(5,y+23,"Hurt:",getModelData(npc).getHurtAnim());
        addButton(new GuiNpcButton(670, width - 22, 2, 20, 20, "X"));
    }

    public void addSelectionBlock(int id, int y, String label, String value){
        addLabel(new GuiNpcLabel(id,label, guiLeft - 85, y + 5,0xffffff));
        addTextField(new GuiNpcTextField(id,this, fontRendererObj, guiLeft - 10, y, 200, 20, value));
        this.addButton(new GuiNpcButton(id, guiLeft + 193, y, 80, 20, "mco.template.button.select"));
    }

    public CustomModelData getModelData(EntityNPCInterface npc){
        return ((IDataDisplay)npc.display).getCustomModelData();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if(button.id == 670){
            close();
        }
        if(button.id==1){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib animation file:",
                AnimationFileUtil.getAnimationFileList(), (name)-> getModelData(npc).setAnimFile(name)));
        }
        if(button.id==2){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib idle animation:",
                AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                (name)-> getModelData(npc).setIdleAnim(name)));
        }
        if(button.id==3){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib walk animation:",
                AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                (name)-> getModelData(npc).setWalkAnim(name)));
        }
        if(button.id==4){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib attack animation:",
                AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                (name)-> getModelData(npc).setAttackAnim(name)));
        }
        if(button.id==5){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib hurt animation:",
                AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                (name)-> getModelData(npc).setHurtAnim(name)));
        }
    }

    public boolean isValidAnimFile(String name){
        return GeckoLibCache.getInstance().getAnimations().containsKey(new ResourceLocation(name));
    }

    public boolean isValidAnimation(String name){
        return AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()).contains(name);
    }

    @Override
    public void unFocused(GuiNpcTextField textfield) {
        if(textfield.id == 1 && isValidAnimFile(textfield.getText())){
            if(!textfield.isEmpty())
                getModelData(npc).setAnimFile(textfield.getText());
            else
                textfield.setText(getModelData(npc).getAnimFile());
        }
        if(textfield.id == 2 && isValidAnimation(textfield.getText())){
            if(!textfield.isEmpty())
                getModelData(npc).setIdleAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getIdleAnim());
        }
        if(textfield.id == 3 && isValidAnimation(textfield.getText())){
            if(!textfield.isEmpty())
                getModelData(npc).setWalkAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getWalkAnim());
        }
        if(textfield.id == 4 && isValidAnimation(textfield.getText())){
            if(!textfield.isEmpty())
                getModelData(npc).setAttackAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getAttackAnim());
        }
        if(textfield.id == 5 && isValidAnimation(textfield.getText())){
            if(!textfield.isEmpty())
                getModelData(npc).setHurtAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getHurtAnim());
        }
    }

    @Override
    public void subGuiClosed(SubGuiInterface subGuiInterface) {
        initGui();
    }
}
