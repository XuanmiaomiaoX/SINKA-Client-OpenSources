/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils.timer;

import net.ccbluex.liquidbounce.utils.misc.RandomUtils;

public final class TimeUtils {

    public static long lastMs;

    public static long randomDelay(final int minDelay, final int maxDelay) {
        return RandomUtils.nextInt(minDelay, maxDelay);
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(final long milliseconds) {
        return getCurrentMS() - lastMs >= milliseconds;
    }

    public void reset() {
        lastMs = getCurrentMS();
    }

    public static boolean isDelayComplete(Double delay) {
        return System.currentTimeMillis() - lastMs > delay;
    }

    public static long randomClickDelay(final int minCPS, final int maxCPS) {
        return (long) ((Math.random() * (1000 / minCPS - 1000 / maxCPS + 1)) + 1000 / maxCPS);
    }
}