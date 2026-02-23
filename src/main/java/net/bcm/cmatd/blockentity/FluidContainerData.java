package net.bcm.cmatd.blockentity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.ContainerData;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidContainerData implements ContainerData {
    FoodReactorMultiblock be;

    public FluidContainerData(FoodReactorMultiblock foodReactor){
        this.be = foodReactor;
    }

    // from dire

    @Override
    public int get(int index) {
        int lookind;
        switch (index){
            case 0 -> {
                lookind = BuiltInRegistries.FLUID.getId(this.be.getFluidTank().getFluid().getFluid());
            }
            case 1 -> {
                lookind = this.be.getFluidTank().getFluidAmount() & '\uffff';
            }
            case 2 -> {
                lookind = this.be.getFluidTank().getFluidAmount() >> 16;
            }
            default -> {
                throw new IllegalArgumentException("Index out of bounds: " + index);
            }
        }
        return lookind;
    }

    @Override
    public void set(int index, int value) {
        switch (index){
            case 0 -> {
                this.be.getFluidTank().setFluid(new FluidStack(BuiltInRegistries.FLUID.byId(value),this.be.getFluidTank().getFluidAmount()));
            }
            case 1 -> {
                this.be.getFluidTank().getFluid().setAmount(this.be.getFluidTank().getFluidAmount() & -65536 | value & '\uffff');
            }
            case 2 -> {
                this.be.getFluidTank().getFluid().setAmount(this.be.getFluidTank().getFluidAmount() & '\uffff' | value << 16);
            }
            default -> {
                throw new IllegalArgumentException("Index out of bounds: " + index);
            }
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
