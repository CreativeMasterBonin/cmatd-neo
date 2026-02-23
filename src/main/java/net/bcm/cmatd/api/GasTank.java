package net.bcm.cmatd.api;

import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Utility;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.system.NonnullDefault;

import java.util.List;
import java.util.function.Predicate;

/**
 * FluidTank From NeoForge, changed to support GasStack, and it's methods and abilities
 *
 */
public class GasTank implements IGasHandler, IGasTank{
    private Predicate<GasStack> validator;
    public GasStack gas = GasStack.EMPTY;
    public int capacity;
    public final List<GasType> supportedGasTypes;
    public final List<GasType> drainSupportGasTypes;
    public final boolean limitedGasStorage;
    public final boolean limitedDrainGases;
    public final boolean insertOnly;
    public final boolean extractOnly;


    public GasTank(int capacity){
        this(capacity,validator -> true);
    }

    public GasTank(int capacity, GasStack gasStack){
        this(capacity);
        this.gas = gasStack;
    }

    public GasTank(int capacity, Predicate<GasStack> validator){
        this.capacity = capacity;
        this.setValidator(validator);
        this.supportedGasTypes = List.of();
        this.drainSupportGasTypes = List.of();
        this.limitedGasStorage = false;
        this.limitedDrainGases = false;
        this.insertOnly = false;
        this.extractOnly = false;
    }

    public GasTank(int capacity,boolean limitedGasStorage, List<GasType> supportedGasTypes){
        this.capacity = capacity;
        this.limitedGasStorage = limitedGasStorage;
        this.supportedGasTypes = supportedGasTypes;
        this.drainSupportGasTypes = List.of();
        this.limitedDrainGases = false;
        this.validator = validator -> true;
        this.insertOnly = false;
        this.extractOnly = false;
    }

    public GasTank(int capacity,boolean limitedGasStorage, List<GasType> supportedGasTypes, boolean limitedDrainGases, List<GasType> drainSupportGasTypes){
        this.capacity = capacity;
        this.limitedGasStorage = limitedGasStorage;
        this.supportedGasTypes = supportedGasTypes;
        this.limitedDrainGases = limitedDrainGases;
        this.drainSupportGasTypes = drainSupportGasTypes;
        this.validator = validator -> true;
        this.insertOnly = false;
        this.extractOnly = false;
    }

    public GasTank(int capacity, boolean insertOnly, boolean extractOnly, boolean limitedGasStorage, List<GasType> supportedGasTypes, boolean limitedDrainGases, List<GasType> drainSupportGasTypes){
        this.capacity = capacity;
        this.limitedGasStorage = limitedGasStorage;
        this.supportedGasTypes = supportedGasTypes;
        this.limitedDrainGases = limitedDrainGases;
        this.drainSupportGasTypes = drainSupportGasTypes;
        this.validator = validator -> true;
        this.insertOnly = insertOnly;
        this.extractOnly = extractOnly;
    }

    public GasTank(int capacity, int insertOrExtractOnly, boolean limitedGasStorage, List<GasType> supportedGasTypes, boolean limitedDrainGases, List<GasType> drainSupportGasTypes){
        this.capacity = capacity;
        this.limitedGasStorage = limitedGasStorage;
        this.supportedGasTypes = supportedGasTypes;
        this.limitedDrainGases = limitedDrainGases;
        this.drainSupportGasTypes = drainSupportGasTypes;
        this.validator = validator -> true;
        switch(insertOrExtractOnly){
            case 0 -> {
                this.insertOnly = true;
                this.extractOnly = false;
            }
            case 1 -> {
                this.insertOnly = false;
                this.extractOnly = true;
            }
            default -> {
                this.insertOnly = false;
                this.extractOnly = false;
            }
        }
    }

    public GasTank(int capacity, int insertOrExtractOnly){
        this.capacity = capacity;
        this.limitedGasStorage = false;
        this.supportedGasTypes = List.of();
        this.limitedDrainGases = false;
        this.drainSupportGasTypes = List.of();
        this.validator = validator -> true;
        switch(insertOrExtractOnly){
            case 0 -> {
                this.insertOnly = true;
                this.extractOnly = false;
            }
            case 1 -> {
                this.insertOnly = false;
                this.extractOnly = true;
            }
            default -> {
                this.insertOnly = false;
                this.extractOnly = false;
            }
        }
    }

    public GasTank(int capacity,boolean limitedGasStorage, boolean limitedDrainGases){
        this.capacity = capacity;
        this.limitedGasStorage = limitedGasStorage;
        this.supportedGasTypes = List.of();
        this.limitedDrainGases = limitedDrainGases;
        this.drainSupportGasTypes = List.of();
        this.validator = validator -> true;
        this.insertOnly = false;
        this.extractOnly = false;
    }

