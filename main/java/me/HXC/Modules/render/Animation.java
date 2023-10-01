package me.HXC.Modules.render;

import net.ccbluex.liquidbounce.features.module.*;
import net.ccbluex.liquidbounce.value.*;

@ModuleInfo(
        name = "Animations",
        description = "Block Animation",
        category = ModuleCategory.RENDER
)
public class Animation extends Module {
    public String getTag() {
        return mode.get();
    }
    private static String[] renderMode = new String[] {
            "White",
            "Gay",
            "IDBUG",
            "Swang",
            "Swank",
            "Swing",
            "Swong",
            "Swaing",
            "Remix",
            "Custom",
            "Old",
            "Rotate",
            "Winter",
            "Circle",
            "Spin",
            "Slide",
            "Sigma",
            "Jigsaw",
            "Luna",
            "Jello",
            "Avatar",
            "Leain",
            "Vanilla",
            "NoSwing",
            "Autumn"
    };
    public static final ListValue mode = new ListValue("Mode", renderMode, "White");
    public static final BoolValue Smooth = new BoolValue("SmoothSwing", true);
    public static final BoolValue noFire = new BoolValue("FireEffect", true);
    public static final BoolValue EveryThingBlock = new BoolValue("EveryThingBlock", true);
    public static final BoolValue leftHand = new BoolValue("LeftHand", false);
    public static final FloatValue fireAlpha = new FloatValue("FireAlpha", 0.4F, 0.0F, 1.0F) {
        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            if(newValue > getMaximum())
                set(getMaximum());
            if(newValue < getMinimum())
                set(getMinimum());
        }
    };
    public static final FloatValue x = new FloatValue("X", 0, -1, 1);
    public static final FloatValue y = new FloatValue("Y", 0, -1, 1);
    public static final FloatValue z = new FloatValue("Z", 0.21F, -1F, 1F);
    public static final IntegerValue Speed = new IntegerValue("Speed", 10, 1, 50);
}
