package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.blockentity.TestGasGeneratorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TestGasGenerator extends BaseEntityBlock {
    public static final MapCodec<TestGasGenerator> CODEC =
            simpleCodec(TestGasGenerator::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public TestGasGenerator(Properties properties) {
        super(properties.noLootTable());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(state.getBlock())){
            return;
        }
        super.onRemove(state,level,pos,newState,movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult){
        if(player.isSecondaryUseActive()){
            if(level.getBlockEntity(pos) instanceof TestGasGeneratorBE gasGeneratorBE){
                
            }
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TestGasGeneratorBE(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return null;
        }
        else{
            return (lvl,pos,st,bet) -> {
                if(bet instanceof TestGasGeneratorBE gen){
                    gen.serverTick();
                }
            };
        }
    }
}
