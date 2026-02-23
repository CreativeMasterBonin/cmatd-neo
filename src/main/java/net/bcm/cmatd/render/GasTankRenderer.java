package net.bcm.cmatd.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.blockentity.GasTankBE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GasTankRenderer implements BlockEntityRenderer<GasTankBE> {
    private final BlockRenderDispatcher blockRenderer;
    private GasInWorldModel model;

    public GasTankRenderer(BlockEntityRendererProvider.Context berp){
        this.blockRenderer = berp.getBlockRenderDispatcher();
        this.model = new GasInWorldModel(berp.bakeLayer(GasInWorldModel.LAYER_LOCATION));
    }

    @Override
    public void render(GasTankBE be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        float scale = Utility.normalizeIntToFloatValue(be.getGasAmount(),0,100000,0.0f,1.0f);

        int color = be.getGasTank().gas.getGas().getColor();

        poseStack.translate(0.5,0.5,0.5);
        float scaleCut = scale / 1.75f;
        poseStack.scale(scaleCut,scaleCut,scaleCut);

        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityTranslucent(GasInWorldModel.LAYER_LOCATION.getModel()));

        this.model.setupAnim(be);
        this.model.renderToBuffer(poseStack,vc,packedLight,packedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.scale(0.5f,0.5f,0.5f);
        poseStack.translate(0.5f,0.0f,0.5f);
        BeaconRenderer.renderBeaconBeam(poseStack,
                bufferSource,
                ResourceLocation.parse("minecraft:textures/block/white_concrete.png"),
                partialTick,1.0f,
                be.getLevel().getGameTime(),
                0,2,
                color,
                0.75f,0.78f);
        poseStack.popPose();
    }

    private void setColors(GasTankBE be){
        int redColor = Utility.intToRGB(be.getGasTank().gas.getGas().getColor(),0);
        int greenColor = Utility.intToRGB(be.getGasTank().gas.getGas().getColor(),1);
        int blueColor = Utility.intToRGB(be.getGasTank().gas.getGas().getColor(),2);

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

        RenderSystem.setShaderColor(clampedRed,clampedGreen,clampedBlue,1.0f);
    }

    @Override
    public int getViewDistance() {
        return 32;
    }

    @Override
    public boolean shouldRenderOffScreen(GasTankBE blockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(GasTankBE be, Vec3 vec3) {
        return Vec3.atCenterOf(be.getBlockPos()).multiply(2.0, 2.0, 2.0)
                .closerThan(vec3.multiply(2.0, 2.0, 2.0), (double)this.getViewDistance());
    }

    @Override
    public AABB getRenderBoundingBox(GasTankBE blockEntity) {
        return new AABB(blockEntity.getBlockPos().getX() - 2, blockEntity.getBlockPos().getY() - 2, blockEntity.getBlockPos().getZ() - 2,
                blockEntity.getBlockPos().getX() + 2, blockEntity.getBlockPos().getY() + 2, blockEntity.getBlockPos().getZ() + 2);
    }
}
