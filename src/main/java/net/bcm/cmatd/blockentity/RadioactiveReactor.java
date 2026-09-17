package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.ServerConfig;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.api.*;
import net.bcm.cmatd.block.CmatdBlock;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockPredicate;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
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
            return stacks; // completely useless as the method is inaccessible outside the object itself
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
    public RadioactiveReactorGasContainerData gasContainerData; // shared via menu
    public RadioactiveReactorFluidContainerData fluidContainerData; // shared via menu
    public int heatAmount = 0; // the heat stored in this machine if not dissipated using coolant
    public int coolantAmount = 0; // the coolant used to cool down the reactor when running and off (0 = kaboom)
    public final int maxCoolantAmount = 30000; // the maximum coolant the reactor can store
    public final int heatAmountToGoBoomAt = 15000; // the heat maximum until the reactor is unstable
    public final int heatAmountToWarnAt = 10000; // the heat to start warning players at until unstable
    public final int processBitsToProduceAt = 1000; // the amount of 'bits' it takes to make some energy from the items inside the reactor
    public boolean isActive = false;
    public int maxHeatWhenCooling = 5500; // affects how much heat output is maintained when using coolant
    public float radioactivityOfFuelAccumulative = 0.0f;

    public float getRadioactivity(){
        return radioactivityOfFuelAccumulative;
    }

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
    public final FluidTank wasteConversionToFluidTank = new FluidTank(1000000){
        @Override
        protected void onContentsChanged() {

        }
    };

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
        if(tag.contains("max_heat_when_cooling")){
            maxHeatWhenCooling = Mth.clamp(tag.getInt("max_heat_when_cooling"),500,heatAmountToGoBoomAt - 1000);
        }
        if(tag.contains("radioactivity")){
            radioactivityOfFuelAccumulative = tag.getFloat("radioactivity");
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
        tag.putInt("max_heat_when_cooling",Mth.clamp(maxHeatWhenCooling,500,heatAmountToGoBoomAt - 1000));
        tag.putFloat("radioactivity",radioactivityOfFuelAccumulative);
    }

    @Override
    public void extraServerTick() {
        if(level != null){
            // enforce chunks needing to be loaded within a range of block positions
            if(!level.hasChunksAt(getBlockPos().south().below(),getBlockPos().north().above())){
                return;
            }
            // iterate over the items (without the module slots)
            for(int index = 0; index < 25; index++){
                // this index is not used and is invalid in this case
                if(index >= 25){
                    break;
                }
                if(itemStackHandler.getStackInSlot(index).is(Tag.VALID_RADIOACTIVE_FUELS)){
                    ItemStack copiedFuelStack = itemStackHandler.getStackInSlot(index);
                    // accumulate radioactivity for each piece of fuel, since radioactive material radioactivity sticks around for a while
                    if(copiedFuelStack.has(Components.RADIOACTIVE)){
                        if(getRadioactivity() < 1000.0f){
                            radioactivityOfFuelAccumulative += copiedFuelStack.get(Components.RADIOACTIVE).radioactivity();
                            updateBlock();
                        }
                    }
                    copiedFuelStack.shrink(1);
                    itemStackHandler.setStackInSlot(index,copiedFuelStack);
                    setChanged();
                }
            }
            // check if radioactivity is higher than normal
            if(getRadioactivity() > 0.0f){
                if(level instanceof ServerLevel serverLevel){
                    List<LivingEntity> entities = serverLevel.getNearbyEntities(
                            LivingEntity.class, TargetingConditions.forNonCombat(),null,new AABB(
                                    getBlockPos().getX() - 7,
                                    getBlockPos().getY() - 7,
                                    getBlockPos().getZ() - 7,
                                    getBlockPos().getX() + 7,
                                    getBlockPos().getY() + 7,
                                    getBlockPos().getZ() + 7
                            )
                    );

                    for(LivingEntity livingEntity : entities){
                        if(!livingEntity.hasEffect(MobEffects.POISON)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,100,1,true,false));
                        }
                        if(!livingEntity.hasEffect(MobEffects.HUNGER)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER,100,2,true,false));
                        }
                        if(!livingEntity.hasEffect(MobEffects.WEAKNESS)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,100,2,true,false));
                        }
                        /*if(livingEntity instanceof Player){
                            System.out.println(livingEntity.distanceToSqr(getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ()));
                        }*/
                        if(livingEntity.distanceToSqr(getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ()) <= 12){
                            livingEntity.hurt(serverLevel.damageSources().inFire(),1.0f);
                        }
                    }

                    if(serverLevel.getRandom().nextIntBetweenInclusive(0,320) <= 50){
                        radioactivityOfFuelAccumulative -= 0.01f;
                    }
                    setChanged();
                }
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
                        // this sound is for accessibility, so do not silence it
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
                boolean shouldBeSilenced = false; // makes the reactor quiet, silencing sound effects, including alarms
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
                            else if(module.get(Components.MODULE_TYPE).intValue() == Utility.MODULE_TYPE_SILENCING){
                                shouldBeSilenced = true;
                            }
                        }
                    }
                }
                // if active, do all actions with modules applied
                if(isActive){
                    produceWaste();
                    // heat level increase based on conditions
                    if(isProcessing){
                        // make a sound if processing and active, no matter what occasion (other than a config option)
                        if(ServerConfig.RADIOACTIVE_REACTOR_MAKES_AMBIENT_SOUNDS.getAsBoolean()){
                            // if the server allows sounds but the block itself is silenced by a module, skip playing a sound
                            if(!shouldBeSilenced){
                                if(level instanceof ServerLevel serverLevel){
                                    if(serverLevel.getGameTime() % 100 == 0){
                                        serverLevel.playSound(null,getBlockPos(),
                                                SoundEvents.BEACON_AMBIENT,SoundSource.BLOCKS,1.0f,0.7f);
                                    }
                                }
                            }
                        }
                        // only reduce coolant when needed based on threshold (and if coolant exists at all)
                        if(coolantAmount > 0 && heatAmount >= Mth.clamp(maxHeatWhenCooling,500,heatAmountToGoBoomAt - 1000)){
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
                    // show some flair for when off but full of things
                    if(getEnergyStorage().getEnergyStored() >= getEnergyStorage().getCapacity() || getWasteGasTank().getGasAmount() >= getWasteGasTank().getCapacity()){
                        // silencing module will stop sounds trying to play here
                        if(level instanceof ServerLevel serverLevel){
                            if(!shouldBeSilenced){
                                if(serverLevel.getRandom().nextIntBetweenInclusive(0,1200) <= 2){
                                    serverLevel.playSound(null,
                                            getBlockPos(),
                                            SoundEvents.BREEZE_WHIRL,
                                            SoundSource.BLOCKS,1.0f,2.0f);
                                }
                            }

                            if(serverLevel.getRandom().nextIntBetweenInclusive(0,72) <= 1){
                                if(!shouldBeSilenced){
                                    serverLevel.playSound(null,
                                            getBlockPos(),
                                            SoundEvents.FIRE_EXTINGUISH,
                                            SoundSource.BLOCKS,0.1f,Utility.nextFloatBetweenInclusive(0.3f,0.4f));
                                }
                                serverLevel.sendParticles(ParticleTypes.CLOUD,
                                        (double)getBlockPos().getX() + 0.5 +
                                                serverLevel.getRandom().nextDouble() / 2.0 *
                                                        (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                        (double)getBlockPos().getY() + 0.45D,
                                        (double)getBlockPos().getZ() + 0.5 +
                                                serverLevel.getRandom().nextDouble() / 2.0 *
                                                        (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                        7,0,0,0,serverLevel.getRandom().nextFloat() * 0.07f);
                            }
                        }
                    }
                }

                // if heat dispersion is active, then when the heat is at the ideal temperature, keep it at that temperature
                if(heatAmount > maxHeatWhenCooling){
                    if(heatDispersionModules > 0){
                        heatAmount = (maxHeatWhenCooling + heatDispersionModules);
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
                if(heatAmount >= heatAmountToWarnAt && heatAmount < heatAmountToGoBoomAt && isActive && isFormed){
                    if(level != null){
                        if(level instanceof ServerLevel serverLevel){
                            if(ServerConfig.RADIOACTIVE_REACTOR_MAKES_DAMAGE_SOUNDS.getAsBoolean()){
                                float randomFloatFromServer = serverLevel.getRandom().nextFloat();
                                // if the reactor is going to explode at some point, warn players around
                                if(serverLevel.getGameTime() % Mth.randomBetweenInclusive(serverLevel.getRandom(),37,76) == 0){
                                    if(!shouldBeSilenced){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.HEAVY_CORE_BREAK,
                                                SoundSource.BLOCKS);
                                    }

                                    serverLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                                            (double)getBlockPos().getX() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                            (double)getBlockPos().getY() + 0.45D,
                                            (double)getBlockPos().getZ() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                            7,0,0,0,randomFloatFromServer * 0.3f);
                                }
                                // sizzle, sizzle
                                if(!shouldBeSilenced){
                                    if(serverLevel.getGameTime() % Mth.randomBetweenInclusive(serverLevel.getRandom(), 25,91) == 0){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.FIRE_EXTINGUISH,
                                                SoundSource.BLOCKS,randomFloatFromServer * 0.5f,randomFloatFromServer * 0.91f);
                                    }
                                }

                                if(serverLevel.getGameTime() % Mth.randomBetweenInclusive(serverLevel.getRandom(),15,50) == 0 && heatAmount > 12501){
                                    if(!shouldBeSilenced){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.ANVIL_LAND,
                                                SoundSource.BLOCKS,
                                                0.1f,Utility.nextFloatBetweenInclusive(0.25f,0.61f));
                                    }

                                    BlockPos randomPosForParticles = neighborPositions.get(serverLevel.getRandom().nextIntBetweenInclusive(0,neighborPositions.size() - 1));

                                    int selectedParticle = serverLevel.getRandom().nextIntBetweenInclusive(0,2);
                                    if(selectedParticle == 0){
                                        serverLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                                                (double)randomPosForParticles.getX() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                                (double)randomPosForParticles.getY() + 0.45D,
                                                (double)randomPosForParticles.getZ() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                                3,0,0,0,randomFloatFromServer * 0.2f);
                                    }
                                    else if(selectedParticle == 1){
                                        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
                                                (double)randomPosForParticles.getX() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.54 : 1.54),
                                                (double)randomPosForParticles.getY() + 0.45D,
                                                (double)randomPosForParticles.getZ() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.54 : 1.54),
                                                1,0,0,0,0);
                                    }
                                    else if(selectedParticle == 2){
                                        serverLevel.sendParticles(ParticleTypes.SNEEZE,
                                                (double)randomPosForParticles.getX() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                                (double)randomPosForParticles.getY() + 0.45D,
                                                (double)randomPosForParticles.getZ() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                                1,0,0,0,randomFloatFromServer * 0.02f);
                                    }
                                }

                                if(heatAmount > 13500){
                                    if(serverLevel.getGameTime() % 2==0){
                                        serverLevel.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                                                (double)getBlockPos().getX() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                                (double)getBlockPos().getY() + 0.75D,
                                                (double)getBlockPos().getZ() + 0.5 + serverLevel.getRandom().nextDouble() / 2.0 * (serverLevel.getRandom().nextBoolean() ? -1.25 : 1.25),
                                                1,0,0,0,randomFloatFromServer * 0.01f);
                                    }
                                }
                                if(heatAmount > 13975){
                                    if (serverLevel.getGameTime() % 5 == 0) {
                                        double d0 = (double)getBlockPos().getX() + serverLevel.getRandom().nextDouble();
                                        double d1 = (double)getBlockPos().getY() + 1.0;
                                        double d2 = (double)getBlockPos().getZ() + serverLevel.getRandom().nextDouble();
                                        serverLevel.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0, 0.01, 0.0);
                                    }
                                }

                                if(!shouldBeSilenced){
                                    // the alarms will start sounding
                                    if(serverLevel.getGameTime() % 20 == 0 && heatAmount < 14000){
                                        // consider resupplying the reactor with coolant
                                        if(heatAmount < 12000 && heatAmount >= 11000){
                                            serverLevel.playSound(null,
                                                    getBlockPos(),
                                                    SoundEvents.NOTE_BLOCK_PLING.value(),
                                                    SoundSource.BLOCKS,0.4f,0.77f);
                                        } // heat level excessive
                                        else if(heatAmount < 13000 && heatAmount >= 12000){
                                            serverLevel.playSound(null,
                                                    getBlockPos(),
                                                    SoundEvents.NOTE_BLOCK_PLING.value(),
                                                    SoundSource.BLOCKS,0.4f,0.86f);
                                        } // heat levels are too high
                                        else if(heatAmount < 14000 && heatAmount >= 13000){
                                            serverLevel.playSound(null,
                                                    getBlockPos(),
                                                    SoundEvents.NOTE_BLOCK_PLING.value(),
                                                    SoundSource.BLOCKS,0.5f,0.91f);
                                        }
                                    } // the heat level is reaching a critical stage
                                    else if(serverLevel.getGameTime() % 10 == 0 && heatAmount > 14000 && heatAmount < 14500){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.NOTE_BLOCK_PLING.value(),
                                                SoundSource.BLOCKS,0.7f,1.25f);
                                    } // better do something now
                                    else if(serverLevel.getGameTime() % 4 == 0 && heatAmount > 14500 && heatAmount < 14900){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.NOTE_BLOCK_PLING.value(),
                                                SoundSource.BLOCKS,1.0f,1.5f);
                                    } // it's over
                                    else if(serverLevel.getGameTime() % 2 == 0 && heatAmount >= 14900){
                                        serverLevel.playSound(null,
                                                getBlockPos(),
                                                SoundEvents.NOTE_BLOCK_PLING.value(),
                                                SoundSource.BLOCKS,1.0f,2.0f);
                                    }
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
                            // you will lose items
                            if(ServerConfig.RADIOACTIVE_REACTOR_EXPLODES_WHEN_TOO_HOT.getAsBoolean()){
                                int wasteTankAmount = wasteGasTank.getGasAmount();
                                this.invalidateCapabilities(); // the block will not exist, so invalidate interactions with it
                                serverLevel.setBlock(getBlockPos(),Blocks.AIR.defaultBlockState(),3);
                                serverLevel.explode(null,
                                        getBlockPos().getX(),
                                        getBlockPos().getY(),
                                        getBlockPos().getZ(),
                                        10.0f,
                                        true,
                                        Level.ExplosionInteraction.BLOCK);
                                // if there was any waste in the gas tank then release it
                                if(wasteTankAmount > 0){
                                    serverLevel.setBlock(getBlockPos(),Blocks.WATER.defaultBlockState(),3);
                                }
                                return;
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
                    return; // the reactor is toast, stop code execution from going any farther
                }
            }
            distributeProducts(); // try sending waste out
        }
    }

    // access in 3d what the required pattern is for reference
    @SuppressWarnings("unused")
    public BlockPattern multiBlockPattern(){
        BlockPattern reactorPattern =
        BlockPatternBuilder.start()
                .aisle("       ",
                        "       ",
                        "   #   ",
                        "  #C#  ",
                        "   #   ",
                        "       ",
                        "       ")
                .aisle("       ",
                        "       ",
                        "   #   ",
                        "  ###  ",
                        "   #   ",
                        "       ",
                        "       ")
                .aisle("       ",
                        "       ",
                        "   #   ",
                        "  # #  ",
                        "   #   ",
                        "       ",
                        "       ")
                .where('#',BlockInWorld.hasState(BlockPredicate.forBlock(CmatdBlock.RADIOACTIVE_REACTOR_MULTIBLOCK_CASING.get())))
                .where('C',BlockInWorld.hasState(BlockPredicate.forBlock(CmatdBlock.RADIOACTIVE_REACTOR_MULTIBLOCK.get()))).build();
        return reactorPattern;
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
            if(stack.is(Tag.VALID_RADIOACTIVE_FUELS)){
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
                    if(stack.has(DataComponents.DAMAGE) && stack.has(Components.COOLANT) && serverLevel.getRandom().nextIntBetweenInclusive(0,100) <= 2){
                        coolantAmount++;
                        // don't modify item stacks directly, make a copy
                        ItemStack copiedStack = stack.copy();
                        int damage = stack.getDamageValue();
                        int maxDamage = stack.getMaxDamage();
                        // clamp this value as otherwise the machine cannot save, as damage must be greater than or equal to 0
                        copiedStack.set(DataComponents.DAMAGE,Mth.clamp(damage - 1,0,maxDamage));
                        if(copiedStack.getDamageValue() <= 0){
                            itemStackHandler.setStackInSlot(index,ItemStack.EMPTY);
                        }
                        else{
                            itemStackHandler.setStackInSlot(index,copiedStack);
                        }
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
                        wasteGasTank.setGas(new GasStack(wasteGasTank.getGasStack().getGas(),Mth.clamp(wasteGasTank.getGasAmount() + (1000 / (1 + efficiencyModules)),0,wasteGasTank.getCapacity())),true);
                        wasteConversionToFluidTank.setFluid(new FluidStack(Fluids.WATER,wasteGasTank.getGasAmount()));
                        wasteGasTank.update();
                    }
                    else{
                        wasteGasTank.setGas(new GasStack(Gases.RADIOACTIVE_WASTE,1000 / (1 + efficiencyModules)),true);
                        wasteConversionToFluidTank.setFluid(new FluidStack(Fluids.WATER,wasteGasTank.getGasAmount()));
                        wasteGasTank.update();
                    }
                    processBits = 0;
                    isProcessing = false;
                    updateBlock();
                }
                // we are full if this area is reached, but don't punish for having too much waste
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
