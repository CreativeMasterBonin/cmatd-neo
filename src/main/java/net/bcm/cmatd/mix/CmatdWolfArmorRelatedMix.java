package net.bcm.cmatd.mix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bcm.cmatd.item.RadioactiveAnimalSuitGear;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Wolf.class)
public class CmatdWolfArmorRelatedMix {
    @WrapOperation(method = "hasArmor",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    public boolean cmatdWolfHasArmor(ItemStack instance, Item item, Operation<Boolean> original){
        return instance.getItem() instanceof RadioactiveAnimalSuitGear || original.call(instance,item);
    }
}
