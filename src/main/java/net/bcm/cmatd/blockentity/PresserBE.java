package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class PresserBE extends BlockEntity{
    private final ItemStackHandler itemHandler;
    private int ticks;
    public int process_bits = 0;
    private final int INVENTORY_SIZE = 6;
    public int sameItem = 0;
    public int solar = 0;

    public PresserBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.PRESSER.get(),pos, blockState);
        this.itemHandler = new ItemStackHandler(INVENTORY_SIZE){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public ItemStackHandler getItemHandler(){
        return this.itemHandler;
    }

    public void dropAllItems(Level level, double x, double y, double z){
        for(int i = 0; i < INVENTORY_SIZE; i++){
            Containers.dropItemStack(level,x,y,z,getItemHandler().getStackInSlot(i));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE,ItemStack.EMPTY);
        items.set(0,itemHandler.getStackInSlot(0));
        items.set(1,itemHandler.getStackInSlot(1));
        items.set(2,itemHandler.getStackInSlot(2));
        items.set(3,itemHandler.getStackInSlot(3));
        items.set(4,itemHandler.getStackInSlot(4));
        items.set(5,itemHandler.getStackInSlot(5));
        ContainerHelper.saveAllItems(tag,items,registries);
        tag.putInt("ticks",ticks);
        tag.putInt("process_bits",process_bits);
        tag.putInt("same_item",sameItem);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        NonNullList<ItemStack> itemsLoad = NonNullList.withSize(INVENTORY_SIZE,ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag,itemsLoad,registries);
        itemHandler.setStackInSlot(0,itemsLoad.get(0)); // press pattern
        itemHandler.setStackInSlot(1,itemsLoad.get(1)); // to be pressed item
        itemHandler.setStackInSlot(2,itemsLoad.get(2)); // output pressed item
        // module slots
        itemHandler.setStackInSlot(3,itemsLoad.get(3));
        itemHandler.setStackInSlot(4,itemsLoad.get(4));
        itemHandler.setStackInSlot(5,itemsLoad.get(5));
        ticks = tag.getInt("ticks");
        process_bits = tag.getInt("process_bits");
        sameItem = tag.getInt("same_item");
    }

    public void serverTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
            setChanged();
        }

        if(level.isDay()){
            solar = 1;
            if(ticks % 5 == 0){
                ItemStack pressPatternItem = getItemHandler().getStackInSlot(0);
                ItemStack toBePressedItem = getItemHandler().getStackInSlot(1);

                ItemStack moduleSlot1 = getItemHandler().getStackInSlot(3);
                ItemStack moduleSlot2 = getItemHandler().getStackInSlot(4);
                ItemStack moduleSlot3 = getItemHandler().getStackInSlot(5);
                // presser process checks and use
                if(getItemHandler().getStackInSlot(2).getCount() < getItemHandler().getStackInSlot(2).getMaxStackSize()){
                    try{
                        Item printOutputItem = null;
                        Item printInputItem = null;
                        int printOutputCount = 0;

                        if(pressPatternItem.has(Components.PATTERN)){
                            printOutputItem = pressPatternItem.get(Components.PATTERN).producing_item().value();
                            printInputItem = pressPatternItem.get(Components.PATTERN).consumed_item().value();
                            printOutputCount = pressPatternItem.get(Components.PATTERN).item_amount_produced();
                        }
                        else{
                            process_bits = 0;
                            return;
                        }

                        if(getItemHandler().getStackInSlot(2).is(printOutputItem)){
                            sameItem = 1;
                        }
                        else{
                            sameItem = 0;
                        }

                        // check speed module
                        int speedModules = 0;
                        if(moduleSlot1.is(CmatdItem.SPEED_MODULE)){
                            speedModules++;
                        }
                        if(moduleSlot2.is(CmatdItem.SPEED_MODULE)){
                            speedModules++;
                        }
                        if(moduleSlot3.is(CmatdItem.SPEED_MODULE)){
                            speedModules++;
                        }

                        if(!getItemHandler().getStackInSlot(1).is(printInputItem)){
                            process_bits = 0;
                            return;
                        }

                        process_bits += (1 + speedModules);

                        if(process_bits >= 18){
                            if(getItemHandler().getStackInSlot(2).is(printOutputItem)){
                                //getItemHandler().getStackInSlot(0).shrink(1);
                                getItemHandler().getStackInSlot(1).shrink(1);
                                getItemHandler().getStackInSlot(2).grow(1);
                                process_bits = 0;
                                level.playSound(null,getBlockPos(),
                                        SoundEvents.HEAVY_CORE_HIT, SoundSource.BLOCKS,1.0f,1.0f);
                                setChanged();
                            }
                            else{
                                if(getItemHandler().getStackInSlot(2).isEmpty()){
                                    //getItemHandler().getStackInSlot(0).shrink(1);
                                    getItemHandler().getStackInSlot(1).shrink(1);
                                    getItemHandler().setStackInSlot(2,new ItemStack(printOutputItem,1));
                                    process_bits = 0;
                                    level.playSound(null,getBlockPos(),
                                            SoundEvents.HEAVY_CORE_HIT, SoundSource.BLOCKS,1.0f,1.0f);
                                    setChanged();
                                }
                                else{
                                    process_bits = 0;
                                    setChanged();
                                }
                            }
                        }
                    }
                    catch (Exception e){
                        Cmatd.getLogger().error("Presser encountered an issue: {}", e.getMessage());
                    }
                }
            }
        }
        else{
            solar = 0;
        }
    }
}
