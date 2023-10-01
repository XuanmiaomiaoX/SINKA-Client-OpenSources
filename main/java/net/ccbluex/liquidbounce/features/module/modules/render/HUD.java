/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Text;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.HanaBiColors;
import net.ccbluex.liquidbounce.utils.render.LnkRenderUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.CLIENT, array = false)
@SideOnly(Side.CLIENT)
public class HUD extends Module {
    public final BoolValue inventoryParticle = new BoolValue("InventoryParticle", false);
    private final BoolValue blurValue = new BoolValue("Blur", true);
   //
    public final BoolValue fontChatValue = new BoolValue("FontChat", false);
    public final BoolValue chatRectValue = new BoolValue("ChatRect", false);
    public final BoolValue chatCombineValue = new BoolValue("ChatCombine", false);
    public final BoolValue chatAnimValue = new BoolValue("ChatAnimation", true);
    public final BoolValue potion = new BoolValue("HUD_Potion", false);
    public final BoolValue hotbar = new BoolValue("HUD_Hotbar", true);
    public final BoolValue vapepotion = new BoolValue("HUD_PotionVape", false);
    //
    public final FloatValue rainbowStart = new FloatValue("RainbowStart",0.41f,0f,1f);
    public final FloatValue rainbowBrightness=new FloatValue("RainbowBrightness",1f,0f,1f);
    public final IntegerValue rainbowSpeed =new IntegerValue("RainbowSpeed",1500,500,7000);
    public final FloatValue rainbowSaturation=new FloatValue("RainbowSaturation",0.7f,0f,1f);
    public final FloatValue rainbowStop=new FloatValue("RainbowStop",0.58f,0f,1f);
    private final Map<Integer, Integer> potionMaxDurations = new HashMap<Integer, Integer>();

    public HUD() {
        setState(true);
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (mc.currentScreen instanceof GuiHudDesigner) return;
        LiquidBounce.hud.render(false);

        ScaledResolution sr = new ScaledResolution(mc);
        float width = sr.getScaledWidth();
        float height = sr.getScaledHeight();

        if (potion.get()) {
            this.renderPotionStatus((int) width, (int) height);
        }

        if (vapepotion.get()) {
            this.renderPotionStatusNew((int) width, (int) height);
        }

        if (hotbar.get() && this.mc.getRenderViewEntity() instanceof EntityPlayer
                && !mc.gameSettings.hideGUI) {
            GameFontRenderer font = Fonts.wqy35;

            long ping = (mc.getCurrentServerData() != null) ? mc.getCurrentServerData().pingToServer : -1;
            RoundedUtils.drawGradientRoundLR(width / 2 - 91+1, height - 19, 180, 20,1,new Color(ClientColor.red, ClientColor.green, ClientColor.blue,ClientColor.air1),new Color(ClientColor.red, ClientColor.green, ClientColor.blue,ClientColor.air1));
            RenderUtils.drawGradientSideways(width / 2 - 91, height - 21, width / 2 - 91+182, height - 20,new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(), 210).getRGB(), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(), 230).getRGB());