    // we are not using this right now, implement a custom class to change behavior
    @Override
    public int getGasTanks() {
        return 1;
    }

    /**
     * Replace the current GasStack in the tank with a new GasStack
     * Mostly an experimental method
     * @param type The GasType to place in the new tank
     * @param amount The amount of gas to place into the new tank (cannot be 0)
     */
    public void setNewGasStack(GasType type, int amount){
        assert !(amount <= 0): "GasStack amount cannot be <= 0!";
        this.gas = new GasStack(type,amount);
    }

    /**
     * Set the current GasStack in the tank to a new predefined one
     * @param gasStack The GasStack instance, throws an exception if amount <= 0
     */
    public void setGas(GasStack gasStack, boolean bypassCheck){
        if(bypassCheck){
            this.gas = gasStack;
            return;
        }

        if(gasStack.getAmount() <= 0){
            throw new RuntimeException("GasStack amount cannot be <= 0!");
        }
        this.gas = gasStack;
    }

    /**
     * Set the current GasTank capacity to a specified integer size
     * @param capacity The integer size of the tank, throws an exception if capacity <= 0!
     */
    public void setCapacity(int capacity){
        if(capacity <= 0){
            throw new RuntimeException("GasTank capacity cannot be <= 0!");
        }
        this.capacity = capacity;
    }

    /**
     * Sets the validator for the current GasTank
     * @param validator Must be valid
     */
    public void setValidator(Predicate<GasStack> validator){
        if(validator != null){
            this.validator = validator;
        }
    }

    /**
     * A standalone version of the getGasStack method
     * @return returns the GasTanks' current GasStack
     */
    public GasStack getGasStackStandalone(){
        return this.gas;
    }

    /**
     * A standalone version of the getGasTankCapacity method
     * @return returns the GasTanks' current capacity
     */
    public int getGasTankCapacityStandalone(){
        return this.capacity;
    }

    @Override
    public GasStack getGasStack(int gasTank) {
        return getGasStackStandalone();
    }

    @Override
    public int getGasTankCapacity(int gasTank) {
        return getGasTankCapacityStandalone();
    }

    @Override
    public boolean isGasValid(int gasTank, GasStack gasStack) {
        return validator.test(gasStack);
    }

    @NonnullDefault
    public boolean canGasBeInsertedIntoTank(GasStack gasStack){
        if(limitedGasStorage){
            return supportedGasTypes.contains(gasStack.getGas());
        }
        else{
            return true;
        }
    }

    public boolean canGasBeDrainedFromTank(GasStack gasStack){
        if(limitedDrainGases){
            return drainSupportGasTypes.contains(gasStack.getGas());
        }
        else{
            return true;
        }
    }

    /**
     * Can be used to validate a GasStack
     * @param gasStack A valid GasStack to test with
     * @return returns the GasStack test result
     */
    public boolean isGasValid(GasStack gasStack) {
        return validator.test(gasStack);
    }

    @Override
    public GasStack getGasStack() {
        return this.gas;
    }

