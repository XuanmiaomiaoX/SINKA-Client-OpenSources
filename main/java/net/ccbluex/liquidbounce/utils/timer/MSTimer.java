/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils.timer;

public final class MSTimer {

    private long time = -1L;
    private long lastMS;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasTimePassed(final long MS) {
        return System.currentTimeMillis() >= time + MS;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public void resetTwo() {
        this.lastMS = this.getCurrentMS();
    }


    public long hasTimeLeft(final long MS) {
        return (MS + time) - System.currentTimeMillis();
    }

    public void reset() {
        time = System.currentTimeMillis();
    }
}
