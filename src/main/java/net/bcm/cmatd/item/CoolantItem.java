package net.bcm.cmatd.item;

import net.bcm.cmatd.Components;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class CoolantItem extends Item {
    public CoolantItem(Properties properties) {
        super(properties.component(Components.COOLANT,true)
                .stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.has(Components.COOLANT) && stack.has(DataComponents.DAMAGE) && stack.has(DataComponents.MAX_DAMAGE);
    }
}
