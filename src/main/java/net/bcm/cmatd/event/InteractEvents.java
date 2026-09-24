package net.bcm.cmatd.event;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Fox;
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
                    if(!wolf.isBaby()){
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
                                    boolean isGoodRepairItem = event.getItemStack().is(CmatdItem.BLAST_PROOF_INGOT);
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
                } // tamable animal entities don't have armor, but do have a body slot like wolves (which are derivatives of tamable animals)
                else if(event.getTarget() instanceof TamableAnimal tamableAnimal){
                    if(!tamableAnimal.isBaby()){
                        if(tamableAnimal.getBodyArmorItem().isEmpty() && tamableAnimal.isTame() && tamableAnimal.isOwnedBy(event.getEntity())){
                            if(event.getItemStack().is(CmatdItem.RADIOACTIVE_PROTECTION_BARRIER)){
                                tamableAnimal.setBodyArmorItem(event.getItemStack().copyWithCount(1)); // copy the stack without modifying it
                                event.getItemStack().consume(1,event.getEntity()); // reduce the stack by n
                                event.setCancellationResult(InteractionResult.SUCCESS);
                                event.setCanceled(true);
                            }
                        }
                        else if(tamableAnimal.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_PROTECTION_BARRIER) && tamableAnimal.isTame() && tamableAnimal.isOwnedBy(event.getEntity())){
                            if(tamableAnimal.getBodyArmorItem().is(CmatdItem.RADIOACTIVE_PROTECTION_BARRIER)){
                                if(event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SHEARS_REMOVE_ARMOR)){
                                    event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).hurtAndBreak(
                                            1,event.getEntity(),
                                            LivingEntity.getSlotForHand(event.getEntity().getUsedItemHand()));
                                    tamableAnimal.spawnAtLocation(tamableAnimal.getBodyArmorItem());
                                    tamableAnimal.setBodyArmorItem(ItemStack.EMPTY);
                                    event.getLevel().playSound(event.getEntity(),
                                            event.getEntity().position().x,event.getEntity().position().y,event.getEntity().position().z,
                                            SoundEvents.ARMOR_UNEQUIP_WOLF, SoundSource.PLAYERS,1.0f,1.0f);
                                    event.setCancellationResult(InteractionResult.SUCCESS);
                                    event.setCanceled(true);
                                }
                                else if(tamableAnimal.getBodyArmorItem().isDamaged() && tamableAnimal.isInSittingPose()){
                                    if(event.getItemStack().is(CmatdItem.BLAST_PROOF_INGOT)){
                                        event.getItemStack().shrink(1);
                                        tamableAnimal.getBodyArmorItem().setDamageValue(tamableAnimal.getBodyArmorItem().getMaxDamage());
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
