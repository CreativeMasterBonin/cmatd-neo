package net.bcm.cmatd.integration;

import net.bcm.cmatd.Utility;
import net.bcm.cmatd.blockentity.JamMakerBE;
import net.bcm.cmatd.blockentity.PresserBE;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum MachineComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;
    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if(blockAccessor.getServerData().contains("machine_tier")){
            int machineTier = blockAccessor.getServerData().getInt("machine_tier");
            iTooltip.add(Component.translatable("integration.data.machine_tier",machineTier));
        }

        if(blockAccessor.getServerData().contains("night_mode")){
            boolean nightMode = blockAccessor.getServerData().getBoolean("night_mode");
            if(nightMode){
                iTooltip.add(Component.translatable("integration.data.can_work_at_type",
                                Component.translatable("integration.data.day_and_night"))
                        .withColor(Utility.GOOD_OK_GREEN));
            }else{
                iTooltip.add(Component.translatable("integration.data.can_work_at_type",
                                Component.translatable("integration.data.day"))
                        .withColor(Utility.BAD_WARNING_YELLOW));
            }
        }

        // if a machine should follow a time system
        if(blockAccessor.getServerData().contains("operational_time")){
            short time = blockAccessor.getServerData().getShort("operational_time");
            if(time == 0){
                iTooltip.add(Component.translatable("integration.data.can_work_at_type",
                                Component.translatable("integration.data.day"))
                        .withColor(Utility.BAD_WARNING_YELLOW));
            }
            else if(time == 1){
                iTooltip.add(Component.translatable("integration.data.can_work_at_type",
                                Component.translatable("integration.data.night"))
                        .withColor(Utility.BAD_WARNING_YELLOW));
            }
            else if(time == 2){
                iTooltip.add(Component.translatable("integration.data.can_work_at_type",
                                Component.translatable("integration.data.day_and_night"))
                        .withColor(Utility.BAD_WARNING_YELLOW));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        if(blockEntity instanceof PresserBE presserBE){
            compoundTag.putInt("machine_tier",presserBE.machineTier);
            compoundTag.putBoolean("night_mode",presserBE.nightUpgrade);
        }
        else if(blockEntity instanceof JamMakerBE jamMakerBE){
            compoundTag.putInt("machine_tier",jamMakerBE.machineTier);
            compoundTag.putBoolean("night_mode",jamMakerBE.nightUpgrade);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Jadegration.MACHINE_TIER_TYPE_UID;
    }
}
