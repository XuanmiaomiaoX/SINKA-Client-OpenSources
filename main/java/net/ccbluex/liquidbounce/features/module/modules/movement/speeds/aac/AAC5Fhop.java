//by HXC
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class AAC5Fhop extends SpeedMode {
    public AAC5Fhop() {
        super("AAC5Fhop");
    }

    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving())
            return;
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            mc.thePlayer.speedInAir = (float) 0.0201;
            mc.timer.timerSpeed = (float) 0.94;
        }
        if (mc.thePlayer.fallDistance > 0.7 && mc.thePlayer.fallDistance < 1.3) {
            mc.thePlayer.speedInAir = (float) 0.02;
            mc.timer.timerSpeed = (float) 1.8;
        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(MoveEvent event) {

    }
}