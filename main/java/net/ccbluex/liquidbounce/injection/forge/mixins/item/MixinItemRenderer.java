/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.init.Items;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinItemRenderer {

    float delay = 0.0F;

    float keep = 0f;
    MSTimer rotateTimer = new MSTimer();

    @Shadow
    private float prevEquippedProgress;

    @Shadow
    private float equippedProgress;


    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    protected abstract void rotateArroundXAndY(float angle, float angleY);

    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);

    @Shadow
    protected abstract void transformFirstPersonItem(float equipProgress, float swingProgress);

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks);

    @Shadow
    protected abstract void doBlockTransformations();

    @Shadow
    protected abstract void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void doItemUsedTransformations(float swingProgress);

    @Shadow
    public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);

    @Shadow
    protected abstract void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress);


    private void func_178096_b(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.2F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    private void slide(float  p_178096_1_, float var9) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.2F, 0.0F);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var11 = MathHelper.sin(var9 * var9 * (float) Math.PI);
        float var12 = MathHelper.sin(MathHelper.sqrt_float(var9) * (float) Math.PI);
        GlStateManager.rotate(var11 * 0.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var12 * 0.0F, 0.0F, 0.0f, 1.0F);
        GlStateManager.rotate(var12 * -40.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }
    private void up(float idk, float idc) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, idk * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(idc * idc * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(idc) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 0.0F, 1.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }
    private void func_178103_d() {
        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
        GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
    }
    MSTimer msTimer =new MSTimer();
    /**
     * @author CCBlueX
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks){
        final KillAura killAura1 = (KillAura) LiquidBounce.moduleManager.getModule(KillAura.class);
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated(Animations.itemPosX.get().doubleValue(), Animations.itemPosY.get().doubleValue(), Animations.itemPosZ.get().doubleValue());
        }
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP) abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated(Animations.itemPosX.get().doubleValue(), Animations.itemPosY.get().doubleValue(), Animations.itemPosZ.get().doubleValue());
        }

        if (this.itemToRender != null) {
            final KillAura killAura = (KillAura) LiquidBounce.moduleManager.getModule(KillAura.class);

            boolean canBlockEverything = LiquidBounce.moduleManager.getModule(Animations.class).getState() && Animations.blockEverything.get() && killAura.getTarget() != null
                    && (itemToRender.getItem() instanceof ItemBucketMilk || itemToRender.getItem() instanceof ItemFood
                    || itemToRender.getItem() instanceof ItemPotion || itemToRender.getItem().equals(Items.stick));

            if (this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap) {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.getItemInUseCount() > 0
                    || (itemToRender.getItem() instanceof ItemSword && killAura.getBlockingStatus())
                    || (itemToRender.getItem() instanceof ItemSword && LiquidBounce.moduleManager.getModule(Animations.class).getState()
                    && Animations.fakeBlock.get() && killAura.getTarget() != null) || canBlockEverything) {

                EnumAction enumaction = (killAura.getBlockingStatus() || canBlockEverything) ? EnumAction.BLOCK : this.itemToRender.getItemUseAction();

                switch (enumaction) {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()){
                            if(Animations.swingMethod.get().equalsIgnoreCase("Swing") ||
                                    Animations.swingMethod.get().equalsIgnoreCase("Default")){
                                this.performDrinking(abstractclientplayer, partialTicks);
                            }
                            this.transformFirstPersonItem(f, f1);
                            if (Animations.RotateItems.get()) {
                                ItemRotate();
                            }
                        }else{
                            this.performDrinking(abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, f1);
                        }
                        break;
                    case BLOCK:
                        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()) {
                            GL11.glTranslated(Animations.blockPosX.get().doubleValue(), Animations.blockPosY.get().doubleValue(), Animations.blockPosZ.get().doubleValue());
                            final String z = Animations.Sword.get();
                            switch (z) {
                                case "Normal": {
                                    this.transformFirstPersonItem(f * Animations.Slide.get(), f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.doBlockTransformations();
                                    GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Remix": {
                                    transformFirstPersonItem(f * Animations.Slide.get(), 0.01f);
                                    if (Animations.RotateItems.get())
                                        rotateItemAnim();
                                    this.func_178103_d();
                                    float f4 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.83f);
                                    GlStateManager.translate(-0.5f, 0.2f, 0.2f);
                                    GlStateManager.rotate(-f4 * 0.0f, 0.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(-f4 * 43.0f, 58.0f, 23.0f, 45.0f);
                                    if (Animations.RotateItems.get())
                                        rotateItemAnim();
                                    break;
                                }
                                case "Sinka": {
                                    float var9 = MathHelper.sin(MathHelper.sqrt_float(this.mc.thePlayer.getSwingProgress(partialTicks)) * 3.1415927F);
                                    GL11.glTranslated(-0.03D, 0.23D, 0.0D);
                                    this.transformFirstPersonItem(f * Animations.Slide.get(), 0.005f);
                                    GlStateManager.rotate(-var9 * 17.0F /1.0F, var9 / 2.0F, 1.0F, 4.0F);
                                    GlStateManager.rotate(-var9 * 35.0F, 1.0F, var9 / 3.0F, -0.0F);
                                    this.func_178103_d();
                                    break;
                                }
                                case "Exhibition": {
                                    float var9 = MathHelper.sin(MathHelper.sqrt_float(this.mc.thePlayer.getSwingProgress(partialTicks)) * 3.1415927F);
                                    GL11.glTranslated(-0.04D, 0.13D, 0.0D);
                                    this.transformFirstPersonItem(f * Animations.Slide.get(), 0.0f);
                                    GlStateManager.rotate(-var9 * 40.0F / 2.0F, var9 / 2.0F, 1.0F, 4.0F);
                                    GlStateManager.rotate(-var9 * 30.0F, 1.0F, var9 / 3.0F, -0.0F);
                                    this.func_178103_d();
                                    break;
                                }
                                case "Minecraft": {
                                    this.slide(f * Animations.Slide.get(), Animations.mcSwordPos.get());
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }

                                case "Slide": {
                                    this.slide(f * Animations.Slide.get(), 17.0F);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                case "Up": {
                                    this.up(f * Animations.Slide.get(), f1);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                //i will add more custom animation if i have time
                                case "SmoothFloat": {
                                    this.func_178096_b(0.0f, 0.95f);
                                    GlStateManager.rotate(this.delay, 1.0F, 0.0F, 2.0F);
                                    if (this.rotateTimer.hasTimePassed(1)) {
                                        ++this.delay;
                                        this.delay = this.delay + Animations.SpeedRotate.get();
                                        this.rotateTimer.reset();
                                    }
                                    if (this.delay > 360.0F) {
                                        this.delay = 0.0F;
                                    }
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }
                                //don't mind this mode. i just make it for fun
                                case "Rotate360": {
                                    this.func_178096_b(0.0f, 0.95f);
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GlStateManager.rotate(this.delay, 1.0F, 0.0F, 2.0F);
                                    if (this.rotateTimer.hasTimePassed(1)) {
                                        ++this.delay;
                                        this.delay = this.delay + Animations.SpeedRotate.get();
                                        this.rotateTimer.reset();
                                    }
                                    if (this.delay > 360.0F) {
                                        this.delay = 0.0F;
                                    }
                                    if (Animations.RotateItems.get()) {
                                        ItemRotate1();
                                    }
                                    break;
                                }

                            }
                        } else {
                            this.transformFirstPersonItem(f * -0.3F, f1);
                            this.doBlockTransformations();
                            GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                        }
                        break;
                    case BOW:
                        if(LiquidBounce.moduleManager.getModule(Animations.class).getState()){
                            this.transformFirstPersonItem(f, f1);
                            if (Animations.RotateItems.get()) {
                                ItemRotate();
                            }
                            if(Animations.swingMethod.get().equalsIgnoreCase("Swing") ||
                                    Animations.swingMethod.get().equalsIgnoreCase("Cancel")){
                                this.doBowTransformations(partialTicks, abstractclientplayer);
                            }
                            if (Animations.RotateItems.get()) {
                                ItemRotate1();
                            }
                        }

                }
            } else {
                if (!Animations.swingMethod.get().equalsIgnoreCase("Swing")) {
                    this.doItemUsedTransformations(f1);
                    this.transformFirstPersonItem(f, f1);
                }else if (Animations.RotateItems.get()) {
                    this.transformFirstPersonItem(f, f1);
                    ItemRotate();
                } else if (Animations.RotateItems.get() && mc.gameSettings.keyBindUseItem.pressed) {
                    this.doBlockTransformations();
                    ItemRotate1();
                }else{
                    this.transformFirstPersonItem(f, f1);
                }
            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();

        if (LiquidBounce.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated(-Animations.itemPosX.get().doubleValue(), -Animations.itemPosY.get().doubleValue(), -Animations.itemPosZ.get().doubleValue());
        }
    }

    //undone
    /*private void customTest(float p_doItemUsedTransformations_1_) {
        float f = -0.4F + Animations.swingPosX.get() * MathHelper.sin(MathHelper.sqrt_float(p_doItemUsedTransformations_1_) * 3.1415927F);
        float f1 = 0.2F + Animations.swingPosY.get()  * MathHelper.sin(MathHelper.sqrt_float(p_doItemUsedTransformations_1_) * 3.1415927F * 2.0F);
        float f2 = -0.2F + Animations.swingPosZ.get()  * MathHelper.sin(p_doItemUsedTransformations_1_ * 3.1415927F);
        GlStateManager.translate(f, f1, f2);
    }*/


    private void ItemRotate() {
        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Rotate1")) {
            GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
        }
        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Rotate2")) {
            GlStateManager.rotate(this.delay, 1.0F, 1.0F, 0.0F);
        }

        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Custom")) {
            GlStateManager.rotate(this.delay, Animations.customRotate1.get().floatValue(), Animations.customRotate2.get().floatValue(), Animations.customRotate3.get().floatValue());
        }

        if (this.rotateTimer.hasTimePassed(1)) {
            ++this.delay;
            this.delay = this.delay + Animations.SpeedRotate.get();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0F) {
            this.delay = 0.0F;
        }
    }

    private void ItemRotate1() {
        if (Animations.doBlockTransformationsRotate.get().equalsIgnoreCase("Rotate1")) {
            GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
        }
        if (Animations.doBlockTransformationsRotate.get().equalsIgnoreCase("Rotate2")) {
            GlStateManager.rotate(this.delay, 1.0F, 1.0F, 0.0F);
        }
        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Custom")) {
            GlStateManager.rotate(this.delay, Animations.customRotate1.get().floatValue(), Animations.customRotate2.get().floatValue(), Animations.customRotate3.get().floatValue());
        }

        if (this.rotateTimer.hasTimePassed(1)) {
            ++this.delay;
            this.delay = this.delay + Animations.SpeedRotate.get();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0F) {
            this.delay = 0.0F;
        }
    }
    private void rotateItemAnim() {
        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("RotateY")) {
            GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
        }
        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("RotateXY")) {
            GlStateManager.rotate(this.delay, 1.0F, 1.0F, 0.0F);
        }

        if (Animations.transformFirstPersonRotate.get().equalsIgnoreCase("Custom")) {
            GlStateManager.rotate(this.delay, Animations.customRotate1.get(), Animations.customRotate2.get(), Animations.customRotate3.get());
        }

        if (this.rotateTimer.hasTimePassed(1)) {
            ++this.delay;
            this.delay = this.delay + Animations.SpeedRotate.get();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0F) {
            this.delay = 0.0F;
        }
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void renderFireInFirstPerson(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind) LiquidBounce.moduleManager.getModule(AntiBlind.class);

        if (antiBlind.getState() && antiBlind.getFireEffect().get()) {
            //vanilla's method
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            callbackInfo.cancel();
        }
    }
}