package net.bcm.cmatd.gui;

import net.bcm.cmatd.api.GasStack;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.api.Registries;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.RadioactiveReactor;
import net.bcm.cmatd.blockentity.RadioactiveReactorFluidContainerData;
import net.bcm.cmatd.blockentity.RadioactiveReactorGasContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class RadioactiveReactorMenu extends AbstractContainerMenu {
    private BlockPos blockPos;
    private RadioactiveReactor reactor;
    private final int SLOT = 0;
    private final int TOTAL_SLOTS = 30;
    public int playerInventoryYOffset = 232; // standard is 142
    // the top left position of the first slot in the reactor inventory, going down left-to-right then top-to-bottom
    public int startReactorSlotX = 26;
    public int startReactorSlotY = 21;
    public int startReactorModuleSlotY = 133;
    public int processBits;
    public RadioactiveReactorGasContainerData gasContainerData;
    public RadioactiveReactorFluidContainerData fluidContainer;
    public int energy;
    public int coolant;
    public int heat;
    public int energyCapacity;
    public int coolantCapacity;
    public int heatCapacity;
    public int wasteCapacity;

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
                    return reactor.heatAmountToGoBoomAt;
                }

                @Override
                public void set(int value) {
                    heatCapacity = value;
                }
            });
            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return reactor.maxCoolantAmount;
                }

                @Override
                public void set(int value) {
                    coolantCapacity = value;
                }
            });
            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return reactor.getEnergyStorage().getCapacity();
                }

                @Override
                public void set(int value) {
                    energyCapacity = value;
                }
            });
            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return reactor.getWasteGasTank().getCapacity();
                }

                @Override
                public void set(int value) {
                    wasteCapacity = value;
                }
            });
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
            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return reactor.heatAmount;
                }

                @Override
                public void set(int value) {
                    heat = value;
                }
            });
            this.addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return reactor.coolantAmount;
                }

                @Override
                public void set(int value) {
                    coolant = value;
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
        // 25 reactor slots and 5 module slots
        // row one
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                0,startReactorSlotX + 18,startReactorSlotY + 18));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                1,startReactorSlotX + (18 * 2),startReactorSlotY + 18));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                2,startReactorSlotX + (18 * 3),startReactorSlotY + 18));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                3,startReactorSlotX + (18 * 4),startReactorSlotY + 18));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                4,startReactorSlotX + (18 * 5),startReactorSlotY + 18));
        // row 2
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                5,startReactorSlotX + 18,startReactorSlotY + (18 * 2)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                6,startReactorSlotX + (18 * 2),startReactorSlotY + (18 * 2)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                7,startReactorSlotX + (18 * 3),startReactorSlotY + (18 * 2)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                8,startReactorSlotX + (18 * 4),startReactorSlotY + (18 * 2)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                9,startReactorSlotX + (18 * 5),startReactorSlotY + (18 * 2)));
        // row 3
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                10,startReactorSlotX + 18,startReactorSlotY + (18 * 3)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                11,startReactorSlotX + (18 * 2),startReactorSlotY + (18 * 3)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                12,startReactorSlotX + (18 * 3),startReactorSlotY + (18 * 3)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                13,startReactorSlotX + (18 * 4),startReactorSlotY + (18 * 3)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                14,startReactorSlotX + (18 * 5),startReactorSlotY + (18 * 3)));
        // row 4
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                15,startReactorSlotX + 18,startReactorSlotY + (18 * 4)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                16,startReactorSlotX + (18 * 2),startReactorSlotY + (18 * 4)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                17,startReactorSlotX + (18 * 3),startReactorSlotY + (18 * 4)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                18,startReactorSlotX + (18 * 4),startReactorSlotY + (18 * 4)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                19,startReactorSlotX + (18 * 5),startReactorSlotY + (18 * 4)));
        // row 5
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                20,startReactorSlotX + 18,startReactorSlotY + (18 * 5)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                21,startReactorSlotX + (18 * 2),startReactorSlotY + (18 * 5)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                22,startReactorSlotX + (18 * 3),startReactorSlotY + (18 * 5)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                23,startReactorSlotX + (18 * 4),startReactorSlotY + (18 * 5)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                24,startReactorSlotX + (18 * 5),startReactorSlotY + (18 * 5)));
        // module slots
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                25,startReactorSlotX + 18,startReactorSlotY + 23 + (18 * 5)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                26,startReactorSlotX + (18 * 2),startReactorSlotY + 23 + (18 * 5)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                27,startReactorSlotX + (18 * 3),startReactorSlotY + 23 + (18 * 5)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                28,startReactorSlotX + (18 * 4),startReactorSlotY + 23 + (18 * 5)));
        this.addSlot(new SlotItemHandler(reactor.itemStackHandler,
                29,startReactorSlotX + (18 * 5),startReactorSlotY + 23 + (18 * 5)));

        // add the default inventory slots
        playerSlots(player);
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

    // gases data start
    public int getGasAmount() {
        return this.gasContainerData == null ? 0 : this.gasContainerData.get(2) << 16 | this.gasContainerData.get(1);
    }

    public GasType getGasType() {
        return this.gasContainerData == null ? Gases.EMPTY : Registries.GAS_TYPES.byId(this.gasContainerData.get(0));
    }

    public GasStack getGasStack() {
        return new GasStack(this.getGasType(), this.getGasAmount());}

    private void playerSlots(Player player){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, (84 + i * 18) + 90));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(player.getInventory(), k, 8 + k * 18, playerInventoryYOffset));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        /*ItemStack itemstack = ItemStack.EMPTY;
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
        }*/

        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(),blockPos), player, CmatdBlock.RADIOACTIVE_REACTOR_MULTIBLOCK.get());
    }
}
