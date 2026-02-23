package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider{
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Item>> parentProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, parentProvider, blockTags, Cmatd.MODID, existingFileHelper);
    }

    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Cmatd.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tag.MODULE)
                .add(CmatdItem.BLANK_MODULE.asItem())
                .add(CmatdItem.SPEED_MODULE.asItem())
                .add(CmatdItem.EFFICIENCY_MODULE.asItem())
                .add(CmatdItem.DOUBLER_MODULE.asItem())
                .add(CmatdItem.TRIPLED_MODULE.asItem())
        ;
        this.tag(Tag.TIER_UPGRADES)
                .add(CmatdItem.BASIC_TIER_DOWNGRADE.asItem())
                .add(CmatdItem.ADVANCED_TIER_UPGRADE.asItem())
                .add(CmatdItem.HIGHLY_ADVANCED_TIER_UPGRADE.asItem())
                .add(CmatdItem.SUPERB_TIER_UPGRADE.asItem())
                .add(CmatdItem.MAXIMUM_TIER_UPGRADE.asItem())
        ;

        this.tag(Tag.MASHED_POTATOES)
                .add(CmatdItem.MASHED_POTATOES.asItem())
        ;

        this.tag(Tag.JAMS)
                .add(CmatdItem.SWEET_BERRY_JAM.asItem())
                .add(CmatdItem.GLOW_BERRY_JAM.asItem())
        ;

        this.tag(Tag.POISONOUS_MASHED_POTATOES)
                .add(CmatdItem.POISONOUS_MASHED_POTATOES.asItem())
        ;

        this.tag(Tags.Items.FOODS)
                .addTag(Tag.MASHED_POTATOES)
                .addTag(Tag.JAMS)
        ;

        this.tag(Tags.Items.FOODS_FOOD_POISONING)
                .addTag(Tag.POISONOUS_MASHED_POTATOES)
        ;

        this.tag(Tag.SIMPLE_ELECTRONIC_DEVICES)
                .add(CmatdItem.RESISTOR.asItem())
                .add(CmatdItem.TRANSISTOR.asItem())
                .add(CmatdItem.CAPACITOR.asItem())
        ;

        this.tag(Tag.PRESSER_PATTERNS)
                .add(CmatdItem.PCB_PATTERN.asItem())
                .add(CmatdItem.PLATE_PATTERN.asItem())
        ;

        this.tag(Tag.IRON_DUSTS)
                .add(CmatdItem.IRON_DUST.asItem())
        ;

        this.tag(Tag.GOLD_DUSTS)
                .add(CmatdItem.GOLD_DUST.asItem())
        ;
        this.tag(Tag.ORES_COMPOUNDITE_ITEM)
                .add(CmatdItem.COMPOUNDITE_ORE.asItem())
                .add(CmatdItem.DEEPSLATE_COMPOUNDITE_ORE.asItem())
        ;
        this.tag(Tag.ORES_LODEALITE_ITEM)
                .add(CmatdItem.LODEALITE_ORE.asItem())
                .add(CmatdItem.DEEPSLATE_LODEALITE_ORE.asItem())
        ;
        this.tag(Tags.Items.ORES_IN_GROUND_STONE)
                .add(CmatdItem.COMPOUNDITE_ORE.asItem())
                .add(CmatdItem.LODEALITE_ORE.asItem())
        ;
        this.tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE)
                .add(CmatdItem.DEEPSLATE_COMPOUNDITE_ORE.asItem())
                .add(CmatdItem.DEEPSLATE_LODEALITE_ORE.asItem())
        ;
        this.tag(Tags.Items.ORES)
                .addTag(Tag.ORES_COMPOUNDITE_ITEM)
                .addTag(Tag.ORES_LODEALITE_ITEM)
        ;
        // tools
        this.tag(Tags.Items.TOOLS_WRENCH)
                .add(CmatdItem.CMATD_WRENCH.asItem())
        ;
        // reactor
        this.tag(Tag.VALID_FOOD_REACTOR_FUELS)
                .addTag(Tag.MASHED_POTATOES)
                .addTag(Tag.POISONOUS_MASHED_POTATOES)
        ;
        this.tag(Tag.VALID_FOOD_REACTOR_COOLANTS)
                .addTag(Tag.JAMS)
        ;
        this.tag(Tag.VALID_FOOD_REACTOR_CASINGS_ITEM)
                .add(CmatdItem.COMPOUNDITE_BLOCK.asItem())
                .add(CmatdItem.LODEALITE_BLOCK.asItem())
        ;
    }

    @Override
    public String getName() {
        return "CMATD Item Tags";
    }
}
