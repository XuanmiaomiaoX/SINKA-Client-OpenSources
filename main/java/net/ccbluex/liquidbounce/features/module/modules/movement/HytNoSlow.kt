/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.item.*
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.network.play.server.S09PacketHeldItemChange
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

@ModuleInfo(name = "HYTNoSlow", description = "Cancels slowness effects caused by soulsand and using items.",
    category = ModuleCategory.MOVEMENT)
class HYTNoSlow : Module() {

    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val packet = BoolValue("OldPacket", true)
    private val C07C08packet = BoolValue("C07C08BlockPacket", true)
    private val C07C08packetDelay = IntegerValue("C07C08BlockPacketDelay", 1,0,10)
    private var C07C08 = 10
    private val C09BlockPacket = BoolValue("C09BlockPacket", true)
    private val C09packetDelay = IntegerValue("C09BlockPacketDelay", 1,0,10)
    private var C09 = 10
    private val C09FoodPacket = BoolValue("C09FoodPacket", true)
    private val C09FoodpacketDelay = IntegerValue("C09FoodPacketDelay", 1,0,10)
    private var C09Food = 10
    private val Debug = BoolValue("Debug",false)
    // Soulsand
    val soulsandValue = BoolValue("Soulsand", true)
    override fun onEnable() {
        C09 = 10
        C09Food = 10
        C07C08 = 10
    }
    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet
        if(packet is C09PacketHeldItemChange){

        }
    }
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if(C09FoodPacket.get()) {
            val heldItem = mc.thePlayer.heldItem
            if ( MovementUtils.isMoving()&& (heldItem.item is ItemPotion||heldItem.item is ItemFood)&&(mc.gameSettings.keyBindRight.isKeyDown||mc.thePlayer.isEating||mc.thePlayer.isUsingItem)){

                C09Food++
                if(C09Food >= C09FoodpacketDelay.get()){
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer!!.inventory.currentItem + 1) % 9))
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer!!.inventory.currentItem) % 9))

                    C09Food = 0
                }
            }else{

                C09Food = 10
            }
        }
        if(C09BlockPacket.get()) {
            if (MovementUtils.isMoving()&&mc.thePlayer.isBlocking) {
                if(KillAura().blockingStatus){return}
                C09++
                if(C09 >= C09packetDelay.get()){
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer!!.inventory.currentItem + 1) % 9))
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer!!.inventory.currentItem) % 9))
                    if(Debug.get()){
                        ClientUtils.displayChatMessage("C09")
                    }
                    C09 = 0
                }
            }else{
                C09 = 10
            }
        }
        if(C07C08packet.get()){
            if (mc.thePlayer.isBlocking || KillAura().blockingStatus) {
                C07C08++
                if (C07C08 >= C07C08packetDelay.get()) {
                    mc.netHandler.addToSendQueue(
                        C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                            BlockPos.ORIGIN,
                            EnumFacing.DOWN
                        )
                    )
                    mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f))
                    C07C08 = 0
                }
            }else{
                C07C08 = 10
            }
        }
        if(this.packet.get()) {
            val heldItem = mc.thePlayer.heldItem
            if (heldItem == null || heldItem.item !is ItemSword || !MovementUtils.isMoving()) {
                return
            }
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            if (!mc.thePlayer.isBlocking && !killAura.blockingStatus) {
                return
            }
            if (this.packet.get()) {
                when (event.eventState) {
                    EventState.PRE -> {
                        val digging = C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                            BlockPos.ORIGIN,
                            EnumFacing.DOWN
                        )
                        mc.netHandler.addToSendQueue(digging)
                    }

                    EventState.POST -> {
                        val blockPlace = C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem())
                        mc.netHandler.addToSendQueue(blockPlace)
                    }
                }
            }
        }
    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer.heldItem?.item

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }

    private fun getMultiplier(item: Item?, isForward: Boolean) = when (item) {
        is ItemFood, is ItemPotion, is ItemBucketMilk -> {
            if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
        }
        is ItemSword -> {
            if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
        }
        is ItemBow -> {
            if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
        }
        else -> 0.2F
    }

}
