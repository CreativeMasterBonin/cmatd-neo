package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WorldlyProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE,OreFeatureGen::bootstrap)
            .add(Registries.PLACED_FEATURE,OrePlacementGen::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS,BiomeModifierGen::bootstrap)
            ;

    public WorldlyProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> r) {
        super(output, r, BUILDER, Set.of(Cmatd.MODID));
    }
}
