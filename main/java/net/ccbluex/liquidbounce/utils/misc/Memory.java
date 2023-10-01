package net.ccbluex.liquidbounce.utils.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class Memory implements Listenable {
    public static long used,max;

    @Override
    public boolean handleEvents(){
        return true;
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        used = memoryUsage.getUsed()/(1024+1024);
        max = memoryUsage.getMax()/(1024+1024);
    }
    public static float get(){
        return (float) used/(float) max;
    }
}
