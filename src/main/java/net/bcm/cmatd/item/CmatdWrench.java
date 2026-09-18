package net.bcm.cmatd.item;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.blockentity.RadioactiveReactor;
import net.bcm.cmatd.network.ReactorWrenchUpdate;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class CmatdWrench extends Item {
    public CmatdWrench(Properties properties) {
        super(properties.stacksTo(1)
                .rarity(Rarity.UNCOMMON)
                .fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.item.cmatd_wrench")
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        InteractionHand hand = context.getHand();
        BlockPos pos = context.getClickedPos();

        // you cannot send server-bound packets on the server, they must be sent by the client
        if(level.isClientSide()){
            if(level.getBlockState(pos).is(CmatdBlock.RADIOACTIVE_REACTOR_MULTIBLOCK)){
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof RadioactiveReactor){
                    try{
                        PacketDistributor.sendToServer(new ReactorWrenchUpdate(pos));
                        return InteractionResult.CONSUME;
                    }
                    catch (Exception e){
                        Cmatd.getLogger().error("CMATD Wrench failed trying to send server-bound ReactorWrenchUpdate! Error: {}", e.getMessage());
                        return InteractionResult.FAIL;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}
