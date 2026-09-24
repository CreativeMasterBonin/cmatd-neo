package net.bcm.cmatd.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RadioactiveProtectionBarrier extends Item implements Equipable {
    public RadioactiveProtectionBarrier(Properties properties) {
        super(properties.fireResistant().rarity(Rarity.UNCOMMON).durability(1200)
                .stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.radioactive_protection_barrier")
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(Tags.Enchantments.ENTITY_DEFENSE_ENHANCEMENTS) || enchantment.is(Enchantments.MENDING)
                || enchantment.is(Enchantments.UNBREAKING);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(CmatdItem.BLAST_PROOF_INGOT.asItem());
    }

    @Override
    public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.BODY;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.BODY;
    }
}
