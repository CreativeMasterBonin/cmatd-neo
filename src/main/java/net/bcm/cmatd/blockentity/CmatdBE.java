package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CmatdBE {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, Cmatd.MODID);

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

    public static final Supplier<BlockEntityType<BaseEnergyMakerBE>> BASE_ENERGY_MAKER_BE = BLOCK_ENTITIES.register(
            "base_energy_maker_be",() ->
                    BlockEntityType.Builder.of(BaseEnergyMakerBE::new, CmatdBlock.BASE_ENERGY_MAKER.get())
                            .build(null));

    public static final Supplier<BlockEntityType<DecorativeBaseDynamoEngineBE>> DECORATIVE_BASE_DYNAMO_ENGINE = BLOCK_ENTITIES.register(
            "decorative_base_dynamo_engine_be",() ->
                    BlockEntityType.Builder.of(DecorativeBaseDynamoEngineBE::new, CmatdBlock.DECORATIVE_BASE_DYNAMO_ENGINE.get())
                            .build(null));

    public static final Supplier<BlockEntityType<BaseCobbleMakerBE>> BASE_COBBLE_MAKER = BLOCK_ENTITIES.register(
            "base_cobble_maker_be",() ->
                    BlockEntityType.Builder.of(BaseCobbleMakerBE::new, CmatdBlock.BASE_COBBLE_MAKER.get())
                            .build(null));

    public static final Supplier<BlockEntityType<RedstoneDynamoEngineBE>> REDSTONE_DYNAMO_ENGINE = BLOCK_ENTITIES.register(
            "redstone_dynamo_engine_be",() ->
                    BlockEntityType.Builder.of(RedstoneDynamoEngineBE::new, CmatdBlock.REDSTONE_DYNAMO_ENGINE.get())
                            .build(null));

    public static final Supplier<BlockEntityType<JamMakerBE>> JAM_MAKER = BLOCK_ENTITIES.register(
            "jam_maker",() ->
                    BlockEntityType.Builder.of(JamMakerBE::new, CmatdBlock.JAM_MAKER.get())
                            .build(null));

    public static final Supplier<BlockEntityType<WindGeneratorBE>> WIND_GENERATOR = BLOCK_ENTITIES.register(
            "wind_generator",() ->
                    BlockEntityType.Builder.of(WindGeneratorBE::new, CmatdBlock.WIND_GENERATOR.get())
                            .build(null));

    public static final Supplier<BlockEntityType<SolarGeneratorBE>> SOLAR_GENERATOR = BLOCK_ENTITIES.register(
            "solar_generator",() ->
                    BlockEntityType.Builder.of(SolarGeneratorBE::new, CmatdBlock.SOLAR_GENERATOR.get())
                            .build(null));

    public static final Supplier<BlockEntityType<LunarGeneratorBE>> LUNAR_GENERATOR = BLOCK_ENTITIES.register(
            "lunar_generator",() ->
                    BlockEntityType.Builder.of(LunarGeneratorBE::new, CmatdBlock.LUNAR_GENERATOR.get())
                            .build(null));

    public static final Supplier<BlockEntityType<HydroGeneratorBE>> HYDRO_GENERATOR = BLOCK_ENTITIES.register(
            "hydro_generator",() ->
                    BlockEntityType.Builder.of(HydroGeneratorBE::new, CmatdBlock.HYDRO_GENERATOR.get())
                            .build(null));

    public static final Supplier<BlockEntityType<PresserBE>> PRESSER = BLOCK_ENTITIES.register(
            "presser",() ->
                    BlockEntityType.Builder.of(PresserBE::new, CmatdBlock.PRESSER.get())
                            .build(null));

    public static final Supplier<BlockEntityType<FoodReactorMultiblock>> FOOD_REACTOR = BLOCK_ENTITIES.register(
            "food_reactor",() ->
                    BlockEntityType.Builder.of(FoodReactorMultiblock::new, CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get())
                            .build(null));

    public static final Supplier<BlockEntityType<LightningGeneratorBE>> LIGHTNING_GENERATOR = BLOCK_ENTITIES.register(
            "lightning_generator",() ->
                    BlockEntityType.Builder.of(LightningGeneratorBE::new, CmatdBlock.LIGHTNING_GENERATOR.get())
                            .build(null));

    public static final Supplier<BlockEntityType<HeatGeneratorBE>> HEAT_GENERATOR = BLOCK_ENTITIES.register(
            "heat_generator",() ->
                    BlockEntityType.Builder.of(HeatGeneratorBE::new, CmatdBlock.HEAT_GENERATOR.get())
                            .build(null));

    public static final Supplier<BlockEntityType<GasTankBE>> GAS_TANK = BLOCK_ENTITIES.register(
            "gas_tank",() ->
                    BlockEntityType.Builder.of(GasTankBE::new, CmatdBlock.GAS_TANK.get())
                            .build(null));

    public static final Supplier<BlockEntityType<TestGasGeneratorBE>> TEST_GAS_GENERATOR = BLOCK_ENTITIES.register(
            "test_gas_generator",() ->
                    BlockEntityType.Builder.of(TestGasGeneratorBE::new, CmatdBlock.TEST_GAS_GENERATOR.get())
                            .build(null));

    public static final Supplier<BlockEntityType<ConduitBE>> CONDUIT = BLOCK_ENTITIES.register(
            "conduit_cable",() ->
                    BlockEntityType.Builder.of(ConduitBE::new, CmatdBlock.CONDUIT.get())
                            .build(null));

    public static final Supplier<BlockEntityType<FacadeConduitBE>> FACADE_CONDUIT = BLOCK_ENTITIES.register(
            "facade_conduit_cable",() ->
                    BlockEntityType.Builder.of(FacadeConduitBE::new, CmatdBlock.FACADE_CONDUIT.get())
                            .build(null));

    public static final Supplier<BlockEntityType<DieselEngineBE>> DIESEL_ENGINE = BLOCK_ENTITIES.register(
            "diesel_engine",() ->
                    BlockEntityType.Builder.of(DieselEngineBE::new, CmatdBlock.DIESEL_ENGINE.get())
                            .build(null));
    /*
    public static final Supplier<BlockEntityType<MasherBE>> MASHER = BLOCK_ENTITIES.register(
            "masher_be",() ->
                    BlockEntityType.Builder.of(MasherBE::new, CmatdBlock.MASHER.get())
                            .build(null));*/
}

