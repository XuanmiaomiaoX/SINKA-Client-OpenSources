package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "Pack", description = "Shows the amount of packets flowing in the arraylist (module tag).", category = ModuleCategory.RENDER)
class Pack : Module(){
    var time = 0
    var aa = 0
    var packets = 0
    var started = false

    override fun onEnable() {
        started = false
    }

    @EventTarget
    fun onPacket(event: PacketEvent){
        aa++
        if(time >= 20) {
            time = 0
            packets = aa
            aa = 0
            started = true
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        time++
    }

    fun getPacket() : String {
        if(started) {
            if(packets < 999) {
                return packets.toString()
            } else {
                return "§d" + packets.toString()
            }
        } else {
            return "Packet..."
        }
    }

    override val tag: String?
        get() = getPacket()
}