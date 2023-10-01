
package net.ccbluex.liquidbounce.utils.render.blur;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KawaseBlur {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static ShaderUtil kawaseDown = new ShaderUtil("kawaseDown");
    public static ShaderUtil kawaseUp = new ShaderUtil("kawaseUp");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);
    private static int currentIterations;
    private static final List<Framebuffer> framebufferList = new ArrayList();

    public static void setupUniforms(float offset) {
        KawaseBlur.kawaseDown.setUniformf("offset", offset, offset);
        KawaseBlur.kawaseUp.setUniformf("offset", offset, offset);
    }

    private static void initFramebuffers(float iterations) {
        Iterator iterator = KawaseBlur.framebufferList.iterator();

        Framebuffer currentBuffer;

        while (iterator.hasNext()) {
            currentBuffer = (Framebuffer) iterator.next();
            currentBuffer.deleteFramebuffer();
        }

        KawaseBlur.framebufferList.clear();
        KawaseBlur.framebufferList.add(KawaseBlur.framebuffer = Utils.createFrameBuffer((Framebuffer) null));

        for (int i = 1; (float) i <= iterations; ++i) {
            currentBuffer = new Framebuffer((int) ((double) mc.displayWidth / Math.pow(2.0D, (double) i)), (int) ((double) mc.displayHeight / Math.pow(2.0D, (double) i)), false);
            currentBuffer.setFramebufferFilter(9729);
            GlStateManager.bindTexture(currentBuffer.framebufferTexture);
            GL11.glTexParameteri(3553, 10242, 33648);
            GL11.glTexParameteri(3553, 10243, 33648);
            GlStateManager.bindTexture(0);
            KawaseBlur.framebufferList.add(currentBuffer);
        }

    }

    public static void renderBlur(int stencilFrameBufferTexture, int iterations, int offset) {
        if (KawaseBlur.currentIterations != iterations || KawaseBlur.framebuffer.framebufferWidth != mc.displayWidth || KawaseBlur.framebuffer.framebufferHeight != mc.displayHeight) {
            initFramebuffers((float) iterations);
            KawaseBlur.currentIterations = iterations;
        }

        renderFBO((Framebuffer) KawaseBlur.framebufferList.get(1), mc.getFramebuffer().framebufferTexture, KawaseBlur.kawaseDown, (float) offset);

        int i;

        for (i = 1; i < iterations; ++i) {
            renderFBO((Framebuffer) KawaseBlur.framebufferList.get(i + 1), ((Framebuffer) KawaseBlur.framebufferList.get(i)).framebufferTexture, KawaseBlur.kawaseDown, (float) offset);
        }

        for (i = iterations; i > 1; --i) {
            renderFBO((Framebuffer) KawaseBlur.framebufferList.get(i - 1), ((Framebuffer) KawaseBlur.framebufferList.get(i)).framebufferTexture, KawaseBlur.kawaseUp, (float) offset);
        }

        Framebuffer lastBuffer = (Framebuffer) KawaseBlur.framebufferList.get(0);

        lastBuffer.framebufferClear();
        lastBuffer.bindFramebuffer(false);
        KawaseBlur.kawaseUp.init();
        KawaseBlur.kawaseUp.setUniformf("offset", (float) offset, (float) offset);
        KawaseBlur.kawaseUp.setUniformi("inTexture", 0);
        KawaseBlur.kawaseUp.setUniformi("check", 1);
        KawaseBlur.kawaseUp.setUniformi("textureToCheck", 16);
        KawaseBlur.kawaseUp.setUniformf("halfpixel", 1.0F / (float) lastBuffer.framebufferWidth, 1.0F / (float) lastBuffer.framebufferHeight);
        KawaseBlur.kawaseUp.setUniformf("iResolution", (float) lastBuffer.framebufferWidth, (float) lastBuffer.framebufferHeight);
        GL13.glActiveTexture(34000);
        Utils.bindTexture(stencilFrameBufferTexture);
        GL13.glActiveTexture(33984);
        Utils.bindTexture(((Framebuffer) KawaseBlur.framebufferList.get(1)).framebufferTexture);
        ShaderUtil.drawQuads();
        KawaseBlur.kawaseUp.unload();
        mc.getFramebuffer().bindFramebuffer(true);
        Utils.bindTexture(((Framebuffer) KawaseBlur.framebufferList.get(0)).framebufferTexture);
        Utils.setAlphaLimit(0.0F);
        GLUtil.startBlend();
        ShaderUtil.drawQuads();
        GlStateManager.bindTexture(0);
    }

    private static void renderFBO(Framebuffer framebuffer, int framebufferTexture, ShaderUtil shader, float offset) {
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(false);
        shader.init();
        Utils.bindTexture(framebufferTexture);
        shader.setUniformf("offset", offset, offset);
        shader.setUniformi("inTexture", 0);
        shader.setUniformi("check", 0);
        shader.setUniformf("halfpixel", 1.0F / (float) framebuffer.framebufferWidth, 1.0F / (float) framebuffer.framebufferHeight);
        shader.setUniformf("iResolution", (float) framebuffer.framebufferWidth, (float) framebuffer.framebufferHeight);
        ShaderUtil.drawQuads();
        shader.unload();
    }
}
