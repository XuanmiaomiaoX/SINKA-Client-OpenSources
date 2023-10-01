
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer


@ModuleInfo(name = "AutoRegen", description = "AutoRegen", category = ModuleCategory.PLAYER)
class AutoRegen : Module() {

    val modeValue = ListValue("Mode", arrayOf("Delay","Packet"), "Delay")
    var packetmod = ListValue("RegenPacketMode", arrayOf("C03","C04","Normal"), "Normal")
    private val regenspeed = IntegerValue("RegenSpeed", 15, 1, 50)
    private val healthValue = FloatValue("Health", 10F, 1F, 20F)
    private val delayValue = IntegerValue("Delay", 150, 0, 1000)
    private val RegenDelayTime = MSTimer()

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        when (modeValue.get().toLowerCase()) {
            "packet" -> {
                if (mc.thePlayer!!.health <= healthValue.get()) {
                    if (mc.thePlayer.onGround) {
                        repeat(regenspeed.get()) {
                            when(packetmod.get().toLowerCase()){
                                "c03" -> mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                                "c04" -> mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
                                "normal" -> {
                                    mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                                    mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
                                }
                            }
                        }
                    }
                }
            }

            "delay" -> {
                if (RegenDelayTime.hasTimePassed(delayValue.get().toLong())) {
                    if (mc.thePlayer!!.health <= healthValue.get()) {
                        if (mc.thePlayer.onGround) {
                            repeat(regenspeed.get()) {
                                when(packetmod.get().toLowerCase()){
                                    "c03" -> mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                                    "c04" -> mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
                                    "normal" -> {
                                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                                        mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
                                        RegenDelayTime.reset()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    override val tag: String
        get() = modeValue.get()
}