package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.PacketUtils.sendPacket
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.*

@ModuleInfo(name = "SoulRegen", description = "BetterRegen", category = ModuleCategory.PLAYER)
class SoulRegen : Module() {
    private val mode = ListValue("Mode", arrayOf("Packet", "Normal", "Ghostly", "Rotation"), "Packet")
    private val health = IntegerValue("Health", 18, 1, 20)
    private val speed = IntegerValue("Speed", 10, 1, 1000)
    private val onlyonground = BoolValue("OnlyOnGround",false)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if ((!onlyonground.get() || mc.thePlayer.onGround) && !mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.health < health.get()) {
            repeat(speed.get()){
                when (mode.get().toLowerCase()) {
                    "packet" -> sendPacket(C03PacketPlayer(mc.thePlayer.onGround))
                    "ghostly" -> sendPacket(C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1E-9 + 0.000001, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false))
                    "normal" -> {
                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
                    }
                    "rotation" -> sendPacket(C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false))
                }
            }
        }
    }
    override val tag: String?
        get() = mode.get()
}