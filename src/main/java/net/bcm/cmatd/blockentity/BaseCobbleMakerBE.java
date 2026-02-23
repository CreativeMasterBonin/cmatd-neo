package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.CmatdSound;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.gui.BaseCobbleMakerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class BaseCobbleMakerBE extends CobbleMakerBaseBE {
    public BaseCobbleMakerBE(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
        this.itemList = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        this.items = new ItemStackHandler(SLOT_COUNT){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        this.lazyItemHandler = Lazy.of(() -> this.items);
        this.energyStorage = new BaseEnergyStorage(
                max_energy,
                max_receive,
                max_extract,0){
            @Override
            public int extractEnergy(int toExtract, boolean simulate) {
                return 0;
            }

            @Override
            public boolean canReceive() {
                return true;
            }

            @Override
            public boolean canExtract() {
                return false;
            }
        };
        this.lazyEnergyStorage = Lazy.of(() -> energyStorage);
    }

    private int ticks = 0;

    public NonNullList<ItemStack> getItemStackList(){
        return this.itemList;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected int machine_tier = 0;
    protected int max_energy = 10000;
    protected int max_receive = 1000;
    protected int max_extract = 1000;
    protected int modules_allowed = 0;
    protected int energy_gen_rate = 50;

    public void setNewTierSettings(int newMachineTier, int newEnergyCapacity, int newMaxReceive, int newMaxExtract, int newAllowedModuleCount, int newEnergyGenRate){
        int oldEnergy = this.energy;
        this.machine_tier = newMachineTier;
        this.max_energy = newEnergyCapacity;
        this.max_receive = newMaxReceive;
        this.max_extract = newMaxExtract;
        this.modules_allowed = newAllowedModuleCount;
        this.energy_gen_rate = newEnergyGenRate;
        this.energyStorage = new BaseEnergyStorage(
                max_energy,
                max_receive,
                max_extract,0){
            @Override
            public int extractEnergy(int toExtract, boolean simulate) {
                return 0;
            }

            @Override
            public boolean canReceive() {
                return true;
            }

            @Override
            public boolean canExtract() {
                return false;
            }
        };
        this.lazyEnergyStorage = Lazy.of(() -> energyStorage);
        for(Direction dir: Direction.values()){
            this.getLevel().updateNeighborsAt(getBlockPos().relative(dir),this.getBlockState().getBlock());
        }
        if(oldEnergy > this.max_energy){
            oldEnergy = this.max_energy; // a fee for convenience
        }
        this.energy = oldEnergy;
        this.energyStorage.setEnergy(this.energy);
        this.setChanged();
        this.invalidateCapabilities();
    }

    public int getTierSettings(int index){
        switch(index){
            case 0 -> {
                return this.machine_tier;
            }
            case 1 -> {
                return this.max_energy;
            }
            case 2 -> {
                return this.max_receive;
            }
            case 3 -> {
                return this.max_extract;
            }
            case 4 -> {
                return this.modules_allowed;
            }
            case 5 -> {
                return this.energy_gen_rate;
            }
            default -> {
                return -1;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.itemList.set(0,getItems().getStackInSlot(0));
        this.itemList.set(1,getItems().getStackInSlot(1));
        this.itemList.set(2,getItems().getStackInSlot(2));
        this.itemList.set(3,getItems().getStackInSlot(3));
        this.itemList.set(4,getItems().getStackInSlot(4));
        this.itemList.set(5,getItems().getStackInSlot(5));
        this.itemList.set(6,getItems().getStackInSlot(6));
        // modules
        this.itemList.set(7,getItems().getStackInSlot(7));
        this.itemList.set(8,getItems().getStackInSlot(8));
        this.itemList.set(9,getItems().getStackInSlot(9));
        ContainerHelper.saveAllItems(tag,this.itemList,registries);

        tag.putInt("energy",this.energyStorage.getEnergyStored());
        tag.putInt("machine_tier",machine_tier);
        tag.putInt("max_energy",max_energy);
        tag.putInt("max_receive",max_receive);
        tag.putInt("max_extract",max_extract);
        tag.putInt("modules_allowed",modules_allowed);
        tag.putInt("energy_gen_rate",energy_gen_rate);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.itemList = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag,this.itemList,registries);

        // cobble stacks
        getItems().setStackInSlot(0,itemList.getFirst());
        getItems().setStackInSlot(1,itemList.get(1));
        getItems().setStackInSlot(2,itemList.get(2));
        getItems().setStackInSlot(3,itemList.get(3));
        getItems().setStackInSlot(4,itemList.get(4));
        getItems().setStackInSlot(5,itemList.get(5));
        getItems().setStackInSlot(6,itemList.get(6));

        // modules
        getItems().setStackInSlot(7,itemList.get(7));
        getItems().setStackInSlot(8,itemList.get(8));
        getItems().setStackInSlot(9,itemList.get(9));

        this.machine_tier = tag.getInt("machine_tier");
        this.max_energy = tag.getInt("max_energy");
        this.max_receive = tag.getInt("max_receive");
        this.max_extract = tag.getInt("max_extract");
        this.modules_allowed = tag.getInt("modules_allowed");
        this.energy_gen_rate = tag.getInt("energy_gen_rate");

        this.energyStorage.setEnergy(tag.getInt("energy"));
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public int getMaxEnergy(){
        return this.energyStorage.getMaxEnergyStored();
    }

    public void serverTick(){
        ticks++;
        int efficiency_modules = 0;
        effGlobal = 0;
        ItemStack moduleSlot1 = this.getItemHandler().getStackInSlot(7);
        ItemStack moduleSlot2 = this.getItemHandler().getStackInSlot(8);
        ItemStack moduleSlot3 = this.getItemHandler().getStackInSlot(9);
        if(!moduleSlot1.isEmpty()){
            if(moduleSlot1.has(Components.MODULE_TYPE)){
                if(moduleSlot1.get(Components.MODULE_TYPE) == 1){
                    efficiency_modules += (1 + moduleSlot1.getCount());
                }
            }
        }
        if(!moduleSlot2.isEmpty()){
            if(moduleSlot2.has(Components.MODULE_TYPE)){
                if(moduleSlot2.get(Components.MODULE_TYPE) == 1){
                    efficiency_modules += (1 + moduleSlot2.getCount());
                }
            }
        }
        if(!moduleSlot3.isEmpty()){
            if(moduleSlot3.has(Components.MODULE_TYPE)){
                if(moduleSlot3.get(Components.MODULE_TYPE) == 1){
                    efficiency_modules += (1 + moduleSlot3.getCount());
                }
            }
        }

        if(efficiency_modules < 0){
            efficiency_modules = 0;
        }
        else if(efficiency_modules > 16){
            efficiency_modules = 16;
        }

        effGlobal = efficiency_modules;

        if(energyStorage.getEnergyStored() > 0 && !(energyStorage
                .simulateSetEnergy(energyStorage.getEnergyStored() - ((16 - effGlobal) + this.machine_tier)) < 0)) {
            doCobbleOperation();
        }
        else{
            operating = false;
        }
        // just in case
        if(energyStorage.getEnergyStored() < 0){
            energyStorage.setEnergy(0);
            setChanged();
        }

        if(operating && getBlockEntity().getLevel() != null){
            if(getBlockEntity().getLevel().getGameTime() % 80L == 0L){
                getBlockEntity().getLevel().playSound(null,this.getBlockPos(),
                        CmatdSound.PROCESSOR_LOOP.value(), SoundSource.BLOCKS,0.25f,1.0f);
            }
        }
        //
        if(ticks > 32767){
            ticks = 0;
        }
    }

    @Override
    public void doCobbleOperation(){
        int slotThatIsEmptyOrStackable = 0;
        boolean slotIsEmpty = false;

        for(int slotNum = 0; slotNum < items.getSlots() - 3; slotNum++){
            if(getItemHandler().getStackInSlot(slotNum).isEmpty()){
                slotThatIsEmptyOrStackable = slotNum;
                slotIsEmpty = true;
                break;
            }
            else if(getItemHandler().getStackInSlot(slotNum).is(Items.COBBLESTONE) &&
            getSlotCobbleCount(slotNum) < itemMaxStackSize(slotNum)){
                slotThatIsEmptyOrStackable = slotNum;
                break;
            }
        }
        cobbleOpWithTier(slotThatIsEmptyOrStackable,slotIsEmpty);
    }

    private void decreaseEnergy(){
        this.energyStorage.setEnergy(energyStorage.getEnergyStored() - ((16 - effGlobal) + this.machine_tier));
    }

    int effGlobal = 0;
    int itemsPlusGlobal = 0;
    boolean operating = false;

    private int calculateAdditionalItemsModules(){
        itemsPlusGlobal = 0;
        int additionalItems = 0;
        for(int slot = 7; slot < 9; slot++){
            ItemStack testingStack = this.getItemHandler().getStackInSlot(slot);
            if(!testingStack.isEmpty()){
                if(testingStack.has(Components.MODULE_TYPE)){
                    switch(testingStack.get(Components.MODULE_TYPE)){
                        case 2 -> {
                            additionalItems += 2;
                        }
                        case 3 -> {
                            additionalItems += 3;
                        }
                        case null -> {}
                        default -> {}
                    }
                }
            }
        }
        if(additionalItems < 0){
            additionalItems = 0;
        }
        else if(additionalItems > 16){
            additionalItems = 16;
        }
        itemsPlusGlobal = additionalItems;
        return itemsPlusGlobal;
    }

    private int calculateBasedOnModules(){
        int calculationResult = 0;
        int module_speed_additional = 0;
        ItemStack moduleSlot1 = this.getItemHandler().getStackInSlot(7);
        ItemStack moduleSlot2 = this.getItemHandler().getStackInSlot(8);
        ItemStack moduleSlot3 = this.getItemHandler().getStackInSlot(9);
        // calculate the efficiency and speed of operation with some logic
        if(!moduleSlot1.isEmpty()){
            if(moduleSlot1.has(Components.MODULE_TYPE)){
                switch(moduleSlot1.get(Components.MODULE_TYPE)){
                    case 0 -> {
                        module_speed_additional += (1 + moduleSlot1.getCount());
                    }
                    case null -> {}
                    default -> {}
                }
            }
        }
        if(!moduleSlot2.isEmpty()){
            if(moduleSlot2.has(Components.MODULE_TYPE)){
                switch(moduleSlot2.get(Components.MODULE_TYPE)){
                    case 0 -> {
                        module_speed_additional += (1 + moduleSlot1.getCount());
                    }
                    case null -> {}
                    default -> {}
                }
            }
        }
        if(!moduleSlot3.isEmpty()){
            if(moduleSlot3.has(Components.MODULE_TYPE)){
                switch(moduleSlot3.get(Components.MODULE_TYPE)){
                    case 0 -> {
                        module_speed_additional += (1 + moduleSlot1.getCount());
                    }
                    case null -> {}
                    default -> {}
                }
            }
        }
        calculationResult = module_speed_additional;
        if(calculationResult < 0){
            calculationResult = 0;
        }
        else if(calculationResult > 16){
            calculationResult = 16;
        }
        return calculationResult;
    }

    private int checkIfNegativeFixIfSo(int value){
        if(value < 0){
            value = value * -1;
            if(value > 8){
                value = 8;
            }
        }
        return value;
    }

    private void cobbleOpWithTier(int slot, boolean emptySlot){
        int val = checkIfNegativeFixIfSo((16 / (this.machine_tier + 1))) - calculateBasedOnModules();
        if(val < 1){
            val = 1;
        }
        else if(val > 16){
            val = 16;
        }
        // continue
        if(ticks % val == 0){
            if(emptySlot){
                this.getItemHandler().insertItem(slot,new ItemStack(Items.COBBLESTONE),false);
                decreaseEnergy();
                operating = true;
                setChanged();
            }
            else{
                if(cobbleCountAddedNotHigherThanMax(slot)){
                    this.getItemHandler().getStackInSlot(slot).grow((1));
                    decreaseEnergy();
                    operating = true;
                    setChanged();
                }
                else{
                    operating = false;
                }
                // make sure the stack is NEVER higher than 64
                if(getSlotCobbleCount(slot) > 64){
                    setSlotCobbleCount(slot,64);
                }
            }
        }
    }

    private boolean cobbleCountAddedNotHigherThanMax(int slot){
        return !(getSlotCobbleCount(slot) + (1 + this.machine_tier) + calculateAdditionalItemsModules() > this.itemMaxStackSize(slot));
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    private int itemMaxStackSize(int slot){
        return this.getItemHandler().getStackInSlot(slot).getMaxStackSize();
    }

    @Override
    public int getCobbleCount() {
        return getItemHandler().getStackInSlot(0).getCount();
    }

    public int getSlotCobbleCount(int slot){
        return getItemHandler().getStackInSlot(slot).getCount();
    }

    @Override
    public void setCobbleCount(int count) {
        getItemHandler().getStackInSlot(0).setCount(count);
    }

    public void setSlotCobbleCount(int slot, int count) {
        getItemHandler().getStackInSlot(slot).setCount(count);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BaseCobbleMakerMenu(containerId,player,getBlockPos());
    }
}
