package net.bcm.cmatd.integration;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiRegistryAdapter;
import dev.emi.emi.api.stack.EmiStack;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Registries;
import net.bcm.cmatd.integration.emirecipe.EJamMakingRecipe;
import net.bcm.cmatd.integration.emirecipe.EMasherRecipe;
import net.bcm.cmatd.integration.emirecipe.EPresserRecipe;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

@EmiEntrypoint
public class EMIntegration implements EmiPlugin {
    public static final ResourceLocation PRESS_ICON = ResourceLocation.parse("cmatd:textures/misc/pressing_icon.png");
    public static final ResourceLocation JAM_ICON = ResourceLocation.parse("cmatd:textures/misc/jammaking_icon.png");
    public static final ResourceLocation MASHER_ICON = ResourceLocation.parse("cmatd:textures/misc/mashing_icon.png");

    public static final EmiStack PRESSER_STACK = EmiStack.of(CmatdItem.PRESSER);
    public static final EmiStack JAM_MAKER_STACK = EmiStack.of(CmatdItem.JAM_MAKER);
    public static final EmiStack MASHER_STACK = EmiStack.of(CmatdItem.MASHER);

    private static final EmiRegistryAdapter<GasType> GAS_TYPE_EMI_REGISTRY_ADAPTER =
            EmiRegistryAdapter.simple(GasType.class, Registries.GAS_TYPES,GasEmiStack::new);

    public static final EmiRecipeCategory PRESSER_CATEGORY
            = new EmiRecipeCategory(ResourceLocation.parse("cmatd:/presser_category"),
            PRESSER_STACK, new EmiTexture(PRESS_ICON,0,0,
            16,16,
            16,16,
            16,16));

    public static final EmiRecipeCategory JAM_MAKING_CATEGORY
            = new EmiRecipeCategory(ResourceLocation.parse("cmatd:/jam_making_category"),
            JAM_MAKER_STACK, new EmiTexture(JAM_ICON,0,0,
            16,16,
            16,16,
            16,16));

    public static final EmiRecipeCategory MASHER_CATEGORY
            = new EmiRecipeCategory(ResourceLocation.parse("cmatd:/masher_category"),
            MASHER_STACK, new EmiTexture(MASHER_ICON,0,0,
            16,16,
            16,16,
            16,16));

    @Override
    public void initialize(EmiInitRegistry registry) {
        registry.addRegistryAdapter(GAS_TYPE_EMI_REGISTRY_ADAPTER);
    }

    @Override
    public void register(EmiRegistry registry){
        registry.addCategory(PRESSER_CATEGORY);
        registry.addWorkstation(PRESSER_CATEGORY,PRESSER_STACK);

        registry.addRecipe(new EPresserRecipe("pressed_pcb",
                Ingredient.of(CmatdItem.PCB_PATTERN),
                Ingredient.of(CmatdItem.UNPRESSED_PCB),
                CmatdItem.PCB.asItem()));

        registry.addRecipe(new EPresserRecipe("plate",
                Ingredient.of(CmatdItem.PLATE_PATTERN),
                Ingredient.of(CmatdItem.INFUSED_INGOT),
                CmatdItem.PLATE.asItem()));

        registry.addCategory(JAM_MAKING_CATEGORY);
        registry.addWorkstation(JAM_MAKING_CATEGORY,JAM_MAKER_STACK);

        registry.addRecipe(new EJamMakingRecipe("glow_berry_jam",
                Ingredient.of(CmatdItem.JAM_JAR),Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.GLOW_BERRIES),
                CmatdItem.GLOW_BERRY_JAM.asItem()));

        registry.addRecipe(new EJamMakingRecipe("sweet_berry_jam",
                Ingredient.of(CmatdItem.JAM_JAR),Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.SWEET_BERRIES),
                CmatdItem.SWEET_BERRY_JAM.asItem()));

        registry.addCategory(MASHER_CATEGORY);
        registry.addWorkstation(MASHER_CATEGORY,MASHER_STACK);

        registry.addRecipe(new EMasherRecipe("mashed_potatoes",
                Ingredient.of(Items.POTATO),
                CmatdItem.MASHED_POTATOES.asItem()));

        registry.addRecipe(new EMasherRecipe("poisonous_mashed_potatoes",
                Ingredient.of(Items.POISONOUS_POTATO),
                CmatdItem.POISONOUS_MASHED_POTATOES.asItem()));
    }
}
