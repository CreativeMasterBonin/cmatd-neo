package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bcm.cmatd.blockentity.RedstoneDynamoEngineBE;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class RedstoneDynamoEngineModel extends Model{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.parse("cmatd:textures/entity/rs_engine.png"), "main");

    public final ModelPart engine;
    public final ModelPart ring;
    public final ModelPart bellows;

    public RedstoneDynamoEngineModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.engine = root.getChild("engine");
        this.ring = engine.getChild("ring");
        this.bellows = engine.getChild("bellows");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition engine = partdefinition.addOrReplaceChild("engine", CubeListBuilder.create().texOffs(66, 0).addBox(-3.0F, -16.0F, -3.0F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -6.0F, -8.0F, 16.0F, 6.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition ring = engine.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(0, 44).addBox(-8.0F, -10.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bellows = engine.addOrReplaceChild("bellows", CubeListBuilder.create().texOffs(0, 24).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    float preRingY = 0;
    float postRingY = 0;

    public void setupAnim(RedstoneDynamoEngineBE be){
        // was 2.35f and 8.14150f
        preRingY = (float)Math.clamp(Math.sin(be.ticks / 4.50001f) * 3.50001f,-3.1001D,2.50001D);
        postRingY = preRingY - 2.50001f;
        this.ring.y = postRingY;
        this.bellows.yScale = 2f;
        this.bellows.y = ring.y + 12f;
    }

    public void setupAnimSlowDown(RedstoneDynamoEngineBE be){
        preRingY = 0;
        postRingY = 0;
        this.bellows.yScale = 0;
        this.bellows.y = 0;
        this.ring.y = 0;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        this.engine.render(poseStack,vertexConsumer,i,i1,i2);
    }
}
