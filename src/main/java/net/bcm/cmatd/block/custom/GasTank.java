package net.bcm.cmatd.block.custom;

import com.mojang.serialization.MapCodec;
import net.bcm.cmatd.CmatdClient;
import net.bcm.cmatd.CmatdClientActionHandler;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.blockentity.GasTankBE;
import net.bcm.cmatd.blockentity.WindGeneratorBE;
import net.bcm.cmatd.gui.GasTankMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GasTank extends BaseEntityBlock {
    public static final MapCodec<GasTank> CODEC =
            simpleCodec(GasTank::new);

    public GasTank(Properties properties) {
        super(properties);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(state.getBlock())){
            return;
        }
        super.onRemove(state,level,pos,newState,movedByPiston);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.gas_tank.additional_info")
                .withStyle(ChatFormatting.GOLD));
        if(CmatdClientActionHandler.keyMappingPressed(CmatdClient.itemDescriptionKeyMapping)){
            tooltipComponents.add(Component.translatable("desc.item.gas_tank.radioactive_gas_warning")
                    .withColor(Utility.BAD_WARNING_YELLOW));
        }
        else{
            tooltipComponents.add(Component.translatable("desc.item.generator.hidden_details",
                            Component.translatable(CmatdClient.itemDescriptionKeyMapping.getKey().getName()))
                    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GasTankBE(pos,state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide){
            BlockEntity be = level.getBlockEntity(pos);
            if(be instanceof GasTankBE){
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("title.gas_tank")
                                .withStyle(ChatFormatting.WHITE);
                    }
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                        return new GasTankMenu(windowId,player,pos);
                    }
                };
                player.openMenu(containerProvider, buf -> buf.writeBlockPos(pos));
                player.swing(player.getUsedItemHand());
                return InteractionResult.CONSUME;
            }
            else{
                throw new IllegalStateException("Container provider missing for GasTankBE at " + pos.toShortString());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Utility.GAS_TANK_ALL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType){
        if(level.isClientSide){
            return (lvl,pos,st,bet) -> {
                if(bet instanceof GasTankBE gen){
                    gen.clientTick();
                }
            };
        }
        else{
            return (lvl,pos,st,bet) -> {
                if(bet instanceof GasTankBE gen){
                    gen.serverTick();
                }
            };
        }
    }
}
