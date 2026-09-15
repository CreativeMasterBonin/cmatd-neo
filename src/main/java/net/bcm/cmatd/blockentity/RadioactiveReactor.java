package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.api.GasTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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

    public final ItemStackHandler itemStackHandler = new ItemStackHandler(35){
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if(slot > 30){
                return stack.has(Components.MODULE_TYPE);
            }
            return super.isItemValid(slot, stack);
        }
    };
    public int gasAmount = 0;
    public RadioactiveReactorGasContainerData gasContainerData;
    public RadioactiveReactorFluidContainerData fluidContainerData;

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
        wasteGasTank.load(registries,tag);
        if(tag.contains("gas_amount")){
            gasAmount = tag.getInt("gas_amount");
        }
        wasteConversionToFluidTank.readFromNBT(registries,tag);
        if(tag.contains("stored_energy")){
            battery.setEnergy(tag.getInt("stored_energy"));
        }
    }

    @Override
    public void saveExtraValues(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("formed",isFormed);
        wasteGasTank.save(registries,tag);
        tag.putInt("gas_amount",gasAmount);
        wasteConversionToFluidTank.writeToNBT(registries,tag);
        tag.putInt("stored_energy",battery.getEnergyStored());
    }

    @Override
    public void extraServerTick() {
        if(level != null){
            // multiblock is not formed, check if it should be
            if(!isFormed){
                // if multiblock can be formed, then set as formed
                if(canForm()){
                    isFormed = true;
                    updateBlock();
                }
            }
            else{
                if(!canForm()){
                    isFormed = false;
                    updateBlock();
                }
            }
        }
    }
}
