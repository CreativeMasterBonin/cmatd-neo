package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.blockentity.BaseCobbleMakerBE;
import net.bcm.cmatd.blockentity.BaseEnergyMakerBE;
import net.bcm.cmatd.blockentity.JamMakerBE;
import net.bcm.cmatd.gui.BaseCobbleMakerMenu;
import net.bcm.cmatd.gui.JamMakerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JamMaker extends BaseEntityBlock {
    public static final MapCodec<JamMaker> CODEC =
            simpleCodec(JamMaker::new);
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public JamMaker(Properties properties) {
        super(properties.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.jam_maker.additional_info")
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston){
        if(newState.is(state.getBlock())){
            return;
        }
        JamMakerBE be = (JamMakerBE)level.getBlockEntity(pos);
        if(be instanceof JamMakerBE){
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                    be.getItemHandler().getStackInSlot(0));
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                    be.getItemHandler().getStackInSlot(1));
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                    be.getItemHandler().getStackInSlot(2));
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                    be.getItemHandler().getStackInSlot(3));
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new JamMakerBE(pos,state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof JamMakerBE) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("title.jam_maker");
                    }
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                        return new JamMakerMenu(windowId, playerEntity, pos);
                    }
                };
                player.openMenu(containerProvider, buf -> buf.writeBlockPos(pos));
            }
            else {
                throw new IllegalStateException("Container provider missing for JamMaker at: " + pos.toShortString() + "!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return null;
        }
        else{
            return ((level1, pos, state1, blockEntity) -> {
                if(blockEntity instanceof JamMakerBE){
                    ((JamMakerBE) blockEntity).serverTick();
                }
            });
        }
    }
}
