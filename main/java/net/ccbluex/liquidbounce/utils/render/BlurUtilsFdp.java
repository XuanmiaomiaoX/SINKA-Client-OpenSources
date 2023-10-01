package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class BlurUtilsFdp extends MinecraftInstance {
    private static ShaderGroup blurShader;

    static {
        try {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), new ResourceLocation("shaders/post/blurAreaFdp.json"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static int lastScale = 0;
    private static int lastScaleWidth = 0;
    private static int lastScaleHeight = 0;

    private static void reInitShader() {
        blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
        final Framebuffer buffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        buffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
    }

    public static void doBlur(final float x, final float y, final float width, final float height, final float radius) {
        final ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3) reInitShader();
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        mc.getFramebuffer().bindFramebuffer(true);
    }
}