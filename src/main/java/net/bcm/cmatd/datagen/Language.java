package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public class Language extends LanguageProvider{
    public Language(PackOutput output) {
        super(output, Cmatd.MODID, "en_us");
    }

    public void addGas(Supplier<? extends GasType> key, String name) {
        add(key.get().getDescriptionId(), name);
    }

    @Override
    protected void addTranslations() {
        // creative tab name
        this.add("itemGroup.cmatd","CMATD");

        // titles
        this.add("title.base_energy_maker","Energy Maker");
        this.add("title.base_cobble_maker","Cobble Maker");
        this.add("title.masher","Masher");
        this.add("title.jam_maker","Jam Maker");
        this.add("title.presser","Presser");
        this.add("title.food_reactor","Food Reactor");
        this.add("title.gas_tank","Gas Tank");
        this.add("title.energy_fe","%s FE");
        this.add("title.energy_fe_with_max","%s FE / %s FE");
        this.add("title.burn_time_left","Burn Time Left: %s");
        this.add("title.solar_status.on","Machine can operate in the daytime");
        this.add("title.solar_status.off","Not enough daylight to operate");
        this.add("title.lunar_status.on","Machine can operate at night");
        this.add("title.lunar_status.off","Not dark enough to operate");
        this.add("title.liquid_amt","%s Mb");
        this.add("title.liquid_amt_with_max","%s Mb / %s Mb");

        // data and integration
        this.add("integration.data.gas","Gas: %s");
        this.add("integration.data.gas_amount","%s GU");
        this.add("integration.data.gas_amount_with_max","%s GU / %s GU");
        this.add("config.jade.plugin_cmatd.gas_type_provider","Gas Type Provider");
        this.add("integration.data.gas.is_empty","Tank Empty");

        // subtitles
        this.add("subtitle.cmatd.heater_loop","Machine burns");
        this.add("subtitle.cmatd.processor_loop","Machine processes");
        this.add("subtitle.cmatd.masher","Machine mashes");
        this.add("subtitle.cmatd.masher_metallic","Machine clunks");

        // tooltips
        this.add("tooltip.module.useless","A useless module");
        this.add("tooltip.module.speedup", "Speeds up machines");
        this.add("tooltip.module.efficiency", "Machines use less energy");
        this.add("tooltip.module.doubler", "Chance to double output");
        this.add("tooltip.module.tripled", "Chance to triple output");
        this.add("tooltip.module.blank_module", "Needs to be turned into a usable module");
        this.add("tooltip.tier_upgrade.desc", "Upgrades machines up to tier: ");
        this.add("tooltip.tier_upgrade.downgrade.desc", "Downgrades machines to tier: ");
        this.add("tooltip.tier_upgrade.unknown", "%s");
        this.add("tooltip.tier_upgrade.basic", "Basic");
        this.add("tooltip.tier_upgrade.advanced", "Advanced");
        this.add("tooltip.tier_upgrade.highly_advanced", "Highly Advanced");
        this.add("tooltip.tier_upgrade.superb", "Superb");
        this.add("tooltip.tier_upgrade.maximum", "Maximum");
        this.add("tooltip.tier_upgrade.already_same", "Already same tier!");

        this.add("desc.item.jam", "Used as a coolant in the Food Reactor");
        this.add("desc.item.mashed_potato_like", "Used as fuel in the Food Reactor");
        this.add("desc.item.generator.gen_rate","Base Gen Rate: %s FE");
        this.add("desc.item.redstone_dynamo_engine.additional_info","+%s FE For Sides with a Redstone Block");
        this.add("desc.item.solar_generator.additional_info","Sky darkness affects output");
        this.add("desc.item.jam_maker.additional_info","Keeps crafting progress without items; works only at daytime");
        this.add("desc.item.lunar_generator.additional_info","Only generates at night, moon phases affect output");
        this.add("desc.item.hydro_generator.additional_info","Needs flowing water around it");
        this.add("desc.item.presser.additional_info","Uses patterns to make items; works only at daytime");
        this.add("desc.item.lightning_generator.additional_info","Harness the power of lightning! Has a long cooldown after a bolt strikes it, and requires a lightning rod on top to function at all");

        // others
        this.add("message.multiblock.unformed_with_number_type","Missing blocks: %s of type %s");
        this.add("message.multiblock.formed_successfully","Successfully formed multiblock at %s");

        // ores
        this.add(CmatdBlock.COMPOUNDITE_ORE.get(),"Compoundite Ore");
        this.add(CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get(),"Deepslate Compoundite Ore");
        this.add(CmatdBlock.LODEALITE_ORE.get(),"Lodealite Ore");
        this.add(CmatdBlock.DEEPSLATE_LODEALITE_ORE.get(),"Deepslate Lodealite Ore");
        this.add(CmatdItem.RAW_COMPOUNDITE.get(),"Raw Compoundite");
        this.add(CmatdItem.RAW_LODEALITE.get(),"Raw Lodealite");
        this.add(CmatdItem.COMPOUNDITE_INGOT.get(),"Compoundite Ingot");
        this.add(CmatdItem.LODEALITE_INGOT.get(),"Lodealite Ingot");

        this.add(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get(),"Raw Compoundite Block");
        this.add(CmatdBlock.RAW_LODEALITE_BLOCK.get(),"Raw Lodealite Block");
        this.add(CmatdBlock.COMPOUNDITE_BLOCK.get(),"Compoundite Block");
        this.add(CmatdBlock.LODEALITE_BLOCK.get(),"Lodealite Block");

        // blocks
        this.add(CmatdBlock.LESSER_MACHINE_FRAME.get(),"Lesser Machine Frame");
        this.add(CmatdBlock.MACHINE_FRAME.get(),"Machine Frame");

        this.add(CmatdBlock.BASE_ENERGY_MAKER.get(),"Energy Maker");
        this.add(CmatdBlock.DECORATIVE_BASE_DYNAMO_ENGINE.get(), "Decorative Dynamo Engine");
        this.add(CmatdBlock.BASE_COBBLE_MAKER.get(), "Cobble Maker");
        this.add(CmatdBlock.REDSTONE_DYNAMO_ENGINE.get(),"Redstone Dynamo Engine");
        this.add(CmatdBlock.MASHER.get(),"Masher");
        this.add(CmatdBlock.JAM_MAKER.get(),"Jam Maker");
        this.add(CmatdBlock.WIND_GENERATOR.get(),"Wind Generator");
        this.add(CmatdBlock.SOLAR_GENERATOR.get(),"Solar Generator");
        this.add(CmatdBlock.LUNAR_GENERATOR.get(),"Lunar Generator");
        this.add(CmatdBlock.HYDRO_GENERATOR.get(),"Hydro Generator");
        this.add(CmatdBlock.PRESSER.get(),"Presser");
        this.add(CmatdBlock.LIGHTNING_GENERATOR.get(),"Lightning Generator");
        this.add(CmatdBlock.HEAT_GENERATOR.get(),"Heat Generator");

        // gas blocks
        this.add(CmatdBlock.GAS_TANK.get(),"Gas Tank");
        this.add(CmatdBlock.TEST_GAS_GENERATOR.get(),"Test Gas Generator");

        // conduits
        this.add(CmatdBlock.CONDUIT.get(),"Conduit Energy Cable");
        this.add(CmatdBlock.FACADE_CONDUIT.get(),"Facade Conduit Energy Cable");

        // multiblocks
        this.add(CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get(),"Food Reactor");

        // module items
        this.add(CmatdItem.BLANK_MODULE.get(),"Blank Module");
        this.add(CmatdItem.SPEED_MODULE.get(),"Module: Speedup");
        this.add(CmatdItem.EFFICIENCY_MODULE.get(),"Module: Efficiency");
        this.add(CmatdItem.DOUBLER_MODULE.get(),"Module: Doubler");
        this.add(CmatdItem.TRIPLED_MODULE.get(),"Module: Tripled");

        // tier items
        this.add(CmatdItem.BASIC_TIER_DOWNGRADE.get(),"Tier Downgrade: Basic");
        this.add(CmatdItem.ADVANCED_TIER_UPGRADE.get(),"Tier Upgrade: Advanced");
        this.add(CmatdItem.HIGHLY_ADVANCED_TIER_UPGRADE.get(),"Tier Upgrade: Highly Advanced");
        this.add(CmatdItem.SUPERB_TIER_UPGRADE.get(),"Tier Upgrade: Superb");
        this.add(CmatdItem.MAXIMUM_TIER_UPGRADE.get(),"Tier Upgrade: Maximum");

        // other items
        this.add(CmatdItem.MASHED_POTATOES.get(),"Mashed Potatoes");
        this.add((CmatdItem.POISONOUS_MASHED_POTATOES.get()),"Poisonous Mashed Potatoes");
        this.add((CmatdItem.JAM_JAR.get()),"Jam Jar");
        this.add((CmatdItem.SWEET_BERRY_JAM.get()),"Sweet Berry Jam");
        this.add((CmatdItem.GLOW_BERRY_JAM.get()),"Glow Berry Jam");
        this.add(CmatdItem.CMATD_WRENCH.get(), "CMATD Wrench");

        // electronic devices
        this.add(CmatdItem.RESISTOR.get(),"Resistor");
        this.add(CmatdItem.TRANSISTOR.get(),"Transistor");
        this.add(CmatdItem.CAPACITOR.get(),"Capacitor");

        // raw materials
        this.add(CmatdItem.IRON_DUST.get(),"Iron Dust");
        this.add(CmatdItem.GOLD_DUST.get(),"Gold Dust");

        // processed materials
        this.add(CmatdItem.INFUSED_INGOT.get(),"Infused Ingot");

        // machine assembler parts
        this.add(CmatdItem.COPPER_PCB_BASE.get(),"Copper PCB Base");
        this.add(CmatdItem.UNPRESSED_PCB.get(),"Unpressed PCB");
        this.add(CmatdItem.PCB.get(),"PCB");
        this.add(CmatdItem.POWER_BOARD.get(),"Power Board");
        this.add(CmatdItem.REDSTONE_ENERGY_COLUMN.get(),"Redstone Energy Column");
        this.add(CmatdItem.SAIL.get(),"Sail");
        this.add(CmatdItem.PLATE.get(),"Plate");
        this.add(CmatdItem.PHOTOVOLTAIC_CELL.get(),"Photovoltaic Cell");
        this.add(CmatdItem.LUNAR_PHOTOVOLTAIC_CELL.get(),"Lunar Photovoltaic Cell");

        // pattern items
        this.add(CmatdItem.PATTERN_BASE.get(),"Pattern Base");
        this.add(CmatdItem.PCB_PATTERN.get(),"Pattern: PCB");
        this.add(CmatdItem.PLATE_PATTERN.get(),"Pattern: Plate");

        // tag translations for recipe viewers and such
        this.add(Tag.MASHED_POTATOES,"Mashed Potatoes");
        this.add(Tag.POISONOUS_MASHED_POTATOES,"Poisonous Mashed Potatoes");
        this.add(Tag.JAMS,"Jams");
        this.add(Tag.MODULE,"Modules");
        this.add(Tag.TIER_UPGRADES,"Tier Upgrades");
        this.add(Tag.SIMPLE_ELECTRONIC_DEVICES,"Simple Electronic Devices");
        this.add(Tag.PRESSER_PATTERNS,"Presser Patterns");
        this.add(Tag.IRON_DUSTS,"Iron Dusts");
        this.add(Tag.GOLD_DUSTS,"Gold Dusts");
        this.add(Tag.ORES_COMPOUNDITE,"Compoundite Ores");
        this.add(Tag.ORES_COMPOUNDITE_ITEM,"Compoundite Ore Items");
        this.add(Tag.ORES_LODEALITE,"Lodealite Ores");
        this.add(Tag.ORES_LODEALITE_ITEM,"Lodealite Ore Items");

        // recipe translations
        this.add("emi.category.cmatd..presser_category","Pressing");
        this.add("emi.category.cmatd.presser_category","Pressing");
        this.add("emi.presser.needs_sun","Needs sun");
        this.add("emi.category.cmatd..jam_making_category","Jam Making");
        this.add("emi.category.cmatd.jam_making_category","Jam Making");
        this.add("emi.category.cmatd..masher_category","Mashing");
        this.add("emi.category.cmatd.masher_category","Mashing");

        // Gases (CMATD API)
        this.addGas(Gases.METHANE,"Methane");
        this.addGas(Gases.STEAM,"Steam");
        this.addGas(Gases.COMPOUNDITE,"Gaseous Compoundite");
        this.addGas(Gases.LODEALITE,"Gaseous Lodealite");
        this.add("gas.cmatd.empty","Empty");
        this.add("message.dumping_attempt.gas_is_radioactive","Cannot dump radioactive gases!");
        this.add("title.gas_amt","%s GU");
        this.add("title.gas_amt_with_max","%s GU / %s GU");
        this.add("button.dump_gas","Dump");
        this.add("button.gas_tank.dump.desc","Dumps all gas (not radioactive) into the air");
    }
}
