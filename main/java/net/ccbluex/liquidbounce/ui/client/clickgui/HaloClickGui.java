package net.ccbluex.liquidbounce.ui.client.clickgui;

import me.HXC.Utils.RenderUtils2;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.client.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor;
import net.ccbluex.liquidbounce.ui.client.GuiBackground;
import net.ccbluex.liquidbounce.ui.client.GuiIconButton;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.QQLogoHelper;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.List;

public class HaloClickGui extends GuiScreen {

    public static ModuleCategory selectCategory = ModuleCategory.COMBAT;

    //Start
    public static int CategoryIndex = 0;
    public static int ModuleIndex = 0;
    public static int CategoryY;
    public static int ModuleY;

    //Mouse
    private boolean mouse_Down;
    private boolean mouse_Downing;
    private boolean mouse_Down_R;
    private boolean mouse_Downing_R;
    private double slide2, progress = 0;
    private final MSTimer msTimer = new MSTimer();

    private int mX,mY;

    @Override
    public void initGui() {
        CategoryY = height/2 - 120 + 105;
        this.buttonList.add(new GuiIconButton(1, (width/2) + 23, 5, 14, 14, "MVPMusic", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(2, (width/2) + 43, 5, 14, 14, "Hud", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(3, (width/2) + 63, 5, 14, 14, "ColorSettings", new ResourceLocation("liquidbounce/head/omg.png")));
        this.buttonList.add(new GuiIconButton(4, (width/2) - 77, 5, 14, 14, "LoginHud", new ResourceLocation("liquidbounce/head/omg.png")));

        super.initGui();
    }
    public static Color color3() {
        return ClickGUI.colorRainbow.get() ? ColorUtils.rainbow() : new Color(255,255,255);
    }

    public static boolean isY1=true;
    public int ii = 0;
    //底色透明度
    public int air1 = 225;

    public int air2 = 161;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //底色渐变透明
        //rainbow
        if (progress < 1) progress += 0.1 * (1 - partialTicks);
        else progress = 1;
        slide2 = EaseUtils.easeOutBack(progress);

        GlStateManager.translate((1.0 - slide2) * (width / 2.0), (1.0 - slide2) * (height / 2.0), 0);
        GlStateManager.scale(1 * slide2, 1 * slide2, 1 * slide2);

        switch (((ClickGUI) Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).backgroundValue.get()) {
            case "Default":
                drawDefaultBackground();
                break;
            case "Gradient":
                drawGradientRect(-1000, height/2-20, width+1000, height, ColorUtils.reAlpha(color3(), 0).getRGB(),new Color(ii,114,175,255).getRGB());
                break;
            default:
                break;
        }

        mX = mouseX;
        mY = mouseY;


        //Pos
        int midX = width/2;
        int midY = height/2;

        int left = midX - 180;
        int right = midX + 180;
        int top = midY - 120;
        int under = midY + 120;
        String omg = "";
        if(QQLogoHelper.QQNumber.length()>5){
            omg = QQLogoHelper.QQNumber;
        }else{
            omg = "NoLogin";
        }


        //TopGui
        RenderUtils.drawRoundRect((width/2) - 80, 2, (width/2) + 80, 22, 3.0F,new Color(255,255,255).getRGB());
        Fonts.roboto40.drawCenteredString("SinkaClient",width/2 - 21, 8,new Color(70,70,70,255).getRGB(),false);

        //LoginGui
        if(ClickGUI.drawlogin.get()){
            RenderUtils.drawImage(new ResourceLocation("liquidbounce/ClickGuilogo/id2.png"),(width/2) - 77, 5,14,14);
            RenderUtils.drawRoundRect(width - 100, height - 45, width - 100 + 95f, height - 41 + 35f, 5.0F,new Color(255,255,255).getRGB());
            mc.getTextureManager().bindTexture(mc.thePlayer.getLocationSkin());
            RenderUtils.drawScaledCustomSizeModalCircle(width - 96, height - 41, 8f, 8f, 8, 8, 30,30, 64f, 64f);
            RenderUtils.drawScaledCustomSizeModalCircle(width - 96, height - 41, 40f, 8f, 8, 8,30,30, 64f, 64f);
            Fonts.roboto40.drawString(mc.thePlayer.getName(),width - 60, height - 38,new Color(0,0,0,255).getRGB(),false);
            Fonts.roboto35.drawString(omg,width - 60, height - 21,new Color(70,70,70,255).getRGB(),false);
        }else{
            RenderUtils.drawImage(new ResourceLocation("liquidbounce/ClickGuilogo/id.png"),(width/2) - 77, 5,14,14);
        }
        RenderUtils.drawImage(new ResourceLocation("liquidbounce/ClickGuilogo/mu.png"),(width/2) + 23, 5,14,14);
        RenderUtils.drawImage(new ResourceLocation("liquidbounce/ClickGuilogo/hud.png"),(width/2) + 43, 5, 14, 14);
        RenderUtils.drawImage(new ResourceLocation("liquidbounce/ClickGuilogo/op.png"),(width/2) + 63, 5,14,14);

        if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= height - 5 && mouseY >= height - 50)
            mc.displayGuiScreen(new GuiHudDesigner());
        //BG
        RenderUtils.drawRoundRect(midX - 180,midY - 120,midX + 180,midY + 120,17.0f,new Color(ClientColor.red3, ClientColor.green3, ClientColor.blue3).getRGB());
        RenderUtils.drawRoundRect(left,midY - 120,left + 80,midY + 120,17.0f,new Color(ClientColor.red2, ClientColor.green2, ClientColor.blue2).getRGB());
        RenderUtils.drawRoundRect(left+50,midY - 120,left + 80,midY + 120,0.0f,new Color(ClientColor.red2, ClientColor.green2, ClientColor.blue2).getRGB());
        //ImgIcon
        RenderUtils.drawImage(new ResourceLocation("liquidbounce/ClickGuilogo/Logo2.png"),left + 10,top + 25,60,60);


        //Mouse
        if(Mouse.isButtonDown(0) && !mouse_Downing){
            mouse_Down = true;
            mouse_Downing = true;
        }
        if(Mouse.isButtonDown(1) && !mouse_Downing_R){
            mouse_Down_R = true;
            mouse_Downing_R = true;
        }

        //Icon y: top + 95
        //ModuleCat
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.makeScissorBox(left,top + 105 - 8,left + 80,top + 105 + (Fonts.roboto40.FONT_HEIGHT + 15) * 4 - 1);
        int dWheel = Mouse.getDWheel();
        if (mouseIn(left,top,left + 80,under) && Mouse.hasWheel()) {
            if (dWheel < 0 && CategoryIndex < ModuleCategory.values().length) {
                CategoryIndex++;
            }
            if (dWheel > 0 && CategoryIndex > 0) {
                CategoryIndex--;
            }

            CategoryY = (int) AnimationUtils.animate(top + 105 - CategoryIndex * (Fonts.roboto40.FONT_HEIGHT + 15),CategoryY,0.5);
        }

        int catY = CategoryY;

        for(int i = 0;i<ModuleCategory.values().length;i++){
            ModuleCategory category = ModuleCategory.values()[i];

            int fontColor = -1;
            if(category == selectCategory ){
                fontColor = 0;
//                RenderUtils.drawRect(left,catY - 5,left + 80,catY + Fonts.font40.FONT_HEIGHT + 1,new Color(255, 160, 200).getRGB());
                RoundedUtils.drawGradientRoundLR(left, catY - 7, 80, 20, 4.0F, new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), air1), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), air2));
            }

            if(mouseIn(left,catY - 8,left + 80,catY + Fonts.font40.FONT_HEIGHT + 4) &&
                    mouse_Down && selectCategory != category && catY + Fonts.font35.FONT_HEIGHT + 10 > top + 105 && catY < under){
                selectCategory = category;
                ModuleIndex = 0;
                ModuleY = top + 15;
            }

            Fonts.roboto40.drawCenteredString(category.getDisplayName(),left + 40,catY-2,new Color(ClientColor.red4, ClientColor.green4, ClientColor.blue4).getRGB(),false);

            catY += Fonts.font40.FONT_HEIGHT + 10;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        //
        //Module
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.makeScissorBox(0,top + 15,width,under - 15);
        int leftModule = left + 95;

        if (mouseIn(leftModule,top + 15,right - 15,under - 15) & Mouse.hasWheel()) {
            if (dWheel < 0) {
                ModuleIndex++;
            }
            if (dWheel > 0 && ModuleIndex > 0) {
                ModuleIndex--;
            }
            ModuleY = (int) AnimationUtils.animate(top + 15 - 26 * ModuleIndex, ModuleY,0.5);
        }

        int modY = ModuleY;
        for(Module module : LiquidBounce.moduleManager.getModuleInCategory(selectCategory)){

            int valueSize = module.getOpenList()?module.getValues().size():0;

            int addHigh = (Fonts.roboto35.FONT_HEIGHT + 5) * valueSize;
            //BG
//            RenderUtils.drawRoundRect(leftModule,modY,right - 15,modY + 26 + addHigh,2.5f,new Color(250, 250, 250,200).getRGB());
            int color = 0;
            color = new Color(ClientColor.red4, ClientColor.green4, ClientColor.blue4).getRGB();
            //moduleBG
            if(module.getState()){
                RoundedUtils.drawGradientRoundLR((float) leftModule-2, (float) modY+8, 8, 8, 1.5f, new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), air1), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),air2));
            }else{
                RoundedUtils.drawGradientRoundLR((float) leftModule-2, (float) modY+8,8, 8, 1.5f, new Color(180, 180, 180), new Color(180, 180, 180, 100));
            }

            //RenderUtils.drawRoundRect(right - 15,modY,right - 16,modY + 26,0.5f,color);

            if(mouseIn(leftModule,modY,right - 15,modY + 26)){
                if(modY + 26 > top + 15 && modY < under - 15) {
                    if (mouse_Down)
                        module.setState(!module.getState());
                    if(mouse_Down_R && !module.getValues().isEmpty()) {
                        module.setOpenList(!module.getOpenList());
                        LiquidBounce.tipSoundManager.getDisableSound();
                    }
                }
            }

            Fonts.Sfui35.drawString(module.getName(),leftModule + 10,modY + 13 - Fonts.roboto35.getHeight()/2 + 1,color);
            Fonts.roboto25.drawString(module.getDescription(),leftModule + 10,modY + 22 - Fonts.roboto35.getHeight()/2 + 1,color);
            //Values
            int valueY = modY + 28;
            if(module.getOpenList()) {
                for (Value value : module.getValues()) {
                    if(!value.display())
                        continue;
                    Fonts.roboto35.drawString(value.getName(),leftModule + 15,valueY,color);
                    if(value instanceof IntegerValue){
                        IntegerValue integerValue = (IntegerValue) value;

                        String text = value instanceof BlockValue ? BlockUtils.getBlockName(integerValue.get()) + " (" + integerValue.get() + ")" : integerValue.get().toString();

                        float valueOfSlide = drawSlider(integerValue.get(), integerValue.getMinimum(), integerValue.getMaximum(), right - 18 - 100, valueY - 1, 96, new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get()));

                        if (valueOfSlide != integerValue.get())
                            integerValue.set(valueOfSlide);

                        Fonts.roboto35.drawString(text + " - ",right-18-100-2 - Fonts.roboto35.getStringWidth(text + " - "),valueY - 1,color);
                    }
                    if(value instanceof FloatValue){
                        FloatValue floatValue = (FloatValue) value;

                        float valueOfSlide = drawSlider(floatValue.get(), floatValue.getMinimum(), floatValue.getMaximum(), right - 18 - 100, valueY, 96, new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get()));

                        if (valueOfSlide != floatValue.get())
                            floatValue.set(valueOfSlide);

                        Fonts.roboto35.drawString(floatValue.get().toString() + " - ",right-18-100-2 - Fonts.roboto35.getStringWidth(floatValue.get().toString() + " - "),valueY,color);
                    }
                    if(value instanceof ListValue){
                        ListValue list = (ListValue) value;
                        Fonts.roboto35.drawString(list.get()+">",right - 18 -Fonts.roboto35.getStringWidth(list.get()) ,valueY,color);

                        if(mouseIn(leftModule + 15,valueY - 2,right - 15,valueY + 6)) {
                            if (mouse_Down) {
                                list.openList = !list.openList;}
                        }

                        if(list.openList){
                            int listY = valueY;

                            for(String one: list.getValues()){
                                Fonts.roboto35.drawStringWithShadow(one,right + 25,listY,new Color(255,255,255).getRGB());
                                RoundedUtils.drawGradientRoundLR(right + 25,listY,140, 7, 2.5f, new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 100), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 100));
                                if(mouseIn(right + 25,listY - 1,right + 80,listY + Fonts.roboto35.FONT_HEIGHT + 2)){
                                    if(mouse_Down &&
                                            listY + Fonts.roboto35.FONT_HEIGHT + 5 > top && listY < under) {
                                        list.set(one);
                                        LiquidBounce.tipSoundManager.getDisableSound();
                                    }
                                }

                                listY += Fonts.roboto35.FONT_HEIGHT + 5;
                            }
                        }
                    }
                    if(value instanceof BoolValue){
                        int boolColor = -1;
                        if(((BoolValue) value).get()){
                            boolColor = new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get()).getRGB();
                        }else{
                            boolColor = new Color(180,180,180).getRGB();
                        }
                        RenderUtils.drawRoundRect(right - 26,valueY - 2,right - 18,valueY + 6,1.0F,boolColor);

                        if(mouseIn(right - 26,valueY - 2,right - 18,valueY + 6)){
                            if(valueY > top + 15 && valueY < under - 15) {
                                if (mouse_Down) {
                                    ((BoolValue) value).set(!((BoolValue) value).get());
                                    LiquidBounce.tipSoundManager.getDisableSound();
                                }
                            }
                        }
                    }
                    if(value instanceof FontValue){
                        final FontValue fontValue = (FontValue) value;
                        final FontRenderer fontRenderer = fontValue.get();

                        String displayString = "Font: Unknown";

                        if (fontRenderer instanceof GameFontRenderer) {
                            final GameFontRenderer liquidFontRenderer = (GameFontRenderer) fontRenderer;

                            displayString = "Font: " + liquidFontRenderer.getDefaultFont().getFont().getName() + " - " + liquidFontRenderer.getDefaultFont().getFont().getSize();
                        } else if (fontRenderer == Fonts.minecraftFont)
                            displayString = "Font: Minecraft";
                        else {
                            final Object[] objects = Fonts.getFontDetails(fontRenderer);

                            if (objects != null) {
                                displayString = objects[0] + ((int) objects[1] != -1 ? " - " + objects[1] : "");
                            }
                        }

                        Fonts.roboto35.drawString(displayString,right - 18 - Fonts.roboto35.getStringWidth(displayString),valueY,0);

                        if(mouseIn(leftModule + 15,valueY - 2,right - 18,valueY + 6)){
                            if(valueY > top + 15 && valueY < under - 15) {
                                if (mouse_Down) {
                                    final List<FontRenderer> fonts = Fonts.getFonts();

                                    for (int i = 0; i < fonts.size(); i++) {
                                        final FontRenderer font = fonts.get(i);

                                        if (font == fontRenderer) {
                                            i++;

                                            if (i >= fonts.size())
                                                i = 0;

                                            fontValue.set(fonts.get(i));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(value instanceof TextValue){
                        Fonts.roboto35.drawString(((TextValue)value).get(),right - 18 -Fonts.roboto35.getStringWidth(((TextValue)value).get()) ,valueY,new Color(255,255,255).getRGB());
                    }
                    valueY += Fonts.roboto35.FONT_HEIGHT + 5;
                    modY += Fonts.roboto35.FONT_HEIGHT + 5;
                }
            }

            modY += 35;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        //Mouse
        if(mouse_Down){
            mouse_Down = false;
        }
        if(mouse_Down_R){
            mouse_Down_R = false;
        }

        if(!Mouse.isButtonDown(0)){
            mouse_Downing = false;
        }

        if(!Mouse.isButtonDown(1)){
            mouse_Downing_R = false;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    private boolean mouseIn(int x,int y,int x1,int y2){
        return mX > x && mX < x1 && mY > y && mY < y2;
    }
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                mc.displayGuiScreen(new MusicGui());
                break;
            }
            case 2: {
                mc.displayGuiScreen(new GuiHudDesigner());
                break;
            }
            case 3: {
                mc.displayGuiScreen(new ColorManagerGui());
                break;
            }
            case 4: {
                if(ClickGUI.drawlogin.get()){
                    ClickGUI.drawlogin.setValue(false);
                }else{
                    ClickGUI.drawlogin.setValue(true);
                }
                break;
            }
        }
    }
    public float drawSlider(final float value, final float min, final float max, final int x, final int y, final int width, final Color color) {
        final float displayValue = Math.max(min, Math.min(value, max));

        final float sliderValue = (float) x + (float) width * (displayValue - min) / (max - min);

        RenderUtils.drawRoundRect(x, y, x + width, y + 2,0.5f,Integer.MAX_VALUE);
        RenderUtils.drawRoundRect(x, y, sliderValue, y + 2,0.5f,color.getRGB());
        RenderUtils.drawFilledCircle((int) sliderValue, y + 1, 3, color);

        if (mX >= x && mX <= x + width && mY >= y && mY <= y + 3 && Mouse.isButtonDown(0)) {
            double i = MathHelper.clamp_double(((double) mX - (double) x) / ((double) width - 3), 0, 1);

            BigDecimal bigDecimal = new BigDecimal(Double.toString((min + (max - min) * i)));
            bigDecimal = bigDecimal.setScale(2, 4);
            return bigDecimal.floatValue();
        }

        return value;
    }
}
