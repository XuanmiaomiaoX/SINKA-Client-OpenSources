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

class GuiWelcome : GuiScreen() {

    override fun initGui() {
        this.buttonList.add(GuiButton(1, this.width / 2 - 100, height - 40, "Ok"))
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)

        val font = Fonts.font35

        font.drawCenteredString("Thank you for downloading and installing our client!", width / 2F, height / 8F + 70, 0xffffff, true)
        font.drawCenteredString("Here is some information you might find useful if you are using LiquidBounce for the first time.", width / 2F, height / 8F + 70 + font.FONT_HEIGHT, 0xffffff, true)

        super.drawScreen(mouseX, mouseY, partialTicks)

        // Title
        GL11.glScalef(2F, 2F, 2F)
        Fonts.font40.drawCenteredString("Welcome!", width / 2 / 2F, height / 8F / 2 + 20, Color(255,255, 255).rgb, true)
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