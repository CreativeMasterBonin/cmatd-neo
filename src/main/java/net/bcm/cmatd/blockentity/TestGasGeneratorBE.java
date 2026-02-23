package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.api.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TestGasGeneratorBE extends BlockEntity implements GasDistributor {
    private GasTank gasTank = new GasTank(100000){
        @Override
        public void update() {
            setChanged();
        }
    };
    private int ticks;

    public GasTank getGasTank(){
        return gasTank;
    }

    public TestGasGeneratorBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.TEST_GAS_GENERATOR.get(), pos, blockState);
    }

    public void serverTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
        }

        if(ticks % 20 == 0){
            generateGas();
            distributeGas();
        }
    }

    @Override
    public void distributeGas(){
        for (Direction direction : Direction.values()) {
            if (gasTank.getGasAmount() <= 0) {
                return;
            }
            // get blocks that are energy capable
            IGasHandler handler = level.getCapability(Capabilities.GasHandler.BLOCK,
                    getBlockPos().relative(direction), null);
            if (handler != null) {
                if (handler.getGasTanks() >= 1) {
                    int received = handler.fill(this.gasTank.getGasStack(),false);
                    this.gasTank.drain(received, false);
                    setChanged();
                }
            }
        }
    }

    @Override
    public void generateGas(){
        if(this.gasTank.getGasAmount() < this.gasTank.getCapacity()){
            if(this.gasTank.getGasStack().getGas() == Gases.METHANE.get()){
                this.gasTank.getGasStack().add(100);
            }
            else{
                this.gasTank.fill(new GasStack(Gases.METHANE,100),false);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        gasTank.save(registries,tag);
        tag.putInt("ticks",ticks);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        gasTank.load(registries,tag);
        ticks = tag.getInt("ticks");
    }
}
