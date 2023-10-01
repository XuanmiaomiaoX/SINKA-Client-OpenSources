package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.ClientColor;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.eldodebug.RoundedUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;


@Mixin(GuiButton.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiButton extends Gui {
   @Shadow
   public int xPosition;

   @Shadow
   public int yPosition;
   private float hover;
   @Shadow
   public int width;

   @Shadow
   public int height;

   @Shadow
   protected boolean hovered;

   @Shadow
   public boolean enabled;
   private RenderUtils RenderUtil;

   @Shadow
   protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

   @Shadow
   public String displayString;

   private double animation = 0.0;
   private long lastUpdate=System.currentTimeMillis();

   @Overwrite
   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
      final int delta = RenderUtils.deltaTime;
      long time=System.currentTimeMillis();
      if (this.hovered) {
         this.hover += 0.03f * delta;
         if (this.hover >= 2.0f) {
            this.hover = 2.0f;
         }
      }
      else {
         this.hover -= 0.03f * delta;
         if (this.hover < 0.0f) {
            this.hover = 0.0f;
         }
      }
      double percent = EaseUtils.easeInOutQuad(animation);
      Fonts.roboto35.drawCenteredString(this.displayString,this.xPosition + this.width / 2F,this.yPosition+this.height/2F-Fonts.roboto35.getHeight()/2F+1- (int)this.hover, new Color(255,255,255).getRGB(),false);
      RoundedUtils.drawGradientRoundLR(this.xPosition,this.yPosition - (int)this.hover,width,height,4, new Color(0,0,0, 100),new Color(0,0,0, 100));
      double half=this.width / 2.0;
      double center=this.xPosition + half;
      lastUpdate=time;
   }
}
