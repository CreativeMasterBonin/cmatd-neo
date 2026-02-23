package net.bcm.cmatd.datagen;

import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;

public class Loot extends VanillaBlockLoot {
    public Loot(HolderLookup.Provider registries) {
        super(registries);
    }

    @Override
    protected void generate() {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        this.dropSelf(CmatdBlock.BASE_ENERGY_MAKER.get());
        this.dropSelf(CmatdBlock.DECORATIVE_BASE_DYNAMO_ENGINE.get());
        this.dropSelf(CmatdBlock.BASE_COBBLE_MAKER.get());
        this.dropSelf(CmatdBlock.REDSTONE_DYNAMO_ENGINE.get());
        this.dropSelf(CmatdBlock.MASHER.get());
        this.dropSelf(CmatdBlock.JAM_MAKER.get());
        this.dropSelf(CmatdBlock.WIND_GENERATOR.get());
        this.dropSelf(CmatdBlock.SOLAR_GENERATOR.get());
        this.dropSelf(CmatdBlock.LUNAR_GENERATOR.get());
        this.dropSelf(CmatdBlock.HYDRO_GENERATOR.get());

        this.dropSelf(CmatdBlock.GAS_TANK.get());
        this.dropSelf(CmatdBlock.DIESEL_ENGINE.get());
        this.dropSelf(CmatdBlock.CONDUIT.get());
        this.dropSelf(CmatdBlock.FACADE_CONDUIT.get());


        this.dropSelf(CmatdBlock.MACHINE_FRAME.get());
        this.dropSelf(CmatdBlock.PRESSER.get());
        this.dropSelf(CmatdBlock.LESSER_MACHINE_FRAME.get());

        this.add(CmatdBlock.COMPOUNDITE_ORE.get(), block ->
                createOreDrop(registrylookup,block,CmatdItem.RAW_COMPOUNDITE.asItem()));
        this.add(CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get(), block ->
                createOreDrop(registrylookup,block,CmatdItem.RAW_COMPOUNDITE.asItem()));

        this.add(CmatdBlock.LODEALITE_ORE.get(), block ->
                createOreDrop(registrylookup,block,CmatdItem.RAW_LODEALITE.asItem()));
        this.add(CmatdBlock.DEEPSLATE_LODEALITE_ORE.get(), block ->
                createOreDrop(registrylookup,block,CmatdItem.RAW_LODEALITE.asItem()));

        this.dropSelf(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get());
        this.dropSelf(CmatdBlock.RAW_LODEALITE_BLOCK.get());
        this.dropSelf(CmatdBlock.COMPOUNDITE_BLOCK.get());
        this.dropSelf(CmatdBlock.LODEALITE_BLOCK.get());


        this.dropSelf(CmatdBlock.LIGHTNING_GENERATOR.get());
        this.dropSelf(CmatdBlock.HEAT_GENERATOR.get());

        // multiblocks
        this.dropSelf(CmatdBlock.FOOD_REACTOR_MULTIBLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return new ArrayList<>(CmatdBlock.BLOCKS.getEntries().stream().map(DeferredHolder::get).toList());
    }

    protected LootTable.Builder createOreDrop(HolderLookup.RegistryLookup<Enchantment> lookup, Block block, Item item) {
        return this.createSilkTouchDispatchTable(
                block,
                this.applyExplosionDecay(
                        block, LootItem.lootTableItem(item).apply(ApplyBonusCount.addOreBonusCount(lookup.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }
}
