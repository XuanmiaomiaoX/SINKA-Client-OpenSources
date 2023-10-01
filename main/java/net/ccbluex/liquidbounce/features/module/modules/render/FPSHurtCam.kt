package net.ccbluex.liquidbounce.features.module.modules.render
import me.HXC.Utils.RenderUtils2
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.ScaledResolution
import java.awt.Color
@ModuleInfo(
    name = "FPSHurtCam",
    description = "Like fps games.",
    category = ModuleCategory.RENDER
)
class FPSHurtCam : Module() {
    var alpha1 = 0
    @EventTarget
    fun onRender2D(event : Render2DEvent) {
        val scaledResolution = ScaledResolution(mc)
        val width = scaledResolution.scaledWidth_double
        val height = scaledResolution.scaledHeight_double
        if (mc.thePlayer.hurtTime > 0) {
            if (alpha1 < 100) {
                alpha1 += 5
            }
        } else {
            if (alpha1 > 0) {
                alpha1 -= 5
            }
        }
        RenderUtils.drawGradientSidewaysV(0.0, 0.0, width, 25.0, Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 0).rgb,Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), alpha1).rgb)
        RenderUtils.drawGradientSidewaysV(0.0, height - 25, width, height,
            Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),alpha1).rgb, Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 0).rgb
        )
    }
}