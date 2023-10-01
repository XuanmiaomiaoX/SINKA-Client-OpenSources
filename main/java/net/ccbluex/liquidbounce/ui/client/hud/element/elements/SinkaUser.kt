package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.QQLogoHelper
import net.ccbluex.liquidbounce.utils.extensions.skin
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.util.ResourceLocation
import java.awt.Color

/**
 * SinkaUser By Mimosa
 */

@ElementInfo(name = "SinkaUser")
class SinkaUser(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    val head = IntegerValue("Head", 1, 1, 7)
    val qqchat = BoolValue("QQChat",true)
    override fun drawElement(): Border? {
        val width1 = if(Fonts.roboto40.getStringWidth(mc.getSession().username)>if(qqchat.get())Fonts.roboto30.getStringWidth("QQ"+QQLogoHelper.QQNumber) else Fonts.roboto30.getStringWidth("Health:"+mc.thePlayer.health.toInt())){
            Fonts.roboto40.getStringWidth(mc.getSession().username)
        }else{
            if(qqchat.get()){
                Fonts.roboto30.getStringWidth("QQ"+QQLogoHelper.QQNumber)
            }else{
                Fonts.roboto30.getStringWidth("Health:"+mc.thePlayer.health.toInt())
            }
        }
        RoundedUtils.drawGradientRoundLR(-1f, 0f, 50f+width1.toFloat(), 34f, 2f, Color(ClientColor.red, ClientColor.green, ClientColor.blue,ClientColor.air1),Color(ClientColor.red, ClientColor.green, ClientColor.blue,ClientColor.air1))
        RoundedUtils.drawGradientRoundLR(42f+ width1, 2f, 4f, 4f, 2f, Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 255), Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 255))

        mc.textureManager.bindTexture(mc.thePlayer.skin)
        RenderUtils.drawScaledCustomSizeModalCircle(1, 2, 8f, 8f, 8, 8, 30,30, 64f, 64f)
        RenderUtils.drawScaledCustomSizeModalCircle(1, 2, 40f, 8f, 8, 8,30,30, 64f, 64f)

        Fonts.roboto40.drawString(mc.getSession().username, 35f, (0+6).toFloat(), Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1).getRGB(), false)
        if(qqchat.get()){
            Fonts.roboto30.drawString("QQ"+QQLogoHelper.QQNumber, 35f, (0 + 22).toFloat(), Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1).getRGB(), false)
        }else{
            Fonts.roboto30.drawString("Health:"+mc.thePlayer.health.toInt(), 35f, (0 + 22).toFloat(), Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1).getRGB(), false)
        }
        return Border(0f, 0f, 48f+ width1, 34f)
    }
}