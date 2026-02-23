package net.bcm.cmatd.gui;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CmatdMenu{
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(
            BuiltInRegistries.MENU, Cmatd.MODID);

    public static final Supplier<MenuType<BaseEnergyMakerMenu>> BASE_ENERGY_MAKER_MENU =
            MENUS.register("base_energy_maker_menu",
                    () -> IMenuTypeExtension.create(
                            (id, inv, data) ->
                                    new BaseEnergyMakerMenu(id,inv.player,data.readBlockPos())));

    public static final Supplier<MenuType<BaseCobbleMakerMenu>> BASE_COBBLE_MAKER_MENU =
            MENUS.register("base_cobble_maker_menu",
                    () -> IMenuTypeExtension.create(
                            (id, inv, data) ->
                                    new BaseCobbleMakerMenu(id,inv.player,data.readBlockPos())));

    public static final Supplier<MenuType<JamMakerMenu>> JAM_MAKER_MENU =
            MENUS.register("jam_maker_menu",
                    () -> IMenuTypeExtension.create(
                            (id, inv, data) ->
                                    new JamMakerMenu(id,inv.player,data.readBlockPos())));

    public static final Supplier<MenuType<PresserMenu>> PRESSER_MENU =
            MENUS.register("presser_menu",
                    () -> IMenuTypeExtension.create(
                            (id, inv, data) ->
                                    new PresserMenu(id,inv.player,data.readBlockPos())));

    public static final Supplier<MenuType<FoodReactorMenu>> FOOD_REACTOR_MENU =
            MENUS.register("food_reactor_menu",
                    () -> IMenuTypeExtension.create(
                            (id, inv, data) ->
                                    new FoodReactorMenu(id,inv.player,data.readBlockPos())));

    public static final Supplier<MenuType<GasTankMenu>> GAS_TANK_MENU =
            MENUS.register("gas_tank_menu",
                    () -> IMenuTypeExtension.create(
                            (id, inv, data) ->
                                    new GasTankMenu(id,inv.player,data.readBlockPos())));

    /*
    public static final Supplier<MenuType<MasherMenu>> MASHER_MENU =
            MENUS.register("masher_menu",
                    () -> IMenuTypeExtension.create(
                            (id, inv, data) ->
                                    new MasherMenu(id,inv,data.readBlockPos())));*/
}
