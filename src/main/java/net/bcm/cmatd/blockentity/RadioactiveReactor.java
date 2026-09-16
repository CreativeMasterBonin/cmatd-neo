package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.api.GasTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class RadioactiveReactor extends TieredMachine{
    public boolean isFormed = false;
    public final List<BlockPos> neighborPositions = List.of(
            // below once, then separately the same level, four directions from master block
            new BlockPos(
                    this.getBlockPos().below().getX(),
                    this.getBlockPos().below().getY(),
                    this.getBlockPos().below().getZ()),
            new BlockPos(
                    this.getBlockPos().north().getX(),
                    this.getBlockPos().north().getY(),
                    this.getBlockPos().north().getZ()),
            new BlockPos(
                    this.getBlockPos().south().getX(),
                    this.getBlockPos().south().getY(),
                    this.getBlockPos().south().getZ()),
            new BlockPos(
                    this.getBlockPos().east().getX(),
                    this.getBlockPos().east().getY(),
                    this.getBlockPos().east().getZ()),
            new BlockPos(
                    this.getBlockPos().west().getX(),
                    this.getBlockPos().west().getY(),
                    this.getBlockPos().west().getZ()),
            // below, 1 step away from master block
            new BlockPos(
                    this.getBlockPos().north().below().getX(),
                    this.getBlockPos().north().below().getY(),
                    this.getBlockPos().north().below().getZ()),
            new BlockPos(
                    this.getBlockPos().south().below().getX(),
                    this.getBlockPos().south().below().getY(),
                    this.getBlockPos().south().below().getZ()),
            new BlockPos(
                    this.getBlockPos().east().below().getX(),
                    this.getBlockPos().east().below().getY(),
                    this.getBlockPos().east().below().getZ()),
            new BlockPos(
                    this.getBlockPos().west().below().getX(),
                    this.getBlockPos().west().below().getY(),
                    this.getBlockPos().west().below().getZ()),
            // 2 below, 1 step away from master block
            new BlockPos(
                    this.getBlockPos().north().below().below().getX(),
                    this.getBlockPos().north().below().below().getY(),
                    this.getBlockPos().north().below().below().getZ()),
            new BlockPos(
                    this.getBlockPos().south().below().below().getX(),
                    this.getBlockPos().south().below().below().getY(),
                    this.getBlockPos().south().below().below().getZ()),
            new BlockPos(
                    this.getBlockPos().east().below().below().getX(),
                    this.getBlockPos().east().below().below().getY(),
                    this.getBlockPos().east().below().below().getZ()),
            new BlockPos(
                    this.getBlockPos().west().below().below().getX(),
                    this.getBlockPos().west().below().below().getY(),
                    this.getBlockPos().west().below().below().getZ())
    );

    public final ItemStackHandler itemStackHandler = new ItemStackHandler(30){
        public NonNullList<ItemStack> getStacks(){
            return stacks;
        }
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if(slot > 24){
                return stack.has(Components.MODULE_TYPE);
            }
            return super.isItemValid(slot, stack);
        }
    };
    public int gasAmount = 0;
    public RadioactiveReactorGasContainerData gasContainerData;
    public RadioactiveReactorFluidContainerData fluidContainerData;
    public int heatAmount = 0; // the heat stored in this machine if not dissipated using coolant
    public int coolantAmount = 0; // the coolant used to cool down the reactor when running and off (0 = kaboom)
    public final int maxCoolantAmount = 30000; // the maximum coolant the reactor can store
    public final int heatAmountToGoBoomAt = 15000; // the heat maximum until the reactor is unstable
    public final int heatAmountToWarnAt = 10000; // the heat to start warning players at until unstable
    public final int processBitsToProduceAt = 1000; // the amount of 'bits' it takes to make some energy from the items inside the reactor
    public boolean isActive = false;

    public final GasTank wasteGasTank = new GasTank(1000000){
        @Override
        public void update() {
            gasAmount = this.getGasAmount();
            setChanged();
            if(!level.isClientSide){
                level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            }
        }
    };
    public GasTank getWasteGasTank(){return this.wasteGasTank;}
    public FluidTank getWasteConvertedToFluidTank(){return this.wasteConversionToFluidTank;}
    public final FluidTank wasteConversionToFluidTank = new FluidTank(1000000);

    public RadioactiveReactor(BlockPos pos, BlockState blockState) {
        super(CmatdBE.RADIOACTIVE_REACTOR.get(), pos, blockState);
        gasContainerData = new RadioactiveReactorGasContainerData(this);
        fluidContainerData = new RadioactiveReactorFluidContainerData(this);
    }

    public boolean canForm(){
        int neededBlocksToForm = neighborPositions.size();
        int foundNeededBlocks = 0;
        if(level != null){
            for(BlockPos position : neighborPositions){
                // check if the state is the required kind of block we need to form the multiblock
                if(level.getBlockState(position).is(Blocks.IRON_BLOCK)){
                    foundNeededBlocks++;
                }
            }
        }
        return foundNeededBlocks >= neededBlocksToForm;
    }

    @Override
    public int getFirstTimeMaxMachineTier() {
        return 0;
    }

    @Override
    public BaseEnergyStorage getFirstTimeEnergyStorage() {
        return new BaseEnergyStorage(10000000,1000000,1000000,0);
    }

    @Override
    public void loadExtraValues(CompoundTag tag, HolderLookup.Provider registries) {
        if(tag.contains("formed")){
            isFormed=tag.getBoolean("formed");
        }
        NonNullList<ItemStack> stacks = NonNullList.withSize(30,ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag,stacks,registries);
        for(int index = 0; index < stacks.size(); index++){
            if(index >= 30){
                break;
            }
            itemStackHandler.setStackInSlot(index,stacks.get(index));
        }

        wasteGasTank.load(registries,tag);
        if(tag.contains("gas_amount")){
            gasAmount = tag.getInt("gas_amount");
        }
        wasteConversionToFluidTank.readFromNBT(registries,tag);
        if(tag.contains("stored_energy")){
            battery.setEnergy(tag.getInt("stored_energy"));
        }
        if(tag.contains("heat_amount")){
            heatAmount = tag.getInt("heat_amount");
        }
        if(tag.contains("coolant_amount")){
            coolantAmount = tag.getInt("coolant_amount");
        }
        if(tag.contains("active")){
            isActive = tag.getBoolean("active");
        }
    }

    @Override
    public void saveExtraValues(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("formed",isFormed);
        NonNullList<ItemStack> stacks = NonNullList.withSize(30,ItemStack.EMPTY);
        for(int index = 0; index < stacks.size(); index++){
            if(index >= 30){
                break;
            }
            stacks.set(index,itemStackHandler.getStackInSlot(index));
        }
        ContainerHelper.saveAllItems(tag,stacks,registries);
        wasteGasTank.save(registries,tag);
        tag.putInt("gas_amount",gasAmount);
        wasteConversionToFluidTank.writeToNBT(registries,tag);
        tag.putInt("stored_energy",battery.getEnergyStored());
        tag.putInt("heat_amount",heatAmount);
        tag.putInt("coolant_amount",coolantAmount);
        tag.putBoolean("active",isActive);
    }

    @Override
    public void extraServerTick() {
        if(level != null){
            // enforce chunks needing to be loaded within a range of block positions
            if(!level.hasChunksAt(getBlockPos().south().below(),getBlockPos().north().above())){
                return;
            }
            // multiblock is not formed, check if it should be
            if(!isFormed){
                // if multiblock can be formed, then set as formed
                if(canForm()){
                    if(level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                (double)getBlockPos().getX() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                (double)getBlockPos().getY() + 0.45D,
                                (double)getBlockPos().getZ() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                7,0,0,0,0);
                    }
                    isFormed = true;
                    updateBlock();
                }
            }
            else{
                // check if multiblock should disassemble
                if(!canForm()){
                    isFormed = false;
                    updateBlock();
                    return;
                }
                checkIfShouldBeActive();
                if(isActive){
                    produceWaste();
                    // heat level increase based on conditions
                    if(isProcessing){
                        if(coolantAmount > 0){
                            heatAmount -= 1;
                            coolantAmount -= 1;
                        }
                        else{
                            heatAmount += 1;
                        }
                        coolantAmount = Mth.clamp(coolantAmount,0,maxCoolantAmount);
                        setChanged();
                    }
                    else{
                        heatAmount -= 1;
                    }
                }
                else{
                    if(heatAmount > 0){
                        heatAmount = Mth.clamp(heatAmount - 1,0,heatAmountToGoBoomAt);
                        setChanged();
                    }
                }

                if(heatAmount > heatAmountToGoBoomAt || heatAmount < 0){
                    heatAmount = Mth.clamp(heatAmount,0,heatAmountToGoBoomAt);
                    setChanged();
                }
                // heat level effects
                if(heatAmount >= heatAmountToWarnAt){
                    if(level != null){
                        if(level instanceof ServerLevel serverLevel){
                            if(serverLevel.getGameTime() % Mth.randomBetweenInclusive(serverLevel.getRandom(),37,76) == 0){
                                serverLevel.playSound(null,
                                        getBlockPos(),
                                        SoundEvents.HEAVY_CORE_BREAK,
                                        SoundSource.BLOCKS);
                            }
                        }
                    }
                }
                if(heatAmount >= heatAmountToGoBoomAt){
                    if(level != null){
                        if(level instanceof ServerLevel serverLevel){
                            serverLevel.explode(null,
                                    getBlockPos().getX(),
                                    getBlockPos().getY(),
                                    getBlockPos().getZ(),
                                    10.0f,
                                    true,
                                    Level.ExplosionInteraction.BLOCK);
                        }
                    }
                    updateBlock();
                    return;
                }
            }
        }
    }

    public void checkIfShouldBeActive(){
        int coolant = 0;
        boolean fuelDetected = false;
        for(int index = 0; index < 30; index++){
            if(index >= 30){
                break;
            }
            ItemStack stack = itemStackHandler.getStackInSlot(index);
            if(stack.is(Items.COAL)){
                fuelDetected = true;
            }
            if(level instanceof ServerLevel serverLevel){
                if(serverLevel.getGameTime() % 15 == 0){
                    if(stack.is(Items.ICE) || stack.is(Items.PACKED_ICE) || stack.is(Items.BLUE_ICE)){
                        coolantAmount += (5 + stack.getCount());
                        stack.setCount(0);
                        setChanged();
                    }
                    if(stack.is(Items.WATER_BUCKET)){
                        coolantAmount++;
                        stack.setCount(0);
                        setChanged();
                    }
                }
            }
        }
        if(fuelDetected){
            isActive = true;
            setChanged();
        }
        else{
            isActive = false;
            setChanged();
        }
    }

    public void produceWaste(){
        if(isActive){
            if(processBits >= processBitsToProduceAt){
                if(!getEnergyStorage().isSaturatedEnergy() && getWasteGasTank().getGasAmount() < getWasteGasTank().getCapacity()){
                    getEnergyStorage().receiveEnergy(1000,false);
                    processBits = 0;
                    isProcessing = false;
                    updateBlock();
                }
            }
            else{
                if(level instanceof ServerLevel serverLevel){
                    if(serverLevel.getGameTime() % 7 == 0){
                        processBits++;
                    }
                }
                isProcessing = true;
                setChanged();
            }
        }
        else{
            if(processBits != 0){
                processBits = 0;
                setChanged();
            }
        }
    }
}
