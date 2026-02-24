package net.bcm.cmatd.datagen;

import com.direwolf20.justdirethings.datagen.JustDireBlockTags;
import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Cmatd.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tag.ORES_COMPOUNDITE)
                .add(CmatdBlock.COMPOUNDITE_ORE.get())
                .add(CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get())
        ;
        tag(Tag.ORES_LODEALITE)
                .add(CmatdBlock.LODEALITE_ORE.get())
                .add(CmatdBlock.DEEPSLATE_LODEALITE_ORE.get())
        ;
        tag(Tag.STORAGE_BLOCKS_COMPOUNDITE)
                .add(CmatdBlock.COMPOUNDITE_BLOCK.get())
        ;
        tag(Tag.STORAGE_BLOCKS_LODEALITE)
                .add(CmatdBlock.LODEALITE_BLOCK.get())
        ;
        tag(Tag.STORAGE_BLOCKS_RAW_COMPOUNDITE)
                .add(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get())
        ;
        tag(Tag.STORAGE_BLOCKS_RAW_LODEALITE)
                .add(CmatdBlock.RAW_LODEALITE_BLOCK.get())
        ;
        tag(Tags.Blocks.STORAGE_BLOCKS)
                .addTag(Tag.STORAGE_BLOCKS_COMPOUNDITE)
                .addTag(Tag.STORAGE_BLOCKS_LODEALITE)
                .addTag(Tag.STORAGE_BLOCKS_RAW_COMPOUNDITE)
                .addTag(Tag.STORAGE_BLOCKS_RAW_LODEALITE)
        ;
        tag(Tags.Blocks.RELOCATION_NOT_SUPPORTED)
                .add(CmatdBlock.BASE_ENERGY_MAKER.get())
                .add(CmatdBlock.DECORATIVE_BASE_DYNAMO_ENGINE.get())
                .add(CmatdBlock.BASE_COBBLE_MAKER.get())
                .add(CmatdBlock.REDSTONE_DYNAMO_ENGINE.get())
                .add(CmatdBlock.MASHER.get())
                .add(CmatdBlock.JAM_MAKER.get())
                .add(CmatdBlock.WIND_GENERATOR.get())
                .add(CmatdBlock.SOLAR_GENERATOR.get())
                .add(CmatdBlock.LUNAR_GENERATOR.get())
                .add(CmatdBlock.HYDRO_GENERATOR.get())
                .add(CmatdBlock.PRESSER.get())
                .add(CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get())
                .add(CmatdBlock.LIGHTNING_GENERATOR.get())
                .add(CmatdBlock.HEAT_GENERATOR.get())
                .add(CmatdBlock.GAS_TANK.get())
                .add(CmatdBlock.DIESEL_ENGINE.get())
        ;
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CmatdBlock.BASE_ENERGY_MAKER.get())
                .add(CmatdBlock.DECORATIVE_BASE_DYNAMO_ENGINE.get())
                .add(CmatdBlock.BASE_COBBLE_MAKER.get())
                .add(CmatdBlock.REDSTONE_DYNAMO_ENGINE.get())
                .add(CmatdBlock.MASHER.get())
                .add(CmatdBlock.JAM_MAKER.get())
                .add(CmatdBlock.WIND_GENERATOR.get())
                .add(CmatdBlock.SOLAR_GENERATOR.get())
                .add(CmatdBlock.LUNAR_GENERATOR.get())
                .add(CmatdBlock.HYDRO_GENERATOR.get())
                .add(CmatdBlock.MACHINE_FRAME.get())
                .addTag(Tag.ORES_COMPOUNDITE)
                .addTag(Tag.ORES_LODEALITE)
                .add(CmatdBlock.PRESSER.get())
                .add(CmatdBlock.LESSER_MACHINE_FRAME.get())
                .add(CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get())
                .add(CmatdBlock.LIGHTNING_GENERATOR.get())
                .add(CmatdBlock.HEAT_GENERATOR.get())
                .add(CmatdBlock.GAS_TANK.get())
                .add(CmatdBlock.DIESEL_ENGINE.get())
                .add(CmatdBlock.COMPOUNDITE_BLOCK.get())
                .add(CmatdBlock.LODEALITE_BLOCK.get())
                .add(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get())
                .add(CmatdBlock.RAW_LODEALITE_BLOCK.get())
        ;
        tag(BlockTags.NEEDS_STONE_TOOL)
                .addTag(Tag.ORES_COMPOUNDITE)
                .add(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get())
                .add(CmatdBlock.COMPOUNDITE_BLOCK.get())
        ;
        tag(BlockTags.NEEDS_IRON_TOOL)
                .addTag(Tag.ORES_LODEALITE)
                .add(CmatdBlock.RAW_LODEALITE_BLOCK.get())
                .add(CmatdBlock.LODEALITE_BLOCK.get())
                // machines
                .add(CmatdBlock.BASE_ENERGY_MAKER.get())
                .add(CmatdBlock.DECORATIVE_BASE_DYNAMO_ENGINE.get())
                .add(CmatdBlock.BASE_COBBLE_MAKER.get())
                .add(CmatdBlock.REDSTONE_DYNAMO_ENGINE.get())
                .add(CmatdBlock.MASHER.get())
                .add(CmatdBlock.JAM_MAKER.get())
                .add(CmatdBlock.WIND_GENERATOR.get())
                .add(CmatdBlock.SOLAR_GENERATOR.get())
                .add(CmatdBlock.LUNAR_GENERATOR.get())
                .add(CmatdBlock.HYDRO_GENERATOR.get())
                //
                .add(CmatdBlock.MACHINE_FRAME.get())
                .add(CmatdBlock.PRESSER.get())
                .add(CmatdBlock.LESSER_MACHINE_FRAME.get())
                .add(CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get())
                .add(CmatdBlock.LIGHTNING_GENERATOR.get())
                .add(CmatdBlock.HEAT_GENERATOR.get())
                .add(CmatdBlock.GAS_TANK.get())
                .add(CmatdBlock.DIESEL_ENGINE.get())
        ;
        tag(JustDireBlockTags.PARADOX_ABSORB_DENY)
                .add(CmatdBlock.BASE_ENERGY_MAKER.get())
                .add(CmatdBlock.DECORATIVE_BASE_DYNAMO_ENGINE.get())
                .add(CmatdBlock.BASE_COBBLE_MAKER.get())
                .add(CmatdBlock.REDSTONE_DYNAMO_ENGINE.get())
                .add(CmatdBlock.MASHER.get())
                .add(CmatdBlock.JAM_MAKER.get())
                .add(CmatdBlock.WIND_GENERATOR.get())
                .add(CmatdBlock.SOLAR_GENERATOR.get())
                .add(CmatdBlock.LUNAR_GENERATOR.get())
                .add(CmatdBlock.HYDRO_GENERATOR.get())
                .add(CmatdBlock.MACHINE_FRAME.get())
                .add(CmatdBlock.PRESSER.get())
                .add(CmatdBlock.LESSER_MACHINE_FRAME.get())
                .add(CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get())
                .add(CmatdBlock.LIGHTNING_GENERATOR.get())
                .add(CmatdBlock.HEAT_GENERATOR.get())
                .add(CmatdBlock.GAS_TANK.get())
                .add(CmatdBlock.DIESEL_ENGINE.get())
        ;
        tag(Tags.Blocks.ORES)
                .addTag(Tag.ORES_COMPOUNDITE)
                .addTag(Tag.ORES_LODEALITE)
        ;
        tag(Tags.Blocks.ORES_IN_GROUND_STONE)
                .add(CmatdBlock.COMPOUNDITE_ORE.get())
                .add(CmatdBlock.LODEALITE_ORE.get())
        ;
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
                .add(CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get())
                .add(CmatdBlock.DEEPSLATE_LODEALITE_ORE.get())
        ;
        tag(Tag.VALID_FOOD_REACTOR_CASINGS)
                .addTag(Tag.STORAGE_BLOCKS_COMPOUNDITE)
                .addTag(Tag.STORAGE_BLOCKS_LODEALITE)
        ;
        // heat producers
        tag(Tag.LOW_HEAT_PRODUCERS)
                .add(Blocks.TORCH)
                .add(Blocks.WALL_TORCH)
                .add(Blocks.SOUL_TORCH)
                .add(Blocks.SOUL_WALL_TORCH)
                .add(Blocks.LANTERN)
                .add(Blocks.SOUL_LANTERN)

                .add(Blocks.CANDLE)
                .add(Blocks.CANDLE_CAKE)

                .add(Blocks.RED_CANDLE) // 1
                .add(Blocks.RED_CANDLE_CAKE)
                .add(Blocks.ORANGE_CANDLE) // 2
                .add(Blocks.ORANGE_CANDLE_CAKE)
                .add(Blocks.YELLOW_CANDLE) // 3
                .add(Blocks.YELLOW_CANDLE_CAKE)
                .add(Blocks.LIME_CANDLE) // 4
                .add(Blocks.LIME_CANDLE_CAKE)
                .add(Blocks.GREEN_CANDLE) // 5
                .add(Blocks.GREEN_CANDLE_CAKE)
                .add(Blocks.CYAN_CANDLE) // 6
                .add(Blocks.CYAN_CANDLE_CAKE)
                .add(Blocks.LIGHT_BLUE_CANDLE) // 7
                .add(Blocks.LIGHT_BLUE_CANDLE_CAKE)
                .add(Blocks.BLUE_CANDLE) // 8
                .add(Blocks.BLUE_CANDLE_CAKE)
                .add(Blocks.PURPLE_CANDLE) // 9
                .add(Blocks.PURPLE_CANDLE_CAKE)
                .add(Blocks.MAGENTA_CANDLE) // 10
                .add(Blocks.MAGENTA_CANDLE_CAKE)
                .add(Blocks.PINK_CANDLE) // 11
                .add(Blocks.PINK_CANDLE_CAKE)
                .add(Blocks.BROWN_CANDLE) // 12
                .add(Blocks.BROWN_CANDLE_CAKE)
                .add(Blocks.GRAY_CANDLE) // 13
                .add(Blocks.GRAY_CANDLE_CAKE)
                .add(Blocks.LIGHT_GRAY_CANDLE) // 14
                .add(Blocks.LIGHT_GRAY_CANDLE_CAKE)
                .add(Blocks.WHITE_CANDLE) // 15
                .add(Blocks.WHITE_CANDLE_CAKE)
                .add(Blocks.BLACK_CANDLE) // 16
                .add(Blocks.BLACK_CANDLE_CAKE)
                .add(Blocks.JACK_O_LANTERN)
        ;
        tag(Tag.MEDIUM_HEAT_PRODUCERS)
                .add(Blocks.FIRE)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.CAMPFIRE)
                .add(Blocks.LAVA_CAULDRON)
                .add(Blocks.FURNACE)
                .add(Blocks.BLAST_FURNACE)
                .add(Blocks.SMOKER)
        ;
        tag(Tag.HIGH_HEAT_PRODUCERS)
                .add(Blocks.SOUL_FIRE)
                .add(Blocks.SOUL_CAMPFIRE)
                .add(Blocks.LAVA)
        ;
        tag(Tag.HEAT_PRODUCERS)
                .addTag(Tag.LOW_HEAT_PRODUCERS)
                .addTag(Tag.MEDIUM_HEAT_PRODUCERS)
                .addTag(Tag.HIGH_HEAT_PRODUCERS)
        ;
    }

    @Override
    public String getName() {
        return "CMATD Block Tags";
    }
}
