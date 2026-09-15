package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.blockentity.TieredMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TieredMachineBlock extends BaseEntityBlock {
    public static final MapCodec<TieredMachineBlock> CODEC = simpleCodec(TieredMachineBlock::new);

    public TieredMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()){
            return null;
        }
        else{
            return (level1, blockPos, blockState, be) -> {
                if(be instanceof TieredMachine){
                    ((TieredMachine) be).serverTick();
                }
            };
        }
    }
}
