package net.bcm.cmatd.integration;

import net.bcm.cmatd.blockentity.RotationalInductionGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum RotationalValueComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;
    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if(blockAccessor.getServerData().contains("rotational_power") && blockAccessor.getServerData().contains("being_rotated")){
            int rotationalPower = blockAccessor.getServerData().getInt("rotational_power");
            boolean beingRotated = blockAccessor.getServerData().getBoolean("being_rotated");

            if(rotationalPower <= 0 || !beingRotated){
                iTooltip.add(Component.translatable("integration.data.no_rotational_power"));
                return;
            }
            else{
                iTooltip.add(Component.translatable("integration.data.rotational_power_amount",rotationalPower,beingRotated));
                return;
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        RotationalInductionGenerator rIGen = (RotationalInductionGenerator)blockAccessor.getBlockEntity();
        if(rIGen instanceof RotationalInductionGenerator gen){
            compoundTag.putInt("rotational_power",gen.getRotationalPower());
            compoundTag.putBoolean("being_rotated",gen.isBeingRotated());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Jadegration.ROTATIONAL_OBJECT_TYPE_UID;
    }
}
