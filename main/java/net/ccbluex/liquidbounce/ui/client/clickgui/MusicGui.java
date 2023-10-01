package net.ccbluex.liquidbounce.ui.client.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientSettings;
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

public class MusicGui extends GuiScreen {
    //Start
    public static int CategoryY;
    private double slide, progress = 0;

    private int mX, mY;
    boolean isinit = false;

    boolean isMusic = true;
    //Mimosa不会简化写的烂代码。。。
    @Override
    public void initGui() {
        CategoryY = height / 2 - 120 + 105;
        this.buttonList.add(new GuiIconButton(1, width / 2 + 55, height/2 - 100, 10, 10, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(2, width / 2 + 55, height/2 - 90, 10, 10, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(3, width / 2 + 55, height/2 - 80, 10, 10, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(4, width / 2 + 55, height/2 - 70, 10, 10, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(5, width / 2 + 55, height/2 - 60, 10, 10, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(6, width / 2 + 55, height/2 - 50, 10, 10, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(7, width / 2 + 55, height/2 - 40, 10, 10, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(8, width / 2 + 55, height/2 - 30, 10, 10, "", new ResourceLocation("liquidbounce/head/omg.png")));
        //bo
        this.buttonList.add(new GuiIconButton(100, width / 2 + 45, height/2, 20, 6, "", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(200, width / 2 + 45, height/2 + 15, 20, 6, "", new ResourceLocation("liquidbounce/head/omg.png")));

        super.initGui();
    }
    public void init() {

    }

    public int ii = 0;
    int music = 1000;

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
        RenderUtils.drawRoundRect(midX - 70, midY - 120, midX + 70, midY + 60, 7.0f, new Color(255, 255, 255).getRGB());
        Fonts.roboto35.drawCenteredString("MVP-Music", width / 2, midY - 115,new Color(70,70,70,255).getRGB(), false);
        //MusicList
        Fonts.roboto30.drawString("1.On My Own", width / 2 - 58, midY - 100,new Color(70,70,70,255).getRGB(), false);
        Fonts.roboto30.drawString("2.Liquid", width / 2 - 58, midY - 90,new Color(70,70,70,255).getRGB(), false);
        Fonts.roboto30.drawString("3.Disorder", width / 2 - 58, midY - 80,new Color(70,70,70,255).getRGB(), false);
        Fonts.roboto30.drawString("4.dashstar", width / 2 - 58, midY - 70,new Color(70,70,70,255).getRGB(), false);
        Fonts.roboto30.drawString("5.GOODRAGE", width / 2 - 58, midY - 60,new Color(70,70,70,255).getRGB(), false);
        Fonts.roboto30.drawString("6.Chronomia", width / 2 - 58, midY - 50,new Color(70,70,70,255).getRGB(), false);
        Fonts.roboto30.drawString("7.OLDGENESIS", width / 2 - 58, midY - 40,new Color(70,70,70,255).getRGB(), false);
        Fonts.roboto30.drawString("8.Drop", width / 2 - 58, midY - 30, Color.BLACK.getRGB(), false);
        RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 100, 6f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
        RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 90, 6f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
        RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 80, 6f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
        RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 70, 6f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
        RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 60, 6f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
        RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 50, 6f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
        RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 40, 6f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
        RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 30, 6f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
        switch (music){
            case 1:
                RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 100, 6f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 160), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 160));
                break;
            case 2:
                RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 90, 6f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 160), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 160));
                break;
            case 3:
                RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 80, 6f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 160), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 160));
                break;
            case 4:
                RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 70, 6f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 160), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 160));
                break;
            case 5:
                RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 60, 6f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 160), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 160));
                break;
            case 6:
                RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 50, 6f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 160), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 160));
                break;
            case 7:
                RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 40, 6f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 160), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 160));
                break;
            case 8:
                RoundedUtils.drawGradientRoundLR(width / 2 + 55, midY - 30, 6f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 160), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 160));
                break;
            default:

                break;
        }
        Fonts.roboto30.drawString("Music", width / 2 - 58, midY, Color.BLACK.getRGB(), false);
        Fonts.roboto30.drawString("AutoServerMode", width / 2 - 58, midY + 15, Color.BLACK.getRGB(), false);

        if(((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusic().get()){
            RoundedUtils.drawGradientRoundLR(width / 2 + 53, midY, 12f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 180), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 180));
            RoundedUtils.drawGradientRoundLR(width / 2 + 60, midY + 1, 4f, 4f, 2.0F,new  Color(255,255,255, 255), new Color(255,255,255, 255));
        }else{
            RoundedUtils.drawGradientRoundLR(width / 2 + 53, midY, 12f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
            RoundedUtils.drawGradientRoundLR(width / 2 + 54, midY + 1, 4f, 4f, 2.0F,new  Color(255,255,255, 255), new Color(255,255,255, 255));
        }
        if(((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getYanZhen().get()){
            RoundedUtils.drawGradientRoundLR(width / 2 + 53, midY + 15, 12f, 6f, 2.0F,new  Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 180), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 180));
            RoundedUtils.drawGradientRoundLR(width / 2 + 60, midY + 16, 4f, 4f, 2.0F,new  Color(255,255,255, 255), new Color(255,255,255, 255));
        }else{
            RoundedUtils.drawGradientRoundLR(width / 2 + 53, midY + 15, 12f, 6f, 2.0F,new  Color(230,230,230, 255), new Color(230,230,230, 255));
            RoundedUtils.drawGradientRoundLR(width / 2 + 54, midY + 16, 4f, 4f, 2.0F,new  Color(255,255,255, 255), new Color(255,255,255, 255));
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1:
                isMusic=true;
                if(isMusic){
                    LiquidBounce.tipSoundManager.getMvp().asyncPlay();
                    isMusic=false;
                }
                music=1;
                ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusicValue().setValue("On-My-Own");
                break;
            case 2:
                isMusic=true;
                if(isMusic){
                    LiquidBounce.tipSoundManager.getMvp1().asyncPlay();
                    isMusic=false;
                }
                music=2;
                ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusicValue().setValue("Liquid");
                break;
            case 3:
                isMusic=true;
                if(isMusic){
                    LiquidBounce.tipSoundManager.getMvp6().asyncPlay();
                    isMusic=false;
                }
                music=3;
                ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusicValue().setValue("Disorder");
                break;
            case 4:
                isMusic=true;
                if(isMusic){
                    LiquidBounce.tipSoundManager.getMvp3().asyncPlay();
                    isMusic=false;
                }
                music=4;
                ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusicValue().setValue("dashstar");
                break;
            case 5:
                isMusic=true;
                if(isMusic){
                    LiquidBounce.tipSoundManager.getMvp2().asyncPlay();
                    isMusic=false;
                }
                music=5;
                ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusicValue().setValue("GOODRAGE");
                break;
            case 6:
                isMusic=true;
                if(isMusic){
                    LiquidBounce.tipSoundManager.getMvp4().asyncPlay();
                    isMusic=false;
                }
                music=6;
                ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusicValue().setValue("Chronomia");
                break;
            case 7:
                isMusic=true;
                if(isMusic){
                    LiquidBounce.tipSoundManager.getMvp5().asyncPlay();
                    isMusic=false;
                }
                music=7;
                ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusicValue().setValue("OLDGENESIS");
                break;
            case 8:
                isMusic=true;
                if(isMusic){
                    LiquidBounce.tipSoundManager.getDrop().asyncPlay();
                    isMusic=false;
                }
                music=8;
                ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusicValue().setValue("Drop");
                break;
            case 100:
                if(((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusic().get()){
                    ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusic().setValue(false);
                }else{
                    ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getMusic().setValue(true);
                }
                break;
            case 200:
                if(((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getYanZhen().get()){
                    ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getYanZhen().setValue(false);
                }else{
                    ((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getYanZhen().setValue(true);
                }
                break;
        }
    }
}
