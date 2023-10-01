/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
public class DCJLhop extends SpeedMode {

    public DCJLhop() {
        super("DCJLhop");
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
    public void onTick(){
        mc.timer.timerSpeed = 1.3F;
        if(MovementUtils.isMoving()) {
            if(mc.thePlayer.onGround){
                mc.thePlayer.motionY = 0.29;
            }
            MovementUtils.strafe(0.37F);
        }
        MovementUtils.strafe();
    }
}