package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.api.GasStack;
import net.bcm.cmatd.api.Registries;
import net.minecraft.world.inventory.ContainerData;

public class RadioactiveReactorGasContainerData implements ContainerData {
    RadioactiveReactor be;

    public RadioactiveReactorGasContainerData(RadioactiveReactor reactor){
        this.be = reactor;
    }

    // gas amount
    @Override
    public int get(int index) {
        int lookind;
        switch (index){
            case 0 -> {
                lookind = Registries.GAS_TYPES.getId(this.be.getWasteGasTank().getGasStack().getGas());
            }
            case 1 -> {
                lookind = this.be.getWasteGasTank().getGasAmount() & '\uffff';
            }
            case 2 -> {
                lookind = this.be.getWasteGasTank().getGasAmount() >> 16;
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
                this.be.getWasteGasTank().setGas(new GasStack(
                        Registries.GAS_TYPES.byId(value),
                                this.be.getWasteGasTank().getGasAmount()),
                        true);
            }
            case 1 -> {
                this.be.getWasteGasTank().getGasStack().setAmount(this.be.getWasteGasTank().getGasAmount() & -65536 | value & '\uffff');
            }
            case 2 -> {
                this.be.getWasteGasTank().getGasStack().setAmount(this.be.getWasteGasTank().getGasAmount() & '\uffff' | value << 16);
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
