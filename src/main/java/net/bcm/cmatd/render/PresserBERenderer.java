package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.blockentity.PresserBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PresserBERenderer implements BlockEntityRenderer<PresserBE> {
    public PresserBERenderer(BlockEntityRendererProvider.Context berp){

    }

    @Override
    public void render(PresserBE blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        try{
            if(blockEntity.getLevel() != null){
                poseStack.translate(0.5, 0.38, 0.5);

                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                poseStack.mulPose(Axis.XP.rotationDegrees(90));

                poseStack.scale(0.85f, 0.85f, 0.85f);

                if(!blockEntity.getItemHandler().getStackInSlot(0).isEmpty()){
                    itemRenderer.renderStatic(blockEntity.getItemHandler().getStackInSlot(0).copy(),
                            ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(),blockEntity.getBlockPos()),
                            OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 1);
                }
            }
        }
        catch (Exception e){
            Cmatd.getLogger().error("PresserBE Renderer encountered an exception: {}",e.getMessage());
        }

        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    @Override
    public boolean shouldRender(PresserBE be, Vec3 vec3) {
        return Vec3.atCenterOf(be.getBlockPos()).multiply(2.0, 2.0, 2.0)
                .closerThan(vec3.multiply(2.0, 2.0, 2.0), (double)this.getViewDistance());
    }

    @Override
    public boolean shouldRenderOffScreen(PresserBE blockEntity) {
        return false;
    }

    @Override
    public int getViewDistance() {
        return 16;
    }

    @Override
    public AABB getRenderBoundingBox(PresserBE blockEntity) {
        return new AABB(blockEntity.getBlockPos().getX() - 2, blockEntity.getBlockPos().getY() - 2, blockEntity.getBlockPos().getZ() - 2,
                blockEntity.getBlockPos().getX() + 2, blockEntity.getBlockPos().getY() + 2, blockEntity.getBlockPos().getZ() + 2);
    }
}
