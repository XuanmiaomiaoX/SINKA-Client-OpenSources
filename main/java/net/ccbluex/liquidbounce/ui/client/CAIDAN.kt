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

class CAIDAN(arg1: GuiMainMenu) : GuiScreen() {

    override fun initGui() {
        this.buttonList.add(GuiButton(1, this.width / 2 - 100, height - 40, "离开"))
    }
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)

        val font = Fonts.font35

        font.drawCenteredString("        *你进入了一个光线很暗的房间*", 180F, height / 8F + 70, 0xffffff, false)
        font.drawCenteredString("             ?:嘿你怎么来到这里的?", 180F, height / 8F + 70 + font.FONT_HEIGHT, 0xffffff, false)
        font.drawCenteredString("          ?:噢 这么说你是支持SINKA的吗", 180F, height / 8F + 80 + font.FONT_HEIGHT * 3, 0xffffff, false)
        font.drawCenteredString("?:其实吧...不是!你是不是以为这还是原版的彩蛋啊", 176F, height / 8 + 80F + font.FONT_HEIGHT * 4, 0xffffff, false)
        font.drawCenteredString("?:你还不知道我是谁吗 你个云黑客", 175F, height / 8F + 80 + font.FONT_HEIGHT * 5, 0xffffff, false)
        font.drawCenteredString("                   ?:跟我来", 180F, height / 8F + 80 + font.FONT_HEIGHT * 6, 0xffffff, true)
        font.drawCenteredString("                     *神秘人消失*", 180F, height / 8F + 80 + font.FONT_HEIGHT * 8, 0xffffff, false)

        super.drawScreen(mouseX, mouseY, partialTicks)

        // Title
        GL11.glScalef(2F, 2F, 2F)
        Fonts.font40.drawCenteredString("A Room", width / 2 / 2F, height / 8F / 2 + 20, Color(255, 255, 255).rgb, true)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (Keyboard.KEY_ESCAPE == keyCode)
            return

        super.keyTyped(typedChar, keyCode)
    }
    override fun actionPerformed(button: GuiButton) {
        if (button.id == 1) {
            mc.displayGuiScreen(CAIDAN2(this) as GuiScreen)
        }
    }
}