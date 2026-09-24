package net.bcm.cmatd.item;

import net.bcm.cmatd.datagen.Tag;
import net.minecraft.core.Holder;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;

public class RadioactiveAnimalSuitGear extends AnimalArmorItem {
    public final String renderTypeName;

    public RadioactiveAnimalSuitGear(Holder<ArmorMaterial> armorMaterial, BodyType bodyType, boolean hasOverlay, String renderType, Properties properties) {
        super(armorMaterial, bodyType, hasOverlay, properties.stacksTo(1).fireResistant()
                .rarity(Rarity.UNCOMMON));
        renderTypeName = renderType;
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

    /*@Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if(player.level().isClientSide()){
            if(stack.is(CmatdItem.RADIOACTIVE_WOLF_SUIT)){
                if(interactionTarget instanceof Wolf wolf){
                    if(!wolf.isWearingBodyArmor()){
                        player.playSound(SoundEvents.ARMOR_EQUIP_WOLF.value(),1.0f, Utility.nextFloatBetweenInclusive(0.95f,1.1f));
                        return InteractionResult.CONSUME;
                    }
                }
            }
        }
        else if(!player.level().isClientSide()){
            if(player.level() instanceof ServerLevel serverLevel){
                if(stack.is(CmatdItem.RADIOACTIVE_WOLF_SUIT)){
                    if(interactionTarget instanceof Wolf wolf){
                        if(!wolf.isWearingBodyArmor()){
                            wolf.setBodyArmorItem(new ItemStack(CmatdItem.RADIOACTIVE_WOLF_SUIT.asItem(),1));
                            return InteractionResult.CONSUME;
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }*/

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
