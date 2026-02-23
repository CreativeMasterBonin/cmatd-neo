package net.bcm.cmatd.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record FoodReactorWrenchUpdate(BlockPos pos) implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<FoodReactorWrenchUpdate> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("cmatd","food_reactor_wrench_update"));

    public static final StreamCodec<FriendlyByteBuf, FoodReactorWrenchUpdate> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, FoodReactorWrenchUpdate::pos,
                    FoodReactorWrenchUpdate::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
