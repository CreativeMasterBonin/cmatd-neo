package net.bcm.cmatd.blockentity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.ContainerData;
import net.neoforged.neoforge.fluids.FluidStack;

public class RadioactiveReactorFluidContainerData implements ContainerData {
    RadioactiveReactor be;

    public RadioactiveReactorFluidContainerData(RadioactiveReactor reactor){
        this.be = reactor;
    }

    @Override
    public int get(int index) {
        int lookind;
        switch (index){
            case 0 -> {
                lookind = BuiltInRegistries.FLUID.getId(this.be.getWasteConvertedToFluidTank().getFluid().getFluid());
            }
            case 1 -> {
                lookind = this.be.getWasteConvertedToFluidTank().getFluidAmount() & '\uffff';
            }
            case 2 -> {
                lookind = this.be.getWasteConvertedToFluidTank().getFluidAmount() >> 16;
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
                this.be.getWasteConvertedToFluidTank().setFluid(new FluidStack(BuiltInRegistries.FLUID.byId(value),this.be.getWasteConvertedToFluidTank().getFluidAmount()));
            }
            case 1 -> {
                this.be.getWasteConvertedToFluidTank().getFluid().setAmount(this.be.getWasteConvertedToFluidTank().getFluidAmount() & -65536 | value & '\uffff');
            }
            case 2 -> {
                this.be.getWasteConvertedToFluidTank().getFluid().setAmount(this.be.getWasteConvertedToFluidTank().getFluidAmount() & '\uffff' | value << 16);
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
