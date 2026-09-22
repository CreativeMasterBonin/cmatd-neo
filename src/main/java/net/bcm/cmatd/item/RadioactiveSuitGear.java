package net.bcm.cmatd.item;

import net.bcm.cmatd.datagen.Tag;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;

public class RadioactiveSuitGear extends ArmorItem {
    public RadioactiveSuitGear(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties.stacksTo(1).rarity(Rarity.UNCOMMON)
                .fireResistant());
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if(stack.is(CmatdItem.RADIOACTIVE_SUIT_HEADGEAR.asItem())){
            return enchantment.is(Tag.SUPPORTED_BY_RADIOACTIVE_HUMANOID_ARMOR) || enchantment.is(EnchantmentTags.ARMOR_EXCLUSIVE);
        }
        else if(stack.is(CmatdItem.RADIOACTIVE_SUIT_BODYWEAR.asItem())){
            return enchantment.is(Tag.SUPPORTED_BY_RADIOACTIVE_HUMANOID_ARMOR) || enchantment.is(EnchantmentTags.ARMOR_EXCLUSIVE);
        }
        else if(stack.is(CmatdItem.RADIOACTIVE_SUIT_PANTS.asItem())){
            return enchantment.is(Tag.SUPPORTED_BY_RADIOACTIVE_HUMANOID_ARMOR) || enchantment.is(EnchantmentTags.ARMOR_EXCLUSIVE);
        }
        else if(stack.is(CmatdItem.RADIOACTIVE_SUIT_BOOTS.asItem())){
            return enchantment.is(Tag.SUPPORTED_BY_RADIOACTIVE_HUMANOID_ARMOR) || enchantment.is(EnchantmentTags.ARMOR_EXCLUSIVE) || enchantment.is(EnchantmentTags.BOOTS_EXCLUSIVE);
        }
        return enchantment.is(Tag.SUPPORTED_BY_RADIOACTIVE_HUMANOID_ARMOR);
    }
}
