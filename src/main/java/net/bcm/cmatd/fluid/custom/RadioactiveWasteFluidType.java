package net.bcm.cmatd.fluid.custom;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Utility;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class RadioactiveWasteFluidType extends CustomFluidType {
    public RadioactiveWasteFluidType(Properties properties) {
        super(properties);
    }

    public ResourceLocation getStillTexture(){
        return ResourceLocation.parse("cmatd:textures/block/radioactive_waste_still");
    }

    // if the fluid updates past 1.21, remove this and integrate new version
    @SuppressWarnings("removal")
    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            public final ResourceLocation stillTexture = ResourceLocation.parse("cmatd:block/radioactive_waste_still");
            public final ResourceLocation flowingTexture = ResourceLocation.parse("cmatd:block/radioactive_waste_flow");
            public final ResourceLocation overlay = ResourceLocation.parse("cmatd:textures/misc/radioactive_waste_overlay");
            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"textures/block/radioactive_waste_particle.png");
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"textures/block/radioactive_waste_particle.png");
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return new Vector3f(0.2f,0.22f,0.11f);
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                /*int red = FastColor.ARGB32.red(Integer.decode("0x5D6D42dd"));
                int green = FastColor.ARGB32.green(Integer.decode("0x5D6D42dd"));
                int blue = FastColor.ARGB32.blue(Integer.decode("0x5D6D42dd"));
                int alpha = FastColor.ARGB32.alpha(Integer.decode("0x5D6D42dd"));*/
                RenderSystem.setShaderFogStart(1F);
                RenderSystem.setShaderFogEnd(10F); // fog starts at this distance
            }

            @Override
            public int getTintColor() {
                return Utility.hexToInt("0xFFFFFF"); //Utility.hexToInt("0x5D6D42")
            }
        });
    }
}
