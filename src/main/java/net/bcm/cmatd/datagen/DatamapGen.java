package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class DatamapGen extends DataMapProvider {
    protected DatamapGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(Cmatd.MASHABLES)
                .replace(false)
                .add(Items.POTATO.builtInRegistryHolder(),new Mashables(CmatdItem.MASHED_POTATOES.asItem()),false)
                .add(Items.POISONOUS_POTATO.builtInRegistryHolder(),new Mashables(CmatdItem.POISONOUS_MASHED_POTATOES.asItem()),false)
        ;
        builder(Cmatd.JAMMABLES)
                .replace(false)
                .add(Items.SWEET_BERRIES.builtInRegistryHolder(),new Jammables(CmatdItem.SWEET_BERRY_JAM.asItem()),false)
                .add(Items.GLOW_BERRIES.builtInRegistryHolder(),new Jammables(CmatdItem.GLOW_BERRY_JAM.asItem()),false)
        ;
        builder(Cmatd.FOOD_REACTOR_FUELS)
                .replace(false)
                .add(CmatdItem.POISONOUS_MASHED_POTATOES.asItem().builtInRegistryHolder(),
                        new FoodReactorFuels(100, UniformFloat.of(0.65f,0.75f)),false)
                .add(CmatdItem.MASHED_POTATOES.asItem().builtInRegistryHolder(),
                        new FoodReactorFuels(100, UniformFloat.of(0.75f,0.87f)),false)

                .add(Items.CARROT.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.04f,0.9f)),
                        false)
                .add(Items.BAKED_POTATO.builtInRegistryHolder(),
                        new FoodReactorFuels(35, UniformFloat.of(0.15f,0.20f)),
                        false)
                .add(Items.SWEET_BERRIES.builtInRegistryHolder(),
                        new FoodReactorFuels(75, UniformFloat.of(0.04f,0.07f)),
                        false)
                .add(Items.BREAD.builtInRegistryHolder(),
                        new FoodReactorFuels(10, UniformFloat.of(0.45f,0.57f)),
                        false)
                .add(Items.COOKIE.builtInRegistryHolder(),
                        new FoodReactorFuels(10, UniformFloat.of(0.45f,0.57f)),
                        false)
                .add(Items.CAKE.builtInRegistryHolder(),
                        new FoodReactorFuels(32, UniformFloat.of(0.55f,0.67f)),
                        false)
                .add(Items.PUMPKIN.builtInRegistryHolder(),
                        new FoodReactorFuels(12, UniformFloat.of(0.15f,0.37f)),
                        false)
                .add(Items.CARVED_PUMPKIN.builtInRegistryHolder(),
                        new FoodReactorFuels(7, UniformFloat.of(0.07f,0.11f)),
                        false)
                .add(Items.SPIDER_EYE.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.001f,0.004f)),
                        false)
                .add(Items.FERMENTED_SPIDER_EYE.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.001f,0.002f)),
                        false)
                .add(Items.ROTTEN_FLESH.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.001f,0.003f)),
                        false)
                .add(Items.DRIED_KELP.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.001f,0.01f)),
                        false)

                .add(Items.BEEF.builtInRegistryHolder(),
                        new FoodReactorFuels(17, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.COOKED_BEEF.builtInRegistryHolder(),
                        new FoodReactorFuels(10, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.PORKCHOP.builtInRegistryHolder(),
                        new FoodReactorFuels(17, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.COOKED_PORKCHOP.builtInRegistryHolder(),
                        new FoodReactorFuels(10, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.MUTTON.builtInRegistryHolder(),
                        new FoodReactorFuels(17, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.COOKED_MUTTON.builtInRegistryHolder(),
                        new FoodReactorFuels(10, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.CHICKEN.builtInRegistryHolder(),
                        new FoodReactorFuels(17, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.COOKED_CHICKEN.builtInRegistryHolder(),
                        new FoodReactorFuels(10, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.RABBIT.builtInRegistryHolder(),
                        new FoodReactorFuels(17, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.COOKED_RABBIT.builtInRegistryHolder(),
                        new FoodReactorFuels(10, UniformFloat.of(0.017f,0.035f)),
                        false)
                .add(Items.COD.builtInRegistryHolder(),
                        new FoodReactorFuels(29, UniformFloat.of(0.02f,0.042f)),
                        false)
                .add(Items.COOKED_COD.builtInRegistryHolder(),
                        new FoodReactorFuels(21, UniformFloat.of(0.02f,0.042f)),
                        false)
                .add(Items.SALMON.builtInRegistryHolder(),
                        new FoodReactorFuels(29, UniformFloat.of(0.02f,0.042f)),
                        false)
                .add(Items.COOKED_SALMON.builtInRegistryHolder(),
                        new FoodReactorFuels(21, UniformFloat.of(0.02f,0.042f)),
                        false)
                .add(Items.TROPICAL_FISH.builtInRegistryHolder(),
                        new FoodReactorFuels(31, UniformFloat.of(0.04f,0.05f)),
                        false)
                .add(Items.PUFFERFISH.builtInRegistryHolder(),
                        new FoodReactorFuels(33, UniformFloat.of(0.04f,0.05f)),
                        false)

                .add(Items.PUMPKIN_PIE.builtInRegistryHolder(),
                        new FoodReactorFuels(42, UniformFloat.of(0.55f,0.67f)),
                        false)
                .add(Items.MILK_BUCKET.builtInRegistryHolder(),
                        new FoodReactorFuels(200, UniformFloat.of(0.89f,0.97f)),
                        false)
                .add(Items.HONEY_BOTTLE.builtInRegistryHolder(),
                        new FoodReactorFuels(80, UniformFloat.of(0.89f,0.96f)),
                        false)
                .add(Items.POTION.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.01f,0.03f)),
                        false)
                .add(Items.SPLASH_POTION.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.02f,0.04f)),
                        false)
                .add(Items.LINGERING_POTION.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.03f,0.05f)),
                        false)
                .add(Items.MUSHROOM_STEM.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.3f,0.5f)),
                        false)
                .add(Items.SUSPICIOUS_STEW.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.35f,0.74f)),
                        false)
                .add(Items.BEETROOT.builtInRegistryHolder(),
                        new FoodReactorFuels(125, UniformFloat.of(0.12f,0.23f)),
                        false)
                .add(Items.BEETROOT_SOUP.builtInRegistryHolder(),
                        new FoodReactorFuels(150, UniformFloat.of(0.21f,0.72f)),
                        false)
                .add(Items.GLOW_BERRIES.builtInRegistryHolder(),
                        new FoodReactorFuels(55, UniformFloat.of(0.02f,0.04f)),false)
                .add(Items.POISONOUS_POTATO.builtInRegistryHolder(),
                        new FoodReactorFuels(50, UniformFloat.of(0.21f,0.3f)),false)
                .add(Items.POTATO.builtInRegistryHolder(),
                        new FoodReactorFuels(50, UniformFloat.of(0.3f,0.4f)),false)
                .add(Items.APPLE.builtInRegistryHolder(),
                        new FoodReactorFuels(200, UniformFloat.of(0.15f,0.45f)),false)
                .add(Items.MELON.builtInRegistryHolder(),
                        new FoodReactorFuels(400, UniformFloat.of(0.35f,0.4f)),false)
                .add(Items.MELON_SLICE.builtInRegistryHolder(),
                        new FoodReactorFuels(5, UniformFloat.of(0.25f,0.35f)),false)
                .add(Items.GOLDEN_CARROT.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(0.5f,0.75f)),false)
                .add(Items.GOLDEN_APPLE.builtInRegistryHolder(),
                        new FoodReactorFuels(40, UniformFloat.of(0.85f,1.9f)),false)
                .add(Items.ENCHANTED_GOLDEN_APPLE.builtInRegistryHolder(),
                        new FoodReactorFuels(20, UniformFloat.of(3.0f,4.0f)),false)
                .add(Items.CHORUS_FRUIT.builtInRegistryHolder(),
                        new FoodReactorFuels(1, UniformFloat.of(4.2f,5.5f)),false)
        ;
    }
}
