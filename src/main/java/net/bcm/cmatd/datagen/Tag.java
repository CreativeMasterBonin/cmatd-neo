package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class Tag{
    public static final TagKey<Item> MODULE = itemTag("module");
    public static final TagKey<Item> TIER_UPGRADES = itemTag("tier_upgrades");
    public static final TagKey<Item> MASHED_POTATOES = commonItemTag("foods/mashed_potatoes");
    public static final TagKey<Item> POISONOUS_MASHED_POTATOES = commonItemTag("foods/poisonous_mashed_potatoes");
    public static final TagKey<Item> JAMS = commonItemTag("foods/jams");
    public static final TagKey<Item> SIMPLE_ELECTRONIC_DEVICES = itemTag("simple_electronic_devices");
    public static final TagKey<Item> PRESSER_PATTERNS = itemTag("presser_patterns");
    public static final TagKey<Item> IRON_DUSTS = commonItemTag("dusts/iron");
    public static final TagKey<Item> GOLD_DUSTS = commonItemTag("dusts/gold");

    public static final TagKey<Block> STORAGE_BLOCKS_COMPOUNDITE = commonBlockTag("storage_blocks/compoundite");
    public static final TagKey<Block> STORAGE_BLOCKS_LODEALITE = commonBlockTag("storage_blocks/lodealite");
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_COMPOUNDITE = commonBlockTag("storage_blocks/raw_compoundite");
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_LODEALITE = commonBlockTag("storage_blocks/raw_lodealite");

    // gas producing blocks and items
    public static final TagKey<Item> GAS_PRODUCER = itemTag("gas_vent");
    public static final TagKey<Block> PRODUCES_METHANE_GAS = blockTag("gas_producers/methane");
    public static final TagKey<Block> PRODUCES_STEAM_GAS = blockTag("gas_producers/steam");

    // reactor parts and items
    public static final TagKey<Block> VALID_FOOD_REACTOR_CASINGS = blockTag("valid_food_reactor_casings");
    public static final TagKey<Item> VALID_FOOD_REACTOR_CASINGS_ITEM = itemTag("valid_food_reactor_casings");
    public static final TagKey<Item> VALID_FOOD_REACTOR_FUELS = itemTag("valid_food_reactor_fuels");
    public static final TagKey<Item> VALID_FOOD_REACTOR_COOLANTS = itemTag("valid_food_reactor_coolants");

    // biome tags
    public static final TagKey<Biome> GENERATES_STEAM_VENTS = biomeTag("generates_steam_vents");

    //COMPOUNDITE LODEALITE
    public static final TagKey<Block> ORES_COMPOUNDITE = commonBlockTag("ores/compoundite");
    public static final TagKey<Block> ORES_LODEALITE = commonBlockTag("ores/lodealite");
    public static final TagKey<Item> ORES_COMPOUNDITE_ITEM = commonItemTag("ores/compoundite");
    public static final TagKey<Item> ORES_LODEALITE_ITEM = commonItemTag("ores/lodealite");

    // heat producers
    public static final TagKey<Block> HEAT_PRODUCERS = blockTag("heat_producers");
    public static final TagKey<Block> HIGH_HEAT_PRODUCERS = blockTag("high_heat_producers");
    public static final TagKey<Block> MEDIUM_HEAT_PRODUCERS = blockTag("medium_heat_producers");
    public static final TagKey<Block> LOW_HEAT_PRODUCERS = blockTag("low_heat_producers");

    // gas tags
    public static final TagKey<GasType> BURNABLE_GASES = gasTag("burnable");
    public static final TagKey<GasType> FREEZABLE_GASES = gasTag("freezable");

    private static TagKey<Fluid> fluidTag(String name){
        return FluidTags.create(ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, name));
    }

    private static TagKey<Block> blockTag(String name){
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, name));
    }

    private static TagKey<Item> itemTag(String name){
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, name));
    }

    private static TagKey<Biome> biomeTag(String name){
        return TagKey.create(net.minecraft.core.registries.Registries.BIOME,ResourceLocation.fromNamespaceAndPath(Cmatd.MODID, name));
    }

    public static TagKey<GasType> gasTag(String name){
        return TagKey.create(Registries.GAS_TYPES_KEY,ResourceLocation.fromNamespaceAndPath(Cmatd.MODID,name));
    }

    private static TagKey<Fluid> commonFluidTag(String name){
        return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Block> commonBlockTag(String name){
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Item> commonItemTag(String name){
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    public static TagKey<GasType> commonGasTag(String name){
        return TagKey.create(Registries.GAS_TYPES_KEY,ResourceLocation.fromNamespaceAndPath("c",name));
    }
}
