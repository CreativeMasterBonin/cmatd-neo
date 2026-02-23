package net.bcm.cmatd.network;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.blockentity.BaseCobbleMakerBE;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BaseCobbleMakerTierUpdatePacket{
    public static final BaseCobbleMakerTierUpdatePacket INSTANCE = new BaseCobbleMakerTierUpdatePacket();

    public static BaseCobbleMakerTierUpdatePacket get(){
        return INSTANCE;
    }

    public void handle(final BaseCobbleMakerTierUpdatePayload payload, final IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            Player ply = ctx.player();
            if(ply == null){
                return;
            }
            Level level = ctx.player().level();
            if(!level.hasChunkAt(payload.bp())){
                return;
            }
            BlockState bs = level.getBlockState(payload.bp());
            BaseCobbleMakerBE be = (BaseCobbleMakerBE)level.getBlockEntity(payload.bp());
            try {
                if(level != null && be != null){
                    be.setNewTierSettings(payload.machine_tier(),payload.energy_capacity(),
                            payload.max_receive(),payload.max_extract(),payload.modules_allowed(),
                            payload.energy_gen_rate());
                }
            }
            catch (Exception e){
                Cmatd.getLogger().error("Packet for Base Cobble Maker Tier Update error: {} : packet from pos: {}", e.getMessage(),
                        payload.bp().toShortString());
            }
        });
    }
}
