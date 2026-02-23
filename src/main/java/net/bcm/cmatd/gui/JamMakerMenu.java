package net.bcm.cmatd.gui;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.JamMakerBE;
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

public class JamMakerMenu extends AbstractContainerMenu{
    private BlockPos pos;
    private JamMakerBE jamMaker;
    public int processBits;
    public int solar;

    private int SLOT = 0;
    private int SLOT_COUNT = 4;

    public JamMakerMenu(int id, Player player, BlockPos bp) {
        super(CmatdMenu.JAM_MAKER_MENU.get(),id);
        this.pos = bp;
        BlockEntity temppBlockEntity = player.level().getBlockEntity(bp);
        if(temppBlockEntity instanceof JamMakerBE) {
            this.jamMaker = (JamMakerBE) temppBlockEntity;
            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return jamMaker.process_bits;
                }

                @Override
                public void set(int value) {
                    processBits = value;
                }
            });
            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return jamMaker.solar;
                }

                @Override
                public void set(int value) {
                    solar = value;
                }
            });
        }

        // sugar slot
        this.addSlot(new SlotItemHandler(jamMaker.getItemHandler(),0,26,25));
        // jam jar slot
        this.addSlot(new SlotItemHandler(jamMaker.getItemHandler(),1,26,43));
        // jammable slot
        this.addSlot(new SlotItemHandler(jamMaker.getItemHandler(),2,44,34));
        // output slot
        this.addSlot(new SlotItemHandler(jamMaker.getItemHandler(),3,120,34));
        playerSlots(player);
    }

    private void playerSlots(Player player){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(player.getInventory(), k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(stack, SLOT_COUNT, Inventory.INVENTORY_SIZE + SLOT_COUNT, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!this.moveItemStackTo(stack, SLOT, SLOT+1, false)) {
                if (index < 27 + SLOT_COUNT) {
                    if (!this.moveItemStackTo(stack, 27 + SLOT_COUNT, 36 + SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < Inventory.INVENTORY_SIZE + SLOT_COUNT && !this.moveItemStackTo(stack, SLOT_COUNT, 27 + SLOT_COUNT, false)) {
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
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, CmatdBlock.JAM_MAKER.get());
    }
}
