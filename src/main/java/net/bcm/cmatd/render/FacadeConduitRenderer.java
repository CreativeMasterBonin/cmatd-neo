package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bcm.cmatd.blockentity.FacadeConduitBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

public class FacadeConduitRenderer implements BlockEntityRenderer<FacadeConduitBE> {
    public FacadeConduitRenderer(BlockEntityRendererProvider.Context berp) {}

    @Override
    public void render(FacadeConduitBE blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.0f,0.0f,0.0f);
        poseStack.scale(1f,1f,1f);
        if(blockEntity.facadeState == null){

        }
        else{
            if(blockEntity.facadeState.getBlock() != Blocks.AIR){
                if(!(blockEntity.facadeState.getBlock() instanceof EntityBlock) && !(blockEntity.facadeState.getBlock() instanceof GameMasterBlock)) {
                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockEntity.facadeState,
                            poseStack, bufferSource, packedLight, packedOverlay, ModelData.EMPTY, RenderType.CUTOUT);
                }
            }
        }
        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 72;
    }

    @Override
    public boolean shouldRenderOffScreen(FacadeConduitBE blockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(FacadeConduitBE be, Vec3 vec3) {
        return Vec3.atCenterOf(be.getBlockPos()).multiply(2.0, 2.0, 2.0)
                .closerThan(vec3.multiply(2.0, 2.0, 2.0), (double)this.getViewDistance());
    }
}
