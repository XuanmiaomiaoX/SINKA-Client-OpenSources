package me.HXC.Modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.*;
import net.ccbluex.liquidbounce.value.*;

@ModuleInfo(
        name = "Animations",
        description = "Block Animation",
        category = ModuleCategory.RENDER
)
class Animations : Module() {
    val presetValue = ListValue(
        "Preset", arrayOf(
            "Exhibition",
            "Exhibition2",
            "SINKAClient",
            "UP",
            "Effect",
            "SIGMA",
            "Jello",
            "Rotate",
            "Old",
            "None",
            "Flux",
            "Mimosa",
            "Mimosa2"
        ),
        "Mimosa"
    )

    /*
    var moveValue = ListValue("InvMode", arrayOf("None", "Slide", "Zoom"), "Slide")
    val timeValue = IntegerValue("InvTime",500,100,500)*/
    var translateX = FloatValue("TranslateX", 0.07f, -0.3f, 0.7f)
    var translateY = FloatValue("TranslateY", -0.18f, -0.3f, 0.7f)
    var translateZ = FloatValue("TranslateZ", 0.13f, -0.1f, 0.5f)
    val itemPosX = FloatValue("ItemPosX", 0.56F, -1.0F, 1.0F)
    val itemPosY = FloatValue("ItemPosY", -0.52F, -1.0F, 1.0F)
    val itemPosZ = FloatValue("ItemPosZ", -0.71999997F, -1.0F, 1.0F)
    var itemScale = FloatValue("ItemScale", 0.4f, 0.0f, 2.0f)
    var swingAnim = BoolValue("SwingAnim", false)
    var noswing = BoolValue("NoSwing", false)
    val swingSpeed = IntegerValue("SwingSpeed", -2, -10, 8);
    override val tag: String
        get() = presetValue.get()
}