/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.module.modules.misc.CustomTitle

class CustomTitleTimerCommand : Command("customtitletimerreset", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        val customtitle = LiquidBounce.moduleManager[CustomTitle::class.java] as CustomTitle

        chat("CustomTitleTimerReset SuccessFul!")
    }
}