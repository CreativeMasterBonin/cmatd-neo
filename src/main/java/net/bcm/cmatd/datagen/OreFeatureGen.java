package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class OreFeatureGen{
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COMPOUNDITE = registerKey("ore_compoundite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LODEALITE = registerKey("ore_lodealite");

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

        register(context,ORE_COMPOUNDITE,Feature.ORE, new OreConfiguration(compounditeOreList,7));
        register(context,ORE_LODEALITE,Feature.ORE, new OreConfiguration(lodealiteOreList,5));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
