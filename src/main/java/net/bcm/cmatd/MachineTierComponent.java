package net.bcm.cmatd;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record MachineTierComponent(int machine_tier, int max_energy, int max_receive, int max_extract,
                                   int modules_allowed, int energy_gen_rate){
    /*
    protected int machine_tier = 0;
    protected int max_energy = 10000;
    protected int max_receive = 1000;
    protected int max_extract = 1000;
    protected int modules_allowed = 0;
    protected int energy_gen_rate = 50;
     */

    public static final Codec<MachineTierComponent> CODEC = RecordCodecBuilder.create(
            kindof1 -> kindof1.group(
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("machine_tier", 0)
                            .forGetter(MachineTierComponent::machine_tier),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_energy", 10000)
                            .forGetter(MachineTierComponent::max_energy),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_receive", 1000)
                            .forGetter(MachineTierComponent::max_receive),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_extract", 1000)
                            .forGetter(MachineTierComponent::max_extract),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("modules_allowed", 0)
                            .forGetter(MachineTierComponent::modules_allowed),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("energy_gen_rate", 50)
                            .forGetter(MachineTierComponent::energy_gen_rate)
                    ).apply(kindof1, MachineTierComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MachineTierComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,MachineTierComponent::machine_tier,
            ByteBufCodecs.INT,MachineTierComponent::max_energy,
            ByteBufCodecs.INT,MachineTierComponent::max_receive,
            ByteBufCodecs.INT,MachineTierComponent::max_extract,
            ByteBufCodecs.INT,MachineTierComponent::modules_allowed,
            ByteBufCodecs.INT,MachineTierComponent::energy_gen_rate,
            MachineTierComponent::new
    );

    public int getMachineTier(){
        return this.machine_tier;
    }

    public int getMaxEnergy(){
        return this.max_energy;
    }

    public int getMaxReceive(){
        return this.max_receive;
    }

    public int getMaxExtract(){
        return this.max_extract;
    }

    public int getModulesAllowed(){
        return this.modules_allowed;
    }

    public int getEnergyGenRate(){
        return this.energy_gen_rate;
    }
}
