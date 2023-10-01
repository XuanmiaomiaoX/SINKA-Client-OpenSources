
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.GLUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Inventory")
class Inventory(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    private val mode1Value= ListValue("Mode", arrayOf("Sinka", "Shadow"), "Sinka")
    /**
     * Draw element
     */

    override fun drawElement(): Border? {
        when(mode1Value.get()) {
            "Sinka" -> {
                RenderUtils.drawRoundRect(8F, 25F, (8.0 + 163.0).toFloat(), ((30.0 + 65.0).toFloat()), 5.0F, Color(ClientColor.red, ClientColor.green, ClientColor.blue,ClientColor.air1).rgb)
                RenderUtils.drawGradientSideways(11.0, 38.0, 11.0 + 157.0, 38.0+1.0,Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 255).rgb, Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 255).rgb)
                Fonts.roboto35.drawString("Inventory", 72, 29,Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1).getRGB())
            }
            "Shadow" -> {
                RenderUtils.drawShadow(8, 25, (8.0 + 157.0).toInt(), (30.0 + 40.0).toInt())
                RenderUtils.drawGradientSideways(8.0, 25.0, 8.0 + 165.0, 27.0,Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(),210).getRGB(),Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),230).getRGB())
            }
        }
        var itemX: Int = 10
        var itemY: Int = 40
        var airs = 0
        for (i in mc.thePlayer.inventory.mainInventory.indices) {
            if (i < 9) continue
            val stack = mc.thePlayer.inventory.mainInventory[i]
            if (stack == null) {
                airs++
            }
            val res = ScaledResolution(mc)
            GL11.glPushMatrix()
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            if (mc.theWorld != null) GLUtils.enableGUIStandardItemLighting()
            GlStateManager.pushMatrix()
            GlStateManager.disableAlpha()
            GlStateManager.clear(256)
            mc.renderItem.zLevel = -150.0f
            mc.renderItem.renderItemAndEffectIntoGUI(stack, itemX, itemY)
            mc.renderItem.renderItemOverlays(mc.fontRendererObj, stack, itemX, itemY)
            mc.renderItem.zLevel = 0.0f
            GlStateManager.disableBlend()
            GlStateManager.scale(0.5, 0.5, 0.5)
            GlStateManager.disableDepth()
            GlStateManager.disableLighting()
            GlStateManager.enableDepth()
            GlStateManager.scale(2.0f, 2.0f, 2.0f)
            GlStateManager.enableAlpha()
            GlStateManager.popMatrix()
            GL11.glPopMatrix()
            if (itemX < 152) {
                itemX += 18
            } else {
                itemX = 10
                itemY += 18
            }
        }

        if (airs == 27) {
            Fonts.wqy40_reg.drawString("Your inventory is empty...", 28, 56, Color(ClientColor.red1, ClientColor.green1, ClientColor.blue1).getRGB())
        }
        return Border(8f, 30f + 10f, 8f + 163f, 30f + 65f)
    }
}