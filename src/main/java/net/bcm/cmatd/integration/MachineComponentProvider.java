package net.bcm.cmatd.integration;

import net.bcm.cmatd.Utility;
import net.bcm.cmatd.blockentity.BaseCobbleMakerBE;
import net.bcm.cmatd.blockentity.BaseEnergyMakerBE;
import net.bcm.cmatd.blockentity.JamMakerBE;
import net.bcm.cmatd.blockentity.PresserBE;
import net.minecraft.ChatFormatting;
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

        if(blockAccessor.getServerData().contains("progress")){
            int progressAmount = blockAccessor.getServerData().getInt("progress");
            iTooltip.add(Component.translatable("integration.data.progress",progressAmount)
                    .withStyle(ChatFormatting.BLUE));
        }

        if(blockAccessor.getServerData().contains("operating")){
            boolean operating = blockAccessor.getServerData().getBoolean("operating");
            if(operating){
                iTooltip.add(Component.translatable("integration.data.operating")
                        .withColor(Utility.GOOD_OK_GREEN));
            }else{
                iTooltip.add(Component.translatable("integration.data.inoperable")
                        .withColor(Utility.BAD_WARNING_YELLOW));
            }
        }

        if(blockAccessor.getServerData().contains("burn_time_left")){
            int burnTime = blockAccessor.getServerData().getInt("burn_time_left");
            iTooltip.add(Component.translatable("integration.data.burn_time_left",burnTime)
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        if(blockEntity instanceof PresserBE presserBE){
            compoundTag.putInt("machine_tier",presserBE.machineTier);
            compoundTag.putBoolean("night_mode",presserBE.nightUpgrade);
            compoundTag.putInt("progress",presserBE.process_bits);
        }
        else if(blockEntity instanceof JamMakerBE jamMakerBE){
            compoundTag.putInt("machine_tier",jamMakerBE.machineTier);
            compoundTag.putBoolean("night_mode",jamMakerBE.nightUpgrade);
            compoundTag.putInt("progress",jamMakerBE.process_bits);
        }
        else if(blockEntity instanceof BaseCobbleMakerBE cobbleMakerBE){
            compoundTag.putInt("machine_tier",cobbleMakerBE.getTierSettings(0));
            compoundTag.putBoolean("operating",cobbleMakerBE.operating);
        }
        else if(blockEntity instanceof BaseEnergyMakerBE energyMakerBE){
            compoundTag.putInt("machine_tier",energyMakerBE.machine_tier);
            compoundTag.putInt("burn_time_left",energyMakerBE.getBurnTime());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Jadegration.MACHINE_TIER_TYPE_UID;
    }
}
