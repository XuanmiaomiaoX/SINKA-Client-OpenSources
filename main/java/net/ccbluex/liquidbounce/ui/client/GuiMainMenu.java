
package net.ccbluex.liquidbounce.ui.client;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientSettings;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.SinkaUser;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;
//Mimosa
public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback

{
    public void initGui() {
        final int j = (int)(this.height / 4.0f + -30.0f)+170;
        GuiBackground.Companion.setBg(((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getBgValue().get());
        this.buttonList.add(new GuiIconButton(1, this.width / 2 - 15 - 132, j + 10, 45, 45, I18n.format("SinglePlayer", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/singleplayer.png")));
        this.buttonList.add(new GuiIconButton(2, this.width / 2 - 15 - 70, j + 10, 45, 45, I18n.format("MultiPlayer", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/multiplayer.png")));
        this.buttonList.add(new GuiIconButton(100, this.width / 2 - 15 -7, j + 10, 45, 45, "AltManager", new ResourceLocation("LiquidBounce".toLowerCase() + "/altmanager.png")));
        this.buttonList.add(new GuiIconButton(102, this.width / 2 - 15 + 55, j + 10, 45, 45, "Background", new ResourceLocation("LiquidBounce".toLowerCase() + "/shader/background.png")));
        this.buttonList.add(new GuiIconButton(1000, this.width / 2 - 15+ 117, j + 10, 45, 45, I18n.format("Options", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/options.png")));
        this.buttonList.add(new GuiIconButton(114514, (int)(this.width / 2.0f - 100), (int)(this.height / 4.0f - 30.0f), 200, 100, I18n.format("", new Object[0]), new ResourceLocation("LiquidBounce".toLowerCase() + "/Sinkatext.png")));
        this.buttonList.add(new GuiIconButton(1234, 12, this.height-42, 30, 30, I18n.format("Click", new Object[0]), new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(12345, 42, this.height-45, 60,34, I18n.format("QQChat", new Object[0]), new ResourceLocation("liquidbounce/head/omg.png")));
        super.initGui();
    }
    public int ii = 0;
    public int head = 1;


    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if(!GuiWarning.Companion.isKnow()){
            this.mc.displayGuiScreen((GuiScreen) new GuiWarning(this));
        }
        this.drawBackground(0);
        switch (((ClientSettings) LiquidBounce.moduleManager.get(ClientSettings.class)).getBgValue().get()){
                case 1:
                    RenderUtils.drawImage(new ResourceLocation("liquidbounce/background/bg.png"),0,0,this.width,this.height);
                    break;
                case 2:
                    RenderUtils.drawImage(new ResourceLocation("liquidbounce/background/bg2.png"),0,0,this.width,this.height);
                    break;
                case 3:
                    RenderUtils.drawImage(new ResourceLocation("liquidbounce/background/bg3.png"),0,0,this.width,this.height);
                    break;
                case 4:
                    RenderUtils.drawImage(new ResourceLocation("liquidbounce/background/bg4.png"),0,0,this.width,this.height);
                    break;
            }
//        this. (ResourceLocation(LiquidBounce.CLIENT_NAME.toLowerCase() + "/background.png"));
        //head
        RoundedUtils.drawGradientRoundLR(8, (this.height-47)+5, 100,34, 2F, new Color(0, 0,0, 40), new Color(0,0,0, 40));
        RenderUtils.drawImage(new ResourceLocation("liquidbounce/head/"+head+".png"),12,(this.height-45)+5,30,30);
        RoundedUtils.drawGradientRoundLR(102,(this.height-37)-3,4,4,2F,new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(),255),new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),255));
        Fonts.roboto40.drawString("SinkaUser",47,(this.height-37)+2, Color.WHITE.getRGB(), false);
        Fonts.roboto30.drawString(mc.getSession().getUsername(),47,(this.height-37)+17, Color.WHITE.getRGB(), false);
        //mainmenu
        int left = 0;
        final int j = (int)(this.height / 4.0f + -30.0f)+170;
        RenderUtils.drawRoundRect(this.width / 2 - 15 - 137,j, this.width / 2 - 15+ 167, j + 68,13f,new Color(255, 255, 255,33).getRGB());
        for (int i = 0; i < this.width; ++i) {
            Gui.drawRect(i, this.height - 75, i + 1, this.height - 70, new Color(255,160,200,0).getRGB());
        }
        final ClientSettings autoPraise = (ClientSettings) LiquidBounce.moduleManager.getModule(ClientSettings.class);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1000: {
                this.mc.displayGuiScreen((GuiScreen) new GuiSettings(this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen((GuiScreen) new GuiSelectWorld((GuiScreen) this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen((GuiScreen) new GuiMultiplayer((GuiScreen) this));
                break;
            }
            case 4: {
                this.mc.shutdown();
                break;
            }
            case 100: {
                this.mc.displayGuiScreen((GuiScreen) new GuiAltManager(this));
                break;
            }
            case 101: {
                this.mc.displayGuiScreen((GuiScreen) new GuiServerStatus(this));
                break;
            }
            case 102: {
                this.mc.displayGuiScreen((GuiScreen) new GuiBackground(this));
                break;
            }
            case 103: {
                this.mc.displayGuiScreen((GuiScreen) new GuiModsMenu(this));
                break;
            }
            case 108: {
                this.mc.displayGuiScreen((GuiScreen) new GuiCredits(this));
                break;
            }
            case 1234: {
                head++;
                if(head<1||head>7){
                    head=1;
                }
                break;
            }
            case 12345: {
               MiscUtils.showURL("http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=CqGeBiGiKJTYLpmXv1cd8xU4nfKCbUfj&authKey=ytGkKVcMFnCXklzvVmDvg0DpRaHZWTXuJM9IhyRiYoMwFR1YuIX%2BcApwelQehva8&noverify=0&group_code=289568953");
                break;
            }
//            case 114514: {
//                this.mc.displayGuiScreen((GuiScreen) new CAIDAN(this));
//                break;
//            }
            case 114514: {
                this.mc.displayGuiScreen((GuiScreen) new Info(this));
                break;
            }
        }
    }
    protected void keyTyped(final char typedChar, final int keyCode) {
    }
}
