package net.bcm.cmatd.fluid;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.fluid.custom.RadioactiveWasteFluid;
import net.bcm.cmatd.fluid.custom.RadioactiveWasteFluidType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class CmatdFluid {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Cmatd.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Cmatd.MODID);

    public static final DeferredHolder<FluidType,FluidType> RADIOACTIVE_WASTE_TYPE =
            FLUID_TYPES.register("radioactive_waste",() -> new RadioactiveWasteFluidType(
                    FluidType.Properties.create()
                            .density(12000)
                            .viscosity(7500)
                            .temperature(900)
                            .sound(
                                    SoundActions.BUCKET_FILL,
                                    SoundEvents.BUCKET_FILL)
                            .sound(
                                    SoundActions.BUCKET_EMPTY,
                                    SoundEvents.BUCKET_EMPTY)
                            .sound(
                                    SoundActions.FLUID_VAPORIZE,
                                    SoundEvents.FIRE_EXTINGUISH
                            )
                            .adjacentPathType(PathType.WATER)
                            .canConvertToSource(false)
                            .motionScale(0.007D)
                            .supportsBoating(true)
                            .canExtinguish(false)
                            .canHydrate(false)
                            .canSwim(true)
                            .canDrown(true)
            ));

    public static final DeferredHolder<Fluid,Fluid> RADIOACTIVE_WASTE_FLUID_FLOWING =
            FLUIDS.register("radioactive_waste_flowing", RadioactiveWasteFluid.RadioactiveWasteFluidFlowing::new);
    public static final DeferredHolder<Fluid,Fluid> RADIOACTIVE_WASTE_FLUID_SOURCE =
            FLUIDS.register("radioactive_waste", RadioactiveWasteFluid.RadioactiveWasteFluidSource::new);
}
