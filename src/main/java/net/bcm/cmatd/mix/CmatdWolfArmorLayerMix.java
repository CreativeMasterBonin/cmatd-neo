package net.bcm.cmatd.mix;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bcm.cmatd.item.CmatdItem;
import net.bcm.cmatd.item.RadioactiveAnimalSuitGear;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WolfArmorLayer.class)
public abstract class CmatdWolfArmorLayerMix {
    @Shadow @Final private WolfModel<Wolf> model;

    @Shadow protected abstract void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, AnimalArmorItem armorItem);

    @Shadow protected abstract void maybeRenderCracks(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack);

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Wolf;FFFFFF)V",at=@At("HEAD"),cancellable = true)
    private void cmatdRenderWolfArmorLayer(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Wolf livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        if(!livingEntity.getBodyArmorItem().isEmpty()){
            if(livingEntity.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_WOLF_SUIT) && livingEntity.getBodyArmorItem().getItem() instanceof RadioactiveAnimalSuitGear gear){
                model.prepareMobModel(livingEntity,limbSwing,limbSwingAmount,partialTick);
                model.setupAnim(livingEntity,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch);
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(
                        ResourceLocation.parse("cmatd:textures/entity/wolf/radioactive_wolf_suit.png")
                ));
                model.renderToBuffer(poseStack,vertexConsumer,packedLight,packedLight);
                maybeRenderColoredLayer(poseStack,bufferSource,packedLight,livingEntity.getBodyArmorItem(),gear); // if the wolf suit used dyes this would do something
                maybeRenderCracks(poseStack,bufferSource,packedLight,livingEntity.getBodyArmorItem()); // if the wolf suit is damaged this should show cracks on it
                ci.cancel();
            }
        }
    }
}
