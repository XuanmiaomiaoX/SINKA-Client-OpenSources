package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import java.awt.Color

/**
 * by mimosa
 *
 * ClientImage
 */
@ElementInfo(name = "ClientLogo")
class ClientLogo(x: Double = 2.00, y: Double = 2.00, scale: Float = 1.0F, side: Side = Side.default()) : Element(x, y, scale, side) {


    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        RenderUtils.drawImage(ResourceLocation("liquidbounce/Sinkatext.png"), -4, -12, 80, 40)
        Fonts.roboto30.drawString("Time:"+ Text.HOUR_FORMAT.format(System.currentTimeMillis())+" | FPS:"+ Minecraft.getDebugFPS().toString(), 1, 24, Color(255,255,255,170).rgb)
        Fonts.roboto30.drawString("Version:R"+LiquidBounce.SINKA_VERSION, 1, 34, Color(255,255,255,170).rgb)

        return Border(
            -2F,
            -2F,
            80F,
            30F
        )
    }
}