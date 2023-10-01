package net.ccbluex.liquidbounce.features.module.modules.client;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.clickgui.ColorManagerGui;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "HUDSettings", description = "butter Settings.", category = ModuleCategory.CLIENT, keyBind = Keyboard.KEY_M, canEnable = false)
public class HUDSettings extends Module {
    @Override
    public void onEnable() {
        mc.displayGuiScreen(new ColorManagerGui());
    }
}
