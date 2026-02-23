package net.bcm.cmatd.network;

import net.bcm.cmatd.blockentity.BaseEnergyMakerBE;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CmatdEnergyPacket(BlockPos pos, int currentEnergy, int currentProduction) implements CustomPacketPayload{
    public static final Type<CmatdEnergyPacket> ENERGY_PACKET_TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("cmatd","energy_update"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ENERGY_PACKET_TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, CmatdEnergyPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, CmatdEnergyPacket::pos,
                    ByteBufCodecs.VAR_INT, CmatdEnergyPacket::currentEnergy,
                    ByteBufCodecs.VAR_INT, CmatdEnergyPacket::currentProduction,
                    CmatdEnergyPacket::new);

    public static void handle(CmatdEnergyPacket message, IPayloadContext ctx) {
        var level = ctx.player().level();
        if (level.isLoaded(message.pos)) {
            if (level.getBlockEntity(message.pos) instanceof BaseEnergyMakerBE be) {
                be.energy = message.currentEnergy;
                be.energyProduction = message.currentProduction;
            }
        }
    }
}
