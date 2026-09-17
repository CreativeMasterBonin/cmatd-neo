package net.bcm.cmatd.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RadioactiveOreBlock extends Block {
    public final IntProvider xpRange;
    public final float radioactivityOfOre; // limited range to keep it within a chunk

    public static final MapCodec<RadioactiveOreBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            IntProvider.codec(0,10).fieldOf("xp_range").forGetter(blockXP -> blockXP.xpRange),
                            Codec.floatRange(0.0f,5.0f).fieldOf("radioactivity").forGetter(blockR -> blockR.radioactivityOfOre),
                            propertiesCodec()
                    )
                    .apply(instance, RadioactiveOreBlock::new)
    );

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public RadioactiveOreBlock(IntProvider provider, float radioactivity, Properties properties) {
        super(properties.randomTicks());
        xpRange = provider;
        radioactivityOfOre = radioactivity;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // no random chunk loading
        if(!level.hasChunkAt(pos)){
            return;
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
                    if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2 + (int)radioactivityOfOre,true,false));
                    }
                    if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,1 + (int)radioactivityOfOre,true,false));
                    }
                }
            }
        }
    }
}
