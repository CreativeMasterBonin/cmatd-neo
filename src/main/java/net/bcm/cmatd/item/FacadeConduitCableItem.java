package net.bcm.cmatd.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class FacadeConduitCableItem extends BlockItem {
    public FacadeConduitCableItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("Duplicates power and doesn't work at all when placed.").withStyle(ChatFormatting.RED));
        if(stack.has(DataComponents.BLOCK_ENTITY_DATA)){
            if(stack.getComponents().get(DataComponents.BLOCK_ENTITY_DATA).contains("facade")){

                stack.getComponents().get(DataComponents.BLOCK_ENTITY_DATA).copyTag().get("facade");
            }
        }
    }

    public static String getFacadeString(ItemStack stack){
        return stack.getComponents().get(DataComponents.BLOCK_ENTITY_DATA).copyTag().get("facade").getAsString();
    }

    public static void userSetMimicBlock(ItemStack item, BlockState mimicBlock, UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        if (world.isClientSide) {
            player.displayClientMessage(Component.translatable("block.cmatd.facade_conduit.set_facade", mimicBlock.getBlock().getDescriptionId()), false);
        }
    }

    public static void setFacade(ItemStack item, BlockState facade) {
        CompoundTag tagCompound = new CompoundTag();
        CompoundTag nbt = NbtUtils.writeBlockState(facade);
        tagCompound.put("facade", nbt);
        item.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tagCompound));
    }
}
