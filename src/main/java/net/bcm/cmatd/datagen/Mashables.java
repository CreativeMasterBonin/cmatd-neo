package net.bcm.cmatd.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record Mashables(Item outputItem){
    public static final Codec<Mashables> MASHABLE_CODEC = BuiltInRegistries.ITEM.byNameCodec()
            .xmap(Mashables::new, Mashables::outputItem);

    public static final Codec<Mashables> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(in -> in.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("converts_to_item").forGetter(Mashables::outputItem))
                    .apply(in, Mashables::new)),
            MASHABLE_CODEC);
}
