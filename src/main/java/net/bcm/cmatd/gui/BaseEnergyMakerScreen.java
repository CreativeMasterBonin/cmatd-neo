package net.bcm.cmatd.gui;

import net.bcm.cmatd.Utility;
import net.bcm.cmatd.blockentity.BaseEnergyMakerBE;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class BaseEnergyMakerScreen extends AbstractContainerScreen<BaseEnergyMakerMenu>{
    private static final int ENERGY_LEFT = 64;
    private static final int ENERGY_WIDTH = 81;
    private static final int ENERGY_TOP = 42;
    private static final int ENERGY_HEIGHT = 6;
    private static final ResourceLocation BG = ResourceLocation.parse("cmatd:textures/gui/base_generator.png");

    public BaseEnergyMakerScreen(BaseEnergyMakerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 110;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(BG, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int power = menu.getEnergy();
        int p = (int) ((power / (float) BaseEnergyMakerBE.CAPACITY) * ENERGY_WIDTH);
        graphics.fillGradient(leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + p, topPos + ENERGY_TOP + ENERGY_HEIGHT, Utility.BRIGHT_LIGHT_BLUE, Utility.DARKER_BLUE);
        graphics.fill(leftPos + ENERGY_LEFT + p, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT, Utility.DARKEST_GRAYER_BLUE);
    }

    @Override
    public void render(GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        // Render tooltip with power if in the energy box
        if (mousex >= leftPos + ENERGY_LEFT && mousex < leftPos + ENERGY_LEFT + ENERGY_WIDTH && mousey >= topPos + ENERGY_TOP && mousey < topPos + ENERGY_TOP + ENERGY_HEIGHT) {
            int power = menu.getEnergy();
            List<Component> components = List.of(
                    Component.translatable("title.energy_fe_with_max",power,menu.getBlockEntity().getMaxEnergy()),
                    Component.translatable("title.burn_time_left",menu.getBurnTime())
            );
            graphics.renderComponentTooltip(this.font,components,mousex,mousey);
        }
        else{
            this.renderTooltip(graphics, mousex, mousey);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, Utility.INT_WHITE, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, Utility.INT_WHITE, false);
    }
}
