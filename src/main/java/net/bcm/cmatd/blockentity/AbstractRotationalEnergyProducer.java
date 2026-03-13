package net.bcm.cmatd.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractRotationalEnergyProducer extends BlockEntity implements RotationalEnergyProducer{
    public int rotationalEnergy;

    public AbstractRotationalEnergyProducer(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public int getRotationalOutputSpeed() {
        return rotationalEnergy;
    }

    public void setRotationalOutputSpeed(int rotation) {
        this.rotationalEnergy = rotation;
    }
}
