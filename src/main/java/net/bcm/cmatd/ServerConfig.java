package net.bcm.cmatd;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue LIGHTNING_GEN_RATE = BUILDER
            .comment("The FE generated per strike on the Lightning Generator").defineInRange("lightning_gen_rate",
                    1_000_000,
                    1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue LIGHTNING_GEN_MAX_CAPACITY = BUILDER
            .comment("The max FE that can be stored in the Lightning Generator").defineInRange("lightning_gen_capacity",
                    16_000_000,
                    1,Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue LIGHTNING_GEN_COOLDOWN_TIME = BUILDER
            .comment("The cooldown time left applied to the Lightning Generator when struck").defineInRange("lightning_gen_cooldown_time",
                    50,
                    25,250);

    public static final ModConfigSpec.IntValue LIGHTNING_GEN_TIMER_SCALE = BUILDER
            .comment("The time scale in ticks that controls cooldown time left for the Lightning Generator").defineInRange("lightning_gen_timer_scale",
                    150,
                    25,150);

    public static final ModConfigSpec.IntValue LIGHTNING_GEN_MAX_RECEIVE_SEND = BUILDER
            .comment("The max speed of energy transfer for the Lightning Generator").defineInRange("lightning_gen_max_receive_send",
                    2_000_000,
                    1,Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();
}
