package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.block.custom.RedstoneDynamoEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class RedstoneDynamoEngineBE extends BlockEntity{
    public int ticks = 0;
    private final BaseEnergyStorage energyStorage =
            new BaseEnergyStorage(10000,1000,1000){
                @Override
                public boolean canReceive() {
                    return true;
                }
            };

    public int energy,energyProduction;
    public int generationRate;

    private final ContainerData machineData;

    public RedstoneDynamoEngineBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.REDSTONE_DYNAMO_ENGINE.get(), pos, blockState);
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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void serverTick(){
        ticks++;
        if(ticks >= 32767){
            ticks = 0;
        }
        if(this.getBlockState().is(CmatdBlock.REDSTONE_DYNAMO_ENGINE)){
            if(this.getBlockState().getValue(RedstoneDynamoEngine.LIT)){
                generateEnergy();
                distributeEnergy();
            }
        }
    }

    public void clientTick(){
        ticks++;
        if(ticks >= 32767){
            ticks = 0;
        }
    }

    private void generateEnergy(){
        generationRate = 10;
        if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()){
            if(getLevel().getBlockState(getBlockPos().above()).is(Blocks.REDSTONE_BLOCK)){
                generationRate += 10;
            }
            if(getLevel().getBlockState(getBlockPos().below()).is(Blocks.REDSTONE_BLOCK)){
                generationRate += 10;
            }
            if(getLevel().getBlockState(getBlockPos().north()).is(Blocks.REDSTONE_BLOCK)){
                generationRate += 10;
            }
            if(getLevel().getBlockState(getBlockPos().south()).is(Blocks.REDSTONE_BLOCK)){
                generationRate += 10;
            }
            if(getLevel().getBlockState(getBlockPos().east()).is(Blocks.REDSTONE_BLOCK)){
                generationRate += 10;
            }
            if(getLevel().getBlockState(getBlockPos().west()).is(Blocks.REDSTONE_BLOCK)){
                generationRate += 10;
            }
            energyStorage.receiveEnergy(generationRate,false);
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
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("energy")){
            energyStorage.setEnergy(tag.getInt("energy"));
        }
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }
}
