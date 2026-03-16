package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.CmatdSound;
import net.bcm.cmatd.datagen.Jammables;
import net.bcm.cmatd.gui.JamMakerMenu;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class JamMakerBE extends BlockEntity implements MenuProvider, WorldlyContainer {
    private final ItemStackHandler itemHandler;
    private int ticks;
    public int process_bits = 0;
    public int solar = 0;

    /*
    sugar slot 0
    jam jar slot 1
    jammable input slot 2
    jammable output slot 3
    */

    public JamMakerBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.JAM_MAKER.get(), pos, blockState);
        this.itemHandler = new ItemStackHandler(4){
            @Override
            public void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                Holder<Item> itemHolder = stack.getItemHolder();
                Jammables jammablesData = itemHolder.getData(Cmatd.JAMMABLES);
                switch (slot){
                    case 0 -> {
                        return stack.is(Items.SUGAR);
                    }
                    case 1 -> {
                        return stack.is(CmatdItem.JAM_JAR.asItem());
                    }
                    case 2 -> {
                        return jammablesData != null;
                    }
                    case 3 -> {
                        return false;
                    }
                    default -> {
                        return super.isItemValid(slot,stack);
                    }
                }
            }
        };
    }

    public ItemStackHandler getItemHandler(){
        return this.itemHandler;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Jam Maker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new JamMakerMenu(containerId,player,getBlockPos());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        NonNullList<ItemStack> items = NonNullList.withSize(4,ItemStack.EMPTY);
        items.set(0,itemHandler.getStackInSlot(0));
        items.set(1,itemHandler.getStackInSlot(1));
        items.set(2,itemHandler.getStackInSlot(2));
        items.set(3,itemHandler.getStackInSlot(3));
        ContainerHelper.saveAllItems(tag,items,registries);
        tag.putInt("ticks",ticks);
        tag.putInt("process_bits",process_bits);
        tag.putInt("solar",solar);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        NonNullList<ItemStack> itemsLoad = NonNullList.withSize(4,ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag,itemsLoad,registries);
        itemHandler.setStackInSlot(0,itemsLoad.get(0));
        itemHandler.setStackInSlot(1,itemsLoad.get(1));
        itemHandler.setStackInSlot(2,itemsLoad.get(2));
        itemHandler.setStackInSlot(3,itemsLoad.get(3));
        ticks = tag.getInt("ticks");
        process_bits = tag.getInt("process_bits");
        solar = tag.getInt("solar");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void serverTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
            setChanged();
        }

        if(level.isDay()){
            solar = 1;
            doStuff();
        }
        else{
            solar = 0;
        }
    }

    public void doStuff(){
        if(ticks % 10 == 0){
            ItemStack sugarSlotItem = getItemHandler().getStackInSlot(0);
            ItemStack jamJarSlotItem = getItemHandler().getStackInSlot(1);
            Holder<Item> itemHolder = getItemHandler().getStackInSlot(2).getItemHolder();

            if(itemHolder != null){
                Jammables jammablesData = itemHolder.getData(Cmatd.JAMMABLES);
                if(sugarSlotItem.is(Items.SUGAR) && jamJarSlotItem.is(CmatdItem.JAM_JAR) && jammablesData != null){
                    if(getItemHandler().getStackInSlot(3).getCount() < getItemHandler().getStackInSlot(3).getMaxStackSize()){
                        process_bits += 1;
                        if(process_bits >= 50){
                            if(getItemHandler().getStackInSlot(3).is(jammablesData.outputItem())){
                                getItemHandler().getStackInSlot(3).grow(1);
                                getItemHandler().getStackInSlot(0).shrink(1);
                                getItemHandler().getStackInSlot(1).shrink(1);
                                getItemHandler().getStackInSlot(2).shrink(1);
                                level.playSound(null,getBlockPos(),
                                        CmatdSound.MASHER.get(), SoundSource.BLOCKS, 1.0f,1.0f);
                                process_bits = 0;
                                setChanged();
                            }
                            else{
                                if(getItemHandler().getStackInSlot(3).isEmpty()){
                                    getItemHandler().setStackInSlot(3,new ItemStack(jammablesData.outputItem()));
                                    getItemHandler().getStackInSlot(0).shrink(1);
                                    getItemHandler().getStackInSlot(1).shrink(1);
                                    getItemHandler().getStackInSlot(2).shrink(1);
                                    level.playSound(null,getBlockPos(),
                                            CmatdSound.MASHER.get(), SoundSource.BLOCKS, 1.0f,1.0f);
                                    process_bits = 0;
                                    setChanged();
                                }
                                else{
                                    process_bits -= 1;
                                    setChanged();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{3};
        }
        else if(side == Direction.EAST){
            return new int[]{0};
        }
        else if(side == Direction.WEST){
            return new int[]{1};
        }
        else if(side == Direction.NORTH || side == Direction.SOUTH){
            return new int[]{2};
        }
        else{
            return new int[]{2};
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        if(index == 3){
            return false;
        }
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if(index == 3){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return getItemHandler().getStackInSlot(0).isEmpty() && getItemHandler().getStackInSlot(1).isEmpty()
                && getItemHandler().getStackInSlot(2).isEmpty() && getItemHandler().getStackInSlot(3).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return getItemHandler().getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack newItemStack = getItemHandler().getStackInSlot(slot);
        newItemStack.shrink(1);
        setChanged();
        return newItemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack newItemStack = getItemHandler().getStackInSlot(slot);
        newItemStack.shrink(1);
        return newItemStack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.getItemHandler().setStackInSlot(0,stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player){
        return Container.stillValidBlockEntity(this,player);
    }

    @Override
    public void clearContent() {
        this.getItemHandler().setStackInSlot(0,ItemStack.EMPTY);
        this.getItemHandler().setStackInSlot(1,ItemStack.EMPTY);
        this.getItemHandler().setStackInSlot(2,ItemStack.EMPTY);
        this.getItemHandler().setStackInSlot(3,ItemStack.EMPTY);
        setChanged();
    }
}
