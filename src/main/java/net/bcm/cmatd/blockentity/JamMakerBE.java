package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.*;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.datagen.Jammables;
import net.bcm.cmatd.datagen.Tag;
import net.bcm.cmatd.gui.JamMakerMenu;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.*;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import org.joml.Vector3f;

public class JamMakerBE extends BlockEntity implements MenuProvider, WorldlyContainer {
    private final ItemStackHandler itemHandler;
    private int ticks;
    public int process_bits = 0;
    public int solar = 0;
    public boolean nightUpgrade = false;
    public int machineTier = 0;
    public static final int INVENTORY_SIZE = 7;
    public int maxProcessBits = 1; // not used for logic, only used for menu display

    /*
    sugar slot 0
    jam jar slot 1
    jammable input slot 2
    jammable output slot 3

    module slot 1 => 4
    module slot 2 => 5
    module slot 3 => 6
    */

    public void updateBlock(){
        this.setChanged();
        if(this.level != null){
            this.level.sendBlockUpdated(this.getBlockPos(),this.getBlockState(),this.getBlockState(),3);
        }
    }

    public void setNightMode(){
        nightUpgrade = true;
        updateBlock();
    }

    public JamMakerBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.JAM_MAKER.get(), pos, blockState);
        this.itemHandler = new ItemStackHandler(INVENTORY_SIZE){
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
                    case 4,5,6 -> {
                        return stack.is(Tag.MODULE) && stack.has(Components.MODULE_TYPE);
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
        NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE,ItemStack.EMPTY);
        // machine slots
        items.set(0,itemHandler.getStackInSlot(0));
        items.set(1,itemHandler.getStackInSlot(1));
        items.set(2,itemHandler.getStackInSlot(2));
        items.set(3,itemHandler.getStackInSlot(3));
        // module slots
        items.set(4,itemHandler.getStackInSlot(4));
        items.set(5,itemHandler.getStackInSlot(5));
        items.set(6,itemHandler.getStackInSlot(6));
        ContainerHelper.saveAllItems(tag,items,registries);
        // data
        tag.putInt("ticks",ticks);
        tag.putInt("process_bits",process_bits);
        tag.putInt("solar",solar);
        tag.putBoolean("night_upgrade",nightUpgrade);
        tag.putInt("machine_tier",machineTier);
        tag.putInt("max_process_bits",maxProcessBits);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        NonNullList<ItemStack> itemsLoad = NonNullList.withSize(INVENTORY_SIZE,ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag,itemsLoad,registries);
        // machine slots
        itemHandler.setStackInSlot(0,itemsLoad.get(0));
        itemHandler.setStackInSlot(1,itemsLoad.get(1));
        itemHandler.setStackInSlot(2,itemsLoad.get(2));
        itemHandler.setStackInSlot(3,itemsLoad.get(3));
        // module slots
        itemHandler.setStackInSlot(4,itemsLoad.get(4));
        itemHandler.setStackInSlot(5,itemsLoad.get(5));
        itemHandler.setStackInSlot(6,itemsLoad.get(6));
        // data
        if(tag.contains("ticks")){
            ticks = tag.getInt("ticks");
        }
        if(tag.contains("process_bits")){
            process_bits = tag.getInt("process_bits");
        }
        if(tag.contains("solar")){
            solar = tag.getInt("solar");
        }
        if(tag.contains("night_upgrade")){
            nightUpgrade = tag.getBoolean("night_upgrade");
        }
        if(tag.contains("machine_tier")){
            machineTier = Mth.clamp(tag.getInt("machine_tier"),1,9);
        }
        if(tag.contains("max_process_bits")){
            maxProcessBits = Mth.clamp(tag.getInt("max_process_bits"),1,50);
        }
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

        if(this.getBlockState().is(CmatdBlock.JAM_MAKER)){
            if(this.getBlockState().hasProperty(CmatdBlockStateProperties.ALL_DAY_NIGHT)){
                if(nightUpgrade == true && !this.getBlockState().getValue(CmatdBlockStateProperties.ALL_DAY_NIGHT)){
                    this.getLevel().setBlock(
                            this.getBlockPos(),
                            this.getBlockState()
                                    .setValue(CmatdBlockStateProperties.ALL_DAY_NIGHT,true),3);
                    this.updateBlock();
                }
            }
        }

        if(level.isDay() || nightUpgrade){
            solar = 1;
            doStuff();
        }
        else{
            solar = 0;
        }
    }

    public void doStuff(){
        // machine tier affects how quickly the machine processes
        if(ticks % (10 - Mth.clamp(machineTier,1,9)) == 0){
            ItemStack sugarSlotItem = getItemHandler().getStackInSlot(0);
            ItemStack jamJarSlotItem = getItemHandler().getStackInSlot(1);
            Holder<Item> itemHolder = getItemHandler().getStackInSlot(2).getItemHolder();

            ItemStack moduleSlotOne = getItemHandler().getStackInSlot(4);
            ItemStack moduleSlotTwo = getItemHandler().getStackInSlot(5);
            ItemStack moduleSlotThree = getItemHandler().getStackInSlot(6);

            int speedModules = Utility.countModulesInStack(moduleSlotOne,0)
                    + Utility.countModulesInStack(moduleSlotTwo,0)
                    + Utility.countModulesInStack(moduleSlotThree,0);
            int efficiencyModules = Utility.countModulesInStack(moduleSlotOne,1)
                    + Utility.countModulesInStack(moduleSlotTwo,1)
                    + Utility.countModulesInStack(moduleSlotThree,1);
            int doublerModules = Utility.countModulesInStack(moduleSlotOne,2)
                    + Utility.countModulesInStack(moduleSlotTwo,2)
                    + Utility.countModulesInStack(moduleSlotThree,2);
            int tripledModules = Utility.countModulesInStack(moduleSlotOne,3)
                    + Utility.countModulesInStack(moduleSlotTwo,3)
                    + Utility.countModulesInStack(moduleSlotThree,3);
            int heatDispersionModules = Utility.countModulesInStack(moduleSlotOne,4)
                    + Utility.countModulesInStack(moduleSlotTwo,4)
                    + Utility.countModulesInStack(moduleSlotThree,4);
            int silencingModules = Utility.countModulesInStack(moduleSlotOne,5)
                    + Utility.countModulesInStack(moduleSlotTwo,5)
                    + Utility.countModulesInStack(moduleSlotThree,5);

            int processBitsMax = Mth.clamp(50 / Mth.clamp(efficiencyModules,1,49),1,50);
            maxProcessBits = processBitsMax;
            setChanged();

            if(itemHolder != null){
                Jammables jammablesData = itemHolder.getData(Cmatd.JAMMABLES);
                if(sugarSlotItem.is(Items.SUGAR) && jamJarSlotItem.is(CmatdItem.JAM_JAR) && jammablesData != null){
                    if(getItemHandler().getStackInSlot(3).getCount() < getItemHandler().getStackInSlot(3).getMaxStackSize()){
                        process_bits = process_bits + (1 + speedModules);
                        if(process_bits >= processBitsMax){
                            if(getItemHandler().getStackInSlot(3).is(jammablesData.outputItem())){
                                if(doublerModules > 0 || tripledModules > 0){
                                    if(level.getRandom().nextIntBetweenInclusive(0,100) <= doublerModules + tripledModules){
                                        // clamping this as there is a chance this may overflow
                                        getItemHandler().getStackInSlot(3).grow(Mth.clamp(1 + ((doublerModules * 2) + (tripledModules * 3)),1,getItemHandler().getStackInSlot(3).getMaxStackSize()));
                                    }
                                    else{
                                        getItemHandler().getStackInSlot(3).grow(1); // randomness failed, just give one
                                    }
                                }
                                else{
                                    getItemHandler().getStackInSlot(3).grow(1); // always give one if no doubling or tripling modules installed
                                }
                                getItemHandler().getStackInSlot(0).shrink(1);
                                getItemHandler().getStackInSlot(1).shrink(1);
                                getItemHandler().getStackInSlot(2).shrink(1);
                                if(silencingModules <= 0){
                                    level.playSound(null,getBlockPos(),
                                            CmatdSound.MASHER.get(), SoundSource.BLOCKS, 1.0f,1.0f);
                                }
                                if(level instanceof ServerLevel serverLevel){
                                    serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM,getItemHandler().getStackInSlot(3)),
                                            (double)getBlockPos().getX() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                            (double)getBlockPos().getY() + 0.45D,
                                            (double)getBlockPos().getZ() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                            1,0,0,0,0.1D);
                                }
                                process_bits = 0;
                                setChanged();
                            }
                            else{
                                if(getItemHandler().getStackInSlot(3).isEmpty()){
                                    getItemHandler().setStackInSlot(3,new ItemStack(jammablesData.outputItem()));
                                    getItemHandler().getStackInSlot(0).shrink(1);
                                    getItemHandler().getStackInSlot(1).shrink(1);
                                    getItemHandler().getStackInSlot(2).shrink(1);
                                    if(silencingModules <= 0){
                                        level.playSound(null,getBlockPos(),
                                                CmatdSound.MASHER.get(), SoundSource.BLOCKS, 1.0f,1.0f);
                                    }
                                    if(level instanceof ServerLevel serverLevel){
                                        serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM,getItemHandler().getStackInSlot(3)),
                                                (double)getBlockPos().getX() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                                (double)getBlockPos().getY() + 0.45D,
                                                (double)getBlockPos().getZ() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                                1,0,0,0,0.1D);
                                    }
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
        else if(index == 4 || index == 5 || index == 6){
            return false;
        }
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if(index == 3){
            return true;
        }
        else if(index == 4 || index == 5 || index == 6){
            return false;
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
        this.getItemHandler().setStackInSlot(4,ItemStack.EMPTY);
        this.getItemHandler().setStackInSlot(5,ItemStack.EMPTY);
        this.getItemHandler().setStackInSlot(6,ItemStack.EMPTY);
        setChanged();
    }
}