            if (mc.thePlayer.inventory.currentItem == 0) {
                RoundedUtils.drawGradientRoundLR(width / 2 - 91+1, height - 19, 20, 20,1, new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(),180), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),100));

            } else {
                RoundedUtils.drawGradientRoundLR((width / 2) - 91 + mc.thePlayer.inventory.currentItem * 20+1, height - 19, 20, 20,1, new Color(ClientColor.colorRedValue.get(), ClientColor.colorGreenValue.get(), ClientColor.colorBlueValue.get(),180), new Color(ClientColor.colorRedValue2.get(), ClientColor.colorGreenValue2.get(), ClientColor.colorBlueValue2.get(),100));
            }
            RenderHelper.enableGUIStandardItemLighting();
            for (int j = 0; j < 9; ++j) {
                int k = (int) (width / 2 - 90 + j * 20 + 2);
                int l = (int) (height - 16 - 3);
                this.customRenderHotbarItem(j, k, l, event.getPartialTicks(), mc.thePlayer);
            }

            GlStateManager.disableBlend();
            GlStateManager.color(1, 1, 1);

            RenderHelper.disableStandardItemLighting();

            GL11.glColor4f(1, 1, 1, 1);
        }
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        LiquidBounce.hud.update();
    }

    @EventTarget
    public void onKey(final KeyEvent event) {
        LiquidBounce.hud.handleKey('a', event.getKey());
    }

    @EventTarget(ignoreCondition = true)
    public void onScreen(final ScreenEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;

        if (getState() && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.getGuiScreen() != null &&
                !(event.getGuiScreen() instanceof GuiChat || event.getGuiScreen() instanceof GuiHudDesigner))
            mc.entityRenderer.loadShader(new ResourceLocation(LiquidBounce.CLIENT_NAME.toLowerCase() + "/blur.json"));
        else if (mc.entityRenderer.getShaderGroup() != null &&
                mc.entityRenderer.getShaderGroup().getShaderGroupName().contains("liquidbounce/blur.json"))
            mc.entityRenderer.stopUseShader();
    }
    public void customRenderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_) {

        GlStateManager.disableBlend();

        ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];

        if (itemstack != null) {
            float f = (float) itemstack.animationsToGo - partialTicks;

            if (f > 0.0F) {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GlStateManager.translate((float) (xPos + 8), (float) (yPos + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(xPos + 8)), (float) (-(yPos + 12)), 0.0F);
            }

            mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, xPos, yPos);

            if (f > 0.0F) {
                GlStateManager.popMatrix();
            }

            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemstack, xPos - 1, yPos);
        }
    }

    Map<Potion, Double> timerMap = new HashMap<Potion, Double>();

    private int x;

    public void renderPotionStatusNew(final int width, final int height) {
        this.x = 0;
        final int length;
        final int tempY = (length = HUD.mc.thePlayer.getActivePotionEffects().size()) * -30;
        if (length != 0) {
            RenderUtils.drawRoundedRect(width - 120, height - 30 + tempY, width - 10, height - 30, 3, new Color(0, 0, 0, 100).getRGB());
        }
        final ArrayList<Integer> needRemove = new ArrayList<Integer>();
        for (final Map.Entry<Integer, Integer> entry : this.potionMaxDurations.entrySet()) {
            if (HUD.mc.thePlayer.getActivePotionEffect(Potion.potionTypes[entry.getKey()]) == null) {
                needRemove.add(entry.getKey());
            }
        }
        for (final int id : needRemove) {
            this.potionMaxDurations.remove(id);
        }
        for (final PotionEffect effect : HUD.mc.thePlayer.getActivePotionEffects()) {
            if (!this.potionMaxDurations.containsKey(effect.getPotionID()) || this.potionMaxDurations.get(effect.getPotionID()) < effect.getDuration()) {
                this.potionMaxDurations.put(effect.getPotionID(), effect.getDuration());
            }
            final Potion potion = Potion.potionTypes[effect.getPotionID()];
            final String PType = I18n.format(potion.getName(), new Object[0]);
            int minutes;
            int seconds;
            try {
                minutes = Integer.parseInt(Potion.getDurationString(effect).split(":")[0]);
                seconds = Integer.parseInt(Potion.getDurationString(effect).split(":")[1]);
            }
            catch (Exception ex) {
                minutes = 0;
                seconds = 0;
            }
            final double total = minutes * 60 + seconds;
            if (!this.timerMap.containsKey(potion)) {
                this.timerMap.put(potion, total);
            }
            if (this.timerMap.get(potion) == 0.0 || total > this.timerMap.get(potion)) {
                this.timerMap.replace(potion, total);
            }
            final int color = Colors.blendColors(new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { new Color(250, 0, 6), new Color(255, 90, 130), new Color(100, 200, 255) }, effect.getDuration() / (1.0f * this.potionMaxDurations.get(effect.getPotionID()))).getRGB();
            final int x1 = (int)((width - 6) * 1.33f);
            final int y1 = (int)((height - 52 - HUD.mc.fontRendererObj.FONT_HEIGHT + this.x + 5) * 1.33f);
            final float rectX = width - 120 + 110.0f * (effect.getDuration() / (1.0f * this.potionMaxDurations.get(effect.getPotionID())));
            if (potion.hasStatusIcon()) {
                GlStateManager.pushMatrix();
                GL11.glDisable(2929);
                GL11.glEnable(3042);
                GL11.glDepthMask(false);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                final int index = potion.getStatusIconIndex();
                final ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
                HUD.mc.getTextureManager().bindTexture(location);
                GlStateManager.scale(0.75, 0.75, 0.75);
                HUD.mc.ingameGUI.drawTexturedModalRect(x1 - 138, y1 + 8, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glEnable(2929);
                GlStateManager.popMatrix();
            }
            final int y2 = height - HUD.mc.fontRendererObj.FONT_HEIGHT + this.x - 38;
            LnkRenderUtils.drawArc(width - 104.75f, y2 + 2.5f, 10.0, new Color(22, 28, 15).getRGB(), 0, 360.0, 3);
            LnkRenderUtils.drawArc(width - 104.75f, y2 + 2.5f, 10.0, color, 0, 360.0f * (effect.getDuration() / (1.0f * this.potionMaxDurations.get(effect.getPotionID()))), 3);
            Fonts.wqy35.drawString(PType.replaceAll("§.", ""), width - 85.0f, y2 - HUD.mc.fontRendererObj.FONT_HEIGHT + 2, -1);
            RenderUtils.drawRect(width - 91.0f, y2 - 3.0f, width - 89.5f, y2 + 10.0f, new Color(255, 255, 255, 100).getRGB());
            Fonts.comfortaa35.drawString(Potion.getDurationString(effect).replaceAll("§.", ""), width - 85.0f, y2 + 3.5f, color);
            this.x -= 30;
        }
    }

    public void renderPotionStatus(int width, int height) {
        x = 0;

        for (PotionEffect effect : (Collection<PotionEffect>) this.mc.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName());
            int minutes = -1;
            int seconds = -2;

            try {
                minutes = Integer.parseInt(potion.getDurationString(effect).split(":")[0]);
                seconds = Integer.parseInt(potion.getDurationString(effect).split(":")[1]);
            } catch (Exception ex) {
                minutes = 0;
                seconds = 0;
            }

            double total = (minutes * 60) + seconds;

            if (!timerMap.containsKey(potion)) {
                timerMap.put(potion, total);
            }

            if (timerMap.get(potion) == 0 || total > timerMap.get(potion)) {
                timerMap.replace(potion, total);
            }

            switch (effect.getAmplifier()) {
                case 0:
                    PType = PType + " I";
                    break;
                case 1:
                    PType = PType + " II";
                    break;
                case 2:
                    PType = PType + " III";
                    break;
                case 3:
                    PType = PType + " IV";
                    break;
                case 4:
                    PType = PType + " V";
                    break;
                case 5:
                    PType = PType + " VI";
                    break;
                case 6:
                    PType = PType + " VII";
                    break;
                case 7:
                    PType = PType + " VIII";
                    break;
                case 8:
                    PType = PType + " IX";
                    break;
                case 9:
                    PType = PType + " X";
                    break;
                case 10:
                    PType = PType + " X+";
                    break;
                default:
                    break;
            }

            int color = HanaBiColors.WHITE.c;

            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                color = HanaBiColors.YELLOW.c;
            } else if (effect.getDuration() < 300) {
                color = HanaBiColors.RED.c;
            } else if (effect.getDuration() > 600) {
                color = HanaBiColors.WHITE.c;
            }

            int x1 = (int) ((width - 6) * 1.33f);
            int y1 = (int) ((height - 52 - this.mc.fontRendererObj.FONT_HEIGHT + x + 5) * 1.33F);

            RenderUtils.drawRect(width - 120, height - 60 + x, width - 10, height - 30 + x,
                    ClientUtils.reAlpha(HanaBiColors.BLACK.c, 0.41f));

            if (potion.hasStatusIcon()) {
                GlStateManager.pushMatrix();

                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDepthMask(false);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(1, 1, 1, 1);
                int index = potion.getStatusIconIndex();
                ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
                mc.getTextureManager().bindTexture(location);
                GlStateManager.scale(0.75, 0.75, 0.75);
                mc.ingameGUI.drawTexturedModalRect(x1 - 138, y1 + 8, 0 + index % 8 * 18, 198 + index / 8 * 18, 18, 18);

                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GlStateManager.popMatrix();
            }

            int y = (height - this.mc.fontRendererObj.FONT_HEIGHT + x) - 38;
            GameFontRenderer font = Fonts.wqy35;
            font.drawString(PType.replaceAll("\247.", ""), (float) width - 91f,
                    y - this.mc.fontRendererObj.FONT_HEIGHT + 1, potion.getLiquidColor());

            Fonts.wqy35.drawString(Potion.getDurationString(effect).replaceAll("\247.", ""),
                    width - 91f, y + 4, ClientUtils.reAlpha(-1, 0.8f));

            x -= 35;
        }
    }
}
