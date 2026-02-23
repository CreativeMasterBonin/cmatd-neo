package net.bcm.cmatd.api;


import net.minecraft.resources.ResourceLocation;

/**
 * From NeoForge, changed to support GasStack
 */
@SuppressWarnings("deprecated")
public final class GasStackHelper {
    /**
     * Experimental getNewStack method
     * Returns a new stack with a GasType specified
     */
    public GasStack getNewStack(GasType type, int amt){
        if(!Registries.GAS_TYPES.containsKey(ResourceLocation.parse(type.getDescriptionId()))){
            return new GasStack(Gases.EMPTY,0);
        }
        return new GasStack(type,amt);
    }

    public boolean isStackRadioactive(GasStack stack){
        return stack.getGas().isRadioactive();
    }

    public float getRadioactivePotency(GasStack stack){
        return stack.getGas().getRadioactivity();
    }

    public boolean isStackHeavyWeight(GasStack stack){
        return stack.getGas().isHeavyGas();
    }

    public boolean isStackLightWeight(GasStack stack){
        return stack.getGas().isLightGas();
    }
}
