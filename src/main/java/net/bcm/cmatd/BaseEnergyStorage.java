package net.bcm.cmatd;

import net.neoforged.neoforge.energy.EnergyStorage;

public class BaseEnergyStorage extends EnergyStorage{
    public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public int getMaxExtract(){
        return maxExtract;
    }

    public int getCapacity(){
        return capacity;
    }

    public int getMaxReceive(){
        return maxReceive;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    public int simulateSetEnergy(int simulatedEnergy){
        return simulatedEnergy;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public void setMaxExtract(int maxExtract){
        this.maxExtract = maxExtract;
    }

    public void setMaxReceive(int maxReceive){
        this.maxReceive = maxReceive;
    }

    /*public void generateEnergy(int energy){
        this.energy = Math.min(capacity,this.energy + energy);
    }

    public void consumeEnergy(int energy){
        this.energy = Math.max(0, this.energy - energy);
    }*/

    public boolean isSaturatedEnergy(){
        return this.getEnergyStored() >= this.getMaxEnergyStored();
    }
}
