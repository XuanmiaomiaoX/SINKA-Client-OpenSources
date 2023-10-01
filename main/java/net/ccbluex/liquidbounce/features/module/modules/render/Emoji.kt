package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

@ModuleInfo(
    name = "Emoji",
    description = ":)",
    category = ModuleCategory.RENDER
)
class Emoji : Module() {
    private val modeValue = ListValue("Mode", arrayOf("JiaRan", "USA"), "USA")

    private val jiaran = ResourceLocation("liquidbounce/cool/emoji/jiaran.png")
    private val usa = ResourceLocation("liquidbounce/cool/emoji/USA.png")

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        for (p in mc.theWorld.playerEntities) {
            if (p != mc.thePlayer && p.canEntityBeSeen(mc.thePlayer) && EntityUtils.isSelected(p, true) && !p.isInvisible) {
                val pX = (p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks
                        - mc.renderManager.renderPosX)
                val pY = (p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks
                        - mc.renderManager.renderPosY)
                val pZ = (p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks
                        - mc.renderManager.renderPosZ)
                GL11.glPushMatrix()
                GL11.glTranslatef(pX.toFloat(), pY.toFloat() + if (p.isSneaking) 0.8f else 1.3f, pZ.toFloat())
                GL11.glNormal3f(1.0f, 1.0f, 1.0f)
                GL11.glRotatef(-mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
                GL11.glRotatef(mc.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
                val scale = 0.06f
                GL11.glScalef(-scale, -scale, scale)
                GL11.glDisable(GL11.GL_LIGHTING)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                GL11.glPushMatrix()
                GL11.glColor4f(1f, 1f, 1f, 1f)
                when (modeValue.get()) {
                    "jiaran" -> drawHead(jiaran)
                    "USA" -> drawHead(usa)
                }
                GL11.glPopMatrix()
                GL11.glPopMatrix()
            }
        }
    }
    private fun drawHead(resource: ResourceLocation) = RenderUtils.drawImage(resource,-8,-14,16,16)
}