package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.Skid.MotionData
import net.ccbluex.liquidbounce.utils.Skid.PlayerUtil
import net.ccbluex.liquidbounce.utils.Skid.TimerUtil
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.init.Blocks
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.util.BlockPos

/**
 * @author KingXingXing and Kiras Help Line
 * @version 0.1
 * @see net.ccbluex.liquidbounce.features.module.Module
 */
@ModuleInfo(name = "AntiFall", description = "Prevents you from falling in void.", category = ModuleCategory.PLAYER)
class AntiFall : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Hypixel", "AntiCheat"), "Hypixel")
    private val distanceValue = FloatValue("Distance", 7.0f, 0.1f, 15.0f)
    private val packetList: MutableList<Packet<*>> = ArrayList()
    private var lastGround: MotionData? = null
    private var overVoid = false
    private var onGround = false
    private val voidTimer = TimerUtil()
    override val tag: String
        get() = modeValue.get()

    override fun onEnable() {
        lastGround = null
        overVoid = false
        packetList.clear()
        voidTimer.reset()
    }

    override fun onDisable() {
        packetList.clear()
        voidTimer.reset()
    }

    /**
     * @param event UpdateEvent
     */
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (modeValue.get() == "AntiCheat") {
            if (mc.thePlayer.fallDistance >= 10) clearPacket()
            if (onGround) {
                lastGround = null
                if (MovementUtils.isOnGround(0.0001)) onGround = false
                return
            }
            if (!isOverVoid()) {
                lastGround = MotionData(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY,
                    mc.thePlayer.posZ,
                    -8.412878165707724E-4,
                    mc.thePlayer.motionY,
                    -0.11784442128235458
                )
                clearPacket()
                overVoid = false
            } else {
                if (mc.thePlayer.fallDistance >= 6 && overVoid && voidTimer.hasReached(500.0)) {
                    packetList.clear()
                    if (lastGround != null) MovementUtils.setPosition(lastGround!!)
                    overVoid = false
                    onGround = true
                    voidTimer.reset()
                }
            }
        }
    }

    /**
     * @param event PacketEvent
     */
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (modeValue.get() == "AntiCheat") {
            if (mc.thePlayer != null && isOverVoid() && packet is C03PacketPlayer) {
                packetList.add(packet)
                event.cancelEvent()
                overVoid = true
            }
        }
        if (modeValue.get() == "Hypixel" && packet is C03PacketPlayer && mc.thePlayer.fallDistance > distanceValue.get() && !PlayerUtil.isBlockUnder()) packet.y += (distanceValue.get() + 4).toDouble()
    }

    private fun clearPacket() {
        if(packetList.isNotEmpty()) packetList.forEach { mc.netHandler.addToSendQueue(it) }
        packetList.clear()
    }

    /**
     *
     * @return boolean
     */
    private fun isOverVoid(): Boolean {
        var inVoid = true
        var block = BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)
        var index = mc.thePlayer.posY + 1
        while (index > 0) {
            if (mc.theWorld.getBlockState(block).block !== Blocks.air) {
                inVoid = false
                break
            }
            block = block.add(0, -1, 0)
            index -= 0.5
        }
        index = 0.0
        while (index < 10) {
            if (MovementUtils.isOnGround(index) && inVoid) {
                inVoid = false
                break
            }
            index += 0.1
        }
        return inVoid
    }
}