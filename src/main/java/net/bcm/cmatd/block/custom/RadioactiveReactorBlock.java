package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.RadioactiveReactor;
import net.bcm.cmatd.gui.RadioactiveReactorMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

import java.util.stream.Stream;

public class RadioactiveReactorBlock extends TieredMachineBlock{
    public static final VoxelShape ALL = Stream.of(
            Block.box(12, 16, 0, 16, 20, 4),
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(12, 16, 4, 16, 20, 8),
            Block.box(12, 16, 12, 16, 20, 16),
            Block.box(8, 16, 12, 12, 20, 16),
            Block.box(8, 16, 4, 12, 20, 8),
            Block.box(8, 16, 0, 12, 20, 4),
            Block.box(4, 16, 12, 8, 20, 16),
            Block.box(4, 16, 4, 8, 20, 8),
            Block.box(4, 16, 0, 8, 20, 4),
            Block.box(0, 16, 12, 4, 20, 16),
            Block.box(0, 16, 4, 4, 20, 8),
            Block.box(0, 16, 0, 4, 20, 4),
            Block.box(0, 16, 8, 4, 20, 12),
            Block.box(4, 16, 8, 8, 20, 12),
            Block.box(8, 16, 8, 12, 20, 12),
            Block.box(12, 16, 8, 16, 20, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(state.getBlock())){
            return;
        }
        else{
            if(level.getBlockEntity(pos) instanceof RadioactiveReactor reactor){
                // drop all items currently in the inventory
                NonNullList<ItemStack> listOfItemsToDrop = NonNullList.withSize(30,ItemStack.EMPTY);
                for(int index = 0; index < 30; index++){
                    if(index >= 30){
                        break;
                    }
                    else{
                        listOfItemsToDrop.set(index,reactor.itemStackHandler.getStackInSlot(index));
                    }
                }
                Containers.dropContents(level,pos,listOfItemsToDrop);
                // now check if waste was left behind when breaking, and heat too
                if(reactor.getWasteGasTank().gas.getGas().isRadioactive()){
                    level.setBlock(pos, CmatdBlock.RADIOACTIVE_WASTE.get().defaultBlockState(),3);
                }
                else if(reactor.heatAmount > 3000){
                    level.setBlock(pos, Blocks.FIRE.defaultBlockState(),3);
                }
            }
        }
        super.onRemove(state,level,pos,newState,movedByPiston);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ALL;
    }

    public RadioactiveReactorBlock(Properties properties) {
        super(properties.strength(1f,50f)
                .sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops()
                .mapColor(MapColor.TERRACOTTA_GRAY).pushReaction(PushReaction.BLOCK)
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
