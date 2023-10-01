/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.Rotation
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "SpinBot", category = ModuleCategory.PLAYER, description = "Cause damage to this area")
class SpinBot : Module() {

    private val X = IntegerValue("X", 150, -180, 180)
    private val Y = IntegerValue("Y", 85, -90, 90)
    private val spinSpeedValue = IntegerValue("spinSpeed", 3, 1, 90)
    private val rotateValue = BoolValue("Silent", true)
    private val sinkarotate = BoolValue("Spin", true)
    private val j = BoolValue("Jitter", false)
    private val huanyuan = BoolValue("Yaw Reduction", true)
    val msTimer1 = MSTimer()
    val msTimer2 = MSTimer()
    var mcyaw = 0
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if(huanyuan.get()&&msTimer2.hasTimePassed(500)){
            mcyaw= mc.thePlayer.rotationYaw.toInt()
            msTimer2.reset()
        }
        //ro
        if(msTimer1.hasTimePassed(5)&&sinkarotate.get()){
            X.value+=spinSpeedValue.value
            j.value=false
            huanyuan.value=false
        }
        if (rotateValue.get()&&!j.get()) {
            RotationUtils.setTargetRotation(Rotation(mcyaw+X.get().toFloat(), Y.get().toFloat()),20)
        } else if(j.get()){
            RotationUtils.setTargetRotation(Rotation(mcyaw+X.get()+ RandomUtils.nextDouble(-10.0, 10.0).toFloat(), Y.get().toFloat()+ RandomUtils.nextDouble(-3.0, 3.0).toFloat()),20)
        }else{
            mc.thePlayer.rotationYaw = X.get().toFloat()
            mc.thePlayer.rotationPitch = Y.get().toFloat()
        }
    }
}
