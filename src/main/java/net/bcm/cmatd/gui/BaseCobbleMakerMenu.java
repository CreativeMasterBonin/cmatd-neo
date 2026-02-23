package net.bcm.cmatd.gui;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.BaseCobbleMakerBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BaseCobbleMakerMenu extends AbstractContainerMenu {
    private final BlockPos pos;
    private int energy;
    private int maxEnergy;
    private BaseCobbleMakerBE be;
    private final int SLOT = 0;
    private final int SLOT_COUNT = 10;

    public BaseCobbleMakerMenu(int id, Player player, BlockPos bp) {
        super(CmatdMenu.BASE_COBBLE_MAKER_MENU.get(), id);
        this.pos = bp;
        if (player.level().getBlockEntity(pos) instanceof BaseCobbleMakerBE maker) {
            this.be = (BaseCobbleMakerBE)player.level().getBlockEntity(pos);

            // cobble output slots
            for(int i = 0; i < 7; i++){
                this.addSlot(new BaseCobbleMakerSlotHandler(be.getItemHandler(),i,8 + i * 18,26,this));
            }

            // module in/out slots
            this.addSlot(new CanDisableMachineSlotHandler(this.be,0,true,be.getItemHandler(),7, 152, 26));
            this.addSlot(new CanDisableMachineSlotHandler(this.be,1,true,be.getItemHandler(),8, 152, 44));
            this.addSlot(new CanDisableMachineSlotHandler(this.be,2,true,be.getItemHandler(),9, 152, 62));

            // energy data slot part 1 16bits
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return maker.getEnergyStorage().getEnergyStored() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    BaseCobbleMakerMenu.this.energy = (BaseCobbleMakerMenu.this.energy & 0xffff0000) | (pValue & 0xffff);
                }
            });
            // energy data slot part 2 16bits
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (maker.getEnergyStorage().getEnergyStored() >> 16) & 0xffff;
                }
                @Override
                public void set(int pValue) {
                    BaseCobbleMakerMenu.this.energy = (BaseCobbleMakerMenu.this.energy & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    this.addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                }
            }
            for (int k = 0; k < 9; k++) {
                this.addSlot(new Slot(player.getInventory(), k, 8 + k * 18, 142));
            }

            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (maker.getTierSettings(1));
                }
                @Override
                public void set(int pValue) {
                    BaseCobbleMakerMenu.this.maxEnergy = (BaseCobbleMakerMenu.this.maxEnergy) | pValue;
                }
            });
        }
    }

    public int getEnergy() {
        return energy;
    }

    public BaseCobbleMakerBE getBlockEntity() {
        return be;
    }

    public int getMaxEnergy(){
        return maxEnergy;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if(slot instanceof CanDisableMachineSlotHandler){
                if(((CanDisableMachineSlotHandler) slot).isDisabledSlot){
                    return itemstack;
                }
            }

            // everything else
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
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, CmatdBlock.BASE_COBBLE_MAKER.get());
    }
}
