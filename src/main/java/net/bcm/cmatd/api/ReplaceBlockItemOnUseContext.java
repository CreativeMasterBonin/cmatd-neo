package net.bcm.cmatd.api;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public class ReplaceBlockItemOnUseContext extends BlockPlaceContext {
    public ReplaceBlockItemOnUseContext(UseOnContext context) {
        super(context);
        replaceClicked = true;
    }
}
