package net.ccbluex.liquidbounce.features.module.modules.render

import me.HXC.Modules.render.Recorder
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.handshake.client.C00Handshake
import net.minecraft.network.play.server.S45PacketTitle


import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

@ModuleInfo(name = "SessionInfo", category = ModuleCategory.RENDER, description="SessionInfo")
class SessionInfo : Module() {
    private val xValue = FloatValue("xValue", 10f, 0f, 500f)
    private val yValue = FloatValue("yValue", 30f, 0f, 500f)
    private val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
    var syncEntity: EntityLivingBase? = null
    var killCounts = 0
    var totalPlayed = 0
    var win = 0
    var startTime = System.currentTimeMillis()
    @EventTarget
    private fun OnRender2D(event: Render2DEvent){
        pushMatrix()
        RenderUtils.drawCircleRect( xValue.get(), yValue.get()+10F, xValue.get()+120F, yValue.get()+75F, 7f,Color(0, 0, 0, 50).rgb)
        Fonts.icon40.drawString("F", xValue.get()+5F, yValue.get()+16F,Color(255,255,255).rgb)
        Fonts.font35.drawString("Play Time: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}", xValue.get()+20F, yValue.get()+15F,-1)
        Fonts.icon40.drawString("G", xValue.get()+5F, yValue.get()+31F, Color(255,255,255).rgb)
        Fonts.font35.drawString("Kills: $killCounts", xValue.get()+20F, yValue.get()+30F,-1)
        Fonts.icon40.drawString("H", xValue.get()+5F, yValue.get()+46F, Color(255,255,255).rgb)
        Fonts.font35.drawString("Win / Total: $win / $totalPlayed", xValue.get()+20F, yValue.get()+45F,-1)
        Fonts.icon40.drawString("I", xValue.get()+5F, yValue.get()+61F, Color(255,255,255).rgb)
        Fonts.font35.drawString("Speed:"+MovementUtils.getSpeed().toString().substring(3)+"/bps", xValue.get()+20F, yValue.get()+60F,-1)
        popMatrix()
    }

    @EventTarget
    private fun onAttack(event: AttackEvent) { syncEntity = event.targetEntity as EntityLivingBase?
    }
    @EventTarget
    private fun onUpdate(event: UpdateEvent) {
        if(syncEntity != null && syncEntity!!.isDead) {
            ++killCounts
            syncEntity = null
        }
    }
    @EventTarget(ignoreCondition = true)
    private fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (event.packet is C00Handshake) startTime = System.currentTimeMillis()

        if (packet is S45PacketTitle) {
            val title = packet.message.formattedText
            if(title.contains("CHAMPION")){
                win++
            }
            if(title.contains("BedWar")){
                totalPlayed++
            }
            if(title.contains("SkyWar")){
                totalPlayed++
            }
        }
    }
}
