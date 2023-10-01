/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.potion.Potion
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.server.S12PacketEntityVelocity
import kotlin.math.sqrt
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing


class NewHypixelHop : SpeedMode("NewHypixelHop") {

    private val bypassMode = ListValue("BypassMode", arrayOf("Latest", "Legit", "GroundStrafe"), "Latest")
    private val customSpeedBoost = FloatValue("SpeedPotJumpModifier", 0.1f, 0f, 0.4f)
    private val yMotion = FloatValue("JumpYMotion", 0.42f, 0.395f, 0.42f)
    private val damageBoost = BoolValue("DamageBoost", true)
    private val sussyPacket = BoolValue("Rise6sussyPacket", true)


    private var minSpeed = 0.0

    private var wasOnGround = false
    private var offGroundTicks = 0
    private var groundTick = 0
    override fun onMotion() {
        TODO("Not yet implemented")
    }

    override fun onUpdate() {
        TODO("Not yet implemented")
    }

    override fun onMove(event: MoveEvent?) {
        mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionZ = 0.0

        if (mc.thePlayer.onGround) {
            offGroundTicks = 0
        } else {
            offGroundTicks += 1
        }



        when (bypassMode.get()) {

            "latest" -> {
                if (sussyPacket.get())
                    PacketUtils.sendPacketNoEvent(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, BlockPos(-1,-1,-1), EnumFacing.UP))

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump()

                    val minSpeed = 0.42f + 0.04f * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1).toFloat()
                    MovementUtils.strafe(MovementUtils.getSpeed() * (1.0 + 0.065 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1)).toFloat())
                    if (MovementUtils.getSpeed() < minSpeed) {
                        MovementUtils.strafe(minSpeed)
                    }
                    MovementUtils.strafe(MovementUtils.getSpeed() * 0.99f)


                } else {

                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        mc.thePlayer.motionX *= (1.0003 + 0.0008 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1))
                        mc.thePlayer.motionZ *= (1.0003 + 0.0008 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1))
                    }

                    mc.thePlayer.speedInAir = 0.02f + 0.0005f * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1).toFloat()

                }
            }

            "legit" -> {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump()
                }
            }
            "groundstrafe" -> {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump()
                    MovementUtils.strafe(MovementUtils.getSpeed())
                }
            }
        }
    }


    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S12PacketEntityVelocity) {
            if (mc.thePlayer == null || (mc.theWorld?.getEntityByID(packet.entityID) ?: return) != mc.thePlayer) {
                return
            }
            if (!LiquidBounce.combatManager.inCombat) {
                return
            }

            if (packet.motionY / 8000.0 > 0.1) {
                if (damageBoost.get()) {
                    mc.thePlayer.motionX *= 1.05
                    mc.thePlayer.motionZ *= 1.05
                }
            }
        }
    }
}
