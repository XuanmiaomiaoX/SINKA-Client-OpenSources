//Mimosa
package net.ccbluex.liquidbounce.features.module.modules.client

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import org.lwjgl.opengl.GL11
import kotlin.math.cos
import kotlin.math.sin

@ModuleInfo(name = "HatAura", description = "Blue Archive", category = ModuleCategory.CLIENT)
class HatAura : Module() {
    private val modeValue= ListValue("Aura", arrayOf("Circle", "Butterfly","SINKA"), "Circle")
    private val circleAlphaValue = IntegerValue("Alpha", 180, 0, 255)
    val rainbow = BoolValue("SINKARainbow",false)
    init {
        state = true
        array = false
    }

    private val msTimer = MSTimer()
    var ii = 0
    var isY = true

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (msTimer.hasTimePassed(5)) {
            if (isY && ii < 255) {
                ii++
                if (ii == 255) {
                    isY = false
                }
            }
            if (!isY && ii > 2) {
                ii--
                if (ii == 2) {
                    isY = true
                }
            }
            msTimer.reset()
        }
        when(modeValue.get()) {
            "Circle" -> {
//2
                GL11.glPushMatrix()
                GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY + 2.1F,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(4.0F)
                GL11.glColor4f(ClientColor.colorRedValue.get().toFloat() / 255.0F, ClientColor.colorGreenValue.get().toFloat() / 255.0F, ClientColor.colorBlueValue.get().toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 5) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * 0.35F, (sin(i * Math.PI / 180.0).toFloat() * 0.35F))
                }

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()
                //高光
                GL11.glPushMatrix()
                GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY + 2.1F,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(2.0F)
                if(rainbow.get()){
                    GL11.glColor4f(ii.toFloat() / 255.0F, 114.toFloat() / 255.0F, 175.toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)

                }else{
                    GL11.glColor4f(255.toFloat() / 255.0F, 255.toFloat() / 255.0F, 255.toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)

                }
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 5) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * 0.35F, (sin(i * Math.PI / 180.0).toFloat() * 0.35F))
                }

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()
            }
            "Butterfly" -> {
//3
                GL11.glPushMatrix()
                GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY + 2.1F,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(4.0F)
                GL11.glColor4f(ClientColor.colorRedValue.get().toFloat() / 255.0F, ClientColor.colorGreenValue.get().toFloat() / 255.0F, ClientColor.colorBlueValue.get().toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 5) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * 0.5F, (sin(i * Math.PI / 180.0).toFloat() * 0.2F))
                }

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()

                //3
                GL11.glPushMatrix()
                GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY + 2.1F,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(4.0F)
                GL11.glColor4f(ClientColor.colorRedValue2.get().toFloat() / 255.0F, ClientColor.colorGreenValue2.get().toFloat() / 255.0F, ClientColor.colorBlueValue2.get().toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 5) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * 0.2F, (sin(i * Math.PI / 180.0).toFloat() * 0.5F))
                }

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()
                //gaoguang
                GL11.glPushMatrix()
                GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY + 2.1F,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(2.0F)
                GL11.glColor4f(255 / 255.0F, 255.toFloat() / 255.0F, 255.toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 5) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * 0.5F, (sin(i * Math.PI / 180.0).toFloat() * 0.2F))
                }

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()

                //3
                GL11.glPushMatrix()
                GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY + 2.1F,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(2.0F)
                GL11.glColor4f(255 / 255.0F, 255.toFloat() / 255.0F, 255.toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 5) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * 0.2F, (sin(i * Math.PI / 180.0).toFloat() * 0.5F))
                }

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()
            }
            "SINKA" ->{
                GL11.glPushMatrix()
                GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY + 2.1F,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(4.0F)
                if(rainbow.get()){
                    GL11.glColor4f(ii.toFloat() / 255.0F, 114.toFloat() / 255.0F, 175.toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)
                }else{
                    GL11.glColor4f(ClientColor.colorRedValue.get().toFloat() / 255.0F, ClientColor.colorGreenValue.get().toFloat() / 255.0F, ClientColor.colorBlueValue.get().toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)

                }
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 61 - 1) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * 0.35F, (sin(i * Math.PI / 180.0).toFloat() * 0.35F))
                }

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()
                //高光
                GL11.glPushMatrix()
                GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY + 2.1F,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
                )
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                GL11.glLineWidth(2.0F)
                GL11.glColor4f(255.toFloat() / 255.0F, 255.toFloat() / 255.0F, 255.toFloat() / 255.0F, circleAlphaValue.get().toFloat() / 255.0F)
                GL11.glRotatef(90F, 1F, 0F, 0F)
                GL11.glBegin(GL11.GL_LINE_STRIP)

                for (i in 0..360 step 61 - 1) { // You can change circle accuracy  (60 - accuracy)
                    GL11.glVertex2f(cos(i * Math.PI / 180.0).toFloat() * 0.35F, (sin(i * Math.PI / 180.0).toFloat() * 0.35F))
                }

                GL11.glEnd()

                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)

                GL11.glPopMatrix()
            }
        }
    }
}