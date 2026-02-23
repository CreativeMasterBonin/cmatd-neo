package net.bcm.cmatd;

public enum MachineTier implements MachineTierInterface{
    EMPTY("empty",999,1,1,1,1,0),
    BASIC("basic",0,50,10000,1000,1000,1),
    IRON("iron",1,350,50000,3500,3500,2),
    DIAMOND("diamond",2,750,150000,7500,7500,3),
    NETHERITE("netherite",3,1000,250000,10000,10000,4),
    MAXIMUM("maximum",4,2500,500000,25000,25000,5);

    private final String tierName;
    private final int tierLevel;
    private final int generationRate;
    private final int powerCapacity;
    private final int maxExtractPowerAmt;
    private final int maxReceivePowerAmt;
    private final int modulesSupported;

    MachineTier(){
        this.tierName = "default";
        this.tierLevel = 0;
        this.generationRate = 1;
        this.powerCapacity = 1;
        this.maxExtractPowerAmt = 1;
        this.maxReceivePowerAmt = 1;
        this.modulesSupported = 1;
    }

    MachineTier(String tierName, int tierLevel, int generationRate, int powerCapacity, int maxExtractPowerAmt, int maxReceivePowerAmt, int modulesSupported){
        this.tierName = tierName;
        this.tierLevel = tierLevel;
        this.generationRate = generationRate;
        this.powerCapacity = powerCapacity;
        this.maxExtractPowerAmt = maxExtractPowerAmt;
        this.maxReceivePowerAmt = maxReceivePowerAmt;
        this.modulesSupported = modulesSupported;
    }

    @Override
    public String getName() {
        return tierName;
    }

    @Override
    public int getTierLevel() {
        return tierLevel;
    }

    @Override
    public int getPowerGenRate() {
        return generationRate;
    }

    @Override
    public int getPowerCapacity() {
        return powerCapacity;
    }

    @Override
    public int maxExtractRate() {
        return maxExtractPowerAmt;
    }

    @Override
    public int maxReceiveRate() {
        return maxReceivePowerAmt;
    }

    @Override
    public int getModulesSupport() {
        return modulesSupported;
    }
}
