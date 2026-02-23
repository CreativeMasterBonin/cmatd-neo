package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.Utility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.checkerframework.checker.units.qual.C;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ConduitBE extends BlockEntity {
    //public static final String ENERGY_TAG = "energy";
    private Set<BlockPos> positions = null;

    private final BaseEnergyStorage energyStorage = new BaseEnergyStorage(Utility.MAX_CONDUIT_ENERGY_CAPACITY,
            Utility.MAX_CONDUIT_ENERGY_TRANSFER_RATE,Utility.MAX_CONDUIT_ENERGY_TRANSFER_RATE,0){
        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            if(toReceive < 0){
                return 0;
            }

            int energyReceived = Mth.clamp(this.capacity - this.energy, 0, Math.min(this.maxReceive, toReceive));
            if (!simulate)
                this.energy += energyReceived;
            return energyReceived;
        }
    };

    // position checks for outputs (adds refreshed positions to set for an entire network)
    // TODO: NOT EFFICIENT, so try upgrading it please
    // mcjty
    private void traverse(BlockPos pos, Consumer<ConduitBE> consumer) {
        Set<BlockPos> traversed = new HashSet<>();
        traversed.add(pos);
        consumer.accept(this);
        traverse(pos, traversed, consumer);
    }

    private void traverse(BlockPos pos, Set<BlockPos> traversed, Consumer<ConduitBE> consumer) {
        for (Direction direction : Direction.values()) {
            BlockPos p = pos.relative(direction);
            if (!traversed.contains(p)) {
                traversed.add(p);
                if (level.getBlockEntity(p) instanceof ConduitBE cable) {
                    consumer.accept(cable);
                    cable.traverse(p, traversed, consumer);
                }
            }
        }
    }

    private void refreshOutputs(){
        if(positions == null){
            positions = new HashSet<>();
            traverse(worldPosition, cable -> {
                // check for energy storage compats but not conduits! then add them to our positions
                for (Direction direction : Direction.values()) {
                    BlockPos p = cable.getBlockPos().relative(direction);
                    BlockEntity te = level.getBlockEntity(p);
                    if (te != null) {
                        IEnergyStorage handler = level.getCapability(Capabilities.EnergyStorage.BLOCK, p, null);
                        if (handler != null) {
                            if (handler.canReceive()) {
                                positions.add(p);
                                setChanged();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag,registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        loadAdditional(tag,lookupProvider);
    }

    public void notUpdatedRefresh(){
        traverse(worldPosition,conduitBE -> conduitBE.positions = null);
        setChanged();
    }

    public BaseEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    // end mcjty

    public ConduitBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.CONDUIT.get(), pos, blockState);
    }

    public ConduitBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState){
        super(type,pos,blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("energy",energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if(tag.contains("energy")){
            energyStorage.setEnergy(tag.getInt("energy"));
        }
    }

    public void serverTick(){
        if (energyStorage.getEnergyStored() > 0) {
            // energy needed to traverse with it!
            refreshOutputs();

            BlockPos above = getBlockPos().above();
            BlockPos below = getBlockPos().below();
            BlockPos north = getBlockPos().north();
            BlockPos south = getBlockPos().south();
            BlockPos east = getBlockPos().east();
            BlockPos west = getBlockPos().west();
            /*
            if(getLevel() != null){
                Block aboveBlockType = getLevel().getBlockState(above).getBlock();
                Block belowBlockType = getLevel().getBlockState(below).getBlock();
                Block northBlockType = getLevel().getBlockState(north).getBlock();
                Block southBlockType = getLevel().getBlockState(south).getBlock();
                Block eastBlockType = getLevel().getBlockState(east).getBlock();
                Block westBlockType = getLevel().getBlockState(west).getBlock();
                Block selfBlock = this.getBlockState().getBlock();
                boolean isValidAbove = getLevel().getCapability(Capabilities.EnergyStorage.BLOCK,above,null) != null;
                boolean isValidBelow = getLevel().getCapability(Capabilities.EnergyStorage.BLOCK,below,null) != null;
                boolean isValidNorth = getLevel().getCapability(Capabilities.EnergyStorage.BLOCK,north,null) != null;
                boolean isValidSouth = getLevel().getCapability(Capabilities.EnergyStorage.BLOCK,south,null) != null;
                boolean isValidEast = getLevel().getCapability(Capabilities.EnergyStorage.BLOCK,east,null) != null;
                boolean isValidWest = getLevel().getCapability(Capabilities.EnergyStorage.BLOCK,west,null) != null;

                if(aboveBlockType == selfBlock || isValidAbove){
                    IEnergyStorage handlerXtra = level.getCapability(Capabilities.EnergyStorage.BLOCK,above,null);
                    if(handlerXtra != null){
                        if(handlerXtra.canReceive()){
                            int received = handlerXtra.receiveEnergy(Utility.MAX_CONDUIT_ENERGY_TRANSFER_RATE, false);
                            energyStorage.extractEnergy(received,false);
                            setChanged();
                        }
                    }
                }
                if(belowBlockType == selfBlock || isValidBelow){
                    IEnergyStorage handlerXtra = level.getCapability(Capabilities.EnergyStorage.BLOCK,below,null);
                    if(handlerXtra != null){
                        if(handlerXtra.canReceive()){
                            int received = handlerXtra.receiveEnergy(Utility.MAX_CONDUIT_ENERGY_TRANSFER_RATE, false);
                            energyStorage.extractEnergy(received,false);
                            setChanged();
                        }
                    }
                }
                if(northBlockType == selfBlock || isValidNorth){
                    IEnergyStorage handlerXtra = level.getCapability(Capabilities.EnergyStorage.BLOCK,north,null);
                    if(handlerXtra != null){
                        if(handlerXtra.canReceive()){
                            int received = handlerXtra.receiveEnergy(Utility.MAX_CONDUIT_ENERGY_TRANSFER_RATE, false);
                            energyStorage.extractEnergy(received,false);
                            setChanged();
                        }
                    }
                }
                if(southBlockType == selfBlock || isValidSouth){
                    IEnergyStorage handlerXtra = level.getCapability(Capabilities.EnergyStorage.BLOCK,south,null);
                    if(handlerXtra != null){
                        if(handlerXtra.canReceive()){
                            int received = handlerXtra.receiveEnergy(Utility.MAX_CONDUIT_ENERGY_TRANSFER_RATE, false);
                            energyStorage.extractEnergy(received,false);
                            setChanged();
                        }
                    }
                }
                if(eastBlockType == selfBlock || isValidEast){
                    IEnergyStorage handlerXtra = level.getCapability(Capabilities.EnergyStorage.BLOCK,east,null);
                    if(handlerXtra != null){
                        if(handlerXtra.canReceive()){
                            int received = handlerXtra.receiveEnergy(Utility.MAX_CONDUIT_ENERGY_TRANSFER_RATE, false);
                            energyStorage.extractEnergy(received,false);
                            setChanged();
                        }
                    }
                }
                if(westBlockType == selfBlock || isValidWest){
                    IEnergyStorage handlerXtra = level.getCapability(Capabilities.EnergyStorage.BLOCK,west,null);
                    if(handlerXtra != null){
                        if(handlerXtra.canReceive()){
                            int received = handlerXtra.receiveEnergy(Utility.MAX_CONDUIT_ENERGY_TRANSFER_RATE, false);
                            energyStorage.extractEnergy(received,false);
                            setChanged();
                        }
                    }
                }
            }*/

            if (!positions.isEmpty()) {
                // distribute energy to all connections in our positions set
                int amount = energyStorage.getEnergyStored() / positions.size();
                for (BlockPos p : positions) {
                    IEnergyStorage handler = level.getCapability(Capabilities.EnergyStorage.BLOCK, p, null);
                    if (handler != null) {
                        if (handler.canReceive()) {
                            int received = handler.receiveEnergy(amount, false);
                            energyStorage.extractEnergy(received, false);
                            setChanged();
                        }
                    }
                }
            }
        }
    }
}
