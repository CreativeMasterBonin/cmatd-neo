package net.bcm.cmatd.item;

import net.bcm.cmatd.Components;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CoolantItem extends Item {
    public CoolantItem(Properties properties) {
        super(properties.component(Components.COOLANT,true)
                .stacksTo(1).rarity(Rarity.UNCOMMON));
    }
}
