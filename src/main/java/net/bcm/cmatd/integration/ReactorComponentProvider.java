package net.bcm.cmatd.integration;

import net.bcm.cmatd.Utility;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.api.Registries;
import net.bcm.cmatd.blockentity.FoodReactorMultiblock;
import net.bcm.cmatd.blockentity.RadioactiveReactor;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum ReactorComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;
    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if(blockAccessor.getServerData().contains("machine_tier")){
            int machineTier = blockAccessor.getServerData().getInt("machine_tier");
            iTooltip.add(Component.translatable("integration.data.machine_tier",machineTier));
        }
        if(blockAccessor.getServerData().contains("reactor_formed")){
            boolean isFormed = blockAccessor.getServerData().getBoolean("reactor_formed");
            iTooltip.add(Component.translatable("integration.data.multiblock_formed",isFormed));
        }
        if(blockAccessor.getServerData().contains("heat_amount")){
            if(blockAccessor.getServerData().contains("heat_amount_max")){
                int heatAmount = blockAccessor.getServerData().getInt("heat_amount");
                int heatAmountMax = blockAccessor.getServerData().getInt("heat_amount_max");
                if(heatAmount > 0 && heatAmount < 500){
                    iTooltip.add(Component.translatable("integration.data.reactor_heat_with_max",heatAmount,heatAmountMax)
                            .withColor(Utility.hexToInt("0x62A0FF"))); // blue
                }
                else if(heatAmount >= 500 && heatAmount < 5500){
                    iTooltip.add(Component.translatable("integration.data.reactor_heat_with_max",heatAmount,heatAmountMax)
                            .withColor(Utility.hexToInt("0x96FF57"))); // green
                }
                else if(heatAmount >= 5501 && heatAmount < 8900){
                    iTooltip.add(Component.translatable("integration.data.reactor_heat_with_max",heatAmount,heatAmountMax)
                            .withColor(Utility.hexToInt("0xFFED48"))); // yellow
                }
                else if(heatAmount >= 8901 && heatAmount < 9500){
                    iTooltip.add(Component.translatable("integration.data.reactor_heat_with_max",heatAmount,heatAmountMax)
                            .withColor(Utility.hexToInt("0xFF7E27"))); // orange
                }
                else if(heatAmount >= 9501){
                    iTooltip.add(Component.translatable("integration.data.reactor_heat_with_max",heatAmount,heatAmountMax)
                            .withColor(Utility.hexToInt("0xFF3810"))); // red
                }
                else{
                    iTooltip.add(Component.translatable("integration.data.reactor_heat_with_max",heatAmount,heatAmountMax)
                            .withStyle(ChatFormatting.GRAY));
                }
            }
            else{
                int heatAmount = blockAccessor.getServerData().getInt("heat_amount");
                iTooltip.add(Component.translatable("integration.data.reactor_heat",heatAmount)
                        .withStyle(ChatFormatting.GRAY));
            }
        }
        if(blockAccessor.getServerData().contains("sealed")){
            boolean isSealedCore = blockAccessor.getServerData().getBoolean("sealed");
            if(isSealedCore){
                iTooltip.add(Component.translatable("integration.data.reactor_sealed")
                        .withColor(Utility.hexToInt("0x96FF57"))); // green
            }
            else{
                iTooltip.add(Component.translatable("integration.data.reactor_unsealed")
                        .withColor(Utility.hexToInt("0xFF7E27"))); // orange
            }
        }
        if(blockAccessor.getServerData().contains("coolant_amount")){
            if(blockAccessor.getServerData().contains("coolant_amount_max")){
                int coolantAmount = blockAccessor.getServerData().getInt("coolant_amount");
                int coolantAmountMax = blockAccessor.getServerData().getInt("coolant_amount_max");
                iTooltip.add(Component.translatable("integration.data.reactor_coolant_with_max",coolantAmount,coolantAmountMax)
                        .withStyle(ChatFormatting.BLUE));
            }
            else{
                int coolantAmount = blockAccessor.getServerData().getInt("coolant_amount");
                iTooltip.add(Component.translatable("integration.data.reactor_coolant",coolantAmount)
                        .withStyle(ChatFormatting.BLUE));
            }
        }
        if(blockAccessor.getServerData().contains("progress")){
            int progressAmount = blockAccessor.getServerData().getInt("progress");
            iTooltip.add(Component.translatable("integration.data.progress",progressAmount)
                    .withStyle(ChatFormatting.BLUE));
        }
        // gas-related
        if(blockAccessor.getServerData().contains("gas")){
            if(blockAccessor.getServerData().getString("gas").equals(Gases.EMPTY.getDescriptionId())){
                iTooltip.append(Component.literal(" "));
                iTooltip.append(Component.translatable("integration.data.gas.is_empty"));
                return;
            }
            // if not empty, continue

            IElementHelper helper = IElementHelper.get();

            if(blockAccessor.getServerData().contains("gas_id")){
                GasType gasType = Registries.GAS_TYPES.get(
                        ResourceLocation.parse(blockAccessor.getServerData().getString("gas_id")));

                IElement icon = new SpriteElementGas(ResourceLocation.parse("minecraft:textures/block/water_still.png"),
                        16,16,gasType)
                        .size(new Vec2(16, 16)).translate(new Vec2(-4, -2));

                iTooltip.add(icon);
                icon.message(null);
            }

            iTooltip.append(Component.translatable("integration.data.gas"," "));
            iTooltip.append(Component.translatable(blockAccessor.getServerData().getString("gas")));

            iTooltip.append(Component.literal(" "));
        }

        if(blockAccessor.getServerData().contains("gas_amount") && blockAccessor.getServerData().contains("max_gas_amount")){
            iTooltip.append(Component.translatable("integration.data.gas_amount_with_max",
                    blockAccessor.getServerData().getInt("gas_amount"),
                    blockAccessor.getServerData().getInt("max_gas_amount")));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        if(blockEntity instanceof FoodReactorMultiblock foodReactor){
            compoundTag.putBoolean("reactor_formed",foodReactor.multiblockFormed);
            compoundTag.putInt("progress",foodReactor.progress);
        }
        else if(blockEntity instanceof RadioactiveReactor radioactiveReactor){
            compoundTag.putBoolean("reactor_formed",radioactiveReactor.isFormed);
            compoundTag.putInt("machine_tier",radioactiveReactor.machineTier);
            compoundTag.putInt("heat_amount",radioactiveReactor.heatAmount);
            compoundTag.putInt("heat_amount_max",radioactiveReactor.heatAmountToGoBoomAt);
            compoundTag.putBoolean("sealed",radioactiveReactor.sealedCore);
            compoundTag.putInt("coolant_amount",radioactiveReactor.coolantAmount);
            compoundTag.putInt("coolant_amount_max",radioactiveReactor.maxCoolantAmount);
            compoundTag.putString("gas", radioactiveReactor.wasteGasTank.getGasStack().getGas().getDescriptionId());
            compoundTag.putString("gas_id",radioactiveReactor.wasteGasTank.getGasStack().getGas().toString());
            compoundTag.putInt("gas_amount",radioactiveReactor.wasteGasTank.getGasStack().getAmount());
            compoundTag.putInt("max_gas_amount",radioactiveReactor.wasteGasTank.getCapacity());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Jadegration.REACTOR_TYPE_UID;
    }
}
