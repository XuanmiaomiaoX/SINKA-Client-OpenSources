package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.utils.Skid.VecRotation;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static java.lang.Math.PI;

@SideOnly(Side.CLIENT)
public final class RotationUtils extends MinecraftInstance implements Listenable {

    private static Random random = new Random();

    private static int keepLength;

    public static Rotation targetRotation;
    public static Rotation serverRotation = new Rotation(0F, 0F);

    public static boolean keepCurrentRotation = false;

    private static double x = random.nextDouble();
    private static double y = random.nextDouble();
    private static double z = random.nextDouble();
    public static Rotation getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;
        double yDiff = y - mc.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / Math.PI);
        return new Rotation(yaw,pitch);
    }
    private static int revTick;
    public static void setTargetRotationReverse(final Rotation rotation, final int keepLength, final int revTick) {
        if(Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch())
                || rotation.getPitch() > 90 || rotation.getPitch() < -90)
            return;

        rotation.fixedSensitivity(mc.gameSettings.mouseSensitivity);
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
        RotationUtils.revTick = revTick+1;
    }
    public static Rotation getAngles(Entity entity) {
        if (entity == null)
            return null;
        final EntityPlayerSP thePlayer = mc.thePlayer;

        final double diffX = entity.posX - thePlayer.posX,
                diffY = entity.posY + entity.getEyeHeight() * 0.9 - (thePlayer.posY + thePlayer.getEyeHeight()),
                diffZ = entity.posZ - thePlayer.posZ, dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ); // @on

        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F,
                pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new Rotation (thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - thePlayer.rotationYaw),
                thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - thePlayer.rotationPitch));
    }
    public static Rotation getDirectionToBlock(final double x, final double y, final double z, final EnumFacing enumfacing) {
        final EntityEgg var4 = new EntityEgg(mc.theWorld);
        var4.posX = x + 0.5D;
        var4.posY = y + 0.5D;
        var4.posZ = z + 0.5D;
        var4.posX += (double) enumfacing.getDirectionVec().getX() * 0.5D;
        var4.posY += (double) enumfacing.getDirectionVec().getY() * 0.5D;
        var4.posZ += (double) enumfacing.getDirectionVec().getZ() * 0.5D;
        return getRotations(var4.posX, var4.posY, var4.posZ);
    }
    public static Rotation calculate(final Vec3 from, final Vec3 to) {
        final Vec3 diff = to.subtract(from);
        final double distance = Math.hypot(diff.xCoord, diff.zCoord);
        final float yaw = (float) (MathHelper.atan2(diff.zCoord, diff.xCoord) * (180F / PI)) - 90.0F;
        final float pitch = (float) (-(MathHelper.atan2(diff.yCoord, distance) * (180F / PI)));
        return new Rotation(yaw, pitch);
    }

    public static Rotation calculate(final Vec3 position, final EnumFacing enumFacing) {
        double x = position.xCoord + 0.5D;
        double y = position.yCoord + 0.5D;
        double z = position.zCoord + 0.5D;

        x += (double) enumFacing.getDirectionVec().getX() * 0.5D;
        y += (double) enumFacing.getDirectionVec().getY() * 0.5D;
        z += (double) enumFacing.getDirectionVec().getZ() * 0.5D;
        return calculate(new Vec3(x, y, z));
    }

    public static Rotation calculate(final Vec3 to) {
        return calculate(mc.thePlayer.getPositionVector().add(new Vec3(0, mc.thePlayer.getEyeHeight(), 0)), new Vec3(to.xCoord, to.yCoord, to.zCoord));
    }
    public static Rotation getRotationFromEyeHasPrev(double x, double y, double z) {
        double xDiff = x - (mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX));
        double yDiff = y - ((mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY)) + (mc.thePlayer.getEntityBoundingBox().maxY - mc.thePlayer.getEntityBoundingBox().minY));
        double zDiff = z - (mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ));
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        return new Rotation((float) (Math.atan2(zDiff, xDiff) * 180D / Math.PI) - 90F, (float) -(Math.atan2(yDiff, dist) * 180D / Math.PI));
    }

    public static Rotation getRotationFromEyeHasPrev(EntityLivingBase target) {
        final double x = (target.prevPosX + (target.posX - target.prevPosX));
        final double y = (target.prevPosY + (target.posY - target.prevPosY));
        final double z = (target.prevPosZ + (target.posZ - target.prevPosZ));
        return getRotationFromEyeHasPrev(x, y, z);
    }
    public static net.ccbluex.liquidbounce.utils.Skid.VecRotation calculateCenter(final String calMode, final String randMode, final double randomRange, final AxisAlignedBB bb, final boolean predict, final boolean throughWalls) {

        net.ccbluex.liquidbounce.utils.Skid.VecRotation vecRotation = null;

        double xMin = 0.0D;
        double yMin = 0.0D;
        double zMin = 0.0D;
        double xMax = 0.0D;
        double yMax = 0.0D;
        double zMax = 0.0D;
        double xDist = 0.0D;
        double yDist = 0.0D;
        double zDist = 0.0D;

        xMin = 0.15D; xMax = 0.85D; xDist = 0.1D;
        yMin = 0.15D; yMax = 1.00D; yDist = 0.1D;
        zMin = 0.15D; zMax = 0.85D; zDist = 0.1D;

        Vec3 curVec3 = null;

        switch(calMode) {
            case "LiquidBounce":
                xMin = 0.15D; xMax = 0.85D; xDist = 0.1D;
                yMin = 0.15D; yMax = 1.00D; yDist = 0.1D;
                zMin = 0.15D; zMax = 0.85D; zDist = 0.1D;
                break;
            case "Full":
                xMin = 0.00D; xMax = 1.00D; xDist = 0.1D;
                yMin = 0.00D; yMax = 1.00D; yDist = 0.1D;
                zMin = 0.00D; zMax = 1.00D; zDist = 0.1D;
                break;
            case "HalfUp":
                xMin = 0.10D; xMax = 0.90D; xDist = 0.1D;
                yMin = 0.50D; yMax = 0.90D; yDist = 0.1D;
                zMin = 0.10D; zMax = 0.90D; zDist = 0.1D;
                break;
            case "HalfDown":
                xMin = 0.10D; xMax = 0.90D; xDist = 0.1D;
                yMin = 0.10D; yMax = 0.50D; yDist = 0.1D;
                zMin = 0.10D; zMax = 0.90D; zDist = 0.1D;
                break;
            case "CenterSimple":
                xMin = 0.45D; xMax = 0.55D; xDist = 0.0125D;
                yMin = 0.65D; yMax = 0.75D; yDist = 0.0125D;
                zMin = 0.45D; zMax = 0.55D; zDist = 0.0125D;
                break;
            case "CenterLine":
                xMin = 0.45D; xMax = 0.55D; xDist = 0.0125D;
                yMin = 0.10D; yMax = 0.90D; yDist = 0.1D;
                zMin = 0.45D; zMax = 0.55D; zDist = 0.0125D;
                break;
        }

        for(double xSearch = xMin; xSearch < xMax; xSearch += xDist) {
            for (double ySearch = yMin; ySearch < yMax; ySearch += yDist) {
                for (double zSearch = zMin; zSearch < zMax; zSearch += zDist) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, predict);

                    if(throughWalls || isVisible(vec3)) {
                        final net.ccbluex.liquidbounce.utils.Skid.VecRotation currentVec = new net.ccbluex.liquidbounce.utils.Skid.VecRotation(vec3, rotation);

                        if (vecRotation == null || (getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))) {
                            vecRotation = currentVec;
                            curVec3 = vec3;
                        }
                    }
                }
            }
        }

        if(vecRotation == null || randMode == "Off")
            return vecRotation;

        double rand1 = random.nextDouble();
        double rand2 = random.nextDouble();
        double rand3 = random.nextDouble();

        final double xRange = bb.maxX - bb.minX;
        final double yRange = bb.maxY - bb.minY;
        final double zRange = bb.maxZ - bb.minZ;
        double minRange = 999999.0D;

        if(xRange<=minRange) minRange = xRange;
        if(yRange<=minRange) minRange = yRange;
        if(zRange<=minRange) minRange = zRange;

        rand1 = rand1 * minRange * randomRange;
        rand2 = rand2 * minRange * randomRange;
        rand3 = rand3 * minRange * randomRange;

        final double xPrecent = minRange * randomRange / xRange;
        final double yPrecent = minRange * randomRange / yRange;
        final double zPrecent = minRange * randomRange / zRange;

        Vec3 randomVec3 = new Vec3(
                curVec3.xCoord - xPrecent * (curVec3.xCoord - bb.minX) + rand1,
                curVec3.yCoord - yPrecent * (curVec3.yCoord - bb.minY) + rand2,
                curVec3.zCoord - zPrecent * (curVec3.zCoord - bb.minZ) + rand3
        );
        switch(randMode) {
            case "Horizonal":
                randomVec3 = new Vec3(
                        curVec3.xCoord - xPrecent * (curVec3.xCoord - bb.minX) + rand1,
                        curVec3.yCoord,
                        curVec3.zCoord - zPrecent * (curVec3.zCoord - bb.minZ) + rand3
                );
                break;
            case "Vertical":
                randomVec3 = new Vec3(
                        curVec3.xCoord,
                        curVec3.yCoord - yPrecent * (curVec3.yCoord - bb.minY) + rand2,
                        curVec3.zCoord
                );
                break;
        }

        final Rotation randomRotation = toRotation(randomVec3, predict);

        vecRotation =  new net.ccbluex.liquidbounce.utils.Skid.VecRotation(randomVec3, randomRotation);

        return vecRotation;
    }

    public static Rotation getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtils.getRotationFromPosition(x, z, y);
    }
    public static Rotation getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = RotationUtils.mc.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double)player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new Rotation(yaw,pitch);
    }

    public static Rotation getRotationsEntity(EntityLivingBase entity) {
        return RotationUtils.getRotations(entity.posX, entity.posY + entity.getEyeHeight() - 0.4, entity.posZ);
    }
    public static Rotation getRotationsNonLivingEntity(Entity entity) {
        return RotationUtils.getRotations(entity.posX, entity.posY + (entity.getEntityBoundingBox().maxY-entity.getEntityBoundingBox().minY)*0.5, entity.posZ);
    }

    public static net.ccbluex.liquidbounce.utils.Skid.VecRotation faceBlock(final BlockPos blockPos) {
        if (blockPos == null)
            return null;

        net.ccbluex.liquidbounce.utils.Skid.VecRotation vecRotation = null;

        for(double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
            for(double ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
                for (double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
                    final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
                    final Vec3 posVec = new Vec3(blockPos).addVector(xSearch, ySearch, zSearch);
                    final double dist = eyesPos.distanceTo(posVec);

                    final double diffX = posVec.xCoord - eyesPos.xCoord;
                    final double diffY = posVec.yCoord - eyesPos.yCoord;
                    final double diffZ = posVec.zCoord - eyesPos.zCoord;

                    final double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

                    final Rotation rotation = new Rotation(
                            MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
                            MathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)))
                    );

                    final Vec3 rotationVector = getVectorForRotation(rotation);
                    final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * dist, rotationVector.yCoord * dist,
                            rotationVector.zCoord * dist);
                    final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(eyesPos, vector, false,
                            false, true);

                    if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        final net.ccbluex.liquidbounce.utils.Skid.VecRotation currentVec = new net.ccbluex.liquidbounce.utils.Skid.VecRotation(posVec, rotation);

                        if (vecRotation == null || getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))
                            vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }

    /**
     * Face target with bow
     *
     * @param target your enemy
     * @param silent client side rotations
     * @param predict predict new enemy position
     * @param predictSize predict size of predict
     */
    public static void faceBow(final Entity target, final boolean silent, final boolean predict, final float predictSize) {
        final EntityPlayerSP player = mc.thePlayer;

        final double posX = target.posX + (predict ? (target.posX - target.prevPosX) * predictSize : 0) - (player.posX + (predict ? (player.posX - player.prevPosX) : 0));
        final double posY = target.getEntityBoundingBox().minY + (predict ? (target.getEntityBoundingBox().minY - target.prevPosY) * predictSize : 0) + target.getEyeHeight() - 0.15 - (player.getEntityBoundingBox().minY + (predict ? (player.posY - player.prevPosY) : 0)) - player.getEyeHeight();
        final double posZ = target.posZ + (predict ? (target.posZ - target.prevPosZ) * predictSize : 0) - (player.posZ + (predict ? (player.posZ - player.prevPosZ) : 0));
        final double posSqrt = Math.sqrt(posX * posX + posZ * posZ);

        float velocity = LiquidBounce.moduleManager.getModule(FastBow.class).getState() ? 1F : player.getItemInUseDuration() / 20F;
        velocity = (velocity * velocity + velocity * 2) / 3;

        if(velocity > 1) velocity = 1;

        final Rotation rotation = new Rotation(
                (float) (Math.atan2(posZ, posX) * 180 / Math.PI) - 90,
                (float) -Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(velocity * velocity * velocity * velocity - 0.006F * (0.006F * (posSqrt * posSqrt) + 2 * posY * (velocity * velocity)))) / (0.006F * posSqrt)))
        );

        if(silent)
            setTargetRotation(rotation);
        else
            limitAngleChange(new Rotation(player.rotationYaw, player.rotationPitch), rotation,10 +
                    new Random().nextInt(6)).toPlayer(mc.thePlayer);
    }

    /**
     * Translate vec to rotation
     *
     * @param vec target vec
     * @param predict predict new location of your body
     * @return rotation
     */
    public static Rotation toRotation(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY +
                mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        if(predict) eyesPos.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);

        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;

        return new Rotation(MathHelper.wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), MathHelper.wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        ));
    }

    /**
     * Get the center of a box
     *
     * @param bb your box
     * @return center of box
     */
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Rotation OtherRotation(final AxisAlignedBB bb,final Vec3 vec, final boolean predict,final boolean throughWalls, final float distance) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY +
                mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        final Vec3 eyes = mc.thePlayer.getPositionEyes(1F);
        net.ccbluex.liquidbounce.utils.Skid.VecRotation vecRotation = null;
        for(double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, predict);
                    final double vecDist = eyes.distanceTo(vec3);

                    if (vecDist > distance)
                        continue;

                    if(throughWalls || isVisible(vec3)) {
                        final net.ccbluex.liquidbounce.utils.Skid.VecRotation currentVec = new net.ccbluex.liquidbounce.utils.Skid.VecRotation(vec3, rotation);

                        if (vecRotation == null)
                            vecRotation = currentVec;
                    }
                }
            }
        }

        if(predict) eyesPos.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);

        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;

        return new Rotation(MathHelper.wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), MathHelper.wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        ));
    }
    public static net.ccbluex.liquidbounce.utils.Skid.VecRotation searchCenter(final AxisAlignedBB bb, final boolean outborder, final boolean random,
                                                                               final boolean predict, final boolean throughWalls, final float distance) {
        if(outborder) {
            final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * (x * 0.3 + 1.0), bb.minY + (bb.maxY - bb.minY) * (y * 0.3 + 1.0), bb.minZ + (bb.maxZ - bb.minZ) * (z * 0.3 + 1.0));
            return new net.ccbluex.liquidbounce.utils.Skid.VecRotation(vec3, toRotation(vec3, predict));
        }

        final Vec3 randomVec = new Vec3(bb.minX + (bb.maxX - bb.minX) * x * 0.8, bb.minY + (bb.maxY - bb.minY) * y * 0.8, bb.minZ + (bb.maxZ - bb.minZ) * z * 0.8);
        final Rotation randomRotation = toRotation(randomVec, predict);

        final Vec3 eyes = mc.thePlayer.getPositionEyes(1F);

        net.ccbluex.liquidbounce.utils.Skid.VecRotation vecRotation = null;

        for(double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch,
                            bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, predict);
                    final double vecDist = eyes.distanceTo(vec3);

                    if (vecDist > distance)
                        continue;

                    if(throughWalls || isVisible(vec3)) {
                        final net.ccbluex.liquidbounce.utils.Skid.VecRotation currentVec = new VecRotation(vec3, rotation);

                        if (vecRotation == null || (random ? getRotationDifference(currentVec.getRotation(), randomRotation) < getRotationDifference(vecRotation.getRotation(), randomRotation) : getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation())))
                            vecRotation = currentVec;
                    }
                }
            }
        }

        return vecRotation;
    }

    /**
     * Calculate difference between the client rotation and your entity
     *
     * @param entity your entity
     * @return difference between rotation
     */
    public static double getRotationDifference(final Entity entity) {
        final Rotation rotation = toRotation(getCenter(entity.getEntityBoundingBox()), true);

        return getRotationDifference(rotation, new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch));
    }

    /**
     * Calculate difference between the server rotation and your rotation
     *
     * @param rotation your rotation
     * @return difference between rotation
     */
    public static double getRotationDifference(final Rotation rotation) {
        return serverRotation == null ? 0D : getRotationDifference(rotation, serverRotation);
    }

    /**
     * Calculate difference between two rotations
     *
     * @param a rotation
     * @param b rotation
     * @return difference between rotation
     */
    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    /**
     * Limit your rotation using a turn speed
     *
     * @param currentRotation your current rotation
     * @param targetRotation your goal rotation
     * @param turnSpeed your turn speed
     * @return limited rotation
     */
    @NotNull
    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)
                ));
    }

    /**
     * Calculate difference between two angle points
     *
     * @param a angle point
     * @param b angle point
     * @return difference between angle points
     */
    private static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

    /**
     * Calculate rotation to vector
     *
     * @param rotation your rotation
     * @return target vector
     */
    public static Vec3 getVectorForRotation(final Rotation rotation) {
        float yawCos = MathHelper.cos(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
        float yawSin = MathHelper.sin(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
        float pitchCos = -MathHelper.cos(-rotation.getPitch() * 0.017453292F);
        float pitchSin = MathHelper.sin(-rotation.getPitch() * 0.017453292F);
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }

    /**
     * Allows you to check if your crosshair is over your target entity
     *
     * @param targetEntity your target entity
     * @param blockReachDistance your reach
     * @return if crosshair is over target
     */
    public static boolean isFaced(final Entity targetEntity, double blockReachDistance) {
        return RaycastUtils.raycastEntity(blockReachDistance, entity -> entity == targetEntity) != null;
    }

    /**
     * Allows you to check if your enemy is behind a wall
     */
    public static boolean isVisible(final Vec3 vec3) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        return mc.theWorld.rayTraceBlocks(eyesPos, vec3) == null;
    }

    /**
     * Handle minecraft tick
     *
     * @param event Tick event
     */
    @EventTarget
    public void onTick(final TickEvent event) {
        if(targetRotation != null) {
            keepLength--;

            if (keepLength <= 0)
                reset();
        }

        if(random.nextGaussian() > 0.8D) x = Math.random();
        if(random.nextGaussian() > 0.8D) y = Math.random();
        if(random.nextGaussian() > 0.8D) z = Math.random();
    }

    /**
     * Handle packet
     *
     * @param event Packet Event
     */
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if(packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;

            if(targetRotation != null && !keepCurrentRotation && (targetRotation.getYaw() != serverRotation.getYaw() || targetRotation.getPitch() != serverRotation.getPitch())) {
                packetPlayer.yaw = targetRotation.getYaw();
                packetPlayer.pitch = targetRotation.getPitch();
                packetPlayer.rotating = true;
            }

            if(packetPlayer.rotating) serverRotation = new Rotation(packetPlayer.yaw, packetPlayer.pitch);
        }
    }

    /**
     * Set your target rotation
     *
     * @param rotation your target rotation
     */
    public static void setTargetRotation(final Rotation rotation) {
        setTargetRotation(rotation, 0);
    }

    /**
     * Set your target rotation
     *
     * @param rotation your target rotation
     */
    public static void setTargetRotation(final Rotation rotation, final int keepLength) {
        if(Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch())
                || rotation.getPitch() > 90 || rotation.getPitch() < -90)
            return;

        rotation.fixedSensitivity(mc.gameSettings.mouseSensitivity);
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
    }

    /**
     * Reset your target rotation
     */
    public static void reset() {
        keepLength = 0;
        targetRotation = null;
    }

    /**
     * @return YESSSS!!!
     */
    @Override
    public boolean handleEvents() {
        return true;
    }
}
