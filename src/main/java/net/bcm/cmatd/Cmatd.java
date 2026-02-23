package net.bcm.cmatd;

import com.mojang.logging.LogUtils;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.CmatdBE;
import net.bcm.cmatd.blockentity.CmatdFluidTank;
import net.bcm.cmatd.blockentity.FoodReactorMultiblock;
import net.bcm.cmatd.datagen.Jammables;
import net.bcm.cmatd.datagen.Mashables;
import net.bcm.cmatd.gui.*;
import net.bcm.cmatd.item.CmatdItem;
import net.bcm.cmatd.network.Handler;
import net.bcm.cmatd.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.*;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod(Cmatd.MODID)
public class Cmatd {
    public static final String MODID = "cmatd";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static Logger getLogger(){
        return LOGGER;
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("main_tab", () ->
            CreativeModeTab.builder().title(Component.translatable("itemGroup.cmatd"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(Items.CRAFTER::getDefaultInstance)
                    .displayItems((parameters, output) -> {
                        output.accept(CmatdItem.BASE_ENERGY_MAKER.asItem());
                        output.accept(CmatdItem.REDSTONE_DYNAMO_ENGINE.asItem());
                        output.accept(CmatdItem.DECORATIVE_BASE_DYNAMO_ENGINE.asItem());
                        output.accept(CmatdItem.BASE_COBBLE_MAKER.asItem());
                        output.accept(CmatdItem.MASHER.asItem());
                        output.accept(CmatdItem.JAM_MAKER.asItem());
                        output.accept(CmatdItem.PRESSER.asItem());
                        output.accept(CmatdItem.WIND_GENERATOR.asItem());
                        output.accept(CmatdItem.SOLAR_GENERATOR.asItem());
                        output.accept(CmatdItem.LUNAR_GENERATOR.asItem());
                        output.accept(CmatdItem.HYDRO_GENERATOR.asItem());
                        output.accept(CmatdItem.HEAT_GENERATOR.asItem());
                        output.accept(CmatdItem.LIGHTNING_GENERATOR.asItem());
                        output.accept(CmatdItem.FOOD_REACTOR_MULTIBLOCK.asItem());
                        // conduits
                        /*output.accept(CmatdItem.CONDUIT.asItem());
                        output.accept(CmatdItem.FACADE_CONDUIT.asItem());*/

                        // storages
                        output.accept(CmatdItem.GAS_TANK.asItem());
                        output.accept(CmatdItem.TEST_GAS_GENERATOR.asItem());
                        // engines
                        output.accept(CmatdItem.DIESEL_ENGINE.asItem());


                        output.accept(CmatdItem.CMATD_WRENCH.asItem());

                        output.accept(CmatdItem.BLANK_MODULE.asItem());
                        output.accept(CmatdItem.SPEED_MODULE.asItem());
                        output.accept(CmatdItem.EFFICIENCY_MODULE.asItem());
                        output.accept(CmatdItem.DOUBLER_MODULE.asItem());
                        output.accept(CmatdItem.TRIPLED_MODULE.asItem());

                        output.accept(CmatdItem.BASIC_TIER_DOWNGRADE.asItem());
                        output.accept(CmatdItem.ADVANCED_TIER_UPGRADE.asItem());
                        output.accept(CmatdItem.HIGHLY_ADVANCED_TIER_UPGRADE.asItem());
                        output.accept(CmatdItem.SUPERB_TIER_UPGRADE.asItem());
                        output.accept(CmatdItem.MAXIMUM_TIER_UPGRADE.asItem());

                        output.accept(CmatdItem.JAM_JAR.asItem());
                        output.accept(CmatdItem.SWEET_BERRY_JAM.asItem());
                        output.accept(CmatdItem.GLOW_BERRY_JAM.asItem());
                        output.accept(CmatdItem.MASHED_POTATOES.asItem());
                        output.accept(CmatdItem.POISONOUS_MASHED_POTATOES.asItem());

                        output.accept(CmatdItem.COMPOUNDITE_ORE.asItem());
                        output.accept(CmatdItem.DEEPSLATE_COMPOUNDITE_ORE.asItem());
                        output.accept(CmatdItem.RAW_COMPOUNDITE.asItem());
                        output.accept(CmatdItem.RAW_COMPOUNDITE_BLOCK.asItem());

                        output.accept(CmatdItem.LODEALITE_ORE.asItem());
                        output.accept(CmatdItem.DEEPSLATE_LODEALITE_ORE.asItem());
                        output.accept(CmatdItem.RAW_LODEALITE.asItem());
                        output.accept(CmatdItem.RAW_LODEALITE_BLOCK.asItem());

                        output.accept(CmatdItem.IRON_DUST.asItem());
                        output.accept(CmatdItem.GOLD_DUST.asItem());

                        output.accept(CmatdItem.COMPOUNDITE_INGOT.asItem());
                        output.accept(CmatdItem.COMPOUNDITE_BLOCK.asItem());
                        output.accept(CmatdItem.LODEALITE_INGOT.asItem());
                        output.accept(CmatdItem.LODEALITE_BLOCK.asItem());
                        output.accept(CmatdItem.INFUSED_INGOT.asItem());

                        output.accept(CmatdItem.PATTERN_BASE.asItem());
                        output.accept(CmatdItem.PCB_PATTERN.asItem());
                        output.accept(CmatdItem.PLATE_PATTERN.asItem());

                        output.accept(CmatdItem.RESISTOR.asItem());
                        output.accept(CmatdItem.TRANSISTOR.asItem());
                        output.accept(CmatdItem.CAPACITOR.asItem());

                        output.accept(CmatdItem.COPPER_PCB_BASE.asItem());
                        output.accept(CmatdItem.UNPRESSED_PCB.asItem());

                        output.accept(CmatdItem.PHOTOVOLTAIC_CELL.asItem());
                        output.accept(CmatdItem.LUNAR_PHOTOVOLTAIC_CELL.asItem());
                        output.accept(CmatdItem.PCB.asItem());
                        output.accept(CmatdItem.POWER_BOARD.asItem());
                        output.accept(CmatdItem.REDSTONE_ENERGY_COLUMN.asItem());
                        output.accept(CmatdItem.SAIL.asItem());

                        output.accept(CmatdItem.PLATE.asItem());
                        output.accept(CmatdItem.LESSER_MACHINE_FRAME.asItem());
                        output.accept(CmatdItem.MACHINE_FRAME.asItem());
                            }).build());

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES,MODID);

    public static final Supplier<AttachmentType<CmatdFluidTank>> FOOD_REACTOR_FLUID_HANDLER = ATTACHMENTS.register(
            "food_reactor_fluid_handler", () -> AttachmentType.serializable(holder -> {
                if (holder instanceof FoodReactorMultiblock be)
                    return new CmatdFluidTank(Utility.FOOD_REACTOR_FLUID_CAPACITY);
                return new CmatdFluidTank(0);
            }).build());



    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER,MODID);

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE,MODID);

