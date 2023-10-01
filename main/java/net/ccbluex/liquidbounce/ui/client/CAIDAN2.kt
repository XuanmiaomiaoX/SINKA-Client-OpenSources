/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.ui.font.Fonts
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color

class CAIDAN2(arg2: CAIDAN) : GuiScreen() {

    override fun initGui() {
        this.buttonList.add(GuiButton(1, this.width / 2 - 100, height - 40, "离开房间"))
    }
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)

        val font = Fonts.font35

        font.drawCenteredString("                      ?:这就想走?", 180F, height / 8F + 70, 0xffffff, false)
        font.drawCenteredString("             *你感受到了危险逼近*", 180F, height / 8F + 70 + font.FONT_HEIGHT, 0xffffff, false)
        font.drawCenteredString("         愚人节快乐",180F, height / 8F + 80 + font.FONT_HEIGHT * 3, 0xffffff, false)
        super.drawScreen(mouseX, mouseY, partialTicks)

        // Title
        GL11.glScalef(2F, 2F, 2F)
        Fonts.font40.drawCenteredString("???", width / 2 / 2F, height / 8F / 2 + 20, Color(255, 0, 0).rgb, true)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (Keyboard.KEY_ESCAPE == keyCode)
            return

        super.keyTyped(typedChar, keyCode)
    }
    override fun actionPerformed(button: GuiButton) {
        if (button.id == 1) {
            mc.displayGuiScreen(GuiMainMenu())
        }
    }
}