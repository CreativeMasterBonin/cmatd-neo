package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BiomeModifierGen{
    public static final ResourceKey<BiomeModifier> ORE_COMPOUNDITE = registerKey("ore_compoundite");
    public static final ResourceKey<BiomeModifier> ORE_LODEALITE = registerKey("ore_lodealite");
    public static final ResourceKey<BiomeModifier> METHANE_GAS_VENT = registerKey("methane_gas_vent");
    public static final ResourceKey<BiomeModifier> STEAM_GAS_VENT = registerKey("steam_gas_vent");

    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ORE_COMPOUNDITE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(PlacementGen.ORE_COMPOUNDITE)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        context.register(ORE_LODEALITE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(PlacementGen.ORE_LODEALITE)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        context.register(METHANE_GAS_VENT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(PlacementGen.METHANE_GAS_VENT)),
                GenerationStep.Decoration.RAW_GENERATION
        ));

        context.register(STEAM_GAS_VENT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tag.GENERATES_STEAM_VENTS),
                HolderSet.direct(placedFeatures.getOrThrow(PlacementGen.STEAM_GAS_VENT)),
                GenerationStep.Decoration.RAW_GENERATION
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, name));
    }
}
