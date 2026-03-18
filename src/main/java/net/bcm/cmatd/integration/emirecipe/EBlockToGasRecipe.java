package net.bcm.cmatd.integration.emirecipe;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.block.custom.GasVent;
import net.bcm.cmatd.integration.EMIntegration;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EBlockToGasRecipe implements EmiRecipe {
    public ResourceLocation rl;
    public List<EmiIngredient> input;

    public EBlockToGasRecipe(String recipeName, Ingredient block){
        this.rl = ResourceLocation.parse("cmatd:/block_to_gas/" + recipeName);
        this.input = List.of(EmiIngredient.of(block));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMIntegration.BLOCK_TO_GAS_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.rl;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public int getDisplayWidth() {
        return 73;
    }

    @Override
    public int getDisplayHeight() {
        return 38;
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder){
        widgetHolder.addSlot(input.getFirst(),8,13);

        if(input.getFirst().getEmiStacks().getFirst().getItemStack().getItem() instanceof BlockItem blockItem){
            if(blockItem.getBlock() instanceof GasVent gasVent){
                widgetHolder.addTexture(ResourceLocation.parse("cmatd:textures/block/stone_steam_vent_top.png"),
                        52,14,16,16,0,0,16,16,
                        16,16);
                widgetHolder.addTooltipText(List.of(Component.translatable(gasVent.getDefaultGasStack().getGas().getDescriptionId())),
                        52,14,18,18);

                widgetHolder.addFillingArrow(27,14,927).tooltip(
                        List.of(ClientTooltipComponent.create(
                                EmiPort.ordered(Component.translatable("emi.block_to_gas.cmatd.produces",
                                                gasVent.gasAmountToProduce.getMinValue(),gasVent.gasAmountToProduce.getMaxValue())
                                        .withColor(Utility.GOOD_OK_GREEN))),
                                ClientTooltipComponent.create(
                                        EmiPort.ordered(Component.translatable("emi.desc.cmatd.every_this_ticks",27))
                                )
                        )
                );
            }
        }
    }
}
