package net.ccbluex.liquidbounce.ui.client.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.Cape;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.*;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class ColorFinder extends GuiScreen {
    //Start
    public static int CategoryY;
    private double slide, progress = 0;

    private int mX, mY;
    boolean isinit = false;
    //Mimosa不会简化写的烂代码。。。
    @Override
    public void initGui() {
        CategoryY = height / 2 - 120 + 105;

        super.initGui();
    }
    public void init() {

    }

    public int ii = 0;

    private final ResourceLocation hudIcon = new ResourceLocation(LiquidBounce.CLIENT_NAME.toLowerCase() + "/custom_hud_icon.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(!isinit){
            init();
            isinit=true;
        }
        if (progress < 1) progress += 0.1 * (1 - partialTicks);
        else progress = 1;
        //
        slide = EaseUtils.easeOutQuart(progress);

        GlStateManager.translate((1.0 - slide) * (width / 2.0), (1.0 - slide) * (height / 2.0), 0);
        GlStateManager.scale(1 * slide, 1 * slide, 1 * slide);

        mX = mouseX;
        mY = mouseY;

        //Pos
        int midX = width / 2;
        int midY = height / 2;
        //BG
        RenderUtils.drawRoundRect(midX - 70, midY - 120, midX + 70, midY + 120, 7.0f, new Color(255, 255, 255).getRGB());
        Fonts.roboto35.drawCenteredString("ColorFinder", midX, midY - 115,new Color(70,70,70,255).getRGB(), false);

        //MusicList
        //Fonts.roboto30.drawString("1.On My Own", width / 2 - 58, midY - 100, Color.BLACK.getRGB(), false);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {

        }
    }
}
