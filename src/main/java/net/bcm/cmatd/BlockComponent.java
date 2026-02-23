package net.bcm.cmatd;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.block.Block;

public record BlockComponent(Holder<Block> block) {
    public static final Codec<BlockComponent> CODEC = RecordCodecBuilder.create(
            kindof -> kindof.group(
                    RegistryFixedCodec.create(Registries.BLOCK).fieldOf("block").forGetter(BlockComponent::block)
            ).apply(kindof, BlockComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,BlockComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.BLOCK),BlockComponent::block,
            BlockComponent::new
    );

    public static BlockComponent create(Block block){
        return new BlockComponent(BuiltInRegistries.BLOCK.wrapAsHolder(block));
    }
}
