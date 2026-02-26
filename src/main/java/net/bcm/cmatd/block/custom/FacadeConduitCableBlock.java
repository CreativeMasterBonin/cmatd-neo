package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.CmatdBE;
import net.bcm.cmatd.blockentity.FacadeConduitBE;
import net.bcm.cmatd.item.FacadeConduitCableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Logger;

public class FacadeConduitCableBlock extends ConduitCableBlock implements EntityBlock {
    public FacadeConduitCableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FacadeConduitBE(pos,state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of();
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
        ItemStack itemStack = new ItemStack(CmatdBlock.FACADE_CONDUIT.asItem());
        BlockState facadeBlock;
        BlockEntity be = level.getBlockEntity(pos);
        CompoundTag tag = new CompoundTag();

        if(be instanceof FacadeConduitBE){
            facadeBlock = ((FacadeConduitBE) be).getFacadeBlock();
            tag = be.saveCustomAndMetadata(level.registryAccess());
            be.removeComponentsFromTag(tag);
            FacadeConduitCableItem.setBlockEntityData(stack,CmatdBE.FACADE_CONDUIT.get(),tag);
            stack.applyComponents(be.collectComponents());
        }

        level.addFreshEntity(new ItemEntity(level,pos.getX(),pos.getY(),pos.getZ(),itemStack));
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
                BlockState itemBlockState = item.getBlock().defaultBlockState();
                if(item.getBlock() instanceof EntityBlock || item.getBlock() instanceof GameMasterBlock || item.getBlock() instanceof LiquidBlock || item.getBlock() instanceof AirBlock || item.getBlock() instanceof StructureVoidBlock || item.getBlock() instanceof BarrierBlock){
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
                try{
                    if(item.getBlock().getStateDefinition().getProperty("facing") != null){
                        if(item.getBlock() instanceof StairBlock || item.getBlock() instanceof HorizontalDirectionalBlock || item.getBlock() instanceof DoorBlock || item.getBlock() instanceof TripWireHookBlock || item.getBlock() instanceof LadderBlock){
                            itemBlockState = itemBlockState.setValue(BlockStateProperties.HORIZONTAL_FACING,player.getDirection());
                        }
                        else{
                            itemBlockState = itemBlockState.setValue(BlockStateProperties.FACING,player.getDirection());
                        }
                    }
                    if(item.getBlock().getStateDefinition().getProperty("shape") != null){

                    }
                    if(item.getBlock().getStateDefinition().getProperty("type") != null){
                        if(item.getBlock() instanceof SlabBlock){
                            if(player.isSecondaryUseActive()){
                                itemBlockState = itemBlockState.setValue(BlockStateProperties.SLAB_TYPE,SlabType.DOUBLE);
                            }
                            else{
                                itemBlockState = itemBlockState.setValue(BlockStateProperties.SLAB_TYPE,player.getBlockY() - 1 > pos.getY() ? SlabType.BOTTOM : SlabType.TOP);
                            }
                        }
                    }
                    if(item.getBlock().getStateDefinition().getProperty("half") != null){
                        if(item.getBlock() instanceof StairBlock){
                            itemBlockState = itemBlockState.setValue(BlockStateProperties.HALF,player.getBlockY() - 1 > pos.getY() ? Half.BOTTOM : Half.TOP);
                        }
                        else if(item.getBlock() instanceof DoorBlock){
                            itemBlockState = itemBlockState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF,player.getBlockY() - 1 > pos.getY() ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER);
                        }
                    }
                    if(item.getBlock().getStateDefinition().getProperty("vertical_direction") != null){
                        itemBlockState = itemBlockState.setValue(BlockStateProperties.VERTICAL_DIRECTION,player.getBlockY() - 1 > pos.getY() ? Direction.DOWN : Direction.UP);
                    }
                }
                catch (Exception e){
                    Logger.getAnonymousLogger().info("Facade Conduit BlockState cannot be set properly: " + e.getLocalizedMessage());
                }
                facadeConduitBE.setFacadeState(itemBlockState);
                player.swing(hand);
                return ItemInteractionResult.CONSUME;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
