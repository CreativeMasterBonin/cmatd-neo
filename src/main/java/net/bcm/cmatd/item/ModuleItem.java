package net.bcm.cmatd.item;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Components;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ModuleItem extends Item{
    final boolean isBlankModule;
    final boolean isGoldenModule;

    public ModuleItem(Properties properties) {
        super(properties.stacksTo(4).fireResistant());
        this.isBlankModule = false;
        this.isGoldenModule = false;
    }

    public ModuleItem(Properties p, boolean blankModule){
        super(p.stacksTo(4).fireResistant());
        this.isBlankModule = blankModule;
        this.isGoldenModule = false;
    }

    public ModuleItem(Properties properties,boolean blankModule,boolean goldenModule) {
        super(properties.stacksTo(4).fireResistant());
        this.isBlankModule = blankModule;
        this.isGoldenModule = goldenModule;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag){
        // golden modules
        if(isGoldenModule){
            if(isBlankModule){
                tooltipComponents.add(Component.translatable("tooltip.module.golden_useless")
                        .withStyle(ChatFormatting.GRAY));
                return;
            }
            else{
                if(stack.has(Components.MODULE_TYPE)){
                    switch (stack.get(Components.MODULE_TYPE)){
                        case 6 -> {
                            tooltipComponents.add(Component.translatable("tooltip.module.aio_basic")
                                    .withStyle(ChatFormatting.GOLD));
                            break;
                        }
                        case 7 -> {
                            tooltipComponents.add(Component.translatable("tooltip.module.aio_advanced")
                                    .withStyle(ChatFormatting.GOLD));
                            break;
                        }
                        case null -> {
                            Cmatd.getLogger().error("{}: Illegal golden MODULE_TYPE specified, type is null", stack.toString());
                            break;
                        }
                        default -> {
                            tooltipComponents.add(Component.translatable("tooltip.module.golden_useless")
                                    .withStyle(ChatFormatting.GRAY));
                            break;
                        }
                    }
                }
            }
            return;
        }

        // standard modules

        if(isBlankModule){
            tooltipComponents.add(Component.translatable("tooltip.module.blank_module")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }

        if(stack.has(Components.MODULE_TYPE)){
            switch(stack.get(Components.MODULE_TYPE)){
                case 0 -> {
                    tooltipComponents.add(Component.translatable("tooltip.module.speedup")
                            .withStyle(ChatFormatting.GRAY));
                    break;
                }
                case 1 -> {
                    tooltipComponents.add(Component.translatable("tooltip.module.efficiency")
                            .withStyle(ChatFormatting.GRAY));
                    break;
                }
                case 2 -> {
                    tooltipComponents.add(Component.translatable("tooltip.module.doubler")
                            .withStyle(ChatFormatting.GRAY));
                    break;
                }
                case 3 -> {
                    tooltipComponents.add(Component.translatable("tooltip.module.tripled")
                            .withStyle(ChatFormatting.GRAY));
                    break;
                }
                case 4 -> {
                    tooltipComponents.add(Component.translatable("tooltip.module.heat_dispersion")
                            .withStyle(ChatFormatting.GRAY));
                    break;
                }
                case 5 -> {
                    tooltipComponents.add(Component.translatable("tooltip.module.silencing")
                            .withStyle(ChatFormatting.GRAY));
                    break;
                }
                case null -> {
                    Cmatd.getLogger().error("{}: Illegal MODULE_TYPE specified, type is null", stack.toString());
                    break;
                }
                default -> {
                    tooltipComponents.add(Component.translatable("tooltip.module.useless")
                            .withStyle(ChatFormatting.GRAY));
                    break;
                }
            }
        }
        else{
            tooltipComponents.add(Component.translatable("tooltip.module.useless")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
