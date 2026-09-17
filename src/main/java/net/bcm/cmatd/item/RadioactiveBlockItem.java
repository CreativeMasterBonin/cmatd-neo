package net.bcm.cmatd.item;

import net.bcm.cmatd.Components;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class RadioactiveBlockItem extends BlockItem {
    public RadioactiveBlockItem(Block block, Properties properties) {
        super(block, properties.rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(!level.isClientSide()){
            if(entity instanceof LivingEntity livingEntity){
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
        }
    }
}
