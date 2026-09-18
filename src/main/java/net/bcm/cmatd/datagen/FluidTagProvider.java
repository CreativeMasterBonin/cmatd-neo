package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.fluid.CmatdFluid;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class FluidTagProvider extends FluidTagsProvider {
    public FluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Cmatd.MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        tag(Tags.Fluids.HIDDEN_FROM_RECIPE_VIEWERS)
                .add(CmatdFluid.RADIOACTIVE_WASTE_FLUID_SOURCE.get())
                .add(CmatdFluid.RADIOACTIVE_WASTE_FLUID_FLOWING.get())
        ;
    }

    @Override
    public String getName() {
        return "CMATD Fluid Tags";
    }
}
