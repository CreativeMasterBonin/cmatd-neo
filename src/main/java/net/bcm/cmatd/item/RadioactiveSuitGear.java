package net.bcm.cmatd.item;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

public class RadioactiveSuitGear extends ArmorItem {
    public RadioactiveSuitGear(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if(stack.is(CmatdItem.RADIOACTIVE_SUIT_HEADGEAR)) {
            return enchantment.is(Enchantments.MENDING) ||
                    enchantment.is(Enchantments.AQUA_AFFINITY) || enchantment.is(Tags.Enchantments.ENTITY_DEFENSE_ENHANCEMENTS)
                    || enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.THORNS);
        }
        else if(stack.is(CmatdItem.RADIOACTIVE_SUIT_BODYWEAR)){
            return enchantment.is(Enchantments.MENDING) || enchantment.is(Enchantments.PROJECTILE_PROTECTION)
                    || enchantment.is(Enchantments.BLAST_PROTECTION) || enchantment.is(Enchantments.FIRE_PROTECTION) ||
                    enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.THORNS);
        }
        else if(stack.is(CmatdItem.RADIOACTIVE_SUIT_PANTS)){
            return enchantment.is(Enchantments.MENDING) || enchantment.is(Enchantments.PROTECTION)
                    || enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.FIRE_PROTECTION)
                    || enchantment.is(Enchantments.BLAST_PROTECTION) || enchantment.is(Enchantments.PROJECTILE_PROTECTION)
                    || enchantment.is(Enchantments.SWIFT_SNEAK) || enchantment.is(Enchantments.THORNS);
        }
        else if(stack.is(CmatdItem.RADIOACTIVE_SUIT_BOOTS)){
            return enchantment.is(Enchantments.MENDING) || enchantment.is(Enchantments.UNBREAKING) ||
                    enchantment.is(Enchantments.PROTECTION) || enchantment.is(Enchantments.FIRE_PROTECTION)
                    || enchantment.is(Enchantments.BLAST_PROTECTION) || enchantment.is(Enchantments.PROJECTILE_PROTECTION)
                    || enchantment.is(Enchantments.DEPTH_STRIDER) || enchantment.is(Enchantments.FROST_WALKER)
                    || enchantment.is(Enchantments.THORNS) || enchantment.is(Enchantments.FEATHER_FALLING);
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

    }
}
