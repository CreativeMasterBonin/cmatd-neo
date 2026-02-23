package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bcm.cmatd.blockentity.WindGeneratorBE;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class WindGeneratorModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.parse("cmatd:textures/entity/wind_generator.png"), "main");
    private final ModelPart base;
    public final ModelPart spinner;

    public WindGeneratorModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.base = root.getChild("base");
        this.spinner = this.base.getChild("spinner");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 3).addBox(-8.0F, 6.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.99F, -6.0F, -2.01F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition spinner = base.addOrReplaceChild("spinner", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -21.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-12.0F, -20.0F, -1.0F, 24.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition spinnypart2_r1 = spinner.addOrReplaceChild("spinnypart2_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-12.0F, -7.0F, -1.0F, 24.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition platingtop = spinner.addOrReplaceChild("platingtop", CubeListBuilder.create(), PartPose.offset(-4.0F, -20.5F, 0.0F));

        PartDefinition cube_r1 = platingtop.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(60, 25).addBox(-2.0F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition cube_r2 = platingtop.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(60, 20).addBox(-4.0F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition cube_r3 = platingtop.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 24).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r4 = platingtop.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 24).addBox(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 4.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition platingbottom = spinner.addOrReplaceChild("platingbottom", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -6.5F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition cube_r5 = platingbottom.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(60, 35).addBox(-2.0F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition cube_r6 = platingbottom.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(60, 30).addBox(-4.0F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition cube_r7 = platingbottom.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(60, 40).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r8 = platingbottom.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(60, 47).addBox(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 4.0F, -0.4363F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 80, 64);
    }

    public void setupAnim(WindGeneratorBE be){
        this.base.yRot = 0;
        this.base.xRot = (float)Math.PI;
        this.base.zRot = 0;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.base.render(poseStack,buffer,packedLight,packedOverlay,color);
    }
}
