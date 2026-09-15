package net.bcm.cmatd.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RadioactiveReactorScreen extends AbstractContainerScreen<RadioactiveReactorMenu> {
    public RadioactiveReactorScreen(RadioactiveReactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.translatable("title.radioactive_reactor").withStyle(ChatFormatting.WHITE));
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

    }
}
