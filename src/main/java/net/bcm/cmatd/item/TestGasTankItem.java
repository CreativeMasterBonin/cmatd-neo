package net.bcm.cmatd.item;

import net.minecraft.world.item.Item;

public class TestGasTankItem extends Item {
    public TestGasTankItem(Properties properties) {
        super(properties.stacksTo(1).fireResistant());
    }
}
