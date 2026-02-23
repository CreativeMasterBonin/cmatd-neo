package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.CmatdSound;
import net.bcm.cmatd.ServerUtilities;
import net.bcm.cmatd.datagen.Mashables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class Masher extends Block {
    public Masher(Properties properties) {
        super(properties.sound(SoundType.NETHERITE_BLOCK).noOcclusion().mapColor(MapColor.COLOR_BLACK));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide){
            return ItemInteractionResult.SUCCESS;
        }
        else{
            double yPos = pos.getY() + 1.0D; // item entity pop location for y
            double ySpeed = 0.35D;
            RandomSource rand = level.getRandom();
            double randX = rand.nextDouble() * 0.15D;
            double randZ = rand.nextDouble() * 0.15D;

            try{
                Holder<Item> itemHolder = stack.getItemHolder();
                Mashables mashables = itemHolder.getData(Cmatd.MASHABLES);
                if(mashables != null){
                    player.getItemInHand(hand).shrink(1);

                    ItemEntity resultItem = new ItemEntity(level,
                            pos.getX() + 0.5,yPos,pos.getZ() + 0.5,
                            new ItemStack(mashables.outputItem()),
                            randX,ySpeed,randZ);

                    level.addFreshEntity(resultItem);
                    level.playSound(player,pos, CmatdSound.MASHER_METALLIC.get(), SoundSource.BLOCKS, 1.0f,1.0f);

                    if(level instanceof ServerLevel){
                        ((ServerLevel) level).sendParticles(ParticleTypes.DUST_PLUME,
                                pos.getX() + 0.5,pos.getY() + 1.0, pos.getZ() + 0.5,
                                4,0,0,0,0);
                    }

                    player.swing(hand);
                    return ItemInteractionResult.CONSUME;
                }
                else {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
            }
            catch (Exception e){
                Cmatd.getLogger().error("Masher at {} cannot do operation as exception was thrown: {}",pos.toShortString(),e.getMessage());
            }

            /*
            if(player.getItemInHand(hand).is(Items.POTATO)){
                player.getItemInHand(hand).shrink(1);

                ItemEntity resultItem = new ItemEntity(level,
                        pos.getX(),yPos,pos.getZ(),
                        new ItemStack(CmatdItem.MASHED_POTATOES.asItem()),
                        0,ySpeed,0);

                level.addFreshEntity(resultItem);
                level.playSound(player,pos, CmatdSound.MASHER.get(), SoundSource.BLOCKS, 1.0f,1.0f);
            }
            else if(player.getItemInHand(hand).is(Items.POISONOUS_POTATO)){
                player.getItemInHand(hand).shrink(1);

                ItemEntity resultItem = new ItemEntity(level,
                        pos.getX(),yPos,pos.getZ(),
                        new ItemStack(CmatdItem.POISONOUS_MASHED_POTATOES.asItem()),
                        0,ySpeed,0);

                level.addFreshEntity(resultItem);
                level.playSound(player,pos, CmatdSound.MASHER.get(), SoundSource.BLOCKS, 1.0f,1.0f);
            }

             */
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
