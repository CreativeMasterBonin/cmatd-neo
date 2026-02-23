package net.bcm.cmatd.api;

import net.minecraft.world.item.ItemStack;

/**
 * From NeoForge, changed to support GasStack
 */
public interface IGasHandlerItem extends IGasHandler{
    ItemStack getContainer();
}
