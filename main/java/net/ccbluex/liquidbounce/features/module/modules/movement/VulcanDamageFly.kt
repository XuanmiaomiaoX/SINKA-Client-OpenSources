package net.ccbluex.liquidbounce.features.module.modules.movement

import ibxm.Module
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.MinecraftInstance.mc
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.settings.GameSettings

import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.client.C0FPacketConfirmTransaction
import net.minecraft.network.play.server.S08PacketPlayerPosLook

import kotlin.math.sqrt
import kotlin.math.cos
import kotlin.math.sin

@ModuleInfo(name = "VulcanDamageFly", description = "VulcanDamageFly" , category = ModuleCategory.MOVEMENT)
class VulcanDamageFly : Module() {
    private val bypassMode = ListValue("BypassMode", arrayOf("Damage", "SelfDamage", "InstantDamage", "Flag"), "InstantDamage")
    private val flyMode = ListValue("FlyMode", arrayOf("Timer", "CancelMove", "Clip"), "CancelMove")
    private val flyHSpeedValue = FloatValue("Horizontal", 1.0f, 0.5f, 2.5f).displayable{ flyMode.equals("CancelMove") }
    private val flyVSpeedValue = FloatValue("Vertical", 0.42f, 0.42f, 2.5f).displayable{ flyMode.equals("CancelMove") }
    private val flyDistanceValue = FloatValue("Distance", 10.0f, 6.0f, 10.0f)
    private val flyTimerValue = FloatValue("Timer", 0.05f, 0.05f, 0.25f).displayable{ flyMode.equals("Timer") }
    private var waitFlag = false
    private var isStarted = false
    var isDamaged = false
    var dmgJumpCount = 0
    var flyTicks = 0

    private var lastSentX = 0.0
    private var lastSentY = 0.0
    private var lastSentZ = 0.0

    private var lastTickX = 0.0
    private var lastTickY = 0.0
    private var lastTickZ = 0.0

