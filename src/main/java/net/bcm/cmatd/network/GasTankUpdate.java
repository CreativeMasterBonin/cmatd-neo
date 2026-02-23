package net.bcm.cmatd.network;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.api.GasStack;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.blockentity.GasTankBE;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GasTankUpdate(BlockPos pos, boolean dumpGas) implements CustomPacketPayload{
    public static final Type<GasTankUpdate> GAS_TANK_UPDATE_TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"gas_tank_update"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return GAS_TANK_UPDATE_TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, GasTankUpdate> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, GasTankUpdate::pos,
                    ByteBufCodecs.BOOL,GasTankUpdate::dumpGas,
                    GasTankUpdate::new);

    public static void handle(GasTankUpdate message, IPayloadContext ctx) {
        var level = ctx.player().level();
        if (level.isLoaded(message.pos)) {
            if (level.getBlockEntity(message.pos) instanceof GasTankBE be) {
                if(message.dumpGas){
                    be.getGasTank().dumpGas(ctx.player());
                    be.setChanged();
                }
            }
        }
    }
}
