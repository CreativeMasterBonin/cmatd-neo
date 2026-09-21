package net.bcm.cmatd.mix;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.AnimalArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HorseArmorLayer.class)
public class CmatdHorseArmorLayerMix {
    @Shadow @Final private HorseModel<Horse> model;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HorseModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"),cancellable = true)
    public void cmatdApplyVCBasedOnItem(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Horse livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        if(livingEntity.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_HORSE_SUIT) && livingEntity.getBodyArmorItem().getItem() instanceof AnimalArmorItem animalArmorItem){
            model.renderToBuffer(poseStack,buffer.getBuffer(RenderType.entityTranslucentCull(animalArmorItem.getTexture())),packedLight, OverlayTexture.NO_OVERLAY);
            ci.cancel();
            return;
        }
    }
}
