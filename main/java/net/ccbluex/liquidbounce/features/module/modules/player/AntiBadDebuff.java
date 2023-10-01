package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.potion.Potion;

@ModuleInfo(name = "AntiBadDebuff", description = "Anti the debuff", category = ModuleCategory.PLAYER)
public class AntiBadDebuff extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc.thePlayer.removePotionEffect(Potion.blindness.getId());
        mc.thePlayer.removePotionEffect(Potion.moveSlowdown.getId());
        mc.thePlayer.removePotionEffect(9);
        mc.thePlayer.removePotionEffectClient(Potion.blindness.getId());
        mc.thePlayer.removePotionEffectClient(Potion.moveSlowdown.getId());
        mc.thePlayer.removePotionEffectClient(9);
    }
}
