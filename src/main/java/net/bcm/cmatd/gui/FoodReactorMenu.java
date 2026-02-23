package net.bcm.cmatd.gui;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.FluidContainerData;
import net.bcm.cmatd.blockentity.FoodReactorMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FoodReactorMenu extends AbstractContainerMenu {
    private BlockPos pos;
    private int energy;
    private FoodReactorMultiblock be;
    public Fluid fluidStored;
    public int fluidAmtStored;
    private final int SLOT = 0;
    private final int SLOT_COUNT = 6;
    private Player tempPlayer;
    public int progress;

    public FluidContainerData fluidContainer;

    public int getEnergy(){
        return this.energy;
    }

    public FoodReactorMultiblock getBlockEntity(){
        return this.be;
    }

    // dire mod
    public int getFluidAmount() {
        return this.fluidContainer == null ? 0 : this.fluidContainer.get(2) << 16 | this.fluidContainer.get(1);
    }

    public Fluid getFluidType() {
        return this.fluidContainer == null ? Fluids.EMPTY : BuiltInRegistries.FLUID.byId(this.fluidContainer.get(0));
    }

    public FluidStack getFluidStack() {
        return new FluidStack(this.getFluidType(), this.getFluidAmount());}
    // end dire mod

    public FoodReactorMenu(int id, Player player, BlockPos pos) {
        super(CmatdMenu.FOOD_REACTOR_MENU.get(), id);
        this.pos = pos;
        this.be = (FoodReactorMultiblock) player.level().getBlockEntity(pos);

        if(be instanceof FoodReactorMultiblock){
            this.fluidContainer = be.getFluidContainer();
            this.tempPlayer = player;
            // input coolant slot
            addSlot(new SlotItemHandler(be.getItemStackHandler(),0,48,52));
            // input fuel slot
            addSlot(new SlotItemHandler(be.getItemStackHandler(),1,80,52));
            // output waste item
            addSlot(new SlotItemHandler(be.getItemStackHandler(),2,112,52));

            // module slot 1
            addSlot(new CanDisableMachineSlotHandler(be,0,true,
                    be.getItemStackHandler(),3,8,34));
            // module slot 2
            addSlot(new CanDisableMachineSlotHandler(be,1,true,
                    be.getItemStackHandler(),4,8,52));
            // module slot 3
            addSlot(new CanDisableMachineSlotHandler(be,2,true,
                    be.getItemStackHandler(),5,8,70));



            // fluid slots
            addDataSlots(fluidContainer);
            /*
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return BuiltInRegistries.FLUID.getId(be.getFluidTank().getFluid().getFluid());
                }

                @Override
                public void set(int value) {
                    fluidStored = be.getFluidTank().getFluid().getFluid();
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return be.getFluidTank().getFluidAmount();
                }

                @Override
                public void set(int value) {
                    fluidAmtStored = value;
                }
            });*/

            // energy slots
            addDataSlot(new DataSlot(){
                @Override
                public int get() {
                    return be.getEnergyStorage().getEnergyStored() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    FoodReactorMenu.this.energy = (FoodReactorMenu.this.energy & 0xffff0000) | (pValue & 0xffff);
                }
            });
            // energy data slot part 2 16bits
            addDataSlot(new DataSlot(){
                @Override
                public int get() {
                    return (be.getEnergyStorage().getEnergyStored() >> 16) & 0xffff;
                }
                @Override
                public void set(int pValue) {
                    FoodReactorMenu.this.energy = (FoodReactorMenu.this.energy & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });

            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return be.progress;
                }

                @Override
                public void set(int value) {
                    progress = value;
                }
            });
            int yOffset = 134;
            playerSlots(player,yOffset);
        }
    }

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
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get());
    }

    public void closeImmediately(){
        this.tempPlayer.closeContainer();
    }
}
