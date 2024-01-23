package noppes.npcs.client.gui.model.custom;

import kamkeel.addon.DataDisplaySupport;
import kamkeel.addon.GeckoAddonSupport;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.DataGecko;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.Collections;
import java.util.Vector;
import java.util.function.Consumer;

import static kamkeel.addon.GeckoAddonSupport.getGeckoData;

public class GuiCustomAnimationSelection extends GuiNPCInterface {
    public GuiNPCStringSlot slot;
    public GuiScreen parent;
    public Consumer<String> action;

    public GuiCustomAnimationSelection(EntityNPCInterface npc, GuiScreen parent, Consumer<String> action){
        super(npc);
        drawDefaultBackground = false;
        title = "";
        this.parent = parent;
        this.action=action;
    }

    @Override
    public void initGui(){
        super.initGui();
        String ss = "Selecting geckolib animation:";
        addLabel(new GuiNpcLabel(0,ss, width / 2 - (this.fontRendererObj.getStringWidth(ss)/2), 20, 0xffffff));
        Vector<String> list = new Vector<String>();

        DataGecko geckoData = (DataGecko) getGeckoData(npc);
        AnimationFile file = GeckoLibCache.getInstance().getAnimations().get(new ResourceLocation(geckoData.animFile));
        if(file!=null) {
            for (Animation anim : file.getAllAnimations()) {
                list.add(anim.animationName);
            }
        }
        Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
        slot = new GuiNPCStringSlot(list,this,false,18);
        slot.registerScrollButtons(4, 5);

        this.addButton(new GuiNpcButton(2, width / 2 - 100, height - 44,98, 20, "gui.back"));
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        slot.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
    }

    @Override
    public void elementClicked(){
    }

    @Override
    public void doubleClicked(){
        action.accept(slot.selected);
        close();
        NoppesUtil.openGUI(player, parent);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        int id = guibutton.id;
        if(id == 2){
            close();
            NoppesUtil.openGUI(player, parent);
        }
    }

    @Override
    public void save() {
    }
}