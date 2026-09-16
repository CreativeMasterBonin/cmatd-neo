package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.blockentity.RadioactiveReactor;
import net.bcm.cmatd.gui.RadioactiveReactorMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RadioactiveReactorBlock extends TieredMachineBlock{
    public static final VoxelShape ALL = Shapes.join(Block.box(4, 12, 4, 12, 20, 12),
            Block.box(0, 0, 0, 16, 16, 16), BooleanOp.OR);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ALL;
    }

    public RadioactiveReactorBlock(Properties properties) {
        super(properties.strength(1f,50f)
                .sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops()
                .mapColor(MapColor.COLOR_GRAY).pushReaction(PushReaction.BLOCK)
                .instrument(NoteBlockInstrument.BANJO));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide()){
            BlockEntity be = level.getBlockEntity(pos);
            if(be instanceof RadioactiveReactor)
                return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        else{
            BlockEntity be = level.getBlockEntity(pos);
            if(be instanceof RadioactiveReactor){
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("title.radioactive_reactor")
                                .withStyle(ChatFormatting.WHITE);
                    }
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                        return new RadioactiveReactorMenu(windowId,player,pos);
                    }
                };
                player.openMenu(containerProvider, buf -> buf.writeBlockPos(pos));
                return InteractionResult.SUCCESS_NO_ITEM_USED;
            }
            else{
                throw new IllegalStateException("Container provider missing for RadioactiveReactor at " + pos.toShortString());
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RadioactiveReactor(blockPos,blockState);
    }
}
