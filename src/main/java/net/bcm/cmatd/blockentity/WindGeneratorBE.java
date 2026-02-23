package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class WindGeneratorBE extends BlockEntity{
    public int ticks;
    private final BaseEnergyStorage energyStorage;
    private final ContainerData machineData;
    private boolean isBelowSeaLevel;
    public boolean canGenerate;

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public WindGeneratorBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.WIND_GENERATOR.get(), pos, blockState);
        this.energyStorage = new BaseEnergyStorage(10000,1000,1000){
            @Override
            public boolean canReceive() {
                return false;
            }

            @Override
            public boolean canExtract() {
                return true;
            }

            @Override
            public int receiveEnergy(int toReceive, boolean simulate) {
                if(toReceive < 0){
                    return 0;
                }

                int energyReceived = Mth.clamp(this.capacity - this.energy, 0, Math.min(this.maxReceive, toReceive));
                if (!simulate)
                    this.energy += energyReceived;
                return energyReceived;
            }
        };

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

    public void clientTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
        }
    }

    public void serverTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
        }

        if(!(level == null)){
            isBelowSeaLevel = (getBlockPos().getY() < level.getSeaLevel());
            if(this.level.canSeeSky(this.getBlockPos())){
                if(!canGenerate){
                    canGenerate = true;
                    setChanged();
                }
                generateEnergy();
            }
            else{
                if(canGenerate){
                    canGenerate = false;
                    setChanged();
                }
            }
            distributeEnergy();
        }
    }

    private void generateEnergy() {
        if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
            int generation_rate;

            if(isBelowSeaLevel){
                generation_rate = 5;
            }
            else{
                generation_rate = 10;
            }
            energyStorage.receiveEnergy(generation_rate, false);
            setChanged();
        }
    }

    private void distributeEnergy() {
        for (Direction direction : Direction.values()) {
            if (energyStorage.getEnergyStored() <= 0) {
                return;
            }
            // get blocks that are energy capable
            IEnergyStorage energy = level.getCapability(Capabilities.EnergyStorage.BLOCK,
                    getBlockPos().relative(direction), null);
            if (energy != null) {
                if (energy.canReceive()) {
                    int received = energy.receiveEnergy(Math.min(
                            this.energyStorage.getEnergyStored(),
                            energyStorage.getMaxExtract()), false);
                    this.energyStorage.extractEnergy(received, false);
                    setChanged();
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy",energyStorage.getEnergyStored());
        tag.putBoolean("below_sea_level",isBelowSeaLevel);
        tag.putBoolean("can_generate",canGenerate);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
        isBelowSeaLevel = tag.getBoolean("below_sea_level");
        canGenerate = tag.getBoolean("can_generate");
    }
}
