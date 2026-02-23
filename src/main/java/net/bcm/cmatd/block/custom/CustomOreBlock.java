package net.bcm.cmatd.block.custom;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class CustomOreBlock extends DropExperienceBlock {
    public CustomOreBlock(int minXp, int maxXp,float destroyTime,float explosionResistance, Properties properties) {
        super(UniformInt.of(minXp, maxXp), properties
                .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
                .strength(destroyTime,explosionResistance));
    }
}
