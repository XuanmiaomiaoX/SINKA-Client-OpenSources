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
import net.minecraft.client.gui.GuiOptions
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import java.awt.Color

class GuiSettings(val prevGui: GuiScreen) : GuiScreen() {

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
        buttonList.add(GuiIconButton(4, width/2 - 100, height/2 + 30, 95, 20, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //Back
        buttonList.add(GuiIconButton(0, width/2 + 5, height/2 + 30, 95, 20, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))

        buttonList.add(GuiIconButton(2, width/2 - 100, height/2 + 60, 200, 20, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            4 -> {
                mc.displayGuiScreen(GuiOptions(this as GuiScreen, mc.gameSettings) as GuiScreen)

            }
            0 -> mc.displayGuiScreen(GuiColor(this) as GuiScreen)
            2 -> mc.displayGuiScreen(prevGui)
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        when((LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).bgValue.get()) {
            1 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg.png"), 0, 0, this.width, this.height)
                GuiBackground.bgN ="Sinka"
            }
            2 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg2.png"), 0, 0, this.width, this.height)
                GuiBackground.bgN ="Gradient"
            }
            3 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg3.png"), 0, 0, this.width, this.height)
                GuiBackground.bgN ="Gemstone"
            }
            4 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg4.png"), 0, 0, this.width, this.height)
                GuiBackground.bgN ="Sinka-Gemstone"
            }
        }
        RoundedUtils.drawGradientRoundLR( width/2 - 100f, height/2 + 30f, 95f, 20f, 5.0F, Color(255,195,230, 210), Color(170,230,255, 230))
        RoundedUtils.drawGradientRoundLR( width/2 + 5f, height/2 + 30f, 95f, 20f, 5.0F, Color(255,230,200),Color(255,195,230))
        RoundedUtils.drawGradientRoundLR( width/2 - 100f, height/2 + 60f, 200f, 20f, 5.0F, Color(230,230,230),Color(230,230,230))

        Fonts.roboto100.drawCenteredString("Client-Settings", this.width / 2F, height /2-70f, Color.white.rgb, false)

        Fonts.font35.drawCenteredString("Minecraft", this.width / 2F-52f, height /2 +37f, Color(255,255,255).rgb, false)
        Fonts.font35.drawCenteredString("Color", this.width / 2F+54f, height /2 +37f, Color(255,255,255).rgb, false)
        Fonts.font35.drawCenteredString("Back", this.width / 2f+1, height /2 +67f, Color(90,90,90).rgb, false)

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