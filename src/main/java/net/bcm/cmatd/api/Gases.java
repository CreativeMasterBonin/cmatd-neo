package net.bcm.cmatd.api;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Utility;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Gases {
    public static final DeferredRegister<GasType> GASES = DeferredRegister.create(Registries.GAS_TYPES,Cmatd.MODID);

    public static final GasType EMPTY = new GasType(GasType.Properties.create());

    public static final DeferredHolder<GasType,GasType> METHANE =
            GASES.register("methane",() -> new GasType(
                    new GasType.Properties(Rarity.COMMON,-1,false,0.0f,0,
                            Utility.hexToInt("0x37353D"))
            ));

    public static final DeferredHolder<GasType,GasType> STEAM =
            GASES.register("steam",() -> new GasType(
                    new GasType.Properties(Rarity.COMMON,-1,false,0.0f,1,
                            Utility.hexToInt("0x555555"))
            ));

    public static final DeferredHolder<GasType,GasType> COMPOUNDITE =
            GASES.register("compoundite",() -> new GasType(
                    new GasType.Properties(Rarity.COMMON,1,false,0.0f,5,
                            Utility.hexToInt("0xFF9E38"))
            ));

    public static final DeferredHolder<GasType,GasType> LODEALITE =
            GASES.register("lodealite",() -> new GasType(
                    new GasType.Properties(Rarity.COMMON,1,false,0.0f,6,
                            Utility.hexToInt("0x7A6897"))
            ));

    public static final DeferredHolder<GasType,GasType> DIESEL =
            GASES.register("diesel",() -> new GasType(
                    new GasType.Properties(Rarity.COMMON,2,false,0.0f,12,
                            Utility.hexToInt("0x8C7A3D"))
            ));

    public static void register(IEventBus eventBus) {
        GASES.register(eventBus);
    }
}
