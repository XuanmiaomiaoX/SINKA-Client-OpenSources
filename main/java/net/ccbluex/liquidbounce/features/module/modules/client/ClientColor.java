/*
 * Code by mimosa
 */
package net.ccbluex.liquidbounce.features.module.modules.client;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClientColor", description = "e", category = ModuleCategory.COLOR, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClientColor extends Module {
    public ModuleCategory moduleCategory = ModuleCategory.COMBAT;
    public static final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    public static final IntegerValue colorGreenValue = new IntegerValue("G", 150, 0, 255);
    public static final IntegerValue colorBlueValue = new IntegerValue("B", 200, 0, 255);
    public static final IntegerValue colorRedValue2 = new IntegerValue("R2", 52, 0, 255);
    public static final IntegerValue colorGreenValue2 = new IntegerValue("G2", 250, 0, 255);
    public static final IntegerValue colorBlueValue2 = new IntegerValue("B2", 255, 0, 255);
    public static final ListValue ColorMode =new ListValue("ColorMode", new String[] {"Pink-Blue", "Purple-Blue","Green-Blue","Yellow-Blue","RedPink","Pink","Blue","Green","Purple","Red","White","custom"}, "Pink-Blue");

    public static ListValue rColorMode =new ListValue("BackgroundColorMode", new String[] {"White","Grey","Black"}, "Black");

    public static ListValue clickMode =new ListValue("ClickGuiColor", new String[] {"White","Grey"}, "White");

    public static BoolValue Alpha = new BoolValue("Alpha",true);
    public static ListValue rAlphaMode =new ListValue("BackgroundAlphaMode", new String[] {"100", "70","50","25"}, "50");
    //无则是背景 1这是字体反色 2是ClickGui的差距bg 3是ClickGui底色 4是click字体
    public static int red = 255;
    public static int green = 255;
    public static int blue = 255;
    public static int red1 = 0;
    public static int green1 = 0;
    public static int blue1 = 0;
    public static int air1 = 230;
    public static int air2 = 230;
    public static int red2 = 255;
    public static int green2 = 255;
    public static int blue2 = 255;

    public static int red3 = 255;
    public static int green3 = 255;
    public static int blue3 = 255;
    public static int red4 = 255;
    public static int green4 = 255;
    public static int blue4 = 255;
    public static boolean WB=true;
}
