package net.ccbluex.liquidbounce.features.module.modules.ghost

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.entity.item.EntityTNTPrimed
import net.minecraft.entity.projectile.EntityFireball

@ModuleInfo(name = "TNTHelper", description = "TNTHelper", category = ModuleCategory.GHOST)
class FireBallHelper : Module() {
    private val timer = MSTimer()

    private val TNT = BoolValue("TNT", true)
    private val Fireball = BoolValue("Fireball", true)
    private val rangeValue = FloatValue("Range", 9f, 1f, 20f)

    @EventTarget
    private fun onUpdate(event: UpdateEvent) {
        for (entity in mc.theWorld.loadedEntityList) {
            if(TNT.get()) {
                if (entity is EntityTNTPrimed && mc.thePlayer.getDistanceToEntity(entity) <= rangeValue.get()) {
                    val tntPrimed = entity
                    mc.thePlayer.jump()
                }
                if(Fireball.get()) {
                    if (entity is EntityFireball && mc.thePlayer.getDistanceToEntity(entity) < 5.5 && timer.hasTimePassed(300)) {
                        mc.thePlayer.jump()
                        break
                    }
                }
            }
        }
    }
}