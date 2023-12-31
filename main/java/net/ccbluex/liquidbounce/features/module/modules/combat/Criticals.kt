
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.*
import net.minecraft.network.play.server.S0BPacketAnimation
import net.minecraft.stats.StatList


@ModuleInfo(name = "Criticals", description = "Automatically deals critical hits." ,category = ModuleCategory.COMBAT)
class Criticals : Module() {

    val modeValue = ListValue("Mode", arrayOf("Packet", "NCPPacket", "HytBoom", "VulCanHop", "VulCanSemi" ,"Low 2" ,"Hypixel", "Hypixel2", "AACPacket" ,"AAC4.3.11OldHYT" ,"Lite" ,"AAC5.0.4" ,"AAC5.0.4New" ,"NoGround", "Visual", "TPHop", "FakeCollide", "Mineplex" ,"More", "TestMinemora", "Motion", "Hover"), "Packet")
    val motionValue = ListValue("MotionMode", arrayOf("RedeSkyLowHop", "Hop", "Jump", "LowJump", "MinemoraTest"), "Jump")
    val hoverValue = ListValue("HoverMode", arrayOf("AAC4", "AAC4Other", "OldRedesky", "Normal1", "Normal2", "Minis", "Minis2", "TPCollide", "2b2t"), "AAC4")
    val packetcriticals = ListValue("CriticalsPacketMode", arrayOf("C04","C06"),"C04")
    val hoverNoFall = BoolValue("HoverNoFall", true)
    val hoverCombat = BoolValue("HoverOnlyCombat", true)
    val delayValue = IntegerValue("Delay", 0, 0, 500)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val debugValue = BoolValue("DebugMessage", false)

    val msTimer = MSTimer()
    val flagTimer = MSTimer()
    private var target = 0
    var jState = 0
    var aacLastState = false
    var attacks = 0

