package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.api.GasTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractGasContainingBE extends BlockEntity {
    public AbstractGasContainingBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    private GasTank gasTank;
    private DieselEngineGasContainerData gasContainerData;
    public int gasAmount;

    public GasTank getGasTank(){
        return this.gasTank;
    }
}
