package net.bcm.cmatd.api;

/**
 * From NeoForge, changed to support GasStack
 */
public interface IGasHandler{
    int getGasTanks();

    /**
     * <em>NOT TO BE USED FOR MODIFICATION OF GAS STACK!</em>
     * @return Returns an unmodifiable GasStack
     */
    GasStack getGasStack(int gasTank);

    /**
     * Gets the max gas capacity of the passed in tank id
     * @param gasTank The id of the tank to be checked
     * @return The max capacity
     */
    int getGasTankCapacity(int gasTank);

    /**
     * Checks if a gas is valid
     * @param gasTank The id of the tank
     * @param gasStack The gas to be passed in
     * @return result of whether the gas is valid
     */
    boolean isGasValid(int gasTank, GasStack gasStack);

    int fill(GasStack gasStack, boolean simulate);
    int fill(int amount, boolean simulate);
    GasStack drain(GasStack gasStack, boolean simulate);
    GasStack drain(int amount, boolean simulate);
}
