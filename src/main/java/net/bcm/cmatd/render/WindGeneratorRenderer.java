package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bcm.cmatd.blockentity.WindGeneratorBE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class WindGeneratorRenderer implements BlockEntityRenderer<WindGeneratorBE> {
    private WindGeneratorModel model;

    public WindGeneratorRenderer(BlockEntityRendererProvider.Context berp){
        model = new WindGeneratorModel(berp.bakeLayer(WindGeneratorModel.LAYER_LOCATION));
    }

    @Override
    public void render(WindGeneratorBE be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, -0.25, 0.5);

        this.model.setupAnim(be);
        float piDividend = (float)(Math.PI / 32.0);
        //float additive = Mth.clamp(be.getBlockPos().getY() + piDividend,1.0f,32f);

        poseStack.mulPose(new Quaternionf().rotationY(be.ticks * piDividend));
        this.model.renderToBuffer(poseStack,
                bufferSource.getBuffer(RenderType.entityCutout(WindGeneratorModel.LAYER_LOCATION.getModel()))
                ,packedLight,packedOverlay);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(WindGeneratorBE be, Vec3 vec3) {
        return Vec3.atCenterOf(be.getBlockPos()).multiply(2.0, 2.0, 2.0)
                .closerThan(vec3.multiply(2.0, 2.0, 2.0), (double)this.getViewDistance());
    }

    @Override
    public boolean shouldRenderOffScreen(WindGeneratorBE blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 64;
    }

    @Override
    public AABB getRenderBoundingBox(WindGeneratorBE blockEntity) {
        return new AABB(blockEntity.getBlockPos().getX() - 2, blockEntity.getBlockPos().getY() - 2, blockEntity.getBlockPos().getZ() - 2,
                blockEntity.getBlockPos().getX() + 2, blockEntity.getBlockPos().getY() + 2, blockEntity.getBlockPos().getZ() + 2);
    }
}
