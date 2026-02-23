package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrePlacementGen{
    public static final ResourceKey<PlacedFeature> ORE_COMPOUNDITE = createKey("ore_compoundite");
    public static final ResourceKey<PlacedFeature> ORE_LODEALITE = createKey("ore_lodealite");

    public static void bootstrap(BootstrapContext<PlacedFeature> context){
        HolderGetter<ConfiguredFeature<?, ?>> holdGetCombo = context.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> compounditeFeatureHolder = holdGetCombo.getOrThrow(OreFeatureGen.ORE_COMPOUNDITE);
        Holder<ConfiguredFeature<?, ?>> lodealiteFeatureHolder = holdGetCombo.getOrThrow(OreFeatureGen.ORE_LODEALITE);

        register(context,ORE_COMPOUNDITE,compounditeFeatureHolder,
                commonOrePlacement(25, HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.top())));

        register(context,ORE_LODEALITE,lodealiteFeatureHolder,
                commonOrePlacement(15, HeightRangePlacement.uniform(VerticalAnchor.absolute(-32), VerticalAnchor.top())));
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
