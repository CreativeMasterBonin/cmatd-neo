package net.bcm.cmatd.api;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;

public final class Capabilities{
    public static final class GasHandler {
        public static final BlockCapability<IGasHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(create("gas_handler"), IGasHandler.class);
        public static final EntityCapability<IGasHandler, @Nullable Direction> ENTITY = EntityCapability.createSided(create("gas_handler"), IGasHandler.class);
        public static final ItemCapability<IGasHandlerItem, @Nullable Void> ITEM = ItemCapability.createVoid(create("gas_handler"), IGasHandlerItem.class);

        private GasHandler(){}
    }

    private static ResourceLocation create(String path) {
        return ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,path);
    }

    private Capabilities() {}
}
