package net.bcm.cmatd;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;
import java.util.stream.Stream;

import static net.minecraft.util.Mth.floor;

public class Utility{
    // AARRGGBB - the color format for some MC systems, which in reality (and referred to as)/is ARGB in HEX format binary
    // think RGB color + Alpha transparency
    public static final int BRIGHT_LIGHT_BLUE = 0xff00aeff; // top grad
    public static final int DARKER_BLUE = 0xff0023b4; // bottom grad
    public static final int DARKEST_GRAYER_BLUE = 0xff0d1433; // bg

    public static final int BAD_STATE_RED = 14230793;
    public static final int GOOD_STATE_GREEN = 645443;

    public static final int BAD_WARNING_RED = 16714808;
    public static final int BAD_WARNING_YELLOW = 16773176;
    public static final int GOOD_OK_GREEN = 651320;

    // integer color format for use in some MC systems, like text and color boxes that can't have/don't need alpha transparency
    public static final int INT_RED = 16711680;
    public static final int INT_YELLOW = 16776960;
    public static final int INT_GREEN = 65280;
    public static final int INT_CYAN = 65535;
    public static final int INT_BLUE = 255;
    public static final int INT_MAGENTA = 16711935;
    public static final int INT_WHITE = 16777215;
    public static final int INT_BLACK = 0;
    private static final Throwable BAD_HEX_COLOR_CODE = new Throwable("bad HEX color String provided as argument");

    /**
     * An integer to RGB color converter, useful to convert MC integer values to rendering red, green and blue values;
     * alpha is not supported here
     * @param color The integer color to be processed
     * @param index The type of value desired, which can be 0 -> red, 1 -> green, or 2 -> blue
     * @return The value (r,g or b) returned after processing
     */
    public static int intToRGB(int color,int index){
        Color lazyColor = new Color(color);
        switch(index){
            case 0 -> {
                return lazyColor.getRed();
            }
            case 1 -> {
                return lazyColor.getGreen();
            }
            case 2 -> {
                return lazyColor.getBlue();
            }
            default -> {
                Cmatd.getLogger().debug("Bad index selected for Color converter! Index is: {} but should be 0 1 or 2",index);
                return -1; // invalid index constant -1
            }
        }
    }

    /**
     * Normalize a range to another range
     * (stackoverflow Yun, edited to convert integers to floats unchecked) <a href="https://stackoverflow.com/questions/929103/convert-a-number-range-to-another-range-maintaining-ratio">...</a>
     * This
     * @param x Takes in a value
     * @param inMin the minimum old range value (integer)
     * @param inMax the maximum old range value (integer)
     * @param outMin the minimum new range value (float)
     * @param outMax the maximum new range value (float)
     * @return the result of normalizing 'x' with the new float range
     */
    public static float normalizeIntToFloatValue(int x, int inMin, int inMax, float outMin, float outMax) {
        float outRange = outMax - outMin;
        int inRange  = inMax - inMin;
        return (x - inMin) * outRange / inRange + outMin;
    }

    /**
     * Normalize a range to another range
     * (stackoverflow Yun, edited to convert doubles to floats unchecked)
     * Doubles cannot convert to floats perfectly, so use with caution
     * @param x
     * @param inMin
     * @param inMax
     * @param outMin
     * @param outMax
     * @return the result of normalizing 'x' with the new float range
     */
    public static float normalizeDoubleToFloatValue(double x, double inMin, double inMax, float outMin, float outMax){
        float outRange = outMax - outMin;
        double inRange = inMax - inMin;
        double value = (x - inMin) * outRange / inRange + outMin;
        return (float)value;
    }

    /**
     * Convert Hexadecimal strings to integers (with shorthand support)
     * @param hex The string value of a hexadecimal number
     * @return The result of processing the hexadecimal input into an integer
     */
    public static int hexToInt(String hex){
        // for normal length that a hex code should be
        if(hex.length() >= 10){
            hex = "0x" + hex.substring(4,10);
        }// normal length codes
        else if (hex.length() == 8){
            // perfectly safe
        }// for shorthand hex codes
        else if(hex.length() == 3){
            hex = "0x" + hex.charAt(0) + hex.charAt(0) + hex.charAt(1) + hex.charAt(1) + hex.charAt(2) + hex.charAt(2);
        }
        else{
            Cmatd.getLogger().error(
                    "Hex color doesn't follow any known format! Value given: {}", hex);
            throw new IllegalArgumentException("Hex Colors cannot deviate from the required length of 10 or 3 characters!",BAD_HEX_COLOR_CODE);
        }

        if(!(hex.charAt(0) == '0' && hex.charAt(1) == 'x')){
            Cmatd.getLogger().error("Hex color doesn't follow full format, as such, 0xffffff has been selected as the hex instead.");
            hex = "0xffffff";
        }
        return Integer.decode(hex);
    }

