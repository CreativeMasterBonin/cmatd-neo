package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.CmatdSound;
import net.bcm.cmatd.Utility;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class SoundDefinitions extends SoundDefinitionsProvider{
    public SoundDefinitions(PackOutput output,ExistingFileHelper helper) {
        super(output,Cmatd.MODID,helper);
    }

    @Override
    public void registerSounds() {
        add(CmatdSound.HEATER_LOOP, SoundDefinition.definition()
                .with(sound("cmatd:heater_loop")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(Utility.MACHINE_SOUND_DISTANCE)
                        .stream(false)
                        .preload(true)
                ).subtitle("subtitle.cmatd.heater_loop").replace(false)
        );
        add(CmatdSound.PROCESSOR_LOOP, SoundDefinition.definition()
                .with(sound("cmatd:processor_loop")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(Utility.MACHINE_SOUND_DISTANCE)
                        .stream(false)
                        .preload(true)
                ).subtitle("subtitle.cmatd.processor_loop").replace(false)
        );

        add(CmatdSound.MASHER, SoundDefinition.definition()
                .with(sound("cmatd:masher")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(Utility.MACHINE_SOUND_DISTANCE)
                        .stream(false)
                        .preload(true)
                ).subtitle("subtitle.cmatd.masher").replace(false)
        );

        add(CmatdSound.MASHER_METALLIC, SoundDefinition.definition()
                .with(sound("cmatd:masher_metallic")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(Utility.MACHINE_SOUND_DISTANCE)
                        .stream(false)
                        .preload(true)
                ).subtitle("subtitle.cmatd.masher_metallic").replace(false)
        );

        add(CmatdSound.REACTOR_LOOP, SoundDefinition.definition()
                .with(sound("cmatd:reactor_loop")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(Utility.MACHINE_SOUND_DISTANCE)
                        .stream(false)
                        .preload(true)
                ).subtitle("subtitle.cmatd.reactor_loop").replace(false)
        );

        add(CmatdSound.ENGINE_LOOP, SoundDefinition.definition()
                .with(sound("cmatd:engine_loop")
                        .volume(0.9f)
                        .pitch(1.0f)
                        .attenuationDistance(Utility.MACHINE_SOUND_DISTANCE)
                        .stream(false)
                        .preload(true)
                ).subtitle("subtitle.cmatd.engine_loop").replace(false)
        );

        add(CmatdSound.ROTATING_LOOP, SoundDefinition.definition()
                .with(sound("cmatd:rotating_loop")
                        .volume(0.9f)
                        .pitch(1.0f)
                        .attenuationDistance(Utility.MACHINE_SOUND_DISTANCE)
                        .stream(false)
                        .preload(true)
                ).subtitle("subtitle.cmatd.rotating_loop").replace(false)
        );
    }
}
