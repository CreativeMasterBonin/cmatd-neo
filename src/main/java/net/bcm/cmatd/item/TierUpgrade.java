package net.bcm.cmatd.item;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.blockentity.BaseCobbleMakerBE;
import net.bcm.cmatd.network.BaseCobbleMakerTierUpdatePayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class TierUpgrade extends Item{
    public TierUpgrade(Properties properties) {
        super(properties.fireResistant().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag){
        if(stack.has(Components.MACHINE_TIER)){
            switch(stack.get(Components.MACHINE_TIER).getMachineTier()){
                case 0 -> {
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.downgrade.desc"));
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.basic"));
                }
                case 1 -> {
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.desc"));
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.advanced"));
                }
                case 2 -> {
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.desc"));
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.highly_advanced"));
                }
                case 3 -> {
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.desc"));
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.superb"));
                }
                case 4 -> {
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.desc"));
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.maximum"));
                }
                default -> {
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.desc"));
                    tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.unknown",
                            stack.get(Components.MACHINE_TIER).getMachineTier()));
                }
            }
        }
    }

    /*
    .component(Components.MACHINE_TIER,new MachineTierComponent(
                        0,
                        10000,
                        1000,
                        1000,
                        0,
                        50)).fireResistant()
     */

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide){
            return InteractionResult.SUCCESS;
        }
        else{
            BlockState bs = context.getLevel().getBlockState(context.getClickedPos());
            BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
            BlockPos clickedPos = context.getClickedPos();
            ItemStack itemInHand = context.getItemInHand();
            if(!itemInHand.has(Components.MACHINE_TIER)){
                return InteractionResult.PASS;
            }
            //
            if(be != null){
                if(be instanceof BaseCobbleMakerBE){
                    if(((BaseCobbleMakerBE) be).getTierSettings(0) == itemInHand.get(Components.MACHINE_TIER).getMachineTier()){
                        context.getPlayer().displayClientMessage(
                                Component.translatable("tooltip.tier_upgrade.already_same"),
                                true);
                        return InteractionResult.CONSUME;
                    }
                    // if not same tier, continue to upgrade (server only)
                    if(!context.getLevel().isClientSide){
                        try{
                            PacketDistributor.sendToServer(new BaseCobbleMakerTierUpdatePayload(
                                    clickedPos,
                                    itemInHand.get(Components.MACHINE_TIER).getMachineTier(),
                                    itemInHand.get(Components.MACHINE_TIER).getMaxEnergy(),
                                    itemInHand.get(Components.MACHINE_TIER).getMaxReceive(),
                                    itemInHand.get(Components.MACHINE_TIER).getMaxExtract(),
                                    itemInHand.get(Components.MACHINE_TIER).getModulesAllowed(),
                                    itemInHand.get(Components.MACHINE_TIER).getEnergyGenRate()
                            ));
                        }
                        catch (Exception e){
                            Cmatd.getLogger().error("Tier Upgrade item had error at {}! Error: {}",clickedPos,e.getMessage());
                            return InteractionResult.FAIL;
                        }
                    }
                    /*
                    ((BaseCobbleMakerBE) be).setNewTierSettings(
                            itemInHand.get(Components.MACHINE_TIER).getMachineTier(),
                            itemInHand.get(Components.MACHINE_TIER).getMaxEnergy(),
                            itemInHand.get(Components.MACHINE_TIER).getMaxReceive(),
                            itemInHand.get(Components.MACHINE_TIER).getMaxExtract(),
                            itemInHand.get(Components.MACHINE_TIER).getModulesAllowed(),
                            itemInHand.get(Components.MACHINE_TIER).getEnergyGenRate()
                    );*/
                    context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                    itemInHand.shrink(1);
                    context.getPlayer().swing(context.getHand());
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
