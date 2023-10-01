/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.ClientSettings
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import java.awt.Color

class GuiBackground(val prevGui: GuiScreen) : GuiScreen() {

    companion object {
        var enabled = true
        var particles = false
        var bg = 1
        var bgN = "Sinka"
    }
    private lateinit var enabledButton: GuiButton
    private lateinit var particlesButton: GuiButton

    override fun initGui() {
        //Revise Wallpaper
        buttonList.add(GuiIconButton(4, width/2 - 100, height/2 + 50, 95, 20, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //Back
        buttonList.add(GuiIconButton(0, width/2 + 5, height/2 + 50, 95, 20, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            4 -> {
                (LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).bgValue.value++
            }
            0 -> mc.displayGuiScreen(prevGui)
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        when((LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).bgValue.get()) {
            1 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg.png"), 0, 0, this.width, this.height)
                bgN="Sinka"
            }
            2 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg2.png"), 0, 0, this.width, this.height)
                bgN="Gradient"
            }
            3 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg3.png"), 0, 0, this.width, this.height)
                bgN="Gemstone"
            }
            4 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg4.png"), 0, 0, this.width, this.height)
                bgN="Sinka-Gemstone"
            }
        }
        if((LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).bgValue.get() in 1..4){

        }else{
            (LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).bgValue.value=1
        }
        RoundedUtils.drawGradientRoundLR( width/2 - 100f, height/2 + 50f, 95f, 20f, 5.0F, Color(255,195,230, 210), Color(170,230,255, 230))
        RoundedUtils.drawGradientRoundLR( width/2 + 5f, height/2 + 50f, 95f, 20f, 5.0F, Color(255,255,255),Color(255,255,255))

        Fonts.roboto100.drawCenteredString("WallPaper", this.width / 2F, height /2-70f, Color.white.rgb, false)
        Fonts.roboto40.drawCenteredString("Name: "+bgN, this.width / 2F, height /2-35f, Color.white.rgb, false)

        Fonts.font35.drawCenteredString("Revise", this.width / 2F-52f, height /2 +57f, Color.WHITE.rgb, false)
        Fonts.font35.drawCenteredString("Back", this.width / 2F+54f, height /2 +57f, Color(90,90,90).rgb, false)

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (Keyboard.KEY_ESCAPE == keyCode) {
            mc.displayGuiScreen(prevGui)
            return
        }

        super.keyTyped(typedChar, keyCode)
    }

}