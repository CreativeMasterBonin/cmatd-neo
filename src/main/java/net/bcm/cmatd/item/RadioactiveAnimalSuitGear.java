package net.bcm.cmatd.item;

import net.bcm.cmatd.datagen.Tag;
import net.minecraft.core.Holder;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class RadioactiveAnimalSuitGear extends AnimalArmorItem {
    public RadioactiveAnimalSuitGear(Holder<ArmorMaterial> armorMaterial, BodyType bodyType, boolean hasOverlay, Properties properties) {
        super(armorMaterial, bodyType, hasOverlay, properties.stacksTo(1).fireResistant());
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if(stack.is(CmatdItem.RADIOACTIVE_HORSE_SUIT.asItem())){
            return enchantment.is(Tag.SUPPORTED_BY_RADIOACTIVE_HORSE_ARMOR);
        }
        else if(stack.is(CmatdItem.RADIOACTIVE_WOLF_SUIT.asItem())){
            return enchantment.is(Tag.SUPPORTED_BY_RADIOACTIVE_WOLF_ARMOR);
        }
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
