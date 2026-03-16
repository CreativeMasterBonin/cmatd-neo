package net.bcm.cmatd.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.api.GasStack;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.api.Registries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class GasVent extends Block{
    // this GasType is final, as it must never change at any time (anonymous classes should be ok)
    public final Holder<GasType> gasTypeToProduce;
    public final IntProvider gasAmountToProduce;

    public static final Codec<Holder<GasType>> GAS_NON_EMPTY_CODEC = Registries.GAS_TYPES.holderByNameCodec().validate(holder -> {
        return holder.is(Gases.EMPTY.getRegistryHolder()) ? DataResult.error(() -> {
            return "Gas must not be cmatd:empty";
        }) : DataResult.success(holder);
    });

    public static final MapCodec<GasVent> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    GAS_NON_EMPTY_CODEC.fieldOf("gas_type").forGetter(vent -> vent.gasTypeToProduce),
                    IntProvider.POSITIVE_CODEC.fieldOf("gas_amount").forGetter(vent -> vent.gasAmountToProduce),
                            propertiesCodec())
                    .apply(instance,GasVent::new)
    );

    public GasVent(Holder<GasType> gasType, IntProvider gasAmount, BlockBehaviour.Properties properties){
        super(properties);
        gasTypeToProduce = gasType;
        gasAmountToProduce = gasAmount;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.gas_vent.additional_info")
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable(this.gasTypeToProduce.value().getDescriptionId())
                .withColor(this.gasTypeToProduce.value().getColor()));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(!(level.getBlockState(pos.above()).getBlock() instanceof GasTank) && level.getBlockState(pos.above()).getBlock() instanceof AirBlock){
            if(level.getGameTime() % 12L == 0){
                level.addAlwaysVisibleParticle(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        true,
                        (double)pos.getX() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? -1 : 1),
                        (double)pos.getY() + 1.01D,
                        (double)pos.getZ() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? -1 : 1),
                        0.0,
                        Mth.randomBetween(level.getRandom(),0.031f,0.047f),
                        0.0
                );
            }
        }
    }

    @Override
    public MapCodec<? extends GasVent> codec() {
        return CODEC;
    }

    /**
     * Returns a default stack of Gas for use in comparing scenarios
     * @return The GasStack based on the standard values setup for this vent
     */
    public GasStack getDefaultGasStack(){
        return new GasStack(gasTypeToProduce,gasAmountToProduce.getMinValue());
    }

    /**
     * Returns the descriptionId of the GasType
     * @return The String which is the GasTypes' name retrieved from the registry
     */
    public String getGasName(){
        return gasTypeToProduce.getRegisteredName();
    }
}
