
package net.ccbluex.liquidbounce.features.module.modules.ghost

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.ListValue


@ModuleInfo(name = "CombatSound", description = "Automatically deals critical hits." ,category = ModuleCategory.GHOST)
class CombatSound : Module() {

    val Sound = ListValue("Sound", arrayOf("Mouse", "GlassSound2", "StoneSound"),"Mouse")
    @EventTarget
    fun onAttack(event: AttackEvent) {
            when(Sound.get()) {
                "Mouse" -> {
                    LiquidBounce.tipSoundManager.mouse.asyncPlay()
                }

                "GlassSound2" -> {
                    LiquidBounce.tipSoundManager.disableSound.asyncPlay()
                }

                "StoneSound" -> {
                    LiquidBounce.tipSoundManager.stone.asyncPlay()
                }
            }
    }
}
