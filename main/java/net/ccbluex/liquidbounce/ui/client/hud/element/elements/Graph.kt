
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.client.Minecraft
import java.awt.Color

/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Graph")
class Graph(x: Double = 0.0, y: Double = 0.0, scale: Float = 1F) : Element(x, y, scale) {
    /**
     * Draw element
     */
    var msTimer: MSTimer = MSTimer()
    private val speedValue = BoolValue("Speed", true)
    private val fallValue = BoolValue("Fall", true)
    private val fpsValue = BoolValue("Fps", true)
    private val backValue = BoolValue("BackGround", true)
    override fun drawElement(): Border? {
        var speed = MovementUtils.getSpeed()*150
        var fall = mc.thePlayer.fallDistance*50
        var fps = Minecraft.getDebugFPS()
        if(speedValue.get()){
            if(speed>165){
                speed=165f
            }
            if(backValue.get()){
                RoundedUtils.drawGradientRoundLR(0F, -3F, 180f,27f,4f,Color(ClientColor.colorRedValue.get(),ClientColor.colorGreenValue.get(),ClientColor.colorBlueValue.get(),100),Color(ClientColor.colorRedValue2.get(),ClientColor.colorGreenValue2.get(),ClientColor.colorBlueValue2.get(),100))
                RoundedUtils.drawGradientRoundLR(5F, 15F, 170f,5f,1f,Color(0,0,0,50),Color(0,0,0,50))
            }
            Fonts.roboto35.drawString("Speed:"+speed.toInt(), 10, 0,Color(255,255,255,255).rgb)
            RoundedUtils.drawGradientRoundLR(5F, 15F, 5+speed,5f,1f,Color(ClientColor.colorRedValue.get(),ClientColor.colorGreenValue.get(),ClientColor.colorBlueValue.get()),Color(ClientColor.colorRedValue2.get(),ClientColor.colorGreenValue2.get(),ClientColor.colorBlueValue2.get()))

        }
        if(fallValue.get()){
            if(fall>165){
                fall=165f
            }
            if(backValue.get()){
                RoundedUtils.drawGradientRoundLR(0F, 27F, 180f,27f,4f,Color(ClientColor.colorRedValue.get(),ClientColor.colorGreenValue.get(),ClientColor.colorBlueValue.get(),100),Color(ClientColor.colorRedValue2.get(),ClientColor.colorGreenValue2.get(),ClientColor.colorBlueValue2.get(),100))
                RoundedUtils.drawGradientRoundLR(5F, 45F, 170f,5f,1f,Color(0,0,0,50),Color(0,0,0,50))
            }
            Fonts.roboto35.drawString("Fall:"+mc.thePlayer.fallDistance, 10, 30,Color(255,255,255,255).rgb)
            RoundedUtils.drawGradientRoundLR(5F, 45F, 5+fall,5f,1f,Color(ClientColor.colorRedValue.get(),ClientColor.colorGreenValue.get(),ClientColor.colorBlueValue.get()),Color(ClientColor.colorRedValue2.get(),ClientColor.colorGreenValue2.get(),ClientColor.colorBlueValue2.get()))

        }
        if(fpsValue.get()){
            if(fps>165){
                fps=165
            }
            if(backValue.get()){
                RoundedUtils.drawGradientRoundLR(0F, 57F, 180f,27f,4f,Color(ClientColor.colorRedValue.get(),ClientColor.colorGreenValue.get(),ClientColor.colorBlueValue.get(),100),Color(ClientColor.colorRedValue2.get(),ClientColor.colorGreenValue2.get(),ClientColor.colorBlueValue2.get(),100))
                RoundedUtils.drawGradientRoundLR(5F, 75F, 170f,5f,1f,Color(0,0,0,50),Color(0,0,0,50))
            }
            Fonts.roboto35.drawString("Fps:"+Minecraft.getDebugFPS(), 10, 60,Color(255,255,255,255).rgb)
            RoundedUtils.drawGradientRoundLR(5F, 75F, (5+fps).toFloat(),5f,1f,Color(ClientColor.colorRedValue.get(),ClientColor.colorGreenValue.get(),ClientColor.colorBlueValue.get()),Color(ClientColor.colorRedValue2.get(),ClientColor.colorGreenValue2.get(),ClientColor.colorBlueValue2.get()))

        }
        return Border(0F, -3F,180f,24f)
    }
}