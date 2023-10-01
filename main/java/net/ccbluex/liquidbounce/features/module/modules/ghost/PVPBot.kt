/*
 *Code by Mimosa
 */
package net.ccbluex.liquidbounce.features.module.modules.ghost

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.settings.GameSettings
import net.minecraft.item.ItemPotion
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.potion.Potion
import kotlin.random.Random

@ModuleInfo(name = "PVPBot", description = "AutoPVP.", category = ModuleCategory.GHOST)
class PVPBot: Module() {
    private val turnSpeedValue = FloatValue("TurnSpeed", 10F, 1F, 180F)
    private val CombatRange = FloatValue("CombatRange", 15F, 3F, 180F)
    private val speedmode = ListValue("SpeedMode",arrayOf("Speed","AutoJump","Off"),"Off")
    private val wtap = ListValue("WtapMode", arrayOf("Wtap","Walk", "Nope"), "Wtap")
    private val fovValue = FloatValue("FOV", 180F, 1F, 180F)
    private val centerValue = BoolValue("Center", false)
    private val block = BoolValue("AutoBlock", false)
    private val lockValue = BoolValue("Lock", true)
    private val onClickValue = BoolValue("OnClick", false)
    private val jitterValue = BoolValue("Jitter", true)
    private val legit = BoolValue("Legit",true)
    private val healthValue = FloatValue("PotHealth", 15F, 1F, 20F)
    private val delayValue = IntegerValue("PotDelay", 500, 500, 1000)

    private val openInventoryValue = BoolValue("PotOpenInv", false)
    private val simulateInventory = BoolValue("PotSimulateInventory", true)

    private val groundDistanceValue = FloatValue("PotGroundDistance", 2F, 0F, 5F)
    private val modeValue = ListValue("PotMode", arrayOf("Normal", "Jump", "Port"), "Normal")

    private val msTimer = MSTimer()
    private var potion = -1

    private val clickTimer = MSTimer()