    @Override
    public int getGasAmount() {
        return this.gas.getAmount();
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Tests the GasTanks' GasStack with the validator
     * @return returns the GasStack test result
     */
    @Override
    public boolean isGasValid() {
        return validator.test(this.gas);
    }


    /**
     * A method to fill tanks with gas
     * @param gasStack The stack to be tested then manipulated
     * @param simulate Whether the stack is actually manipulated, or just a fake version is
     * @return the result of simulation or real manipulation
     */
    @Override
    public int fill(GasStack gasStack, boolean simulate) {
        // if gas is valid and not empty, continue
        if(gasStack.isEmpty() || !isGasValid(gasStack)){
            return 0;
        }

        if(!canGasBeInsertedIntoTank(gasStack)){
            return 0;
        }

        if(extractOnly){
            return 0;
        }

        // fake change of Gas Stack
        if(simulate){
            if(gasStack.isEmpty()){
                return Math.min(capacity,gasStack.getAmount());
            }
            if(!GasStack.isSameGasSameComponents(gas,gasStack)){
                return 0;
            }
            return Math.min(capacity - gas.getAmount(), gasStack.getAmount());
        } // real change of gas stack
        else{
            if(gas.isEmpty()){
                gas = gasStack.copyWithAmount(Math.min(capacity,gasStack.getAmount()));
                update();
                return gas.getAmount();
            }
            if(!GasStack.isSameGasSameComponents(gas,gasStack)){
                return 0; // not the same gas, so reject it and return 0
            }

            int fillGas = capacity - gas.getAmount();

            if(gas.getAmount() < fillGas){
                gas.add(gas.getAmount());
                fillGas = gas.getAmount(); // set the temp variable to the amount we just updated
            }
            else{
                gas.setAmount(capacity); // sets the gas to the capacity of the tank
            }
            if(fillGas > 0){
                update();
                return fillGas;
            }
        }
        return -1;
    }

    @Override
    public int fill(int amount, boolean simulate) {
        if(simulate){
            return -1;
        }
        return amount;
    }

    /**
     * A secondary method to drain gas out of the GasTank; this one will completely remove all gas
     * @param gasStack The GasStack to manipulate
     * @param simulate Whether to actually manipulate the gas or do a fake run
     * @return the result of simulation or real manipulation
     */
    @Override
    public GasStack drain(GasStack gasStack, boolean simulate) {
        // we won't be using simulation here
        if(simulate || gasStack.isEmpty() || !GasStack.isSameGasSameComponents(gasStack,gas)){
            return GasStack.EMPTY;
        }
        else{
            return drain(gasStack.getAmount(),false);
        }
    }

    /** A no backtrack method to dump all gas while checking if it is radioactive
     *
     */
    public void dumpGas(Player player){
        boolean radioactive = gas.getGas().isRadioactive();
        boolean radioactivityIsHigh = gas.getGas().getRadioactivity() > Utility.DEFINITIVE_LIMIT_PRECISE_FLOAT_ZERO;
        boolean isEveryTypeRadioactive = radioactive && radioactivityIsHigh;
        if(radioactive || radioactivityIsHigh || isEveryTypeRadioactive){
            player.displayClientMessage(Component.translatable("message.dumping_attempt.gas_is_radioactive")
                    .withStyle(ChatFormatting.RED),true);
            return;
        }
        else{
            this.gas = new GasStack(Gases.EMPTY,0);
            update();
        }
    }

    /**
     * A method to drain a certain amount of gas out of the GasTank
     * @param amount The amount to take out of the GasTanks' GasStack
     * @param simulate Whether to actually manipulate the gas or just do a fake run
     * @return returns a GasStack after manipulation or simulation
     */
    @Override
    public GasStack drain(int amount, boolean simulate) {
        if(simulate){
            if(limitedDrainGases){
                if(!canGasBeDrainedFromTank(this.getGasStack())){
                    return GasStack.EMPTY;
                }
            }

            if(insertOnly){
                return GasStack.EMPTY;
            }
            // if ok to drain, continue
            return new GasStack(gas.getGas(),gas.getAmount() - amount); // a fake GasStack
        }
        else{
            if(limitedDrainGases){
                if(!canGasBeDrainedFromTank(this.getGasStack())){
                    return GasStack.EMPTY;
                }
            }

            if(insertOnly){
                return GasStack.EMPTY;
            }

            // if ok to drain, continue
            if(gas.getAmount() < amount){
                amount = gas.getAmount();
            }
            GasStack gasStack2 = gas.copyWithAmount(amount); // copy the changed GasStack
            if(amount > 0){
                gas.remove(amount);
                update();
            }
            return gasStack2;
        }
    }

    /**
     * A method to drain the gas from the tank without returning anything (respects GasType rules, no simulation allowed)
     * @param amount the amount to remove from the gas tank
     */
    public void drain(int amount){
        if(limitedDrainGases){
            if(!canGasBeDrainedFromTank(this.getGasStack())){
                return;
            }
        }

        if(gas.getAmount() < amount){
            amount = gas.getAmount();
        }
        if(amount > 0){
            gas.remove(amount);
            update();
        }
    }

    /**
     * A way to update an Object using a GasTank if needed
     * To add custom behavior, extend {@net.bcm.cmatd.GasTank} and override this method
     */
    public void update(){};

    public CompoundTag save(HolderLookup.Provider provider, CompoundTag tag){
        tag.putString("gas",gas.getGas().toString());
        tag.putInt("gas_amount",gas.getAmount());
        return tag;
    }

    public void load(HolderLookup.Provider provider, CompoundTag tag){
        if (tag.getString("gas").isBlank() || tag.getString("gas").isEmpty() || tag.getString("gas").equals("cmatd:empty")) {
            return;
        }

        String name = tag.getString("gas");

        if(Registries.GAS_TYPES.containsKey(ResourceLocation.parse(tag.getString("gas")))){
            gas = new GasStack(Registries.GAS_TYPES.get(ResourceLocation.parse(tag.getString("gas"))),
                    tag.getInt("gas_amount"));
        }
        else{
            Cmatd.getLogger().debug("GasTank has unknown gas type: {}",name);
        }
    }
}
