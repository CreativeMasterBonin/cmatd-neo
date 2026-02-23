package net.bcm.cmatd;

public interface MachineTierInterface{
    String getName();
    int getTierLevel();
    int getPowerGenRate();
    int getPowerCapacity();
    int maxExtractRate();
    int maxReceiveRate();
    int getModulesSupport();
}
