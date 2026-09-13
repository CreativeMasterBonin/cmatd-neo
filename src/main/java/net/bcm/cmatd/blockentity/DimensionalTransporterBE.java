package net.bcm.cmatd.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DimensionalTransporterBE extends BlockEntity {
    public String levelToTeleportToName = Level.OVERWORLD.toString();
    public List<String> teleportedToDimensionsAtLeastOnce = List.of();
    public static final String visitedDimensionsTagName = "visited_dimension_";
    public String levelWeAreIn = Level.OVERWORLD.toString();

    public DimensionalTransporterBE(BlockPos pos, BlockState blockState) {
        super(CmatdBE.DIMENSIONAL_TRANSPORTER.get(), pos, blockState);
    }

    @Override
    public void onLoad(){
        super.onLoad();
        if(level != null && level instanceof ServerLevel serverLevel){
            levelWeAreIn = serverLevel.dimension().toString();
        }
    }

    public void updateBlock(){
        this.setChanged();
        if(this.level != null){
            this.level.sendBlockUpdated(this.getBlockPos(),this.getBlockState(),this.getBlockState(),3);
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
        ResourceKey<Level> defaultDimension = server.overworld().dimension();
        boolean couldNotFindDimensionKey = false;

        for(ServerLevel level : server.getAllLevels()){
            if(level.dimension().toString().equals(levelToTeleportToName)){
                DimensionType type = level.dimensionType();
                double coordScale = type.coordinateScale();
                int movement = 0;
                Optional<Vec3> optionalFreePos = level.findFreePosition(player, Shapes.box(-16,-16,-16,16,16,16),new Vec3(0,0,0),0,0,0);
                if(optionalFreePos.isPresent()){
                    Vec3 mutableTeleportPos = optionalFreePos.get();
                    // move the player up
                    if(optionalFreePos.get().y < level.dimensionType().minY()){
                        mutableTeleportPos = new Vec3(mutableTeleportPos.x,level.dimensionType().minY() + 63,mutableTeleportPos.z);
                    }
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,60,127,true,false));

                    Optional<BlockPos> safestPos = level.findSupportingBlock(player,player.getBoundingBox());
                    if(safestPos.isPresent()){
                        player.teleportTo(level,
                                safestPos.get().getX() * coordScale,
                                safestPos.get().getY() * coordScale,
                                safestPos.get().getZ() * coordScale,
                                RelativeMovement.unpack(movement),
                                player.getYRot(),player.getXRot()
                        );
                    }
                    else{
                        player.teleportTo(level,
                                mutableTeleportPos.x * coordScale,
                                mutableTeleportPos.y * coordScale,
                                mutableTeleportPos.z * coordScale,
                                RelativeMovement.unpack(movement),
                                player.getYRot(),player.getXRot()
                        );
                    }
                    return;
                }
                else{
                    player.teleportTo(level,0,0,0,RelativeMovement.unpack(movement),
                            player.getYRot(),player.getXRot()
                    );
                    return;
                }
            }
            else{
                couldNotFindDimensionKey = true;
            }
        }
        // dimension was not found
        if(couldNotFindDimensionKey){
            player.sendSystemMessage(Component.literal("Could not teleport: " + player + " because dimension key: " + levelToTeleportToName + " cannot be teleported to"));
        }
    }

    /**
     * This method verifies that the player has visited the requested dimension at least once, allowing them to teleport anywhere in the targeted world
     * @return Whether the player has been to the dimension at least once
     */
    public boolean hasVisitedDimensionAlready(){
        return teleportedToDimensionsAtLeastOnce.contains(levelToTeleportToName);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        // save the current targeted dimension key
        tag.putString("level_to_teleport_to",levelToTeleportToName);
        // save any dimension registries that have been visited at least once
        int dimensionOrderIndex = 0;
        for(String levelName : teleportedToDimensionsAtLeastOnce){
            tag.putString(visitedDimensionsTagName + String.valueOf(dimensionOrderIndex),levelName);
            dimensionOrderIndex++;
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        // if the targeted dimension has been saved, load the key
        if(tag.contains("level_to_teleport_to")){
            levelToTeleportToName = tag.getString("level_to_teleport_to");
        }
        else{
            levelToTeleportToName = Level.OVERWORLD.toString();
        }
        // load any dimension registries that have been visited once
        for(String key : tag.getAllKeys().stream().filter(key ->
               key.startsWith(visitedDimensionsTagName)).collect(Collectors.toSet())){
            String savedVisitedDimensionName = tag.getString(key);
            if(!savedVisitedDimensionName.isBlank() && !savedVisitedDimensionName.isEmpty()){
                this.teleportedToDimensionsAtLeastOnce.add(savedVisitedDimensionName);
            }
        }
    }
}
