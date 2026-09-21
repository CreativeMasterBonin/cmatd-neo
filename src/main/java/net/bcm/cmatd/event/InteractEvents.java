package net.bcm.cmatd.event;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.item.CmatdItem;
import net.bcm.cmatd.item.RadioactiveAnimalSuitGear;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid=Cmatd.MODID)
public class InteractEvents {
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event){
        if(!event.isCanceled()){
            if(!event.getLevel().isClientSide()){
                if(event.getTarget() instanceof Wolf wolf){
                    if(!wolf.hasArmor() && wolf.isTame() && wolf.isOwnedBy(event.getEntity())){
                        if(event.getItemStack().is(CmatdItem.RADIOACTIVE_WOLF_SUIT)){
                            wolf.setBodyArmorItem(event.getItemStack().copyWithCount(1)); // copy the stack without modifying it
                            event.getItemStack().consume(1,event.getEntity()); // reduce the stack by n
                            event.setCancellationResult(InteractionResult.SUCCESS);
                            event.setCanceled(true);
                        }
                    }
                    else if(wolf.hasArmor() && wolf.isTame() && wolf.isOwnedBy(event.getEntity())){
                        if(wolf.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_WOLF_SUIT)){
                            if(event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SHEARS_REMOVE_ARMOR)){
                                event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).hurtAndBreak(
                                        1,event.getEntity(),
                                        LivingEntity.getSlotForHand(event.getEntity().getUsedItemHand()));
                                wolf.spawnAtLocation(wolf.getBodyArmorItem());
                                wolf.setBodyArmorItem(ItemStack.EMPTY);
                                event.getLevel().playSound(event.getEntity(),
                                        event.getEntity().position().x,event.getEntity().position().y,event.getEntity().position().z,
                                        SoundEvents.ARMOR_UNEQUIP_WOLF, SoundSource.PLAYERS,1.0f,1.0f);
                                event.setCancellationResult(InteractionResult.SUCCESS);
                                event.setCanceled(true);
                            }
                            else if(wolf.getBodyArmorItem().isDamaged() && wolf.isInSittingPose()){
                                if(event.getItemStack().getItem() instanceof RadioactiveAnimalSuitGear gear){
                                    boolean isGoodRepairItem = gear.getMaterial().value().repairIngredient().get().test(event.getItemStack());
                                    if(isGoodRepairItem){
                                        event.getItemStack().shrink(1);
                                        wolf.getBodyArmorItem().setDamageValue(wolf.getBodyArmorItem().getMaxDamage());
                                        event.getLevel().playSound(event.getEntity(),
                                                event.getEntity().position().x,event.getEntity().position().y,event.getEntity().position().z,
                                                SoundEvents.WOLF_ARMOR_REPAIR, SoundSource.PLAYERS,1.0f,1.0f);
                                        event.setCancellationResult(InteractionResult.SUCCESS);
                                        event.setCanceled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
