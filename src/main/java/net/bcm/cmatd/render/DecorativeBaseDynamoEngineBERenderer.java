package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bcm.cmatd.blockentity.DecorativeBaseDynamoEngineBE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DecorativeBaseDynamoEngineBERenderer implements BlockEntityRenderer<DecorativeBaseDynamoEngineBE> {
    private BaseEngineModel model;

    public DecorativeBaseDynamoEngineBERenderer(BlockEntityRendererProvider.Context berp){
        model = new BaseEngineModel(berp.bakeLayer(BaseEngineModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRenderOffScreen(DecorativeBaseDynamoEngineBE blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 64;
    }

    @Override
    public boolean shouldRender(DecorativeBaseDynamoEngineBE be, Vec3 vec3) {
        return Vec3.atCenterOf(be.getBlockPos()).multiply(2.0, 2.0, 2.0)
                .closerThan(vec3.multiply(2.0, 2.0, 2.0), (double)this.getViewDistance());
    }

    @Override
    public void render(DecorativeBaseDynamoEngineBE be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, -0.5, 0.5);
        switch(be.getBlockState().getValue(BlockStateProperties.FACING)){
            case NORTH -> {
                poseStack.mulPose(be.getBlockState().getValue(BlockStateProperties.FACING).getOpposite().getRotation());
                poseStack.translate(0.0,-1.0,-1.0);
            }
            case SOUTH -> {
                poseStack.mulPose(be.getBlockState().getValue(BlockStateProperties.FACING).getOpposite().getRotation());
                poseStack.translate(0.0,-1.0,-1.0);
            }
            case EAST -> {
                poseStack.mulPose(be.getBlockState().getValue(BlockStateProperties.FACING).getOpposite().getRotation());
                poseStack.translate(0.0,-1.0,-1.0);
            }
            case WEST -> {
                poseStack.mulPose(be.getBlockState().getValue(BlockStateProperties.FACING).getOpposite().getRotation());
                poseStack.translate(0.0,-1.0,-1.0);
            }
            case UP -> {
                poseStack.mulPose(be.getBlockState().getValue(BlockStateProperties.FACING).getOpposite().getRotation());
                poseStack.translate(0.0,-2.0,0.0);
            }
        }

        if(be.getBlockState().getValue(BlockStateProperties.LIT)){
            this.model.setupAnim(be);
            this.model.renderToBuffer(poseStack,
                    bufferSource.getBuffer(RenderType.entityCutout(BaseEngineModel.LAYER_LOCATION.getModel()))
                    ,packedLight,packedOverlay);
        }
        else{
            this.model.setupAnimSlowDown(be);
            this.model.renderToBuffer(poseStack,
                    bufferSource.getBuffer(RenderType.entityCutout(BaseEngineModel.LAYER_LOCATION.getModel()))
                    ,packedLight,packedOverlay);
        }
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(DecorativeBaseDynamoEngineBE blockEntity) {
        return new AABB(blockEntity.getBlockPos().getX() - 2, blockEntity.getBlockPos().getY() - 2, blockEntity.getBlockPos().getZ() - 2,
                blockEntity.getBlockPos().getX() + 2, blockEntity.getBlockPos().getY() + 2, blockEntity.getBlockPos().getZ() + 2);
    }
}
