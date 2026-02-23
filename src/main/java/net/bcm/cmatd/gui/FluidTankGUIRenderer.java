package net.bcm.cmatd.gui;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.api.GasStack;
import net.bcm.cmatd.api.Gases;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

import java.text.NumberFormat;

/*
The MIT License (MIT)

Copyright (c) 2014-2015 mezz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

// modified for methods not requiring ingredients

public class FluidTankGUIRenderer {
    private static final NumberFormat nf = NumberFormat.getIntegerInstance();
    private static final int TEXTURE_SIZE = 16;
    private static final int MIN_FLUID_HEIGHT = 1;

    private final long capacity;
    private final int width;
    private final int height;

    
    public FluidTankGUIRenderer(long capacity, int width, int height) {
        Preconditions.checkArgument(capacity > 0, "capacity must be > 0");
        Preconditions.checkArgument(width > 0, "width must be > 0");
        Preconditions.checkArgument(height > 0, "height must be > 0");
        this.capacity = capacity;
        this.width = width;
        this.height = height;
    }

    public void render(GuiGraphics guiGraphics, FluidStack fluidStack) {
        render(guiGraphics, fluidStack, 0, 0);
    }

    public void render(GuiGraphics guiGraphics, FluidStack ingredient, int posX, int posY) {
        RenderSystem.enableBlend();

        drawFluid(guiGraphics, width, height, ingredient, posX, posY);

        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.disableBlend();
    }

    public void renderGas(GuiGraphics guiGraphics, GasStack gasStack, int posX, int posY){
        RenderSystem.enableBlend();

        drawGas(guiGraphics, width, height, gasStack, posX, posY);

        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.disableBlend();
    }

    /**
     * Draws a gas using liquid rendering to guigraphics
     * @param guiGraphics
     * @param width
     * @param height
     * @param gasStack
     * @param posX
     * @param posY
     */
    private void drawGas(GuiGraphics guiGraphics, final int width, final int height, GasStack gasStack, int posX, int posY) {
        if (gasStack.is(Gases.EMPTY)) {
            return;
        }

        int fluidColor = Utility.INT_WHITE;

        long amount = gasStack.getAmount();
        long scaledAmount = (amount * height) / capacity;
        if (amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
            scaledAmount = MIN_FLUID_HEIGHT;
        }
        if (scaledAmount > height) {
            scaledAmount = height;
        }

        Fluid fluid = Fluids.WATER;
        ResourceLocation fluidStill = IClientFluidTypeExtensions.of(fluid).getStillTexture();
        if(fluidStill == null){
            fluidStill = MissingTextureAtlasSprite.getLocation();
        }
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);

        int redColor = Utility.intToRGB(gasStack.getGas().getColor(),0);
        int greenColor = Utility.intToRGB(gasStack.getGas().getColor(),1);
        int blueColor = Utility.intToRGB(gasStack.getGas().getColor(),2);

        float redShaderColor = Utility.normalizeIntToFloatValue(
                redColor,
                0,255,
                0.0f,1.0f);

        float greenShaderColor = Utility.normalizeIntToFloatValue(
                greenColor,
                0,255,
                0.0f,1.0f);

        float blueShaderColor = Utility.normalizeIntToFloatValue(
                blueColor,
                0,255,
                0.0f,1.0f);

        float clampedRed = Mth.clamp(redShaderColor,0.0f,1.0f);
        float clampedGreen = Mth.clamp(greenShaderColor,0.0f,1.0f);
        float clampedBlue = Mth.clamp(blueShaderColor,0.0f,1.0f);

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.setShaderColor(clampedRed,
                clampedGreen,
                clampedBlue,
                1.0f);
        RenderSystem.enableBlend();
        drawTiledGas(guiGraphics, width, height, scaledAmount, fluidStillSprite, posX, posY);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.disableBlend();
    }

    private static void drawTiledGas(GuiGraphics guiGraphics, final int tiledWidth, final int tiledHeight, long scaledAmount, TextureAtlasSprite sprite, int posX, int posY) {
        Matrix4f matrix = guiGraphics.pose().last().pose();

        final int xTileCount = tiledWidth / TEXTURE_SIZE;
        final int xRemainder = tiledWidth - (xTileCount * TEXTURE_SIZE);
        final long yTileCount = scaledAmount / TEXTURE_SIZE;
        final long yRemainder = scaledAmount - (yTileCount * TEXTURE_SIZE);

        final int yStart = tiledHeight + posY;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : TEXTURE_SIZE;
                long height = (yTile == yTileCount) ? yRemainder : TEXTURE_SIZE;
                int x = posX + (xTile * TEXTURE_SIZE);
                int y = yStart - ((yTile + 1) * TEXTURE_SIZE);
                if (width > 0 && height > 0) {
                    long maskTop = TEXTURE_SIZE - height;
                    int maskRight = TEXTURE_SIZE - width;

                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    private void drawFluid(GuiGraphics guiGraphics, final int width, final int height, FluidStack fluidStack, int posX, int posY) {
        if (fluidStack.getFluid().isSame(Fluids.EMPTY)) {
            return;
        }

        int fluidColor = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor();

        long amount = fluidStack.getAmount();
        long scaledAmount = (amount * height) / capacity;
        if (amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
            scaledAmount = MIN_FLUID_HEIGHT;
        }
        if (scaledAmount > height) {
            scaledAmount = height;
        }

        Fluid fluid = fluidStack.getFluid();
        ResourceLocation fluidStill = IClientFluidTypeExtensions.of(fluid).getStillTexture();
        if(fluidStill == null){
            fluidStill = MissingTextureAtlasSprite.getLocation();
        }
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);

        drawTiledSprite(guiGraphics, width, height, fluidColor, scaledAmount, fluidStillSprite, posX, posY);
    }

    private static void drawTiledSprite(GuiGraphics guiGraphics, final int tiledWidth, final int tiledHeight, int color, long scaledAmount, TextureAtlasSprite sprite, int posX, int posY) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        setGLColorFromInt(color);

        final int xTileCount = tiledWidth / TEXTURE_SIZE;
        final int xRemainder = tiledWidth - (xTileCount * TEXTURE_SIZE);
        final long yTileCount = scaledAmount / TEXTURE_SIZE;
        final long yRemainder = scaledAmount - (yTileCount * TEXTURE_SIZE);

        final int yStart = tiledHeight + posY;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : TEXTURE_SIZE;
                long height = (yTile == yTileCount) ? yRemainder : TEXTURE_SIZE;
                int x = posX + (xTile * TEXTURE_SIZE);
                int y = yStart - ((yTile + 1) * TEXTURE_SIZE);
                if (width > 0 && height > 0) {
                    long maskTop = TEXTURE_SIZE - height;
                    int maskRight = TEXTURE_SIZE - width;

                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    public static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, long maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax = uMax - (maskRight / 16F * (uMax - uMin));
        vMax = vMax - (maskTop / 16F * (vMax - vMin));

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix, xCoord, yCoord + 16, zLevel).setUv(uMin, vMax);
        bufferBuilder.addVertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).setUv(uMax, vMax);
        bufferBuilder.addVertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).setUv(uMax, vMin);
        bufferBuilder.addVertex(matrix, xCoord, yCoord + maskTop, zLevel).setUv(uMin, vMin);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }
}
