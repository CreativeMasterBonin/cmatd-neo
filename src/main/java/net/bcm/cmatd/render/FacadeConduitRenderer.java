package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bcm.cmatd.blockentity.FacadeConduitBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.Tags;

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
            try{
                if(blockEntity.facadeState.getBlock() != Blocks.AIR){
                    if(!(blockEntity.facadeState.getBlock() instanceof EntityBlock) && !(blockEntity.facadeState.getBlock() instanceof GameMasterBlock)) {
                        if(blockEntity.facadeState.getBlock() instanceof StainedGlassBlock || blockEntity.facadeState.getBlock() instanceof StainedGlassPaneBlock || blockEntity.facadeState.getBlock() instanceof TintedGlassBlock || blockEntity.facadeState.getBlock() instanceof SlimeBlock || blockEntity.facadeState.getBlock() instanceof HoneyBlock){
                            Minecraft.getInstance().getBlockRenderer().renderBatched(blockEntity.facadeState,
                                    blockEntity.getBlockPos(),blockEntity.getLevel(),poseStack,
                                    bufferSource.getBuffer(RenderType.TRANSLUCENT),true,blockEntity.getLevel().getRandom());
                        }
                        else if(blockEntity.facadeState.is(Tags.Blocks.GLASS_BLOCKS_COLORLESS) || blockEntity.facadeState.is(Tags.Blocks.GLASS_PANES_COLORLESS) || !blockEntity.facadeState.isSolidRender(blockEntity.getLevel(),blockEntity.getBlockPos()) || blockEntity.facadeState.is(Blocks.GRASS_BLOCK)){
                            Minecraft.getInstance().getBlockRenderer().renderBatched(blockEntity.facadeState,
                                    blockEntity.getBlockPos(),blockEntity.getLevel(),poseStack,
                                    bufferSource.getBuffer(RenderType.CUTOUT),true,blockEntity.getLevel().getRandom());
                        }
                        else{
                            Minecraft.getInstance().getBlockRenderer().renderBatched(blockEntity.facadeState,
                                    blockEntity.getBlockPos(),blockEntity.getLevel(),poseStack,
                                    bufferSource.getBuffer(RenderType.SOLID),true,blockEntity.getLevel().getRandom());
                        }
                    }
                }
            }
            catch (Exception e){}
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
