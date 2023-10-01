package net.ccbluex.liquidbounce.ui.client.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor;
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

public class ColorManagerGui extends GuiScreen {
    //Start
    public static int CategoryY;
    private double slide, progress = 0;

    private int mX, mY;
    boolean isinit = false;
    //Mimosa不会简化写的烂代码。。。
    @Override
    public void initGui() {
        CategoryY = height / 2 - 120 + 105;
        //bo
        this.buttonList.add(new GuiIconButton(4, width/2 + 90, height/2 +90, 60, 20, "",new ResourceLocation("liquidbounce/head/omg.png")));
        //1
        this.buttonList.add(new GuiIconButton(11, width/2 + 9, height/2 -2, 10, 10, "",new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(12, width/2 + 26, height/2 -2, 10, 10, "",new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(13, width/2 + 43, height/2 -2, 10, 10, "",new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(14, width/2 + 60, height/2 -2, 10, 10, "",new ResourceLocation("liquidbounce/head/omg.png")));

        //2
        this.buttonList.add(new GuiIconButton(21, width/2 + 10, height/2 + 24, 10, 10, "",new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(22, width/2 + 26, height/2 + 24, 10, 10, "",new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(23, width/2 + 45, height/2 + 24, 10, 10, "",new ResourceLocation("liquidbounce/head/omg.png")));

        //3
        this.buttonList.add(new GuiIconButton(31, width/2 - 25, height/2 + 49, 21, 11, "",new ResourceLocation("liquidbounce/head/omg.png")));
        //4
        this.buttonList.add(new GuiIconButton(41, width/2 - 65, height/2 + 73, 21, 11, "",new ResourceLocation("liquidbounce/head/omg.png")));
        //choose
        super.initGui();
    }
    public void init() {

        switch (Gui) {
            case "HUD": {
                mc.displayGuiScreen(new MusicGui());
            }
            case "COLOR": {

            }
        }
    }

    public int ii = 0;
    String Gui = "COLOR";
    String realColor=ClientColor.ColorMode.get();
    int Color = 11;
    private final ResourceLocation hudIcon = new ResourceLocation(LiquidBounce.CLIENT_NAME.toLowerCase() + "/custom_hud_icon.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(!isinit){
            init();
            isinit=true;
        }
        if(Color>21){
            Color=11;
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
        RenderUtils.drawRoundRect(midX-160, midY - 120, midX+160, midY + 120, 7.0f, new Color(255, 255, 255).getRGB());
        RenderUtils.drawRoundRect(midX-110, midY - 120, midX-109.5f, midY + 120, 0.0f, new Color(0, 0, 0,40).getRGB());
        RenderUtils.drawImage(new ResourceLocation("liquidbounce/Logo.png"),width/2 -150, height/2 - 112, 32, 32);
        Fonts.roboto35.drawCenteredString("Sinka",width/2 -133.5f, height/2 - 77f,new Color(0,0,0,60).getRGB(),false);
        //Draw Color
        if(Gui.equals("HUD")){

        }else{
            Fonts.regular60.drawString("StyleManager", width / 2 - 100, midY - 110,new Color(0,0,0,255).getRGB(), false);
            //线
            RoundedUtils.drawGradientRoundLR(midX-100.5f, midY - 90.5f, 121f, 76f, 4.7F, new Color(0,0,0,40), new Color(0,0,0,40));
            //bg2
            RoundedUtils.drawGradientRoundLR(midX-100, midY - 90, 120f, 75f, 5.0F, new Color(255,255,255), new Color(255,255,255));
            Fonts.roboto35.drawString("ClientColor:"+realColor,midX-95,midY - 85,new Color(0,0,0,70).getRGB(),false);
            Fonts.roboto35.drawString("BGColor:"+ClientColor.rColorMode.get(),midX-95,midY - 70,new Color(0,0,0,70).getRGB(),false);
            Fonts.roboto35.drawString("ClickGui:"+ClientColor.clickMode.get(),midX-95,midY - 55,new Color(0,0,0,70).getRGB(),false);
            Fonts.roboto35.drawString("Alpha:"+ClientColor.rAlphaMode.get()+"%",midX-95,midY - 40,new Color(0,0,0,70).getRGB(),false);
            //ModeFont
            //1
            Fonts.roboto40.drawString("BackgroundAlphaMode",midX-95,midY,new Color(50,50,100,200).getRGB(),false);
            //2
            Fonts.roboto40.drawString("Draw-Rect-ColorMode",midX-95,midY + 25,new Color(50,50,100,200).getRGB(),false);
            //3
            Fonts.roboto40.drawString("ClickGui-White",midX-95,midY + 50,new Color(50,50,100,200).getRGB(),false);
            //4
            Fonts.roboto40.drawString("White",midX-95,midY + 75,new Color(50,50,100,200).getRGB(),false);

            //bo
            RoundedUtils.drawGradientRoundLR(midX+ 90, midY +90, 60f, 20f, 5.0F,new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 180), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 180));
            Fonts.roboto40.drawCenteredString("Color",midX+ 122, midY +96,new Color(255,255,255,255).getRGB(),false);

            //modebo
            RoundedUtils.drawGradientRoundLR(midX + 10, midY, 10f, 10f, 2.0F,new Color(240,240,240, 255), new Color(240,240,240, 255));
            //1
            RoundedUtils.drawGradientRoundLR(midX + 9.5f, midY - 2.5f, 11f, 11f, 2.0F,new  Color(0,0,0, 40), new Color(0,0,0, 40));
            RoundedUtils.drawGradientRoundLR(midX + 26.5f, midY - 2.5f, 11f, 11f, 2.0F,new Color(0,0,0, 40), new Color(0,0,0, 40));
            RoundedUtils.drawGradientRoundLR(midX + 43.5f, midY - 2.5f, 11f, 11f, 2.0F,new Color(0,0,0, 40), new Color(0,0,0, 40));
            RoundedUtils.drawGradientRoundLR(midX + 60.5f, midY - 2.5f, 11f, 11f, 2.0F,new Color(0,0,0, 40), new Color(0,0,0, 40));

            //bo
            RoundedUtils.drawGradientRoundLR(midX + 10, midY -2, 10f, 10f, 2.0F,new  Color(255,255,255, 255), new Color(255,255,255, 255));
            RoundedUtils.drawGradientRoundLR(midX + 27, midY -2, 10f, 10f, 2.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));
            RoundedUtils.drawGradientRoundLR(midX + 44, midY -2, 10f, 10f, 2.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));
            RoundedUtils.drawGradientRoundLR(midX + 61, midY -2, 10f, 10f, 2.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));
            if(ClientColor.rAlphaMode.get().equals("100")){
                RoundedUtils.drawGradientRoundLR(midX + 10, midY -2, 10f, 10f, 2.0F,new Color(255,130,160, 180), new Color(255,160,200, 180));
            }else if(ClientColor.rAlphaMode.get().equals("70")){
                RoundedUtils.drawGradientRoundLR(midX + 27, midY -2, 10f, 10f, 2.0F,new  Color(255,130,160, 180), new Color(255,160,200, 180));
            }else if(ClientColor.rAlphaMode.get().equals("50")){
                RoundedUtils.drawGradientRoundLR(midX + 44, midY -2, 10f, 10f, 2.0F,new Color(255,130,160, 180), new Color(255,160,200, 180));
            }else{
                RoundedUtils.drawGradientRoundLR(midX + 61, midY -2, 10f, 10f, 2.0F,new Color(255,130,160, 180), new Color(255,160,200, 180));
            }
            //2
            //线
            RoundedUtils.drawGradientRoundLR(midX + 9.5f, midY + 23.5f, 11f, 11f, 2.0F,new  Color(0,0,0, 40), new Color(0,0,0, 40));
            RoundedUtils.drawGradientRoundLR(midX + 26.5f, midY + 23.5f, 11f, 11f, 2.0F,new Color(0,0,0, 40), new Color(0,0,0, 40));
            RoundedUtils.drawGradientRoundLR(midX + 43.5f, midY + 23.5f, 11f, 11f, 2.0F,new Color(0,0,0, 40), new Color(0,0,0, 40));
            //bo
            RoundedUtils.drawGradientRoundLR(midX + 10, midY + 24, 10f, 10f, 2.0F,new  Color(255,255,255, 255), new Color(255,255,255, 255));
            RoundedUtils.drawGradientRoundLR(midX + 27, midY + 24, 10f, 10f, 2.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));
            RoundedUtils.drawGradientRoundLR(midX + 44, midY + 24, 10f, 10f, 2.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));
            if(ClientColor.rColorMode.get().equals("White")){
                RoundedUtils.drawGradientRoundLR(midX + 10, midY + 24, 10f, 10f, 2.0F,new Color(255,130,160, 180), new Color(255,160,200, 180));
            }else if(ClientColor.rColorMode.get().equals("Grey")){
                RoundedUtils.drawGradientRoundLR(midX + 27, midY + 24, 10f, 10f, 2.0F,new  Color(255,130,160, 180), new Color(255,160,200, 180));
            }else{
                RoundedUtils.drawGradientRoundLR(midX + 44, midY + 24, 10f, 10f, 2.0F,new Color(255,130,160, 180), new Color(255,160,200, 180));
            }
            //3
            if(ClientColor.clickMode.get().equals("White")){
                RoundedUtils.drawGradientRoundLR(midX - 25.5f, midY + 48.5f, 21f, 11f, 5.0F,new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 180), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 180));
                RoundedUtils.drawGradientRoundLR(midX - 14, midY + 50, 8f, 8f, 4.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));
            }else{
                RoundedUtils.drawGradientRoundLR(midX - 25.5f, midY + 48.5f, 21f, 11f, 5.0F,new Color(0,0,0, 40), new Color(0,0,0, 40));
                RoundedUtils.drawGradientRoundLR(midX - 24, midY + 50, 8f, 8f, 4.0F,new Color(240,240,240, 255), new Color(240,240,240, 255));
            }
            //4
            if(ClientColor.ColorMode.get().equals("White")){
                RoundedUtils.drawGradientRoundLR(midX - 65.5f, midY + 73.5f, 21f, 11f, 5.0F,new Color(255,160,200, 180), new Color(100,200,255, 180));
                RoundedUtils.drawGradientRoundLR(midX - 54, midY + 75, 8f, 8f, 4.0F,new Color(255,255,255, 255), new Color(255,255,255, 255));
            }else{
                RoundedUtils.drawGradientRoundLR(midX - 65.5f, midY + 73.5f, 21f, 11f, 5.0F,new Color(0,0,0, 40), new Color(0,0,0, 40));
                RoundedUtils.drawGradientRoundLR(midX - 64, midY + 75, 8f, 8f, 4.0F,new Color(240,240,240, 255), new Color(240,240,240, 255));
            }

        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 3: {
                mc.displayGuiScreen(new GuiHudDesigner());
                break;
            }
            case 4: {
                mc.displayGuiScreen(new ColorFinder());
                break;
            }
            case 11: {
                ClientColor.rAlphaMode.setValue("100");
                break;
            }
            case 12: {
                ClientColor.rAlphaMode.setValue("70");
                break;
            }
            case 13: {
                ClientColor.rAlphaMode.setValue("50");
                break;
            }
            case 14: {
                ClientColor.rAlphaMode.setValue("25");
                break;
            }

            case 21: {
                ClientColor.rColorMode.setValue("White");
                break;
            }
            case 22: {
                ClientColor.rColorMode.setValue("Grey");
                break;
            }
            case 23: {
                ClientColor.rColorMode.setValue("Black");
                break;
            }
            case 31: {
                if(ClientColor.clickMode.get().equals("White")){
                    ClientColor.clickMode.setValue("Grey");
                   }else{
                    ClientColor.clickMode.setValue("White");
                   }
                break;
            }
            case 41: {
                Color = 21;
                break;
            }
        }
    }

}
