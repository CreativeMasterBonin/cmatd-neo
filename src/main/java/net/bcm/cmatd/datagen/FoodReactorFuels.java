package net.bcm.cmatd.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProvider;

public record FoodReactorFuels(int waterAmountOutput, FloatProvider powerOutputMultiplier){
    public static final Codec<FoodReactorFuels> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("water_waste_amount").forGetter(FoodReactorFuels::waterAmountOutput),
                    FloatProvider.CODEC.fieldOf("power_output_multiplier").forGetter(FoodReactorFuels::powerOutputMultiplier)
            ).apply(instance,FoodReactorFuels::new)
    );
}
