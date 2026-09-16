package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.ServerConfig;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.api.*;
import net.bcm.cmatd.datagen.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
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

        @Override
        public boolean canGasBeInsertedIntoTank(GasStack gasStack) {
            return false;
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
                if(level.getBlockState(position).is(Tag.VALID_RADIOACTIVE_REACTOR_CASINGS)){
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
                        serverLevel.sendParticles(ParticleTypes.SONIC_BOOM,
                                (double)getBlockPos().getX() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                (double)getBlockPos().getY() + 0.45D,
                                (double)getBlockPos().getZ() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -0.5 : 0.5),
                                1,0,0,0,0);
                        for(BlockPos position : neighborPositions){
                            serverLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                                    (double)position.getX() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -1.25 : 1.25),
                                    (double)position.getY() + 0.45D,
                                    (double)position.getZ() + 0.5 + level.getRandom().nextDouble() / 2.0 * (level.getRandom().nextBoolean() ? -1.25 : 1.25),
                                    1,0,0,0,0.04);
                        }
                        serverLevel.playSound(null,getBlockPos(),
                                SoundEvents.END_PORTAL_FRAME_FILL,SoundSource.BLOCKS);
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
                int speedModules = 0; // affects heat output
                int efficiencyModules = 0; // makes coolant more effective
                int heatDispersionModules = 0; // heat is negated entirely
                // isolate the modules
                List<ItemStack> moduleStacks = List.of(
                        itemStackHandler.getStackInSlot(25),
                        itemStackHandler.getStackInSlot(26),
                        itemStackHandler.getStackInSlot(27),
                        itemStackHandler.getStackInSlot(28),
                        itemStackHandler.getStackInSlot(29)
                );
                // iterate over the modules and see if they really are modules and what type they are
                for(ItemStack module : moduleStacks){
                    if(!module.isEmpty()){
                        if(module.has(Components.MODULE_TYPE)){
                            // now apply their effects
                            if(module.get(Components.MODULE_TYPE).intValue() == Utility.MODULE_TYPE_SPEED){
                                speedModules++;
                            }
                            else if(module.get(Components.MODULE_TYPE).intValue() == Utility.MODULE_TYPE_EFFICIENCY){
                                efficiencyModules++;
                            }
                            else if(module.get(Components.MODULE_TYPE).intValue() == Utility.MODULE_TYPE_HEAT_DISPERSING){
                                heatDispersionModules++;
                            }
                        }
                    }
                }
                // if active, do all actions with modules applied
                if(isActive){
                    produceWaste();
                    // heat level increase based on conditions
                    if(isProcessing){
                        if(coolantAmount > 0){
                            // just make sure heat is never less than zero
                            heatAmount = Mth.clamp(heatAmount - (efficiencyModules + 1),0,heatAmountToGoBoomAt);
                            // coolant is used less often when efficiency is very high
                            if(efficiencyModules > 8){
                                if(level instanceof ServerLevel serverLevel){
                                    // randomly decide based on efficiency module count when to reduce coolant, making them what is called '(n)%' better
                                    // the max is 200% efficiency (or what is effectively that percentage)
                                    if(serverLevel.getRandom().nextIntBetweenInclusive(0,Mth.clamp(10 * efficiencyModules,10,200)) <= 10){
                                        coolantAmount -= 1;
                                    }
                                }
                            }
                            else{
                                coolantAmount -= 1;
                            }
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

                // if heat dispersion is active, then when the heat is at the ideal temperature, keep it at that temperature
                if(heatAmount > 5500){
                    if(heatDispersionModules > 0){
                        heatAmount = (5500 + heatDispersionModules);
                        setChanged();
                    }
                }

                // heat must be at the max and above or at zero
                if(heatAmount > heatAmountToGoBoomAt || heatAmount < 0){
                    heatAmount = Mth.clamp(heatAmount,0,heatAmountToGoBoomAt);
                    setChanged();
                }
                // heat level effects
                // start making noises that tell players the reactor is failing
                if(heatAmount >= heatAmountToWarnAt && heatAmount < heatAmountToGoBoomAt){
                    if(level != null){
                        if(level instanceof ServerLevel serverLevel){
                            if(ServerConfig.RADIOACTIVE_REACTOR_MAKES_DAMAGE_SOUNDS.getAsBoolean()){
                                if(serverLevel.getGameTime() % Mth.randomBetweenInclusive(serverLevel.getRandom(),37,76) == 0){
                                    serverLevel.playSound(null,
                                            getBlockPos(),
                                            SoundEvents.HEAVY_CORE_BREAK,
                                            SoundSource.BLOCKS);

                                    serverLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                                            (double)getBlockPos().getX() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                            (double)getBlockPos().getY() + 0.45D,
                                            (double)getBlockPos().getZ() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                            7,0,0,0,serverLevel.getRandom().nextFloat() * 0.3f);
                                }
                                if(serverLevel.getGameTime() % Mth.randomBetweenInclusive(serverLevel.getRandom(), 25,91) == 0){
                                    serverLevel.playSound(null,
                                            getBlockPos(),
                                            SoundEvents.FIRE_EXTINGUISH,
                                            SoundSource.BLOCKS,0.5f,serverLevel.getRandom().nextFloat() * 0.91f);
                                }

                                if(serverLevel.getGameTime() % 20 == 0 && heatAmount < 14000){
                                    if(heatAmount < 12000 && heatAmount >= 11000){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.NOTE_BLOCK_PLING.value(),
                                                SoundSource.BLOCKS,0.4f,0.77f);
                                    }
                                    else if(heatAmount < 13000 && heatAmount >= 12000){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.NOTE_BLOCK_PLING.value(),
                                                SoundSource.BLOCKS,0.4f,0.86f);
                                    }
                                    else if(heatAmount < 14000 && heatAmount >= 13000){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.NOTE_BLOCK_PLING.value(),
                                                SoundSource.BLOCKS,0.5f,0.91f);
                                    }
                                }
                                else if(serverLevel.getGameTime() % 10 == 0 && heatAmount > 14000 && heatAmount < 14500){
                                    serverLevel.playSound(null,
                                            getBlockPos(),
                                            SoundEvents.NOTE_BLOCK_PLING.value(),
                                            SoundSource.BLOCKS,0.7f,1.25f);
                                }
                                else if(serverLevel.getGameTime() % 4 == 0 && heatAmount >= 14500){
                                    serverLevel.playSound(null,
                                            getBlockPos(),
                                            SoundEvents.NOTE_BLOCK_PLING.value(),
                                            SoundSource.BLOCKS,1.0f,1.5f);
                                }
                            }
                        }
                    }
                }
                // reactor is too hot, we're done for!
                if(heatAmount >= heatAmountToGoBoomAt){
                    if(level != null){
                        if(level instanceof ServerLevel serverLevel){
                            // the extremely dangerous consequence if the reactor is too hot
                            if(ServerConfig.RADIOACTIVE_REACTOR_EXPLODES_WHEN_TOO_HOT.getAsBoolean()){
                                serverLevel.explode(null,
                                        getBlockPos().getX(),
                                        getBlockPos().getY(),
                                        getBlockPos().getZ(),
                                        10.0f,
                                        true,
                                        Level.ExplosionInteraction.BLOCK);
                                // if there was any waste in the gas tank then release it
                                if(wasteGasTank.gas.getAmount() > 0){
                                    wasteGasTank.setGas(GasStack.EMPTY,true);
                                    serverLevel.setBlock(getBlockPos(),Blocks.WATER.defaultBlockState(),3);
                                }
                            }
                            else{
                                // the nice way to make the reactor fail
                                heatAmount = 0;
                                coolantAmount = 0;
                                isFormed = false;
                            }
                        }
                    }
                    updateBlock();
                    return;
                }
            }
            distributeProducts(); // try sending waste out
        }
    }

    public void distributeProducts(){
        for(Direction direction : Direction.values()){
            // only try gas first, and if not present, then try fluid
            IGasHandler handler = level.getCapability(Capabilities.GasHandler.BLOCK,
                    getBlockPos().relative(direction), null);
            if (handler != null) {
                if (handler.getGasTanks() >= 1) {
                    int received = handler.fill(wasteGasTank.gas,false);
                    // drain the gas tank of the amount to drain
                    this.wasteGasTank.drain(received, false);
                    // force remove liquid from the tank
                    this.wasteConversionToFluidTank.drain(received, IFluidHandler.FluidAction.EXECUTE);
                    setChanged();
                }
            }
            else{
                IFluidHandler fluidHandler = level.getCapability(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK,
                        getBlockPos().relative(direction),null);
                if(fluidHandler != null){
                    if(fluidHandler.getTanks() >= 1){
                        int receivedFluid = fluidHandler.fill(wasteConversionToFluidTank.getFluid(), IFluidHandler.FluidAction.EXECUTE);
                        // force remove gas from the gas tank
                        this.wasteGasTank.setGas(new GasStack(wasteGasTank.getGasStack().getGas(),wasteGasTank.getGasStack().getAmount() - receivedFluid),true);

                        // drain the fluid tank of the amount to drain
                        this.wasteConversionToFluidTank.drain(receivedFluid, IFluidHandler.FluidAction.EXECUTE);
                        setChanged();
                    }
                }
            }
        }
    }

    // should the reactor even be active?
    public void checkIfShouldBeActive(){
        boolean fuelDetected = false;
        // iterate over the slots and what item they have
        for(int index = 0; index < 30; index++){
            if(index >= 30){
                break;
            }
            ItemStack stack = itemStackHandler.getStackInSlot(index);
            if(stack.is(Items.COAL)){
                fuelDetected = true;
            }
            if(level instanceof ServerLevel serverLevel){
                // every 15 ticks, check if a coolant is in the reactor
                // if so, apply the coolant dependent on the type of coolant it is; the colder, the better
                if(serverLevel.getGameTime() % 15 == 0){
                    if(stack.is(Items.ICE) || stack.is(Items.PACKED_ICE) || stack.is(Items.BLUE_ICE)){
                        coolantAmount += (5 + stack.getCount());
                        stack.setCount(0);
                        setChanged();
                    }
                    // water buckets turn into buckets when used, so do that here
                    if(stack.is(Items.WATER_BUCKET)){
                        coolantAmount += 3;
                        itemStackHandler.setStackInSlot(index,new ItemStack(Items.BUCKET));
                        setChanged();
                    }
                    // durable items should be considered special as they can repair or be damaged at the same time
                    if(stack.has(DataComponents.DAMAGE) && stack.has(Components.COOLANT)){
                        coolantAmount++;
                        stack.set(DataComponents.DAMAGE,stack.get(DataComponents.DAMAGE) - 1);
                        setChanged();
                    }
                }
            }
        }
        // waste tank is full, stop producing or doing anything
        if(wasteGasTank.getGasAmount() >= wasteGasTank.getCapacity()){
            if(isActive){
                isActive = false;
                setChanged();
            }
            return;
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
            int speedModules = 0; // increases speed of production but also heat increase
            int efficiencyModules = 0; // processing takes less time
            int doubleOutputModules = 0; // energy output doubled times module count
            int tripleOutputModules = 0; // energy output tripled times module count

            // isolate the modules
            List<ItemStack> moduleStacks = List.of(
                    itemStackHandler.getStackInSlot(25),
                    itemStackHandler.getStackInSlot(26),
                    itemStackHandler.getStackInSlot(27),
                    itemStackHandler.getStackInSlot(28),
                    itemStackHandler.getStackInSlot(29)
            );
            // iterate over the modules and see if they really are modules and what type they are
            for(ItemStack module : moduleStacks){
                if(!module.isEmpty()){
                    if(module.has(Components.MODULE_TYPE)){
                        // now apply their effects
                        if(module.get(Components.MODULE_TYPE).intValue() == Utility.MODULE_TYPE_SPEED){
                            speedModules++;
                        }
                        else if(module.get(Components.MODULE_TYPE).intValue() == Utility.MODULE_TYPE_EFFICIENCY){
                            efficiencyModules++;
                        }
                        else if(module.get(Components.MODULE_TYPE).intValue() == Utility.MODULE_TYPE_DOUBLING){
                            doubleOutputModules++;
                        }
                        else if(module.get(Components.MODULE_TYPE).intValue() == Utility.MODULE_TYPE_TRIPLING){
                            tripleOutputModules++;
                        }
                    }
                }
            }
            // divide by the amount of efficiency modules to speed up production of power, at a cost
            if(processBits >= (int)(processBitsToProduceAt / Mth.clamp(efficiencyModules,1,20))){
                if(!getEnergyStorage().isSaturatedEnergy() && getWasteGasTank().getGasAmount() < getWasteGasTank().getCapacity()){
                    // we do not want energy to be multiplied by zero with no modules installed, so add 1
                    getEnergyStorage().receiveEnergy(1000 * (1 + (doubleOutputModules + tripleOutputModules)),false);
                    if(!wasteGasTank.getGasStack().isEmpty()){
                        wasteGasTank.setGas(new GasStack(wasteGasTank.getGasStack().getGas(),Mth.clamp(wasteGasTank.getGasAmount() + (10 * (1 + efficiencyModules)),0,wasteGasTank.getCapacity())),true);
                        wasteConversionToFluidTank.setFluid(new FluidStack(Fluids.WATER,wasteGasTank.getGasAmount()));
                        wasteGasTank.update();
                    }
                    else{
                        wasteGasTank.setGas(new GasStack(Gases.RADIOACTIVE_WASTE,10 * (1 + efficiencyModules)),true);
                        wasteConversionToFluidTank.setFluid(new FluidStack(Fluids.WATER,wasteGasTank.getGasAmount()));
                        wasteGasTank.update();
                    }
                    processBits = 0;
                    isProcessing = false;
                    updateBlock();
                }
            }
            else{
                if(level instanceof ServerLevel serverLevel){
                    // speed modules will make processing faster
                    if(serverLevel.getGameTime() % Mth.clamp(speedModules,1,100) == 0){
                        // efficiency modules make processing more effective
                        processBits += (1 + efficiencyModules);
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
