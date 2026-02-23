package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Cmatd.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGen{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.getGenerator().addProvider(event.includeServer(),new EMCProvider(packOutput,lookupProvider));

        event.getGenerator().addProvider(event.includeServer(),new WorldlyProvider(packOutput,lookupProvider));

        event.getGenerator().addProvider(event.includeClient(),
                new BlockState(packOutput,event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(),
                new BlockModel(packOutput,event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(),
                new ItemModel(packOutput,event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(),
                new SoundDefinitions(packOutput,event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(),new Language(packOutput));

        generator.addProvider(event.includeServer(),new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(Loot::new, LootContextParamSets.BLOCK)),lookupProvider));

        BlockTagProvider blockTags = new BlockTagProvider(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);

        event.getGenerator().addProvider(event.includeServer(),
                new ItemTagProvider(packOutput, lookupProvider, blockTags.contentsGetter(),event.getExistingFileHelper()));

        generator.addProvider(event.includeServer(),new Recipe(packOutput,lookupProvider));

        generator.addProvider(event.includeServer(),new DatamapGen(packOutput,lookupProvider));
    }
}
