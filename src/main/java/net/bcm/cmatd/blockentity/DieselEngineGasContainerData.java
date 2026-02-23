package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.api.GasStack;
import net.bcm.cmatd.api.Registries;
import net.minecraft.world.inventory.ContainerData;

public class DieselEngineGasContainerData implements ContainerData {
    DieselEngineBE be;

    public DieselEngineGasContainerData(DieselEngineBE gasTankBE){
        this.be = gasTankBE;
    }

    // gas amount
    @Override
    public int get(int index) {
        int lookind;
        switch (index){
            case 0 -> {
                lookind = Registries.GAS_TYPES.getId(this.be.getGasTank().getGasStack().getGas());
            }
            case 1 -> {
                lookind = this.be.getGasTank().getGasAmount() & '\uffff';
            }
            case 2 -> {
                lookind = this.be.getGasTank().getGasAmount() >> 16;
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
                this.be.getGasTank().setGas(new GasStack(
                        Registries.GAS_TYPES.byId(value),
                                this.be.getGasTank().getGasAmount()),
                        true);
            }
            case 1 -> {
                this.be.getGasTank().getGasStack().setAmount(this.be.getGasTank().getGasAmount() & -65536 | value & '\uffff');
            }
            case 2 -> {
                this.be.getGasTank().getGasStack().setAmount(this.be.getGasTank().getGasAmount() & '\uffff' | value << 16);
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
