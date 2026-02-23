package net.bcm.cmatd.gui;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.network.GasTankUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class GasTankScreen extends AbstractContainerScreen<GasTankMenu>{
    private static final int GAS_LEFT = 70;
    private static final int GAS_TOP = 10;
    private static final int GAS_WIDTH = 36;
    private static final int GAS_HEIGHT = 116;

    private static final ResourceLocation BG = ResourceLocation.parse("cmatd:textures/gui/gas_tank.png");
    private static final ResourceLocation GAS_METER_MARKS = ResourceLocation.parse("cmatd:textures/gui/sprites/gas_meter_marks.png");

    public FluidTankGUIRenderer fluidRenderer;

    public Button dumpGasButton;

    public GasTankScreen(GasTankMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 216;
        this.imageWidth = 176;
    }

    protected void setupWidgets(){
        this.dumpGasButton = new ExtendedButton(this.leftPos + 135,this.topPos + 9,
                32,16,Component.translatable("button.dump_gas"),
                (handler) -> {
                    PacketDistributor.sendToServer(new GasTankUpdate(
                            menu.getGasTankBE().getBlockPos(),true));
                    Minecraft.getInstance().getSoundManager()
                            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK,1.0f));
        });
    }

    @Override
    protected void init() {
        super.init();
        this.fluidRenderer = new FluidTankGUIRenderer(menu.getGasTankBE().getGasTank().getCapacity(),
                GAS_WIDTH, GAS_HEIGHT);
        setupWidgets();
        addRenderableWidget(dumpGasButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BG, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        guiGraphics.blit(GAS_METER_MARKS,
                leftPos + 70, topPos + 10,
                18,110,
                0,0,
                18,110,
                18,110);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if(mouseX >= leftPos + GAS_LEFT && mouseX < leftPos + GAS_LEFT + GAS_WIDTH && mouseY >= topPos + GAS_TOP && mouseY < topPos + GAS_TOP + GAS_HEIGHT){
            int gasAmount = menu.getGasAmount();
            List<Component> components2 = List.of(
                    Component.translatable("title.gas_amt_with_max",gasAmount,
                            menu.getGasTankBE().getGasTank().getCapacity())
            );
            guiGraphics.renderComponentTooltip(this.font,components2,mouseX,mouseY);
        }
        else if(mouseX >= leftPos + 135 && mouseX < leftPos + 135 + 32 && mouseY >= topPos + 9 && mouseY < topPos + 9 + 16){
            List<Component> components2 = List.of(
                    Component.translatable("button.gas_tank.dump.desc")
            );
            guiGraphics.renderComponentTooltip(this.font,components2,mouseX,mouseY);
        }
        else{
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }


        try{
            fluidRenderer.renderGas(guiGraphics,menu.getGasStack(),
                    leftPos + GAS_LEFT,topPos + GAS_TOP);
        }
        catch(Exception e){
            Cmatd.getLogger().error("GasTankScreen encountered an error: {}",e.getMessage());
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title,
                this.imageWidth - 169, this.imageHeight - 210, Utility.INT_WHITE, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle,
                this.imageWidth - 169, this.imageHeight - 100, Utility.INT_WHITE, false);
    }
}
