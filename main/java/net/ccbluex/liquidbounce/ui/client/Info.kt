/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color

class Info(arg1: GuiMainMenu) : GuiScreen() {

    override fun initGui() {
        this.buttonList.add(GuiButton(1, this.width / 2 - 100, height - 40, "Back"))
    }
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)

        Fonts.font35.drawString("1,整体颜色调节在Color列表中的ClientColor里调节,包括arraylist,为了防止颜色不统一而混乱", 0F, 30F, Color(255,255,255).getRGB(), false)
        Fonts.font35.drawString("2,当反作弊拉回时某些移动功能会关闭,请在Client列表里的ClientSettings里关闭AutoDisable", 0F, 40F, 0xffffff, false)
        Fonts.font35.drawString("3,当我们获得胜利时会触发自动广告,我们可以点AutoPraise里的AutoSinka即可", 0F, 50F, 0xffffff, false)
        Fonts.font35.drawString("4,反馈问题可以加QQ:3388389190 我们会定期查看的 大概2天", 0F, 60F, 0xffffff, false)
        Fonts.font35.drawString("5,ClickGui已修复水影看不到下面的问题 并添加新UI", 0F, 70F, 0xffffff, false)
        Fonts.font35.drawString("6,虽然一般,但是我们永久免费,倒卖请联系我们", 0F, 80F, 0xffffff, false)
        Fonts.font35.drawString("Dev:Mimosa QQ:3124008465 or 3388389190", 0F, 100F, 0xffffff, false)
        super.drawScreen(mouseX, mouseY, partialTicks)

        // Title
        GL11.glScalef(2F, 2F, 2F)
        RenderUtils.drawImage(ResourceLocation("liquidbounce/text/ToughGrass2.png"), 0, 0, 30, 15)
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