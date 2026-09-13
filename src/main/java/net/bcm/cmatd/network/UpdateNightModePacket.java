package net.bcm.cmatd.network;

import net.bcm.cmatd.blockentity.JamMakerBE;
import net.bcm.cmatd.blockentity.PresserBE;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateNightModePacket {
    public static final UpdateNightModePacket INSTANCE = new UpdateNightModePacket();

    public static UpdateNightModePacket get(){
        return INSTANCE;
    }

    public void handle(final UpdateNightModePayload payload, final IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            Player ply = ctx.player();
            if(ply == null){
                return;
            }
            Level level = ctx.player().level();
            if(!level.hasChunkAt(payload.position())){
                return;
            }
            BlockState bs = level.getBlockState(payload.position());
            if(level.getBlockEntity(payload.position()) instanceof JamMakerBE jamMakerBE){
                jamMakerBE.setNightMode();
                jamMakerBE.updateBlock();
                return;
            }
            else if(level.getBlockEntity(payload.position()) instanceof PresserBE presserBE){
                presserBE.setNightMode();
                presserBE.updateBlock();
                return;
            }
        });
    }
}
