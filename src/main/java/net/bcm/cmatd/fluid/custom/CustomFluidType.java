package net.bcm.cmatd.fluid.custom;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

public abstract class CustomFluidType extends FluidType {
    public CustomFluidType(Properties properties) {
        super(properties);
    }

    public abstract ResourceLocation getStillTexture();
}
