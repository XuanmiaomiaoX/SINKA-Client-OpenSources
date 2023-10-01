package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.extensions.*
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.FontValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.roundToInt

@ElementInfo(name = "Targets")
class Target : Element(-46.0,-40.0,1F,Side(Side.Horizontal.MIDDLE,Side.Vertical.MIDDLE)) {
    private val modeValue = ListValue("Mode", arrayOf("Liquid"), "Liquid")
    private val switchModeValue = ListValue("SwitchMode", arrayOf("Slide","Zoom","None"), "Zoom")
    private val animSpeedValue = IntegerValue("AnimSpeed",5,5,20)
    private val switchAnimSpeedValue = IntegerValue("SwitchAnimSpeed",5,5,40)
    private val fontValue = FontValue("Font", Fonts.font40)
    val fontRenderer = fontValue.get()

    val counter1 = intArrayOf(50)
    val counter2 = intArrayOf(100)
    private var prevTarget:EntityLivingBase?=null
    private var lastHealth=20F
    private var lastChangeHealth=20F
    private var changeTime=System.currentTimeMillis()
    private var displayPercent=0f
    private var lastUpdate = System.currentTimeMillis()
    private val decimalFormat = DecimalFormat("0.0")
    var msTimer = MSTimer()

    private fun getHealth(entity: EntityLivingBase?):Float{
        return if(entity==null || entity.isDead){ 0f }else{ entity.health }
    }

