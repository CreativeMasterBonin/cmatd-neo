package net.bcm.cmatd.fluid.custom;

import net.bcm.cmatd.fluid.CmatdFluid;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class RadioactiveWasteFluid extends BaseFlowingFluid {
    public static final Properties PROPERTIES =
            new BaseFlowingFluid.Properties(
                    CmatdFluid.RADIOACTIVE_WASTE_TYPE,
                    CmatdFluid.RADIOACTIVE_WASTE_FLUID_SOURCE,
                    CmatdFluid.RADIOACTIVE_WASTE_FLUID_FLOWING
            );

    public RadioactiveWasteFluid(Properties properties) {
        super(properties);
    }

    public Fluid getFlowing() {
        return CmatdFluid.RADIOACTIVE_WASTE_FLUID_FLOWING.get();
    }

    public Fluid getSource() {
        return CmatdFluid.RADIOACTIVE_WASTE_FLUID_SOURCE.get();
    }

    public Item getBucket() {
        return CmatdItem.RADIOACTIVE_FLUID_BUCKET.asItem();
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return false;
    }

    protected boolean canConvertToSource(Level pLevel) {
        return false;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 0;
    }

    public static class RadioactiveWasteFluidSource extends RadioactiveWasteFluid {
        public RadioactiveWasteFluidSource() {
            super(PROPERTIES);
        }

        public int getAmount(FluidState pState) {
            return 8;
        }

        public boolean isSource(FluidState pState) {
            return true;
        }
    }

    public static class RadioactiveWasteFluidFlowing extends RadioactiveWasteFluid {
        public RadioactiveWasteFluidFlowing() {
            super(PROPERTIES);
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> pBuilder) {
            super.createFluidStateDefinition(pBuilder);
            pBuilder.add(LEVEL);
        }

        public int getAmount(FluidState pState) {
            return pState.getValue(LEVEL);
        }

        public boolean isSource(FluidState pState) {
            return false;
        }
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return level.dimensionType().ultraWarm() ? 13 : 17;
    }
}
