package net.bcm.cmatd;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.CmatdBE;
import net.bcm.cmatd.gui.*;
import net.bcm.cmatd.item.CmatdItem;
import net.bcm.cmatd.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(value = Cmatd.MODID,dist = Dist.CLIENT)
public class CmatdClient {
    public CmatdClient(IEventBus modEventBus, ModContainer modContainer){
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::clientExtensions);
        modEventBus.addListener(this::setupBlockColors);
        modEventBus.addListener(this::setupItemColors);
        modEventBus.addListener(this::registerModelLayers);
        modEventBus.addListener(this::registerMenus);
        modEventBus.addListener(this::addCreative);
    }

    public void registerMenus(RegisterMenuScreensEvent event){
        event.register(CmatdMenu.BASE_ENERGY_MAKER_MENU.get(), BaseEnergyMakerScreen::new);
        event.register(CmatdMenu.BASE_COBBLE_MAKER_MENU.get(), BaseCobbleMakerScreen::new);
        event.register(CmatdMenu.JAM_MAKER_MENU.get(), JamMakerScreen::new);
        event.register(CmatdMenu.PRESSER_MENU.get(), PresserScreen::new);
        event.register(CmatdMenu.FOOD_REACTOR_MENU.get(),FoodReactorScreen::new);
        event.register(CmatdMenu.GAS_TANK_MENU.get(),GasTankScreen::new);
    }

    public void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(BaseEngineModel.LAYER_LOCATION,BaseEngineModel::createBodyLayer);
        event.registerLayerDefinition(RedstoneDynamoEngineModel.LAYER_LOCATION,RedstoneDynamoEngineModel::createBodyLayer);
        event.registerLayerDefinition(WindGeneratorModel.LAYER_LOCATION,WindGeneratorModel::createBodyLayer);
        event.registerLayerDefinition(GasInWorldModel.LAYER_LOCATION,GasInWorldModel::createBodyLayer);
        event.registerLayerDefinition(DieselEngineModel.LAYER_LOCATION,DieselEngineModel::createBodyLayer);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(CmatdBE.DECORATIVE_BASE_DYNAMO_ENGINE.get(), DecorativeBaseDynamoEngineBERenderer::new);
        BlockEntityRenderers.register(CmatdBE.REDSTONE_DYNAMO_ENGINE.get(), RedstoneDynamoEngineBERenderer::new);
        BlockEntityRenderers.register(CmatdBE.WIND_GENERATOR.get(), WindGeneratorRenderer::new);
        BlockEntityRenderers.register(CmatdBE.PRESSER.get(), PresserBERenderer::new);
        BlockEntityRenderers.register(CmatdBE.GAS_TANK.get(),GasTankRenderer::new);
        BlockEntityRenderers.register(CmatdBE.DIESEL_ENGINE.get(),DieselEngineRenderer::new);
    }

    public static int customGetColor(BlockAndTintGetter btgetter, BlockPos bp, ColorResolver cr) {
        return btgetter.getBlockTint(bp,cr);
    }

    public void setupBlockColors(RegisterColorHandlersEvent.Block event){
        event.register((blockState,tintGetter,blockPos,i) ->
                        tintGetter != null && blockPos != null ?
                                customGetColor(tintGetter,blockPos, BiomeColors.WATER_COLOR_RESOLVER) : 4159204, // default water color
                CmatdBlock.BASE_COBBLE_MAKER.get()
        );
    }

    public void setupItemColors(RegisterColorHandlersEvent.Item event){
        event.register((itemStack,i) -> {
                    BlockState bs = ((BlockItem)itemStack.getItem()).getBlock().defaultBlockState();
                    return Minecraft.getInstance().getBlockColors().getColor(bs,null,null,i);
                },
                CmatdBlock.BASE_COBBLE_MAKER.asItem(),CmatdItem.BASE_COBBLE_MAKER.asItem()
        );
        /*event.register((itemStack,i) -> {
            return 4159204; // default water color
        },CmatdBlock.BASE_COBBLE_MAKER.asItem(),
                CmatdItem.BASE_COBBLE_MAKER.asItem());*/
    }

    public void clientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new CmatdBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(),Minecraft.getInstance().getEntityModels());
            }},CmatdItem.DIESEL_ENGINE);
    }

    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == Cmatd.MAIN_TAB.getKey()){
            // we add items to the creative tab directly
        }
    }
}
