package net.bcm.cmatd;

import net.minecraft.util.StringRepresentable;

public enum ConduitType implements StringRepresentable {
    NONE("none"),CONDUIT("conduit"),BLOCK("block");
    final String name;

    ConduitType(String name){
        this.name = name;
    }

    public static final ConduitType[] VALUES = values();

    public String getName() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return name.toLowerCase();
    }
}
