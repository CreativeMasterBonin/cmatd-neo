package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.block.custom.ConduitCableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

public class FacadeConduitBE extends ConduitBE {
    public BlockState facadeState = Blocks.AIR.defaultBlockState();

    public FacadeConduitBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.FACADE_CONDUIT.get(), pos, blockState);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);

        if(level.isClientSide){
            level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        if(facadeState != null) {
            CompoundTag tag2 = NbtUtils.writeBlockState(facadeState);
            tag.put("facade", tag2);
        }
        saveAdditional(tag,registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag,lookupProvider);
    }

    public BlockState getFacadeBlock() {
        return facadeState;
    }

    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(ConduitCableBlock.FACADE_ID,facadeState).build();
    }

    public void setFacadeState(BlockState newFacadeState){
        this.facadeState = newFacadeState;
        setChanged();
        level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("facade")) {
            facadeState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(),tag.getCompound("facade"));
        }
        else {
            facadeState = Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (facadeState != null) {
            CompoundTag tag2 = NbtUtils.writeBlockState(facadeState);
            tag.put("facade", tag2);
        }
    }
}
