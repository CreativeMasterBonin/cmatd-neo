package net.bcm.cmatd.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class TieredMachine extends BlockEntity {
    public int machineTier = 0;
    public final int maxMachineTier;
    public final IEnergyStorage battery; // it 'is' basically a battery
    public abstract int getMaxMachineTier();
    public abstract IEnergyStorage getEnergyStorage();
    public String operationTime = operationTime(OperationTime.ALWAYS);

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

    public String operationTime(OperationTime opTime){
        return opTime.getSerializedName();
    }

    public TieredMachine(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        maxMachineTier = getMaxMachineTier();
        battery = getEnergyStorage();
    }
}
