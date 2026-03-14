package net.bcm.cmatd.api;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class Registries {
    public static final ResourceKey<Registry<GasType>> GAS_TYPES_KEY = key("gas_type");

    /**
     * Limited to 32767 Gas Types for reasons
     */
    public static final Registry<GasType> GAS_TYPES = new RegistryBuilder<>(GAS_TYPES_KEY)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, "empty"))
            .maxId(32767)
            .sync(true)
            .withIntrusiveHolders() // is there someway to remove this so that the mod can be updated past this version? since: 1.21.1
            .create();

    // the 'AIR' of CMATD gas types
    public static final ResourceKey<GasType> EMPTY_GAS_KEY = ResourceKey.create(GAS_TYPES_KEY,
            ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"empty"));

    // easier to read code using a key method rather than inline
    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, name));
    }
}
