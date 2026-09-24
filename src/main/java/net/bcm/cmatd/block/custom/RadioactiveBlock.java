package net.bcm.cmatd.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bcm.cmatd.ServerConfig;
import net.bcm.cmatd.ServerUtilities;
import net.bcm.cmatd.datagen.Tag;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RadioactiveBlock extends Block {
    public final float radioactivityOfOre; // limited range to keep it within a chunk

    public static final MapCodec<RadioactiveBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.floatRange(0.0f,5.0f).fieldOf("radioactivity").forGetter(blockR -> blockR.radioactivityOfOre),
                            propertiesCodec()
                    )
                    .apply(instance, RadioactiveBlock::new)
    );

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public RadioactiveBlock(float radioactivity, Properties properties) {
        super(properties.randomTicks());
        radioactivityOfOre = radioactivity;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // no random chunk loading
        if(!level.hasChunkAt(pos)){
            return;
        }
        // disable radiation effects if the config is set to
        if(!level.isClientSide()){
            if(level instanceof ServerLevel){
                if(!ServerConfig.RADIOACTIVITY_ENABLED.getAsBoolean()){
                    return;
                }
            }
        }
        // do not check for entities if ticks are lagging or rate is too high
        if(!level.isClientSide() && level.tickRateManager().tickrate() < 30.0f && level.tickRateManager().runsNormally()){
            List<LivingEntity> livingEntities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(),null,new AABB(
                    pos.getX() - 2 - radioactivityOfOre,
                    pos.getY() - 2 - radioactivityOfOre,
                    pos.getZ() - 2 - radioactivityOfOre,
                    pos.getX() + 2 + radioactivityOfOre,
                    pos.getY() + 2 + radioactivityOfOre,
                    pos.getZ() + 2 + radioactivityOfOre
            ));
            for(LivingEntity entity : livingEntities){
                if(entity instanceof LivingEntity livingEntity){
                    // wolves can wear the armor, but currently it can't be applied nor is visible
                    if(livingEntity instanceof Wolf wolf){
                        if(!wolf.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_WOLF_SUIT)){
                            if(!wolf.hasEffect(MobEffects.WEAKNESS)){
                                wolf.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,1,true,false));
                            }
                            if(!wolf.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)){
                                wolf.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,1,true,false));
                            }
                        }
                    } // horse armor
                    else if(livingEntity instanceof AbstractHorse abstractHorse){
                        if(!abstractHorse.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_HORSE_SUIT)){
                            if(!abstractHorse.hasEffect(MobEffects.WEAKNESS)){
                                abstractHorse.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,1,true,false));
                            }
                            if(!abstractHorse.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)){
                                abstractHorse.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,1,true,false));
                            }
                        }
                    }
                    else if(livingEntity instanceof Fox fox){ // foxes can technically equip the barrier, so allow them protection too
                        if(!fox.getItemBySlot(EquipmentSlot.MAINHAND).is(CmatdItem.RADIOACTIVE_PROTECTION_BARRIER)){
                            if(!fox.hasEffect(MobEffects.WEAKNESS)){
                                fox.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,1,true,false));
                            }
                            if(!fox.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)){
                                fox.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,1,true,false));
                            }
                        }
                    }
                    else if(livingEntity instanceof TamableAnimal tamableAnimal){ // for all tamable animals that need protection from radiation
                        if(!tamableAnimal.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_HORSE_SUIT)){
                            if(!tamableAnimal.hasEffect(MobEffects.WEAKNESS)){
                                tamableAnimal.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,1,true,false));
                            }
                            if(!tamableAnimal.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)){
                                tamableAnimal.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,1,true,false));
                            }
                        }
                    }
                    else{ // every thing and every one else
                        livingEntity.getArmorSlots().iterator().forEachRemaining(armorStack -> {
                            // wear a full suit to be safe from this
                            if(!armorStack.is(Tag.HAZMAT_SUIT_PIECES)){
                                if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false));
                                }
                                if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,1,true,false));
                                }
                                if(!livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,1,true,false));
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
