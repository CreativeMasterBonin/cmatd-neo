package net.bcm.cmatd.api;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.util.DataComponentUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("deprecated")
/*
 * This is like the FluidStack and by nature, the ItemStack class
 * Only use this for Gases in inventories
 */
public class GasStack implements MutableDataComponentHolder {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final GasStack EMPTY = new GasStack(null,null);
    private int amount;
    private final GasType gas;
    private final PatchedDataComponentMap components;

    // codecs
    public static final Codec<Holder<GasType>> GAS_NON_EMPTY_CODEC = Registries.GAS_TYPES.holderByNameCodec().validate(holder -> {
        return holder.is(Gases.EMPTY.getRegistryHolder()) ? DataResult.error(() -> {
            return "Gas must not be cmatd:empty";
        }) : DataResult.success(holder);
    });
    public static final Codec<GasStack> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(
                    instance -> instance.group(
                                    GAS_NON_EMPTY_CODEC.fieldOf("id").forGetter(GasStack::getGasHolder),
                                    ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(GasStack::getAmount),
                                    DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                                            .forGetter(stack -> stack.components.asPatch()))
                            .apply(instance, GasStack::new)));


    public GasStack(GasType gas, PatchedDataComponentMap components) {
        this.gas = gas;
        this.components = components;
    }

    public GasStack(Holder<GasType> gas, int amount){
        this(gas.value(),amount);
    }

    public GasStack(GasType gasType, int amount){
        this(gasType,amount,new PatchedDataComponentMap(DataComponentMap.EMPTY));
    }

    public GasStack(Holder<GasType> holder, int amount, DataComponentPatch patch){
        this(holder.value(),amount,PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY,patch));
    }

    private GasStack(GasType value, int amount, PatchedDataComponentMap typedDataComponents) {
        this.gas = value;
        this.amount = amount;
        this.components = typedDataComponents;
    }

    public static Codec<GasStack> fixedAmountCodec(int amount) {
        return Codec.lazyInitialized(
                () -> RecordCodecBuilder.create(
                        instance -> instance.group(
                                        GAS_NON_EMPTY_CODEC.fieldOf("id").forGetter(GasStack::getGasHolder),
                                        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                                                .forGetter(stack -> stack.components.asPatch()))
                                .apply(instance, (holder, patch) -> new GasStack(holder, amount, patch))));
    }

    public void setGasType(GasType gasType){
        return;
    }

    public GasType getGas() {
        return this.isEmpty() ? Gases.EMPTY : this.gas;
    }

    public Holder<GasType> getGasHolder(){
        return Holder.direct(this.gas);
    }

    public boolean isEmpty(){
        return this == EMPTY || this.gas == Gases.EMPTY || this.amount <= 0;
    }

    public static Optional<GasStack> parse(HolderLookup.Provider lookupProvider, Tag tag) {
        return CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial(error -> LOGGER.error("Tried to load invalid gas: '{}'", error));
    }

    public static GasStack parseOptional(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        return tag.isEmpty() ? EMPTY : parse(lookupProvider, tag).orElse(EMPTY);
    }

    public GasStack split(int amount) {
        int i = Math.min(amount, this.amount);
        GasStack gasStack = this.copyWithAmount(i);
        this.remove(i);
        return gasStack;
    }

    public GasStack copyAndClear() {
        if (this.isEmpty()) {
            return EMPTY;
        }
        else {
            GasStack gasStack = this.copy();
            this.setAmount(0); // set stack to 0
            return gasStack;
        }
    }

    /**
     * Sets the amount of the GasStack to a split difference (by two) of the current amount
     * The split stack is separated from the main stack as a half of the first stack, which can be used to put in another place, or voided
     * @param simulate Whether to do a fake run
     * @return returns a new fake GasStack or the split stack
     */
    public GasStack splitByTwo(boolean simulate){
        if(simulate){
            return new GasStack(this.gas,(int)Math.round(this.amount / 2.0D),this.components.copy());
        }
        // this stack if it had 100, would be copied with 100.
        // 100 is the starter amount. As a rule, if it is split into two groups and the two split numbers are equal...
        // ...then the split was done correctly and so an even split of two numbers will be returned, one overriding the existing amount of gas
        GasStack splitAmountStack = this.copy(); // the copied stack
        int checkAmount = this.amount; // the gas amount to check against
        double uncheckedDoubleAmount = this.amount / 2.0D; // the dividing of the gas amount by two

        double uniformAmount = uncheckedDoubleAmount + uncheckedDoubleAmount; // the addition sum of the split number

        // if the addition sum is the same as the existing unsplit stack amount, and it has no remainder, the stack can be split evenly and will be updated
        if(uniformAmount == checkAmount && uniformAmount % 2 == 0){
            splitAmountStack.setAmount((int)Math.round(uncheckedDoubleAmount));
            this.setAmount((int)uncheckedDoubleAmount);
        }
        return splitAmountStack; // return the split stack (whether the split was successful or not)
    }

    // tags and such
    public boolean is(TagKey<GasType> tag) {
        return this.getGas().getRegistryHolder().is(tag);
    }

    public boolean is(Predicate<Holder<GasType>> holderPredicate) {
        return holderPredicate.test(this.getGasHolder());
    }

    public boolean is(Holder<GasType> holder) {
        return is(holder.value());
    }

    public boolean is(HolderSet<GasType> holderSet) {
        return holderSet.contains(this.getGasHolder());
    }

    public Stream<TagKey<GasType>> getTags() {
        return this.getGas().getRegistryHolder().tags();
    }
    // end tags and such

    public Tag save(HolderLookup.Provider lookupProvider, Tag prefix) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty GasStack");
        } else {
            return DataComponentUtil.wrapEncodingExceptions(this, CODEC, lookupProvider, prefix);
        }
    }

    // save to tag
    public Tag save(HolderLookup.Provider lookupProvider) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty GasStack");
        }
        else {
            return DataComponentUtil.wrapEncodingExceptions(this, CODEC, lookupProvider);
        }
    }

    // save to tag and empty ones too
    public Tag saveOptional(HolderLookup.Provider lookupProvider) {
        return this.isEmpty() ? new CompoundTag() : this.save(lookupProvider, new CompoundTag());
    }



    public GasStack copy() {
        if (this.isEmpty()) {
            return EMPTY;
        }
        else {
            return new GasStack(this.gas, this.amount, this.components.copy());
        }
    }


    public GasStack copyWithAmount(int amount) {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            GasStack gasStack = this.copy();
            gasStack.setAmount(amount);
            return gasStack;
        }
    }

    // if all components, count and gasType matches another stack
    public static boolean matches(GasStack first, GasStack second) {
        if (first == second) {
            return true;
        }
        else {
            return first.getAmount() != second.getAmount() ? false : isSameGasSameComponents(first, second);
        }
    }

    public static boolean isSameGas(GasStack first, GasStack second) {
        return first.is(second.getGas());
    }

    public static boolean isSameGasSameComponents(GasStack first, GasStack second) {
        if (!first.is(second.getGas())) {
            return false;
        }
        else {
            return first.isEmpty() && second.isEmpty() ? true : Objects.equals(first.components, second.components);
        }
    }

    public static MapCodec<GasStack> lenientOptionalFieldOf(String fieldName) {
        return CODEC.lenientOptionalFieldOf(fieldName)
                .xmap(optional -> optional.orElse(EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));
    }


    public static int hashGasAndComponents(@Nullable GasStack stack) {
        if (stack != null) {
            int i = 31 + stack.getGas().hashCode();
            return 31 * i + stack.getComponents().hashCode();
        } else {
            return 0;
        }
    }

    /**
     * Determines if the gas can be inserted into a tank
     * @param overrideRadioactiveRequirement Bypasses the requirement that this stack not be radioactive
     * @return returns true or false depending on requirements
     */
    public boolean canBeInsertedIntoTank(boolean overrideRadioactiveRequirement){
        if(overrideRadioactiveRequirement){
            return true;
        }
        return this.getGas().getRadioactivity() <= 0.0f;
    }


    // mutable stuff
    @Override
    public <T> @Nullable T set(DataComponentType<? super T> componentType, @Nullable T value) {
        return this.components.set(componentType, value);
    }

    @Override
    public <T> @Nullable T remove(DataComponentType<? extends T> componentType) {
        return this.components.remove(componentType);
    }

    @Override
    public void applyComponents(DataComponentPatch patch) {
        this.components.applyPatch(patch);
    }

    @Override
    public void applyComponents(DataComponentMap components) {
        this.components.setAll(components);
    }

    // legacy method kept for safety
    public Component getHoverName() {
        return getGas().getDescription(this);
    }

    public int getAmount() {
        return this.isEmpty() ? 0 : this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void limitSize(int amount) {
        if (!this.isEmpty() && this.getAmount() > amount) {
            this.setAmount(amount);
        }
    }

    public GasStack limitedSizeStack(int amount){
        if(!this.isEmpty() && this.getAmount() > amount){
            return new GasStack(this.gas,amount,this.components.copy());
        }
        return this;
    }

    /**
     * Creates a GasStack Mutable list with a specific amount of duplicate (of the current stack) stacks and returns it
     * @param count The number of GasStacks to create
     * @return returns a compacted List of type GasStack
     */
    public List<GasStack> makeArrayOfSize(int count){
        List<GasStack> stackArray = new ArrayList<>(List.of());

        for(int c = 0; c < count; c++){
            stackArray.add(new GasStack(this.gas,this.amount,this.components.copy()));
        }
        return stackArray;
    }

    /**
     * Copies the GasStack components and amount to a new stack, but replaces the GasType with a provided one
     * @param gasType The GasType for the returned stack to have
     * @return returns a GasStack that does not have the same gas as the current stack
     */
    public GasStack copyWithGasType(GasType gasType){
        return new GasStack(gasType,this.amount,this.components.copy());
    }

    /**
     * Copies the GasStack GasType and amount to a new stack, but replaces the component map with a provided one
     * @param map The PatchedDataComponentMap for the returned stack to have
     * @return returns a GasStack that does not have the same Components as the current stack
     */
    public GasStack copyWithComponents(PatchedDataComponentMap map){
        return new GasStack(this.gas,this.amount,map);
    }


    public void add(int addedAmount) {
        this.setAmount(this.getAmount() + addedAmount);
    }
    public void remove(int removedAmount) {
        this.add(-removedAmount);
    }


    public boolean is(GasType type){
        return getGas() == type;
    }

    @Override
    public PatchedDataComponentMap getComponents() {
        return components;
    }

    public DataComponentPatch getComponentsPatch() {
        return !this.isEmpty() ? this.components.asPatch() : DataComponentPatch.EMPTY;
    }

    public boolean isComponentsPatchEmpty() {
        return !this.isEmpty() ? this.components.isPatchEmpty() : true;
    }

    // legacy like systems
    public boolean isGasEqual(GasStack gasStack){
        return this.getGas() == gasStack.getGas();
    }
}
