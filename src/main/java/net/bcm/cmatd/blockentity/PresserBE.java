package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.CmatdBlockStateProperties;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
    public boolean nightUpgrade = false;
    public int machineTier = 0;

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

    public PresserBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.PRESSER.get(),pos, blockState);
        this.itemHandler = new ItemStackHandler(INVENTORY_SIZE){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack){
                if (stack.has(Components.MODULE_TYPE)){
                    return (slot == 3 || slot == 4 || slot == 5);
                }
                else{
                    if(stack.has(Components.PATTERN)){
                        return slot == 0; // pattern slot
                    }
                    else if(getStackInSlot(0).has(Components.PATTERN)){ // make sure we have the component first
                        Item consumedItem = getStackInSlot(0).get(Components.PATTERN).consumed_item().value();
                        if(consumedItem != null){
                            if(consumedItem == stack.getItem()){
                                return slot == 1; // consumed item (based on pattern) slot
                            }
                        }
                    }
                }
                return false;
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
        tag.putBoolean("night_upgrade",nightUpgrade);
        tag.putInt("machine_tier",machineTier);
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
        if(tag.contains("ticks")){
            ticks = tag.getInt("ticks");
        }
        if(tag.contains("process_bits")){
            process_bits = tag.getInt("process_bits");
        }
        if(tag.contains("same_item")){
            sameItem = tag.getInt("same_item");
        }
        if(tag.contains("night_upgrade")){
            nightUpgrade = tag.getBoolean("night_upgrade");
        }
        if(tag.contains("machine_tier")){
            machineTier = Mth.clamp(tag.getInt("machine_tier"),1,4);
        }
    }

    public void serverTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
            setChanged();
        }

        if(this.getBlockState().is(CmatdBlock.PRESSER)){
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

        boolean shouldBeSilenced = false; // machine will make no sounds if this is true

        if(level.isDay() || nightUpgrade){
            solar = 1;
            // machine tier affects how quickly the machine processes
            if(ticks % (5 - Mth.clamp(machineTier,1,4)) == 0){
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

                        int silenceSoundsAsTooFast = 3;
                        int efficiency_modules = 0;
                        // check efficiency modules
                        if(moduleSlot1.is(CmatdItem.EFFICIENCY_MODULE)){
                            efficiency_modules += (moduleSlot1.getCount());
                        }
                        if(moduleSlot2.is(CmatdItem.EFFICIENCY_MODULE)){
                            efficiency_modules += (moduleSlot1.getCount());
                        }
                        if(moduleSlot3.is(CmatdItem.EFFICIENCY_MODULE)){
                            efficiency_modules += (moduleSlot1.getCount());
                        }

                        if(moduleSlot1.is(CmatdItem.SILENCING_MODULE) || moduleSlot2.is(CmatdItem.SILENCING_MODULE) || moduleSlot3.is(CmatdItem.SILENCING_MODULE)){
                            shouldBeSilenced = true;
                        }

                        if(!getItemHandler().getStackInSlot(1).is(printInputItem)){
                            process_bits = 0;
                            return;
                        }

                        if(efficiency_modules > 0){
                            process_bits += ((1 + speedModules) * Mth.clamp(efficiency_modules,0,12));
                        }
                        else{
                            process_bits += (1 + speedModules);
                        }

                        if(process_bits >= 18){
                            if(getItemHandler().getStackInSlot(2).is(printOutputItem)){
                                //getItemHandler().getStackInSlot(0).shrink(1);
                                getItemHandler().getStackInSlot(1).shrink(1);
                                getItemHandler().getStackInSlot(2).grow(1);
                                process_bits = 0;
                                if(efficiency_modules >= silenceSoundsAsTooFast || speedModules >= silenceSoundsAsTooFast){
                                    if(level.getRandom().nextIntBetweenInclusive(0,300) <= 7){
                                        if(level instanceof ServerLevel serverLevel){
                                            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                                    getBlockPos().getX() + 0.5D,getBlockPos().getY() + 0.75D, getBlockPos().getZ() + 0.5D,
                                                    1,0,0,0,0.02);
                                        }
                                    }
                                    if(!shouldBeSilenced){
                                        if(level.getRandom().nextIntBetweenInclusive(0,100) <= 1){
                                            switch (level.getRandom().nextIntBetweenInclusive(0,3)){
                                                case 0 -> {
                                                    level.playSound(null,getBlockPos(),
                                                            SoundEvents.HEAVY_CORE_HIT, SoundSource.BLOCKS,0.5f,Utility.nextFloatBetweenInclusive(0.95f,1.0f));
                                                    break;
                                                }
                                                case 1 -> {
                                                    level.playSound(null,getBlockPos(),
                                                            SoundEvents.ANVIL_LAND, SoundSource.BLOCKS,0.1f,Utility.nextFloatBetweenInclusive(1.2f,1.5f));
                                                    break;
                                                }
                                                case 2 -> {
                                                    level.playSound(null,getBlockPos(),
                                                            SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS,0.25f,1.0f);
                                                    break;
                                                }
                                                case 3 -> {
                                                    level.playSound(null,getBlockPos(),
                                                            SoundEvents.VILLAGER_WORK_ARMORER, SoundSource.BLOCKS,0.25f,1.0f);
                                                    break;
                                                }
                                                default -> {
                                                    level.playSound(null,getBlockPos(),
                                                            SoundEvents.METAL_HIT, SoundSource.BLOCKS,1.0f,1.0f);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                else{
                                    if(!shouldBeSilenced){
                                        switch (level.getRandom().nextIntBetweenInclusive(0,3)){
                                            case 0 -> {
                                                level.playSound(null,getBlockPos(),
                                                        SoundEvents.HEAVY_CORE_HIT, SoundSource.BLOCKS,0.5f,Utility.nextFloatBetweenInclusive(0.95f,1.0f));
                                                break;
                                            }
                                            case 1 -> {
                                                level.playSound(null,getBlockPos(),
                                                        SoundEvents.ANVIL_LAND, SoundSource.BLOCKS,0.1f,Utility.nextFloatBetweenInclusive(1.2f,1.5f));
                                                break;
                                            }
                                            case 2 -> {
                                                level.playSound(null,getBlockPos(),
                                                        SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS,0.25f,1.0f);
                                                break;
                                            }
                                            case 3 -> {
                                                level.playSound(null,getBlockPos(),
                                                        SoundEvents.VILLAGER_WORK_ARMORER, SoundSource.BLOCKS,0.25f,1.0f);
                                                break;
                                            }
                                            default -> {
                                                level.playSound(null,getBlockPos(),
                                                        SoundEvents.METAL_HIT, SoundSource.BLOCKS,1.0f,1.0f);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(efficiency_modules < silenceSoundsAsTooFast && speedModules < silenceSoundsAsTooFast){
                                    if(level instanceof ServerLevel serverLevel){
                                        serverLevel.sendParticles(ParticleTypes.SCRAPE,
                                                (double)getBlockPos().getX() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                                (double)getBlockPos().getY() + 0.45D,
                                                (double)getBlockPos().getZ() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                                1,0,0,0,0);
                                    }
                                }
                                setChanged();
                            }
                            else{
                                if(getItemHandler().getStackInSlot(2).isEmpty()){
                                    //getItemHandler().getStackInSlot(0).shrink(1);
                                    getItemHandler().getStackInSlot(1).shrink(1);
                                    getItemHandler().setStackInSlot(2,new ItemStack(printOutputItem,1));
                                    process_bits = 0;
                                    if(!shouldBeSilenced){
                                        if(efficiency_modules >= silenceSoundsAsTooFast || speedModules >= silenceSoundsAsTooFast){
                                            if(level.getRandom().nextIntBetweenInclusive(0,100) <= 1){
                                                switch (level.getRandom().nextIntBetweenInclusive(0,3)){
                                                    case 0 -> {
                                                        level.playSound(null,getBlockPos(),
                                                                SoundEvents.HEAVY_CORE_HIT, SoundSource.BLOCKS,0.5f,Utility.nextFloatBetweenInclusive(0.95f,1.0f));
                                                        break;
                                                    }
                                                    case 1 -> {
                                                        level.playSound(null,getBlockPos(),
                                                                SoundEvents.ANVIL_LAND, SoundSource.BLOCKS,0.1f,Utility.nextFloatBetweenInclusive(1.2f,1.5f));
                                                        break;
                                                    }
                                                    case 2 -> {
                                                        level.playSound(null,getBlockPos(),
                                                                SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS,0.25f,1.0f);
                                                        break;
                                                    }
                                                    case 3 -> {
                                                        level.playSound(null,getBlockPos(),
                                                                SoundEvents.VILLAGER_WORK_ARMORER, SoundSource.BLOCKS,0.25f,1.0f);
                                                        break;
                                                    }
                                                    default -> {
                                                        level.playSound(null,getBlockPos(),
                                                                SoundEvents.METAL_HIT, SoundSource.BLOCKS,1.0f,1.0f);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        else{
                                            switch (level.getRandom().nextIntBetweenInclusive(0, 3)) {
                                                case 0 -> {
                                                    level.playSound(null, getBlockPos(),
                                                            SoundEvents.HEAVY_CORE_HIT, SoundSource.BLOCKS, 0.5f, Utility.nextFloatBetweenInclusive(0.95f, 1.0f));
                                                    break;
                                                }
                                                case 1 -> {
                                                    level.playSound(null, getBlockPos(),
                                                            SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.1f, Utility.nextFloatBetweenInclusive(1.2f, 1.5f));
                                                    break;
                                                }
                                                case 2 -> {
                                                    level.playSound(null, getBlockPos(),
                                                            SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS, 0.25f, 1.0f);
                                                    break;
                                                }
                                                case 3 -> {
                                                    level.playSound(null, getBlockPos(),
                                                            SoundEvents.VILLAGER_WORK_ARMORER, SoundSource.BLOCKS, 0.25f, 1.0f);
                                                    break;
                                                }
                                                default -> {
                                                    level.playSound(null, getBlockPos(),
                                                            SoundEvents.METAL_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    // particles for pressing things
                                    if(efficiency_modules < silenceSoundsAsTooFast && speedModules < silenceSoundsAsTooFast){
                                        if(level instanceof ServerLevel serverLevel){
                                            serverLevel.sendParticles(ParticleTypes.SCRAPE,
                                                    (double)getBlockPos().getX() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                                    (double)getBlockPos().getY() + 0.45D,
                                                    (double)getBlockPos().getZ() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                                    1,0,0,0,0);
                                        }
                                    }
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
