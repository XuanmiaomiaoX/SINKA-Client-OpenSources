package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement

@ModuleInfo(name = "HytPacketFix", description = "Fix C08 Packet is Hand", category = ModuleCategory.EXPLOIT)
class HytPacketFix : Module(){

    val packetmode = ListValue("PacketMode", arrayOf("OldFix","NewFix"),"NewFix")
    val cancelnullc08 = BoolValue("CanCelNullC08",false)
    val test = BoolValue("TestJump",false)

    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet
        if(cancelnullc08.get()){
        if(packet is C08PacketPlayerBlockPlacement){
            if(mc.thePlayer.heldItem == null || mc.thePlayer.heldItem.item == null){
                   event.cancelEvent()
                }
            }
        }
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (test.get()) mc.thePlayer.jumpMovementFactor = 0F
    }
}