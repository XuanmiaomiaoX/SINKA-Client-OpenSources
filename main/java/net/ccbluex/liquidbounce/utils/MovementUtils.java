/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.utils.Skid.MotionData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

import java.math.BigDecimal;

public final class MovementUtils extends MinecraftInstance {
    public static float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }


    public static void strafe() {
        strafe(getSpeed());
    }

    public static void setPosition(MotionData posAndMotion) {
        mc.thePlayer.setPosition(posAndMotion.x, posAndMotion.y, posAndMotion.z);
        mc.thePlayer.motionX = posAndMotion.motionX;
        mc.thePlayer.motionY = posAndMotion.motionY;
        mc.thePlayer.motionZ = posAndMotion.motionZ;
    }

    public static void stop() {
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
    }

    public static void move() {
        move(getSpeed());
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            baseSpeed *= 1.0D + 0.2D * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);

        return baseSpeed;
    }

    public static double getBlockSpeed(final EntityLivingBase entityIn) {
        return BigDecimal.valueOf(Math.sqrt(Math.pow(entityIn.posX - entityIn.prevPosX, 2) + Math.pow(entityIn.posZ - entityIn.prevPosZ, 2)) * 20).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double getPredictedMotionY(final double motionY) {
        return (motionY - 0.08) * 0.98F;
    }

    public static boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
    }

    public static boolean hasMotion() {
        return mc.thePlayer.motionX != 0D && mc.thePlayer.motionZ != 0D && mc.thePlayer.motionY != 0D;
    }

    public static boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static void strafe(final float speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static void move(final float speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        mc.thePlayer.motionX += -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ += Math.cos(yaw) * speed;
    }

    public static void limitSpeed(final float speed){
        final double yaw = getDirection();
        final double maxXSpeed=-Math.sin(yaw) * speed;
        final double maxZSpeed=Math.cos(yaw) * speed;

        if(mc.thePlayer.motionX>maxZSpeed){
            mc.thePlayer.motionX=maxXSpeed;
        }
        if(mc.thePlayer.motionZ>maxZSpeed){
            mc.thePlayer.motionZ=maxZSpeed;
        }
    }

    public static void forward(final double length) {
        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        mc.thePlayer.setPosition(mc.thePlayer.posX + (-Math.sin(yaw) * length), mc.thePlayer.posY, mc.thePlayer.posZ + (Math.cos(yaw) * length));
    }

    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if(mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(mc.thePlayer.moveForward < 0F)
            forward = -0.5F;
        else if(mc.thePlayer.moveForward > 0F)
            forward = 0.5F;

        if(mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    private static double bps=0.0,lastX=0.0,lastY=0.0,lastZ=0.0;

    public static void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0F));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0F));
            mc.thePlayer.motionX = forward * speed * cos
                    + strafe * speed * sin;
            mc.thePlayer.motionZ = forward * speed * sin
                    - strafe * speed * cos;
        }
    }

    public static void updateBlocksPerSecond() {
        if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 1) {
            bps=0.0;
        }
        final double distance = mc.thePlayer.getDistance(lastX, lastY, lastZ);
        lastX=mc.thePlayer.posX;
        lastY=mc.thePlayer.posY;
        lastZ=mc.thePlayer.posZ;
        bps = distance * (20 * mc.timer.timerSpeed);
    }

    public static double getBlocksPerSecond(){
        return bps;
    }

    public static void setSpeed(final MoveEvent moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setZ(0);
            moveEvent.setX(0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

            moveEvent.setX((forward * moveSpeed * cos + strafe * moveSpeed * sin));
            moveEvent.setZ((forward * moveSpeed * sin - strafe * moveSpeed * cos));
        }
    }

    // TODO: Make better and faster calculation lol
    public static double calculateGround() {
        final AxisAlignedBB playerBoundingBox = mc.thePlayer.getEntityBoundingBox();
        double blockHeight = 1D;

        for(double ground = mc.thePlayer.posY; ground > 0D; ground -= blockHeight) {
            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);

            if(mc.theWorld.checkBlockCollision(customBox)) {
                if(blockHeight <= 0.05D)
                    return ground + blockHeight;

                ground += blockHeight;
                blockHeight = 0.05D;
            }
        }

        return 0F;
    }
}