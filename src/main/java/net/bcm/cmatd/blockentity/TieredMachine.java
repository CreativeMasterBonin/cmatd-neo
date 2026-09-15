package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.block.custom.TieredMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TieredMachine extends BlockEntity {
    public int machineTier = 0;
    public final int maxMachineTier;
    public final BaseEnergyStorage battery; // it 'is' basically a battery
    public abstract int getFirstTimeMaxMachineTier();
    public abstract BaseEnergyStorage getFirstTimeEnergyStorage();
    public String operationTime = operationTime(OperationTime.ALWAYS); // the 'time of day' this machine should be active (otherwise is inactive and cannot process)
    public int processBits = 0;
    public int maxProcessBitsTillCompletion = 100;
    public boolean isProcessing = false;
    private final ContainerData machineData;

    public enum OperationTime implements StringRepresentable {
        MORNING("morning"),
        NOON("noon"),
        EVENING("evening"),
        MIDNIGHT("midnight"),
        DAY("day"),
        NIGHT("night"),
        DAY_AND_NIGHT("day_and_night"),
        NOON_AND_MIDNIGHT("noon_and_midnight"),
        ALWAYS("always");
        private final String name;

        OperationTime(String internalName){
            this.name = internalName;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public void updateBlock(){
        this.setChanged();
        if(this.level != null){
            this.level.sendBlockUpdated(this.getBlockPos(),this.getBlockState(),this.getBlockState(),3);
        }
    }

    public int getMaxMachineTier(){
        return this.maxMachineTier;
    }

    public BaseEnergyStorage getEnergyStorage(){
        return this.battery;
    }

    public String operationTime(OperationTime opTime){
        return opTime.getSerializedName();
    }

    public TieredMachine(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        maxMachineTier = getFirstTimeMaxMachineTier();
        battery = getFirstTimeEnergyStorage();
        machineData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index){
                    case 0 -> battery.getEnergyStored() & 0xFFFF;
                    case 1 -> battery.getEnergyStored() >> 16;
                    default -> throw new IllegalArgumentException("index not in bounds " + index);
                };
            }
            @Override
            public void set(int index, int value) {
                switch(index){
                    case 0 -> battery.setEnergy((battery.getEnergyStored() & 0xFFFF0000) | (value & 0xFFFF));
                    case 1 -> battery.setEnergy((battery.getEnergyStored() & 0xFFFF) | (value << 16));
                    default -> throw new IllegalArgumentException("index not in bounds " + index);
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public abstract void loadExtraValues(CompoundTag tag, HolderLookup.Provider registries);
    public abstract void saveExtraValues(CompoundTag tag, HolderLookup.Provider registries);

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        this.saveAdditional(tag,lookupProvider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag,registries);
        tag.putInt("machine_tier", Mth.clamp(machineTier,0,getMaxMachineTier()));
        tag.putInt("process_bits",processBits);
        saveExtraValues(tag,registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("machine_tier")){
            machineTier = Mth.clamp(tag.getInt("machine_tier"),0,getMaxMachineTier());
        }
        if(tag.contains("process_bits")){
            processBits = tag.getInt("process_bits");
        }
        loadExtraValues(tag,registries);
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return state.getBlock() instanceof TieredMachineBlock;
    }

    public abstract void extraServerTick();

    public void serverTick(){
        extraServerTick();
    }
}
