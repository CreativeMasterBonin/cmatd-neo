package net.bcm.cmatd.network;

import net.bcm.cmatd.Cmatd;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Handler{
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar regex_reg = event.registrar(Cmatd.MODID);
        regex_reg.playToServer(CmatdEnergyPacket.ENERGY_PACKET_TYPE,CmatdEnergyPacket.STREAM_CODEC,CmatdEnergyPacket::handle);
        regex_reg.playToServer(BaseCobbleMakerTierUpdatePayload.TYPE,BaseCobbleMakerTierUpdatePayload.STREAM_CODEC,BaseCobbleMakerTierUpdatePacket.get()::handle);
        regex_reg.playToServer(ReactorWrenchUpdate.TYPE, ReactorWrenchUpdate.STREAM_CODEC, ReactorWrenchUpdatePacket.get()::handle);
        regex_reg.playToServer(GasTankUpdate.GAS_TANK_UPDATE_TYPE,GasTankUpdate.STREAM_CODEC,GasTankUpdate::handle);
        regex_reg.playToServer(UpdateNightModePayload.TYPE,UpdateNightModePayload.STREAM_CODEC,UpdateNightModePacket.get()::handle);
        regex_reg.playToServer(MachineTierUpgradePayload.TYPE,MachineTierUpgradePayload.STREAM_CODEC,MachineTierUpgradePacket.get()::handle);
    }
}