    public static final DataMapType<Item, Mashables> MASHABLES = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MODID,"mashables"),
            Registries.ITEM, Mashables.CODEC).synced(Mashables.MASHABLE_CODEC,
            false).build();

    public static final DataMapType<Item, Jammables> JAMMABLES = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MODID,"jammables"),
            Registries.ITEM,Jammables.CODEC).synced(Jammables.JAMMABLES_CODEC,
            false).build();


    public Cmatd(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(this::registerListeners);
        //
        modEventBus.addListener(Handler::register);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);

        Gases.GASES.register(modEventBus);

        CmatdSound.SOUND_EVENTS.register(modEventBus);
        Components.setup(modEventBus);
        ATTACHMENTS.register(modEventBus);
        modEventBus.addListener(this::registerDatamaps);

        CmatdMenu.MENUS.register(modEventBus);
        CmatdBlock.BLOCKS.register(modEventBus);
        CmatdItem.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        CmatdBE.register(modEventBus);

        //NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        // do config things
        //modContainer.registerConfig(ModConfig.Type.COMMON,Config.SPEC);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event){
        // energy maker mk1
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,CmatdBE.BASE_ENERGY_MAKER_BE.get(),
                (o,direction) -> o.getItemHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.BASE_ENERGY_MAKER_BE.get(),
                (o,direction) -> o.getEnergyStorage());

        // cobble maker mk1
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,CmatdBE.BASE_COBBLE_MAKER.get(),
                (o,direction) -> o.getItemHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.BASE_COBBLE_MAKER.get(),
                (o,direction) -> o.getEnergyStorage());

        // rs engine
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.REDSTONE_DYNAMO_ENGINE.get(),
                (o,direction) -> o.getEnergyStorage());

        // jam maker
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,CmatdBE.JAM_MAKER.get(),
                (o,direction) -> o.getItemHandler());

        // wind generator
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.WIND_GENERATOR.get(),
                (o, direction) -> o.getEnergyStorage());

        // solar generator
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.SOLAR_GENERATOR.get(),
                (o, direction) -> o.getEnergyStorage());

        // lunar generator
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.LUNAR_GENERATOR.get(),
                (o, direction) -> o.getEnergyStorage());

        // hydro generator
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.HYDRO_GENERATOR.get(),
                (o, direction) -> o.getEnergyStorage());

        // presser
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,CmatdBE.PRESSER.get(),
                (o, direction) -> o.getItemHandler());

        // food reactor
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,CmatdBE.FOOD_REACTOR.get(),
                (o, direction) -> o.getItemStackHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.FOOD_REACTOR.get(),
                (o, direction) -> o.getEnergyStorage());


        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,CmatdBE.FOOD_REACTOR.get(),
                (o, direction) -> o.getData(FOOD_REACTOR_FLUID_HANDLER));

        // lightning gen
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.LIGHTNING_GENERATOR.get(),
                (o, direction) -> o.getEnergyStorage());

        // heat gen
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.HEAT_GENERATOR.get(),
                (o,direction) -> o.getEnergyStorage());

        // conduits
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.CONDUIT.get(),
                (o,direction) -> o.getEnergyStorage());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,CmatdBE.FACADE_CONDUIT.get(),
                (o,direction) -> o.getEnergyStorage());


        // gas related machines and generators
        // gas generator
        event.registerBlockEntity(net.bcm.cmatd.api.Capabilities.GasHandler.BLOCK,CmatdBE.TEST_GAS_GENERATOR.get(),
                (o, direction) -> o.getGasTank());

        // gas tank
        event.registerBlockEntity(net.bcm.cmatd.api.Capabilities.GasHandler.BLOCK,CmatdBE.GAS_TANK.get(),
                (o,direction) -> o.getGasTank());


        // diesel engine
        event.registerBlockEntity(net.bcm.cmatd.api.Capabilities.GasHandler.BLOCK,CmatdBE.DIESEL_ENGINE.get(),
                (o,direction) -> o.getGasTank());
    }

    private void registerRegistries(NewRegistryEvent event){
        event.register(net.bcm.cmatd.api.Registries.GAS_TYPES);
    }
    private void registerListeners(RegisterEvent event){
        event.register(net.bcm.cmatd.api.Registries.GAS_TYPES_KEY,
                net.bcm.cmatd.api.Registries.EMPTY_GAS_KEY.location(),
                () -> Gases.EMPTY);
    }

    private void onDataMapUpdate(DataMapsUpdatedEvent event){
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void registerDatamaps(final RegisterDataMapTypesEvent event){
        event.register(MASHABLES);
        event.register(JAMMABLES);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == Cmatd.MAIN_TAB.getKey()){

        }
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        public static int customGetColor(BlockAndTintGetter btgetter, BlockPos bp, ColorResolver cr) {
            return btgetter.getBlockTint(bp,cr);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BlockEntityRenderers.register(CmatdBE.DECORATIVE_BASE_DYNAMO_ENGINE.get(),DecorativeBaseDynamoEngineBERenderer::new);
            BlockEntityRenderers.register(CmatdBE.REDSTONE_DYNAMO_ENGINE.get(),RedstoneDynamoEngineBERenderer::new);
            BlockEntityRenderers.register(CmatdBE.WIND_GENERATOR.get(),WindGeneratorRenderer::new);
            BlockEntityRenderers.register(CmatdBE.PRESSER.get(),PresserBERenderer::new);
            BlockEntityRenderers.register(CmatdBE.GAS_TANK.get(),GasTankRenderer::new);
            BlockEntityRenderers.register(CmatdBE.DIESEL_ENGINE.get(),DieselEngineRenderer::new);
        }

        @SubscribeEvent
        public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
            event.registerLayerDefinition(BaseEngineModel.LAYER_LOCATION,BaseEngineModel::createBodyLayer);
            event.registerLayerDefinition(RedstoneDynamoEngineModel.LAYER_LOCATION,RedstoneDynamoEngineModel::createBodyLayer);
            event.registerLayerDefinition(WindGeneratorModel.LAYER_LOCATION,WindGeneratorModel::createBodyLayer);
            event.registerLayerDefinition(GasInWorldModel.LAYER_LOCATION,GasInWorldModel::createBodyLayer);
            event.registerLayerDefinition(DieselEngineModel.LAYER_LOCATION,DieselEngineModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void registerMenus(RegisterMenuScreensEvent event){
            event.register(CmatdMenu.BASE_ENERGY_MAKER_MENU.get(),BaseEnergyMakerScreen::new);
            event.register(CmatdMenu.BASE_COBBLE_MAKER_MENU.get(),BaseCobbleMakerScreen::new);
            event.register(CmatdMenu.JAM_MAKER_MENU.get(),JamMakerScreen::new);
            event.register(CmatdMenu.PRESSER_MENU.get(),PresserScreen::new);
            event.register(CmatdMenu.FOOD_REACTOR_MENU.get(),FoodReactorScreen::new);
            event.register(CmatdMenu.GAS_TANK_MENU.get(),GasTankScreen::new);
        }

        @SubscribeEvent
        private static void setupBlockColors(RegisterColorHandlersEvent.Block event){
            event.register((blockState,tintGetter,blockPos,i) ->
                            tintGetter != null && blockPos != null ?
                                    customGetColor(tintGetter,blockPos,BiomeColors.WATER_COLOR_RESOLVER) : FoliageColor.getDefaultColor(),
                    CmatdBlock.BASE_COBBLE_MAKER.get()
            );
        }

        @SubscribeEvent
        private static void setupItemColors(RegisterColorHandlersEvent.Item event){
            event.register((itemStack,i) -> {
                        BlockState bs = ((BlockItem)itemStack.getItem()).getBlock().defaultBlockState();
                        return Minecraft.getInstance().getBlockColors().getColor(bs,null,null,i);
                    },
                    CmatdBlock.BASE_COBBLE_MAKER.asItem(),
                    CmatdItem.BASE_COBBLE_MAKER.asItem()
            );
        }
    }
}
