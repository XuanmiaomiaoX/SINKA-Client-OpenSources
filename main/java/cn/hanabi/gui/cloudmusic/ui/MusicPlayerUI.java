package cn.hanabi.gui.cloudmusic.ui;

import cn.hanabi.gui.cloudmusic.MusicManager;
import cn.hanabi.gui.cloudmusic.api.CloudMusicAPI;
import cn.hanabi.gui.cloudmusic.impl.Track;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaPlayer.Status;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.*;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.RenderUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MusicPlayerUI extends GuiScreen {
    public float x = 10;
    public float y = 10;
    public float x2 = 0;
    public float y2 = 0;

    public boolean drag = false;
    //	public MouseHandler handler = new MouseHandler(0);
    public CopyOnWriteArrayList<TrackSlot> slots = new CopyOnWriteArrayList<>();

    public float width = 150;
    public float height = 250;

    public boolean extended = false;
    public float sidebarAnimation = 0;

    // 滚动
    public float scrollY = 0;
    public float scrollAni = 0;
    public float minY = -100;

    public CustomTextField textField = new CustomTextField("");

    public void initGui() {
        final int j = (int) (this.height - 80);
        this.buttonList.add(new GuiIconButton(114514, (int)(this.width / 2.0f+-35), (int)(this.height / 4.0f + -30.0f), 40, 40, I18n.format("", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/Logo.png")));

        super.initGui();
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        int left = 0;
        final int j = (int) (this.height - 80);
        RenderUtils.drawRoundRect(this.width / 2 - 15,this.height - 100, this.width / 2 - 15+ 300, this.height+100,13f,new Color(255, 255, 255,33).getRGB());
        for (int i = 0; i < this.width; ++i) {
            Gui.drawRect(i, (int) (this.height - 75), i + 1, (int) (this.height - 70), new Color(255,160,200,0).getRGB());
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 114514: {
                LiquidBounce.tipSoundManager.getStartup().asyncPlay();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen((GuiScreen) new GuiOptions((GuiScreen) this, this.mc.gameSettings));
                break;
            }
        }
    }
    protected void keyTyped(final char typedChar, final int keyCode) {
    }
}
