package net.bcm.cmatd.api;

import net.bcm.cmatd.Utility;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

/**
 * From NeoForge, changed to only be a Gas with no inworld variant
 */
@SuppressWarnings("deprecated")
public class GasType {
    // tracking size of RL Set of GasType registries
    public static final Lazy<Integer> SIZE = Lazy.of(() -> Registries.GAS_TYPES.keySet().size());
    private final Holder.Reference<GasType> registryHolder = Registries.GAS_TYPES.createIntrusiveHolder(this);


    private String descriptionId;
    private Rarity rarity;
    private int density;
    private boolean radioactive;
    private float radioactivity;
    private int temperature;
    private int color;

    /**
     * A constructor for a GasType, taking a GasType.Properties object as a parameter
     * @param properties A non-mutable {net.bcm.cmatd.api#GasType.Properties} object;
     *     -
     *     -         - example use: {@code new GasType.Properties(Rarity.EPIC,-1,false,0.2f,4,16777215)}
     */
    public GasType(final Properties properties){
        this.descriptionId = properties.descriptionId;
        this.rarity = properties.rarity;
        this.density = properties.density;
        this.radioactive = properties.radioactive;
        this.radioactivity = properties.radioactivity;
        this.temperature = properties.temperature;
        this.color = properties.color;
    }

    public Holder.Reference<GasType> getRegistryHolder() {
        return this.registryHolder;
    }

    /**
     * Returns the name of the GasType as a namespace and location String
     * @return A String that is either the defined name (key) of this GasType or if null, returns a predefined String
     */
    public String toString() {
        @Nullable
        ResourceLocation name = Registries.GAS_TYPES.getKey(this);
        return name != null ? name.toString() : "Unregistered CMATD GasType";
    }

    /**
     * Returns the name of the GasType as a translated key String (used in inventories, chat, or otherwise text-based areas)
     * @return returns a MutableComponent (not a regular Component, of which can be translated)
     */
    public MutableComponent getDescription(){
        return Component.translatable(this.getDescriptionId());
    }

    public MutableComponent getDescription(GasStack stack){
        return Component.translatable(this.getDescriptionId());
    }

    /**
     * Can be used to check if the gas is light or not
     * @return A boolean that is true if the gas is lighter than air
     */
    public boolean isLightGas(){
        return this.density <= 0;
    }

    /**
     * Can be used to check if the gas is heavy or not
     * @return A boolean that is true if the gas is heavier than air
     */
    public boolean isHeavyGas(){
        return this.density > 0;
    }

    /**
     * Used for getting the name of the GasType internally
     * @return returns a string
     */
    public String getDescriptionId(){
        if(this.descriptionId == null){
            this.descriptionId = Util.makeDescriptionId("gas",
                    Registries.GAS_TYPES.getKey(this));
        }
        return this.descriptionId;
    }

    // getters

    public Rarity getRarity(){
        return this.rarity;
    }

    public int getDensity(){
        return this.density;
    }

    public boolean isRadioactive(){
        if(this.getRadioactivity() <= Utility.DEFINITIVE_LIMIT_PRECISE_FLOAT_ZERO){
            return false;
        }
        return this.radioactive;
    }

    public float getRadioactivity(){
        return this.radioactivity;
    }

    public int getTemperature(){
        return this.temperature;
    }

    public int getColor(){
        return this.color;
    }

    /**
     * Properties that define what the gas is like, and it's behavior, and how hard it is to get and work with
     */
    public static final class Properties {
        private String descriptionId;
        private Rarity rarity = Rarity.COMMON;
        private int density = 0;
        private boolean radioactive = false;
        private float radioactivity = 0.0f;
        private int temperature = 0;
        private int color = Utility.INT_WHITE;

        /**
         * A no parameters version of a properties instance constructor for gas types;
         * This takes no parameters in and creates properties for a common, air-like, non-radioactive, room temperature gas
         */
        public Properties(){}

