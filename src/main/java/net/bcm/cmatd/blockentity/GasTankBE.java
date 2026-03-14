package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.api.*;
import net.bcm.cmatd.block.custom.GasVent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GasTankBE extends AbstractGasContainingBE{
    private GasTank gasTank = new GasTank(100000){
        @Override
        public void update() {
            setChanged();
            if(!level.isClientSide){
                level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            }
        }
    };
    private GasContainerData gasContainerData;
    public int ticks;
    public int gasAmount = 0;

    public GasTank getGasTank(){
        return gasTank;
    }
    public GasContainerData getGasContainerData(){
        return gasContainerData;
    }

    public GasTankBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.GAS_TANK.get(), pos, blockState);
        this.gasContainerData = new GasContainerData(this);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag,lookupProvider);
    }

    public int getGasAmount() {
        return this.gasAmount;
    }

    public void clientTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
        }

        if(ticks % 5 == 0){
            this.gasAmount = gasTank.gas.getAmount();
            setChanged();
        }
    }

    public void serverTick() {
        ticks++;

        if(ticks % 7 == 0){
            this.gasAmount = gasTank.gas.getAmount();
            setChanged();
        }

        if(ticks % 17 == 0){
            if(gasTank.canGasBeDrainedFromTank(gasTank.getGasStack())){
                distributeGas();
            }
        }

        if(ticks % 27 == 0){
            if(level.getBlockState(getBlockPos().below()).getBlock() instanceof GasVent gasVent){
                if(gasTank.getGasAmount() <= 0 && gasTank.getGasStack().getGas() == Gases.EMPTY){
                    gasTank.setGas(gasVent.getDefaultGasStack(),false);
                    level.playSound(null,getBlockPos(),
                            SoundEvents.BUCKET_FILL_LAVA,SoundSource.BLOCKS,
                            0.75f,Mth.nextFloat(level.getRandom(),0.95f,1.1f));
                    setChanged();
                }
                else{
                    if(gasTank.getGasStack().getGas() == gasVent.gasTypeToProduce.value()){
                        if(gasTank.getGasAmount() < gasTank.capacity){
                            gasTank.fill(gasVent.gasAmountToProduce.sample(level.getRandom()));
                            if(level.getRandom().nextIntBetweenInclusive(1,12) <= 2){
                                level.playSound(null,getBlockPos(),
                                        SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON,SoundSource.BLOCKS,
                                        0.75f,Mth.nextFloat(level.getRandom(),0.95f,1.1f));
                            }
                            setChanged();
                        }
                    }
                }
            }
        }

        if (ticks > 32767) {
            ticks = 0;
        }
    }

    public void distributeGas(){
        for (Direction direction : Direction.values()){

            if (gasTank.getGasAmount() <= 0) {
                return;
            }
            // get blocks that are energy capable
            boolean testForTestBlock = level.getBlockEntity(getBlockPos().relative(direction)) instanceof TestGasGeneratorBE;
            if(!testForTestBlock){
                IGasHandler handler = level.getCapability(Capabilities.GasHandler.BLOCK,
                        getBlockPos().relative(direction), null);
                if (handler != null) {
                    if (handler.getGasTanks() >= 1) {
                        int received = handler.fill(gasTank.gas,false);
                        this.gasTank.drain(received, false);
                        setChanged();
                    }
                }
            }
        }
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        gasTank.save(registries,tag);
        tag.putInt("gas_amount",gasAmount);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        gasTank.load(registries,tag);
        gasAmount = tag.getInt("gas_amount");
    }
}
