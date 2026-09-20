package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagProvider extends EnchantmentTagsProvider {
    public EnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Cmatd.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tag.SUPPORTED_BY_RADIOACTIVE_HUMANOID_ARMOR)
                .add(Enchantments.THORNS)
                .add(Enchantments.RESPIRATION)
                .add(Enchantments.AQUA_AFFINITY)
                .add(Enchantments.DEPTH_STRIDER)
                .add(Enchantments.FROST_WALKER)
                .add(Enchantments.SWIFT_SNEAK)
                .add(Enchantments.MENDING)
                .add(Enchantments.UNBREAKING)
        ;

        tag(Tag.SUPPORTED_BY_RADIOACTIVE_HORSE_ARMOR)
                .add(Enchantments.RESPIRATION)
                .add(Enchantments.THORNS)
                .add(Enchantments.PROTECTION)
                .add(Enchantments.PROJECTILE_PROTECTION)
                .add(Enchantments.BLAST_PROTECTION)
                .add(Enchantments.FIRE_PROTECTION)
                .add(Enchantments.MENDING)
                .add(Enchantments.UNBREAKING)
        ;

        tag(Tag.SUPPORTED_BY_RADIOACTIVE_WOLF_ARMOR)
                .add(Enchantments.RESPIRATION)
                .add(Enchantments.THORNS)
                .add(Enchantments.PROTECTION)
                .add(Enchantments.PROJECTILE_PROTECTION)
                .add(Enchantments.BLAST_PROTECTION)
                .add(Enchantments.FIRE_PROTECTION)
                .add(Enchantments.MENDING)
                .add(Enchantments.UNBREAKING)
        ;
    }
}
