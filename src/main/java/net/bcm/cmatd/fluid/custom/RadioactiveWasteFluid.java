package net.bcm.cmatd.fluid.custom;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.fluid.CmatdFluid;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
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
            ).bucket(CmatdItem.RADIOACTIVE_WASTE_BUCKET)
                    .block(() -> (LiquidBlock)CmatdBlock.RADIOACTIVE_WASTE.get())
                    .explosionResistance(100f)
                    .levelDecreasePerBlock(1)
                    .slopeFindDistance(4);

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
        return CmatdItem.RADIOACTIVE_WASTE_BUCKET.asItem();
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

        @Override
        public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
            BlockPos blockpos = pos.above();
            if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level,blockpos)) {
                if (random.nextInt(172) == 0) {
                    level.addParticle(ParticleTypes.SNEEZE,
                            pos.getX() + random.nextDouble(),
                            pos.getY() + 1.0,
                            pos.getZ() + random.nextDouble(),
                            0.0,
                            0.0,
                            0.0);
                }

                if (random.nextInt(321) == 0) {
                    level.playLocalSound(
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            SoundEvents.LAVA_AMBIENT,
                            SoundSource.BLOCKS,
                            0.2F + random.nextFloat() * 0.2F,
                            0.5F + random.nextFloat() * 0.15F,
                            false
                    );
                }
            }
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

        @Override
        protected void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
            BlockPos blockpos = pos.above();
            if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level,blockpos)) {
                if (random.nextInt(335) == 0) {
                    level.addParticle(ParticleTypes.SNEEZE,
                            pos.getX() + random.nextDouble(),
                            pos.getY() + 1.0,
                            pos.getZ() + random.nextDouble(),
                            0.0,
                            0.0,
                            0.0);
                }
            }
        }
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return level.dimensionType().ultraWarm() ? 13 : 17;
    }
}
