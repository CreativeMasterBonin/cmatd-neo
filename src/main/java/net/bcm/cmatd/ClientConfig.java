package net.bcm.cmatd;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHOW_GAS_AMOUNT_CUBE = BUILDER
            .comment("Render the gas amount cube on gas tanks")
            .define("show_gas_amount_cube",true);

    public static final ModConfigSpec.BooleanValue SHOW_GAS_TYPE_BEAM = BUILDER
            .comment("Render the gas type beam on gas tanks")
            .define("show_gas_type_beam",true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
