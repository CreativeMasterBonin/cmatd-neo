package net.bcm.cmatd.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bcm.cmatd.Utility;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class PresserScreen extends AbstractContainerScreen<PresserMenu> {
    public final int PROGRESS_BAR_X = 75;
    public final int PROGRESS_BAR_Y = 23;
    private static final ResourceLocation BG = ResourceLocation.parse("cmatd:textures/gui/pattern_press.png");
    private ResourceLocation progressBarFull = ResourceLocation.parse("cmatd:textures/gui/sprites/press_progress_bar.png");
    private ResourceLocation progressBarFullBad = ResourceLocation.parse("cmatd:textures/gui/sprites/press_progress_bar_bad.png");

    private ResourceLocation noDayNight = ResourceLocation.parse("cmatd:textures/gui/sprites/no_solar_lunar.png");
    private ResourceLocation solar = ResourceLocation.parse("cmatd:textures/gui/sprites/solar.png");
    private ResourceLocation lunar = ResourceLocation.parse("cmatd:textures/gui/sprites/lunar.png");

    public PresserScreen(PresserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BG, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        int length = Mth.ceil(this.menu.processBits * 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(Utility.normalizeIntToFloatValue(this.menu.processBits,0,18,0.0f,1.0f),Utility.normalizeIntToFloatValue(this.menu.processBits,0,18,0.0f,1.0f),Utility.normalizeIntToFloatValue(this.menu.processBits,0,18,0.0f,1.0f),1.0f);
        if(menu.sameItem == 0){
            guiGraphics.blit(this.progressBarFullBad,
                    this.leftPos + PROGRESS_BAR_X - 1, this.topPos + PROGRESS_BAR_Y - 1,
                    0, 0,
                    length,34,18,34);
        }
        else{
            guiGraphics.blit(this.progressBarFull,
                    this.leftPos + PROGRESS_BAR_X - 1, this.topPos + PROGRESS_BAR_Y - 1,
                    0, 0,
                    length, 34,18,34);
        }
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.disableBlend();

        float f = 1.0f - Mth.sin(Util.getMillis() / 360.0f) + 1.0f;
        // solar check
        if(menu.nightMode == 1){
            if(f > 1.5f){
                guiGraphics.blit(lunar,this.leftPos + 28,this.topPos + 35,
                        0,8,
                        8,8,8,8);
            }
            else{
                guiGraphics.blit(solar,this.leftPos + 28,this.topPos + 35,
                        0,8,
                        8,8,8,8);
            }
        }
        else{
            if(menu.solar == 0){
                guiGraphics.blit(noDayNight,this.leftPos + 28,this.topPos + 35,
                        0,8,
                        8,8,8,8);
            }
            else{
                guiGraphics.blit(solar,this.leftPos + 28,this.topPos + 35,
                        0,8,
                        8,8,8,8);
            }
        }
    }

    private static final int SOLAR_LEFT = 28;
    private static final int SOLAR_WIDTH = 8;
    private static final int SOLAR_TOP = 35;
    private static final int SOLAR_HEIGHT = 8;

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (mouseX >= leftPos + SOLAR_LEFT && mouseX < leftPos + SOLAR_LEFT + SOLAR_WIDTH
                && mouseY >= topPos + SOLAR_TOP && mouseY < topPos + SOLAR_TOP + SOLAR_HEIGHT) {
            // if the machine has the night mode upgrade, it will never turn off!
            if(menu.nightMode == 1){
                List<Component> components = List.of(Component.translatable("title.solar_status.always_active")
                        .withColor(Utility.GOOD_STATE_GREEN));
                guiGraphics.renderComponentTooltip(this.font,components,mouseX,mouseY);
            }
            else{
                if(menu.solar == 0){
                    List<Component> components = List.of(Component.translatable("title.solar_status.off")
                            .withColor(Utility.BAD_STATE_RED));
                    guiGraphics.renderComponentTooltip(this.font,components,mouseX,mouseY);
                }
                else{
                    List<Component> components = List.of(Component.translatable("title.solar_status.on")
                            .withColor(Utility.GOOD_STATE_GREEN));
                    guiGraphics.renderComponentTooltip(this.font,components,mouseX,mouseY);
                }
            }
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
