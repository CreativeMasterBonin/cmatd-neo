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

public class EJamMakingRecipe implements EmiRecipe{
    private final ResourceLocation rl;
    private final List<EmiIngredient> inputList;
    private final List<EmiStack> outputList;

    public EJamMakingRecipe(String recipeName, Ingredient jar, Ingredient sugar, Ingredient foodType, Item outputItem){
        this.rl = ResourceLocation.parse("cmatd:/" + recipeName);
        this.inputList = List.of(EmiIngredient.of(sugar),EmiIngredient.of(jar),EmiIngredient.of(foodType));
        this.outputList = List.of(EmiStack.of(outputItem));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMIntegration.JAM_MAKING_CATEGORY;
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
        return 73;
    }

    @Override
    public int getDisplayHeight() {
        return 38;
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addSlot(inputList.get(0),1,1); // sugar
        widgetHolder.addSlot(inputList.get(1),1,19); // jar
        widgetHolder.addSlot(inputList.get(2),19,10); // food

        widgetHolder.addSlot(outputList.get(0),54,10).recipeContext(this); // jam output

        widgetHolder.addTexture(ResourceLocation.parse("cmatd:textures/gui/sprites/solar.png"),
                -16,16,8,8,0,0,8,8,
                8,8);
        widgetHolder.addTooltipText(List.of(Component.translatable("emi.presser.needs_sun")),
                -16,16,8,16);
    }
}
