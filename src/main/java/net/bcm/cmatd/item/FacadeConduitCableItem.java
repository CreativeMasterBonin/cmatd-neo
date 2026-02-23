package net.bcm.cmatd.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
    }

    private static String getFacadeString(ItemStack stack) {
        return "unset";
    }

    private static void userSetMimicBlock(ItemStack item, BlockState mimicBlock, UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        //setFacadeBlock(item, mimicBlock);
        if (world.isClientSide) {
            player.displayClientMessage(Component.translatable("", mimicBlock.getBlock().getDescriptionId()), false);
        }
    }

    public static void setFacade(ItemStack item, BlockState facade) {
        CompoundTag tagCompound = new CompoundTag();
        CompoundTag nbt = NbtUtils.writeBlockState(facade);
        tagCompound.put("facade", nbt);

    }
}
