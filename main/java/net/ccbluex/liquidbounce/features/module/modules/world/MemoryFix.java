package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name = "MemoryFix", description = "MemoryFix", category = ModuleCategory.WORLD)
public class MemoryFix extends Module {

    private final FloatValue delay = new FloatValue("Delay", 120.0F, 10.0F, 600.0F);
    private final FloatValue limit = new FloatValue("Limit", 80.0F, 20.0F, 95.0F);
    public MSTimer timer = new MSTimer();

    @EventTarget
    public void onTick(TickEvent event) {
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        long usedMem = totalMem - freeMem;
        float pct = (float)(usedMem * 100L / maxMem);
        if (timer.hasReached(delay.getValue() * 1000.0D) && limit.getValue() <= (double)pct) {
            Runtime.getRuntime().gc();
            timer.resetTwo();
        }
    }
}