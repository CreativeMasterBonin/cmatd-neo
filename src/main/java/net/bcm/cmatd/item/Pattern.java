package net.bcm.cmatd.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class Pattern extends Item{
    public Pattern(Properties properties) {
        super(properties.craftRemainder(CmatdItem.PATTERN_BASE.asItem()).stacksTo(1).setNoRepair());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(CmatdItem.PATTERN_BASE.asItem());
    }
}
