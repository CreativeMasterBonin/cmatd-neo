package net.bcm.cmatd.integration;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.gui.FluidTankGUIRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;
import snownee.jade.api.ui.Element;

/**
 * A custom Element type that implements
 */
public class SpriteElementGas extends Element {
    private final ResourceLocation sprite;
    private final int width;
    private final int height;
    private final GasType gas;
    private final ResourceLocation blockLocation = ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png-atlas");
    private final boolean useCustomLocation;

    public SpriteElementGas(ResourceLocation location, int width, int height, GasType gas){
        this.sprite = location;
        this.width = width;
        this.height = height;
        this.gas = gas;
        this.useCustomLocation = false;
    }

    public SpriteElementGas(boolean customLocation, ResourceLocation location, int width, int height, GasType gas){
        this.sprite = location;
        this.width = width;
        this.height = height;
        this.gas = gas;
        this.useCustomLocation = customLocation;
    }

    public SpriteElementGas(){
        this.sprite = blockLocation;
        this.width = 16;
        this.height = 16;
        this.gas = Gases.EMPTY;
        this.useCustomLocation = false;
    }

    @Override
    public Vec2 getSize() {
        return new Vec2((float)this.width, (float)this.height);
    }

    /**
     * Render the gas using vanilla water textures and the custom FluidRenderer for GUIs
     * @param guiGraphics the graphics that all render methods use
     * @param x x pos of image
     * @param y y pos of image
     * @param maxX
     * @param maxY
     */
    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        int redColor = Utility.intToRGB(gas.getColor(),0);
        int greenColor = Utility.intToRGB(gas.getColor(),1);
        int blueColor = Utility.intToRGB(gas.getColor(),2);

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

        ResourceLocation fluidStill = IClientFluidTypeExtensions.of(Fluids.WATER).getStillTexture();
        if(useCustomLocation){
            fluidStill = sprite;
        }

        if(fluidStill == null){
            fluidStill = MissingTextureAtlasSprite.getLocation();
        }
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.setShaderColor(clampedRed,
                clampedGreen,
                clampedBlue,
                1.0f);
        Matrix4f matrix = guiGraphics.pose().last().pose();

        RenderSystem.enableBlend();
        FluidTankGUIRenderer.drawTextureWithMasking(matrix,
                x,y,
                fluidStillSprite,
                16 - this.height,16 - this.width,
                100);

        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.disableBlend();

        // old code that DOESN'T work properly
            /*
            RenderSystem.setShaderTexture(0,InventoryMenu.BLOCK_ATLAS);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(clampedRed,
                    clampedGreen,
                    clampedBlue,
                    1.0f);

            ResourceLocation textureLocation = IClientFluidTypeExtensions.of(Fluids.WATER).getFlowingTexture();
            TextureAtlasSprite fluidSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(
                    textureLocation);


            RenderSystem.enableBlend();
            guiGraphics.blit(fluidSprite.atlasLocation(),
                    Math.round(x),Math.round(y),
                    16,16,
                    0,0,
                    Math.round(this.getCachedSize().x),Math.round(this.getCachedSize().y),
                    this.width,this.height
            );
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();*/
    }
}
