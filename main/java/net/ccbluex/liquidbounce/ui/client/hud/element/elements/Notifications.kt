/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", single = true)
class Notifications(x: Double = 3.0, y: Double = 11.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = Notification("Notification", "This is an example notification.", NotifyType.INFO)
    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        // bypass java.util.ConcurrentModificationException
        LiquidBounce.hud.notifications.map { it }.forEachIndexed { index, notify ->
            GL11.glPushMatrix()

            if(notify.drawNotification(index)){
                LiquidBounce.hud.notifications.remove(notify)
            }

            GL11.glPopMatrix()
        }

        if (mc.currentScreen is GuiHudDesigner) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification))
                LiquidBounce.hud.addNotification(exampleNotification)

            exampleNotification.fadeState = FadeState.STAY
            exampleNotification.displayTime = System.currentTimeMillis()
//            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-exampleNotification.width.toFloat(), -exampleNotification.height.toFloat(),0F,0F)
        }
        return null
    }

}

class Notification(val title: String, val content: String, val type: NotifyType, val time: Int=1500, val animeTime: Int=500) {
    val width=100.coerceAtLeast(Fonts.font40.getStringWidth(this.title)
                    .coerceAtLeast(Fonts.font40.getStringWidth(this.content)) + 10)
    val height=30

    var fadeState = FadeState.IN
    var x =0f
    var y =0f
    var nowY=-height
    var displayTime=System.currentTimeMillis()
    var animeXTime=System.currentTimeMillis()
    var animeYTime=System.currentTimeMillis()

    /**
     * Draw notification
     */
    fun drawNotification(index: Int):Boolean {
        val realY=-(index+1)*height
        val nowTime=System.currentTimeMillis()

        //Y-Axis Animation
        if(nowY!=realY){
            var pct=(nowTime-animeYTime)/animeTime.toDouble()
            if(pct>1){
                nowY=realY
                pct=1.0
            }else{
                pct=EaseUtils.easeOutExpo(pct)
            }
            GL11.glTranslated(0.0,(realY-nowY)*pct,0.0)
        }else{
            animeYTime=nowTime
        }
        GL11.glTranslated(0.0,nowY.toDouble(),0.0)

        //X-Axis Animation
        var pct=(nowTime-animeXTime)/animeTime.toDouble()
        when(fadeState){
            FadeState.IN -> {
                if(pct>1){
                    fadeState=FadeState.STAY
                    animeXTime=nowTime
                    pct=1.0
                }
                pct=EaseUtils.easeOutExpo(pct)
            }

            FadeState.STAY -> {
                pct=1.0
                if((nowTime-animeXTime)>time){
                    fadeState=FadeState.OUT
                    animeXTime=nowTime
                }
            }

            FadeState.OUT -> {
                if(pct>1){
                    fadeState=FadeState.END
                    animeXTime=nowTime
                    pct=1.0
                }
                pct=1-EaseUtils.easeInExpo(pct)
            }

            FadeState.END -> {
                return true
            }
        }
        GL11.glTranslated(width-(width*pct),0.0,0.0)
        GL11.glTranslatef(-width.toFloat(),0F,0F)

        //draw notify
//        GL11.glPushMatrix()
//        GL11.glEnable(GL11.GL_SCISSOR_TEST)
//        GL11.glScissor(width-(width*pct).toFloat(),0F, width.toFloat(),height.toFloat())
        RenderUtils.drawRoundRect2(-25F,0F,width.toFloat(),height.toFloat(),2.0f,Color(0, 0, 0,ClientColor.air1).rgb)
        RenderUtils.drawGradientSideways((-25F).toDouble(), (height-2F).toDouble(), max(width-width*((nowTime-displayTime)/(animeTime*2F+time)),0F).toDouble(), height.toFloat().toDouble(),Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(),200).getRGB(),Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),255).rgb)
        Fonts.roboto40.drawString(title,4F,6F,Color(255,255,255).getRGB())
        Fonts.roboto35.drawString(content,4F,17F,Color(255,255,255).getRGB())
        RenderUtils.drawImage(ResourceLocation("liquidbounce/noti/"+type.name+".png"),-x.toInt() - 21,-y.toInt() - 28 + 32,20,20)

        return false
    }
}

enum class NotifyType(var renderColor: Color) {
    WARNING(Color(0xF5FD00)),
    BOT(Color(0xF5FD00)),
    SINKAAA(Color(0xF5FD00)),
    ALL(Color(255,255,0)),
    KILL(Color(255,255,0)),
    LOST(Color(0,200,255)),
    PLAY(Color(255,160,200)),
    HACK(Color(255,0,0)),
    SUCCESS(Color(0x60E092)),
    ERROR(Color(0xFF2F2F)),
    DROP(Color(0xFF2F2F)),
    INFO(Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get()));
}


enum class FadeState { IN, STAY, OUT, END }

