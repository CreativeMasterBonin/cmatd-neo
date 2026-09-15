package net.bcm.cmatd.gui;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.FluidContainerData;
import net.bcm.cmatd.blockentity.RadioactiveReactor;
import net.bcm.cmatd.blockentity.RadioactiveReactorFluidContainerData;
import net.bcm.cmatd.blockentity.RadioactiveReactorGasContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class RadioactiveReactorMenu extends AbstractContainerMenu {
    private BlockPos blockPos;
    private RadioactiveReactor reactor;
    private final int SLOT = 0;
    private final int TOTAL_SLOTS = 30;
    public int playerInventoryYOffset = 173; // standard is 142
    // the top left position of the first slot in the reactor inventory, going down left-to-right then top-to-bottom
    public int startReactorSlotX = 43;
    public int startReactorSlotY = 38;
    public int startReactorModuleSlotY = 133;
    public int processBits;
    public RadioactiveReactorGasContainerData gasContainerData;
    public RadioactiveReactorFluidContainerData fluidContainer;
    public int energy;

    public RadioactiveReactorMenu(int id, Player player, BlockPos position) {
        super(CmatdMenu.RADIOACTIVE_REACTOR_MENU.get(),id);
        this.blockPos = position;
        BlockEntity blockEntity = player.level().getBlockEntity(blockPos);
        if(blockEntity instanceof RadioactiveReactor){
            this.reactor = (RadioactiveReactor)blockEntity;
            this.gasContainerData = reactor.gasContainerData;
            this.fluidContainer = reactor.fluidContainerData;
            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return reactor.processBits;
                }

                @Override
                public void set(int value) {
                    processBits = value;
                }
            });
            this.addDataSlots(gasContainerData); // gas tank storage
            this.addDataSlots(fluidContainer); // waste gas -> waste fluid tank
            // energy data
            // slot 1 for first half of energy value
            addDataSlot(new DataSlot(){
                @Override
                public int get() {
                    return reactor.getEnergyStorage().getEnergyStored() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    RadioactiveReactorMenu.this.energy = (RadioactiveReactorMenu.this.energy & 0xffff0000) | (pValue & 0xffff);
                }
            });
            // slot 2 for second half of energy value
            addDataSlot(new DataSlot(){
                @Override
                public int get() {
                    return (reactor.getEnergyStorage().getEnergyStored() >> 16) & 0xffff;
                }
                @Override
                public void set(int pValue) {
                    RadioactiveReactorMenu.this.energy = (RadioactiveReactorMenu.this.energy & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
        }
        // reactor slots
        for(int y = 0; y < 5; y++){
            for(int x = 0; x < 5; x++){
                this.addSlot(new SlotItemHandler(reactor.itemStackHandler,y + x,startReactorSlotX + x + 18,startReactorSlotY + y + 18));
            }
        }

        // module slots
        for(int i = 0; i < 5; i++){
            this.addSlot(new SlotItemHandler(reactor.itemStackHandler,30 + i,startReactorSlotX + i + 18,startReactorModuleSlotY));
        }

        // add the default inventory slots
        playerSlots(player);
    }

    private void playerSlots(Player player){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(player.getInventory(), k, 8 + k * 18, playerInventoryYOffset));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if (index < TOTAL_SLOTS) {
                if (!this.moveItemStackTo(stack, TOTAL_SLOTS, Inventory.INVENTORY_SIZE + TOTAL_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!this.moveItemStackTo(stack, SLOT, SLOT+1, false)) {
                if (index < 27 + TOTAL_SLOTS) {
                    if (!this.moveItemStackTo(stack, 27 + TOTAL_SLOTS, 36 + TOTAL_SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < Inventory.INVENTORY_SIZE + TOTAL_SLOTS && !this.moveItemStackTo(stack, TOTAL_SLOTS, 27 + TOTAL_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(),blockPos), player, CmatdBlock.RADIOACTIVE_REACTOR_MULTIBLOCK.get());
    }
}
