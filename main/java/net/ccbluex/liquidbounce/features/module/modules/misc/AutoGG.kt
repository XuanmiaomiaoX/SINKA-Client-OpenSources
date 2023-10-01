package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.server.S02PacketChat

@ModuleInfo(name = "AutoGG", description = ":/", category = ModuleCategory.MISC)
class AutoGG : Module(){

    private val custommatchertext = TextValue("CustomContains","获得胜利!")
    private val customchatmessage = TextValue("CustomChatMessage","[CoolSense]GG")
    private val autonextgame = BoolValue("AutoNextGame",true)
    private val AutoNextGamemessage = TextValue("AutoNextGameMessage","/game bw-team")

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is S02PacketChat) {
            if (packet.chatComponent.unformattedText.contains(custommatchertext.get())) {
                mc.thePlayer.sendChatMessage(customchatmessage.get())
                ClientUtils.displayChatMessage("[SINKA]")
                if(autonextgame.get()) {
                    mc.thePlayer.sendChatMessage("/hub")
                    Thread {
                        try {
                            Thread.sleep(3000)
                            mc.thePlayer.sendChatMessage("[SINKA] Keep trying! | QQ:539182578")
                        } catch (ex: InterruptedException) {
                            ex.printStackTrace()
                        }
                    }.start()
                }
            }
        }
    }
}