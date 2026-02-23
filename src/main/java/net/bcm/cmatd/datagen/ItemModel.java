package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModel extends ItemModelProvider{
    public ItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Cmatd.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // modules
        defaultCustomSimpleItem(CmatdItem.BLANK_MODULE.get());
        defaultCustomSimpleItem(CmatdItem.SPEED_MODULE.get());
        defaultCustomSimpleItem(CmatdItem.EFFICIENCY_MODULE.get());
        defaultCustomSimpleItem(CmatdItem.DOUBLER_MODULE.get());
        defaultCustomSimpleItem(CmatdItem.TRIPLED_MODULE.get());
        // tier upgrades
        defaultCustomSimpleItem(CmatdItem.BASIC_TIER_DOWNGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.ADVANCED_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.HIGHLY_ADVANCED_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.SUPERB_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.MAXIMUM_TIER_UPGRADE.asItem());
        // other items
        defaultCustomSimpleItem(CmatdItem.MASHED_POTATOES.asItem());
        defaultCustomSimpleItem(CmatdItem.POISONOUS_MASHED_POTATOES.asItem());
        defaultCustomSimpleItem(CmatdItem.JAM_JAR.asItem());
        defaultCustomSimpleItem(CmatdItem.SWEET_BERRY_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.GLOW_BERRY_JAM.asItem());

        defaultCustomSimpleItem(CmatdItem.RESISTOR.asItem());
        defaultCustomSimpleItem(CmatdItem.TRANSISTOR.asItem());
        defaultCustomSimpleItem(CmatdItem.CAPACITOR.asItem());
        defaultCustomSimpleItem(CmatdItem.IRON_DUST.asItem());
        defaultCustomSimpleItem(CmatdItem.GOLD_DUST.asItem());
        defaultCustomSimpleItem(CmatdItem.INFUSED_INGOT.asItem());
        defaultCustomSimpleItem(CmatdItem.RAW_COMPOUNDITE.asItem());
        defaultCustomSimpleItem(CmatdItem.RAW_LODEALITE.asItem());
        defaultCustomSimpleItem(CmatdItem.COMPOUNDITE_INGOT.asItem());
        defaultCustomSimpleItem(CmatdItem.LODEALITE_INGOT.asItem());

        defaultCustomSimpleItem(CmatdItem.COPPER_PCB_BASE.asItem());
        defaultCustomSimpleItem(CmatdItem.UNPRESSED_PCB.asItem());
        defaultCustomSimpleItem(CmatdItem.PCB.asItem());
        defaultCustomSimpleItem(CmatdItem.PLATE.asItem());
        defaultCustomSimpleItem(CmatdItem.PHOTOVOLTAIC_CELL.asItem());
        defaultCustomSimpleItem(CmatdItem.LUNAR_PHOTOVOLTAIC_CELL.asItem());
        defaultCustomSimpleItem(CmatdItem.POWER_BOARD.asItem());
        defaultCustomSimpleItem(CmatdItem.SAIL.asItem());
        defaultCustomSimpleItem(CmatdItem.REDSTONE_ENERGY_COLUMN.asItem());
        defaultCustomHeldItem(CmatdItem.CMATD_WRENCH.asItem());


        // pattern items
        defaultCustomSimpleItem(CmatdItem.PATTERN_BASE.asItem());
        defaultCustomSimpleItem(CmatdItem.PCB_PATTERN.asItem());
        defaultCustomSimpleItem(CmatdItem.PLATE_PATTERN.asItem());

        simpleBlockItem(CmatdBlock.COMPOUNDITE_ORE.get());
        simpleBlockItem(CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get());
        simpleBlockItem(CmatdBlock.LODEALITE_ORE.get());
        simpleBlockItem(CmatdBlock.DEEPSLATE_LODEALITE_ORE.get());

        simpleBlockItem(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get());
        simpleBlockItem(CmatdBlock.RAW_LODEALITE_BLOCK.get());
        simpleBlockItem(CmatdBlock.COMPOUNDITE_BLOCK.get());
        simpleBlockItem(CmatdBlock.LODEALITE_BLOCK.get());
    }

    private ItemModelBuilder defaultCustomSimpleItem(Item item){
        return withExistingParent(item.asItem().toString(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"item/" + String.format(item.asItem().toString()).replaceAll("cmatd:","")));
    }

    private ItemModelBuilder defaultCustomHeldItem(Item item){
        return withExistingParent(item.asItem().toString(),
                ResourceLocation.withDefaultNamespace("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,"item/" + String.format(item.asItem().toString()).replaceAll("cmatd:","")));
    }
}
