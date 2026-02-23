package net.bcm.cmatd;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class BaseFluidStorage extends FluidTank implements INBTSerializable<CompoundTag>{
    public BaseFluidStorage(int fluidCapacity){
        super(fluidCapacity);
    }

    public BaseFluidStorage(int fluidCapacity, Predicate<FluidStack> predicateValidator){
        super(fluidCapacity,predicateValidator);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return super.writeToNBT(provider,new CompoundTag());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.fluid = super.readFromNBT(provider,nbt).getFluid();
    }
}
