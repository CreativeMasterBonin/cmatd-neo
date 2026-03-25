package net.bcm.cmatd.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DimensionalTransporterBE extends BlockEntity {
    public List<Level> stems;
    public String levelToTeleportToName = Level.OVERWORLD.registry().toString();

    public DimensionalTransporterBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.DIMENSIONAL_TRANSPORTER.get(), pos, blockState);
    }

    @Override
    public void onLoad(){
        super.onLoad();
        if(level != null && level instanceof ServerLevel serverLevel){
            if(stems == null){
                stems = serverLevel.registryAccess().registryOrThrow(Registries.DIMENSION).stream().toList();
                setChanged();
            }
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
        saveAdditional(tag,registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag,lookupProvider);
    }

    public void teleportPlayer(MinecraftServer server, ServerPlayer player){

    }

    public void updateDimensionDestination(int index){
        if(stems == null){
            levelToTeleportToName = Level.OVERWORLD.registry().toString();
        }
        levelToTeleportToName = stems.get(index).dimension().registry().toString();
        setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putString("level_to_teleport_to",levelToTeleportToName);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if(tag.contains("level_to_teleport_to")){
            levelToTeleportToName = tag.getString("level_to_teleport_to");
        }
        else{
            levelToTeleportToName = Level.OVERWORLD.registry().toString();
        }
    }
}
