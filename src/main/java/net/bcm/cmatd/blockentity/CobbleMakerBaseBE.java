package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.MachineTier;
import net.bcm.cmatd.MachineTierInterface;
import net.bcm.cmatd.gui.BaseCobbleMakerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class CobbleMakerBaseBE extends BlockEntity implements CobbleMakerLike, MenuProvider {
    // machine and cobble maker
    protected MachineTierInterface machineTier = MachineTier.BASIC;
    private final ContainerData machineData;

    // energy
    protected BaseEnergyStorage energyStorage;
    protected Lazy<IEnergyStorage> lazyEnergyStorage;
    public int energy;
    public static int SLOT_COUNT = 10;

    // items
    protected NonNullList<ItemStack> itemList = null;
    protected ItemStackHandler items = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected Lazy<IItemHandler> lazyItemHandler = Lazy.of(() -> items);

    public ItemStackHandler getItems(){
        return items;
    }

    // specific to BE
    public CobbleMakerBaseBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.BASE_COBBLE_MAKER.get(), pos, blockState);
        this.machineData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index){
                    case 0 -> energyStorage.getEnergyStored() & 0xFFFF;
                    case 1 -> energyStorage.getEnergyStored() >> 16;
                    default -> throw new IllegalArgumentException("index not in bounds " + index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index){
                    case 0 -> energyStorage.setEnergy((energyStorage.getEnergyStored() & 0xFFFF0000) | (value & 0xFFFF));
                    case 1 -> energyStorage.setEnergy((energyStorage.getEnergyStored() & 0xFFFF) | (value << 16));
                    default -> throw new IllegalArgumentException("index not in bounds " + index);
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void setMachineTier(MachineTierInterface machineTier) {
        this.machineTier = machineTier;
        this.invalidateCapabilities();
    }

    @Override
    public void doCobbleOperation(){}

    @Override
    public int getCobbleCount(){return 0;}

    @Override
    public void setCobbleCount(int count){}

    public IItemHandler getItemHandler(){
        return lazyItemHandler.get();
    }

    public ContainerData getContainerData(){
        return machineData;
    }

    public BlockEntity getBlockEntity(){
        return this;
    }

    public IEnergyStorage getEnergyStorage(){
        return lazyEnergyStorage.get();
    }

    public int getMaxEnergy(){
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public Component getDisplayName(){
        return Component.literal("Cobble Maker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BaseCobbleMakerMenu(containerId,player,getBlockPos());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }
}
