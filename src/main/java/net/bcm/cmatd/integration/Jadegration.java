package net.bcm.cmatd.integration;

import net.bcm.cmatd.block.custom.GasTank;
import net.bcm.cmatd.blockentity.GasTankBE;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class Jadegration implements IWailaPlugin {
    public static final ResourceLocation GAS_TYPE_UID = ResourceLocation.parse("cmatd:gas_type_provider");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(GasBlockComponentProvider.INSTANCE,GasTankBE.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(GasBlockComponentProvider.INSTANCE, GasTank.class);
    }
}
