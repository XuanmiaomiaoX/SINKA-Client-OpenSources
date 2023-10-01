/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.client;

import me.HXC.Utils.RenderUtils2;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.HaloClickGui;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.HaloFoxStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.LiquidBounceStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", description = "ClickGUI Settings", category = ModuleCategory.CLIENT, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUI extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[] {"LiquidBounce","Sinka"}, "Sinka") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };

    public final ListValue backgroundValue = new ListValue("Background", new String[] {"Default", "Gradient", "None"}, "None");
    public final ListValue animationValue = new ListValue("Animation", new String[] {"Azura", "Slide", "SlideBounce", "Zoom", "ZoomBounce", "None"}, "Slide");

    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.3F, 2F);

    public static FloatValue speed1 = new FloatValue("Speed", 230F, 10F, 5000F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    public static final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    public static final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    public static final IntegerValue colorBlueValue = new IntegerValue("B", 200, 0, 255);
    public static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public static final BoolValue drawlogin = new BoolValue("ThePlayerHud", true);

    int i = 0;

    @Override
    public void onEnable() {
        updateStyle();

        if (styleValue.get().toLowerCase().equalsIgnoreCase("Sinka"))
            mc.displayGuiScreen(new HaloClickGui());
        else
            mc.displayGuiScreen(LiquidBounce.clickGui);
    }

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    private void updateStyle() {
        switch(styleValue.get().toLowerCase()) {
            case "liquidbounce":
                LiquidBounce.clickGui.style = new SlowlyStyle();
                break;
            case "Sinka":
                LiquidBounce.clickGui.style = new HaloFoxStyle();
                break;
        }
    }
}