        /**
         * The properties definition instance of the current gas type
         *
         * @param rarity The rarity of the gas
         * @param density The weight of the gas (like fluids) <= 0 is lighter than air, > 0 is heavier than air
         * @param radioactive Whether the gas is dangerous to expose to open air or not
         * @param radioactivity The potency of the radioactive nature of the gas (0.5 is 0.5sv Units a Second, 0.0 is no radioactivity)
         * @param temperature The heat of the gas; negative temperatures are cooler gases, while higher than 0 temperatures are hotter gases,
         *                    0 means it is the same as air, or 'neutral' temperature
         * @param color The color defines the look of the gas; needs to be an integer color see {@net.bcm.cmatd.Utility}
         */
        public Properties(Rarity rarity, int density, boolean radioactive, float radioactivity, int temperature, int color){
            this.rarity = rarity;
            this.density = density;
            this.radioactive = radioactive;
            this.radioactivity = radioactivity;
            this.temperature = temperature;
            this.color = color;
        }

        /**
         * Creates and returns a pre-made basic {@net.bcm.cmatd#GasType.Properties} object
         * @return returns a basic Properties holder
         */
        public static Properties create(){
            return new Properties();
        }

        /**
         * Returns the properties holder after setting a String for the description of this GasType;
         * this is basically the name of the GasType, not to be confused with its ID/ResourceKey
         * @param descriptionId A string representing the GasType's name
         * @return returns the holder for Properties
         */
        public Properties descriptionId(String descriptionId){
            this.descriptionId = descriptionId;
            return this;
        }

        /**
         * Rarity {@net.minecraft.world.item.Rarity} determines how rare this gas is (usually reserved for items);
         * if a gas is EPIC, it is hard to obtain a massive amount of, while a COMMON gas is easy to obtain much of
         * @param rarity A Rarity enum value usually reserved for items; determines how easy it is to obtain or find this gas
         * @return returns the holder for Properties
         */
        public Properties rarity(Rarity rarity){
            this.rarity = rarity;
            return this;
        }

        /**
         * Gas density determines if the gas is lighter or heavier than air,
         * which airs' density is 0 for modded usage
         * @param density An integer (negative or positive) representing the 'weight' of the gas compared to air (which is 0);
         *                -1 would be lighter and 1 would be heavier than air
         * @return returns the holder for Properties
         */
        public Properties gasDensity(int density){
            this.density = density;
            return this;
        }

        /**
         * A Radioactive gas has the ability to harm things around it,
         * or emit invisible ionizing particles of some form that mutate objects,
         * or just break down into smaller particles in general
         * @param radioactive A boolean representing whether the gas is toxic (long-term) to the world and entities;
         *                    the actual implementation is unfinished, however set the radioactive parameter correctly anyway
         * @return returns the holder for Properties
         */
        public Properties radioactive(boolean radioactive){
            this.radioactive = radioactive;
            return this;
        }

        /**
         * Radioactivity is the amount of sv Units per second this gas will emit if it is radioactive;
         * so a value of 0.5 means ~0.5sv units per second will be emitted by this gas, and 0 meaning nothing is emitted
         * @param radioactivity A float (positive only)
         * @return returns the holder for Properties
         */
        public Properties radioactivity(float radioactivity){
            if(radioactivity < Utility.DEFINITIVE_LIMIT_PRECISE_FLOAT_ZERO){
                radioactivity = Utility.DEFINITIVE_LIMIT_PRECISE_FLOAT_ZERO;
            }
            this.radioactivity = radioactivity;
            return this;
        }

        /**
         * Temperature determines how hot or cold this gas is, useful for reactors which a gaseous coolant
         * or whether a gas is a good fuel source; 0 means it is the same temperature as air
         * @param temperature An integer (negative or positive) representing the heat of the gas
         * @return returns the holder for Properties
         */
        public Properties temperature(int temperature){
            this.temperature = temperature;
            return this;
        }

        /**
         * Color determines what the gas will look like (color-only) in guis and inventories
         * @param color An integer representation of a color
         * @return returns the holder for Properties
         */
        public Properties color(int color){
            this.color = color;
            return this;
        }
    }
}
