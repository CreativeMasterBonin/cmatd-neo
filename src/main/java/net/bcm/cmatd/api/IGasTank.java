package net.bcm.cmatd.api;

/**
 * From NeoForge, changed to support GasStack
 */
public interface IGasTank{
    GasStack getGasStack();
    int getGasAmount();
    int getCapacity();
    boolean isGasValid();

    int fill(GasStack gasStack, boolean simulate);

    GasStack drain(int amount, boolean simulate);

    GasStack drain(GasStack gasStack, boolean simulate);
}