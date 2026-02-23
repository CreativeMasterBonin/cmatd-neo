package net.bcm.cmatd.integration.emirecipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.bcm.cmatd.integration.EMIntegration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EPresserRecipe implements EmiRecipe {
    private final ResourceLocation rl;
    private final List<EmiIngredient> inputList;
    private final List<EmiStack> outputList;

    public EPresserRecipe(String recipeName, Ingredient patternType, Ingredient inputMaterial, Item outputItem){
        this.rl = ResourceLocation.parse("cmatd:/" + recipeName);
        this.inputList = List.of(EmiIngredient.of(patternType),EmiIngredient.of(inputMaterial));
        this.outputList = List.of(EmiStack.of(outputItem));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMIntegration.PRESSER_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return rl;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputList;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputList;
    }

    @Override
    public int getDisplayWidth() {
        return 92;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addSlot(inputList.get(1),16,10);
        widgetHolder.addSlot(inputList.get(0),16,28);
        widgetHolder.addSlot(outputList.get(0),62,19).recipeContext(this);

        widgetHolder.addTexture(ResourceLocation.parse("cmatd:textures/gui/sprites/solar.png"),
                4,24,8,8,0,0,8,8,
                8,8);
        widgetHolder.addTooltipText(List.of(Component.translatable("emi.presser.needs_sun")),
                4,24,8,16);
    }
}
