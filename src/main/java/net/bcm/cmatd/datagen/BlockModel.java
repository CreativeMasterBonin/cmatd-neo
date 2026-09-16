package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.block.CmatdBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockModel extends BlockModelProvider {
    public BlockModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Cmatd.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels(){
        cubeBottomTop(CmatdBlock.RADIOACTIVE_REACTOR_MULTIBLOCK_CASING.getRegisteredName(),
                ResourceLocation.parse("cmatd:block/blast_casing_side"),
                ResourceLocation.parse("cmatd:block/blast_casing_bottom"),
                ResourceLocation.parse("cmatd:block/blast_casing_top"))
                .ao(true);
    }
}
