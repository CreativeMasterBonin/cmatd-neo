package net.bcm.cmatd.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.Optional;

public class BaseCobbleMakerSlotHandler extends SlotItemHandler {
    private final BaseCobbleMakerMenu menu;

    public BaseCobbleMakerSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, BaseCobbleMakerMenu menu) {
        super(itemHandler, index, xPosition, yPosition);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public Optional<ItemStack> tryRemove(int count, int decrement, Player player) {
        return super.tryRemove(count, decrement, player);
    }

    @Override
    public boolean allowModification(Player player) {
        return mayPickup(player);
    }
}
