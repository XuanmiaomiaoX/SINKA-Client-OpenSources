package net.ccbluex.liquidbounce.utils.misc;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class PositionAndRotationUtils {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public PositionAndRotationUtils(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PositionAndRotationUtils(C03PacketPlayer packet) {
        this.x = packet.getPositionX();
        this.y = packet.getPositionY();
        this.z = packet.getPositionZ();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
    }

    public PositionAndRotationUtils(S08PacketPlayerPosLook packet) {
        this.x = packet.getX();
        this.y = packet.getY();
        this.z = packet.getZ();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
    }

    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setPositionAndRotation(S08PacketPlayerPosLook packet) {
        this.x = packet.getX();
        this.y = packet.getY();
        this.z = packet.getZ();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
    }

    public void reset() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public static double getDistanceBetweenAngles(float angle1, float angle2) {
        float distance = Math.abs((float) (angle1 - angle2)) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }
}