    fun runSelfDamageCore(): Boolean {
        mc.timer.timerSpeed = 1.0f
        if (bypassMode.equals("Damage") || bypassMode.equals("Flag")) {
            if (!bypassMode.equals("Flag")) {
                if (mc.thePlayer.hurtTime > 0 || isDamaged) {
                    isDamaged = true
                    dmgJumpCount = 999
                    return false
                }else {
                    return true
                }
            }
            isDamaged = true
            dmgJumpCount = 999
            return false
        }
        if (isDamaged) {
            dmgJumpCount = 999
            return false
        }
        mc.thePlayer.jumpMovementFactor = 0.00f
        if (mc.thePlayer.onGround) {
            if (dmgJumpCount >= 4) {
                mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
                isDamaged = true
                dmgJumpCount = 999
                return false
            }
            dmgJumpCount++
            FDPMovementUtils.resetMotion(true)
            mc.thePlayer.jump()
        }
        FDPMovementUtils.resetMotion(false)
        return true
    }
    @EventTarget
    fun onEnable() {
        flyTicks = 0
        waitFlag = false
        isStarted = false
        isDamaged = false
        dmgJumpCount = 0
        mc.timer.timerSpeed = 1.0f
        if (bypassMode.equals("InstantDamage")) {
            dmgJumpCount = 11451
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0784, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.41999998688697815, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.7531999805212, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.4199999868869781, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.7531999805212, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.419999986886978, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.7531999805212, mc.thePlayer.posZ, false))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.00133597911214, mc.thePlayer.posZ, false))
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.00133597911214, mc.thePlayer.posZ)
            waitFlag = true
        } else if (bypassMode.equals("Flag")) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ, true))
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
        } else {
            runSelfDamageCore()
        }
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {

        if (!bypassMode.equals("InstantDamage") && runSelfDamageCore()) {
            return
        }
        if (bypassMode.equals("InstantDamage") && dmgJumpCount == 11451) {
            if (!isStarted) {
                return
            } else {
                isStarted = false
                waitFlag = false
                mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
                dmgJumpCount = 999
            }
        }
        mc.thePlayer.jumpMovementFactor = 0.00f
        if (!isStarted && !waitFlag) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0784, mc.thePlayer.posZ, false))
            waitFlag = true
        }
        if (isStarted) {
            when (flyMode.get()) {
                "cancelmove" -> {
                    mc.timer.timerSpeed = 1.0f
                    FDPMovementUtils.resetMotion(false)
                    if (!mc.gameSettings.keyBindSneak.isKeyDown) {
                        FDPMovementUtils.resetMotion(true)
                        if (mc.gameSettings.keyBindJump.isKeyDown) {
                            mc.thePlayer.motionY = flyVSpeedValue.get().toDouble()
                        }
                    }

                    MovementUtils.strafe(flyHSpeedValue.get())
                }
                "timer" -> {
                    flyTicks++
                    mc.timer.timerSpeed = flyTimerValue.get()
                    FDPMovementUtils.resetMotion(true)
                    if (flyTicks > 4) {
                        MovementUtils.strafe(flyDistanceValue.get() - 0.005f)
                    } else {
                        MovementUtils.strafe(flyDistanceValue.get() - 0.205f + flyTicks.toFloat() * 0.05f)
                    }
                }
                "clip" -> {
                    FDPMovementUtils.resetMotion(true)
                    if (mc.thePlayer.ticksExisted % 10 == 0) {
                        flyTicks++
                        val yaw = Math.toRadians(mc.thePlayer.rotationYaw.toDouble())
                        mc.thePlayer.setPosition(mc.thePlayer.posX + (-sin(yaw) * flyDistanceValue.get()), mc.thePlayer.posY + 0.42, mc.thePlayer.posZ + (cos(yaw) * flyDistanceValue.get()))
                        PacketUtils.sendPacketNoEvent(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    }
                }
            }
        }
    }

    fun onDisable() {
        mc.timer.timerSpeed = 1.0f
    }

    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is C03PacketPlayer && waitFlag) {
            event.cancelEvent()
        }
        if (packet is C03PacketPlayer && (dmgJumpCount < 4 && ( bypassMode.equals("SelfDamage") || bypassMode.equals("InstantDamage") ) )) {
            packet.onGround = false
        }
        if (isStarted && flyMode.equals("cancelmove")) {
            if(packet is C03PacketPlayer && (packet is C04PacketPlayerPosition || packet is C06PacketPlayerPosLook)) {
                val deltaX = packet.x - lastSentX
                val deltaY = packet.y - lastSentY
                val deltaZ = packet.z - lastSentZ

                if (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) > flyDistanceValue.get()) {
                    flyTicks++
                    PacketUtils.sendPacketNoEvent(C04PacketPlayerPosition(lastTickX, lastTickY, lastTickZ, false))
                    lastSentX = lastTickX
                    lastSentY = lastTickY
                    lastSentZ = lastTickZ
                }
                lastTickX = packet.x
                lastTickY = packet.y
                lastTickZ = packet.z
                event.cancelEvent()
            }else if(packet is C03PacketPlayer) {
                event.cancelEvent()
            }
        }

        if (packet is C03PacketPlayer && flyMode.equals("clip") && isStarted) {
            event.cancelEvent()
        }

        if (packet is S08PacketPlayerPosLook) {
            isStarted = true
            waitFlag = false
        }

        if (packet is S08PacketPlayerPosLook && waitFlag && !flyMode.equals("cancelmove")) {
            if (bypassMode.equals("InstantDamage")) PacketUtils.sendPacketNoEvent(C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, false))
            mc.timer.timerSpeed = 1.0f
            flyTicks = 0

        } else if (packet is S08PacketPlayerPosLook && flyMode.equals("cancelmove")) {
            lastSentX = packet.x
            lastSentY = packet.y
            lastSentZ = packet.z

            if (!bypassMode.equals("InstantDamage")) event.cancelEvent()

            TransferUtils.noMotionSet = true
            PacketUtils.sendPacketNoEvent(C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, false))
        }

        if (packet is C0FPacketConfirmTransaction) { //Make sure it works with Vulcan Velocity
            val transUID = (packet.uid).toInt()
            if (transUID >= -31767 && transUID <= -30769) {
                event.cancelEvent()
                PacketUtils.sendPacketNoEvent(packet)
            }
        }
    }
}
