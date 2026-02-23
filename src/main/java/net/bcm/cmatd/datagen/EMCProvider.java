package net.bcm.cmatd.datagen;

import moze_intel.projecte.api.data.CustomConversionProvider;
import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class EMCProvider extends CustomConversionProvider {
    protected EMCProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Cmatd.MODID);
    }

    @Override
    protected void addCustomConversions(HolderLookup.Provider provider) {
        createConversionBuilder(ResourceLocation.parse("cmatd:emc_conversions"))
                .before(CmatdItem.LODEALITE_INGOT,256)
                .before(CmatdItem.COMPOUNDITE_INGOT,128)
                .before(CmatdItem.LODEALITE_BLOCK,2304)
                .before(CmatdItem.COMPOUNDITE_BLOCK,1152)
                .before(CmatdItem.PCB,1292)
                .before(CmatdItem.PLATE,626)
                .before(CmatdItem.SWEET_BERRY_JAM,44)
                .before(CmatdItem.GLOW_BERRY_JAM,44)
                .before(CmatdItem.MASHED_POTATOES,64)
                .before(CmatdItem.POISONOUS_MASHED_POTATOES,64)
                .comment("All CMATD items that are bases for other items and blocks")
                .group("Cmatd Items")
        ;
    }
}
