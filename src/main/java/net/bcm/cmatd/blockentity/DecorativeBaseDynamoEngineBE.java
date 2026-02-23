package net.bcm.cmatd.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DecorativeBaseDynamoEngineBE extends BlockEntity{
    public int ticks;

    public DecorativeBaseDynamoEngineBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.DECORATIVE_BASE_DYNAMO_ENGINE.get(), pos, blockState);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void serverTick(Level lvl, BlockPos bp, BlockState bs, DecorativeBaseDynamoEngineBE be){
        ++be.ticks;
        if(be.ticks >= 32767){
            be.ticks = 0;
        }
    }
    public static void clientTick(Level lvl, BlockPos bp, BlockState bs, DecorativeBaseDynamoEngineBE be){
        ++be.ticks;
        if(be.ticks >= 32767){
            be.ticks = 0;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }
}
