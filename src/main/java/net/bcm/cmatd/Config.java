package net.bcm.cmatd;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
@EventBusSubscriber(modid = Cmatd.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue LIGHTNING_GEN_RATE_CFG = BUILDER
            .comment("The FE per operation the Lightning Generator generates").defineInRange("lightning_gen_rate",
                    1_000_000,
                    1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();


    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        Utility.LIGHTNING_GENERATOR_ENERGY_RATE = LIGHTNING_GEN_RATE_CFG.get();
    }
}
