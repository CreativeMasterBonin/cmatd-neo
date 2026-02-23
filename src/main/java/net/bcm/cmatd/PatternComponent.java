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
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record PatternComponent(String pattern_name, Holder<Item> consumed_item, Holder<Item> producing_item, int item_amount_produced){
    public static final Codec<PatternComponent> CODEC = RecordCodecBuilder.create(
        kindof -> kindof.group(
                Codec.STRING.fieldOf("pattern_name").forGetter(PatternComponent::pattern_name),
                RegistryFixedCodec.create(Registries.ITEM).fieldOf("consumed_item").forGetter(PatternComponent::consumed_item),
                RegistryFixedCodec.create(Registries.ITEM).fieldOf("producing_item").forGetter(PatternComponent::producing_item),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("item_amount_produced").forGetter(PatternComponent::item_amount_produced)
        ).apply(kindof,PatternComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,PatternComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,PatternComponent::pattern_name,
            ByteBufCodecs.holderRegistry(Registries.ITEM),PatternComponent::consumed_item,
            ByteBufCodecs.holderRegistry(Registries.ITEM),PatternComponent::producing_item,
            ByteBufCodecs.VAR_INT,PatternComponent::item_amount_produced,
            PatternComponent::new
    );

    public static PatternComponent create(String name,Item consumedItem, Item producedItem, int producedItemCount){
        return new PatternComponent(name,BuiltInRegistries.ITEM.wrapAsHolder(consumedItem),
                BuiltInRegistries.ITEM.wrapAsHolder(producedItem),producedItemCount);
    }
}
