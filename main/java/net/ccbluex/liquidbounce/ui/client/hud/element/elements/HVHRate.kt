package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.util.ResourceLocation
import java.awt.Color

@ElementInfo(name = "HVHRate")
class HVHRate(x: Double = 0.0, y: Double = 120.0, scale: Float = 1F) : Element(x, y, scale) {
    var yHP = 20
    var mHP = 20
    var yH = 0
    var yN = ""
    var win = "ing.."
    var msTimer = MSTimer()
    private var msTimer1 = MSTimer()

    private var msTimer2 = MSTimer()
    var x1 =3
    var y1 = 15
    var size = 15
    override fun drawElement(): Border? {
       if((LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!=null){
           //Math
           msTimer.reset()
           msTimer1.reset()
           if((LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!.hurtTime==10){
               yH=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!.hurtTime
               yN=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!.name
           }
           if(msTimer.hasTimePassed(1500)){
               var me = mc.thePlayer.health
               var you = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!

               if(me>=you.health){
                   if(mc.thePlayer.hurtTime>=you.hurtTime){
                       win="Win"
                   }else if(me-1>=you.health){
                       win="Win"
                   }else{
                       win = "Lost"
                   }
               }else {
                   win = "Lost"
               }
           }
       }
        if(!msTimer.hasTimePassed(1000)){
            RoundedUtils.drawGradientRoundLR(0f, 0f, 68f, 34f, 2f, Color(ClientColor.red, ClientColor.green, ClientColor.blue, ClientColor.air1), Color(ClientColor.red, ClientColor.green, ClientColor.blue, ClientColor.air1))
            Fonts.roboto35.drawString("The result: "+win,5f,5f,Color(250,250,250).rgb,false)
            RenderUtils.drawGradientSideways(6.0, 13.0, 62.0, 14.0,Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 255).rgb, Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 255).rgb)

            RenderUtils.drawGradientSideways(6.0, 30.0,8.0+(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!.hurtTime,31.0, Color(255, 0, 0, 244).rgb, Color(255, 0, 0, 244).rgb)
            RenderUtils.drawImage(ResourceLocation("liquidbounce/notifications/HP.png"), x1, y1, size, size)
            Fonts.roboto25.drawString("HurtTime:"+yH,26f,17f,Color(250,250,250).rgb,false)
            Fonts.roboto25.drawString("Name:"+yN,26f,24f,Color(250,250,250).rgb,false)
        }
        if((LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target==null){
            win = "ing.."
        }
        //Anim
        if((LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target!!.hurtTime in 8..10){
            x1=5
            y1=17
            size=11
        }
        if(x1>3&&y1>15&&size<15&&msTimer2.hasTimePassed(150)){
            x1--
            y1--
            size+=2
            msTimer2.reset()
        }
        //DrawUI

        return Border(0f, 0f, 68f, 34f)
    }
}