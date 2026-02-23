package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.JamMakerBE;
import net.bcm.cmatd.datagen.Tag;
import net.bcm.cmatd.gui.FoodReactorMenu;
import net.bcm.cmatd.gui.JamMakerMenu;
import net.bcm.cmatd.network.FoodReactorWrenchUpdate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import net.bcm.cmatd.blockentity.FoodReactorMultiblock;

public class FoodReactorBlock extends BaseEntityBlock{
    public static final MapCodec<FoodReactorBlock> CODEC =
            simpleCodec(FoodReactorBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public FoodReactorBlock(Properties properties) {
        super(properties.strength(2f,10f)
                .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK)
                .requiresCorrectToolForDrops().pushReaction(PushReaction.BLOCK));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FoodReactorMultiblock) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("title.food_reactor");
                    }
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                        return new FoodReactorMenu(windowId, playerEntity, pos);
                    }
                };
                player.openMenu(containerProvider, buf -> buf.writeBlockPos(pos));
                player.swing(player.getUsedItemHand());
                return InteractionResult.CONSUME;
            }
            else {
                throw new IllegalStateException("Container provider missing for FoodReactorMultiblock at: " + pos.toShortString() + "!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide){
            if(stack.isEmpty()){
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            //
            if(stack.is(Tags.Items.TOOLS_WRENCH)){
                try{
                    PacketDistributor.sendToServer(new FoodReactorWrenchUpdate(pos));
                    return ItemInteractionResult.SUCCESS;
                }
                catch (Exception e){
                    Cmatd.getLogger().error("Food Reactor error at: {}! Error: {}", pos.toShortString(), e.getMessage());
                    return ItemInteractionResult.FAIL;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (newState.is(state.getBlock())){
            return;
        }
        FoodReactorMultiblock be = (FoodReactorMultiblock)level.getBlockEntity(pos);
        if(be instanceof FoodReactorMultiblock){
            Containers.dropItemStack(level,pos.getX(),pos.getY(),pos.getZ(),
                    be.getItemStackHandler().getStackInSlot(0));
            Containers.dropItemStack(level,pos.getX(),pos.getY(),pos.getZ(),
                    be.getItemStackHandler().getStackInSlot(1));
            Containers.dropItemStack(level,pos.getX(),pos.getY(),pos.getZ(),
                    be.getItemStackHandler().getStackInSlot(2));
            Containers.dropItemStack(level,pos.getX(),pos.getY(),pos.getZ(),
                    be.getItemStackHandler().getStackInSlot(3));
            Containers.dropItemStack(level,pos.getX(),pos.getY(),pos.getZ(),
                    be.getItemStackHandler().getStackInSlot(4));
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
        return new FoodReactorMultiblock(pos,state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING,context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return null;
        }
        else{
            return (lvl,pos,st,bet) -> {
                if(bet instanceof FoodReactorMultiblock gen){
                    gen.serverTick();
                }
            };
        }
    }
}
