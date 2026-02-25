package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.FacadeConduitBE;
import net.bcm.cmatd.item.FacadeConduitCableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FacadeConduitCableBlock extends ConduitCableBlock implements EntityBlock {
    public FacadeConduitCableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FacadeConduitBE(pos,state);
    }

    /*@Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.setBlock(pos,Blocks.COBBLESTONE.defaultBlockState(),3); // force remove and replace with cobblestone
    }*/

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
        ItemStack itemStack = new ItemStack(CmatdBlock.FACADE_CONDUIT.asItem());
        BlockState facadeBlock;
        BlockEntity be = level.getBlockEntity(pos);

        if(be instanceof FacadeConduitBE){
            facadeBlock = ((FacadeConduitBE) be).getFacadeBlock();
        }
        else{
            facadeBlock = Blocks.COBBLESTONE.defaultBlockState();
        }
        FacadeConduitCableItem.setFacade(itemStack,facadeBlock);
        popResource(level,pos,itemStack);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide){
            return InteractionResult.SUCCESS;
        }
        else{
            if(player.isShiftKeyDown() && player.getItemInHand(player.getUsedItemHand()) == ItemStack.EMPTY){
                if(level.getBlockEntity(pos) instanceof FacadeConduitBE facadeConduitBE){
                    facadeConduitBE.setFacadeState(Blocks.AIR.defaultBlockState());
                }
                player.swing(player.getUsedItemHand());
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide){
            return ItemInteractionResult.SUCCESS;
        }
        else{
            if(level.getBlockEntity(pos) instanceof FacadeConduitBE facadeConduitBE && stack.getItem() instanceof BlockItem item){
                if(item.getBlock() instanceof EntityBlock || item.getBlock() instanceof GameMasterBlock){
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
                facadeConduitBE.setFacadeState(((BlockItem) stack.getItem()).getBlock().defaultBlockState());
                player.swing(hand);
                return ItemInteractionResult.CONSUME;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
