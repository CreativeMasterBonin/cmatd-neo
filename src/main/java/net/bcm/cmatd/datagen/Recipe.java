package net.bcm.cmatd.datagen;

import com.buuz135.industrial.recipe.LaserDrillOreRecipe;
import com.buuz135.industrial.recipe.LaserDrillRarity;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Recipe extends RecipeProvider{
    public Recipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput rc) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.UNPRESSED_PCB)
                .define('p',CmatdItem.COPPER_PCB_BASE)
                .define('r',Items.GREEN_DYE)
                .define('g',Tag.GOLD_DUSTS)
                .define('i',Tag.IRON_DUSTS)
                .pattern("rrr")
                .pattern("gpi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COPPER_PCB_BASE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.JAM_JAR)
                .define('g',Items.GLASS_PANE)
                .define('s',ItemTags.WOODEN_SLABS)
                .pattern("sss")
                .pattern("g g")
                .pattern("ggg")
                .unlockedBy(Utility.HAS_ITEM,has(Items.GLASS_PANE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.MACHINE_FRAME)
                .define('p',CmatdItem.PLATE)
                .define('c',CmatdItem.COMPOUNDITE_INGOT)
                .define('i',Items.IRON_BLOCK)
                .pattern("pcp")
                .pattern("cic")
                .pattern("pcp")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.PLATE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.LESSER_MACHINE_FRAME)
                .define('p',Items.IRON_INGOT)
                .define('c',CmatdItem.COMPOUNDITE_INGOT)
                .define('i',Items.SMOOTH_STONE)
                .pattern("pcp")
                .pattern("cic")
                .pattern("pcp")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc);

        // decorative machine recipes
        stonecutterAny(CmatdItem.LESSER_MACHINE_FRAME.asItem(),Ingredient.of(CmatdItem.LESSER_MACHINE_FRAME),
                CmatdItem.DECORATIVE_BASE_DYNAMO_ENGINE.asItem(),1)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LESSER_MACHINE_FRAME))
                .save(rc);



        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.RESISTOR)
                .define('o',Items.BRICK)
                .define('c',Items.COPPER_INGOT)
                .define('i',CmatdItem.COMPOUNDITE_INGOT)
                .pattern("oio")
                .pattern("c c")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.TRANSISTOR)
                .define('d',Items.DRIED_KELP)
                .define('c',Items.COPPER_INGOT)
                .define('i',CmatdItem.COMPOUNDITE_INGOT)
                .define('b',Items.IRON_BARS)
                .pattern("dbd")
                .pattern("did")
                .pattern("ccc")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.CAPACITOR)
                .define('d',Items.DRIED_KELP)
                .define('c',Items.COPPER_INGOT)
                .define('i',CmatdItem.COMPOUNDITE_INGOT)
                .pattern("ddd")
                .pattern("cic")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.PATTERN_BASE)
                .define('l',CmatdItem.LODEALITE_INGOT)
                .define('p',Items.PAPER)
                .pattern(" l ")
                .pattern("lpl")
                .pattern(" l ")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LODEALITE_INGOT))
                .save(rc);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.INFUSED_INGOT,2)
                .requires(Items.FLINT)
                .requires(Items.LAPIS_LAZULI)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LODEALITE_INGOT))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.BLANK_MODULE,1)
                .requires(CmatdItem.PCB)
                .requires(CmatdItem.INFUSED_INGOT)
                .requires(CmatdItem.RESISTOR)
                .requires(CmatdItem.TRANSISTOR)
                .requires(CmatdItem.CAPACITOR)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.PCB))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.SPEED_MODULE)
                .define('b',CmatdItem.BLANK_MODULE)
                .define('r',Items.BREEZE_ROD)
                .define('f',Items.RABBIT_FOOT)
                .pattern("rfr")
                .pattern("rbr")
                .pattern("rfr")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.BLANK_MODULE))
                        .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.EFFICIENCY_MODULE)
                .define('b',CmatdItem.BLANK_MODULE)
                .define('r',Items.BLAZE_ROD)
                .define('p',Items.GOLDEN_PICKAXE)
                .pattern("rpr")
                .pattern("rbr")
                .pattern("rpr")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.BLANK_MODULE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.DOUBLER_MODULE)
                .define('b',CmatdItem.BLANK_MODULE)
                .define('r',Items.BLAZE_ROD)
                .define('l',CmatdItem.LODEALITE_INGOT)
                .pattern("rlr")
                .pattern("rbr")
                .pattern("rlr")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.BLANK_MODULE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.TRIPLED_MODULE)
                .define('b',CmatdItem.DOUBLER_MODULE)
                .define('r',Items.BREEZE_ROD)
                .define('l',CmatdItem.LODEALITE_INGOT)
                .define('y',Items.YELLOW_GLAZED_TERRACOTTA)
                .pattern("rlr")
                .pattern("yby")
                .pattern("rlr")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.DOUBLER_MODULE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.BASIC_TIER_DOWNGRADE)
                .define('f',CmatdItem.PLATE)
                .define('d',ItemTags.DIRT)
                .pattern(" d ")
                .pattern("dfd")
                .pattern(" d ")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.PLATE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.ADVANCED_TIER_UPGRADE)
                .define('b',CmatdItem.BASIC_TIER_DOWNGRADE)
                .define('d',Items.REDSTONE)
                .pattern(" d ")
                .pattern("dbd")
                .pattern(" d ")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.BASIC_TIER_DOWNGRADE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.HIGHLY_ADVANCED_TIER_UPGRADE)
                .define('b',CmatdItem.ADVANCED_TIER_UPGRADE)
                .define('d',Items.DIAMOND)
                .pattern(" d ")
                .pattern("dbd")
                .pattern(" d ")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.ADVANCED_TIER_UPGRADE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.SUPERB_TIER_UPGRADE)
                .define('b',CmatdItem.HIGHLY_ADVANCED_TIER_UPGRADE)
                .define('n',Items.NETHERITE_INGOT)
                .pattern(" n ")
                .pattern("nbn")
                .pattern(" n ")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.HIGHLY_ADVANCED_TIER_UPGRADE))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.MAXIMUM_TIER_UPGRADE)
                .define('b',CmatdItem.SUPERB_TIER_UPGRADE)
                .define('m',Items.ECHO_SHARD)
                .pattern(" m ")
                .pattern("mbm")
                .pattern(" m ")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.SUPERB_TIER_UPGRADE))
                .save(rc);



        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.COPPER_PCB_BASE)
                .requires(Items.COPPER_INGOT)
                .requires(CmatdItem.PCB_PATTERN)
                .unlockedBy(Utility.HAS_ITEM,has(Items.COPPER_INGOT))
                .save(rc);

        stonecutterAny(CmatdItem.PATTERN_BASE.asItem(),
                Ingredient.of(CmatdItem.PATTERN_BASE),CmatdItem.PLATE_PATTERN.asItem(),1)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.PLATE_PATTERN))
                .save(rc);
        stonecutterAny(CmatdItem.PATTERN_BASE.asItem(),
                Ingredient.of(CmatdItem.PATTERN_BASE),CmatdItem.PCB_PATTERN.asItem(),1)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.PCB_PATTERN))
                .save(rc);

        stonecutterAny(Items.IRON_INGOT,Ingredient.of(Items.IRON_INGOT), CmatdItem.IRON_DUST.asItem(),2)
                .unlockedBy(Utility.HAS_ITEM,has(Items.IRON_INGOT))
                .save(rc);
        stonecutterAny(Items.GOLD_INGOT,Ingredient.of(Items.GOLD_INGOT), CmatdItem.GOLD_DUST.asItem(),2)
                .unlockedBy(Utility.HAS_ITEM,has(Items.GOLD_INGOT))
                .save(rc);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CmatdItem.RAW_COMPOUNDITE),RecipeCategory.MISC,
                CmatdItem.COMPOUNDITE_INGOT,0.1f,200)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.RAW_COMPOUNDITE))
                .save(rc,"compoundite_ingot");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CmatdItem.RAW_LODEALITE),RecipeCategory.MISC,
                CmatdItem.LODEALITE_INGOT,0.1f,200)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.RAW_LODEALITE))
                .save(rc,"lodealite_ingot");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CmatdItem.COMPOUNDITE_ORE),RecipeCategory.MISC,
                CmatdItem.COMPOUNDITE_INGOT,0.1f,200)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_ORE))
                .save(rc,"compoundite_ore_to_ingot");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CmatdItem.LODEALITE_ORE),RecipeCategory.MISC,
                CmatdItem.LODEALITE_INGOT,0.1f,200)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LODEALITE_ORE))
                .save(rc,"lodealite_ore_to_ingot");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CmatdItem.DEEPSLATE_COMPOUNDITE_ORE),RecipeCategory.MISC,
                CmatdItem.COMPOUNDITE_INGOT,0.1f,200)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.DEEPSLATE_COMPOUNDITE_ORE))
                .save(rc,"deep_compoundite_ore_to_ingot");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CmatdItem.DEEPSLATE_LODEALITE_ORE),RecipeCategory.MISC,
                CmatdItem.LODEALITE_INGOT,0.1f,200)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.DEEPSLATE_LODEALITE_ORE))
                .save(rc,"deep_lodealite_ore_to_ingot");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CmatdItem.RAW_COMPOUNDITE),RecipeCategory.MISC,
                CmatdItem.COMPOUNDITE_INGOT,0.2f,100)
                .group("blasting")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.RAW_COMPOUNDITE))
                .save(rc,"raw_compoundite_from_blasting");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CmatdItem.RAW_LODEALITE),RecipeCategory.MISC,
                CmatdItem.LODEALITE_INGOT,0.2f,100)
                .group("blasting")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.RAW_LODEALITE))
                .save(rc,"raw_lodealite_from_blasting");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CmatdItem.COMPOUNDITE_ORE),RecipeCategory.MISC,
                CmatdItem.COMPOUNDITE_INGOT,0.2f,100)
                .group("blasting")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_ORE))
                .save(rc,"compoundite_ore_to_ingot_blasting");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CmatdItem.LODEALITE_ORE),RecipeCategory.MISC,
                CmatdItem.LODEALITE_INGOT,0.2f,100)
                .group("blasting")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LODEALITE_ORE))
                .save(rc,"lodealite_ore_to_ingot_blasting");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CmatdItem.DEEPSLATE_COMPOUNDITE_ORE),RecipeCategory.MISC,
                CmatdItem.COMPOUNDITE_INGOT,0.2f,100)
                .group("blasting")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.DEEPSLATE_COMPOUNDITE_ORE))
                .save(rc,"deep_compoundite_ore_to_ingot_blasting");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CmatdItem.DEEPSLATE_LODEALITE_ORE),RecipeCategory.MISC,
                CmatdItem.LODEALITE_INGOT,0.2f,100)
                .group("blasting")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.DEEPSLATE_LODEALITE_ORE))
                .save(rc,"deep_lodealite_ore_to_ingot_blasting");



        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.PHOTOVOLTAIC_CELL,2)
                .requires(Items.DAYLIGHT_DETECTOR)
                .requires(Items.LAPIS_LAZULI)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .unlockedBy(Utility.HAS_ITEM,has(Items.DAYLIGHT_DETECTOR))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.LUNAR_PHOTOVOLTAIC_CELL,1)
                .requires(CmatdItem.PHOTOVOLTAIC_CELL)
                .requires(Items.ENDER_EYE)
                .requires(CmatdItem.LODEALITE_INGOT)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.PHOTOVOLTAIC_CELL))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.RAW_COMPOUNDITE_BLOCK,1)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .requires(CmatdItem.RAW_COMPOUNDITE)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.RAW_COMPOUNDITE))
                .save(rc);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.RAW_LODEALITE_BLOCK,1)
                .requires(CmatdItem.RAW_LODEALITE)
                .requires(CmatdItem.RAW_LODEALITE)
                .requires(CmatdItem.RAW_LODEALITE)
                .requires(CmatdItem.RAW_LODEALITE)
                .requires(CmatdItem.RAW_LODEALITE)
                .requires(CmatdItem.RAW_LODEALITE)
                .requires(CmatdItem.RAW_LODEALITE)
                .requires(CmatdItem.RAW_LODEALITE)
                .requires(CmatdItem.RAW_LODEALITE)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.RAW_LODEALITE))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.COMPOUNDITE_BLOCK,1)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.LODEALITE_BLOCK,1)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LODEALITE_INGOT))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.COMPOUNDITE_INGOT,9)
                .requires(CmatdItem.COMPOUNDITE_BLOCK)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_BLOCK))
                .save(rc,"compoundite_block_to_ingots");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.LODEALITE_INGOT,9)
                .requires(CmatdItem.LODEALITE_BLOCK)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LODEALITE_BLOCK))
                .save(rc,"lodealite_block_to_ingots");


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.RAW_COMPOUNDITE,9)
                .requires(CmatdItem.RAW_COMPOUNDITE_BLOCK)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.RAW_COMPOUNDITE_BLOCK))
                .save(rc,"raw_compoundite_block_to_ore_pieces");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.RAW_LODEALITE,9)
                .requires(CmatdItem.RAW_LODEALITE_BLOCK)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.RAW_LODEALITE_BLOCK))
                .save(rc,"raw_lodealite_block_to_ore_pieces");

        // simple machine recipes
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.PRESSER,1)
                .requires(CmatdItem.PHOTOVOLTAIC_CELL)
                .requires(CmatdItem.PHOTOVOLTAIC_CELL)
                .requires(CmatdItem.LESSER_MACHINE_FRAME)
                .requires(Items.PISTON)
                .requires(CmatdItem.PATTERN_BASE)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LESSER_MACHINE_FRAME))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.JAM_MAKER,1)
                .requires(CmatdItem.PHOTOVOLTAIC_CELL)
                .requires(CmatdItem.PHOTOVOLTAIC_CELL)
                .requires(CmatdItem.LESSER_MACHINE_FRAME)
                .requires(Items.CRAFTING_TABLE)
                .requires(CmatdItem.JAM_JAR)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LESSER_MACHINE_FRAME))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.MASHER,1)
                .requires(CmatdItem.LESSER_MACHINE_FRAME)
                .requires(Items.PISTON)
                .requires(Items.IRON_INGOT)
                .requires(CmatdItem.LODEALITE_INGOT)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LESSER_MACHINE_FRAME))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.POWER_BOARD,1)
                .requires(CmatdItem.PCB)
                .requires(CmatdItem.CAPACITOR)
                .requires(CmatdItem.RESISTOR)
                .requires(Items.QUARTZ)
                .requires(Items.REDSTONE)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.PCB))
                .save(rc);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,CmatdItem.SAIL,2)
                .requires(CmatdItem.COMPOUNDITE_INGOT)
                .requires(ItemTags.WOOL_CARPETS)
                .requires(ItemTags.WOODEN_SLABS)
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc);

        // redstone column recipes
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.REDSTONE_ENERGY_COLUMN)
                .define('c',CmatdItem.COMPOUNDITE_INGOT)
                .define('s',Items.REDSTONE)
                .define('r',Items.BLAZE_ROD)
                .pattern("c")
                .pattern("r")
                .pattern("s")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc,"straight_redstone_energy_column");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.REDSTONE_ENERGY_COLUMN)
                .define('c',CmatdItem.COMPOUNDITE_INGOT)
                .define('s',Items.REDSTONE)
                .define('r',Items.BLAZE_ROD)
                .pattern("  c")
                .pattern(" r ")
                .pattern("s  ")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc,"diagonal_right_redstone_energy_column");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.REDSTONE_ENERGY_COLUMN)
                .define('c',CmatdItem.COMPOUNDITE_INGOT)
                .define('s',Items.REDSTONE)
                .define('r',Items.BLAZE_ROD)
                .pattern("c  ")
                .pattern(" r ")
                .pattern("  s")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.COMPOUNDITE_INGOT))
                .save(rc,"diagonal_left_redstone_energy_column");
        // end redstone column recipes

        // more machines and other recipes
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.REDSTONE_DYNAMO_ENGINE)
                .define('p',CmatdItem.POWER_BOARD)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('r',CmatdItem.REDSTONE_ENERGY_COLUMN)
                .define('m',CmatdItem.LESSER_MACHINE_FRAME)
                .pattern(" r ")
                .pattern(" p ")
                .pattern("imi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.LESSER_MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.WIND_GENERATOR)
                .define('p',CmatdItem.POWER_BOARD)
                .define('w',CmatdItem.SAIL)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('m',CmatdItem.MACHINE_FRAME)
                .pattern("wiw")
                .pattern(" p ")
                .pattern("imi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.HYDRO_GENERATOR)
                .define('p',CmatdItem.POWER_BOARD)
                .define('t',Items.IRON_TRAPDOOR)
                .define('w',Items.WATER_BUCKET)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('m',CmatdItem.MACHINE_FRAME)
                .pattern("ipi")
                .pattern("tmt")
                .pattern("iwi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.SOLAR_GENERATOR)
                .define('p',CmatdItem.POWER_BOARD)
                .define('s',CmatdItem.PHOTOVOLTAIC_CELL)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('m',CmatdItem.MACHINE_FRAME)
                .pattern("sss")
                .pattern(" m ")
                .pattern("ipi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.LUNAR_GENERATOR)
                .define('p',CmatdItem.POWER_BOARD)
                .define('l',CmatdItem.LUNAR_PHOTOVOLTAIC_CELL)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('m',CmatdItem.MACHINE_FRAME)
                .pattern("lll")
                .pattern(" m ")
                .pattern("ipi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.BASE_ENERGY_MAKER)
                .define('p',CmatdItem.POWER_BOARD)
                .define('f',Items.FURNACE)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('m',CmatdItem.MACHINE_FRAME)
                .pattern("iii")
                .pattern("fmf")
                .pattern("ipi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.BASE_COBBLE_MAKER)
                .define('p',CmatdItem.POWER_BOARD)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('m',CmatdItem.MACHINE_FRAME)
                .define('w',Items.WATER_BUCKET)
                .define('l',Items.LAVA_BUCKET)
                .pattern("iii")
                .pattern("wml")
                .pattern("ipi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.CMATD_WRENCH)
                .define('c',CmatdItem.COMPOUNDITE_INGOT)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('r',CmatdItem.REDSTONE_ENERGY_COLUMN)
                .pattern("i")
                .pattern("r")
                .pattern("c")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.REDSTONE_ENERGY_COLUMN))
                .save(rc,"straight_wrench");

        // other mod integrations

        /*
        PressurizedReactionRecipeBuilder.reaction(
                IngredientCreatorAccess.item().from(CmatdItem.LODEALITE_BLOCK),
                IngredientCreatorAccess.fluid().from(FluidTags.WATER,1_000),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.NUCLEAR_WASTE, 1_000),
                700,
                new ItemStack(Items.STONE),
                MekanismChemicals.POLONIUM.asStack(350L)
        ).build(rc, ResourceLocation.parse("cmatd:nuclear_waste_to_polonium"));

        PressurizedReactionRecipeBuilder.reaction(
                IngredientCreatorAccess.item().from(CmatdItem.COMPOUNDITE_BLOCK),
                IngredientCreatorAccess.fluid().from(MekanismTags.Fluids.NUTRITIONAL_PASTE,2_000),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.NUCLEAR_WASTE, 1_000),
                200,
                new ItemStack(Items.STONE),
                MekanismChemicals.PLUTONIUM.asStack(150L)
        ).build(rc, ResourceLocation.parse("cmatd:nuclear_waste_to_plutonium"));
        */
        LaserDrillOreRecipe.createTagRecipe(rc, "ores/compoundite", DyeColor.ORANGE.getId(),
                new LaserDrillRarity(
                        new LaserDrillRarity.BiomeRarity(List.of(), List.of()),
                        new LaserDrillRarity.DimensionRarity(List.of(),
                                List.of(BuiltinDimensionTypes.NETHER,BuiltinDimensionTypes.END)),
                        -28, 64, 5));

        LaserDrillOreRecipe.createTagRecipe(rc, "ores/lodealite", DyeColor.PURPLE.getId(),
                new LaserDrillRarity(
                        new LaserDrillRarity.BiomeRarity(List.of(), List.of()),
                        new LaserDrillRarity.DimensionRarity(List.of(),
                                List.of(BuiltinDimensionTypes.NETHER,BuiltinDimensionTypes.END)),
                        -32, 64, 6));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.HEAT_GENERATOR)
                .define('p',CmatdItem.POWER_BOARD)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('f',CmatdItem.MACHINE_FRAME)
                .define('m',Items.MAGMA_BLOCK)
                .pattern("iii")
                .pattern("mfm")
                .pattern("ipi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.FOOD_REACTOR_MULTIBLOCK)
                .define('p',CmatdItem.POWER_BOARD)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('f',CmatdItem.MACHINE_FRAME)
                .define('s',Items.SMOKER)
                .define('c',Tag.VALID_FOOD_REACTOR_CASINGS_ITEM)
                .pattern("iii")
                .pattern("sfs")
                .pattern("cpc")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.MACHINE_FRAME))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.GAS_TANK)
                .define('b',Items.IRON_BARS)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('g',Items.TINTED_GLASS)
                .pattern("ibi")
                .pattern("ggg")
                .pattern("iii")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.INFUSED_INGOT))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.DIESEL_ENGINE)
                .define('i',Items.IRON_BLOCK)
                .define('f',CmatdItem.MACHINE_FRAME)
                .define('m',Items.IRON_BARS)
                .define('g',CmatdItem.GAS_TANK)
                .pattern("iii")
                .pattern("mfm")
                .pattern("igi")
                .unlockedBy(Utility.HAS_ITEM,has(CmatdItem.GAS_TANK))
                .save(rc);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CmatdItem.LIGHTNING_GENERATOR)
                .define('l',Items.LIGHTNING_ROD)
                .define('f',CmatdItem.MACHINE_FRAME)
                .define('p',CmatdItem.POWER_BOARD)
                .define('i',CmatdItem.INFUSED_INGOT)
                .define('r',CmatdItem.REDSTONE_ENERGY_COLUMN)
                .pattern("rlr")
                .pattern("pfp")
                .pattern("ifi")
                .unlockedBy(Utility.HAS_ITEM,has(Items.LIGHTNING_ROD))
                .save(rc);
    }

    public static RecipeBuilder stonecutterAny(Item requiredItem, Ingredient inputItem, Item result, int amt){
        return SingleItemRecipeBuilder.stonecutting(inputItem, RecipeCategory.MISC,result,amt)
                .unlockedBy("has_item",inventoryTrigger(ItemPredicate.Builder.item().of(requiredItem).build()));
    }
}
