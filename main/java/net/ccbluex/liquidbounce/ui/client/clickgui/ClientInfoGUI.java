package net.ccbluex.liquidbounce.ui.client.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.client.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientSettings;
import net.ccbluex.liquidbounce.ui.client.*;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ClientInfoGUI extends GuiScreen {
    //Start
    public static int CategoryY;
    private double slide, progress = 0;

    private int mX, mY;

    @Override
    public void initGui() {
        CategoryY = height / 2 - 120 + 105;
        this.buttonList.add(new GuiIconButton(1, width / 2 - 25, (height / 2 - 120) + 213, 50, 20, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(2,  width/2 +26, height /2 + 67, 21, 11, "", new ResourceLocation("liquidbounce/head/omg.png")));

        super.initGui();
    }

    public int ii = 0;
    boolean isTrue = false;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).isInfo().setValue(isTrue);
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
        int top = midY - 120;

        //BG
        RenderUtils.drawRoundRect(midX - 70, midY - 120, midX + 70, midY + 120, 7.0f, new Color(255, 255, 255).getRGB());
        //ImgIcon
        RenderUtils.drawImage(new ResourceLocation("liquidbounce/ClickGuilogo/Logo2.png"), width / 2 - 45, top + 10, 90, 90);
        Fonts.regular60.drawCenteredString("Sinka Remake", width / 2, top + 100, Color.BLACK.getRGB(), false);
        RenderUtils.drawGradientSideways(width / 2 - 50, top + 115, width / 2 + 50, top + 115.5, new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 150).getRGB(), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 150).getRGB());
        Fonts.roboto35.drawCenteredString("Welcome to this client", width / 2, top + 119, Color.BLACK.getRGB(), false);
        Fonts.roboto35.drawString("1.HUDSettings > M", width / 2 - 45, top + 145, Color.BLACK.getRGB(), false);
        Fonts.roboto35.drawString("2.ClickGui > Rshift", width / 2 - 45, top + 160, Color.BLACK.getRGB(), false);
        Fonts.roboto35.drawString("3.\"E\" ExitGui", width / 2 - 45, top + 175, Color.BLACK.getRGB(), false);
        Fonts.roboto35.drawString("4.Never again? >", width / 2 - 45, top + 190, Color.BLACK.getRGB(), false);

        //bo
        RoundedUtils.drawGradientRoundLR(width / 2 - 25f, top + 213, 50f, 20f, 5.0F, new Color(255, 195, 230, 150), new Color(170, 230, 255, 150));
        Fonts.roboto35.drawCenteredString("JoinQQ", width / 2, top + 220,new Color(50,50,100).getRGB(), false);
        if(isTrue){
            RoundedUtils.drawGradientRoundLR(midX + 26.5f, midY + 67.5f, 21f, 11f, 5.0F,new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 180), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 180));
            RoundedUtils.drawGradientRoundLR(midX + 38, midY + 69, 8f, 8f, 4.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));
        }else{
            RoundedUtils.drawGradientRoundLR(midX + 26.5f, midY + 67.5f, 21f, 11f, 5.0F,new Color(200,200,200), new Color(200,200,200));
            RoundedUtils.drawGradientRoundLR(midX + 28, midY + 69, 8f, 8f, 4.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));

        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                MiscUtils.showURL("http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=CqGeBiGiKJTYLpmXv1cd8xU4nfKCbUfj&authKey=ytGkKVcMFnCXklzvVmDvg0DpRaHZWTXuJM9IhyRiYoMwFR1YuIX%2BcApwelQehva8&noverify=0&group_code=289568953");
                break;
            }
            case 2: {
                if(isTrue){
                    isTrue=false;
                }else{
                    isTrue=true;
                }
               break;
            }
        }
    }
}
