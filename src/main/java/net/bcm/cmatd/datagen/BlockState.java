package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockState extends BlockStateProvider {
    public BlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Cmatd.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(CmatdBlock.COMPOUNDITE_ORE.get());
        simpleBlock(CmatdBlock.DEEPSLATE_COMPOUNDITE_ORE.get());
        simpleBlock(CmatdBlock.LODEALITE_ORE.get());
        simpleBlock(CmatdBlock.DEEPSLATE_LODEALITE_ORE.get());
        simpleBlock(CmatdBlock.VITIATIUM_ORE.get());
        simpleBlock(CmatdBlock.DEEPSLATE_VITIATIUM_ORE.get());

        simpleBlock(CmatdBlock.RAW_COMPOUNDITE_BLOCK.get());
        simpleBlock(CmatdBlock.RAW_LODEALITE_BLOCK.get());
        simpleBlock(CmatdBlock.RAW_VITIATIUM_BLOCK.get());
        simpleBlock(CmatdBlock.COMPOUNDITE_BLOCK.get());
        simpleBlock(CmatdBlock.LODEALITE_BLOCK.get());
        simpleBlock(CmatdBlock.VITIATIUM_BLOCK.get());

        simpleBlockWithItem(CmatdBlock.DIMENSIONAL_TRANSPORTER.get(), new ModelFile.UncheckedModelFile(
                ResourceLocation.parse("cmatd:block/conduit_all")
        ));
        simpleBlockWithItem(CmatdBlock.RADIOACTIVE_REACTOR_MULTIBLOCK.get(),new ModelFile.UncheckedModelFile(
                ResourceLocation.parse("cmatd:block/radioactive_reactor_core")
        ));
        simpleBlockWithItem(CmatdBlock.RADIOACTIVE_REACTOR_MULTIBLOCK_CASING.get(),new ModelFile.UncheckedModelFile(
                ResourceLocation.parse("cmatd:block/radioactive_reactor_casing")
        ));
        simpleBlock(CmatdBlock.RADIOACTIVE_WASTE.get(), new ModelFile.UncheckedModelFile(
                ResourceLocation.parse("cmatd:block/radioactive_waste")
        ));
    }
}
