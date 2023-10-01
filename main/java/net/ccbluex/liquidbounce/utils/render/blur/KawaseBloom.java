package net.ccbluex.liquidbounce.utils.render.blur;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_LINEAR;

public class KawaseBloom{
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static ShaderUtil kawaseDown = new ShaderUtil("kawaseDownBloom");
    public static ShaderUtil kawaseUp = new ShaderUtil("kawaseUpBloom");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, true);
    private static int currentIterations;
    private static final List<Framebuffer> framebufferList = new ArrayList();

    public KawaseBloom() {
    }

    private static void initFramebuffers(float iterations) {
        Iterator var1 = framebufferList.iterator();

        Framebuffer currentBuffer;
        while(var1.hasNext()) {
            currentBuffer = (Framebuffer)var1.next();
            currentBuffer.deleteFramebuffer();
        }

        framebufferList.clear();
        framebufferList.add(KawaseBloom.framebuffer = Utils.createFrameBuffer((Framebuffer)null, true));

        for(int i = 1; (float)i <= iterations; ++i) {
            currentBuffer = new Framebuffer((int)((double)mc.displayWidth / Math.pow(2.0, (double)i)), (int)((double)mc.displayHeight / Math.pow(2.0, (double)i)), true);
            currentBuffer.setFramebufferFilter(9729);
            GlStateManager.bindTexture(currentBuffer.framebufferTexture);
            GL11.glTexParameteri(3553, 10242, 33648);
            GL11.glTexParameteri(3553, 10243, 33648);
            GlStateManager.bindTexture(0);
            framebufferList.add(currentBuffer);
        }

    }

    public static void renderBlur(int framebufferTexture, int iterations, int offset) {
        if (currentIterations != iterations || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            initFramebuffers((float)iterations);
            currentIterations = iterations;
        }

        Utils.setAlphaLimit(0.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(1, 1);
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        renderFBO((Framebuffer)framebufferList.get(1), framebufferTexture, kawaseDown, (float)offset);

        int i;
        for(i = 1; i < iterations; ++i) {
            renderFBO((Framebuffer)framebufferList.get(i + 1), ((Framebuffer)framebufferList.get(i)).framebufferTexture, kawaseDown, (float)offset);
        }

        for(i = iterations; i > 1; --i) {
            renderFBO((Framebuffer)framebufferList.get(i - 1), ((Framebuffer)framebufferList.get(i)).framebufferTexture, kawaseUp, (float)offset);
        }

        Framebuffer lastBuffer = (Framebuffer)framebufferList.get(0);
        lastBuffer.framebufferClear();
        lastBuffer.bindFramebuffer(false);
        kawaseUp.init();
        kawaseUp.setUniformf("offset", new float[]{(float)offset, (float)offset});
        kawaseUp.setUniformi("inTexture", new int[]{0});
        kawaseUp.setUniformi("check", new int[]{1});
        kawaseUp.setUniformi("textureToCheck", new int[]{16});
        kawaseUp.setUniformf("halfpixel", new float[]{1.0F / (float)lastBuffer.framebufferWidth, 1.0F / (float)lastBuffer.framebufferHeight});
        kawaseUp.setUniformf("iResolution", new float[]{(float)lastBuffer.framebufferWidth, (float)lastBuffer.framebufferHeight});
        GlStateManager.setActiveTexture(34000);
        Utils.bindTexture(framebufferTexture);
        GlStateManager.setActiveTexture(33984);
        Utils.bindTexture(((Framebuffer)framebufferList.get(1)).framebufferTexture);
        ShaderUtil.drawQuads();
        kawaseUp.unload();
        GlStateManager.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
        mc.getFramebuffer().bindFramebuffer(false);
        Utils.bindTexture(((Framebuffer)framebufferList.get(0)).framebufferTexture);
        Utils.setAlphaLimit(0.0F);
        GLUtil.startBlend();
        ShaderUtil.drawQuads();
        GlStateManager.bindTexture(0);
        Utils.setAlphaLimit(0.0F);
        GLUtil.startBlend();
    }

    private static void renderFBO(Framebuffer framebuffer, int framebufferTexture, ShaderUtil shader, float offset) {
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(false);
        shader.init();
        Utils.bindTexture(framebufferTexture);
        shader.setUniformf("offset", new float[]{offset, offset});
        shader.setUniformi("inTexture", new int[]{0});
        shader.setUniformi("check", new int[]{0});
        shader.setUniformf("halfpixel", new float[]{1.0F / (float)framebuffer.framebufferWidth, 1.0F / (float)framebuffer.framebufferHeight});
        shader.setUniformf("iResolution", new float[]{(float)framebuffer.framebufferWidth, (float)framebuffer.framebufferHeight});
        ShaderUtil.drawQuads();
        shader.unload();
    }
}
