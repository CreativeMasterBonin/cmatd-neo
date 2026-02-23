package net.bcm.cmatd.gui;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.blockentity.BaseEnergyMakerBE;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class FoodReactorScreen extends AbstractContainerScreen<FoodReactorMenu> {
    private static final int ENERGY_LEFT = 7;
    private static final int ENERGY_WIDTH = 121;
    private static final int ENERGY_TOP = 90;
    private static final int ENERGY_HEIGHT = 4;

    private static final int LIQUID_LEFT = 132;
    private static final int LIQUID_TOP = 16;
    private static final int LIQUID_BOTTOM = 116;
    private static final int LIQUID_RIGHT = 170;
    private static final int LIQUID_WIDTH = 38;
    private static final int LIQUID_HEIGHT = 100;

    private static final ResourceLocation BG = ResourceLocation.parse("cmatd:textures/gui/food_reactor.png");
    private static final ResourceLocation WHITE_BOX = ResourceLocation.parse("minecraft:textures/block/white_stained_glass.png");

    private static final ResourceLocation REACTOR_HEAT = ResourceLocation.parse("cmatd:textures/gui/sprites/reactor_heat_bar.png");

    public FluidTankGUIRenderer fluidRenderer;

    public FoodReactorScreen(FoodReactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 216;
        this.imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();
        this.fluidRenderer = new FluidTankGUIRenderer(Utility.FOOD_REACTOR_FLUID_CAPACITY,
                LIQUID_WIDTH,LIQUID_HEIGHT);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BG, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int power = menu.getEnergy();
        int p = (int) ((power / (float) BaseEnergyMakerBE.CAPACITY) * ENERGY_WIDTH);

        int powerWidth = menu.getEnergy() == Integer.MAX_VALUE ? ENERGY_WIDTH - 2 : Math.max(1,(int)((double)(ENERGY_WIDTH - 2) *
                Utility.divisionDoubleSplit(power,menu.getBlockEntity().getEnergyStorage().getMaxEnergyStored())));

        p = powerWidth;

        guiGraphics.fillGradient(leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + p, topPos + ENERGY_TOP + ENERGY_HEIGHT, Utility.BRIGHT_LIGHT_BLUE, Utility.DARKER_BLUE);
        guiGraphics.fill(leftPos + ENERGY_LEFT + p, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT, Utility.DARKEST_GRAYER_BLUE);

        int progressTicks = Mth.ceil(menu.progress * 1.0F);

        guiGraphics.blit(REACTOR_HEAT,this.leftPos + 72,this.topPos + 44,
                0,0,
                0,32,progressTicks,
                32,32);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (mouseX >= leftPos + ENERGY_LEFT && mouseX < leftPos + ENERGY_LEFT + ENERGY_WIDTH && mouseY >= topPos + ENERGY_TOP && mouseY < topPos + ENERGY_TOP + ENERGY_HEIGHT) {
            int power = menu.getEnergy();
            List<Component> components = List.of(
                    Component.translatable("title.energy_fe_with_max",power,menu.getBlockEntity().energyStorage.getMaxEnergyStored())
            );
            guiGraphics.renderComponentTooltip(this.font,components,mouseX,mouseY);
        }
        else if(mouseX >= leftPos + LIQUID_LEFT && mouseX < leftPos + LIQUID_LEFT + LIQUID_WIDTH && mouseY >= topPos + LIQUID_TOP && mouseY < topPos + LIQUID_TOP + LIQUID_HEIGHT){
            int liquidAmount = menu.getFluidAmount();
            List<Component> components2 = List.of(
                    Component.translatable("title.liquid_amt_with_max",liquidAmount,
                            Utility.FOOD_REACTOR_FLUID_CAPACITY)
            );
            guiGraphics.renderComponentTooltip(this.font,components2,mouseX,mouseY);
        }
        else{
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }

        //
        try{
            fluidRenderer.render(guiGraphics,menu.getFluidStack(),
                    leftPos + LIQUID_LEFT,topPos + LIQUID_TOP);
        }
        catch(Exception e){
            Cmatd.getLogger().error("FoodReactorScreen encountered an error: {}",e.getMessage());
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
