package net.bcm.cmatd.gui;

import net.bcm.cmatd.Utility;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class BaseCobbleMakerScreen extends AbstractContainerScreen<BaseCobbleMakerMenu> {
    private static final ResourceLocation BG = ResourceLocation.parse("cmatd:textures/gui/cobble_generator.png");
    private static final int ENERGY_LEFT = 7;
    private static final int ENERGY_WIDTH = 126;
    private static final int ENERGY_TOP = 45;
    private static final int ENERGY_HEIGHT = 5;

    public BaseCobbleMakerScreen(BaseCobbleMakerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.inventoryLabelY = this.imageHeight - 110;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BG, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int power = menu.getEnergy();
        // use the energy value to try to fit the gradient and bg into the screen to prevent runoff of ui elements
        int powerWidth = menu.getEnergy() == Integer.MAX_VALUE ? ENERGY_WIDTH - 2 : Math.max(1,(int)((double)(ENERGY_WIDTH - 2) *
                Utility.divisionDoubleSplit(power,menu.getBlockEntity().getMaxEnergy())));

        //int p = (int) ((power / (float) BaseEnergyMakerBE.CAPACITY) * ENERGY_WIDTH); // old method
        int p = powerWidth;
        guiGraphics.fillGradient(leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + p, topPos + ENERGY_TOP + ENERGY_HEIGHT, Utility.BRIGHT_LIGHT_BLUE, Utility.DARKER_BLUE);
        guiGraphics.fill(leftPos + ENERGY_LEFT + p, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT, Utility.DARKEST_GRAYER_BLUE);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // Render tooltip with power if in the energy box
        if (mouseX >= leftPos + ENERGY_LEFT && mouseX < leftPos + ENERGY_LEFT + ENERGY_WIDTH && mouseY >= topPos + ENERGY_TOP && mouseY < topPos + ENERGY_TOP + ENERGY_HEIGHT) {
            int power = menu.getEnergy();
            int powerMax = menu.getMaxEnergy();
            List<Component> components = List.of(
                    Component.translatable("title.energy_fe_with_max",power,powerMax)
            );
            guiGraphics.renderComponentTooltip(this.font,components,mouseX,mouseY);
        }
        else{
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, Utility.INT_WHITE, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, Utility.INT_WHITE, false);
    }
}
