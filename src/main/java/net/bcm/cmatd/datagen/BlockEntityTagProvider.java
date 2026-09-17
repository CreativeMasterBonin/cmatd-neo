package net.bcm.cmatd.datagen;

import moze_intel.projecte.gameObjs.PETags;
import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.blockentity.CmatdBE;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockEntityTagProvider extends TagsProvider<BlockEntityType<?>> {
    public BlockEntityTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK_ENTITY_TYPE, lookupProvider, Cmatd.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(PETags.BlockEntities.BLACKLIST_TIME_WATCH)
                .add(CmatdBE.DECORATIVE_BASE_DYNAMO_ENGINE.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.REDSTONE_DYNAMO_ENGINE.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.SOLAR_GENERATOR.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.LUNAR_GENERATOR.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.WIND_GENERATOR.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.HEAT_GENERATOR.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.HYDRO_GENERATOR.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.LIGHTNING_GENERATOR.get().builtInRegistryHolder().getKey())

                .add(CmatdBE.ROTATIONAL_INDUCTION_GENERATOR.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.DIESEL_ENGINE.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.GAS_TANK.get().builtInRegistryHolder().getKey())

                .add(CmatdBE.FOOD_REACTOR.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.RADIOACTIVE_REACTOR.get().builtInRegistryHolder().getKey())

                .add(CmatdBE.CONDUIT.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.FACADE_CONDUIT.get().builtInRegistryHolder().getKey())
                .add(CmatdBE.DIMENSIONAL_TRANSPORTER.get().builtInRegistryHolder().getKey())
        ;
    }
}
