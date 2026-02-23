package net.bcm.cmatd.block;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.custom.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CmatdBlock{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Cmatd.MODID);

    public static final DeferredBlock<Block> MACHINE_FRAME = BLOCKS.register("machine_frame",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BIT).isViewBlocking(CmatdBlock::never)
                    .isSuffocating(CmatdBlock::never).noOcclusion())); // has recipe

    public static final DeferredBlock<Block> LESSER_MACHINE_FRAME = BLOCKS.register("lesser_machine_frame",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BIT).isViewBlocking(CmatdBlock::never)
                    .isSuffocating(CmatdBlock::never).noOcclusion())); // has recipe

    // machines and generators

    public static final DeferredBlock<Block> BASE_ENERGY_MAKER = BLOCKS.register("base_energy_maker",
            () -> new BaseEnergyMaker(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> DECORATIVE_BASE_DYNAMO_ENGINE = BLOCKS.register("decorative_base_dynamo_engine",
            () -> new DecorativeBaseDynamoEngine(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> BASE_COBBLE_MAKER = BLOCKS.register("base_cobble_maker",
            () -> new BaseCobbleMaker(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> REDSTONE_DYNAMO_ENGINE = BLOCKS.register("redstone_dynamo_engine",
            () -> new RedstoneDynamoEngine(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> MASHER = BLOCKS.register("masher",
            () -> new Masher(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> JAM_MAKER = BLOCKS.register("jam_maker",
            () -> new JamMaker(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> PRESSER = BLOCKS.register("presser",
            () -> new Presser(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> WIND_GENERATOR = BLOCKS.register("wind_generator",
            () -> new WindGenerator(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> SOLAR_GENERATOR = BLOCKS.register("solar_generator",
            () -> new SolarGenerator(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> LUNAR_GENERATOR = BLOCKS.register("lunar_generator",
            () -> new LunarGenerator(BlockBehaviour.Properties.of())); // has recipe

    public static final DeferredBlock<Block> HYDRO_GENERATOR = BLOCKS.register("hydro_generator",
            () -> new HydroGenerator(BlockBehaviour.Properties.of())); // has recipe

    // multiblocks
    public static final DeferredBlock<Block> FOOD_REACTOR_MULTIBLOCK = BLOCKS.register("food_reactor",
            () -> new FoodReactorBlock(BlockBehaviour.Properties.of()));
    // end multiblocks


    public static final DeferredBlock<Block> LIGHTNING_GENERATOR = BLOCKS.register("lightning_generator",
            () -> new LightningGenerator(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> HEAT_GENERATOR = BLOCKS.register("heat_generator",
            () -> new HeatGenerator(BlockBehaviour.Properties.of()));


    // conduits
    public static final DeferredBlock<Block> CONDUIT = BLOCKS.register("conduit_cable",
            () -> new ConduitCableBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> FACADE_CONDUIT = BLOCKS.register("facade_conduit_cable",
            () -> new FacadeConduitCableBlock(BlockBehaviour.Properties.of()));

    // storage blocks
    public static final DeferredBlock<Block> GAS_TANK = BLOCKS.register("gas_tank",
            () -> new GasTank(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> TEST_GAS_GENERATOR = BLOCKS.register("test_gas_generator",
            () -> new TestGasGenerator(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> DIESEL_ENGINE = BLOCKS.register("diesel_engine",
            () -> new DieselEngine(BlockBehaviour.Properties.of()));

    // ores
    public static final DeferredBlock<Block> COMPOUNDITE_ORE = BLOCKS.register("compoundite_ore",
            () -> new CustomOreBlock(1,2,2.5f,2.5f,
                    BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_COMPOUNDITE_ORE = BLOCKS.register("deepslate_compoundite_ore",
            () -> new CustomOreBlock(1,2,2.75f,2.75f,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> LODEALITE_ORE = BLOCKS.register("lodealite_ore",
            () -> new CustomOreBlock(2,3,3.0f,3.0f,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_LODEALITE_ORE = BLOCKS.register("deepslate_lodealite_ore",
            () -> new CustomOreBlock(2,3,3.25f,3.25f,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> RAW_COMPOUNDITE_BLOCK = BLOCKS.register("raw_compoundite_block",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_ORANGE).sound(SoundType.TUFF)
                    .strength(2.5f,2.5f)));

    public static final DeferredBlock<Block> COMPOUNDITE_BLOCK = BLOCKS.register("compoundite_block",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_ORANGE).sound(SoundType.TUFF)
                    .strength(3.0f,3.0f)));


    public static final DeferredBlock<Block> RAW_LODEALITE_BLOCK = BLOCKS.register("raw_lodealite_block",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GILDED_BLACKSTONE)
                    .strength(3.0f,3.0f)));

    public static final DeferredBlock<Block> LODEALITE_BLOCK = BLOCKS.register("lodealite_block",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GILDED_BLACKSTONE)
                    .strength(3.5f,3.5f)));

    // state predicates
    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }
}
