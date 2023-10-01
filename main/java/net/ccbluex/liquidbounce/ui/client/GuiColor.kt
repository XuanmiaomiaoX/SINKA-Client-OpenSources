/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.features.module.modules.client.ClientSettings
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import java.awt.Color

class GuiColor(val prevGui: GuiScreen) : GuiScreen() {

    companion object {
        var enabled = true
        var particles = false

        var red1 = 255
        var green1 = 255
        var blue1 = 255
        var red2 = 200
        var green2 = 200
        var blue2 = 200
        var isOK = false

        var color = "Pink-Blue"
    }
    private lateinit var enabledButton: GuiButton
    private lateinit var particlesButton: GuiButton

    override fun initGui() {
        //Revise Wallpaper
        buttonList.add(GuiIconButton(4, width/2 - 100, height -40, 95, 20, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //Back
        buttonList.add(GuiIconButton(0, width/2 + 5, height -40, 95, 20, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))


        //Color
        //pink-blue
        buttonList.add(GuiIconButton(1, 30, 60, 30, 30, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //purple-blue
        buttonList.add(GuiIconButton(2, 100, 60, 30, 30, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //yellow-blue
        buttonList.add(GuiIconButton(3, 170, 60, 30, 30, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))

        //green-blue
        buttonList.add(GuiIconButton(5, 30, 140, 30, 30, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //redPink
        buttonList.add(GuiIconButton(6, 100, 140, 30, 30,  I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //pink
        buttonList.add(GuiIconButton(7, 170, 140, 30, 30, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))

        //blue
        buttonList.add(GuiIconButton(8, 30, 220, 30, 30, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //green
        buttonList.add(GuiIconButton(9, 100, 220, 30, 30, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //purple
        buttonList.add(GuiIconButton(10, 170, 220, 30, 30, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))

    }
    var msTimer1 = MSTimer()
    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            4 -> {
                ClientColor.ColorMode.value=color
                isOK=true
                LiquidBounce.tipSoundManager.omg.asyncPlay()
            }
            0 -> mc.displayGuiScreen(prevGui)

            1 -> {
                color = "Pink-Blue"
                red1 = 255
                green1 = 180
                blue1 = 230
                red2 = 170
                green2 = 235
                blue2 = 255
            }

            2 -> {
                color = "Purple-Blue"
                red1 = 130
                green1 = 243
                blue1 = 255
                red2 = 195
                green2 = 141
                blue2 = 255
            }

            3 -> {
                color = "Yellow-Blue"
                red1 = 113
                green1 = 255
                blue1 = 253
                red2 = 252
                green2 = 255
                blue2 = 0
            }

            5 -> {
                color = "Green-Blue"
                red1 = 202
                green1 = 255
                blue1 = 153
                red2 = 100
                green2 = 255
                blue2 = 231
            }

            6 -> {
                color = "RedPink"
                red1 = 255
                green1 = 103
                blue1 = 141
                red2 = 255
                green2 = 255
                blue2 = 255
            }

            7 -> {
                color = "Pink"
                red1 = 255
                green1 = 160
                blue1 = 200
                red2 = 255
                green2 = 255
                blue2 = 255
            }

            8 -> {
                color = "Blue"
                red1 = 52
                green1 = 243
                blue1 = 250
                red2 = 255
                green2 = 255
                blue2 = 255
            }

            9 -> {
                color = "Green"
                red1 = 200
                green1 = 255
                blue1 = 153
                red2 = 255
                green2 = 255
                blue2 = 255
            }

            10 -> {
                color = "Purple"
                red1 = 195
                green1 = 141
                blue1 = 255
                red2 = 255
                green2 = 255
                blue2 = 255
            }
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        when((LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).bgValue.get()) {
            1 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg.png"), 0, 0, this.width, this.height)
            }
            2 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg2.png"), 0, 0, this.width, this.height)
            }
            3 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg3.png"), 0, 0, this.width, this.height)
            }
            4 -> {
                net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(ResourceLocation("liquidbounce/background/bg4.png"), 0, 0, this.width, this.height)
            }
        }
        if(isOK&&!msTimer1.hasTimePassed(2000)){
            Fonts.font35.drawCenteredString("Success!",width/2f,height-90f,Color.GREEN.rgb,true)
        }else{
            isOK=false
            msTimer1.reset()
        }
        RoundedUtils.drawGradientRoundLR( 0f, 0f, width.toFloat(), height.toFloat(), 0.0F, Color(0,0,0, 70), Color(0,0,0, 70))
        //color ball
        //pink-blue
        RoundedUtils.drawGradientRoundLR( 30f, 60f, 30f, 30f, 15.0F, Color(255,170,230, 255), Color(170,230,255, 255))
        //purple-blue
        RoundedUtils.drawGradientRoundLR( 100f, 60f, 30f, 30f, 15.0F, Color(130,243,255, 255), Color(195,141,255, 255))
        //yellow-blue
        RoundedUtils.drawGradientRoundLR( 170f, 60f, 30f, 30f, 15.0F, Color(113,255,254, 255), Color(252,255,0, 255))

        //green-blue
        RoundedUtils.drawGradientRoundLR( 30f, 140f, 30f, 30f, 15.0F, Color(202,255,152, 255), Color(100,255,231, 255))
        //redPink
        RoundedUtils.drawGradientRoundLR( 100f, 140f, 30f, 30f, 15.0F, Color(255,103,141, 255), Color(255,255,255, 255))
        //pink
        RoundedUtils.drawGradientRoundLR( 170f, 140f, 30f, 30f, 15.0F, Color(255,195,230, 255), Color(255,255,255, 255))

        //blue
        RoundedUtils.drawGradientRoundLR( 30f, 220f, 30f, 30f, 15.0F, Color(52,242,250, 255), Color(255,255,255, 255))
        //green
        RoundedUtils.drawGradientRoundLR( 100f, 220f, 30f, 30f, 15.0F, Color(200,255,153, 255), Color(255,255,255, 255))
        //purple
        RoundedUtils.drawGradientRoundLR( 170f, 220f, 30f, 30f, 15.0F, Color(195,141,255, 255), Color(255,255,255, 255))

        //choose
        RoundedUtils.drawGradientRoundLR( width-182f, 58f, 134f, 134f, 67F, Color(255, 255, 255, 255), Color(255, 255, 255, 255))

        RoundedUtils.drawGradientRoundLR( width-180f, 60f, 130f, 130f, 65F, Color(red1, green1, blue1, 255), Color(red2, green2, blue2, 255))

        //over
        Fonts.roboto100.drawString("ColorFinder", 10f, 10f, Color.white.rgb, false)
        Fonts.roboto40.drawCenteredString("Color: "+color, width-180f+65, 60f+150f, Color.white.rgb, false)

        RoundedUtils.drawGradientRoundLR( width/2 - 100f, height -40f, 95f, 20f, 5.0F, Color(255,195,230, 210), Color(170,230,255, 230))

        Fonts.font35.drawCenteredString("Choose", this.width / 2F-52f, height-33f, Color.WHITE.rgb, false)

        RoundedUtils.drawGradientRoundLR( width/2 + 5f, height -40f, 95f, 20f, 5.0F, Color(255,255,255),Color(255,255,255))

        Fonts.font35.drawCenteredString("Back", this.width / 2F+54f, height -33f, Color(90,90,90).rgb, false)

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