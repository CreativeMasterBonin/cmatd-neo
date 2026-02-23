package net.bcm.cmatd.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record Jammables(Item outputItem) {
    // boiled to pulp with sugar
    public static final Codec<Jammables> JAMMABLES_CODEC = BuiltInRegistries.ITEM.byNameCodec()
            .xmap(Jammables::new, Jammables::outputItem);

    public static final Codec<Jammables> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(in -> in.group(
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("converts_to_item").forGetter(Jammables::outputItem))
                    .apply(in, Jammables::new)),
            JAMMABLES_CODEC);
}
