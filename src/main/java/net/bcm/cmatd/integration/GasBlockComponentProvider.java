package net.bcm.cmatd.integration;

import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Gases;
import net.bcm.cmatd.api.Registries;
import net.bcm.cmatd.blockentity.AbstractGasContainingBE;
import net.bcm.cmatd.blockentity.DieselEngineBE;
import net.bcm.cmatd.blockentity.GasTankBE;
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

public enum GasBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if(blockAccessor.getServerData().contains("gas")){
            if(blockAccessor.getServerData().getString("gas").equals(Gases.EMPTY.getDescriptionId())){
                iTooltip.append(Component.literal("\n"));
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
    public ResourceLocation getUid() {
        return Jadegration.GAS_TYPE_UID;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        AbstractGasContainingBE gasTankBE = (AbstractGasContainingBE) blockAccessor.getBlockEntity();
        if(gasTankBE instanceof AbstractGasContainingBE){
            compoundTag.putString("gas", gasTankBE.getGasTank().getGasStack().getGas().getDescriptionId());
            compoundTag.putString("gas_id",gasTankBE.getGasTank().getGasStack().getGas().toString());
            compoundTag.putInt("gas_amount",gasTankBE.getGasTank().getGasStack().getAmount());
            compoundTag.putInt("max_gas_amount",gasTankBE.getGasTank().getCapacity());
        }
    }
}
