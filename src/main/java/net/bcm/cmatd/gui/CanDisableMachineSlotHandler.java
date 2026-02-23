package net.bcm.cmatd.gui;

import net.bcm.cmatd.blockentity.BaseCobbleMakerBE;
import net.bcm.cmatd.item.ModuleItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.Optional;

public class CanDisableMachineSlotHandler extends SlotItemHandler{
    boolean isDisabledSlot = false;
    final int moduleIndex;
    final boolean isModuleSlot;
    BlockEntity be;

    public CanDisableMachineSlotHandler(BlockEntity be,int moduleIndex, boolean isModuleSlot,IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.moduleIndex = moduleIndex;
        this.isModuleSlot = isModuleSlot;
        this.be = be;
    }

    @Override
    public void initialize(ItemStack stack) {
        super.initialize(stack);
        if(be instanceof BaseCobbleMakerBE){
            int tier = ((BaseCobbleMakerBE) be).getTierSettings(4);
            if(tier < 0){
                isDisabledSlot = false;
            }
            else if(tier == 0 || tier == 1){
                if(moduleIndex >= 0){
                    isDisabledSlot = true;
                }
            }
            else if(tier == 2 || tier == 3){
                if(moduleIndex >= 1){
                    isDisabledSlot = true;
                }
            }
            else if(tier == 4 || tier == 5){
                if(moduleIndex >= 2){
                    isDisabledSlot = true;
                }
            }
            else if(tier == 6 || tier == 7){
                if(moduleIndex >= 3){
                    isDisabledSlot = true;
                }
            }
            else{
                isDisabledSlot = false;
            }
        }
    }

    @Override
    public Optional<ItemStack> tryRemove(int count, int decrement, Player player) {
        return super.tryRemove(count, decrement, player);
    }

    @Override
    public boolean mayPlace(ItemStack stack){
        if(this.isModuleSlot){
            if(!stack.isEmpty() && !isDisabledSlot && stack.getItem() instanceof ModuleItem){
                return this.getItemHandler().isItemValid(index, stack);
            }
        }
        else{
            if(!stack.isEmpty() && !isDisabledSlot){
                return this.getItemHandler().isItemValid(index, stack);
            }
        }
        return false;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        if(isDisabledSlot)
            return false;
        return !this.getItemHandler().extractItem(index, 1, true).isEmpty();
    }

    @Override
    public boolean allowModification(Player player) {
        return !isDisabledSlot && this.mayPickup(player) && this.mayPlace(this.getItem());
    }
}
