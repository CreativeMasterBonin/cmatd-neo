package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.CmatdSound;
import net.bcm.cmatd.api.GasStack;
import net.bcm.cmatd.api.GasTank;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.datagen.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class DieselEngineBE extends AbstractGasContainingBE implements RotationalEnergyProducer {
    public int ticks = 0;

    public static final int rotationalOutputAdditive = 10;

    private GasTank gasTank = new GasTank(64000){
        @Override
        public void update() {
            setChanged();
            if(!level.isClientSide){
                level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            }
        }
    };
    private DieselEngineGasContainerData gasContainerData;
    public int gasAmount = 0;

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag,registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag,lookupProvider);
    }

    public int getGasAmount() {
        return this.gasAmount;
    }

    public GasTank getGasTank(){
        return gasTank;
    }
    public DieselEngineGasContainerData getGasContainerData(){
        return gasContainerData;
    }

    public DieselEngineBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.DIESEL_ENGINE.get(), pos, blockState);
        this.gasContainerData = new DieselEngineGasContainerData(this);
    }

    public void clientTick(){
        ticks++;
        if(ticks >= 32767){
            this.gasAmount = gasTank.gas.getAmount();
            ticks = 0;
        }
    }

    public void serverTick(){
        ticks++;

        if(ticks % 4 == 0){
            this.gasAmount = gasTank.gas.getAmount();
        }

        if(getGasTank().getGasStack() != GasStack.EMPTY){
            if(getGasTank().getGasAmount() > 0){
                // dynamic speedup heating system
                int ticksPerActionNeeded = 10;
                int fuelNeededPerGo = 20;
                int tierOfHeat = 1;
                if(level.getBlockState(worldPosition.above()).is(Tag.HIGH_HEAT_PRODUCERS)){
                    ticksPerActionNeeded = 2;
                    fuelNeededPerGo = 5;
                    tierOfHeat = 10;
                }
                else if(level.getBlockState(worldPosition.above()).is(Tag.MEDIUM_HEAT_PRODUCERS)){
                    ticksPerActionNeeded = 4;
                    fuelNeededPerGo = 7;
                    tierOfHeat = 4;
                }
                else if(level.getBlockState(worldPosition.above()).is(Tag.LOW_HEAT_PRODUCERS)){
                    ticksPerActionNeeded = 8;
                    fuelNeededPerGo = 14;
                    tierOfHeat = 2;
                }
                else{
                    ticksPerActionNeeded = 10;
                    fuelNeededPerGo = 20;
                    tierOfHeat = 1;
                }

                // when the heat level additive is selected and the fuel amount per cycle is set, do some math, then set the rotational speed and use some gas up
                if(ticks % ticksPerActionNeeded == 0){
                    setRotationalOutputSpeed(getRotationalOutputSpeed() + (rotationalOutputAdditive * tierOfHeat));
                    getGasTank().getGasStack().remove(fuelNeededPerGo + Mth.clamp(getRotationalOutputSpeed() / 32,1,95));
                }

                if(!getBlockState().getValue(BlockStateProperties.POWERED)){
                    getLevel().setBlock(getBlockPos(),getBlockState().setValue(BlockStateProperties.POWERED,true),3);
                }
                if(ticks % 7 == 0){
                    if(getRotationalOutputSpeed() >= getMaxRotationalOutputSpeed()){
                        setRotationalOutputSpeed(getMaxRotationalOutputSpeed());
                    }
                }
                if(ticks % 47 == 0){
                    level.playSound(null,getBlockPos(), CmatdSound.ENGINE_LOOP.get(), SoundSource.BLOCKS,0.75f,1.0f);
                }
            }
            else if(getGasTank().getGasAmount() <= 0){
                getGasTank().setGas(GasStack.EMPTY,true);
                setChanged();
            }
        }
        else{
            if(getBlockState().getValue(BlockStateProperties.POWERED)){
                getLevel().setBlock(getBlockPos(),getBlockState().setValue(BlockStateProperties.POWERED,false),3);
                setChanged();
            }
            else{
                if(getRotationalOutputSpeed() > 0){
                    setRotationalOutputSpeed(getRotationalOutputSpeed() - 200); // slow down fast as the engine is not visually moving
                }
                else if(getRotationalOutputSpeed() <= 202 && getRotationalOutputSpeed() != 0){
                    setRotationalOutputSpeed(0);
                }
            }
        }

        if(ticks >= 32767){
            ticks = 0;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        gasTank.save(registries,tag);
        tag.putInt("gas_amount",gasAmount);
        tag.putInt("rotational_energy",rotationalEnergy);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        gasTank.load(registries,tag);
        gasAmount = tag.getInt("gas_amount");
        rotationalEnergy = tag.getInt("rotational_energy");
    }

    public int rotationalEnergy = 0;

    @Override
    public int getRotationalOutputSpeed() {
        return rotationalEnergy;
    }

    @Override
    public void setRotationalOutputSpeed(int rotation) {
        this.rotationalEnergy = rotation;
        this.setChanged();
    }

    @Override
    public int getMaxRotationalOutputSpeed() {
        return 2570;
    }
}
