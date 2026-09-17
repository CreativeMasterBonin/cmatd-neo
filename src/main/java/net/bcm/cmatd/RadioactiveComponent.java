package net.bcm.cmatd;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record RadioactiveComponent(float radioactivity, int burnForTicks, boolean extremelyPotent) {
    public static final Codec<RadioactiveComponent> CODEC = RecordCodecBuilder.create(
            kindof -> kindof.group(
                    Codec.FLOAT.fieldOf("radioactivity").forGetter(RadioactiveComponent::radioactivity),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("burn_for_ticks").forGetter(RadioactiveComponent::burnForTicks),
                    Codec.BOOL.fieldOf("extremely_potent").forGetter(RadioactiveComponent::extremelyPotent)
            ).apply(kindof,RadioactiveComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,RadioactiveComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,RadioactiveComponent::radioactivity,
            ByteBufCodecs.VAR_INT,RadioactiveComponent::burnForTicks,
            ByteBufCodecs.BOOL,RadioactiveComponent::extremelyPotent,
            RadioactiveComponent::new
    );

    public static RadioactiveComponent create(float radioactivityAmount, int burningForTicks, boolean isExtremelyPotent){
        return new RadioactiveComponent(radioactivityAmount,burningForTicks,isExtremelyPotent);
    }
}
