package net.bcm.cmatd.integration.emirecipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.bcm.cmatd.integration.EMIntegration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EMasherRecipe implements EmiRecipe {
    private final ResourceLocation rl;
    private final List<EmiIngredient> inputList;
    private final List<EmiStack> outputList;

    public EMasherRecipe(String recipeName, Ingredient inputItem, Item outputItem){
        this.rl = ResourceLocation.parse("cmatd:/" + recipeName);
        this.inputList = List.of(EmiIngredient.of(inputItem));
        this.outputList = List.of(EmiStack.of(outputItem));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMIntegration.MASHER_CATEGORY;
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
        return 66;
    }

    @Override
    public int getDisplayHeight() {
        return 38;
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addSlot(inputList.get(0),7,10);
        widgetHolder.addSlot(outputList.get(0),40,10).recipeContext(this);
    }
}
