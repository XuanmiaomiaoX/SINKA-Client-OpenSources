/*
 *Code by Mimosa
 */
package net.ccbluex.liquidbounce.features.module.modules.ghost

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.timer.TimeUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemSword

@ModuleInfo(name = "AutoBlock", description = "Block.", category = ModuleCategory.GHOST)
class AutoBlock : Module() {

    private val BlockValue= ListValue("Mode", arrayOf("AutoBlock","BlockNow"), "AutoBlock")
    private val maxDelay: IntegerValue = object : IntegerValue("MaxDelay", 50, 20, 200) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val minDelayValueObject =  minDelay.get()
            if (minDelayValueObject > newValue) set(minDelayValueObject)
            delay1 = TimeUtils.randomDelay( minDelay.get(), this.get())
        }
    }

    private val minDelay: IntegerValue = object : IntegerValue("MinDelay", 50, 20, 200) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val maxDelayValueObject = maxDelay.get()
            if (maxDelayValueObject < newValue) set(maxDelayValueObject)
            delay1 = TimeUtils.randomDelay(this.get(), maxDelay.get())
        }
    }

    private val msTimer = MSTimer()
    var delay1 = TimeUtils.randomDelay(maxDelay.get(), minDelay.get())


    @EventTarget
    fun onAttack(event: AttackEvent) {
        when(BlockValue.get()){
            "AutoBlock"-> {
                while (msTimer.hasTimePassed(delay1)){
                    mc.rightClickMouse()
                    msTimer.reset()
                }
            }
            "BlockNow"-> {
                if (event.targetEntity is EntityLivingBase&& mc.thePlayer.heldItem.item is ItemSword) {
                    mc.rightClickMouse()
                }
            }
        }
    }
}