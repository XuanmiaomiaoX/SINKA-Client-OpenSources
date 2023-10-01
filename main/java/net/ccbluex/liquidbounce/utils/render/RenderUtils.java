/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils.render;

import me.HXC.Utils.RenderUtils2;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.particles.Particle;
import net.ccbluex.liquidbounce.utils.particles.Vec3;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;
import static net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public final class RenderUtils extends MinecraftInstance {
    public static float delta;

    private static final Map<Integer, Boolean> glCapMap = new HashMap<>();

    public static int deltaTime;

    public static void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(skin);
        RenderUtils.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,
                64F, 64F);
        RenderUtils.drawScaledCustomSizeModalRect(x, y, 40F, 8F, 8, 8, width, height,
                64F, 64F);
    }
    public static int loadGlTexture(BufferedImage bufferedImage){
        int textureId = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, bufferedImage.getWidth(), bufferedImage.getHeight(),
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, ImageUtils.readImageToBuffer(bufferedImage));

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        return textureId;
    }
    private static void quickPolygonCircle(float x, float y, float xRadius, float yRadius, int start, int end, int split) {
        for(int i = end; i >= start; i-=split) {
            glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * xRadius, y + Math.cos(i * Math.PI / 180.0D) * yRadius);
        }
    }
    public static void drawRoundedCornerRect(float x, float y, float x1, float y1, float radius, int color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        glColor(color);
        drawRoundedCornerRect(x, y, x1, y1, radius);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }

    public static void drawRoundedCornerRect(float x, float y, float x1, float y1, float radius) {
        glBegin(GL_POLYGON);

        float xRadius = (float) Math.min((x1 - x) * 0.5, radius);
        float yRadius = (float) Math.min((y1 - y) * 0.5, radius);
        quickPolygonCircle(x + xRadius,y + yRadius, xRadius, yRadius,180,270,4);
        quickPolygonCircle(x1 - xRadius,y + yRadius, xRadius, yRadius,90,180,4);
        quickPolygonCircle(x1 - xRadius,y1 - yRadius, xRadius, yRadius,0,90,4);
        quickPolygonCircle(x + xRadius,y1 - yRadius, xRadius, yRadius,270,360,4);

        glEnd();
    }
    public static void drawRoundRect(float x0, float y0, float x1, float y1,float radius,int color) {
        if(x0 == x1 || y0 == y1) return;
        final int Semicircle = 18;
        final float f = 90.0f / Semicircle;
        final float f2 = (color >> 24 & 0xFF) / 255.0f;
        final float f3 = (color >> 16 & 0xFF) / 255.0f;
        final float f4 = (color >> 8 & 0xFF) / 255.0f;
        final float f5 = (color & 0xFF) / 255.0f;
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(5);
        GL11.glVertex2f(x0 + radius, y0);
        GL11.glVertex2f(x0 + radius, y1);
        GL11.glVertex2f(x1 - radius, y0);
        GL11.glVertex2f(x1 - radius, y1);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x0, y0 + radius);
        GL11.glVertex2f(x0 + radius, y0 + radius);
        GL11.glVertex2f(x0, y1 - radius);
        GL11.glVertex2f(x0 + radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x1, y0 + radius);
        GL11.glVertex2f(x1 - radius, y0 + radius);
        GL11.glVertex2f(x1, y1 - radius);
        GL11.glVertex2f(x1 - radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(6);
        float f6 = x1 - radius;
        float f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);
        int j = 0;
        for (j = 0; j <= Semicircle; ++j) {
            final float f8 = j * f;
            GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f8))),
                    (float) (f7 - radius * Math.sin(Math.toRadians(f8))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f9 = j * f;
            GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f9))),
                    (float) (f7 - radius * Math.sin(Math.toRadians(f9))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f10 = j * f;
            GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f10))),
                    (float) (f7 + radius * Math.sin(Math.toRadians(f10))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f11 = j * f;
            GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f11))),
                    (float) (f7 + radius * Math.sin(Math.toRadians(f11))));
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    public static void drawRoundRect2(float x0, float y0, float x1, float y1,float radius,int color2) {
        if(x0 == x1 || y0 == y1) return;
        final int Semicircle = 18;
        final float f = 90.0f / Semicircle;
        final float f2 = (color2 >> 24 & 0xFF) / 255.0f;
        final float f3 = (color2 >> 16 & 0xFF) / 255.0f;
        final float f4 = (color2 >> 8 & 0xFF) / 255.0f;
        final float f5 = (color2 & 0xFF) / 255.0f;
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(5);
        GL11.glVertex2f(x0 + radius, y0);
        GL11.glVertex2f(x0 + radius, y1);
        GL11.glVertex2f(x1 - radius, y0);
        GL11.glVertex2f(x1 - radius, y1);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x0, y0 + radius);
        GL11.glVertex2f(x0 + radius, y0 + radius);
        GL11.glVertex2f(x0, y1 - radius);
        GL11.glVertex2f(x0 + radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x1, y0 + radius);
        GL11.glVertex2f(x1 - radius, y0 + radius);
        GL11.glVertex2f(x1, y1 - radius);
        GL11.glVertex2f(x1 - radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(6);
        float f6 = x1 - radius;
        float f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);
        int j = 0;
        for (j = 0; j <= Semicircle; ++j) {
            final float f8 = j * f;
            GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f8))),
                    (float) (f7 - radius * Math.sin(Math.toRadians(f8))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f9 = j * f;
            GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f9))),
                    (float) (f7 - radius * Math.sin(Math.toRadians(f9))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f10 = j * f;
            GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f10))),
                    (float) (f7 + radius * Math.sin(Math.toRadians(f10))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f11 = j * f;
            GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f11))),
                    (float) (f7 + radius * Math.sin(Math.toRadians(f11))));
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    public static int Astolfo(int var2, float st, float bright) {
        double currentColor = Math.ceil(System.currentTimeMillis() + (long) (var2 * 130)) / 6;
        return Color.getHSBColor((double) ((float) ((currentColor %= 360.0) / 360.0)) < 0.5 ? -((float) (currentColor / 360.0)) : (float) (currentColor / 360.0), st, bright).getRGB();
    }

    public static void drawTriangle(float x,float y,float x2,float y2,float x3,float y3,Color color) {
        glEnable(GL_LINE_SMOOTH);
        glColor(color);
        glBegin(GL_TRIANGLES);
        glVertex2f(x,y);
        glVertex2f(x2, y2);
        glVertex2f(x3, y3);
        glEnd();
        glDisable(GL_LINE_SMOOTH);
    }

    public static void glColor114514(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255F;
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void drawTriAngle(float cx,float cy,float r,float n,int color) {
        GL11.glPushMatrix();
        cx *= 2.0;
        cy *= 2.0;
        double b = Math.PI * 2 / n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        double x = r * 2.0;
        double y = 0.0;
        enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GlStateManager.color(0,0,0);
        GlStateManager.resetColor();
        glColor114514(color);
        GL11.glBegin(2);
        int ii = 0;
        while (ii < n) {
            GL11.glVertex2d(x + cx, y + cy);
            double t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ii++;
        }
        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPopMatrix();
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static ScaledResolution getResolution() {
        return new ScaledResolution(mc);
    }

    public static void MoondrawRect(double x, double y, double width, double height, int color) {
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glColor4f(f1, f2, f3, f);
        MoondrawRect2(x, y, x + width, y + height, color);
    }

    private static final IntBuffer viewport = BufferUtils.createIntBuffer(16);
    private final static FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final static FloatBuffer projections = GLAllocation.createDirectFloatBuffer(16);

    public static javax.vecmath.Vector3d project(double x, double y, double z) {
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projections);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, modelview, projections, viewport, vector)) {
            return new Vector3d(vector.get(0) / getResolution().getScaleFactor(), (Display.getHeight() - vector.get(1)) / getResolution().getScaleFactor(), vector.get(2));
        }
        return null;
    }

    public static void MoondrawRect2(double left, double top, double right, double bottom, int color) {
        if (left < right)
        {
            int i = (int)left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = (int)top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
        drawRoundedRect(paramXStart, paramYStart, paramXEnd, paramYEnd, radius, color, true);
    }

    public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color, boolean popPush) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        float z = 0;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }

        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }

        double x1 = (double)(paramXStart + radius);
        double y1 = (double)(paramYStart + radius);
        double x2 = (double)(paramXEnd - radius);
        double y2 = (double)(paramYEnd - radius);

        if (popPush) glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);

        glColor4f(red, green, blue, alpha);
        glBegin(GL_POLYGON);

        double degree = Math.PI / 180;
        for (double i = 0; i <= 90; i += 0.5)
            glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        for (double i = 90; i <= 180; i += 0.5)
            glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        for (double i = 180; i <= 270; i += 0.5)
            glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        for (double i = 270; i <= 360; i += 0.5)
            glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        if (popPush) glPopMatrix();
    }

    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
    }

    public static void drawExhiRect(float x, float y, float x2, float y2) {
        drawRect(x - 3.5F, y - 3.5F, x2 + 3.5F, y2 + 3.5F, Color.black.getRGB());
        drawRect(x - 3F, y - 3F, x2 + 3F, y2 + 3F, new Color(50, 50, 50).getRGB());
        //drawBorder(x - 1.5F, y - 1.5F, x2 + 1.5F, y2 + 1.5F, 2.5F, new Color(26, 26, 26).getRGB());
        drawRect(x - 2.5F, y - 2.5F, x2 + 2.5F, y2 + 2.5F, new Color(26, 26, 26).getRGB());
        drawRect(x - 0.5F, y - 0.5F, x2 + 0.5F, y2 + 0.5F, new Color(50, 50, 50).getRGB());
        drawRect(x, y, x2, y2, new Color(18, 18, 18).getRGB());
    }

    private static final Frustum frustrum = new Frustum();

    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void fastRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius) {
        float z = 0;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }

        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }

        double x1 = (double)(paramXStart + radius);
        double y1 = (double)(paramYStart + radius);
        double x2 = (double)(paramXEnd - radius);
        double y2 = (double)(paramYEnd - radius);

        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);

        glBegin(GL_POLYGON);

        double degree = Math.PI / 180;
        for (double i = 0; i <= 90; i += 0.25)
            glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        for (double i = 90; i <= 180; i += 0.25)
            glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        for (double i = 180; i <= 270; i += 0.25)
            glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        for (double i = 270; i <= 360; i += 0.25)
            glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        glEnd();
        glDisable(GL_LINE_SMOOTH);
    }

    public static void drawShadow(int x, int y, int width, int height) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawTexturedRect(x - 9, y - 9, 9, 9, "paneltopleft", sr);
        drawTexturedRect(x - 9, y + height, 9, 9, "panelbottomleft", sr);
        drawTexturedRect(x + width, y + height, 9, 9, "panelbottomright", sr);
        drawTexturedRect(x + width, y - 9, 9, 9, "paneltopright", sr);
        drawTexturedRect(x - 9, y, 9, height, "panelleft", sr);
        drawTexturedRect(x + width, y, 9, height, "panelright", sr);
        drawTexturedRect(x, y - 9, width, 9, "paneltop", sr);
        drawTexturedRect(x, y + height, width, 9, "panelbottom", sr);
    }

    public static void HWIDYES() {
        LiquidBounce.INSTANCE.setHWID(true);
    }

    public static void drawTexturedRect(int x, int y, int width, int height, String image, ScaledResolution sr) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(new ResourceLocation("liquidbounce/cool/potionrender/" + image + ".png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }

    public static int getRainbowOpaque(int get, float get1, float get2, int i) {
        return 0;
    }

    public static int SkyRainbow2(int var2, float st, float bright) {
        double v1 = Math.ceil(System.currentTimeMillis() + (long) (var2 * 109)) / 5;
        return Color.getHSBColor((double) ((float) ((v1 %= 360.0) / 360.0)) < 0.5 ? -((float) (v1 / 360.0)) : (float) (v1 / 360.0), st, bright).getRGB();
    }

    public static Color skyRainbow(int var2, float st, float bright) {
        double v1 = Math.ceil(System.currentTimeMillis() + (long) (var2 * 109)) / 5;
        return Color.getHSBColor((double) ((float) ((v1 %= 360.0) / 360.0)) < 0.5 ? -((float) (v1 / 360.0)) : (float) (v1 / 360.0), st, bright);
    }

    public static int SkyRainbow(int i, float get, float get1) {
        return 0;
    }


    public static void drawRoundedRect3(float x, float y, float x2, float y2, final float round, final int color,final int mode) {
        final float rectX = x,rectY = y, rectX2 = x2, rectY2 = y2;
        x += (float) (round / 2.0f + 0.5);
        y += (float) (round / 2.0f + 0.5);
        x2 -= (float) (round / 2.0f + 0.5);
        y2 -= (float) (round / 2.0f + 0.5);
        if(mode == 1)
            drawRect(x , rectY, rectX2, rectY2, color);
        else
            drawRect(rectX, rectY, x2, rectY2, color);
        circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        circle(x + round / 2.0f, y + round / 2.0f, round, color);
        circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        drawRect((int) (x - round / 2.0f - 0.5f), (int) (y + round / 2.0f), (int) x2, (int) (y2 - round / 2.0f),
                color);
        drawRect((int) x, (int) (y + round / 2.0f), (int) (x2 + round / 2.0f + 0.5f), (int) (y2 - round / 2.0f),
                color);
        drawRect((int) (x + round / 2.0f), (int) (y - round / 2.0f - 0.5f), (int) (x2 - round / 2.0f),
                (int) (y2 - round / 2.0f), color);
        drawRect((int) (x + round / 2.0f), (int) y, (int) (x2 - round / 2.0f), (int) (y2 + round / 2.0f + 0.5f),
                color);
    }
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
    }
    public static void renderParticles(final List<Particle> particles) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int i = 0;
        try {
            for (final Particle particle : particles) {
                i++;
                final Vec3 v = particle.position;
                boolean draw = true;

                final double x = v.xCoord - (mc.getRenderManager()).renderPosX;
                final double y = v.yCoord - (mc.getRenderManager()).renderPosY;
                final double z = v.zCoord - (mc.getRenderManager()).renderPosZ;

                final double distanceFromPlayer = mc.thePlayer.getDistance(v.xCoord, v.yCoord - 1, v.zCoord);
                int quality = (int) (distanceFromPlayer * 4 + 10);

                if (quality > 350)
                    quality = 350;

                if (!isInViewFrustrum(new EntityEgg(mc.theWorld, v.xCoord, v.yCoord, v.zCoord)))
                    draw = false;

                if (i % 10 != 0 && distanceFromPlayer > 25)
                    draw = false;

                if (i % 3 == 0 && distanceFromPlayer > 15)
                    draw = false;

                if (draw) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);

                    final float scale = 0.04F;
                    GL11.glScalef(-scale, -scale, -scale);

                    GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                    GL11.glRotated(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0D : 1.0D, 0.0D, 0.0D);
                    final Color c = RenderUtils2.getGradientOffset(new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(),1),new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),1), ((double) Math.abs(
                            System.currentTimeMillis() / 100 + (20 / 10)
                    ) / 10));
                    drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);

                    if (distanceFromPlayer < 4)
                        drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);

                    if (distanceFromPlayer < 20)
                        drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);

                    GL11.glScalef(0.8F, 0.8F, 0.8F);
                    GL11.glPopMatrix();
                }
            }
        } catch (final ConcurrentModificationException ignored) {
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glColor3d(255, 255, 255);
    }
    public static void drawFastRoundedRect(final float x0, final float y0, final float x1, final float y1,
                                           final float radius, final int color) {
        final int Semicircle = 18;
        final float f = 90.0f / Semicircle;
        final float f2 = (color >> 24 & 0xFF) / 255.0f;
        final float f3 = (color >> 16 & 0xFF) / 255.0f;
        final float f4 = (color >> 8 & 0xFF) / 255.0f;
        final float f5 = (color & 0xFF) / 255.0f;
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GlStateManager.enableBlend();
        //GL11.glBlendFunc(770, 771);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(5);
        GL11.glVertex2f(x0 + radius, y0);
        GL11.glVertex2f(x0 + radius, y1);
        GL11.glVertex2f(x1 - radius, y0);
        GL11.glVertex2f(x1 - radius, y1);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x0, y0 + radius);
        GL11.glVertex2f(x0 + radius, y0 + radius);
        GL11.glVertex2f(x0, y1 - radius);
        GL11.glVertex2f(x0 + radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x1, y0 + radius);
        GL11.glVertex2f(x1 - radius, y0 + radius);
        GL11.glVertex2f(x1, y1 - radius);
        GL11.glVertex2f(x1 - radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(6);
        float f6 = x1 - radius;
        float f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);
        int j;
        for (j = 0; j <= Semicircle; ++j) {
            final float f8 = j * f;
            GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f8))),
                    (float) (f7 - radius * Math.sin(Math.toRadians(f8))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f9 = j * f;
            GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f9))),
                    (float) (f7 - radius * Math.sin(Math.toRadians(f9))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f10 = j * f;
            GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f10))),
                    (float) (f7 + radius * Math.sin(Math.toRadians(f10))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= Semicircle; ++j) {
            final float f11 = j * f;
            GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f11))),
                    (float) (f7 + radius * Math.sin(Math.toRadians(f11))));
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawFullCircle(final float x, final float y,final float radius,
                                      final int color,final int outSideColor) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float outSideAlpha = (outSideColor >> 24 & 0xFF) / 255.0f;
        final float outSideRed = (outSideColor >> 16 & 0xFF) / 255.0f;
        final float outSideGreen = (outSideColor >> 8 & 0xFF) / 255.0f;
        final float outSideBlue = (outSideColor & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(outSideRed, outSideGreen, outSideBlue, outSideAlpha);
        if (alpha > 0.5f) {
            GL11.glEnable(2881);
            GL11.glEnable(2848);
//            GL11.glLineWidth(2.5F);
            enableSmoothLine(2F);

            GL11.glBlendFunc(770,  771);
            //GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for(int i = 0; i <= 360; ++i) {
                GL11.glVertex2d(
                        ( x + Math.sin( ((double) i * 3.141526 / 180.0)) * radius),
                        ( y + Math.cos( ((double) i * 3.141526 / 180.0)) * radius));
            }
            GL11.glEnd();
            GL11.glDisable(2848);
            GL11.glDisable(2881);
            GlStateManager.resetColor();
        }
        GlStateManager.color(red, green, blue, alpha);
        GL11.glBegin(6);
        for(int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(
                    (x + Math.sin( ((double) i * 3.141526 / 180.0)) * radius),
                    (y + Math.cos( ((double) i * 3.141526 / 180.0)) * radius));
        }
        GL11.glEnd();
        disableSmoothLine();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void startGlScissor(int x, int y, int width, int height) {
        int scaleFactor = new ScaledResolution(mc).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        GL11.glScissor((x * scaleFactor), (mc.displayHeight - (y + height) * scaleFactor),
                (width * scaleFactor), ((height += 14) * scaleFactor));
    }

    public static void arcIiiilllIIiii(final float x, final float y, final float start, final float end, final float radius, final int color, final float lineWidth) {
        illlIIIIiii(x, y, start, end, radius, radius, color, lineWidth);
    }

    public static void drawOutFullCircle(final float x, final float y, final float radius, final int fill, final float lineWidth) {
        arcIiiilllIIiii(x, y, 0.0f, 360.0f, radius, fill, lineWidth);
    }

    public static void drawOutFullCircle(final float x, final float y, final float radius, final int fill, final float lineWidth, final float start, final float end) {
        arcIiiilllIIiii(x, y, start, end, radius, fill, lineWidth);
    }


    public static void enableSmoothLine(float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }

    public static double getAnimationState(double animation, final double finalState, final double speed) {
        final float add = (float) (RenderUtils.delta * speed);
        if (animation < finalState) {
            if (animation + add < finalState) {
                animation += add;
            }
            else {
                animation = finalState;
            }
        }
        else if (animation - add > finalState) {
            animation -= add;
        }
        else {
            animation = finalState;
        }
        return animation;
    }


    public static double getAnimationState2(double animation, double finalState, double speed) {
        float add = (float) (0.01 * speed);
        if (animation < finalState) {
            if (animation + add < finalState)
                animation += add;
            else
                animation = finalState;
        } else {
            if (animation - add > finalState)
                animation -= add;
            else
                animation = finalState;
        }
        return animation;
    }


    public static void drawScaledCustomSizeModalCircle(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        float xRadius = width / 2f;
        float yRadius = height / 2f;
        float uRadius = (((u + (float) uWidth) * f) - (u * f)) / 2f;
        float vRadius = (((v + (float) vHeight) * f1) - (v * f1)) / 2f;
        for(int i = 0; i <= 360; i+=10) {
            double xPosOffset = Math.sin(i * Math.PI / 180.0D);
            double yPosOffset = Math.cos(i * Math.PI / 180.0D);
            worldrenderer.pos(x + xRadius + xPosOffset * xRadius, y + yRadius + yPosOffset * yRadius, 0)
                    .tex(u * f + uRadius + xPosOffset * uRadius, v * f1 + vRadius + yPosOffset * vRadius).endVertex();
        }
        tessellator.draw();
    }

    public static void drawLimitedCircle(final float lx, final float ly, final float x2, final float y2,final int xx, final int yy, final float radius, final Color color) {
        int sections = 50;
        double dAngle = 2 * Math.PI / sections;
        float x, y;

        glPushAttrib(GL_ENABLE_BIT);

        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i < sections; i++) {
            x = (float) (radius * Math.sin((i * dAngle)));
            y = (float) (radius * Math.cos((i * dAngle)));

            glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
            glVertex2f(Math.min(x2,Math.max(xx + x,lx)), Math.min(y2,Math.max(yy + y,ly)));
        }

        GlStateManager.color(0, 0, 0);

        glEnd();

        glPopAttrib();
    }

    public static void illlIIIIiii(final float x, final float y, float start, float end, final float w, final float h, final int color, final float lineWidth) {
        if (start > end) {
            final float temp = end;
            end = start;
            start = temp;
        }
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.color(red, green, blue, alpha);
        GL11.glEnable(2881);
        GL11.glEnable(2848);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        for (float i = end; i >= start; i -= 4.0f) {
            GL11.glVertex2d(x + Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001, y + Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }



    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + (float) vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + (float) uWidth) * f, (v + (float) vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + (float) uWidth) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    public static void drawBorderRect(float x, float y, float x2, float y2, float round, int color) {
        drawRect((x += ((round / 2.0F) + 0.5F)),
                (y += ((round / 2.0f) + 0.5F)),
                (x2 -= ((round / 2.0f) + 0.5F)),
                (y2 -=  ((round / 2.0f) + 0.5F)), color);
        circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        circle(x + round / 2.0f, y2 - round / 2.0f - 0.2f, round, color);
        circle(x + round / 2.0f, y + round / 2.0f, round, color);
        circle(x2 - round / 2.0f, y2 - round / 2.0f - 0.2f, round, color);
        drawRect((x - round / 2.0f - 0.5f), (y + round / 2.0f), x2, (y2 - round / 2.0f),
                color);
        drawRect( x,  (y + round / 2.0f), (x2 + round / 2.0f + 0.5f), (y2 - round / 2.0f),
                color);
        drawRect((x + round / 2.0f), (y - round / 2.0f - 0.5f), (x2 - round / 2.0f),
                (y2 - round / 2.0f), color);
        drawRect((x + round / 2.0f), y, (x2 - round / 2.0f),(y2 + round / 2.0f + 0.5f),
                color);

    }

    public static void drawRoundedRect2(float x, float y, float x2, float y2, final float round, final int color) {
        x += (float) (round / 2.0f + 0.5);
        y += (float) (round / 2.0f + 0.5);
        x2 -= (float) (round / 2.0f + 0.5);
        y2 -= (float) (round / 2.0f + 0.5);
        drawRect((int) x, (int) y, (int) x2, (int) y2, color);
        circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        circle(x + round / 2.0f, y + round / 2.0f, round, color);
        circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        drawRect((int) (x - round / 2.0f - 0.5f), (int) (y + round / 2.0f), (int) x2, (int) (y2 - round / 2.0f),
                color);
        drawRect((int) x, (int) (y + round / 2.0f), (int) (x2 + round / 2.0f + 0.5f), (int) (y2 - round / 2.0f),
                color);
        drawRect((int) (x + round / 2.0f), (int) (y - round / 2.0f - 0.5f), (int) (x2 - round / 2.0f),
                (int) (y2 - round / 2.0f), color);
        drawRect((int) (x + round / 2.0f), (int) y, (int) (x2 - round / 2.0f), (int) (y2 + round / 2.0f + 0.5f),
                color);
    }



    public static void circle(final float x, final float y, final float radius, final int fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void circle(final float x, final float y, final float radius, final Color fill) {
        arc3(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc3(final float x, final float y, final float start, final float end, final float radius,
                            final Color color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void stopGlScissor() {
        GL11.glDisable(3089);
        GL11.glPopMatrix();
    }

    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h,
                                  final Color color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final Tessellator var9 = Tessellator.getInstance();
        var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
                color.getAlpha() / 255.0f);
        if (color.getAlpha() > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float) Math.cos(i * Math.PI / 180.0) * w * 1.001f;
                final float ldy = (float) Math.sin(i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float) Math.cos(i * Math.PI / 180.0) * w;
            final float ldy = (float) Math.sin(i * Math.PI / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void circle2(final float x, final float y, final float radius, final int color) {
        arc(x,y,180.0F,360.0F,radius,color);
    }

    public static void arc(final float x, final float y, final float start, final float end, final float radius,
                           final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc2(final float x, final float y, final float start, final float end, final float radius,
                            final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void drawNoFullCircle(final float x, final float y, final float radius, final int fill) {
        arc233(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc233(final float x, final float y, final float start, final float end, final float radius,
                              final int color) {
        arcEllipse233(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse233(final float x, final float y, float start, float end, final float w, final float h,
                                     final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        if (alpha > 0.5f) {
            GL11.glEnable(GL_POLYGON_SMOOTH);
            GL11.glEnable(2848);
            GL11.glLineWidth(1.5F);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float) Math.cos(i * Math.PI / 180.0) * w * 1.001f;
                final float ldy = (float) Math.sin(i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
            GL11.glDisable(GL_POLYGON_SMOOTH);
        }
//        GL11.glBegin(6);
//        for (float i = end; i >= start; i -= 4.0F) {
//            final float ldx = (float) Math.cos(i * Math.PI / 180.0) * w;
//            final float ldy = (float) Math.sin(i * Math.PI / 180.0) * h;
//            GL11.glVertex2f(x + ldx, y + ldy);
//        }
//        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h,
                                  final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        if (alpha > 0.5f) {
            GL11.glEnable(GL_POLYGON_SMOOTH);
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float) Math.cos(i * Math.PI / 180.0) * w * 1.001f;
                final float ldy = (float) Math.sin(i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
            GL11.glDisable(GL_POLYGON_SMOOTH);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0F) {
            final float ldx = (float) Math.cos(i * Math.PI / 180.0) * w;
            final float ldy = (float) Math.sin(i * Math.PI / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlinedString(String str, int x, int y, int color,int color2) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRendererObj.drawString(str, (int) (x - 1.0F), y, color2);
        mc.fontRendererObj.drawString(str, (int) (x + 1.0F), y,color2);
        mc.fontRendererObj.drawString(str, x, (int) (y + 1.0F), color2);
        mc.fontRendererObj.drawString(str, x, (int) (y -1.0F),color2);
        mc.fontRendererObj.drawString(str, x, y, color);
    }

    public static void HanaBidrawRect(float x1, float y1, float x2, float y2, int color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        color(color);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
        Gui.drawRect(0,0,0,0,0);
    }

    public static void color(int color) {
        float f = (float)(color >> 24 & 255) / 255.0f;
        float f1 = (float)(color >> 16 & 255) / 255.0f;
        float f2 = (float)(color >> 8 & 255) / 255.0f;
        float f3 = (float)(color & 255) / 255.0f;
        GL11.glColor4f(f1, f2, f3, f);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 255) / 255.0F;
        float f1 = (float)(col1 >> 16 & 255) / 255.0F;
        float f2 = (float)(col1 >> 8 & 255) / 255.0F;
        float f3 = (float)(col1 & 255) / 255.0F;
        float f4 = (float)(col2 >> 24 & 255) / 255.0F;
        float f5 = (float)(col2 >> 16 & 255) / 255.0F;
        float f6 = (float)(col2 >> 8 & 255) / 255.0F;
        float f7 = (float)(col2 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void quickDrawHead(ResourceLocation skin, int x, int y, int width, int height) {
        mc.getTextureManager().bindTexture(skin);
        RenderUtils.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, width, height,
                64F, 64F);
        RenderUtils.drawScaledCustomSizeModalRect(x, y, 40F, 8F, 8, 8, width, height,
                64F, 64F);
    }

    public static void drawBlockBox2(final BlockPos blockPos, final Color color, final boolean outline, final boolean box, final float outlineWidth) {
        final RenderManager renderManager = mc.getRenderManager();
        final Timer timer = mc.timer;

        final double x = blockPos.getX() - renderManager.renderPosX;
        final double y = blockPos.getY() - renderManager.renderPosY;
        final double z = blockPos.getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = BlockUtils.getBlock(blockPos);

        if (block != null) {
            final EntityPlayer player = mc.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, blockPos)
                    .expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
                    .offset(-posX, -posY, -posZ);
        }

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        if(box) {
            glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : outline ? 26 : 35);
            drawFilledBox(axisAlignedBB);
        }

        if (outline) {
            glLineWidth(outlineWidth);
            enableGlCap(GL_LINE_SMOOTH);
            glColor(color);

            drawSelectionBoundingBox(axisAlignedBB);
        }

        GlStateManager.resetColor();
        glDepthMask(true);
        resetCaps();
    }

    public static void drawRect2(final double x, final double y, final double x2, final double y2, final int color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        glColor(color);
        glBegin(GL_QUADS);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }

    public static void drawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0f;
        float f1 = (float) (col1 >> 16 & 255) / 255.0f;
        float f2 = (float) (col1 >> 8 & 255) / 255.0f;
        float f3 = (float) (col1 & 255) / 255.0f;
        float f4 = (float) (col2 >> 24 & 255) / 255.0f;
        float f5 = (float) (col2 >> 16 & 255) / 255.0f;
        float f6 = (float) (col2 >> 8 & 255) / 255.0f;
        float f7 = (float) (col2 & 255) / 255.0f;
        GL11.glEnable((int) 3042);
        GL11.glDisable((int) 3553);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glEnable((int) 2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f((float) f1, (float) f2, (float) f3, (float) f);
        GL11.glVertex2d((double) left, (double) bottom);
        GL11.glVertex2d((double) right, (double) bottom);
        GL11.glColor4f((float) f5, (float) f6, (float) f7, (float) f4);
        GL11.glVertex2d((double) right, (double) top);
        GL11.glVertex2d((double) left, (double) top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        GL11.glDisable((int) 2848);
        GL11.glShadeModel((int) 7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }

    public static void quickDrawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        glBegin(7);
        glColor(col1);
        glVertex2d(left, top);
        glVertex2d(left, bottom);

        glColor(col2);
        glVertex2d(right, bottom);
        glVertex2d(right, top);
        glEnd();
    }

    public static void drawBlockBox(final BlockPos blockPos, final Color color, final boolean outline) {
        final RenderManager renderManager = mc.getRenderManager();
        final Timer timer = mc.timer;

        final double x = blockPos.getX() - renderManager.renderPosX;
        final double y = blockPos.getY() - renderManager.renderPosY;
        final double z = blockPos.getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = BlockUtils.getBlock(blockPos);

        if (block != null) {
            final EntityPlayer player = mc.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, blockPos)
                    .expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
                    .offset(-posX, -posY, -posZ);
        }

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : outline ? 26 : 35);
        drawFilledBox(axisAlignedBB);

        if (outline) {
            glLineWidth(1F);
            enableGlCap(GL_LINE_SMOOTH);
            glColor(color);
            drawSelectionBoundingBox(axisAlignedBB);
        }

        GlStateManager.resetColor();
        glDepthMask(true);
        resetCaps();
    }

    public static void quickRenderCircle(double x, double y, double start, double end, double w, double h) {
        if (start > end) {
            double temp = end;
            end = start;
            start = temp;
        }

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(x, y);
        for(double i = end; i >= start; i-=4) {
            double ldx = Math.cos(i * Math.PI / 180.0) * w;
            double ldy = Math.sin(i * Math.PI / 180.0) * h;
            GL11.glVertex2d(x + ldx, y + ldy);
        }
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }

    public static void quickDrawRect(final float x, final float y, final float x2, final float y2) {
        glBegin(GL_QUADS);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);

        glEnd();
    }

    public static void drawCircleRect(float x, float y, float x1, float y1, float radius, int color){
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(color);

        // 圆角
        quickRenderCircle(x1-radius,y1-radius,0,90,radius,radius);
        quickRenderCircle(x+radius,y1-radius,90,180,radius,radius);
        quickRenderCircle(x+radius,y+radius,180,270,radius,radius);
        quickRenderCircle(x1-radius,y+radius,270,360,radius,radius);

        // 矩形
        quickDrawRect(x+radius,y+radius,x1-radius,y1-radius);
        quickDrawRect(x,y+radius,x+radius,y1-radius);
        quickDrawRect(x1-radius,y+radius,x1,y1-radius);
        quickDrawRect(x+radius,y,x1-radius,y+radius);
        quickDrawRect(x+radius,y1-radius,x1-radius,y1);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircleRectFix(float x, float y, float x1, float y1, float radius, int color){
        GlStateManager.color(0, 0, 0);
        GL11.glColor4f(0, 0, 0, 0);

        float var11 = (color >> 24 & 255) / 255.0F;
        float var6 = (color >> 16 & 255) / 255.0F;
        float var7 = (color >> 8 & 255) / 255.0F;
        float var8 = (color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);

        // 圆角
        quickRenderCircle(x1-radius,y1-radius,0,90,radius,radius);
        quickRenderCircle(x+radius,y1-radius,90,180,radius,radius);
        quickRenderCircle(x+radius,y+radius,180,270,radius,radius);
        quickRenderCircle(x1-radius,y+radius,270,360,radius,radius);

        // 矩形
        quickDrawRect(x+radius,y+radius,x1-radius,y1-radius);
        quickDrawRect(x,y+radius,x+radius,y1-radius);
        quickDrawRect(x1-radius,y+radius,x1,y1-radius);
        quickDrawRect(x+radius,y,x1-radius,y+radius);
        quickDrawRect(x+radius,y1-radius,x1-radius,y1);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawEntityBox(final Entity entity, final Color color, final boolean outline) {
        final RenderManager renderManager = mc.getRenderManager();
        final Timer timer = mc.timer;

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks
                - renderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks
                - renderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks
                - renderManager.renderPosZ;

        final AxisAlignedBB entityBox = entity.getEntityBoundingBox();
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBox.minX - entity.posX + x - 0.05D,
                entityBox.minY - entity.posY + y,
                entityBox.minZ - entity.posZ + z - 0.05D,
                entityBox.maxX - entity.posX + x + 0.05D,
                entityBox.maxY - entity.posY + y + 0.15D,
                entityBox.maxZ - entity.posZ + z + 0.05D
        );

        if (outline) {
            glLineWidth(1F);
            enableGlCap(GL_LINE_SMOOTH);
            glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            drawSelectionBoundingBox(axisAlignedBB);
        }

        glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
        drawFilledBox(axisAlignedBB);
        GlStateManager.resetColor();
        glDepthMask(true);
        resetCaps();
    }

    public static void drawEntityOnScreen(final int posX, final int posY, final int scale, final EntityLivingBase entity) {
        GlStateManager.pushMatrix();
        GlStateManager.enableColorMaterial();

        GlStateManager.translate(posX, posY, 50.0);
        GlStateManager.scale((-scale), scale, scale);
        GlStateManager.rotate(180F, 0F, 0F, 1F);
        GlStateManager.rotate(135F, 0F, 1F, 0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135F, 0F, 1F, 0F);
        GlStateManager.translate(0.0, 0.0, 0.0);

        float renderYawOffset = entity.renderYawOffset;
        float rotationYaw = entity.rotationYaw;
        float rotationPitch = entity.rotationPitch;
        float prevRotationYawHead = entity.prevRotationYawHead;
        float rotationYawHead = entity.rotationYawHead;


        entity.renderYawOffset = 0;
        entity.rotationYaw = 0;
        entity.rotationPitch = 90;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;

        RenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0F, 1F);
        rendermanager.setRenderShadow(true);

        entity.renderYawOffset = renderYawOffset;
        entity.rotationYaw = rotationYaw;
        entity.rotationPitch = rotationPitch;
        entity.prevRotationYawHead = prevRotationYawHead;
        entity.rotationYawHead = rotationYawHead;

        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void drawEntityBox2(final Entity entity, final Color color, final boolean outline, final boolean box, final float outlineWidth) {
        final RenderManager renderManager = mc.getRenderManager();
        final Timer timer = mc.timer;

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks
                - renderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks
                - renderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks
                - renderManager.renderPosZ;

        final AxisAlignedBB entityBox = entity.getEntityBoundingBox();
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBox.minX - entity.posX + x - 0.05D,
                entityBox.minY - entity.posY + y,
                entityBox.minZ - entity.posZ + z - 0.05D,
                entityBox.maxX - entity.posX + x + 0.05D,
                entityBox.maxY - entity.posY + y + 0.15D,
                entityBox.maxZ - entity.posZ + z + 0.05D
        );

        if (outline) {
            glLineWidth(outlineWidth);
            enableGlCap(GL_LINE_SMOOTH);
            glColor(color.getRed(), color.getGreen(), color.getBlue(), box?170:255);
            drawSelectionBoundingBox(axisAlignedBB);
        }

        if(box) {
            glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
            drawFilledBox(axisAlignedBB);
        }

        GlStateManager.resetColor();
        glDepthMask(true);
        resetCaps();
    }

    public static void drawAxisAlignedBB(final AxisAlignedBB axisAlignedBB, final Color color, final boolean outline, final boolean box, final float outlineWidth) {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glLineWidth(outlineWidth);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glColor(color);

        if (outline) {
            glLineWidth(outlineWidth);
            enableGlCap(GL_LINE_SMOOTH);
            glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            drawSelectionBoundingBox(axisAlignedBB);
        }

        if(box) {
            glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
            drawFilledBox(axisAlignedBB);
        }

        GlStateManager.resetColor();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }


    public static void drawAxisAlignedBB(final AxisAlignedBB axisAlignedBB, final Color color) {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glLineWidth(2F);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glColor(color);
        drawFilledBox(axisAlignedBB);
        GlStateManager.resetColor();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void drawPlatform(final double y, final Color color, final double size) {
        final RenderManager renderManager = mc.getRenderManager();
        final double renderY = y - renderManager.renderPosY;

        drawAxisAlignedBB(new AxisAlignedBB(size, renderY + 0.02D, size, -size, renderY, -size), color);
    }

    public static void drawPlatform(final Entity entity, final Color color) {
        final RenderManager renderManager = mc.getRenderManager();
        final Timer timer = mc.timer;

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks
                - renderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks
                - renderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks
                - renderManager.renderPosZ;

        final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox()
                .offset(-entity.posX, -entity.posY, -entity.posZ)
                .offset(x, y, z);

        drawAxisAlignedBB(
                new AxisAlignedBB(axisAlignedBB.minX, axisAlignedBB.maxY + 0.2, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY + 0.26, axisAlignedBB.maxZ),
                color
        );
    }

    public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawRectBlurFix(final float x, final float y, final float x2, final float y2, final int color) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        glColor(color);
        glBegin(GL_QUADS);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    public static void drawRectBlur(final float x, final float y, final float x2, final float y2, final int color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        glColor(color);
        quickDrawRect(x, y, x2, y2);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }


    public static void drawRect(final float x, final float y, final float x2, final float y2, final int color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glColor(color);
        glBegin(GL_QUADS);
        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }

    public static void drawRect(final float x, final float y, final float x2, final float y2, final Color color) {
        drawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawRect(double left, double top, double right, double bottom, int color)
    {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRectBlur(final float x, final float y, final float x2, final float y2, final float width,
                                            final int color1, final int color2) {
        drawRectBlurFix(x, y, x2, y2, color2);
        drawBorder(x, y, x2, y2, width, color1);
    }

    public static void drawBorder(float x, float y, float x2, float y2, float width, int color1) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        glColor(color1);
        glLineWidth(width);

        glBegin(GL_LINE_LOOP);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);

        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }

    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float width,
                                        final int color1, final int color2) {
        drawRect(x, y, x2, y2, color2);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glColor(color1);
        glLineWidth(width);
        glBegin(1);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x2, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }

    public static void drawLoadingCircle(float x, float y) {
        for (int i = 0; i < 4; i++) {
            int rot = (int) ((System.nanoTime() / 5000000 * i) % 360);
            drawCircle(x, y, i * 10, rot - 180, rot);
        }
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor(Color.WHITE);

        glEnable(GL_LINE_SMOOTH);
        glLineWidth(2F);
        glBegin(GL_LINE_STRIP);
        for (float i = end; i >= start; i -= (360 / 90))
            glVertex2f((float) (x + (cos(i * PI / 180) * (radius * 1.001F))), (float) (y + (sin(i * PI / 180) * (radius * 1.001F))));
        glEnd();
        glDisable(GL_LINE_SMOOTH);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col) {
        float f = (float) (col >> 24 & 255) / 255.0f;
        float f1 = (float) (col >> 16 & 255) / 255.0f;
        float f2 = (float) (col >> 8 & 255) / 255.0f;
        float f3 = (float) (col & 255) / 255.0f;
        int sections = 50;
        double dAngle = 6.283185307179586 / (double) sections;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glBegin(6);
        int i = 0;
        while (i < sections) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));
            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f((float) xx + x, (float) yy + y);
            ++i;
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(float xx, float yy, float radius, int col) {
        float f = (float) (col >> 24 & 255) / 255.0f;
        float f1 = (float) (col >> 16 & 255) / 255.0f;
        float f2 = (float) (col >> 8 & 255) / 255.0f;
        float f3 = (float) (col & 255) / 255.0f;
        int sections = 50;
        double dAngle = 6.283185307179586 / (double) sections;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        int i = 0;
        while (i < sections) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));
            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f(xx + x, yy + y);
            ++i;
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(final int xx, final int yy, final float radius, final Color color) {
        int sections = 50;
        double dAngle = 2 * Math.PI / sections;
        float x, y;

        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i < sections; i++) {
            x = (float) (radius * Math.sin((i * dAngle)));
            y = (float) (radius * Math.cos((i * dAngle)));

            glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
            glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0, 0, 0);

        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }



    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public static void glColor(final int red, final int green, final int blue, final int alpha) {
        GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void glColor(final Color color) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;
        final float alpha = color.getAlpha() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255F;
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(final int hex, final int alpha) {
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha / 255F);
    }

    public static void glColor(final int hex, final float alpha) {
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(final Color color, final float alpha) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void draw2D(final EntityLivingBase entity, final double posX, final double posY, final double posZ, final int color, final int backgroundColor) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        glNormal3f(0F, 0F, 0F);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
        GlStateManager.scale(-0.1D, -0.1D, 0.1D);
        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);

        drawRect(-7F, 2F, -4F, 3F, color);
        drawRect(4F, 2F, 7F, 3F, color);
        drawRect(-7F, 0.5F, -6F, 3F, color);
        drawRect(6F, 0.5F, 7F, 3F, color);

        drawRect(-7F, 3F, -4F, 3.3F, backgroundColor);
        drawRect(4F, 3F, 7F, 3.3F, backgroundColor);
        drawRect(-7.3F, 0.5F, -7F, 3.3F, backgroundColor);
        drawRect(7F, 0.5F, 7.3F, 3.3F, backgroundColor);

        GlStateManager.translate(0, 21 + -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12, 0);

        drawRect(4F, -20F, 7F, -19F, color);
        drawRect(-7F, -20F, -4F, -19F, color);
        drawRect(6F, -20F, 7F, -17.5F, color);
        drawRect(-7F, -20F, -6F, -17.5F, color);

        drawRect(7F, -20F, 7.3F, -17.5F, backgroundColor);
        drawRect(-7.3F, -20F, -7F, -17.5F, backgroundColor);
        drawRect(4F, -20.3F, 7.3F, -20F, backgroundColor);
        drawRect(-7.3F, -20.3F, -4F, -20F, backgroundColor);

        // Stop render
        glEnable(GL_DEPTH_TEST);
        GlStateManager.popMatrix();
    }

    public static void draw2D(final BlockPos blockPos, final int color, final int backgroundColor) {
        final RenderManager renderManager = mc.getRenderManager();

        final double posX = (blockPos.getX() + 0.5) - renderManager.renderPosX;
        final double posY = blockPos.getY() - renderManager.renderPosY;
        final double posZ = (blockPos.getZ() + 0.5) - renderManager.renderPosZ;

        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        glNormal3f(0F, 0F, 0F);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
        GlStateManager.scale(-0.1D, -0.1D, 0.1D);
        setGlCap(GL_DEPTH_TEST, false);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);

        drawRect(-7F, 2F, -4F, 3F, color);
        drawRect(4F, 2F, 7F, 3F, color);
        drawRect(-7F, 0.5F, -6F, 3F, color);
        drawRect(6F, 0.5F, 7F, 3F, color);

        drawRect(-7F, 3F, -4F, 3.3F, backgroundColor);
        drawRect(4F, 3F, 7F, 3.3F, backgroundColor);
        drawRect(-7.3F, 0.5F, -7F, 3.3F, backgroundColor);
        drawRect(7F, 0.5F, 7.3F, 3.3F, backgroundColor);

        GlStateManager.translate(0, 9, 0);

        drawRect(4F, -20F, 7F, -19F, color);
        drawRect(-7F, -20F, -4F, -19F, color);
        drawRect(6F, -20F, 7F, -17.5F, color);
        drawRect(-7F, -20F, -6F, -17.5F, color);

        drawRect(7F, -20F, 7.3F, -17.5F, backgroundColor);
        drawRect(-7.3F, -20F, -7F, -17.5F, backgroundColor);
        drawRect(4F, -20.3F, 7.3F, -20F, backgroundColor);
        drawRect(-7.3F, -20.3F, -4F, -20F, backgroundColor);

        // Stop render
        resetCaps();
        GlStateManager.popMatrix();
    }

    public static void renderNameTag(final String string, final double x, final double y, final double z) {
        final RenderManager renderManager = mc.getRenderManager();

        glPushMatrix();
        glTranslated(x - renderManager.renderPosX, y - renderManager.renderPosY, z - renderManager.renderPosZ);
        glNormal3f(0F, 1F, 0F);
        glRotatef(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
        glRotatef(mc.getRenderManager().playerViewX, 1F, 0F, 0F);
        glScalef(-0.05F, -0.05F, 0.05F);
        setGlCap(GL_LIGHTING, false);
        setGlCap(GL_DEPTH_TEST, false);
        setGlCap(GL_BLEND, true);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        final int width = Fonts.font35.getStringWidth(string) / 2;

        Gui.drawRect(-width - 1, -1, width + 1, Fonts.font35.FONT_HEIGHT, Integer.MIN_VALUE);
        Fonts.font35.drawString(string, -width, 1.5F, Color.WHITE.getRGB(), true);

        resetCaps();
        glColor4f(1F, 1F, 1F, 1F);
        glPopMatrix();
    }

    public static void drawLine(final double x, final double y, final double x1, final double y1, final float width) {
        glDisable(GL_TEXTURE_2D);
        glLineWidth(width);
        glBegin(GL_LINES);
        glVertex2d(x, y);
        glVertex2d(x1, y1);
        glEnd();
        glEnable(GL_TEXTURE_2D);
    }

    public static void makeScissorBox(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int factor = scaledResolution.getScaleFactor();
        glScissor((int) (x * factor), (int) ((scaledResolution.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    /**
     * GL CAP MANAGER
     *
     * TODO: Remove gl cap manager and replace by something better
     */

    public static void resetCaps() {
        glCapMap.forEach(RenderUtils::setGlState);
    }

    public static void enableGlCap(final int cap) {
        setGlCap(cap, true);
    }

    public static void enableGlCap(final int... caps) {
        for (final int cap : caps)
            setGlCap(cap, true);
    }

    public static void disableGlCap(final int cap) {
        setGlCap(cap, true);
    }

    public static void disableGlCap(final int... caps) {
        for (final int cap : caps)
            setGlCap(cap, false);
    }

    public static void setGlCap(final int cap, final boolean state) {
        glCapMap.put(cap, glGetBoolean(cap));
        setGlState(cap, state);
    }

    public static void setGlState(final int cap, final boolean state) {
        if (state)
            glEnable(cap);
        else
            glDisable(cap);
    }
}