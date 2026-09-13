package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.blockentity.DimensionalTransporterBE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DimensionalTransporter extends BaseEntityBlock {
    public static final MapCodec<DimensionalTransporter> CODEC = simpleCodec(DimensionalTransporter::new);
    public DimensionalTransporter(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("cmatd.dimensional_transporter.unfinished_warning_desc")
                .withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(state.getBlock())){
            return;
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if(level instanceof ServerLevel serverLevel){
            DimensionalTransporterBE transporter = (DimensionalTransporterBE)serverLevel.getBlockEntity(pos);
            if(transporter instanceof DimensionalTransporterBE){
                transporter.levelWeAreIn = serverLevel.dimension().toString();
                transporter.updateBlock();
            }
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide){
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        else{
            // shift click tot teleport
            if(player.isSecondaryUseActive()){
                if(level instanceof ServerLevel serverLevel){
                    if(player instanceof ServerPlayer serverPlayer){
                        DimensionalTransporterBE transporter = (DimensionalTransporterBE)serverLevel.getLevel().getBlockEntity(pos);
                        if(transporter instanceof DimensionalTransporterBE){
                            transporter.teleportPlayer(serverLevel.getServer(),serverPlayer);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
            else{
                if(level instanceof ServerLevel serverLevel){
                    DimensionalTransporterBE transporter = (DimensionalTransporterBE)serverLevel.getLevel().getBlockEntity(pos);
                    if(transporter instanceof DimensionalTransporterBE){
                        // already visited once dimensions
                        player.displayClientMessage(
                                Component.literal(
                                        "List of dimensions that have been visited once with this transporter: "
                                        + transporter.teleportedToDimensionsAtLeastOnce.toArray().toString() + "\n\n"),
                                false);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DimensionalTransporterBE(pos,state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
