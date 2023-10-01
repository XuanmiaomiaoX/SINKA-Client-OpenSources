package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.item.ItemBucketMilk
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemPotion
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

@ModuleInfo(name = "FastUse", description = "Fast Eat" ,category = ModuleCategory.PLAYER)
class FastUse : Module() {

    private val modeValue = ListValue("Mode", arrayOf("NCP","Instant", "Timer", "CustomDelay", "Normal" ,"DelayedInstant", "MinemoraTest", "AAC", "VulCan" ,"NewAAC"), "DelayedInstant")
    private val timerValue = FloatValue("Timer", 1.22F, 0.1F, 2.0F)
    private val durationValue = IntegerValue("InstantDelay", 14, 0, 35)
    private val delayValue = IntegerValue("CustomDelay", 0, 0, 300)

    private val msTimer = MSTimer()
    private var usedTimer = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {

        mc.timer.timerSpeed = 1F

        if (usedTimer) {
            mc.timer.timerSpeed = 1F
            usedTimer = false
        }

        if (!mc.thePlayer.isUsingItem) {
            return
        }

        val usingItem = mc.thePlayer.itemInUse.item

        if (usingItem is ItemFood || usingItem is ItemBucketMilk || usingItem is ItemPotion) {
            when (modeValue.get().toLowerCase()) {
                "delayedinstant" -> if (mc.thePlayer.itemInUseDuration > durationValue.get()) {
                    repeat(36 - mc.thePlayer.itemInUseDuration) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    }

                    mc.playerController.onStoppedUsingItem(mc.thePlayer)
                }

                "ncp" -> if (mc.thePlayer.itemInUseDuration > 14) {
                    repeat(20) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    }

                    mc.playerController.onStoppedUsingItem(mc.thePlayer)}

                "instant" -> {
                    repeat(35) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    }

                    mc.playerController.onStoppedUsingItem(mc.thePlayer)
                }
                "aac" -> {
                    mc.timer.timerSpeed = 0.49F
                    usedTimer = true
                    if (mc.thePlayer.itemInUseDuration > 14) {
                        repeat(23) {
                            mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        }
                    }
                }
                "newaac" -> {
                    mc.timer.timerSpeed = 0.49F
                    usedTimer = true
                    repeat(2) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    }

                    // mc.playerController.onStoppedUsingItem(mc.thePlayer)
                }
                "timer" -> {
                    mc.timer.timerSpeed = timerValue.get()
                    usedTimer = true
                }

                "normal" ->{
                    mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
                }

                "vulcan" ->{
                    mc.timer.timerSpeed = 0.40F
                    usedTimer = true
                    mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
                }

                "minemoratest" -> {
                    mc.timer.timerSpeed = 0.5F
                    usedTimer = true
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        repeat(2) {
                            mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        }
                    }
                }

                "customdelay" -> {
                    if (!msTimer.hasTimePassed(delayValue.get().toLong())) {
                        return
                    }

                    mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    msTimer.reset()
                }
            }
        }
    }

    override fun onDisable() {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F
            usedTimer = false
        }
    }

    override val tag: String
        get() = modeValue.get()
}