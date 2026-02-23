package net.bcm.cmatd.datagen;

import net.bcm.cmatd.Cmatd;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockModel extends BlockModelProvider {
    public BlockModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Cmatd.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels(){

    }
}
