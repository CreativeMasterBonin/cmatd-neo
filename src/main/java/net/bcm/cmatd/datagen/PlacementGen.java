package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class PlacementGen {
    public static final ResourceKey<PlacedFeature> ORE_COMPOUNDITE = createKey("ore_compoundite");
    public static final ResourceKey<PlacedFeature> ORE_LODEALITE = createKey("ore_lodealite");
    public static final ResourceKey<PlacedFeature> METHANE_GAS_VENT = createKey("methane_gas_vent");
    public static final ResourceKey<PlacedFeature> STEAM_GAS_VENT = createKey("steam_gas_vent");


    public static void bootstrap(BootstrapContext<PlacedFeature> context){
        HolderGetter<ConfiguredFeature<?, ?>> holdGetCombo = context.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> compounditeFeatureHolder = holdGetCombo.getOrThrow(ConfiguredFeatureGen.ORE_COMPOUNDITE);
        Holder<ConfiguredFeature<?, ?>> lodealiteFeatureHolder = holdGetCombo.getOrThrow(ConfiguredFeatureGen.ORE_LODEALITE);
        Holder<ConfiguredFeature<?, ?>> methaneGasVentNormal = holdGetCombo.getOrThrow(ConfiguredFeatureGen.METHANE_GAS_VENT);
        Holder<ConfiguredFeature<?, ?>> steamGasVentNormal = holdGetCombo.getOrThrow(ConfiguredFeatureGen.STEAM_GAS_VENT);

        register(context,ORE_COMPOUNDITE,compounditeFeatureHolder,
                commonOrePlacement(25, HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.top())));

        register(context,ORE_LODEALITE,lodealiteFeatureHolder,
                commonOrePlacement(15, HeightRangePlacement.uniform(VerticalAnchor.absolute(-32), VerticalAnchor.top())));

        PlacementUtils.register(
                context,
                METHANE_GAS_VENT,
                methaneGasVentNormal,
                CountPlacement.of(21),
                RarityFilter.onAverageOnceEvery(4),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(List.of(Blocks.STONE,Blocks.DEEPSLATE,Blocks.INFESTED_STONE,Blocks.INFESTED_DEEPSLATE))),
                RandomOffsetPlacement.vertical(ConstantInt.of(2)),
                BiomeFilter.biome()
        );
        PlacementUtils.register(
                context,
                STEAM_GAS_VENT,
                steamGasVentNormal,
                CountPlacement.of(19),
                RarityFilter.onAverageOnceEvery(4),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(List.of(Blocks.STONE,Blocks.DEEPSLATE))),
                RandomOffsetPlacement.vertical(ConstantInt.of(2)),
                BiomeFilter.biome()
        );
    }


    private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }

    public static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,name));
    }

    public static void register(
            BootstrapContext<PlacedFeature> context,
            ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> configuredFeature,
            List<PlacementModifier> placements
    ) {
        context.register(key, new PlacedFeature(configuredFeature, List.copyOf(placements)));
    }

    public static void registerAccessor(
            BootstrapContext<PlacedFeature> context,
            ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> configuredFeature,
            PlacementModifier... placements
    ) {
        register(context, key, configuredFeature, List.of(placements));
    }
}
