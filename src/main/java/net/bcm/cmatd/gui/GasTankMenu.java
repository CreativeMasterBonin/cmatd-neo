package net.bcm.cmatd.gui;

import net.bcm.cmatd.api.*;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.GasContainerData;
import net.bcm.cmatd.blockentity.GasTankBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GasTankMenu extends AbstractContainerMenu {
    private BlockPos pos;
    private GasTankBE be;
    public Player tempPlayer;
    private final int SLOT = 0;
    private final int SLOT_COUNT = 0;

    public GasContainerData gasContainer;

    public GasTankBE getGasTankBE(){
        return this.be;
    }

    public GasTankMenu(int id, Player player, BlockPos pos) {
        super(CmatdMenu.GAS_TANK_MENU.get(), id);
        this.pos = pos;
        this.be = (GasTankBE)player.level().getBlockEntity(pos);
        if(be instanceof GasTankBE){
            this.gasContainer = be.getGasContainerData();
            this.tempPlayer = player;
            addDataSlots(gasContainer);
        }

        int yOffset = 134;
        playerSlots(player,yOffset);
    }

    // gases data start
    public int getGasAmount() {
        return this.gasContainer == null ? 0 : this.gasContainer.get(2) << 16 | this.gasContainer.get(1);
    }

    public GasType getGasType() {
        return this.gasContainer == null ? Gases.EMPTY : Registries.GAS_TYPES.byId(this.gasContainer.get(0));
    }

    public GasStack getGasStack() {
        return new GasStack(this.getGasType(), this.getGasAmount());}
    // gases data end


    private void playerSlots(Player player,int yOffset){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, yOffset + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            // 142 is old or 'standard' y pos
            this.addSlot(new Slot(player.getInventory(), k, 8 + k * 18, 192));
        }
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
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, CmatdBlock.GAS_TANK.get());
    }
}
