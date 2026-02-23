package net.bcm.cmatd.api;

import com.mojang.blaze3d.platform.NativeImage;
import net.bcm.cmatd.Utility;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeneratedTestAtlasSprite {
    private static NativeImage generateDebugColoredImage(int width, int height) {
        NativeImage nativeimage = new NativeImage(width, height, false);

        int firstColor = (int)Math.round((Math.random() * (double)Utility.INT_WHITE));
        int secondColor = (int)Math.round((Math.random() * (double)Utility.INT_WHITE));

        for (int k = 0; k < height; k++) {
            for (int l = 0; l < width; l++) {
                if (k < height / 2 ^ l < width / 2) {
                    nativeimage.setPixelRGBA(l, k, firstColor);
                } else {
                    nativeimage.setPixelRGBA(l, k, secondColor);
                }
            }
        }

        return nativeimage;
    }
}
