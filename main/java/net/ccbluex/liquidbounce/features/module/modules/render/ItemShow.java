package net.ccbluex.liquidbounce.features.module.modules.render;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.*;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.item.*;

import java.awt.*;

@ModuleInfo(name = "ItemShow", description = "Show your held item", category = ModuleCategory.RENDER)
public class ItemShow extends Module {
    private IntegerValue xValue = new IntegerValue("X", 125, 0, 1000);
    private IntegerValue yValue = new IntegerValue("Y", 125, 0, 1000);
    private IntegerValue rectWidthValue = new IntegerValue("RectWidth", 110, 0, 200);
    private IntegerValue rectHeightValue = new IntegerValue("RectHeight", 40, 0, 200);
    private String Item = "";
    @EventTarget
    void onRender2D(Render2DEvent event) {
        RenderUtils.drawBorderedRect(xValue.get(),yValue.get(),xValue.get() + rectWidthValue.get(), yValue.get() + rectHeightValue.get(),1,new Color(0,0,0,255).getRGB(), new Color(0,0,0,140).getRGB());
        if(mc.thePlayer.getHeldItem() != null) {
            Item CurItem = mc.thePlayer.getHeldItem().getItem();
            if(CurItem instanceof ItemSword) {
                Item = "Sword";
            } else {
                if(CurItem instanceof ItemFood || CurItem instanceof ItemBucketMilk) {
                    Item = "Food";
                } else {
                    if(CurItem instanceof ItemBlock) {
                        Item = "Block";
                    } else {
                        if(CurItem instanceof ItemBow) {
                            Item = "Bow";
                        } else {
                            if(CurItem instanceof ItemTool) {
                                Item = "Tool";
                            } else {
                                if(CurItem instanceof ItemPotion) {
                                    Item = "Potion";
                                } else {
                                    if(CurItem instanceof ItemMap || CurItem instanceof ItemEmptyMap) {
                                        Item = "Map";
                                    } else {
                                        Item = "Misc";
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String ItemName = mc.thePlayer.getHeldItem().getDisplayName();
            Fonts.font35.drawString("Name:" + ItemName,xValue.get() + 4, yValue.get() + 5, new Color(255,255,255).getRGB(), true);
            Fonts.font35.drawString("Item:" + Item, xValue.get() + rectWidthValue.get() / 2.8f, yValue.get() + rectHeightValue.get() / 2, new Color(255,255,255).getRGB(), true);
            mc.getRenderItem().renderItemIntoGUI(mc.thePlayer.getHeldItem(), xValue.get() + rectWidthValue.get() / 8, yValue.get() + rectHeightValue.get() / 3);
        }
    }
}
