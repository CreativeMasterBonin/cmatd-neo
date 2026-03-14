package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.CmatdSound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class RotationalInductionGenerator extends BlockEntity{
    public boolean beingRotated = false;
    public int rotationalPower = 0;
    public int ticks;
    private final BaseEnergyStorage energyStorage;
    private final ContainerData machineData;

    public RotationalInductionGenerator(BlockPos pos, BlockState blockState) {
        super(CmatdBE.ROTATIONAL_INDUCTION_GENERATOR.get(), pos, blockState);
        this.energyStorage = new BaseEnergyStorage(48000,1750,1750){
            @Override
            public boolean canReceive() {
                return true;
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

    public ContainerData getMachineData() {
        return machineData;
    }

    public BaseEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getRotationalPower() {
        return rotationalPower;
    }

    public boolean isBeingRotated() {
        return beingRotated;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag,registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag,lookupProvider);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag,registries);
        if(tag.contains("rotational_power")){
            rotationalPower = tag.getInt("rotational_power");
        }
        if(tag.contains("energy")){
            energyStorage.setEnergy(tag.getInt("energy"));
        }
        if(tag.contains("being_rotated")){
            beingRotated = tag.getBoolean("being_rotated");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag,registries);
        tag.putInt("rotational_power",rotationalPower);
        tag.putInt("energy",energyStorage.getEnergyStored());
        tag.putBoolean("being_rotated",beingRotated);
    }

    public void clientTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
        }
    }

    public void serverTick(){
        ticks++; // TODO this BE is not saving the rotational power, fix this

        if(level != null){
            for(Direction dir : Direction.values()){
                BlockEntity blockEntity = level.getBlockEntity(getBlockPos().relative(dir));
                if(blockEntity != null){
                    if(blockEntity instanceof DieselEngineBE dieselEngineBE){
                        if(dieselEngineBE.getRotationalOutputSpeed() > 0){
                            int clampedRotationalOutputSpeed = Mth.clamp(dieselEngineBE.getRotationalOutputSpeed(),0,1000000);
                            generateEnergy((int)((double)clampedRotationalOutputSpeed / 10.0D)); // penalty to be like energy loss from spinning
                            rotationalPower = Mth.lerpInt((float)(ticks / 2),0,clampedRotationalOutputSpeed); // the rotational ability of the generator
                            beingRotated = true;
                            setChanged();
                        }
                        else{
                            rotationalPower = Mth.lerpInt((float)(ticks / 2),rotationalPower,0); // slowly decrease the ability
                            beingRotated = false;
                            setChanged();
                        }
                    }
                }
            }
            if(rotationalPower < 0){
                rotationalPower = 0;
                setChanged();
            }
            if(rotationalPower > 1000000){
                rotationalPower = 1000000;
                setChanged();
            }
            if(rotationalPower > 0 && ticks % 47 == 0){
                level.playSound(null,getBlockPos(), CmatdSound.ROTATING_LOOP.get(), SoundSource.BLOCKS,0.85f,1.0f);
            }
        }
        distributeEnergy();
        if(ticks > 32767){
            ticks = 0;
        }
    }


    // this generation method requires a non-negative-non-zero value as rotation has to give energy!
    private void generateEnergy(int amount){
        if(amount <= 0){
            return;
        }
        if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()){
            energyStorage.receiveEnergy(amount, false);
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
}
