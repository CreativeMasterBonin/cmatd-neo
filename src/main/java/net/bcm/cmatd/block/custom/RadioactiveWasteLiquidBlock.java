package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.ServerConfig;
import net.bcm.cmatd.datagen.Tag;
import net.bcm.cmatd.fluid.CmatdFluid;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.TriState;

import java.util.List;

public class RadioactiveWasteLiquidBlock extends LiquidBlock {
    public RadioactiveWasteLiquidBlock(Properties properties) {
        super((FlowingFluid)CmatdFluid.RADIOACTIVE_WASTE_FLUID_SOURCE.get(),
                properties.mapColor(MapColor.COLOR_BROWN)
                        .strength(100f).pushReaction(PushReaction.DESTROY)
                        .replaceable().noCollission()
                        .noLootTable().liquid().sound(SoundType.EMPTY)
        );
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
        boolean isWasteSupportedPlant = plant.is(Tag.RADIOACTIVE_WASTE_SUPPORTED);
        return isWasteSupportedPlant ? TriState.TRUE : TriState.DEFAULT;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(!level.isClientSide()){
            if(level instanceof ServerLevel){
                if(!ServerConfig.RADIOACTIVITY_ENABLED.getAsBoolean()){
                    return;
                }
            }
            if(entity instanceof LivingEntity livingEntity){
                if(livingEntity instanceof Wolf wolf){
                    if(!wolf.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_WOLF_SUIT)){
                        BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                        Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                        // entity is too close, the radioactivity is high and will burn
                        if(entityPos.distSqr(blockPosAsInt) <= 10D){
                            if(!livingEntity.isOnFire()){
                                livingEntity.setRemainingFireTicks(40);
                            }
                        } // entity is getting pretty close, the radioactivity will start to poison
                        else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                            if(!livingEntity.hasEffect(MobEffects.POISON)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                            }
                        } // entity is close, but far enough to prevent serious effects
                        else{
                            if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                            }
                            if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
                            }
                        }
                    }
                }
                else if(livingEntity instanceof AbstractHorse abstractHorse){
                    if(!abstractHorse.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_HORSE_SUIT)){
                        BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                        Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                        // entity is too close, the radioactivity is high and will burn
                        if(entityPos.distSqr(blockPosAsInt) <= 10D){
                            if(!livingEntity.isOnFire()){
                                livingEntity.setRemainingFireTicks(40);
                            }
                        } // entity is getting pretty close, the radioactivity will start to poison
                        else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                            if(!livingEntity.hasEffect(MobEffects.POISON)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                            }
                        } // entity is close, but far enough to prevent serious effects
                        else{
                            if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                            }
                            if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
                            }
                        }
                    }
                }
                else if(livingEntity instanceof TamableAnimal tamableAnimal){ // for tamable animals that need to be protected from radiation
                    if(!tamableAnimal.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_PROTECTION_BARRIER)){
                        BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                        Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                        // entity is too close, the radioactivity is high and will burn
                        if(entityPos.distSqr(blockPosAsInt) <= 10D){
                            if(!livingEntity.isOnFire()){
                                livingEntity.setRemainingFireTicks(40);
                            }
                        } // entity is getting pretty close, the radioactivity will start to poison
                        else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                            if(!livingEntity.hasEffect(MobEffects.POISON)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                            }
                        } // entity is close, but far enough to prevent serious effects
                        else{
                            if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                            }
                            if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
                            }
                        }
                    }
                }
                else{
                    // each entities armor slots for each slot (checked when iterating for safety)
                    livingEntity.getArmorSlots().iterator().forEachRemaining(stack -> {
                        // wear a full suit to be safe from this
                        if(!stack.is(Tag.HAZMAT_SUIT_PIECES)){
                            BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                            Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                            // entity is too close, the radioactivity is high and will burn
                            if(entityPos.distSqr(blockPosAsInt) <= 10D){
                                if(!livingEntity.isOnFire()){
                                    livingEntity.setRemainingFireTicks(40);
                                }
                            } // entity is getting pretty close, the radioactivity will start to poison
                            else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                                if(!livingEntity.hasEffect(MobEffects.POISON)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                                }
                            } // entity is close, but far enough to prevent serious effects
                            else{
                                if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                                }
                                if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
                                }
                            }
                        }
                    });
                }
            }
        }
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
        // plus the time must be divisible by 3 (to reduce amount of time checking for entities if tick boosted somehow)
        if(!level.isClientSide() && level.tickRateManager().tickrate() < 30.0f && level.tickRateManager().runsNormally()){
            List<LivingEntity> livingEntities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(),null,new AABB(
                    pos.getX() - 5,
                    pos.getY() - 5,
                    pos.getZ() - 5,
                    pos.getX() + 5,
                    pos.getY() + 5,
                    pos.getZ() + 5
            ));
            // entities in the range
            for(LivingEntity entity : livingEntities){
                if(entity instanceof LivingEntity livingEntity){
                    if(livingEntity instanceof Wolf wolf){
                        if(!wolf.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_WOLF_SUIT)){
                            BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                            Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                            // entity is too close, the radioactivity is high and will burn
                            if(entityPos.distSqr(blockPosAsInt) <= 10D){
                                if(!livingEntity.isOnFire()){
                                    livingEntity.setRemainingFireTicks(40);
                                }
                            } // entity is getting pretty close, the radioactivity will start to poison
                            else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                                if(!livingEntity.hasEffect(MobEffects.POISON)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                                }
                            } // entity is close, but far enough to prevent serious effects
                            else{
                                if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                                }
                                if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
                                }
                            }
                        }
                    }
                    else if(livingEntity instanceof AbstractHorse abstractHorse){
                        if(!abstractHorse.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_HORSE_SUIT)){
                            BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                            Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                            // entity is too close, the radioactivity is high and will burn
                            if(entityPos.distSqr(blockPosAsInt) <= 10D){
                                if(!livingEntity.isOnFire()){
                                    livingEntity.setRemainingFireTicks(40);
                                }
                            } // entity is getting pretty close, the radioactivity will start to poison
                            else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                                if(!livingEntity.hasEffect(MobEffects.POISON)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                                }
                            } // entity is close, but far enough to prevent serious effects
                            else{
                                if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                                }
                                if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
                                }
                            }
                        }
                    }
                    else if(livingEntity instanceof Fox fox){ // foxes can hold items in their 'hand' so having a certain item counts as protection from radiation
                        if(!fox.getItemBySlot(EquipmentSlot.MAINHAND).is(CmatdItem.RADIOACTIVE_PROTECTION_BARRIER)){
                            BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                            Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                            // entity is too close, the radioactivity is high and will burn
                            if(entityPos.distSqr(blockPosAsInt) <= 10D){
                                if(!livingEntity.isOnFire()){
                                    livingEntity.setRemainingFireTicks(40);
                                }
                            } // entity is getting pretty close, the radioactivity will start to poison
                            else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                                if(!livingEntity.hasEffect(MobEffects.POISON)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                                }
                            } // entity is close, but far enough to prevent serious effects
                            else{
                                if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                                }
                                if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
                                }
                            }
                        }
                    }
                    else if(livingEntity instanceof TamableAnimal tamableAnimal){ // for tamable animals that need protection from radiation
                        if(!tamableAnimal.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_PROTECTION_BARRIER)){
                            BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                            Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                            // entity is too close, the radioactivity is high and will burn
                            if(entityPos.distSqr(blockPosAsInt) <= 10D){
                                if(!livingEntity.isOnFire()){
                                    livingEntity.setRemainingFireTicks(40);
                                }
                            } // entity is getting pretty close, the radioactivity will start to poison
                            else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                                if(!livingEntity.hasEffect(MobEffects.POISON)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                                }
                            } // entity is close, but far enough to prevent serious effects
                            else{
                                if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                                }
                                if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
                                }
                            }
                        }
                    }
                    else{
                        // each entities armor slots for each slot (checked when iterating for safety)
                        livingEntity.getArmorSlots().iterator().forEachRemaining(stack -> {
                            // wear a full suit to be safe from this
                            if(!stack.is(Tag.HAZMAT_SUIT_PIECES)){
                                BlockPos entityPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
                                Vec3i blockPosAsInt = new Vec3i(pos.getX(),pos.getY(),pos.getZ());
                                // entity is too close, the radioactivity is high and will burn
                                if(entityPos.distSqr(blockPosAsInt) <= 10D){
                                    if(!livingEntity.isOnFire()){
                                        livingEntity.setRemainingFireTicks(40);
                                    }
                                } // entity is getting pretty close, the radioactivity will start to poison
                                else if(entityPos.distSqr(blockPosAsInt) <= 15D){
                                    if(!livingEntity.hasEffect(MobEffects.POISON)){
                                        livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2,true,false,false));
                                    }
                                } // entity is close, but far enough to prevent serious effects
                                else{
                                    if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                                        livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,3,true,false,false));
                                    }
                                    if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                                        livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false,false));
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
