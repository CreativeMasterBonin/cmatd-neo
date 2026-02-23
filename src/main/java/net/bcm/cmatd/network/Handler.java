package net.bcm.cmatd.network;

import net.bcm.cmatd.Cmatd;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Handler{
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar regex_reg = event.registrar(Cmatd.MODID);
        regex_reg.playToServer(CmatdEnergyPacket.ENERGY_PACKET_TYPE,CmatdEnergyPacket.STREAM_CODEC,CmatdEnergyPacket::handle);
        regex_reg.playToServer(BaseCobbleMakerTierUpdatePayload.TYPE,BaseCobbleMakerTierUpdatePayload.STREAM_CODEC,BaseCobbleMakerTierUpdatePacket.get()::handle);
        regex_reg.playToServer(FoodReactorWrenchUpdate.TYPE,FoodReactorWrenchUpdate.STREAM_CODEC, FoodReactorWrenchUpdatePacket.get()::handle);
        regex_reg.playToServer(GasTankUpdate.GAS_TANK_UPDATE_TYPE,GasTankUpdate.STREAM_CODEC,GasTankUpdate::handle);
    }
}
