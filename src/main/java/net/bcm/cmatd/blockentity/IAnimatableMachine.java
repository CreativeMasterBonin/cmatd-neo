package net.bcm.cmatd.blockentity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public interface IAnimatableMachine{
    public List<String> tagNames = List.of("rotationX","rotationY","rotationZ");

    public void savePosesAndAnimationState(CompoundTag tag, HolderLookup.Provider registries);
    public void loadPosesAndAnimationState(CompoundTag tag, HolderLookup.Provider registries);
}
