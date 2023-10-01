/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.ClientSettings
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import java.awt.Color

class GuiWarning(val prevGui: GuiScreen) : GuiScreen() {

    companion object {
        var enabled = true
        var particles = false
        var bg = 1
        var bgN = "Sinka"
        var isKnow = false
    }
    private lateinit var enabledButton: GuiButton
    private lateinit var particlesButton: GuiButton

    override fun initGui() {
        //Revise Wallpaper
        buttonList.add(GuiIconButton(4, width/2 - 30, height/2, 60, 20, I18n.format("", *arrayOfNulls(0)), ResourceLocation("liquidbounce/head/omg.png")))
        //Back
    }
    val guimainmenu = GuiMainMenu()
    val msTimer = MSTimer()
    val msTimer1 = MSTimer()
    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            4 -> {
                isKnow=true
                LiquidBounce.tipSoundManager.omg.asyncPlay()
                mc.displayGuiScreen(prevGui)
            }
        }
    }
    var i = 0
    var isAir = false
    var air = 255
    var air1 = 20
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if(msTimer.hasTimePassed(5)&&i<300){
            i++
        }
        if(isAir&&air>0&&msTimer.hasTimePassed(5)){
            air=air-2
            if(air<4){
                air=4
            }
            msTimer1.reset()
        }
        if(air<30&&air1<250){
            air1=air1+2
        }
        when ((LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings?)!!.bgValue.get()) {
            1 -> RenderUtils.drawImage(
                ResourceLocation("liquidbounce/background/bg.png"), 0, 0,
                width,
                height
            )

            2 -> RenderUtils.drawImage(
                ResourceLocation("liquidbounce/background/bg2.png"), 0, 0,
                width,
                height
            )

            3 -> RenderUtils.drawImage(
                ResourceLocation("liquidbounce/background/bg3.png"), 0, 0,
                width,
                height
            )

            4 -> RenderUtils.drawImage(
                ResourceLocation("liquidbounce/background/bg4.png"), 0, 0,
                width,
                height
            )
        }
        RoundedUtils.drawGradientRoundLR( 0f, 0f, width.toFloat(), height.toFloat(), 0.0F, Color(0,0,0,air), Color(0,0,0,air))
        if(i<150){
            Fonts.regular90.drawCenteredString("Welcome! to",width/2f,height/2f,Color.WHITE.rgb,false)
        }else{
            Fonts.regular90.drawCenteredString("SINKA",width/2f,height/2f,Color.WHITE.rgb,false)
        }
        if((LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).bgValue.get() in 1..4){

        }else{
            (LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).bgValue.value=1
        }
        if(i==300){
            isAir=true
            if(air<30){
                isKnow=true
                LiquidBounce.tipSoundManager.omg.asyncPlay()
                mc.displayGuiScreen(prevGui)
            }
        }
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