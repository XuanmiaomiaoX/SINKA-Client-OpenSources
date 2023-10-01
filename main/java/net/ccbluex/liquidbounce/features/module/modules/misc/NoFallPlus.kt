package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.player.NoFall
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "NoFallPlus", description = ":/", category = ModuleCategory.MISC)
class NoFallPlus : Module(){
    var distance = FloatValue("Distance",2.0F,2.0F,10F)
    @EventTarget
    fun onUpdate(event: UpdateEvent){
        val noFall = LiquidBounce.moduleManager[NoFall::class.java] as NoFall
        if (mc.thePlayer.fallDistance > distance.get()) noFall.state = true
        if (mc.thePlayer.onGround) noFall.state = false
    }
}