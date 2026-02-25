package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.blockentity.BaseEnergyMakerBE;
import net.bcm.cmatd.blockentity.CmatdBE;
import net.bcm.cmatd.blockentity.RedstoneDynamoEngineBE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RedstoneDynamoEngine extends BaseEntityBlock implements EntityBlock,SimpleWaterloggedBlock {
    public static final MapCodec<RedstoneDynamoEngine> CODEC = Block.simpleCodec(RedstoneDynamoEngine::new);
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.generator.gen_rate",10)
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("desc.item.redstone_dynamo_engine.additional_info",10,Component.translatable(Blocks.REDSTONE_BLOCK.getDescriptionId()))
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston){
        if (newState.is(state.getBlock())){
            return;
        }
        RedstoneDynamoEngineBE be = (RedstoneDynamoEngineBE)level.getBlockEntity(pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public RedstoneDynamoEngine(BlockBehaviour.Properties properties) {
        super(properties.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.FACING, Direction.UP)
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(LIT,false)
        );
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidstate) {
        return state.getValue(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState bs) {
        return bs.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(bs);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite())
                .setValue(BlockStateProperties.WATERLOGGED, fluidstate.getType() == Fluids.WATER)
                .setValue(LIT,context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.FACING,BlockStateProperties.WATERLOGGED,LIT);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean flag = state.getValue(LIT);
            if (flag != level.hasNeighborSignal(pos)) {
                if (flag) {
                    level.scheduleTick(pos, this, 4);
                } else {
                    level.setBlock(pos, state.cycle(LIT), 2);
                }
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(LIT), 2);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return (lvl,pos,st,bet) -> {
                if(bet instanceof RedstoneDynamoEngineBE gen){
                    gen.clientTick();
                }
            };
        }
        else{
            return (lvl,pos,st,bet) -> {
                if(bet instanceof RedstoneDynamoEngineBE gen){
                    gen.serverTick();
                }
            };
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneDynamoEngineBE(pos,state);
    }
}
