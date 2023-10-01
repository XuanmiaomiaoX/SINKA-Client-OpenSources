//by HXC
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.util.MathHelper;

public class AAC5Lhop extends SpeedMode {
    public AAC5Lhop() {
        super("AAC5Lhop");
    }

    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving())
            return;
        if (mc.thePlayer.onGround) {
            mc.gameSettings.keyBindJump.pressed = false;
            mc.thePlayer.jump();
        }
        if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance <= 0.1) {
            mc.thePlayer.speedInAir = (float) 0.02;
            mc.timer.timerSpeed = 1.5F;
        }
        if (mc.thePlayer.fallDistance > 0.1 && mc.thePlayer.fallDistance < 1.3) {
            mc.timer.timerSpeed = (float) 0.7;
        }
        if (mc.thePlayer.fallDistance >= 1.3) {
            mc.timer.timerSpeed = 1;
            mc.thePlayer.speedInAir = (float) 0.02;
        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(MoveEvent event) {

    }
}