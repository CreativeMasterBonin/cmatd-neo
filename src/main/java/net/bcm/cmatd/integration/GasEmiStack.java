package net.bcm.cmatd.integration;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.api.GasStack;
import net.bcm.cmatd.api.GasType;
import net.bcm.cmatd.api.Registries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("deprecated")
public class GasEmiStack extends EmiStack {
    private final Holder<GasType> gas;
    private final DataComponentPatch patch;
    private final ResourceLocation blockTexture = ResourceLocation.parse("minecraft:textures/block/white_concrete.png");

    public GasEmiStack(GasStack gasStack){
        this.gas = gasStack.getGasHolder();
        this.amount = gasStack.getAmount();
        this.patch = gasStack.getComponentsPatch();
    }

    public GasEmiStack(Holder<GasType> gas, long amount, DataComponentPatch patch){
        this.gas = gas;
        this.amount = amount;
        this.patch = patch;
    }

    public GasEmiStack(GasType gasType, DataComponentPatch dataComponentPatch, long l) {
        this.gas = gasType.getRegistryHolder();
        this.patch = dataComponentPatch;
        this.amount = l;
    }

    @Override
    public EmiStack copy() {
        GasEmiStack gasEmiStackCopy = new GasEmiStack(this.gas,this.amount,this.patch);
        gasEmiStackCopy.setChance(this.chance);
        gasEmiStackCopy.setRemainder(this.getRemainder().copy());
        gasEmiStackCopy.comparison = this.comparison;
        return gasEmiStackCopy;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float delta, int flags){
        if(gas != null){
            RenderSystem.enableBlend();
            guiGraphics.blit(blockTexture,x,y,0,0,0,
                    16,16,
                    16,16);

            RenderSystem.setShaderColor(Utility.intToRGB(gas.value().getColor(),0),
                    Utility.intToRGB(gas.value().getColor(),1),
                    Utility.intToRGB(gas.value().getColor(),2),
                    1);

            RenderSystem.disableBlend();
        }
    }

    @Override
    public boolean isEmpty() {
        return amount <= 0 || gas.is(Registries.EMPTY_GAS_KEY);
    }

    @Override
    public DataComponentPatch getComponentChanges() {
        return getComponentChanges();
    }

    @Override
    public Object getKey() {
        return gas.value();
    }

    @Override
    public ResourceLocation getId() {
        ResourceKey<GasType> gasKey = gas.getKey();
        return gasKey == null ? Registries.EMPTY_GAS_KEY.location() : gasKey.location();
    }

    @Override
    public List<Component> getTooltipText() {
        if(isEmpty()){
            return Collections.emptyList();
        }
        List<Component> components = new ArrayList<>();
        components.add(getName());

        return components;
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> clientTooltipList = getTooltipText().stream()
                .map(EmiTooltipComponents::of)
                .collect(Collectors.toList());
        if(this.amount > 1){
            MutableComponent component = null;
            component.append(Component.translatable("title.gas_amt",this.amount)
                    .withStyle(ChatFormatting.GRAY));
            clientTooltipList.add(EmiTooltipComponents.of(component));
        }

        EmiTooltipComponents.appendModName(clientTooltipList,getId().getNamespace());
        clientTooltipList.addAll(super.getTooltip());
        return clientTooltipList;
    }

    @Override
    public Component getName() {
        return gas.value().getDescription();
    }
}
