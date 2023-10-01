package net.ccbluex.liquidbounce.utils.misc;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

/**
 * @author luckhwb
 */
public class SystemUtil {

    private static OperatingSystemMXBean operatingSystemMXBean;

    static {
        operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    /**
     * 获取cpu使用率
     * @return
     */
    public static double getSystemCpuLoad() {
        return operatingSystemMXBean.getSystemCpuLoad();
    }

    /**
     * 获取cpu数量
     * @return
     */
    public static int getSystemCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取cpu使用百分比
     * @return
     */
    public static double getSystemCpuLoadTwo() {
        return (int) (operatingSystemMXBean.getSystemCpuLoad() * 100);
    }
}

