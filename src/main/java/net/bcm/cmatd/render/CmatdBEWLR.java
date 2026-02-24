package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.DieselEngineBE;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CmatdBEWLR extends BlockEntityWithoutLevelRenderer {
    private DieselEngineModel dieselEngineModel;
    private DieselEngineBE dieselEngine;

    public CmatdBEWLR(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.dieselEngineModel = new DieselEngineModel(Minecraft.getInstance().getEntityModels().bakeLayer(DieselEngineModel.LAYER_LOCATION));
        this.dieselEngine = new DieselEngineBE(new BlockPos(0,0,0), CmatdBlock.DIESEL_ENGINE.get().defaultBlockState());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.dieselEngineModel = new DieselEngineModel(Minecraft.getInstance().getEntityModels().bakeLayer(DieselEngineModel.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        VertexConsumer vc;
        if(stack.getItem() == CmatdItem.DIESEL_ENGINE.asItem()){
            poseStack.pushPose();
            float defYPos = -7f;
            vc = buffer.getBuffer(RenderType.entityCutout(DieselEngineModel.LAYER_LOCATION.getModel()));
            this.dieselEngineModel.setupAnim(dieselEngine);

            this.dieselEngineModel.shaftholder.zRot = 0f;
            this.dieselEngineModel.piston.x = defYPos;
            this.dieselEngineModel.piston3.x = defYPos;
            this.dieselEngineModel.piston5.x = defYPos;
            this.dieselEngineModel.piston7.x = defYPos;
            this.dieselEngineModel.piston2.x = defYPos;
            this.dieselEngineModel.piston4.x = defYPos;
            this.dieselEngineModel.piston6.x = defYPos;
            this.dieselEngineModel.piston8.x = defYPos;
            float offsetY = 1.75f;

            this.dieselEngineModel.shaftholder.y = 7.25f;
            this.dieselEngineModel.pistonshaftholder1.y = offsetY;
            this.dieselEngineModel.pistonshaftholder2.y = offsetY;
            this.dieselEngineModel.pistonshaftholder3.y = offsetY;
            this.dieselEngineModel.pistonshaftholder4.y = offsetY;
            this.dieselEngineModel.pistonshaftholder5.y = offsetY;
            this.dieselEngineModel.pistonshaftholder6.y = offsetY;
            this.dieselEngineModel.pistonshaftholder7.y = offsetY;
            this.dieselEngineModel.pistonshaftholder8.y = offsetY;


            if(displayContext.firstPerson()){
                if(displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND){
                    poseStack.translate(-0.35f,-0.1f,0f);
                }
                else if(displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND){
                    poseStack.translate(1.5f,-0.1f,0f);
                }
            }
            else{
                if(displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND){
                    poseStack.scale(0.35f,0.35f,0.35f);
                    poseStack.translate(1.5f,1.4f,1.25f);
                }
                else if(displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND){
                    poseStack.scale(0.35f,0.35f,0.35f);
                    poseStack.translate(1.5f,1.4f,1.25f);
                }
                else if(displayContext == ItemDisplayContext.FIXED){
                    poseStack.scale(0.35f,0.35f,0.35f);
                    poseStack.translate(1.5f,1.0f,1.4f);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180));
                }
                else if(displayContext == ItemDisplayContext.GROUND){
                    poseStack.scale(0.35f,0.35f,0.35f);
                    poseStack.translate(1.5f,1.0f,1.25f);
                }
                else if(displayContext == ItemDisplayContext.GUI){
                    poseStack.mulPose(Axis.XP.rotationDegrees(35));
                    poseStack.mulPose(Axis.YP.rotationDegrees(135));
                    poseStack.scale(0.5f,0.5f,0.5f);
                    poseStack.translate(-0.5f,0.85f,0.75f);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    poseStack.translate(0.1f,0f,-0.12f);
                }
            }

            this.dieselEngineModel.renderToBuffer(poseStack,vc,packedLight,packedOverlay);
            poseStack.popPose();
        }
        else{
            super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}