    override fun onEnable() {
        if (modeValue.equals("NoGround")) {
            mc.thePlayer.jump()
        }
        jState = 0
        attacks = 0
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityLivingBase) {
            val entity = event.targetEntity
            target = entity.entityId


            if (!mc.thePlayer.onGround || mc.thePlayer.isOnLadder || mc.thePlayer.isInWeb || mc.thePlayer.isInWater ||
                mc.thePlayer.isInLava || mc.thePlayer.ridingEntity != null || entity.hurtTime > hurtTimeValue.get() ||
                LiquidBounce.moduleManager[Fly::class.java]!!.state || !msTimer.hasTimePassed(delayValue.get().toLong())) {
                return
            }



            fun sendCriticalPacket(xOffset: Double = 0.0, yOffset: Double = 0.0, zOffset: Double = 0.0, ground: Boolean) {
                val x = mc.thePlayer.posX + xOffset
                val y = mc.thePlayer.posY + yOffset
                val z = mc.thePlayer.posZ + zOffset
                if (packetcriticals.get() == "C06") {
                    mc.netHandler.addToSendQueue(C06PacketPlayerPosLook(x, y, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, ground))
                } else {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, ground))
                }
            }

            when (modeValue.get().toLowerCase()) {
                "packet" -> {
                    sendCriticalPacket(yOffset = 0.0625, ground = true)
                    sendCriticalPacket(ground = false)
                    sendCriticalPacket(yOffset = 1.1E-5, ground = false)
                    sendCriticalPacket(ground = false)
                }

                "ncppacket" -> {
                    sendCriticalPacket(yOffset = 0.11, ground = false)
                    sendCriticalPacket(yOffset = 0.1100013579, ground = false)
                    sendCriticalPacket(yOffset = 0.0000013579, ground = false)
                }

                "hytboom" -> {
                    attacks++
                    if(attacks > 8) {
                        mc.thePlayer.motionY = 0.23333333//哈哈哈哈哈
                        attacks = 0
                    }
                }

                "aac5.0.4new" -> {
                    sendCriticalPacket(yOffset = 0.00133545, ground = false)
                    sendCriticalPacket(yOffset = -0.000000433, ground = false)
                    sendCriticalPacket(yOffset = 0.00114514, ground = false)
                    sendCriticalPacket(yOffset = -0.0000004114514, ground = false)
                }

                "aac5.0.4" -> { //aac5.0.4 moment but with bad cfg(cuz it will flag for timer)
                    sendCriticalPacket(yOffset = 0.00133545, ground = false)
                    sendCriticalPacket(yOffset = -0.000000433, ground = false)
                }

                "hypixel" -> {
                    sendCriticalPacket(yOffset = 0.04132332, ground = false)
                    sendCriticalPacket(yOffset = 0.023243243674, ground = false)
                    sendCriticalPacket(yOffset = 0.01, ground = false)
                    sendCriticalPacket(yOffset = 0.0011, ground = false)
                }

                "vulcanhop" -> {
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.21
                    }
                }

                "vulcansemi" -> {
                    attacks++
                    if(attacks > 6) {
                        sendCriticalPacket(yOffset = 0.2, ground = false)
                        sendCriticalPacket(yOffset = 0.1216, ground = false)
                        attacks = 0
                    }
                }

                "aac4.3.11oldhyt" -> {
                    sendCriticalPacket(yOffset = 0.042487, ground = false)
                    sendCriticalPacket(yOffset = 0.0104649713461000007, ground = false)
                    sendCriticalPacket(yOffset = 0.0014749900000101, ground = false)
                    sendCriticalPacket(yOffset = 0.0000007451816400000, ground = false)
                }

                "hypixel2" -> {
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                }

                "mineplex" -> {
                    sendCriticalPacket(yOffset = 0.0000000000000045, ground = false)
                    sendCriticalPacket(ground = false)
                }

                "low 2" -> {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (0.3 - Math.random() / 500), mc.thePlayer.posZ)
                }

                "more" -> {
                    sendCriticalPacket(yOffset = 0.00000000001, ground = false)
                    sendCriticalPacket(ground = false)
                }

                // Minemora criticals without test
                "testminemora" -> {
                    sendCriticalPacket(yOffset = 0.0114514, ground = false)
                    sendCriticalPacket(yOffset = 0.0010999999940395355, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.0012016413, ground = false)
                }

                "aacpacket" -> {
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.01400000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                }


                "fakecollide" -> {
                    val motionX: Double
                    val motionZ: Double
                    if (MovementUtils.isMoving()) {
                        motionX = mc.thePlayer.motionX
                        motionZ = mc.thePlayer.motionZ
                    } else {
                        motionX = 0.00
                        motionZ = 0.00
                    }
                    mc.thePlayer.triggerAchievement(StatList.jumpStat)
                    sendCriticalPacket(xOffset = motionX / 3, yOffset = 0.20000004768372, zOffset = motionZ / 3, ground = false)
                    sendCriticalPacket(xOffset = motionX / 1.5, yOffset = 0.12160004615784, zOffset = motionZ / 1.5, ground = false)
                }

                "tphop" -> {
                    sendCriticalPacket(yOffset = 0.02, ground = false)
                    sendCriticalPacket(yOffset = 0.01, ground = false)
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ)
                }

                "visual" -> mc.thePlayer.onCriticalHit(entity)

                "motion" -> {
                    when (motionValue.get().toLowerCase()) {
                        "jump" -> mc.thePlayer.motionY = 0.42
                        "lowjump" -> mc.thePlayer.motionY = 0.3425
                        "redeskylowhop" -> mc.thePlayer.motionY = 0.35
                        "hop" -> {
                            mc.thePlayer.motionY = 0.1
                            mc.thePlayer.fallDistance = 0.1f
                            mc.thePlayer.onGround = false
                        }
                        "minemoratest" -> {
                            mc.timer.timerSpeed = 0.82f
                            mc.thePlayer.motionY = 0.124514
                        }
                    }
                }
            }
            msTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {

        val packet = event.packet

        if ((mc.thePlayer!!.fallDistance > 0.12f) && (mc.thePlayer!!.fallDistance < 10f) && (mc.timer.timerSpeed < 1f)) {
            mc.timer.timerSpeed = 1f
        }

        if (packet is C03PacketPlayer) {
            when (modeValue.get().toLowerCase()) {

                "noground" -> packet.onGround = false

                "motion" -> {
                    when (motionValue.get().toLowerCase()) {
                        "minemoratest" -> if (!LiquidBounce.combatManager.inCombat) mc.timer.timerSpeed = 1.00f
                    }
                }

                "hover" -> {
                    if (hoverCombat.get() && !LiquidBounce.combatManager.inCombat) return
                    if (packet is C05PacketPlayerLook) {
                        mc.netHandler.addToSendQueue(C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, packet.yaw, packet.pitch, packet.onGround))
                        event.cancelEvent()
                        return
                    } else if (!(packet is C04PacketPlayerPosition) && !(packet is C06PacketPlayerPosLook)) {
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, packet.onGround))
                        event.cancelEvent()
                        return
                    }
                    when (hoverValue.get().toLowerCase()) {
                        "2b2t" -> {
                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                                jState++
                                when (jState) {
                                    2 -> packet.y += 0.02
                                    3 -> packet.y += 0.01
                                    4 -> {
                                        if (hoverNoFall.get()) packet.onGround = true
                                        jState = 1
                                    }
                                    else -> jState = 1
                                }
                            } else jState = 0
                        }
                        "minis2" -> {
                            if (mc.thePlayer.onGround && !aacLastState) {
                                packet.onGround = mc.thePlayer.onGround
                                aacLastState = mc.thePlayer.onGround
                                return
                            }
                            aacLastState = mc.thePlayer.onGround
                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                                jState++
                                if (jState % 2 == 0) {
                                    packet.y += 0.015625
                                } else if (jState> 100) {
                                    if (hoverNoFall.get()) packet.onGround = true
                                    jState = 1
                                }
                            } else jState = 0
                        }
                        "tpcollide" -> {
                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                                jState++
                                when (jState) {
                                    2 -> packet.y += 0.20000004768372
                                    3 -> packet.y += 0.12160004615784
                                    4 -> {
                                        if (hoverNoFall.get()) packet.onGround = true
                                        jState = 1
                                    }
                                    else -> jState = 1
                                }
                            } else jState = 0
                        }

                        "minis" -> {
                            if (mc.thePlayer.onGround && !aacLastState) {
                                packet.onGround = mc.thePlayer.onGround
                                aacLastState = mc.thePlayer.onGround
                                return
                            }
                            aacLastState = mc.thePlayer.onGround
                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                                jState++
                                if (jState % 2 == 0) {
                                    packet.y += 0.0625
                                } else if (jState> 50) {
                                    if (hoverNoFall.get()) packet.onGround = true
                                    jState = 1
                                }
                            } else jState = 0
                        }
                        "normal1" -> {
                            if (mc.thePlayer.onGround) {
                                if (!(hoverNoFall.get() && jState == 0)) packet.onGround = false
                                jState++
                                when (jState) {
                                    2 -> packet.y += 0.001335979112147
                                    3 -> packet.y += 0.0000000131132
                                    4 -> packet.y += 0.0000000194788
                                    5 -> packet.y += 0.00000000001304
                                    6 -> {
                                        if (hoverNoFall.get()) packet.onGround = true
                                        jState = 1
                                    }
                                    else -> jState = 1
                                }
                            } else jState = 0
                        }
                        "aac4other" -> {
                            if (mc.thePlayer.onGround && !aacLastState && hoverNoFall.get()) {
                                packet.onGround = mc.thePlayer.onGround
                                aacLastState = mc.thePlayer.onGround
                                packet.y += 0.00101
                                return
                            }
                            aacLastState = mc.thePlayer.onGround
                            packet.y += 0.001
                            if (mc.thePlayer.onGround || !hoverNoFall.get()) packet.onGround = false
                        }
                        "aac4" -> {
                            if (mc.thePlayer.onGround && !aacLastState && hoverNoFall.get()) {
                                packet.onGround = mc.thePlayer.onGround
                                aacLastState = mc.thePlayer.onGround
                                packet.y += 0.000000000000136
                                return
                            }
                            aacLastState = mc.thePlayer.onGround
                            packet.y += 0.000000000000036
                            if (mc.thePlayer.onGround || !hoverNoFall.get()) packet.onGround = false
                        }
                        "normal2" -> {
                            if (mc.thePlayer.onGround) {
                                if (!(hoverNoFall.get() && jState == 0)) packet.onGround = false
                                jState++
                                when (jState) {
                                    2 -> packet.y += 0.00000000000667547
                                    3 -> packet.y += 0.00000000000045413
                                    4 -> packet.y += 0.000000000000036
                                    5 -> {
                                        if (hoverNoFall.get()) packet.onGround = true
                                        jState = 1
                                    }
                                    else -> jState = 1
                                }
                            } else jState = 0
                        }
                        "oldredesky" -> {
                            if (hoverNoFall.get() && mc.thePlayer.fallDistance> 0) {
                                packet.onGround = true
                                return
                            }

                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                            }
                        }
                    }
                }
            }
        }
        if (packet is S0BPacketAnimation && debugValue.get()) {
            if (packet.animationType == 4 && packet.entityID == target) {
                val name = (LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura).target!!.name
                alert("§b[§bCoolSenseTips]§f触发§c暴击§b(§6玩家:§a$name)")
            }
        }
    }

    override val tag: String
        get() = modeValue.get()
}
