package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.CmatdClient;
import net.bcm.cmatd.CmatdClientActionHandler;
import net.bcm.cmatd.blockentity.LightningGeneratorBE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class LightningGenerator extends Block implements EntityBlock {
    public static final MapCodec<LightningGenerator> CODEC =
            simpleCodec(LightningGenerator::new);
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
    public static final VoxelShape ALL = Stream.of(
            Block.box(0, 0, 0, 16, 8, 16),
            Block.box(7, 8, 7, 9, 16, 9),
            Block.box(5, 8, 5, 11, 10, 11),
            Block.box(4, 11, 4, 12, 13, 12),
            Block.box(3, 13.99, 3, 13, 15.99, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public LightningGenerator(BlockBehaviour.Properties properties) {
        super(properties.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK)
                .noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.POWERED,false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ALL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.POWERED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.POWERED,false);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(state.getValue(BlockStateProperties.POWERED)){
            level.addAlwaysVisibleParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    true,
                    (double)pos.getX() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? -1 : 1),
                    (double)pos.getY() + random.nextDouble() + random.nextDouble(),
                    (double)pos.getZ() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? -1 : 1),
                    0.0,
                    0.02,
                    0.0
            );
            if(random.nextIntBetweenInclusive(1,21) <= 7){
                level.addAlwaysVisibleParticle(
                        ParticleTypes.ELECTRIC_SPARK,
                        true,
                        (double)pos.getX() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? -1 : 1),
                        (double)pos.getY() + random.nextDouble() + random.nextDouble(),
                        (double)pos.getZ() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? -1 : 1),
                        0.0,
                        0.0,
                        0.0
                );
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.generator.gen_rate",1_000_000)
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("desc.item.lightning_generator.additional_info")
                .withStyle(ChatFormatting.GOLD));
        if(CmatdClientActionHandler.keyMappingPressed(CmatdClient.itemDescriptionKeyMapping)){
            tooltipComponents.add(Component.translatable("desc.item.generator.lightning_generator.cooldown")
                    .withColor(14448511));
        }
        else{
            tooltipComponents.add(Component.translatable("desc.item.generator.hidden_details",
                            Component.translatable(CmatdClient.itemDescriptionKeyMapping.getKey().getName()))
                    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston){
        if(newState.is(state.getBlock())){
            return;
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LightningGeneratorBE(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType){
        if(level.isClientSide){
            return (lvl,pos,st,bet) -> {
                if(bet instanceof LightningGeneratorBE gen){
                    gen.clientTick();
                }
            };
        }
        else{
            return (lvl,pos,st,bet) -> {
                if(bet instanceof LightningGeneratorBE gen){
                    gen.serverTick();
                }
            };
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
