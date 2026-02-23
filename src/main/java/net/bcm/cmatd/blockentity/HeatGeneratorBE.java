package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.CmatdSound;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.block.custom.HeatGenerator;
import net.bcm.cmatd.datagen.Tag;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class HeatGeneratorBE extends BlockEntity {
    public int ticks;
    private final BaseEnergyStorage energyStorage;
    private final ContainerData machineData;

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public HeatGeneratorBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.HEAT_GENERATOR.get(), pos, blockState);
        this.energyStorage = new BaseEnergyStorage(10000,5000,5000){
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

    public EnergyStorage getEnergyStorage(){
        return this.energyStorage;
    }

    public void serverTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
        }

        if(!(level == null)){
            if(ticks % 40 == 0){
                int heaterCount = 0;
                if(level.dimensionType().ultraWarm()){
                    generateEnergy(50);
                    heaterCount++;
                }
                //
                for(Direction direction: Direction.values()){
                    if(level.getBlockState(getBlockPos().relative(direction)).is(Tag.HEAT_PRODUCERS)){
                        if(level.getBlockState(getBlockPos().relative(direction)).is(Tag.LOW_HEAT_PRODUCERS)){
                            boolean isCandleLike = level.getBlockState(getBlockPos().relative(direction)).getBlock() instanceof AbstractCandleBlock;
                            if(isCandleLike){
                                if(level.getBlockState(getBlockPos().relative(direction)).getValue(AbstractCandleBlock.LIT)){
                                    generateEnergy(100);
                                    heaterCount++;
                                }
                            }
                            else{
                                generateEnergy(100);
                                heaterCount++;
                            }
                        }
                        else if(level.getBlockState(getBlockPos().relative(direction)).is(Tag.MEDIUM_HEAT_PRODUCERS)){
                            boolean isFurnaceLike = level.getBlockState(getBlockPos().relative(direction)).getBlock() instanceof AbstractFurnaceBlock;
                            boolean isCampfireLike = level.getBlockState(getBlockPos().relative(direction)).getBlock() instanceof CampfireBlock;
                            if(isFurnaceLike){ // furnace likes
                                if(level.getBlockState(getBlockPos().relative(direction)).getValue(AbstractFurnaceBlock.LIT)){
                                    generateEnergy(500);
                                    heaterCount++;
                                }
                            }
                            else if(isCampfireLike){ // campfire lower tier
                                if(level.getBlockState(getBlockPos().relative(direction)).getValue(BlockStateProperties.LIT)){
                                    generateEnergy(500);
                                    heaterCount++;
                                }
                            }
                            else{
                                generateEnergy(500);
                                heaterCount++;
                            }
                        }
                        else if(level.getBlockState(getBlockPos().relative(direction)).is(Tag.HIGH_HEAT_PRODUCERS)){
                            boolean isCampfireLike = level.getBlockState(getBlockPos().relative(direction)).getBlock() instanceof CampfireBlock;
                            if(isCampfireLike){
                                if(level.getBlockState(getBlockPos().relative(direction)).getValue(BlockStateProperties.LIT)){
                                    generateEnergy(1000);
                                    heaterCount++;
                                }
                            }
                            else{
                                generateEnergy(1000);
                                heaterCount++;
                            }
                        }
                    }
                }
                if(heaterCount > 0) {
                    if (level.getBlockState(getBlockPos()).getBlock() instanceof HeatGenerator) {
                        if (!getBlockState().getValue(BlockStateProperties.POWERED)) {
                            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true));
                            setChanged();
                        }
                    }
                }
                else{
                    if(level.getBlockState(getBlockPos()).getBlock() instanceof HeatGenerator){
                        if(getBlockState().getValue(BlockStateProperties.POWERED)){
                            level.setBlockAndUpdate(getBlockPos(),getBlockState().setValue(BlockStateProperties.POWERED,false));
                            setChanged();
                        }
                    }
                }
            }
            distributeEnergy();
        }

        if(level.getBlockState(getBlockPos()).getBlock() instanceof HeatGenerator) {
            if(getBlockState().getValue(BlockStateProperties.POWERED) && ticks % 82 == 0) {
                level.playSound(null,getBlockPos(),
                        CmatdSound.HEATER_LOOP.get(), SoundSource.BLOCKS,
                        0.75f,1.0f);
            }
        }
    }

    private void generateEnergy(int amount){
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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy",energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
    }
}
