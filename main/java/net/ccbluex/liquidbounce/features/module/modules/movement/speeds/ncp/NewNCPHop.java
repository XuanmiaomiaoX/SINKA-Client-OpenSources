/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class NewNCPHop extends SpeedMode {
    private boolean legitJump;

    public NewNCPHop() {
        super("NewNCPHop");
    }

    @Override
    public void onMotion() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(MoveEvent event) {

    }

    @Override
    public void onTick() {
        mc.timer.timerSpeed = 1F;

        if(mc.thePlayer.isInWater())
            return;

        if(MovementUtils.isMoving()) {
            mc.timer.timerSpeed = 1.00f;
            if (Math.abs(mc.thePlayer.movementInput.moveStrafe) < 0.1f) {
                mc.thePlayer.jumpMovementFactor = 0.0265f;
            }else {
                mc.thePlayer.jumpMovementFactor = 0.0244f;
            }
                MovementUtils.strafe(0.34f);
            if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
                mc.timer.timerSpeed = 1.0f;
                mc.gameSettings.keyBindJump.pressed = false;
                mc.thePlayer.jump();
                MovementUtils.strafe();
                MovementUtils.strafe(0.34f);
            }else if (!MovementUtils.isMoving()) {
                mc.timer.timerSpeed = 1.00f;
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
            }
        }
    }
}