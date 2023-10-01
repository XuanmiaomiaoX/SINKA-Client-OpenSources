/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name = "Animations", description = "Render items Animations", category = ModuleCategory.CLIENT)
public class Animations extends Module {

    //some ListValue
    public static final ListValue Sword = new ListValue("PresetAnimationSword", new String[]{"Sinka","Normal","Remix","Exhibition","Slide", "Minecraft","Up" ,"Rotate360", "SmoothFloat","Custom",}, "Sinka");

    public static final BoolValue isSlide = new BoolValue("ForceAttackSlide",true);
    public static final ListValue transformFirstPersonRotate = new ListValue("TransformFirstPersonRotate", new String[]{"Rotate1", "Rotate2", "Custom" , "None"}, "Rotate1");

    //item hold in hand incompatible. idk why?
    public static final ListValue doBlockTransformationsRotate = new ListValue("DoBlockTransformationsRotate", new String[]{"Rotate1", "Rotate2", "Custom" , "None"}, "None");

    //modify swingSpeed animation.mode custom will break swing
    public static final ListValue swingMethod = new ListValue("SwingMethod", new String[]{"Swing", "Cancel", "Default"}, "Default");

    //normal item position
    public static final FloatValue itemPosX = new FloatValue("ItemPosX", 0f, -1f, 1f);
    public static final FloatValue itemPosY = new FloatValue("ItemPosY", 0f, -1f, 1f);
    public static final FloatValue itemPosZ = new FloatValue("ItemPosZ", 0f, -1f, 1f);

    //scale all items
    public static final FloatValue Scale = new FloatValue("Scale", 0.4f, 0f, 4f);

    //change Position Blocking Sword
    public static final FloatValue blockPosX = new FloatValue("BlockPosX", 0f, -1f, 1f);
    public static final FloatValue blockPosY = new FloatValue("BlockPosY", 0f, -1f, 1f);
    public static final FloatValue blockPosZ = new FloatValue("BlockPosZ", 0f, -1f, 1f);

    //custom item rotate (idk why i made this)
    public static final IntegerValue customRotate1 = new IntegerValue("CustomRotate1", 0, -360, 360);
    public static final IntegerValue customRotate2 = new IntegerValue("CustomRotate2", 0, -360, 360);
    public static final IntegerValue customRotate3 = new IntegerValue("CustomRotate3", 0, -360, 360);

    //modify item swing and rotate
    public static final BoolValue RotateItems = new BoolValue("RotateItems", false);
    public static final FloatValue SpeedRotate = new FloatValue("SpeedRotate", 1f, 0f, 10f);
    public static final FloatValue SpeedSwing = new FloatValue("SpeedSwing", 1.0f, 0.2f, 10.0f);
    public static final FloatValue Slide = new FloatValue("ForceAttackSlide-Value", 1.0F, 0.1F, 3.0F);
    //custom animation sword
    public static final FloatValue mcSwordPos =  new FloatValue("MCSwordPos", 0.01f, 0, 0.5f);

    //idk why i add this. XD. but it is fun right :)
    public static final BoolValue fakeBlock = new BoolValue("FakeBlock",false);

    //wtf i just block a block!
    public static final BoolValue blockEverything = new BoolValue("BlockEverything", false);


}