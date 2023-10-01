package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.player.Blink
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "FuckerHelper", description = "Bypass fucker", category = ModuleCategory.MISC)
class FuckerHelper : Module() {

    private val timer = FloatValue("SetTimer",0.1F,0.15F,0.5F)

    override fun onEnable() {
        LiquidBounce.moduleManager.getModule(Fucker::class.java)!!.state = true
        LiquidBounce.moduleManager.getModule(Blink::class.java)!!.state = true
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        mc.timer.timerSpeed = timer.get()
    }

    override fun onDisable() {
        LiquidBounce.moduleManager.getModule(Fucker::class.java)!!.state = false
        LiquidBounce.moduleManager.getModule(Blink::class.java)!!.state = false
        mc.timer.timerSpeed = 1F
    }

    override val tag: String?
        get() = "Bypass"
}