    override fun onEnable() {
        LiquidBounce.hud.addNotification(Notification("BOT WARNING!","You are being controlled by a bot",
            NotifyType.BOT,10000))
        super.onEnable()
    }


    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        //判断speed开启 如果不是就自动跳
        when(speedmode.get()){
            "Speed" -> {
                LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = true
            }
            "AutoJump" -> {
                if (MovementUtils.isMoving()) {
                    LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = false
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump()
                    }
                }
            }
            "Off" -> {
                LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = false
            }
        }
        when(wtap.get()) {
            "Wtap" -> {
                var target=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target
                var target2=(LiquidBounce.moduleManager[LegitAura::class.java] as LegitAura).target
                //判断目标是否充满
                if (!legit.get()){
                    if (block.get()){
                        if(target!=null){
                            mc.rightClickMouse()
                        }
                    }
                    mc.gameSettings.keyBindForward.pressed = target==null
                    LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = true
                }
                if (legit.get()){
                    if (block.get()){
                        if(target2!=null){
                            mc.rightClickMouse()
                        }
                    }
                    mc.gameSettings.keyBindForward.pressed = target2==null
                    LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                }
            }
            "Walk" -> {
                var target=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target
                var target2=(LiquidBounce.moduleManager[LegitAura::class.java] as LegitAura).target
                if (!legit.get()){
                    if (block.get()){
                        if(target!=null){
                            mc.rightClickMouse()
                        }
                    }
                    mc.gameSettings.keyBindForward.pressed = true
                    LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = true
                }
                if (legit.get()){
                    if (block.get()){
                        if(target2!=null){
                            mc.rightClickMouse()
                        }
                    }
                    mc.gameSettings.keyBindForward.pressed = true
                    LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                }
            }
            "Nope" -> {
                var target=(LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target
                var target2=(LiquidBounce.moduleManager[LegitAura::class.java] as LegitAura).target
                if (!legit.get()){
                    if (block.get()){
                        if(target!=null){
                            mc.rightClickMouse()
                        }
                    }
                    LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = true
                }
                if (legit.get()){
                    if (block.get()){
                        if(target2!=null){
                            mc.rightClickMouse()
                        }
                    }
                    LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                }
            }
        }
        //判断安全光环的真假
        LiquidBounce.moduleManager.getModule(LegitAura::class.java)!!.state = legit.get()

    }
    //自瞄模块
    @EventTarget
    fun onStrafe(event: StrafeEvent) {
        if (mc.gameSettings.keyBindAttack.isKeyDown)
            clickTimer.reset()

        if (onClickValue.get() && clickTimer.hasTimePassed(500L))
            return

        val range = CombatRange.get()
        val entity = mc.theWorld.loadedEntityList
            .filter {
                EntityUtils.isSelected(it, true) && mc.thePlayer.canEntityBeSeen(it) &&
                        mc.thePlayer.getDistanceToEntityBox(it) <= range && RotationUtils.getRotationDifference(it) <= fovValue.get()
            }
            .minBy { RotationUtils.getRotationDifference(it) } ?: return

        if (!lockValue.get() && RotationUtils.isFaced(entity, range.toDouble()))
            return

        val rotation = RotationUtils.limitAngleChange(
            Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch),
            if (centerValue.get())
                RotationUtils.toRotation(RotationUtils.getCenter(entity.entityBoundingBox), true)
            else
                RotationUtils.searchCenter(entity.entityBoundingBox, false, false, true,
                    false,range).rotation,
            (turnSpeedValue.get() + Math.random()).toFloat()
        )

        rotation.toPlayer(mc.thePlayer)

        if (jitterValue.get()) {
            val yaw = Random.nextBoolean()
            val pitch = Random.nextBoolean()
            val yawNegative = Random.nextBoolean()
            val pitchNegative = Random.nextBoolean()

            if (yaw)
                mc.thePlayer.rotationYaw += if (yawNegative) -RandomUtils.nextFloat(0F, 1F) else RandomUtils.nextFloat(0F, 1F)

            if (pitch) {
                mc.thePlayer.rotationPitch += if (pitchNegative) -RandomUtils.nextFloat(0F, 1F) else RandomUtils.nextFloat(0F, 1F)
                if (mc.thePlayer.rotationPitch > 90)
                    mc.thePlayer.rotationPitch = 90F
                else if (mc.thePlayer.rotationPitch < -90)
                    mc.thePlayer.rotationPitch = -90F
            }
        }
    }

    override fun onDisable() {
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward))
        LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
        LiquidBounce.moduleManager.getModule(LegitAura::class.java)!!.state = false
        LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = false
        LiquidBounce.hud.addNotification(
            Notification("BOT Exit","Bot Exit Control",
                NotifyType.BOT,10000)
        )
    }
    @EventTarget
    fun onMotion(motionEvent: MotionEvent) {
        if (!msTimer.hasTimePassed(delayValue.get().toLong()) || mc.playerController.isInCreativeMode)
            return

        when (motionEvent.eventState) {
            EventState.PRE -> {
                // Hotbar Potion
                val potionInHotbar = findPotion(36, 45)

                if (mc.thePlayer.health <= healthValue.get() && potionInHotbar != -1) {
                    if (mc.thePlayer.onGround) {
                        when (modeValue.get().toLowerCase()) {
                            "jump" -> mc.thePlayer.jump()
                            "port" -> mc.thePlayer.moveEntity(0.0, 0.42, 0.0)
                        }
                    }

                    // Prevent throwing potions into the void
                    val fallingPlayer = FallingPlayer(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY,
                        mc.thePlayer.posZ,
                        mc.thePlayer.motionX,
                        mc.thePlayer.motionY,
                        mc.thePlayer.motionZ,
                        mc.thePlayer.rotationYaw,
                        mc.thePlayer.moveStrafing,
                        mc.thePlayer.moveForward
                    )

                    val collisionBlock = fallingPlayer.findCollision(20)

                    if (mc.thePlayer.posY - (collisionBlock?.y ?: 0) >= groundDistanceValue.get())
                        return

                    potion = potionInHotbar
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange(potion - 36))

                    if (mc.thePlayer.rotationPitch <= 80F) {
                        RotationUtils.setTargetRotation(Rotation(mc.thePlayer.rotationYaw, RandomUtils.nextFloat(80F, 90F)))
                    }
                    return
                }

                // Inventory Potion -> Hotbar Potion
                val potionInInventory = findPotion(9, 36)
                if (potionInInventory != -1 && InventoryUtils.hasSpaceHotbar()) {
                    if (openInventoryValue.get() && mc.currentScreen !is GuiInventory)
                        return

                    val openInventory = mc.currentScreen !is GuiInventory && simulateInventory.get()

                    if (openInventory)
                        mc.netHandler.addToSendQueue(
                            C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
                        )

                    mc.playerController.windowClick(0, potionInInventory, 0, 1, mc.thePlayer)

                    if (openInventory)
                        mc.netHandler.addToSendQueue(C0DPacketCloseWindow())

                    msTimer.reset()
                }
            }
            EventState.POST -> {
                if (potion >= 0 && RotationUtils.serverRotation.pitch >= 75F) {
                    val itemStack = mc.thePlayer.inventoryContainer.getSlot(potion).stack

                    if (itemStack != null) {
                        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(itemStack))
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))

                        msTimer.reset()
                    }

                    potion = -1
                }
            }
        }
    }

    private fun findPotion(startSlot: Int, endSlot: Int): Int {
        for (i in startSlot until endSlot) {
            val stack = mc.thePlayer.inventoryContainer.getSlot(i).stack

            if (stack == null || stack.item !is ItemPotion || !ItemPotion.isSplash(stack.itemDamage))
                continue

            val itemPotion = stack.item as ItemPotion

            for (potionEffect in itemPotion.getEffects(stack))
                if (potionEffect.potionID == Potion.heal.id)
                    return i

            if (!mc.thePlayer.isPotionActive(Potion.regeneration))
                for (potionEffect in itemPotion.getEffects(stack))
                    if (potionEffect.potionID == Potion.regeneration.id) return i
        }

        return -1
    }
}
