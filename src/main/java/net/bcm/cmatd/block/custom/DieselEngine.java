package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.blockentity.DieselEngineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class DieselEngine extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public DieselEngine(Properties properties) {
        super(properties.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_LIGHT_GRAY)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.POWERED,false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(state.getBlock())){
            return;
        }
        super.onRemove(state,level,pos,newState,movedByPiston);
    }

    public static final MapCodec<DieselEngine> CODEC =
            simpleCodec(DieselEngine::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DieselEngineBE(pos,state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(level.getBlockState(pos).getBlock() instanceof HeatGenerator){
            if(state.getValue(BlockStateProperties.POWERED)) {
                boolean isAirTypeBlockAbove = level.getBlockState(pos.above()).is(Blocks.AIR) || level.getBlockState(pos.above()).is(Blocks.CAVE_AIR) || level.getBlockState(pos.above()).is(Blocks.VOID_AIR);
                if(random.nextDouble() < 0.1){
                    level.playLocalSound(pos.getX(),pos.getY(),pos.getZ(),
                            SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 0.75F, false);
                }
                if(isAirTypeBlockAbove){
                    if(random.nextDouble() <= 0.05){
                        level.addParticle(ParticleTypes.LARGE_SMOKE,
                                pos.getX() + 0.5,pos.getY() + 0.85,pos.getZ() + 0.5, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.POWERED,false)
                .setValue(FACING,context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.POWERED,FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return (lvl,pos,st,bet) -> {
                if(bet instanceof DieselEngineBE gen){
                    gen.clientTick();
                }
            };
        }
        else{
            return (lvl,pos,st,bet) -> {
                if(bet instanceof DieselEngineBE engineBE){
                    engineBE.serverTick();
                }
            };
        }
    }
}
