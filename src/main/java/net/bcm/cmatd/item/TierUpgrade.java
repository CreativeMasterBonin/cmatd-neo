package net.bcm.cmatd.item;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.blockentity.*;
import net.bcm.cmatd.network.BaseCobbleMakerTierUpdatePayload;
import net.bcm.cmatd.network.MachineTierUpgradePayload;
import net.bcm.cmatd.network.UpdateNightModePayload;
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
    public final boolean isExcessUpgrade; // whether the upgrade is not a standard upgrade from one to the next
    public final int tierUpgradeLevel; // what level of tier upgrades this item is in (higher tiers unlock new functionality in machines)

    public TierUpgrade(Properties properties) {
        super(properties.fireResistant().stacksTo(1));
        isExcessUpgrade = false;
        tierUpgradeLevel = 0;
    }

    public TierUpgrade(Properties properties, boolean isExcessUpgrade) {
        super(properties.fireResistant().stacksTo(1));
        this.isExcessUpgrade = isExcessUpgrade;
        tierUpgradeLevel = 0;
    }

    public TierUpgrade(Properties properties,int tierUpgradeLevel) {
        super(properties.fireResistant().stacksTo(1));
        this.isExcessUpgrade = false;
        this.tierUpgradeLevel = tierUpgradeLevel;
    }

    public TierUpgrade(Properties properties, boolean isExcessUpgrade,int tierUpgradeLevel) {
        super(properties.fireResistant().stacksTo(1));
        this.isExcessUpgrade = isExcessUpgrade;
        this.tierUpgradeLevel = tierUpgradeLevel;
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
                    if(isExcessUpgrade){
                        tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.excess"));
                    }
                    else{
                        tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.desc"));
                        tooltipComponents.add(Component.translatable("tooltip.tier_upgrade.unknown",
                                stack.get(Components.MACHINE_TIER).getMachineTier()));
                    }
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
        if(!context.getLevel().isClientSide){
            return InteractionResult.SUCCESS;
        }
        else{
            BlockState bs = context.getLevel().getBlockState(context.getClickedPos());
            BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
            BlockPos clickedPos = context.getClickedPos();
            ItemStack itemInHand = context.getItemInHand();
            // item in hand must have the machine tier component to do anything
            if(!itemInHand.has(Components.MACHINE_TIER)){
                return InteractionResult.PASS;
            }
            // get tier
            int tier = itemInHand.get(Components.MACHINE_TIER).getMachineTier();
            // check blockentity type
            if(be != null){
                if(be instanceof BaseCobbleMakerBE){
                    if(((BaseCobbleMakerBE) be).getTierSettings(0) == itemInHand.get(Components.MACHINE_TIER).getMachineTier()){
                        context.getPlayer().displayClientMessage(
                                Component.translatable("tooltip.tier_upgrade.already_same"),
                                true);
                        return InteractionResult.CONSUME;
                    }
                    // if not same tier, continue to upgrade (server only)
                    if(context.getLevel().isClientSide()){
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
                    context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                    itemInHand.shrink(1);
                    context.getPlayer().swing(context.getHand());
                    return InteractionResult.CONSUME;
                }
                else if(be instanceof JamMakerBE jamMakerBE){
                    if(tier == 1 && (!jamMakerBE.nightUpgrade || jamMakerBE.machineTier < 1)){
                        if(context.getLevel().isClientSide()){
                            context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                            PacketDistributor.sendToServer(new UpdateNightModePayload(clickedPos,true));
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                    else{
                        if(context.getLevel().isClientSide()) {
                            context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                            PacketDistributor.sendToServer(new MachineTierUpgradePayload(clickedPos, tier));
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                else if(be instanceof PresserBE presserBE){
                    if(tier == 1 && (!presserBE.nightUpgrade || presserBE.machineTier < 1)){
                        if(context.getLevel().isClientSide()){
                            context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                            PacketDistributor.sendToServer(new UpdateNightModePayload(clickedPos,true));
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                    else{
                        if(context.getLevel().isClientSide()) {
                            context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                            PacketDistributor.sendToServer(new MachineTierUpgradePayload(clickedPos, tier));
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                else if(be instanceof BaseEnergyMakerBE energyMakerBE){
                    if(energyMakerBE.machine_tier < tier){
                        if(context.getLevel().isClientSide()){
                            context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                            PacketDistributor.sendToServer(new MachineTierUpgradePayload(clickedPos,tier));
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                else if(be instanceof FoodReactorMultiblock foodReactor){
                    if(foodReactor.machine_tier < tier){
                        if(context.getLevel().isClientSide()){
                            context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                            PacketDistributor.sendToServer(new MachineTierUpgradePayload(clickedPos,tier));
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                // mass upgrade tiered machines
                else if(be instanceof TieredMachine tieredMachine){
                    if(tieredMachine.machineTier < tier){
                        if(context.getLevel().isClientSide()){
                            context.getPlayer().playSound(SoundEvents.SMITHING_TABLE_USE,0.75f,1.0f);
                            PacketDistributor.sendToServer(new MachineTierUpgradePayload(clickedPos,tier));
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}
