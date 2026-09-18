package net.bcm.cmatd.item;

import net.bcm.cmatd.Components;
import net.bcm.cmatd.datagen.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class RadioactiveItem extends Item {
    public RadioactiveItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(!level.isClientSide()){
            if(level instanceof ServerLevel serverLevel){
                if(serverLevel.tickRateManager().tickrate() < 30.0f && serverLevel.tickRateManager().runsNormally()){
                    if(entity instanceof LivingEntity livingEntity){
                        livingEntity.getArmorSlots().iterator().forEachRemaining(armorStack -> {
                            // wear a full suit to be safe from this
                            if(!armorStack.is(Tag.HAZMAT_SUIT_PIECES)){
                                if(stack.has(Components.RADIOACTIVE)){
                                    if(stack.get(Components.RADIOACTIVE).extremelyPotent()){
                                        if(!livingEntity.hasEffect(MobEffects.POISON)){
                                            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,1,true,false));
                                        }
                                        if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                            livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,3,true,false));
                                        }
                                        if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,10,true,false));
                                        }
                                    }
                                    if(!livingEntity.isOnFire() && !livingEntity.fireImmune()){
                                        livingEntity.setRemainingFireTicks(stack.get(Components.RADIOACTIVE).burnForTicks());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
