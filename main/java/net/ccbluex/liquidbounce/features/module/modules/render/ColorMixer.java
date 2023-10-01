/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 *
 * This code belongs to WYSI-Foundation. Please give credits when using this in your repository.
 */
/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.BlendUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import java.awt.Color;

@ModuleInfo(name = "ColorMixer", description = "Mix two colors together.", category = ModuleCategory.RENDER, canEnable = false)
public class ColorMixer extends Module {

    public static IntegerValue col1RedValue = new IntegerValue("Color1-Red", 255, 0, 255);
    public static IntegerValue col1GreenValue = new IntegerValue("Color1-Green", 255, 0, 255);
    public static IntegerValue col1BlueValue = new IntegerValue("Color1-Blue", 255, 0, 255);

    public static IntegerValue col2RedValue = new IntegerValue("ClientColor-Red", 255, 0, 255);
    public static IntegerValue col2GreenValue = new IntegerValue("ClientColor-Green", 255, 0, 255);
    public static IntegerValue col2BlueValue = new IntegerValue("ClientColor-Blue", 255, 0, 255);

    public static Color getMixedColor(int index, int seconds) {
        Color col1 = new Color(col1RedValue.get(), col1GreenValue.get(), col1BlueValue.get());
        Color col2 = new Color(col2RedValue.get(), col2GreenValue.get(), col2BlueValue.get());
        return BlendUtils.blendColors(new float[] {0, 0.5F, 1F}, new Color[] {col1, col2, col1}, (System.currentTimeMillis() + index) % (seconds * 1000) / (float) (seconds * 1000));
    }
}