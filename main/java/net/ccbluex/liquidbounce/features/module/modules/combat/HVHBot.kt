package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.features.module.modules.movement.TargetStrafe
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.Rotation
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import kotlin.random.Random

@ModuleInfo(name = "HVHBot", description = "SinkaHVH" ,category = ModuleCategory.COMBAT)
class HVHBot : Module() {

    private val rangeValue = FloatValue("Range", 50F, 10F, 60F)
    private val turnSpeedValue = FloatValue("TurnSpeed", 30F, 20F, 180F)
    val trackDelay = IntegerValue("TrackDelay",3500,3500,3500)
    val hurtTrack = BoolValue("HurtTrack",true)
    private val keepBlock = BoolValue("KeepBlock",false)
    private val speedValue = BoolValue("OpenSpeed",true)
    private val targetStrafe = BoolValue("TargetStrafe",false)
    private val fovValue = 180F
    private val centerValue =true
    private val lockValue =true
    var isGO = false
    private val onClickValue =false
    private val jitterValue = false

    private val clickTimer = MSTimer()
    val msTimer = MSTimer()


    override fun onEnable() {
        LiquidBounce.hud.addNotification(Notification("BOT WARNING!","You are being controlled by a hacker bot", NotifyType.BOT,10000))
        super.onEnable()
    }
    override fun onDisable() {
        LiquidBounce.hud.addNotification(
            Notification("BOT Exit","Hacker Bot Exit Control",
                NotifyType.BOT,10000)
        )
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if(keepBlock.get()){
            mc.rightClickMouse()
        }
        (LiquidBounce.moduleManager[TargetStrafe::class.java]as TargetStrafe).state = targetStrafe.get()
        (LiquidBounce.moduleManager[KillAura::class.java]as KillAura).state=true
        if(hurtTrack.get()){

            if(mc.thePlayer.hurtTime!=0) {
                msTimer.reset()
                isGO=true
            }
            if(!isGO){
                mc.gameSettings.keyBindForward.pressed = false
                (LiquidBounce.moduleManager[Speed::class.java]as Speed).state = false
            }
            if (isGO && !msTimer.hasTimePassed(trackDelay.get().toLong())) {
                mc.gameSettings.keyBindForward.pressed = true
                if(speedValue.get()){
                    (LiquidBounce.moduleManager[Speed::class.java]as Speed).state = true
                }
                aimBot()
            }
            if(msTimer.hasTimePassed(trackDelay.get().toLong())){
                isGO = false
            }
        }else{
            mc.gameSettings.keyBindForward.pressed = true
            aimBot()
            (LiquidBounce.moduleManager[Speed::class.java]as Speed).state = speedValue.get()
        }
    }
    private fun aimBot(){
        if (mc.gameSettings.keyBindAttack.isKeyDown)
            clickTimer.reset()

        if (onClickValue && clickTimer.hasTimePassed(500L))
            return

        val range = rangeValue.get()
        val entity = mc.theWorld.loadedEntityList.filter {
            EntityUtils.isSelected(it, true) && mc.thePlayer.canEntityBeSeen(it) && mc.thePlayer.getDistanceToEntityBox(it) <= range && RotationUtils.getRotationDifference(it) <= fovValue
        }
            .minBy { RotationUtils.getRotationDifference(it) } ?: return

        if (!lockValue&& RotationUtils.isFaced(entity, range.toDouble()))
            return

        val rotation = RotationUtils.limitAngleChange(
            Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch),
            if (centerValue)
                RotationUtils.toRotation(RotationUtils.getCenter(entity.entityBoundingBox), true)
            else
                RotationUtils.searchCenter(entity.entityBoundingBox, false, false, true,
                    false,range).rotation,
            (turnSpeedValue.get() + Math.random()).toFloat()
        )

        rotation.toPlayer(mc.thePlayer)

        if (jitterValue) {
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
}