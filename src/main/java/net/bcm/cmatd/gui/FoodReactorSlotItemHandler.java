package net.bcm.cmatd.gui;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FoodReactorSlotItemHandler extends SlotItemHandler{
    // gives this slot a unique name
    public final String type;

    public FoodReactorSlotItemHandler(String type, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.type = type;
    }

    // this did nothing but cause pain
    /*@Override
    public ItemStack safeInsert(ItemStack stack){
        Holder<Item> itemHolder = stack.getItemHolder();
        FoodReactorFuels foodReactorFuels = itemHolder.getData(FOOD_REACTOR_FUELS);
        boolean invalidFuel = (foodReactorFuels == null);

        if(Objects.equals(type, "coolant") && !stack.is(Tag.VALID_FOOD_REACTOR_COOLANTS)){
            return stack;
        }
        else if(Objects.equals(type, "coolant") && stack.is(Tag.VALID_FOOD_REACTOR_COOLANTS)){
            return super.safeInsert(stack);
        }

        if(Objects.equals(type, "fuel") && invalidFuel){
            return stack;
        }
        else if(Objects.equals(type, "fuel") && !invalidFuel){
            return super.safeInsert(stack);
        }

        return super.safeInsert(stack);
    }

    @Override
    public ItemStack safeInsert(ItemStack stack, int increment){
        Holder<Item> itemHolder = stack.getItemHolder();
        FoodReactorFuels foodReactorFuels = itemHolder.getData(FOOD_REACTOR_FUELS);
        boolean invalidFuel = (foodReactorFuels == null);

        if(Objects.equals(type, "coolant") && !stack.is(Tag.VALID_FOOD_REACTOR_COOLANTS)){
            return stack;
        }
        else if(Objects.equals(type, "coolant") && stack.is(Tag.VALID_FOOD_REACTOR_COOLANTS)){
            return super.safeInsert(stack, increment);
        }

        if(Objects.equals(type, "fuel") && invalidFuel){
            return stack;
        }
        else if(Objects.equals(type, "fuel") && !invalidFuel){
            return super.safeInsert(stack, increment);
        }

        return super.safeInsert(stack, increment);
    }*/
}
