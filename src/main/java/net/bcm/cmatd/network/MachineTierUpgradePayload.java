package net.bcm.cmatd.network;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record MachineTierUpgradePayload(BlockPos position, int tierToUpgradeTo) implements CustomPacketPayload {
    // this is separate from machine tier components
    public static final CustomPacketPayload.Type<MachineTierUpgradePayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"machine_tier_upgrade"));

    public static final StreamCodec<FriendlyByteBuf,MachineTierUpgradePayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,MachineTierUpgradePayload::position,
                    ByteBufCodecs.INT,MachineTierUpgradePayload::tierToUpgradeTo,
                    MachineTierUpgradePayload::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
