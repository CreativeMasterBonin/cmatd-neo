package net.bcm.cmatd.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bcm.cmatd.blockentity.DieselEngineBE;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DieselEngineModel extends Model{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.parse("cmatd:textures/entity/diesel_engine.png"), "main");

    public final ModelPart baseframe;
    public final ModelPart shaftholder;
    public final ModelPart pistonshaftholder1;
    public final ModelPart piston;
    public final ModelPart pistonshaftholder2;
    public final ModelPart piston2;
    public final ModelPart pistonshaftholder3;
    public final ModelPart piston3;
    public final ModelPart pistonshaftholder4;
    public final ModelPart piston4;
    public final ModelPart pistonshaftholder5;
    public final ModelPart piston5;
    public final ModelPart pistonshaftholder6;
    public final ModelPart piston6;
    public final ModelPart pistonshaftholder7;
    public final ModelPart piston7;
    public final ModelPart pistonshaftholder8;
    public final ModelPart piston8;

    public DieselEngineModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.baseframe = root.getChild("baseframe");
        this.shaftholder = root.getChild("shaftholder");
        this.pistonshaftholder1 = root.getChild("pistonshaftholder1");
        this.piston = this.pistonshaftholder1.getChild("piston");
        this.pistonshaftholder2 = root.getChild("pistonshaftholder2");
        this.piston2 = this.pistonshaftholder2.getChild("piston2");
        this.pistonshaftholder3 = root.getChild("pistonshaftholder3");
        this.piston3 = this.pistonshaftholder3.getChild("piston3");
        this.pistonshaftholder4 = root.getChild("pistonshaftholder4");
        this.piston4 = this.pistonshaftholder4.getChild("piston4");
        this.pistonshaftholder5 = root.getChild("pistonshaftholder5");
        this.piston5 = this.pistonshaftholder5.getChild("piston5");
        this.pistonshaftholder6 = root.getChild("pistonshaftholder6");
        this.piston6 = this.pistonshaftholder6.getChild("piston6");
        this.pistonshaftholder7 = root.getChild("pistonshaftholder7");
        this.piston7 = this.pistonshaftholder7.getChild("piston7");
        this.pistonshaftholder8 = root.getChild("pistonshaftholder8");
        this.piston8 = this.pistonshaftholder8.getChild("piston8");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition baseframe = partdefinition.addOrReplaceChild("baseframe", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 10.0F, -8.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(45, 73).addBox(2.0F, 0.0F, -9.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 73).addBox(2.0F, 0.0F, 7.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, 0.0F, -9.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-4.0F, 0.0F, 7.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(27, 27).addBox(-3.0F, -1.0F, -7.0F, 6.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition shaftholder = partdefinition.addOrReplaceChild("shaftholder", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -3.0F, -7.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition pistonshaftholder1 = partdefinition.addOrReplaceChild("pistonshaftholder1", CubeListBuilder.create().texOffs(57, 44).addBox(-8.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 56).addBox(-8.0F, -4.0F, 2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 82).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(73, 25).addBox(-8.0F, -4.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 71).addBox(-8.0F, -1.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 21.0F, 5.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition piston = pistonshaftholder1.addOrReplaceChild("piston", CubeListBuilder.create().texOffs(65, 12).addBox(-1.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 51).addBox(-2.5F, -2.0F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -2.0F, 0.0F));

        PartDefinition pistonshaftholder2 = partdefinition.addOrReplaceChild("pistonshaftholder2", CubeListBuilder.create().texOffs(19, 56).addBox(-8.0F, -4.0F, -2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 32).addBox(-8.0F, -4.0F, 1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 82).addBox(-1.0F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 67).addBox(-8.0F, -4.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 63).addBox(-8.0F, -1.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 21.0F, 2.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition piston2 = pistonshaftholder2.addOrReplaceChild("piston2", CubeListBuilder.create().texOffs(0, 65).addBox(-1.5F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 42).addBox(-2.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -2.0F, 0.0F));

        PartDefinition pistonshaftholder3 = partdefinition.addOrReplaceChild("pistonshaftholder3", CubeListBuilder.create().texOffs(54, 26).addBox(-8.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).addBox(-8.0F, -4.0F, 2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(81, 80).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(19, 71).addBox(-8.0F, -4.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 70).addBox(-8.0F, -1.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 21.0F, -3.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition piston3 = pistonshaftholder3.addOrReplaceChild("piston3", CubeListBuilder.create().texOffs(36, 62).addBox(-1.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(22, 75).addBox(-2.5F, -2.0F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -2.0F, 0.0F));

        PartDefinition pistonshaftholder4 = partdefinition.addOrReplaceChild("pistonshaftholder4", CubeListBuilder.create().texOffs(38, 50).addBox(-8.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(19, 50).addBox(-8.0F, -4.0F, 2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(76, 75).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(55, 69).addBox(-8.0F, -4.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 69).addBox(-8.0F, -1.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 21.0F, -7.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition piston4 = pistonshaftholder4.addOrReplaceChild("piston4", CubeListBuilder.create().texOffs(17, 62).addBox(-1.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(11, 74).addBox(-2.5F, -2.0F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -2.0F, 0.0F));

        PartDefinition pistonshaftholder5 = partdefinition.addOrReplaceChild("pistonshaftholder5", CubeListBuilder.create().texOffs(49, 6).addBox(-8.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(49, 0).addBox(-8.0F, -4.0F, 2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 26).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(68, 38).addBox(-8.0F, -4.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(68, 8).addBox(-8.0F, -1.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 21.0F, 7.0F, 0.0F, 3.1416F, -0.9599F));

        PartDefinition piston5 = pistonshaftholder5.addOrReplaceChild("piston5", CubeListBuilder.create().texOffs(55, 60).addBox(-1.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 74).addBox(-2.5F, -2.0F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -2.0F, 0.0F));

        PartDefinition pistonshaftholder6 = partdefinition.addOrReplaceChild("pistonshaftholder6", CubeListBuilder.create().texOffs(0, 47).addBox(-8.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 20).addBox(-8.0F, -4.0F, 2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(27, 26).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(68, 4).addBox(-8.0F, -4.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(68, 0).addBox(-8.0F, -1.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 21.0F, 3.0F, 0.0F, 3.1416F, -0.9599F));

        PartDefinition piston6 = pistonshaftholder6.addOrReplaceChild("piston6", CubeListBuilder.create().texOffs(0, 59).addBox(-1.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(65, 73).addBox(-2.5F, -2.0F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -2.0F, 0.0F));

        PartDefinition pistonshaftholder7 = partdefinition.addOrReplaceChild("pistonshaftholder7", CubeListBuilder.create().texOffs(38, 44).addBox(-8.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(19, 44).addBox(-8.0F, -4.0F, 2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(9, 7).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(19, 67).addBox(-8.0F, -4.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(53, 65).addBox(-8.0F, -1.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 21.0F, -1.0F, 0.0F, 3.1416F, -0.9599F));

        PartDefinition piston7 = pistonshaftholder7.addOrReplaceChild("piston7", CubeListBuilder.create().texOffs(57, 55).addBox(-1.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 73).addBox(-2.5F, -2.0F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -2.0F, 0.0F));

        PartDefinition pistonshaftholder8 = partdefinition.addOrReplaceChild("pistonshaftholder8", CubeListBuilder.create().texOffs(0, 41).addBox(-8.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(27, 20).addBox(-8.0F, -4.0F, 2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(9, 0).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(65, 21).addBox(-8.0F, -4.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(65, 17).addBox(-8.0F, -1.0F, 0.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 21.0F, -5.0F, 0.0F, 3.1416F, -0.9599F));

        PartDefinition piston8 = pistonshaftholder8.addOrReplaceChild("piston8", CubeListBuilder.create().texOffs(57, 50).addBox(-1.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(73, 29).addBox(-2.5F, -2.0F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -2.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public void setupAnim(DieselEngineBE engine){
        baseframe.xRot = 3.14f;
        shaftholder.x = 0.0f;
        shaftholder.z = 0f;
        shaftholder.y = 7.5f;

        pistonshaftholder1.xRot = 0f;
        pistonshaftholder2.xRot = 0f;
        pistonshaftholder3.xRot = 0f;
        pistonshaftholder4.xRot = 0f;
        pistonshaftholder1.yRot = 0f;
        pistonshaftholder2.yRot = 0f;
        pistonshaftholder3.yRot = 0f;
        pistonshaftholder4.yRot = 0f;
        pistonshaftholder1.zRot = 4f;
        pistonshaftholder2.zRot = 4f;
        pistonshaftholder3.zRot = 4f;
        pistonshaftholder4.zRot = 4f;

        pistonshaftholder5.xRot = 0f;
        pistonshaftholder6.xRot = 0f;
        pistonshaftholder7.xRot = 0f;
        pistonshaftholder8.xRot = 0f;
        pistonshaftholder5.yRot = 3.14f;
        pistonshaftholder6.yRot = 3.14f;
        pistonshaftholder7.yRot = 3.14f;
        pistonshaftholder8.yRot = 3.14f;
        pistonshaftholder5.zRot = -4f;
        pistonshaftholder6.zRot = -4f;
        pistonshaftholder7.zRot = -4f;
        pistonshaftholder8.zRot = -4f;





        pistonshaftholder1.x = 3f;
        pistonshaftholder2.x = 3f;
        pistonshaftholder3.x = 3f;
        pistonshaftholder4.x = 3f;
        pistonshaftholder5.x = -3f;
        pistonshaftholder6.x = -3f;
        pistonshaftholder7.x = -3f;
        pistonshaftholder8.x = -3f;
    }

    public void setupAnimRunning(DieselEngineBE engine){
        baseframe.xRot = 3.14f;
        shaftholder.x = 0.0f;
        shaftholder.z = 0f;
        shaftholder.y = 7.5f;

        pistonshaftholder1.xRot = 0;
        pistonshaftholder2.xRot = 0;
        pistonshaftholder3.xRot = 0;
        pistonshaftholder4.xRot = 0;
        pistonshaftholder1.yRot = 0f;
        pistonshaftholder2.yRot = 0f;
        pistonshaftholder3.yRot = 0f;
        pistonshaftholder4.yRot = 0f;
        pistonshaftholder1.zRot = 4f;
        pistonshaftholder2.zRot = 4f;
        pistonshaftholder3.zRot = 4f;
        pistonshaftholder4.zRot = 4f;

        pistonshaftholder5.xRot = 0f;
        pistonshaftholder6.xRot = 0f;
        pistonshaftholder7.xRot = 0f;
        pistonshaftholder8.xRot = 0f;
        pistonshaftholder5.yRot = 3.14f;
        pistonshaftholder6.yRot = 3.14f;
        pistonshaftholder7.yRot = 3.14f;
        pistonshaftholder8.yRot = 3.14f;
        pistonshaftholder5.zRot = -4f;
        pistonshaftholder6.zRot = -4f;
        pistonshaftholder7.zRot = -4f;
        pistonshaftholder8.zRot = -4f;


        pistonshaftholder1.x = 3f;
        pistonshaftholder2.x = 3f;
        pistonshaftholder3.x = 3f;
        pistonshaftholder4.x = 3f;
        pistonshaftholder5.x = -3f;
        pistonshaftholder6.x = -3f;
        pistonshaftholder7.x = -3f;
        pistonshaftholder8.x = -3f;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.baseframe.render(poseStack,buffer,packedLight,packedOverlay);
        this.shaftholder.render(poseStack,buffer,packedLight,packedOverlay);
        this.pistonshaftholder1.render(poseStack,buffer,packedLight,packedOverlay);
        this.pistonshaftholder3.render(poseStack,buffer,packedLight,packedOverlay);
        this.pistonshaftholder5.render(poseStack,buffer,packedLight,packedOverlay);
        this.pistonshaftholder7.render(poseStack,buffer,packedLight,packedOverlay);
        this.pistonshaftholder2.render(poseStack,buffer,packedLight,packedOverlay);
        this.pistonshaftholder4.render(poseStack,buffer,packedLight,packedOverlay);
        this.pistonshaftholder6.render(poseStack,buffer,packedLight,packedOverlay);
        this.pistonshaftholder8.render(poseStack,buffer,packedLight,packedOverlay);
    }
}
