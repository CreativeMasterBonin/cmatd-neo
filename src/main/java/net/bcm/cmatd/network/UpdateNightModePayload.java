package net.bcm.cmatd.network;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpdateNightModePayload(BlockPos position, boolean shouldBeNightMode) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateNightModePayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"update_night_mode_status"));

    public static final StreamCodec<FriendlyByteBuf,UpdateNightModePayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,UpdateNightModePayload::position,
                    ByteBufCodecs.BOOL,UpdateNightModePayload::shouldBeNightMode,
                    UpdateNightModePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
