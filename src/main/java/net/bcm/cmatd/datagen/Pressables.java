package net.bcm.cmatd.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record Pressables(Item inputItem, Item pressItem, Item outputItem, int outputCount) {
    public static final Codec<Pressables> CODEC = RecordCodecBuilder.create(
            codecOf -> codecOf.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("input_item").forGetter(Pressables::inputItem),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("press_item").forGetter(Pressables::pressItem),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("output_item").forGetter(Pressables::outputItem),
                    Codec.INT.fieldOf("output_count").forGetter(Pressables::outputCount)
            ).apply(codecOf,Pressables::new));
}
