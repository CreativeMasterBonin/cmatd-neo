package net.bcm.cmatd.item;

import net.bcm.cmatd.*;
import net.bcm.cmatd.block.CmatdBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class CmatdItem{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Cmatd.MODID);

    // block items
    public static final DeferredItem<BlockItem> MACHINE_FRAME =
            ITEMS.register("machine_frame",
                    () -> new BlockItem(CmatdBlock.MACHINE_FRAME.get(),new Item.Properties()));
    public static final DeferredItem<BlockItem> LESSER_MACHINE_FRAME =
            ITEMS.register("lesser_machine_frame",
                    () -> new BlockItem(CmatdBlock.LESSER_MACHINE_FRAME.get(),new Item.Properties()));


    public static final DeferredItem<BlockItem> BASE_ENERGY_MAKER =
            ITEMS.register("base_energy_maker",
                    () -> new BlockItem(CmatdBlock.BASE_ENERGY_MAKER.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> DECORATIVE_BASE_DYNAMO_ENGINE =
            ITEMS.register("decorative_base_dynamo_engine",
                    () -> new BlockItem(CmatdBlock.DECORATIVE_BASE_DYNAMO_ENGINE.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> BASE_COBBLE_MAKER =
            ITEMS.register("base_cobble_maker",
                    () -> new BlockItem(CmatdBlock.BASE_COBBLE_MAKER.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> REDSTONE_DYNAMO_ENGINE =
            ITEMS.register("redstone_dynamo_engine",
                    () -> new BlockItem(CmatdBlock.REDSTONE_DYNAMO_ENGINE.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> MASHER =
            ITEMS.register("masher",
                    () -> new BlockItem(CmatdBlock.MASHER.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> JAM_MAKER =
            ITEMS.register("jam_maker",
                    () -> new BlockItem(CmatdBlock.JAM_MAKER.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> PRESSER =
            ITEMS.register("presser",
                    () -> new BlockItem(CmatdBlock.PRESSER.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> WIND_GENERATOR =
            ITEMS.register("wind_generator",
                    () -> new BlockItem(CmatdBlock.WIND_GENERATOR.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> SOLAR_GENERATOR =
            ITEMS.register("solar_generator",
                    () -> new BlockItem(CmatdBlock.SOLAR_GENERATOR.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> LUNAR_GENERATOR =
            ITEMS.register("lunar_generator",
                    () -> new BlockItem(CmatdBlock.LUNAR_GENERATOR.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> HYDRO_GENERATOR =
            ITEMS.register("hydro_generator",
                    () -> new BlockItem(CmatdBlock.HYDRO_GENERATOR.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> LIGHTNING_GENERATOR =
            ITEMS.register("lightning_generator",
                    () -> new BlockItem(CmatdBlock.LIGHTNING_GENERATOR.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> HEAT_GENERATOR =
            ITEMS.register("heat_generator",
                    () -> new BlockItem(CmatdBlock.HEAT_GENERATOR.get(),new Item.Properties()));

    // mutliblocks
    public static final DeferredItem<BlockItem> FOOD_REACTOR_MULTIBLOCK =
            ITEMS.register("food_reactor",
                    () -> new BlockItem(CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get(),new Item.Properties()));


    // gas supporting blockitems
    public static final DeferredItem<BlockItem> GAS_TANK =
            ITEMS.register("gas_tank",
                    () -> new BlockItem(CmatdBlock.GAS_TANK.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> TEST_GAS_GENERATOR =
            ITEMS.register("test_gas_generator",
                    () -> new BlockItem(CmatdBlock.TEST_GAS_GENERATOR.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> DIESEL_ENGINE =
            ITEMS.register("diesel_engine",
                    () -> new BlockItem(CmatdBlock.DIESEL_ENGINE.get(),new Item.Properties()));

    // conduits cables
    public static final DeferredItem<BlockItem> CONDUIT =
            ITEMS.register("conduit_cable",
                    () -> new BlockItem(CmatdBlock.CONDUIT.get(),new Item.Properties()){
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            tooltipComponents.add(Component.literal("Slightly broken. Duplicates power when received, which is not intentional. Connects to CMATD machines as well as other mods' machines").withStyle(ChatFormatting.RED));
                        }
                    });

    public static final DeferredItem<BlockItem> FACADE_CONDUIT =
            ITEMS.register("facade_conduit_cable",
                    () -> new FacadeConduitCableItem(CmatdBlock.FACADE_CONDUIT.get(),new Item.Properties()));



    public static final DeferredItem<BlockItem> COMPOUNDITE_ORE =
            ITEMS.register("compoundite_ore",
                    () -> new BlockItem(CmatdBlock.COMPOUNDITE_ORE.get(),new Item.Properties()));
    public static final DeferredItem<BlockItem> DEEPSLATE_COMPOUNDITE_ORE =
            ITEMS.register("deepslate_compoundite_ore",
                    () -> new BlockItem(CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> LODEALITE_ORE =
            ITEMS.register("lodealite_ore",
                    () -> new BlockItem(CmatdBlock.LODEALITE_ORE.get(),new Item.Properties()));
    public static final DeferredItem<BlockItem> DEEPSLATE_LODEALITE_ORE =
            ITEMS.register("deepslate_lodealite_ore",
                    () -> new BlockItem(CmatdBlock.DEEPSLATE_LODEALITE_ORE.get(),new Item.Properties()));

    public static final DeferredItem<Item> RAW_COMPOUNDITE = ITEMS.register("raw_compoundite",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_LODEALITE = ITEMS.register("raw_lodealite",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COMPOUNDITE_INGOT = ITEMS.register("compoundite_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LODEALITE_INGOT = ITEMS.register("lodealite_ingot",
            () -> new Item(new Item.Properties()));


    public static final DeferredItem<BlockItem> RAW_LODEALITE_BLOCK =
            ITEMS.register("raw_lodealite_block",
                    () -> new BlockItem(CmatdBlock.RAW_LODEALITE_BLOCK.get(),new Item.Properties()));
    public static final DeferredItem<BlockItem> LODEALITE_BLOCK =
            ITEMS.register("lodealite_block",
                    () -> new BlockItem(CmatdBlock.LODEALITE_BLOCK.get(),new Item.Properties()));

    public static final DeferredItem<BlockItem> RAW_COMPOUNDITE_BLOCK =
            ITEMS.register("raw_compoundite_block",
                    () -> new BlockItem(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get(),new Item.Properties()));
    public static final DeferredItem<BlockItem> COMPOUNDITE_BLOCK =
            ITEMS.register("compoundite_block",
                    () -> new BlockItem(CmatdBlock.COMPOUNDITE_BLOCK.get(),new Item.Properties()));

    // misc items

    public static final DeferredItem<Item> MASHED_POTATOES =
            ITEMS.register("mashed_potatoes",
                    () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.1F)
                            .build())){
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            tooltipComponents.add(
                                    Component.translatable("desc.item.mashed_potato_like")
                                            .withStyle(ChatFormatting.GRAY));
                        }
                    });

    public static final DeferredItem<Item> POISONOUS_MASHED_POTATOES =
            ITEMS.register("poisonous_mashed_potatoes",
                    () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.1F)
                            .effect(() -> new MobEffectInstance(
                                    MobEffects.POISON,
                                    90,
                                    0),0.3f)
                            .build())){
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            tooltipComponents.add(
                                    Component.translatable("desc.item.mashed_potato_like")
                                            .withStyle(ChatFormatting.GRAY));
                        }
                    });

    public static final DeferredItem<Item> JAM_JAR =
            ITEMS.register("jam_jar",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SWEET_BERRY_JAM =
            ITEMS.register("sweet_berry_jam",
                    () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(1.0f)
                            .usingConvertsTo(CmatdItem.JAM_JAR)
                            .build())){
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            tooltipComponents.add(
                                    Component.translatable("desc.item.jam")
                                            .withStyle(ChatFormatting.GRAY));
                        }
                    });

    public static final DeferredItem<Item> GLOW_BERRY_JAM =
            ITEMS.register("glow_berry_jam",
                    () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(1.0f)
                            .usingConvertsTo(CmatdItem.JAM_JAR)
                            .build())){
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            tooltipComponents.add(
                                    Component.translatable("desc.item.jam")
                                            .withStyle(ChatFormatting.GRAY));
                        }
                    });

    // items
    // modules
    public static final DeferredItem<Item> BLANK_MODULE =
            ITEMS.register("module",
                    () -> new ModuleItem(new Item.Properties(),true));

    public static final DeferredItem<Item> SPEED_MODULE =
            ITEMS.register("speed_module",
                    () -> new ModuleItem(new Item.Properties().component(Components.MODULE_TYPE,0)));

    public static final DeferredItem<Item> EFFICIENCY_MODULE =
            ITEMS.register("efficiency_module",
                    () -> new ModuleItem(new Item.Properties().component(Components.MODULE_TYPE,1)));

    public static final DeferredItem<Item> DOUBLER_MODULE =
            ITEMS.register("doubler_module",
                    () -> new ModuleItem(new Item.Properties().component(Components.MODULE_TYPE,2)));

    public static final DeferredItem<Item> TRIPLED_MODULE =
            ITEMS.register("tripled_module",
                    () -> new ModuleItem(new Item.Properties().component(Components.MODULE_TYPE,3)));

    // tier upgrades
    public static final DeferredItem<Item> BASIC_TIER_DOWNGRADE =
            ITEMS.register("basic_tier_downgrade",
                    () -> new TierUpgrade(new Item.Properties()
                            .component(Components.MACHINE_TIER,new MachineTierComponent(
                                    MachineTier.BASIC.getTierLevel(),
                                    MachineTier.BASIC.getPowerCapacity(),
                                    MachineTier.BASIC.maxReceiveRate(),
                                    MachineTier.BASIC.maxExtractRate(),
                                    MachineTier.BASIC.getModulesSupport(),
                                    MachineTier.BASIC.getPowerGenRate()))));

    public static final DeferredItem<Item> ADVANCED_TIER_UPGRADE =
            ITEMS.register("advanced_tier_upgrade",
                    () -> new TierUpgrade(new Item.Properties()
                            .component(Components.MACHINE_TIER,new MachineTierComponent(
                                    MachineTier.IRON.getTierLevel(),
                                    MachineTier.IRON.getPowerCapacity(),
                                    MachineTier.IRON.maxReceiveRate(),
                                    MachineTier.IRON.maxExtractRate(),
                                    MachineTier.IRON.getModulesSupport(),
                                    MachineTier.IRON.getPowerGenRate()))));

    public static final DeferredItem<Item> HIGHLY_ADVANCED_TIER_UPGRADE =
            ITEMS.register("highly_advanced_tier_upgrade",
                    () -> new TierUpgrade(new Item.Properties()
                            .component(Components.MACHINE_TIER,new MachineTierComponent(
                                    MachineTier.DIAMOND.getTierLevel(),
                                    MachineTier.DIAMOND.getPowerCapacity(),
                                    MachineTier.DIAMOND.maxReceiveRate(),
                                    MachineTier.DIAMOND.maxExtractRate(),
                                    MachineTier.DIAMOND.getModulesSupport(),
                                    MachineTier.DIAMOND.getPowerGenRate()))));

    public static final DeferredItem<Item> SUPERB_TIER_UPGRADE =
            ITEMS.register("superb_tier_upgrade",
                    () -> new TierUpgrade(new Item.Properties()
                            .component(Components.MACHINE_TIER,new MachineTierComponent(
                                    MachineTier.NETHERITE.getTierLevel(),
                                    MachineTier.NETHERITE.getPowerCapacity(),
                                    MachineTier.NETHERITE.maxReceiveRate(),
                                    MachineTier.NETHERITE.maxExtractRate(),
                                    MachineTier.NETHERITE.getModulesSupport(),
                                    MachineTier.NETHERITE.getPowerGenRate()))));

    public static final DeferredItem<Item> MAXIMUM_TIER_UPGRADE =
            ITEMS.register("maximum_tier_upgrade",
                    () -> new TierUpgrade(new Item.Properties()
                            .component(Components.MACHINE_TIER,new MachineTierComponent(
                                    MachineTier.MAXIMUM.getTierLevel(),
                                    MachineTier.MAXIMUM.getPowerCapacity(),
                                    MachineTier.MAXIMUM.maxReceiveRate(),
                                    MachineTier.MAXIMUM.maxExtractRate(),
                                    MachineTier.MAXIMUM.getModulesSupport(),
                                    MachineTier.MAXIMUM.getPowerGenRate()))));

    // electronic components (used to build base components)
    public static final DeferredItem<Item> CAPACITOR =
            ITEMS.register("capacitor",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TRANSISTOR =
            ITEMS.register("transistor",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RESISTOR =
            ITEMS.register("resistor",
                    () -> new Item(new Item.Properties()));

    // base construct items (items used to craft machines)
    public static final DeferredItem<Item> IRON_DUST =
            ITEMS.register("iron_dust",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GOLD_DUST =
            ITEMS.register("gold_dust",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COPPER_PCB_BASE =
            ITEMS.register("copper_pcb_base",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> UNPRESSED_PCB =
            ITEMS.register("unpressed_pcb",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PLATE =
            ITEMS.register("plate",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PCB =
            ITEMS.register("pcb",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> INFUSED_INGOT =
            ITEMS.register("infused_ingot",
                    () -> new Item(new Item.Properties()));

    // pattern system (used to create components from pattern shapes)
    public static final DeferredItem<Item> PATTERN_BASE =
            ITEMS.register("pattern_base",
                    () -> new Item(new Item.Properties().component(DataComponents.MAX_STACK_SIZE,
                            Item.ABSOLUTE_MAX_STACK_SIZE)));

    public static final DeferredItem<Item> PCB_PATTERN =
            ITEMS.register("pcb_pattern",
                    () -> new Pattern(new Item.Properties()
                            .component(Components.PATTERN,
                            PatternComponent.create("pcb_pattern",
                                    CmatdItem.UNPRESSED_PCB.asItem(),CmatdItem.PCB.asItem(), 1))));

    public static final DeferredItem<Item> PLATE_PATTERN =
            ITEMS.register("plate_pattern",
                    () -> new Pattern(new Item.Properties()
                            .component(Components.PATTERN,
                            PatternComponent.create("plate_pattern",
                                    CmatdItem.INFUSED_INGOT.asItem(),CmatdItem.PLATE.asItem(), 3))));

    public static final DeferredItem<Item> PHOTOVOLTAIC_CELL =
            ITEMS.register("photovoltaic_cell",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LUNAR_PHOTOVOLTAIC_CELL =
            ITEMS.register("lunar_photovoltaic_cell",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POWER_BOARD =
            ITEMS.register("power_board",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SAIL =
            ITEMS.register("sail",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> REDSTONE_ENERGY_COLUMN =
            ITEMS.register("redstone_energy_column",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CMATD_WRENCH =
            ITEMS.register("cmatd_wrench",
                    () -> new Item(new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            .fireResistant()));

    // debug items
}
