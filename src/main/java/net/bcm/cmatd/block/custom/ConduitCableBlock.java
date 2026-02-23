package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.ConduitType;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.blockentity.ConduitBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ConduitCableBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    // mcjty tutorial
    public static final EnumProperty<ConduitType> NORTH =
            EnumProperty.create("north",ConduitType.class);
    public static final EnumProperty<ConduitType> SOUTH =
            EnumProperty.create("south",ConduitType.class);
    public static final EnumProperty<ConduitType> EAST =
            EnumProperty.create("east",ConduitType.class);
    public static final EnumProperty<ConduitType> WEST =
            EnumProperty.create("west",ConduitType.class);
    public static final EnumProperty<ConduitType> DOWN =
            EnumProperty.create("down",ConduitType.class);
    public static final EnumProperty<ConduitType> UP =
            EnumProperty.create("up",ConduitType.class);

    public static final ModelProperty<BlockState> FACADE_ID = new ModelProperty<>();

    public static VoxelShape[] CACHE = null;

    public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected int calculateShapeIndex(ConduitType north, ConduitType south, ConduitType west, ConduitType east, ConduitType up, ConduitType down) {
        int length = ConduitType.values().length;
        return ((((south.ordinal() * length + north.ordinal()) * length + west.ordinal()) * length + east.ordinal()) * length + up.ordinal()) * length + down.ordinal();
    }

    protected void makeShapes() {
        if (CACHE == null) {
            int length = ConduitType.values().length;
            CACHE = new VoxelShape[length * length * length * length * length * length];

            for (ConduitType up : ConduitType.VALUES) {
                for (ConduitType down : ConduitType.VALUES) {
                    for (ConduitType north : ConduitType.VALUES) {
                        for (ConduitType south : ConduitType.VALUES) {
                            for (ConduitType east : ConduitType.VALUES) {
                                for (ConduitType west : ConduitType.VALUES) {
                                    int index = calculateShapeIndex(north, south, west, east, up, down);
                                    CACHE[index] = makeShape(north, south, west, east, up, down);
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    protected VoxelShape makeShape(ConduitType north, ConduitType south, ConduitType west, ConduitType east, ConduitType up, ConduitType down) {
        VoxelShape centerShape = Utility.CENTER_CONDUIT_CONNECTOR;
        centerShape = combineShape(centerShape, north, Utility.NORTH_CONDUIT, Utility.NORTH_CONDUIT_BLOCK);
        centerShape = combineShape(centerShape, south, Utility.SOUTH_CONDUIT, Utility.SOUTH_CONDUIT_BLOCK);
        centerShape = combineShape(centerShape, west, Utility.WEST_CONDUIT, Utility.WEST_CONDUIT_BLOCK);
        centerShape = combineShape(centerShape, east, Utility.EAST_CONDUIT, Utility.EAST_CONDUIT_BLOCK);
        centerShape = combineShape(centerShape, up, Utility.UP_CONDUIT, Utility.UP_CONDUIT_BLOCK);
        centerShape = combineShape(centerShape, down, Utility.DOWN_CONDUIT, Utility.DOWN_CONDUIT_BLOCK);
        return centerShape;
    }

    protected VoxelShape combineShape(VoxelShape shape, ConduitType connectorType, VoxelShape cableShape, VoxelShape blockShape) {
        if (connectorType == ConduitType.CONDUIT) {
            return Shapes.join(shape, cableShape, BooleanOp.OR);
        } else if (connectorType == ConduitType.BLOCK) {
            return Shapes.join(shape, Shapes.join(blockShape, cableShape, BooleanOp.OR), BooleanOp.OR);
        } else {
            return shape;
        }
    }
    // end mcjty

    public ConduitCableBlock(Properties properties) {
        super(properties.strength(1f,20f)
                .mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK));
        makeShapes();
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        ConduitType north = get(level, pos, Direction.NORTH);
        ConduitType south = get(level, pos, Direction.SOUTH);
        ConduitType west = get(level, pos, Direction.WEST);
        ConduitType east = get(level, pos, Direction.EAST);
        ConduitType up = get(level, pos, Direction.UP);
        ConduitType down = get(level, pos, Direction.DOWN);
        int index = calculateShapeIndex(north, south, west, east, up, down);
        return CACHE[index];
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        LevelReader lvl = (LevelReader)level;
        if(state.getValue(WATERLOGGED)){
            level.getFluidTicks().schedule(new ScheduledTick<>(Fluids.WATER,pos,Fluids.WATER.getTickDelay(lvl),0L));
        }
        return calculateState(level,pos,state);
    }

    // block entity

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ConduitBE(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return null;
        }
        else{
            return (lvl,pos,st,be) -> {
                if(be instanceof ConduitBE){
                    ((ConduitBE) be).serverTick();
                }
            };
        }
    }

    // end block entity

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ConduitBE conduit) {
            conduit.notUpdatedRefresh();
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ConduitBE conduit) {
            conduit.notUpdatedRefresh();
        }
        BlockState blockState = calculateState(level, pos, state);
        if (state != blockState) {
            level.setBlockAndUpdate(pos, blockState);
        }
    }

    // pos and direction determine connecting block thingy
    private static ConduitType get(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof ConduitCableBlock) {
            return ConduitType.CONDUIT;
        }
        else if (canConnect(world, connectorPos, facing)) {
            return ConduitType.BLOCK;
        }
        else {
            return ConduitType.NONE;
        }
    }

    // support energy then can connect
    public static boolean canConnect(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (te == null) {
            return false;
        }
        return te.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, pos, null) != null;
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return calculateState(world, pos, defaultBlockState())
                .setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
    }

    @Nonnull
    protected static BlockState calculateState(LevelAccessor world, BlockPos pos, BlockState state) {
        ConduitType north = get(world, pos, Direction.NORTH);
        ConduitType south = get(world, pos, Direction.SOUTH);
        ConduitType west = get(world, pos, Direction.WEST);
        ConduitType east = get(world, pos, Direction.EAST);
        ConduitType up = get(world, pos, Direction.UP);
        ConduitType down = get(world, pos, Direction.DOWN);

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(EAST, east)
                .setValue(UP, up)
                .setValue(DOWN, down);
    }

    @Nonnull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(state.getBlock())){
            return;
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
