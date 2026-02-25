package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.Utility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class LightningGeneratorBE extends BlockEntity{
    public int ticks;
    private final BaseEnergyStorage energyStorage = new BaseEnergyStorage(16_000_000,
            2_000_000,2_000_000,0){
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

    private final ContainerData machineData;
    private int cooldownTicksLeft = 0;

    public int getCooldownTicksLeft(){
        return this.cooldownTicksLeft;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public LightningGeneratorBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.LIGHTNING_GENERATOR.get(), pos, blockState);
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

    public BaseEnergyStorage getEnergyStorage() {
        return energyStorage;
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

        if(ticks % 150 == 0){
            if(cooldownTicksLeft >= 1){
                cooldownTicksLeft--;
            }
        }

        if(ticks % 10 == 0){
            if(cooldownTicksLeft == 0 && this.getBlockState().getValue(BlockStateProperties.POWERED)){
                this.getLevel().setBlockAndUpdate(getBlockPos(),getBlockState().setValue(BlockStateProperties.POWERED,false));
                setChanged();
            }
        }

        if(!(level == null)){
            if(level.getBlockState(getBlockPos().above()).is(Blocks.LIGHTNING_ROD) && cooldownTicksLeft <= 0){
                if(level.getBlockState(getBlockPos().above()).getValue(LightningRodBlock.POWERED)){
                    generateEnergy();
                    level.playSound(null,getBlockPos(),
                            SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS,
                            1.0f,0.75f);
                    cooldownTicksLeft = 50;
                    this.getLevel().setBlockAndUpdate(getBlockPos(),getBlockState().setValue(BlockStateProperties.POWERED,true));
                    setChanged();
                }
            }
            distributeEnergy();
        }
    }

    private void generateEnergy(){
        if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()){
            energyStorage.receiveEnergy(Utility.LIGHTNING_GENERATOR_ENERGY_RATE, false);
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
        tag.putInt("cooldown_ticks_left",cooldownTicksLeft);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
        cooldownTicksLeft = tag.getInt("cooldown_ticks_left");
    }
}
