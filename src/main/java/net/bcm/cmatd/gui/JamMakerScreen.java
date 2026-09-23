package net.bcm.cmatd.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bcm.cmatd.Utility;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class JamMakerScreen extends AbstractContainerScreen<JamMakerMenu> {
    public final int PROGRESS_BAR_X = 66;
    public final int PROGRESS_BAR_Y = 41;
    private ResourceLocation sugarSprite = ResourceLocation.parse("cmatd:textures/gui/sprites/dust.png");
    private ResourceLocation jamSprite = ResourceLocation.parse("cmatd:textures/gui/sprites/jar.png");
    private ResourceLocation lockedSlotSprite = ResourceLocation.parse("cmatd:textures/gui/sprites/locked_slot.png");
    private ResourceLocation progressBarFull = ResourceLocation.parse("cmatd:textures/gui/sprites/indent_progress_bar.png");
    private static final ResourceLocation BG = ResourceLocation.parse("cmatd:textures/gui/jam_maker.png");

    private ResourceLocation noDayNight = ResourceLocation.parse("cmatd:textures/gui/sprites/no_solar_lunar.png");
    private ResourceLocation solar = ResourceLocation.parse("cmatd:textures/gui/sprites/solar.png");
    private ResourceLocation lunar = ResourceLocation.parse("cmatd:textures/gui/sprites/lunar.png");

    public JamMakerScreen(JamMakerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BG, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        // item placement sprites
        guiGraphics.blit(this.sugarSprite,this.leftPos + 26 + 2,this.topPos + 28,0,0,
                12,11,12,11);
        guiGraphics.blit(this.jamSprite,this.leftPos + 26 + 3,this.topPos + 44,0,0,
                10,14,10,14);

        // process bar
        int length = Mth.ceil(this.menu.processBits * 1.0f);
        // alpha calculation
        float alpha = Utility.normalizeIntToFloatValue(this.menu.processBits,0,Mth.clamp(this.menu.maxProcessBits <= 0 ? 50 : this.menu.maxProcessBits,1,50),0.0f,1.0f);
        // enable blending in the rendering system
        RenderSystem.enableBlend();
        // set the color for the next rendered thing
        RenderSystem.setShaderColor(0.25f,Mth.clamp(alpha + 0.5f,0.5f,1.0f),alpha,1.0f);
        guiGraphics.blit(this.progressBarFull,
                this.leftPos + PROGRESS_BAR_X - 1, this.topPos + PROGRESS_BAR_Y - 1,
                0, 0,
                length, 4,50,4);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.disableBlend();

        /*guiGraphics.blit(this.lockedSlotSprite,this.leftPos + 151,this.topPos + 25,0,0,
                18,18,18,18);
        guiGraphics.blit(this.lockedSlotSprite,this.leftPos + 151,this.topPos + 25 + 18,0,0,
                18,18,18,18);
        guiGraphics.blit(this.lockedSlotSprite,this.leftPos + 151,this.topPos + 25 + 18 + 18,0,0,
                18,18,18,18);*/

        float f = 1.0f - Mth.sin(Util.getMillis() / 360.0f) + 1.0f;
        // solar check
        if(menu.nightMode == 1){
            if(f > 1.5f){
                guiGraphics.blit(lunar,this.leftPos + 9,this.topPos + 38,
                        0,8,
                        8,8,8,8);
            }
            else{
                guiGraphics.blit(solar,this.leftPos + 9,this.topPos + 38,
                        0,8,
                        8,8,8,8);
            }
        }
        else{
            if(menu.solar == 0){
                guiGraphics.blit(noDayNight,this.leftPos + 9,this.topPos + 38,
                        0,8,
                        8,8,8,8);
            }
            else{
                guiGraphics.blit(solar,this.leftPos + 9,this.topPos + 38,
                        0,8,
                        8,8,8,8);
            }
        }
    }

    private static final int SOLAR_LEFT = 9;
    private static final int SOLAR_WIDTH = 8;
    private static final int SOLAR_TOP = 38;
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
        else if(mouseX >= leftPos + PROGRESS_BAR_X && mouseX < leftPos + PROGRESS_BAR_X + 50
                && mouseY >= topPos + PROGRESS_BAR_Y && mouseY < topPos + PROGRESS_BAR_Y + 4){
            List<Component> components = List.of(Component.translatable("menu.cmatd.machine.progress.value_with_max",this.menu.processBits,this.menu.maxProcessBits));
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
