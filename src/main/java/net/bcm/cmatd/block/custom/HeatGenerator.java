package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.CmatdClientActionHandler;
import net.bcm.cmatd.CmatdClient;
import net.bcm.cmatd.blockentity.HeatGeneratorBE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeatGenerator extends Block implements EntityBlock {
    public static final MapCodec<HeatGenerator> CODEC =
            simpleCodec(HeatGenerator::new);
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public HeatGenerator(Properties properties) {
        super(properties.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.POWERED,false));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.generator.gen_rate",10)
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("desc.item.generator.heat_generator.additional_info")
                .withStyle(ChatFormatting.GOLD));
        if(CmatdClientActionHandler.keyMappingPressed(CmatdClient.itemDescriptionKeyMapping)){
            tooltipComponents.add(Component.translatable("desc.item.generator.heat_generator.heat_sources_low").withColor(16770304));
            tooltipComponents.add(Component.translatable("desc.item.generator.heat_generator.heat_sources_medium").withColor(15697920));
            tooltipComponents.add(Component.translatable("desc.item.generator.heat_generator.heat_sources_high").withColor(15678208));
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
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HeatGeneratorBE(pos,state);
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
                        level.addParticle(ParticleTypes.FLAME,
                                pos.getX() + 0.5,pos.getY() + 0.85,pos.getZ() + 0.5, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType){
        if(level.isClientSide){
            return null;
        }
        else{
            return (lvl,pos,st,bet) -> {
                if(bet instanceof HeatGeneratorBE gen){
                    gen.serverTick();
                }
            };
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.POWERED,false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.POWERED);
    }
}
