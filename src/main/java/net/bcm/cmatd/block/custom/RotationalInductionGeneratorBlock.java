package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.blockentity.RotationalInductionGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RotationalInductionGeneratorBlock extends BaseEntityBlock {
    public static final MapCodec<RotationalInductionGeneratorBlock> CODEC =
            simpleCodec(RotationalInductionGeneratorBlock::new);

    public RotationalInductionGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.generator.gen_rate_variable",Component.translatable("desc.item.generator.variable_type.rotational_ability"))
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("desc.item.generator.rotational_induction_generator.additional_info")
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RotationalInductionGenerator(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return (lvl,pos,st,bet) -> {
                if(bet instanceof RotationalInductionGenerator gen){
                    gen.clientTick();
                }
            };
        }
        else{
            return (lvl,pos,st,bet) -> {
                if(bet instanceof RotationalInductionGenerator engineBE){
                    engineBE.serverTick();
                }
            };
        }
    }
}
