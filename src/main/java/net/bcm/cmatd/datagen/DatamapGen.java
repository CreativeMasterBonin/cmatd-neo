package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
    }
}
