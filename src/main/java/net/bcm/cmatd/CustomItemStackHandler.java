package net.bcm.cmatd;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CustomItemStackHandler extends ItemStackHandler{
    public CustomItemStackHandler() {
        this(1);
    }

    public CustomItemStackHandler(int size) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public CustomItemStackHandler(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public NonNullList<ItemStack> getStacks(){
        return this.stacks;
    }
}
