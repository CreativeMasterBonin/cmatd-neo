package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.DripstoneClusterFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class ConfiguredFeatureGen {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COMPOUNDITE = registerKey("ore_compoundite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LODEALITE = registerKey("ore_lodealite");

    public static final ResourceKey<ConfiguredFeature<?,?>> METHANE_GAS_VENT = registerKey("methane_gas_vent");
    public static final ResourceKey<ConfiguredFeature<?,?>> STEAM_GAS_VENT = registerKey("steam_gas_vent");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context){
        RuleTest stone_ore_replaces = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslate_ore_replaces = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> compounditeOreList = List.of(
                OreConfiguration.target(stone_ore_replaces,CmatdBlock.COMPOUNDITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslate_ore_replaces,CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> lodealiteOreList = List.of(
                OreConfiguration.target(stone_ore_replaces,CmatdBlock.LODEALITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslate_ore_replaces,CmatdBlock.DEEPSLATE_LODEALITE_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> methaneGasVents = List.of(
                OreConfiguration.target(stone_ore_replaces,CmatdBlock.METHANE_GAS_VENT.get().defaultBlockState()),
                OreConfiguration.target(deepslate_ore_replaces,CmatdBlock.DEEPSLATE_METHANE_GAS_VENT.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> steamGasVents = List.of(
                OreConfiguration.target(stone_ore_replaces,CmatdBlock.STEAM_GAS_VENT.get().defaultBlockState()),
                OreConfiguration.target(deepslate_ore_replaces,CmatdBlock.DEEPSLATE_STEAM_GAS_VENT.get().defaultBlockState())
        );

        register(context,ORE_COMPOUNDITE,Feature.ORE, new OreConfiguration(compounditeOreList,7));
        register(context,ORE_LODEALITE,Feature.ORE, new OreConfiguration(lodealiteOreList,5));

        register(context,METHANE_GAS_VENT,Feature.SCATTERED_ORE, new OreConfiguration(methaneGasVents,18));
        register(context,STEAM_GAS_VENT,Feature.SCATTERED_ORE, new OreConfiguration(steamGasVents,17));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
