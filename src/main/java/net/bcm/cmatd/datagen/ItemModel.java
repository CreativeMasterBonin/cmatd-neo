package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.fluid.custom.CustomFluidType;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;

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
        defaultCustomSimpleItem(CmatdItem.HEAT_DISPERSION_MODULE.get());
        defaultCustomSimpleItem(CmatdItem.SILENCING_MODULE.get());
        // tier upgrades
        defaultCustomSimpleItem(CmatdItem.BASIC_TIER_DOWNGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.ADVANCED_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.HIGHLY_ADVANCED_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.SUPERB_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.MAXIMUM_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.SIXTH_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.SEVENTH_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.EIGHTH_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.NINTH_TIER_UPGRADE.asItem());
        defaultCustomSimpleItem(CmatdItem.SUPERFLUOUS_TIER_UPGRADE.asItem());
        // other items
        defaultCustomSimpleItem(CmatdItem.MASHED_POTATOES.asItem());
        defaultCustomSimpleItem(CmatdItem.POISONOUS_MASHED_POTATOES.asItem());

        defaultCustomSimpleItem(CmatdItem.RESISTOR.asItem());
        defaultCustomSimpleItem(CmatdItem.TRANSISTOR.asItem());
        defaultCustomSimpleItem(CmatdItem.CAPACITOR.asItem());
        defaultCustomSimpleItem(CmatdItem.IRON_DUST.asItem());
        defaultCustomSimpleItem(CmatdItem.GOLD_DUST.asItem());
        defaultCustomSimpleItem(CmatdItem.INFUSED_INGOT.asItem());
        defaultCustomSimpleItem(CmatdItem.RAW_COMPOUNDITE.asItem());
        defaultCustomSimpleItem(CmatdItem.RAW_LODEALITE.asItem());
        defaultCustomSimpleItem(CmatdItem.RAW_VITIATIUM.asItem());
        defaultCustomSimpleItem(CmatdItem.COMPOUNDITE_INGOT.asItem());
        defaultCustomSimpleItem(CmatdItem.LODEALITE_INGOT.asItem());
        defaultCustomSimpleItem(CmatdItem.VITIATIUM_INGOT.asItem());

        // packages
        defaultCustomSimpleItem(CmatdItem.BASIC_COOLANT_PACKAGE.get());

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

        defaultCustomSimpleItem(CmatdItem.SEALED_STRONG_PLATE.asItem());
        defaultCustomSimpleItem(CmatdItem.BLAST_PROOF_INGOT.asItem());


        // pattern items
        defaultCustomSimpleItem(CmatdItem.PATTERN_BASE.asItem());
        defaultCustomSimpleItem(CmatdItem.PCB_PATTERN.asItem());
        defaultCustomSimpleItem(CmatdItem.PLATE_PATTERN.asItem());

        simpleBlockItem(CmatdBlock.COMPOUNDITE_ORE.get());
        simpleBlockItem(CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get());
        simpleBlockItem(CmatdBlock.LODEALITE_ORE.get());
        simpleBlockItem(CmatdBlock.DEEPSLATE_LODEALITE_ORE.get());
        simpleBlockItem(CmatdBlock.VITIATIUM_ORE.get());
        simpleBlockItem(CmatdBlock.DEEPSLATE_VITIATIUM_ORE.get());

        simpleBlockItem(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get());
        simpleBlockItem(CmatdBlock.RAW_LODEALITE_BLOCK.get());
        simpleBlockItem(CmatdBlock.COMPOUNDITE_BLOCK.get());
        simpleBlockItem(CmatdBlock.LODEALITE_BLOCK.get());
        simpleBlockItem(CmatdBlock.RAW_VITIATIUM_BLOCK.get());
        simpleBlockItem(CmatdBlock.VITIATIUM_BLOCK.get());

        defaultCustomHeldItem(CmatdItem.RADIOACTIVE_WASTE_BUCKET.asItem());

        // armors
        trimmedArmorItem(CmatdItem.RADIOACTIVE_SUIT_HEADGEAR);
        trimmedArmorItem(CmatdItem.RADIOACTIVE_SUIT_BODYWEAR);
        trimmedArmorItem(CmatdItem.RADIOACTIVE_SUIT_PANTS);
        trimmedArmorItem(CmatdItem.RADIOACTIVE_SUIT_BOOTS);
        // horse armors
        defaultCustomSimpleItem(CmatdItem.RADIOACTIVE_HORSE_SUIT.asItem());
        defaultCustomSimpleItem(CmatdItem.RADIOACTIVE_WOLF_SUIT.asItem());


        // jam making items
        defaultCustomSimpleItem(CmatdItem.JAM_JAR.asItem());
        defaultCustomSimpleItem(CmatdItem.SWEET_BERRY_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.GLOW_BERRY_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.CACTUS_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.CHORUS_FRUIT_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.COCOA_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.GHAST_TEAR_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.GLISTERING_MELON_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.GLOW_INK_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.INK_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.KELP_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.MAGMA_CREAM_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.RAW_EGG_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.SLIME_JAM.asItem());
        defaultCustomSimpleItem(CmatdItem.SPIDER_EYE_JAM.asItem());
    }

    // no credit nor license may override this variable's license, it is still: MIT
    // https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X/blob/17-armor/LICENSE
    // material hash map variable credit to (circa - 2024): El_Redstoniano and credit to kaupenjoe for publicizing it
    public static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    // no credit nor license may override this method's license, it is still: MIT
    // https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X/blob/17-armor/LICENSE
    // trim gen code credit to (circa - 2024): El_Redstoniano and credit to kaupenjoe for publicizing it
    public void trimmedArmorItem(DeferredItem<ArmorItem> itemDeferredItem) {
        final String MOD_ID = Cmatd.MODID; // Change this to your mod id

        if(itemDeferredItem.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = armorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemDeferredItem.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace()  + ":item/" + trimNameResLoc.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                ResourceLocation.fromNamespaceAndPath(MOD_ID,
                                        "item/" + itemDeferredItem.getId().getPath()));
            });
        }
    }

    // continue cmatd special methods

    private ItemModelBuilder bucketDrip(Item item, ResourceLocation fluidTexture){
        return withExistingParent(item.asItem().toString(),
                ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket_drip"))
                .texture("fluid",
                        fluidTexture);
    }

    private ItemModelBuilder bucketDripFromCustomFluidType(Item item, CustomFluidType fluid){
        return withExistingParent(item.asItem().toString(),
                ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket_drip"))
                .texture("fluid",
                fluid.getStillTexture() + ".png");
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

    @Override
    public String getName() {
        return "CMATD Item Models";
    }
}
