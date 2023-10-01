package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils.sendPacketNoEvent
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.settings.KeyBinding
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.*
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import org.lwjgl.input.Keyboard

@ModuleInfo(
    name = "InventoryMove",
    description = "InvMove",
    category = ModuleCategory.MOVEMENT
)
class InventoryMove : Module() {
    val modeValue = ListValue("Mode", arrayOf("Basic", "NoPacket", "Medusa", "Vulcan","Hypixel"), "Basic")
    val sprintMode = ListValue("NoSprint", arrayOf("Real", "Spoof", "Off"), "Spoof")
    var lastInvOpen = false
    var invOpen = false
    var canCancel = false
    override val tag: String
        get() = modeValue.get()

    @EventTarget
    fun onKey(event: KeyEvent?) {
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (!MovementUtils.isMoving()) return
        val packet = event.packet
        if (lastInvOpen != invOpen) {
            if (sprintMode.get() == "Spoof" || modeValue.get() == "Medusa" || modeValue.get() == "Vulcan") {
                sendPacketNoEvent(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING))
            }
            lastInvOpen = invOpen
        } else if (mc.currentScreen is GuiInventory && packet is C0BPacketEntityAction) {
            event.cancelEvent()
        }
        if (lastInvOpen) {
            if (modeValue.get() == "Vulcan") {
                if (packet is C0EPacketClickWindow && !canCancel) {
                    canCancel = true
                }
                if (packet is C03PacketPlayer && canCancel) {
                    canCancel = false
                    event.cancelEvent()
                    val yaw = MovementUtils.getDirection()
                    sendPacketNoEvent(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
                }
            }
        }
        if (modeValue.get() == "NoPacket") {
            if (event.packet is C16PacketClientStatus && event.packet.status == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT || packet is C0BPacketEntityAction && packet.action == C0BPacketEntityAction.Action.OPEN_INVENTORY) event.cancelEvent()
        }
        if(modeValue.get() == "Hypixel"){
            if (packet is C0DPacketCloseWindow) {
                event.cancelEvent()
            }
            if (packet is C0EPacketClickWindow) {
                event.cancelEvent()
                sendPacketNoEvent(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
                sendPacketNoEvent(event.packet as Packet<INetHandlerPlayServer>)
                sendPacketNoEvent(C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId))
            }
            if (packet is C16PacketClientStatus) {
                if (packet.status == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                    event.cancelEvent()
                }
            }
        }
    }

    @EventTarget
    fun onClick(event: ClickWindowEvent?) {
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        invOpen = mc.currentScreen is GuiInventory
        if (mc.currentScreen != null && mc.currentScreen !is GuiChat) {
            val key = arrayOf(mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump)
            var array: Array<KeyBinding>
            val length = key.also { array = it }.size
            var i = 0
            while (i < length) {
                val b = array[i]
                KeyBinding.setKeyBindState(b.keyCode, Keyboard.isKeyDown(b.keyCode))
                ++i
            }
        }
    }
}