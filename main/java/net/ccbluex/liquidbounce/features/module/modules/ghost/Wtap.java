/*
 * Code by mimosa
 */
package net.ccbluex.liquidbounce.features.module.modules.ghost;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "WTap", description = "WtapPVP", category = ModuleCategory.GHOST)
public class Wtap extends Module {
    int nmsl;
    @Override
    public void onDisable() {
        mc.gameSettings.keyBindForward.pressed=false;
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        nmsl++;
        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            if ((!(mc.currentScreen instanceof GuiContainer)) && (!(mc.currentScreen instanceof GuiChat)) && (!(mc.currentScreen instanceof GuiScreen))) {
                if ((mc.objectMouseOver != null) && (mc.objectMouseOver.entityHit != null) && ((mc.objectMouseOver.entityHit instanceof EntityLivingBase))) {
                    if (nmsl >= 1) {
                        mc.gameSettings.keyBindForward.pressed=true;
                        if (nmsl >= 2) {
                            nmsl = 0;
                        }
                    }
                    if (nmsl == 0) {
                        mc.gameSettings.keyBindForward.pressed=false;
                    }
                }
            }
        } else if (!mc.gameSettings.keyBindUseItem.isKeyDown()) {
            mc.thePlayer.itemInUseCount = 0;
        }
    }
}
