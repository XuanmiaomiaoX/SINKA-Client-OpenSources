/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class BMCHop extends SpeedMode {

    public BMCHop() {
        super("BMCHop");
    }

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1.0865F;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        if(MovementUtils.isMoving()) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.timer.timerSpeed = (float) 0.97F;
                mc.thePlayer.speedInAir = 0.0223F;
                mc.thePlayer.onGround = false;
                MovementUtils.strafe(0.28F);
            }
            if (mc.thePlayer.fallDistance > 0.7 && mc.thePlayer.fallDistance < 1.3) {
                mc.thePlayer.speedInAir = (float) 0.02;
                mc.timer.timerSpeed = (float) 1.7;
            }
        }else{
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }

    @Override
    public void onMove(MoveEvent event) {

    }
}