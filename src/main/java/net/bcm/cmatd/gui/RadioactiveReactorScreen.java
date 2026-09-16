package net.bcm.cmatd.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bcm.cmatd.Utility;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class RadioactiveReactorScreen extends AbstractContainerScreen<RadioactiveReactorMenu> {
    private static final ResourceLocation BG = ResourceLocation.parse("cmatd:textures/gui/radioactive_reactor.png");
    private ResourceLocation GAS_METER_MARKS = ResourceLocation.parse("cmatd:textures/gui/sprites/gas_meter_marks.png");
    public static final int gasMeterMarksWidth = 18;
    public static final int gasMeterMarksHeight = 110;
    public static final int heatMeterStartBottomX = 15;
    public static final int reactorMetersStartBottomY = 150;
    public static final int coolantMeterStartBottomX = 25;
    public FluidTankGUIRenderer fluidRendererForGas;
    public int fluidRendererX = 146;
    public int fluidRendererY = 36;
    public int fluidRendererWidth = 20;
    public int fluidRendererHeight = 114;

    public RadioactiveReactorScreen(RadioactiveReactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.translatable("title.radioactive_reactor").withStyle(ChatFormatting.WHITE));
        this.inventoryLabelX = this.leftPos + 8;
        this.inventoryLabelY = this.topPos + 156;
        this.imageHeight = 256;
        this.imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();
        this.fluidRendererForGas = new FluidTankGUIRenderer(1000000,
                fluidRendererWidth, fluidRendererHeight);
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // bilt the background first
        guiGraphics.blit(BG, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        // blit the gas marks sprite next
        guiGraphics.blit(GAS_METER_MARKS,this.leftPos + 146,this.topPos + 38,0,0,
                gasMeterMarksWidth,gasMeterMarksHeight,gasMeterMarksWidth,gasMeterMarksHeight);

        // the heat level converted to a range that a sprite can use in the ui
        int heat = (int)Mth.clamp(Utility.normalizeIntToFloatValue(menu.heat,0,15000,0,126),0,126);
        // the color of the heat level, which is used when the heat level is not higher than 9000
        float hotValue = Mth.clamp(Utility.normalizeIntToFloatValue(menu.heat,0,15000,0.0f,1.0f),0.0f,1.0f);

        RenderSystem.enableBlend();
        if(menu.heat < 9000){
            RenderSystem.setShaderColor(1.0f,1.0f,hotValue,1.0f);
        }
        else{
            RenderSystem.setShaderColor(1.0f,0.0f,0.0f,1.0f);
        }
        guiGraphics.blit(ResourceLocation.parse("cmatd:textures/gui/sprites/reactor_heat_bar.png"),
                this.leftPos + 15,(this.topPos + 150) - heat,
                0,8,
                0,5,heat,
                32,126);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);// reset color to default
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderSlotHighlight(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, float partialTick) {
        super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
        guiGraphics.drawString(this.font,String.valueOf(slot.index),slot.x,slot.y,Utility.INT_WHITE);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if(mouseX >= leftPos + fluidRendererX && mouseX < leftPos + fluidRendererX + fluidRendererWidth && mouseY >= topPos + fluidRendererY && mouseY < topPos + fluidRendererY + fluidRendererHeight){
            int gasAmount = menu.getGasAmount();
            List<Component> components2 = List.of(
                    Component.translatable("title.gas_amt_with_max",gasAmount,
                            menu.wasteCapacity)
            );
            guiGraphics.renderComponentTooltip(this.font,components2,mouseX,mouseY);
        }
        else{
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }

        // render the fluid contents (which in this case is gas)
        if(fluidRendererForGas != null){
            fluidRendererForGas.renderGas(guiGraphics,menu.getGasStack(),
                    leftPos + fluidRendererX,topPos + fluidRendererY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, Utility.INT_WHITE, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, Utility.INT_WHITE, false);

        // test labels
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.heat)),
                this.titleLabelX, this.titleLabelY + 32, Utility.BAD_WARNING_YELLOW, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.coolant)),
                this.titleLabelX + 16, this.titleLabelY + 32, Utility.GOOD_STATE_GREEN, false);
        // gas amount
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.getGasAmount())),
                this.titleLabelX + 142, this.titleLabelY + 32, Utility.hexToInt("0xddeedd"), false);
        // the converted value of gas to fluid
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.getFluidAmount())),
                this.titleLabelX + 128, this.titleLabelY + 32, Utility.hexToInt("0x0044ff"), false);
        // processing bits
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.processBits)),
                this.titleLabelX + 48, this.titleLabelY + 16, Utility.BRIGHT_LIGHT_BLUE, false);
    }
}
