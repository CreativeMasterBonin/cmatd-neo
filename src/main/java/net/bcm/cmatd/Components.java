package net.bcm.cmatd;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Components{
    private static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,Cmatd.MODID);

    public static void setup(IEventBus eventBus){
        COMPONENTS.register(eventBus);
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY =
            COMPONENTS.registerComponentType("energy", builder ->
                    builder.persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MODULE_TYPE =
            COMPONENTS.registerComponentType("module_type", builder ->
                    builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MachineTierComponent>> MACHINE_TIER =
            COMPONENTS.registerComponentType("machine_tier", builder ->
                    builder.persistent(MachineTierComponent.CODEC)
                            .networkSynchronized(MachineTierComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PatternComponent>> PATTERN =
            COMPONENTS.registerComponentType("pattern", builder ->
                    builder.persistent(PatternComponent.CODEC)
                            .networkSynchronized(PatternComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockComponent>> BLOCK =
            COMPONENTS.registerComponentType("block",builder ->
                    builder.persistent(BlockComponent.CODEC)
                            .networkSynchronized(BlockComponent.STREAM_CODEC));


}
