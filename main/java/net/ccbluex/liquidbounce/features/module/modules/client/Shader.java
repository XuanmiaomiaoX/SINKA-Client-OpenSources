package net.ccbluex.liquidbounce.features.module.modules.client;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.event.EventShader;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.blur.KawaseBloom;
import net.ccbluex.liquidbounce.utils.render.blur.KawaseBlur;
import net.ccbluex.liquidbounce.utils.render.blur.Utils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.DoubleValue;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;

@ModuleInfo(name = "Shader", description = "", category = ModuleCategory.CLIENT)
public class Shader extends Module {
    private final BoolValue enable = new BoolValue("Enable", true);
    private final BoolValue blur = new BoolValue("Blur", false);
    private final DoubleValue iterations = new DoubleValue("BlurRadius", 2.0, 1.0, 8.0);
    private final DoubleValue offset = new DoubleValue("BlurOffset", 3.0, 1.0, 10.0);
    private final BoolValue bloom = new BoolValue("Bloom", false);
    private final DoubleValue shadowRadius = new DoubleValue("ShadowRadius", 1.0, 1.0, 8.0);
    private final DoubleValue shadowOffset = new DoubleValue("ShadowOffset", 2.0, 1.0, 10.0);

    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);

    public void blurScreen() {
        if (enable.get()) {
            if (bloom.getValue()) {
                stencilFramebuffer = Utils.createFrameBuffer(stencilFramebuffer);
                stencilFramebuffer.framebufferClear();
                stencilFramebuffer.bindFramebuffer(false);
                RenderHelper.enableGUIStandardItemLighting();
                LiquidBounce.eventManager.callEvent(new EventShader(true, false));
                RenderHelper.disableStandardItemLighting();
                stencilFramebuffer.unbindFramebuffer();
                KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, shadowRadius.getValue().intValue(), shadowOffset.getValue().intValue());
            }
            if (blur.getValue()) {
                stencilFramebuffer = Utils.createFrameBuffer(stencilFramebuffer);
                stencilFramebuffer.framebufferClear();
                stencilFramebuffer.bindFramebuffer(false);
                RenderHelper.enableGUIStandardItemLighting();
                LiquidBounce.eventManager.callEvent(new EventShader(false, true));
                RenderHelper.disableStandardItemLighting();
                stencilFramebuffer.unbindFramebuffer();
                KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, iterations.getValue().intValue(), offset.getValue().intValue());
            }
        }
    }
}