    // strings used for various things
    public static final String HAS_ITEM = "has_item";

    // other misc things
    public static final int MACHINE_SOUND_DISTANCE = 6; // not used outside datagen
    public static final VoxelShape DOUBLE_TALL_BLOCK_SHAPE_ALL = Block.box(0.0D,0.0D,0.0D,16.0D,32.0D,16.0D);
    public static final int FOOD_REACTOR_FLUID_CAPACITY = 100000; // in Mb
    public static int LIGHTNING_GENERATOR_ENERGY_RATE = 1_000_000; // in FE

    public static int MAX_CONDUIT_ENERGY_TRANSFER_RATE = 500; // in FE
    public static int MAX_CONDUIT_ENERGY_CAPACITY = 1_000; // in FE

    // Use GUs for calculations (1 air block is worth 1 GU)
    public static int MAX_CONDUIT_GAS_TRANSFER_RATE = 1_000;
    public static int MAX_CONDUIT_GAS_CAPACITY = 10_000;

    public static final float DEFINITIVE_LIMIT_PRECISE_FLOAT_ZERO = 0.0000000f;
    public static final VoxelShape GAS_TANK_ALL = Stream.of(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(4, 2, 4, 12, 14, 12),
            Block.box(6, 6, 0, 10, 10, 16),
            Block.box(0, 6, 6, 16, 10, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final VoxelShape CENTER_CONDUIT_CONNECTOR = Block.box(4, 4, 4, 12, 12, 12);
    public static final VoxelShape NORTH_CONDUIT_BLOCK = Shapes.join(Block.box(2, 2, 0, 14, 14, 1),
            Block.box(5, 5, 1, 11, 11, 4), BooleanOp.OR);
    public static final VoxelShape WEST_CONDUIT_BLOCK = Shapes.join(Block.box(0, 2, 2, 1, 14, 14),
            Block.box(1, 5, 5, 4, 11, 11), BooleanOp.OR);
    public static final VoxelShape EAST_CONDUIT_BLOCK = Shapes.join(Block.box(15, 2, 2, 16, 14, 14),
            Block.box(12, 5, 5, 15, 11, 11), BooleanOp.OR);
    public static final VoxelShape SOUTH_CONDUIT_BLOCK = Shapes.join(Block.box(2, 2, 15, 14, 14, 16),
            Block.box(5, 5, 12, 11, 11, 15), BooleanOp.OR);
    public static final VoxelShape DOWN_CONDUIT_BLOCK = Shapes.join(Block.box(2, 0, 2, 14, 1, 14),
            Block.box(5, 1, 5, 11, 4, 11), BooleanOp.OR);
    public static final VoxelShape UP_CONDUIT_BLOCK = Shapes.join(Block.box(2, 15, 2, 14, 16, 14),
            Block.box(5, 12, 5, 11, 15, 11), BooleanOp.OR);

    public static final VoxelShape NORTH_CONDUIT = Block.box(5, 5, 0, 11, 11, 4);
    public static final VoxelShape WEST_CONDUIT = Block.box(0, 5, 5, 4, 11, 11);
    public static final VoxelShape EAST_CONDUIT = Block.box(12, 5, 5, 16, 11, 11);
    public static final VoxelShape SOUTH_CONDUIT = Block.box(5, 5, 12, 11, 11, 16);
    public static final VoxelShape DOWN_CONDUIT = Block.box(5, 0, 5, 11, 4, 11);
    public static final VoxelShape UP_CONDUIT = Block.box(5, 12, 5, 11, 16, 11);



    // MATH

    // check for divide by zero possibility and make sure we don't divide a bigger number by a smaller one
    public static int divisionIntSplit(int top, int divider){
        if(divider != 0 && !(top > divider)){
            return top / divider;
        }
        else{
            return 1;
        }
    }

    public static double divisionDoubleSplit(double top, double divider){
        if(divider != 0.0D && !(top > divider)){
            return top / divider;
        }
        else{
            return 1.0D;
        }
    }

    public static float lerpDiscrete(float delta, float start, float end){
        float i = end - start;
        return start + floor(delta * (i - 1)) + (delta > 0.0F ? 1 : 0);
    }
}
