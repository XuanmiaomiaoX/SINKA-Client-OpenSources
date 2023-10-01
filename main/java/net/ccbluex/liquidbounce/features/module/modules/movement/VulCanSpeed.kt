package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.world.BlockFly
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.Skid.PlayerUtil
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.BlockAir
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.server.S08PacketPlayerPosLook


@ModuleInfo(name = "Speed2", description = "Skid Rise and iz1_", category = ModuleCategory.MOVEMENT)
class VulCanSpeed : Module() {

    private val modeValue = ListValue("Mode", arrayOf("VulCan1", "VulCan2","VulCan3", "Nettion"), "VulCan1")

    private var bool = false
    private var offGroundTicks = 0
    private var ticks = 0
    private var onGroundTicks = 0
    private var ticksDisable = 21

    override fun onEnable() {
        bool = false
        offGroundTicks = 0
        ticksDisable = 21
    }

    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet

        if (ticksDisable <= 20) {
            return
        }

        if (mc.thePlayer.ticksExisted <= 1) return

        if(modeValue.get() == "VulCan2") {
            if (packet is S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 20) {
                val s08 = packet
                if (mc.thePlayer.getDistanceSq(s08.x, s08.y, s08.z) < 25 * 4) {
                    event.cancelEvent()
                }
            }
        }
    }

    @EventTarget
    fun onStrafe(event: StrafeEvent) {
        if(modeValue.get() == "VulCan1"){
            if (offGroundTicks <= 2) MovementUtils.strafe()
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(modeValue.get() == "VulCan3"){
            mc.timer.timerSpeed = 1.00f
            if (Math.abs(mc.thePlayer.movementInput.moveStrafe) < 0.1f) {
                mc.thePlayer.jumpMovementFactor = 0.0265f
            }else {
                mc.thePlayer.jumpMovementFactor = 0.0244f
            }
            if (!mc.thePlayer.onGround) {
                mc.gameSettings.keyBindJump.pressed = mc.gameSettings.keyBindJump.isKeyDown
            }
            if (MovementUtils.getSpeed() < 0.215f) {
                MovementUtils.strafe(0.215f)
            }
            if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
                mc.timer.timerSpeed = 1.25f
                mc.gameSettings.keyBindJump.pressed = false
                mc.thePlayer.jump()
                MovementUtils.strafe()
                if(MovementUtils.getSpeed() < 0.5f) {
                    MovementUtils.strafe(0.4849f)
                }
            }else if (!MovementUtils.isMoving()) {
                mc.timer.timerSpeed = 1.00f
                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
            }
        } else if (modeValue.get() == "Nettion") {
            if (!PlayerUtil.isInLiquid()) {
                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
                if (PlayerUtil.isMoving() && mc.thePlayer.onGround) {
                    mc.thePlayer.jump()
                    MovementUtils.strafe()
                } else if (PlayerUtil.getSpeed() < 0.28) {
                    MovementUtils.strafe(0.28F)
                }
            }
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent){
        ++ticks
        ticksDisable++
        if (ticksDisable < 20) {
            return
        }

        if (mc.thePlayer.onGround) {
            offGroundTicks = 0
            onGroundTicks += 1
        } else {
            offGroundTicks += 1
            onGroundTicks = 0
        }

        if(modeValue.get() == "VulCan2"){
            if (mc.thePlayer.onGround && !(LiquidBounce.moduleManager[BlockFly::class.java] as BlockFly)
                    .state && mc.thePlayer.motionY > -.2
            ) {
                PacketUtils.sendPacket(
                    C04PacketPlayerPosition(
                        (mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2,
                        (mc.thePlayer.posY + mc.thePlayer.lastTickPosY) / 2 - 0.0784000015258789,
                        (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2,
                        false
                    )
                )
                PacketUtils.sendPacket(
                    C06PacketPlayerPosLook(
                        (mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2,
                        (mc.thePlayer.posY + mc.thePlayer.lastTickPosY) / 2,
                        (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2,
                        mc.thePlayer.rotationYaw,
                        mc.thePlayer.rotationPitch,
                        true
                    )
                )
                PacketUtils.sendPacket(
                    C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY,
                        mc.thePlayer.posZ,
                        false
                    )
                )
                PacketUtils.sendPacket(
                    C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY - 0.0784000015258789,
                        mc.thePlayer.posZ,
                        false
                    )
                )
                PacketUtils.sendPacket(
                    C06PacketPlayerPosLook(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY,
                        mc.thePlayer.posZ,
                        mc.thePlayer.rotationYaw,
                        mc.thePlayer.rotationPitch,
                        true
                    )
                )
                MovementUtils.strafe((MovementUtils.getBaseMoveSpeed() * 1.25 * 2).toFloat())
            } else if (offGroundTicks == 1) {
                MovementUtils.strafe((MovementUtils.getBaseMoveSpeed() * 0.91f).toFloat())
            }
        }

        if (modeValue.get() == "VulCan1") {
            if (MovementUtils.isMoving()) {
                if (mc.thePlayer.onGround) {
                    val speed: Double = MovementUtils.getBaseMoveSpeed() - 0.01
                    MovementUtils.strafe((speed - Math.random() / 2000).toFloat())
                    mc.thePlayer.jump()
                    bool = true
                } else {
                    if (bool) {
                        if (offGroundTicks > 3) mc.thePlayer.motionY =
                            MovementUtils.getPredictedMotionY(mc.thePlayer.motionY)
                        if (PlayerUtil.getBlockRelativeToPlayer(
                                0.0,
                                2.0,
                                0.0
                            ) !is BlockAir
                        ) MovementUtils.strafe((MovementUtils.getSpeed() * (1.1 - Math.random() / 500)).toFloat())
                    }
                    if (PlayerUtil.isInLiquid() || mc.thePlayer.hurtTime == 9) MovementUtils.strafe()
                }
            } else MovementUtils.stop()
        }
    }
}