    override fun drawElement(): Border? {
        var target=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target
        var on=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).state
        var killaura=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura)
        val time=System.currentTimeMillis()
        val pct = (time - lastUpdate) / (switchAnimSpeedValue.get()*50f)
        lastUpdate=System.currentTimeMillis()

        if (mc.currentScreen is GuiHudDesigner) {
            target=mc.thePlayer
        }
        if (target != null) {
            prevTarget = target
        }
        prevTarget ?: return getTBorder()
        if (target!=null) {
            msTimer.reset()
            while (displayPercent != 1f){
                if (displayPercent < 1) {
                    displayPercent += pct
                }
                if (displayPercent > 1) {
                    displayPercent = 1f
                }
            }
        } else {
            if (msTimer.hasTimePassed(300)){
                if (displayPercent > 0) {
                    displayPercent -= pct
                }
                if (displayPercent < 0) {
                    displayPercent = 0f
                    prevTarget=null
                    return getTBorder()
                }
            }
        }

        if(getHealth(prevTarget)!=lastHealth){
            lastChangeHealth=lastHealth
            lastHealth=getHealth(prevTarget)
            changeTime=time
        }
        val nowAnimHP=if((time-(animSpeedValue.get()*50))<changeTime){
            getHealth(prevTarget)+(lastChangeHealth-getHealth(prevTarget))*(1-((time-changeTime)/(animSpeedValue.get()*50F)))
        }else{
            getHealth(prevTarget)
        }

        when(switchModeValue.get().toLowerCase()){
            "zoom" -> {
                val border=getTBorder() ?: return null
                GL11.glScalef(displayPercent,displayPercent,displayPercent)
                GL11.glTranslatef(((border.x2 * 0.5f * (1-displayPercent))/displayPercent).toFloat(), ((border.y2 * 0.5f * (1-displayPercent))/displayPercent).toFloat(), 0f)
            }
            "slide" -> {
                val percent=EaseUtils.easeInQuint(1.0-displayPercent)
                val xAxis=ScaledResolution(mc).scaledWidth-renderX
                GL11.glTranslated(xAxis*percent,0.0,0.0)
            }
        }

        when(modeValue.get().toLowerCase()){
            "novoline" -> drawNovo(prevTarget!!,nowAnimHP)
            "astolfo" -> drawAstolfo(prevTarget!!,nowAnimHP)
            "liquid" -> drawLiquid(prevTarget!!,nowAnimHP)
            "flux" -> drawFlux(prevTarget!!,nowAnimHP)
            "rise" -> drawRise(prevTarget!!,nowAnimHP)
            "zamorozka" -> drawZamorozka(prevTarget!!,nowAnimHP)
        }

        return getTBorder()
    }

    private fun drawAstolfo(target: EntityLivingBase, nowAnimHP: Float){
        val font=fontValue.get()
        val color=ColorUtils.skyRainbow(1,1F,0.9F,5.0)
        val hpPct=nowAnimHP/target.maxHealth

        RenderUtils.drawRect(0F,0F, 140F, 60F, Color(0,0,0,110).rgb)

        // health rect
        RenderUtils.drawRect(3F, 55F, 137F, 58F,ColorUtils.reAlpha(color,100).rgb)
        RenderUtils.drawRect(3F,55F,3+(hpPct*134F),58F,color.rgb)
        GL11.glColor4f(1f,1f,1f,1f)
        RenderUtils.drawEntityOnScreen(18,46,20,target)

        font.drawStringWithShadow(target.name, 37F, 6F, -1)
        GL11.glPushMatrix()
        GL11.glScalef(2F,2F,2F)
        font.drawString("${getHealth(target).roundToInt()} ❤", 19,9, color.rgb)
        GL11.glPopMatrix()
    }

    private fun drawNovo(target: EntityLivingBase, nowAnimHP: Float){
        val font=fontValue.get()
        val color=ColorUtils.healthColor(getHealth(target),target.maxHealth)
        val darkColor=ColorUtils.darker(color,0.6F)
        val hpPos=33F+((getHealth(target) / target.maxHealth * 10000).roundToInt() / 100)

        RenderUtils.drawRect(0F,0F, 140F, 40F, Color(40,40,40).rgb)
        font.drawString(target.name, 33, 5, Color.WHITE.rgb)
        RenderUtils.drawEntityOnScreen(20, 35, 15, target)
        RenderUtils.drawRect(hpPos, 18F, 33F + ((nowAnimHP / target.maxHealth * 10000).roundToInt() / 100), 25F, darkColor)
        RenderUtils.drawRect(33F, 18F, hpPos, 25F, color)
        font.drawString("❤", 33, 30, Color.RED.rgb)
        font.drawString(decimalFormat.format(getHealth(target)), 43, 30, Color.WHITE.rgb)
    }

    private fun drawLiquid(target: EntityLivingBase, easingHealth: Float){
        val width = (36).coerceAtLeast(65).toFloat()
        val font = fontValue.get()
        val hp = decimalFormat.format(easingHealth)
        val additionalWidth = font.getStringWidth("${target.name}  ${hp} hp").coerceAtLeast(75)

        // Draw rect box
        RenderUtils.drawRoundRect(-2f,-2f,width+45,36.0f, 5.0F,Color(ClientColor.red, ClientColor.green, ClientColor.blue,ClientColor.air1).getRGB());
        RoundedUtils.drawGradientRoundLR(38.0F, 25.0F,width, 4.0F,1.0F,Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(),35),Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),35))
        val customColor = Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 255)
        val customColor1 = Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 255)
        val customColor2 = Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 255)
        Fonts.roboto20.drawString("Hurttime:"+target.hurtTime, 39, 16, Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1,200).rgb)
        if (easingHealth > target.health)
        counter1[0] += 1
        counter2[0] += 1
        counter1[0] = counter1[0].coerceIn(0, 50)
        counter2[0] = counter2[0].coerceIn(0, 100)
        val stopPos = (1 + (65 * (easingHealth / target.maxHealth))).toInt()
        for (i in 5..stopPos step 5) {
            val x1 = (i + 5).coerceAtMost(stopPos).toDouble()
            RoundedUtils.drawGradientRoundLR(38.0F, 25.0F, x1.toFloat(), 4.0F,0.2F,Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(),210),Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),230))
            Fonts.roboto20.drawString(BigDecimal((target.health / target.maxHealth * 100).toDouble()).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "", ((target.health / target.maxHealth) * width)+30F, 32F, Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1,200).rgb)
        }
        val hurtPercent=target.hurtPercent
        val scale=if(hurtPercent==0f){ 1f }
        else if(hurtPercent<0.5f){
            1-(0.2f*hurtPercent*2)
        }else{
            0.8f+(0.2f*(hurtPercent-0.5f)*2)
        }
        val size=30
        GL11.glPushMatrix()
        GL11.glTranslatef(5f, 5f, 0f)
        // 受伤的缩放效果
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslatef(((size * 0.5f * (1-scale))/scale), ((size * 0.5f * (1-scale))/scale), 0f)
        // 受伤的红色效果
        GL11.glColor4f(1f, 1-hurtPercent, 1-hurtPercent, 1f)
        // 绘制头部图片
        RenderUtils.quickDrawHead(target.skin, -3, -3, size, size)
        RenderUtils.drawShadow(-3, -3, size, size)
        GL11.glPopMatrix()

        // Draw info
        val playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
        if (playerInfo != null) {
            Fonts.roboto35.drawString(target.name, 38, 4, Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1,200).rgb)
        }else{
            Fonts.roboto35.drawString("Mob", 38, 4, Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1,200).rgb)
        }
    }

    private fun drawZamorozka(target: EntityLivingBase, easingHealth: Float){
        val font=fontValue.get()

        // Frame
        RenderUtils.drawCircleRect(0f,0f,150f,55f,5f,Color(0,0,0,70).rgb)
        RenderUtils.drawRect(7f,7f,35f,40f,Color(0,0,0,70).rgb)
        GL11.glColor4f(1f,1f,1f,1f)
        RenderUtils.drawEntityOnScreen(21, 38, 15, target)

        // Healthbar
        val barLength=143-7f
        RenderUtils.drawCircleRect(7f,45f,143f,50f,2.5f,Color(0,0,0,70).rgb)
        RenderUtils.drawCircleRect(7f,45f,7+((easingHealth/target.maxHealth)*barLength).coerceAtLeast(5f),50f,2.5f,ColorUtils.rainbowWithAlpha(90).rgb)
        RenderUtils.drawCircleRect(7f,45f,7+((target.health/target.maxHealth)*barLength).coerceAtLeast(5f),50f,2.5f,ColorUtils.rainbow().rgb)

        // Info
        RenderUtils.drawCircleRect(43f,15f-font.FONT_HEIGHT,143f,17f,(font.FONT_HEIGHT+1)*0.45f,Color(0,0,0,70).rgb)
        font.drawCenteredString("${target.name} ${if(target.ping!=-1) { "§f${target.ping}ms" } else { "" }}", 93f, 16f-font.FONT_HEIGHT, ColorUtils.rainbow().rgb, false)
        font.drawString("Health: ${decimalFormat.format(easingHealth)} §7/ ${decimalFormat.format(target.maxHealth)}", 43, 11+font.FONT_HEIGHT,Color.WHITE.rgb)
        font.drawString("Distance: ${decimalFormat.format(mc.thePlayer.getDistanceToEntityBox(target))}", 43, 11+font.FONT_HEIGHT*2,Color.WHITE.rgb)
    }

    private fun drawMoon(target: EntityLivingBase, easingHealth: Float){
        val font = fontValue.get()
        val hp = decimalFormat.format(easingHealth)
        val additionalWidth = font.getStringWidth("${target.name}  ${hp} hp").coerceAtLeast(75)
        //RenderUtils.drawBorderCircle(0F,0F,45F+additionalWidth,35F,4f,3f,Color(205,70,205,255).rgb)


        // info text
        GL11.glEnable(3042)
        GL11.glDisable(3553)
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(2848)
        GL11.glShadeModel(7425)
        val yPos = 5 + font.FONT_HEIGHT + 3f

        val stopPos =
            (5 + ((135 - font.getStringWidth(decimalFormat.format(target.maxHealth))) * (easingHealth / target.maxHealth))).toInt()
        for (i in 5..stopPos step 5) {
            val x1 = (i + 5).coerceAtMost(stopPos).toDouble()
            RenderUtils.quickDrawGradientSideways(i.toDouble()-5.0, 0.0-1/3, 45.0 + additionalWidth-1, 1.0,
                ColorUtils.hslRainbow(i, indexOffset = 10).rgb, ColorUtils.hslRainbow(x1.toInt(), indexOffset = 0).rgb)
        }


        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glDisable(2848)
        GL11.glShadeModel(7424)
        GL11.glColor4f(1f, 1f, 1f, 1f)




        // hp bar
        RenderUtils.drawRect(37f, yPos+5, 37f + additionalWidth, yPos + 13,Color(0, 0, 0,100).rgb)
        if (target.health<=target.maxHealth){
            RenderUtils.drawCircleRect(37f, yPos+5, 37f + ((easingHealth / target.maxHealth * 8100).roundToInt() / 100), yPos + 13,3f,Color(0, 255, 0).rgb)
        }
        if (target.health<target.maxHealth/2){
            RenderUtils.drawCircleRect(37f, yPos+5, 37f + ((easingHealth / target.maxHealth * 8100).roundToInt() / 100), yPos + 13,3f,Color(255, 255, 0).rgb)
        }
        if (target.health<target.maxHealth/4){
            RenderUtils.drawCircleRect(37f, yPos+5, 37f + ((easingHealth / target.maxHealth * 8100).roundToInt() / 100), yPos + 13,3f,Color(255, 0, 0).rgb)
        }


        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glDisable(2848)
        GL11.glShadeModel(7424)
        GL11.glColor4f(1f, 1f, 1f, 1f)

        font.drawString(target.name, 37, 5, Color.WHITE.rgb)
        RenderUtils.drawHead(target.skin, 2, 2, 32, 32)
        GL11.glScaled(0.7, 0.7, 0.7)
        "$hp hp".also {
            font.drawString(it, 53 , 23, Color.LIGHT_GRAY.rgb)
        }
    }


    private fun drawRise(target: EntityLivingBase, easingHealth: Float){
        val font=fontValue.get()

        RenderUtils.drawCircleRect(0f,0f,150f,50f,5f,Color(0,0,0,130).rgb)

        val hurtPercent=target.hurtPercent
        val scale=if(hurtPercent==0f){ 1f }
        else if(hurtPercent<0.5f){
            1-(0.2f*hurtPercent*2)
        }else{
            0.8f+(0.2f*(hurtPercent-0.5f)*2)
        }
        val size=30

        GL11.glPushMatrix()
        GL11.glTranslatef(5f, 5f, 0f)
        // 受伤的缩放效果
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslatef(((size * 0.5f * (1-scale))/scale), ((size * 0.5f * (1-scale))/scale), 0f)
        // 受伤的红色效果
        GL11.glColor4f(1f, 1-hurtPercent, 1-hurtPercent, 1f)
        // 绘制头部图片
        RenderUtils.quickDrawHead(target.skin, 0, 0, size, size)
        GL11.glPopMatrix()

        font.drawString("Name ${target.name}", 40, 11,Color.WHITE.rgb)
        font.drawString("Distance ${decimalFormat.format(mc.thePlayer.getDistanceToEntityBox(target))} Hurt ${target.hurtTime}", 40, 11+font.FONT_HEIGHT,Color.WHITE.rgb)

        // 渐变血量条
        GL11.glEnable(3042)
        GL11.glDisable(3553)
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(2848)
        GL11.glShadeModel(7425)
        fun renderSideway(x: Int,x1: Int){
            RenderUtils.quickDrawGradientSideways(x.toDouble(),39.0, x1.toDouble(),45.0,ColorUtils.hslRainbow(x,indexOffset = 10).rgb,ColorUtils.hslRainbow(x1,indexOffset = 10).rgb)
        }
        val stopPos=(5+((135-font.getStringWidth(decimalFormat.format(target.maxHealth)))*(easingHealth/target.maxHealth))).toInt()
        for(i in 5..stopPos step 5){
            renderSideway(i, (i + 5).coerceAtMost(stopPos))
        }
        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glDisable(2848)
        GL11.glShadeModel(7424)
        GL11.glColor4f(1f, 1f, 1f, 1f)

        font.drawString(decimalFormat.format(easingHealth),stopPos+5,43-font.FONT_HEIGHT/2,Color.WHITE.rgb)
    }
    
    private fun drawFlux(target: EntityLivingBase, nowAnimHP: Float){
        val width = (38 + target.name.let(Fonts.font40::getStringWidth))
            .coerceAtLeast(70)
            .toFloat()

        // draw background
        RenderUtils.drawRect(0F, 0F, width,34F,Color(40,40,40).rgb)
        RenderUtils.drawRect(2F, 22F, width-2F, 24F, Color.BLACK.rgb)
        RenderUtils.drawRect(2F, 28F, width-2F, 30F, Color.BLACK.rgb)

        // draw bars
        RenderUtils.drawRect(2F, 22F, 2+(nowAnimHP / target.maxHealth) * (width-4), 24F, Color(231,182,0).rgb)
        RenderUtils.drawRect(2F, 22F, 2+(getHealth(target) / target.maxHealth) * (width-4), 24F, Color(0, 224, 84).rgb)
        RenderUtils.drawRect(2F, 28F, 2+(target.totalArmorValue / 20F) * (width-4), 30F, Color(77, 128, 255).rgb)

        // draw text
        Fonts.font40.drawString(target.name,22,3,Color.WHITE.rgb)
        GL11.glPushMatrix()
        GL11.glScaled(0.7,0.7,0.7)
        Fonts.font35.drawString("Health: ${decimalFormat.format(getHealth(target))}",22/0.7F,(4+Fonts.font40.height)/0.7F,Color.WHITE.rgb)
        GL11.glPopMatrix()

        // Draw head
        RenderUtils.drawHead(target.skin, 2,2,16,16)
    }

    private fun getTBorder():Border?{
        return when(modeValue.get().toLowerCase()){
            "novoline" -> Border(0F,0F,140F,40F)
            "astolfo" -> Border(0F,0F,140F,60F)
            "liquid" -> Border(0F,0F,100F,40F)
            "flux" -> Border(0F,0F,(38 + mc.thePlayer.name.let(Fonts.font40::getStringWidth))
                .coerceAtLeast(70)
                .toFloat(),34F)
            "rise" -> Border(0F,0F,150F,55F)
            "zamorozka" -> Border(0F,0F,150F,55F)
            "exhibition" -> Border(0F, 0F, 140F, 45F)
            else -> null
        }
    }
}
