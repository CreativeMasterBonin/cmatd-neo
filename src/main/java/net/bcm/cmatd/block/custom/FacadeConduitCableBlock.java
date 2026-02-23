package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.FacadeConduitBE;
import net.bcm.cmatd.item.FacadeConduitCableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
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

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(level.getBlockEntity(pos) instanceof FacadeConduitBE conduit){
            BlockState bs = conduit.getFacadeBlock();
            if(bs != null){
                return bs.getShape(level,pos,context);
            }
        }
        return super.getShape(state,level,pos,context);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.setBlock(pos,Blocks.COBBLESTONE.defaultBlockState(),3); // force remove and replace with cobblestone
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        ItemStack itemStack = new ItemStack(CmatdBlock.FACADE_CONDUIT.asItem());
        BlockState facadeBlock;
        if(blockEntity instanceof FacadeConduitBE){
            facadeBlock = ((FacadeConduitBE) blockEntity).getFacadeBlock();
        }
        else{
            facadeBlock = Blocks.COBBLESTONE.defaultBlockState();
        }
        FacadeConduitCableItem.setFacade(itemStack,facadeBlock);
        popResource(level,pos,itemStack);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockState defaultState = CmatdBlock.FACADE_CONDUIT.get().defaultBlockState();
        BlockState newState = FacadeConduitCableBlock.calculateState(level,pos,defaultState);
        return ((LevelAccessor) level).setBlock(pos, newState, ((LevelAccessor) level).isClientSide()
                ? Block.UPDATE_ALL + Block.UPDATE_IMMEDIATE
                : Block.UPDATE_ALL);
    }
}
