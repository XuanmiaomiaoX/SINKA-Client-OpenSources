package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.RenderWings;
import org.jetbrains.annotations.Nullable;


@ModuleInfo(name = "Wings", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER)
public class Wings extends Module {

    @EventTarget
    public void onRenderPlayer(Render3DEvent event) {
        RenderWings renderWings = new RenderWings();
        renderWings.renderWings(event.getPartialTicks());
    }

    @Nullable
    @Override
    public String getTag() {
        return "Dragon";
    }
}