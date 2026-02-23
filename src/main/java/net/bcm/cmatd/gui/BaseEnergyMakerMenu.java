package net.bcm.cmatd.gui;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.BaseEnergyMakerBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

import static net.bcm.cmatd.blockentity.BaseEnergyMakerBE.SLOT;
import static net.bcm.cmatd.blockentity.BaseEnergyMakerBE.SLOT_COUNT;

public class BaseEnergyMakerMenu extends AbstractContainerMenu{
    private final BlockPos pos;
    private int energy;
    private BaseEnergyMakerBE be;
    private int burnTime;

    public int getEnergy(){
        return energy;
    }

    public BaseEnergyMakerBE getBlockEntity(){
        return be;
    }

    public int getBurnTime(){
        return burnTime;
    }

    public BaseEnergyMakerMenu(int id, Player player, BlockPos pos) {
        super(CmatdMenu.BASE_ENERGY_MAKER_MENU.get(),id);
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof BaseEnergyMakerBE maker) {
            this.be = maker;
            addSlot(new SlotItemHandler(maker.getItemHandler(), SLOT, 37, 37)); // input slot fuel
            // energy data slot part 1 16bits
            addDataSlot(new DataSlot(){
                @Override
                public int get() {
                    return maker.getEnergyStorage().getEnergyStored() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    BaseEnergyMakerMenu.this.energy = (BaseEnergyMakerMenu.this.energy & 0xffff0000) | (pValue & 0xffff);
                }
            });
            // energy data slot part 2 16bits
            addDataSlot(new DataSlot(){
                @Override
                public int get() {
                    return (maker.getEnergyStorage().getEnergyStored() >> 16) & 0xffff;
                }
                @Override
                public void set(int pValue) {
                    BaseEnergyMakerMenu.this.energy = (BaseEnergyMakerMenu.this.energy & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            // burn data
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return maker.getBurnTime();
                }

                @Override
                public void set(int value){
                    burnTime = value;
                }
            });
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
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, CmatdBlock.BASE_ENERGY_MAKER.get());
    }
}
