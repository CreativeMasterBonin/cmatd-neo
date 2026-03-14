package net.bcm.cmatd;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CmatdSound{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT,Cmatd.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> HEATER_LOOP = SOUND_EVENTS.register(
            "heater_loop",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"heater_loop")));

    public static final DeferredHolder<SoundEvent, SoundEvent> PROCESSOR_LOOP = SOUND_EVENTS.register(
            "processor_loop",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"processor_loop")));

    public static final DeferredHolder<SoundEvent, SoundEvent> MASHER = SOUND_EVENTS.register(
            "masher",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"masher")));

    public static final DeferredHolder<SoundEvent, SoundEvent> MASHER_METALLIC = SOUND_EVENTS.register(
            "masher_metallic",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"masher_metallic")));

    public static final DeferredHolder<SoundEvent, SoundEvent> REACTOR_LOOP = SOUND_EVENTS.register(
            "reactor_loop",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"reactor_loop")));

    public static final DeferredHolder<SoundEvent, SoundEvent> ENGINE_LOOP = SOUND_EVENTS.register(
            "engine_loop",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"engine_loop")));

    public static final DeferredHolder<SoundEvent, SoundEvent> ROTATING_LOOP = SOUND_EVENTS.register(
            "rotating_loop",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"rotating_loop")));
}
