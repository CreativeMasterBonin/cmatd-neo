package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bcm.cmatd.block.custom.DieselEngine;
import net.bcm.cmatd.blockentity.DieselEngineBE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;

public class DieselEngineRenderer implements BlockEntityRenderer<DieselEngineBE> {
    private DieselEngineModel model;

    public DieselEngineRenderer(BlockEntityRendererProvider.Context berp){
        model = new DieselEngineModel(berp.bakeLayer(DieselEngineModel.LAYER_LOCATION));
    }

    @Override
    public void render(DieselEngineBE blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5,0.125,0.5);

        float p2 = Mth.sin(2.0f * (blockEntity.ticks + partialTick)) * 2.0f;
        float p3 = -p2;
        p2 = Mth.clamp(p2,0.1f,1.32f);
        p3 = Mth.clamp(p3,0.1f,1.32f);

        float defYPos = -7f;

        if(blockEntity.getBlockState().getValue(BlockStateProperties.POWERED)){
            this.model.setupAnimRunning(blockEntity);
            this.model.shaftholder.zRot = Mth.rotLerp(blockEntity.ticks / 2f,0f,1f);
            this.model.piston.x = p2 - 8f;
            this.model.piston3.x = p2 - 8f;
            this.model.piston5.x = p2 - 8f;
            this.model.piston7.x = p2 - 8f;
            this.model.piston2.x = p3 - 8f;
            this.model.piston4.x = p3 - 8f;
            this.model.piston6.x = p3 - 8f;
            this.model.piston8.x = p3 - 8f;
        }
        else{
            this.model.setupAnim(blockEntity);
            this.model.shaftholder.zRot = 0f;
            this.model.piston.x = defYPos;
            this.model.piston3.x = defYPos;
            this.model.piston5.x = defYPos;
            this.model.piston7.x = defYPos;
            this.model.piston2.x = defYPos;
            this.model.piston4.x = defYPos;
            this.model.piston6.x = defYPos;
            this.model.piston8.x = defYPos;
        }

        float offsetY = 1.75f;

        this.model.shaftholder.y = 7.25f;
        this.model.pistonshaftholder1.y = offsetY;
        this.model.pistonshaftholder2.y = offsetY;
        this.model.pistonshaftholder3.y = offsetY;
        this.model.pistonshaftholder4.y = offsetY;
        this.model.pistonshaftholder5.y = offsetY;
        this.model.pistonshaftholder6.y = offsetY;
        this.model.pistonshaftholder7.y = offsetY;
        this.model.pistonshaftholder8.y = offsetY;

        if(blockEntity.getBlockState().getValue(DieselEngine.FACING) == Direction.NORTH || blockEntity.getBlockState().getValue(DieselEngine.FACING) == Direction.SOUTH){
            poseStack.mulPose(new Quaternionf().rotateY(3.14f));
        }
        else if(blockEntity.getBlockState().getValue(DieselEngine.FACING) == Direction.EAST || blockEntity.getBlockState().getValue(DieselEngine.FACING) == Direction.WEST){
            poseStack.mulPose(new Quaternionf().rotateY(1.58f));
        }

        /*
        if(be.getBlockState().getValue(CleverBlackboard.FACING) == Direction.NORTH){
                poseStack.mulPose(new Quaternionf().rotateY(3.15000000f));
                poseStack.translate(-0.5,0.0,-0.8);
            }
            else if (be.getBlockState().getValue(CleverBlackboard.FACING) == Direction.SOUTH) {
                poseStack.mulPose(new Quaternionf().rotateY(0.0f));
                poseStack.translate(0.5,0.0,0.2);
            }
            else if (be.getBlockState().getValue(CleverBlackboard.FACING) == Direction.EAST) {
                poseStack.mulPose(new Quaternionf().rotateY(1.57000000f));
                poseStack.translate(-0.5,0.0,0.2);
            }
            else if (be.getBlockState().getValue(CleverBlackboard.FACING) == Direction.WEST){
                poseStack.mulPose(new Quaternionf().rotateY(-1.57000000f));
                poseStack.translate(0.5,0.0,-0.8);
            }
         */

        this.model.renderToBuffer(poseStack,
                bufferSource.getBuffer(RenderType.entityCutout(DieselEngineModel.LAYER_LOCATION.getModel()))
                ,packedLight,packedOverlay);
        poseStack.popPose();
    }
}
