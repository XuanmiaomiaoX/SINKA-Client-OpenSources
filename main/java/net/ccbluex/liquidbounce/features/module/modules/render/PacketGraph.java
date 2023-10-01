package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.BlockObject;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeHelper;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(name = "PacketGraph", description = "", category = ModuleCategory.RENDER)
public class PacketGraph extends Module{
    public final FloatValue xValue;
    public final FloatValue yValue;
    private List<BlockObject> clientBlocks;
    private List<BlockObject> serverBlocks;
    private TimeHelper timerUtil;
    private TimeHelper secTimerUtil;
    private static int clientPackets;
    private static int serverPackets;
    private static int secClientPackets;
    private static int secServerPackets;
    private int renderSecClientPackets;
    private int renderSecServerPackets;

    public PacketGraph() {
        this.xValue = new FloatValue("X", 10.0f, 0.0f, 2000.0f);
        this.yValue = new FloatValue("Y", 100.0f, 0.0f, 2000.0f);
        this.clientBlocks = new CopyOnWriteArrayList<BlockObject>();
        this.serverBlocks = new CopyOnWriteArrayList<BlockObject>();
        this.timerUtil = new TimeHelper();
        this.secTimerUtil = new TimeHelper();
    }

    public void clear() {
        PacketGraph.clientPackets = 0;
        PacketGraph.serverPackets = 0;
        PacketGraph.secClientPackets = 0;
        PacketGraph.secServerPackets = 0;
        this.renderSecClientPackets = 0;
        this.renderSecServerPackets = 0;
        this.clientBlocks.clear();
        this.serverBlocks.clear();
        this.timerUtil.reset();
        this.secTimerUtil.reset();
    }

    @EventTarget
    public void onPacket(final PacketEvent e) {
        if (e.getPacket().getClass().getSimpleName().toLowerCase().startsWith("c")) {
            ++PacketGraph.clientPackets;
            ++PacketGraph.secClientPackets;
        }
        else if (e.getPacket().getClass().getSimpleName().toLowerCase().startsWith("s")) {
            ++PacketGraph.serverPackets;
            ++PacketGraph.secServerPackets;
        }
    }

    @EventTarget
    public void onRender(final Render2DEvent event) {
        final int x = ((Float)this.xValue.get()).intValue();
        final int y = ((Float)this.yValue.get()).intValue();
        RenderUtils.drawBorderedRect((float)(x - 3), (float)(y - 68), (float)(x + 153), (float)(y + 74), 1.0f, new Color(20, 220, 120, 198).getRGB(), new Color(130, 130, 130, 108).getRGB());
        if (this.timerUtil.hasReached(50L)) {
            this.clientBlocks.forEach(blockObject -> --blockObject.x);
            this.clientBlocks.add(new BlockObject(x + 150, Math.min(PacketGraph.clientPackets, 55)));
            PacketGraph.clientPackets = 0;
            this.serverBlocks.forEach(blockObject -> --blockObject.x);
            this.serverBlocks.add(new BlockObject(x + 150, Math.min(PacketGraph.serverPackets, 55)));
            PacketGraph.serverPackets = 0;
            this.timerUtil.reset();
        }
        if (this.secTimerUtil.hasReached(1000L)) {
            this.renderSecClientPackets = PacketGraph.secClientPackets;
            this.renderSecServerPackets = PacketGraph.secServerPackets;
            PacketGraph.secClientPackets = 0;
            PacketGraph.secServerPackets = 0;
            this.secTimerUtil.reset();
        }
        int graphY = y;
        for (int i = 0; i < 2; ++i) {
            this.drawGraph(i, x, graphY);
            graphY += 68;
        }
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        if (!this.clientBlocks.isEmpty()) {
            final BlockObject firstBlock = this.clientBlocks.get(this.clientBlocks.size() - 1);
            PacketGraph.mc.fontRendererObj.drawString("< avg: " + firstBlock.height, x * 2 + 301, y * 2 - firstBlock.height * 2 - 4, -1);
        }
        if (!this.serverBlocks.isEmpty()) {
            final BlockObject firstBlock = this.serverBlocks.get(this.serverBlocks.size() - 1);
            PacketGraph.mc.fontRendererObj.drawString("< avg: " + firstBlock.height, x * 2 + 301, (y + 68) * 2 - firstBlock.height * 2 - 4, -1);
        }
        GL11.glPopMatrix();
        this.clientBlocks.removeIf(block -> block.x < x);
        this.serverBlocks.removeIf(block -> block.x < x);
    }

    private void drawGraph(final int mode, final int x, final int y) {
        final boolean isClient = mode == 0;
        RenderUtils.drawRect((double)x, y + 0.5, (double)(x + 150), (double)(y - 60), new Color(0, 0, 0, 80).getRGB());
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        final String secString = isClient ? (this.renderSecClientPackets + " packets/sec") : (this.renderSecServerPackets + " packets/sec");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        PacketGraph.mc.fontRendererObj.drawString(isClient ? "Outgoing packets" : "Incoming packets", x * 2, y * 2 - 129, -1);
        PacketGraph.mc.fontRendererObj.drawString(secString, x * 2 + 300 - PacketGraph.mc.fontRendererObj.getStringWidth(secString), y * 2 - 129, -1);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBegin(1);
        int rainbowTicks = 0;
        final List<BlockObject> list = isClient ? this.clientBlocks : this.serverBlocks;
        for (final BlockObject block : list) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glVertex2d((double)block.x, (double)(y - block.height));
            try {
                final BlockObject lastBlock = list.get(list.indexOf(block) + 1);
                GL11.glVertex2d((double)(block.x + 1), (double)(y - lastBlock.height));
            }
            catch (Exception ex) {}
            rainbowTicks += 300;
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }
}