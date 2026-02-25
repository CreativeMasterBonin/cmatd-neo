package net.bcm.cmatd;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import java.util.logging.Logger;

@OnlyIn(Dist.CLIENT)
public class CmatdClientActionHandler {
    public static boolean keyMappingPressed(KeyMapping inputMapping){
        if(inputMapping.isUnbound()){
            return false;
        }
        else if(inputMapping.getKeyConflictContext().isActive() && inputMapping.getKeyModifier().isActive(inputMapping.getKeyConflictContext())){
            InputConstants.Key key = inputMapping.getKey();
            // key must not be unknown
            if(key.getValue() != InputConstants.UNKNOWN.getValue()){
                try{
                    if(key.getType() == InputConstants.Type.KEYSYM){
                        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),key.getValue());
                    }
                    else if(key.getType() == InputConstants.Type.MOUSE){
                        return GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(),key.getValue()) == 1;
                    }
                }
                catch (Exception windowExc){
                    Logger.getAnonymousLogger().info("CMATD encountered an error when processing keyMappingPressed: " + windowExc.getMessage());
                }
            }
        }
        else{
            InputConstants.Key key = inputMapping.getKey();
            boolean returningValue = false; // the value that will be used in the 'and' statement that will determine whether a key is pressed
            if(key.getValue() != InputConstants.UNKNOWN.getValue()){
                try{
                    if(key.getType() == InputConstants.Type.KEYSYM){
                        returningValue = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),key.getValue());
                    }
                    else if(key.getType() == InputConstants.Type.MOUSE){
                        returningValue =  GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(),key.getValue()) == 1;
                    }
                }
                catch (Exception windowExc){
                    Logger.getAnonymousLogger().info("CMATD encountered an error when processing keyMappingPressed: " + windowExc.getMessage());
                }
            }

            return KeyModifier.isKeyCodeModifier(inputMapping.getKey()) && returningValue;
        }
        return false; // keys that fail to work or are invalid consider a key unpressed
    }
}
