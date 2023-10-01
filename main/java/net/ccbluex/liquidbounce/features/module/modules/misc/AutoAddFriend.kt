package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.server.S02PacketChat
import java.util.regex.Pattern

@ModuleInfo(name = "AutoAddFriend", description = "Add-SendTextFriend", category = ModuleCategory.MISC)
class AutoAddFriend : Module(){
    private val text1 = TextValue("Text1", "宣传语1")
    private val text2 = TextValue("Text2", "宣传语2")
    private val text3 = TextValue("Text3", "宣传语3")
    private val savefriend = BoolValue("SaveFriend",false)
    private val tipsadd = BoolValue("TipsAdd",true)
    private val text1s = BoolValue("Text1Start",false)
    private val text2s = BoolValue("Text2Start",false)
    private val text3s = BoolValue("Text3Start",true)
    private val playerName: MutableList<String> = ArrayList()
    private var atext = ""
    private var atext2 = ""
    private var atext3 = ""
    //花雨庭天坑专用 本Kt码子可以正则表达式匹配你自己配置的人 然后添加 不打自己人 你只需要填好Text1这些参数即可

    override fun onEnable() {
        atext = ""
        atext2 = ""
        atext3 = ""
        playerName.clear()
        LiquidBounce.fileManager.friendsConfig.clearFriends() //开启后自动删除所有之前添加过的(当然觉得不需要的吧这代码去了就行)
    }

    override fun onDisable() {
        atext = ""
        atext2 = ""
        atext3 = ""
        playerName.clear()
        LiquidBounce.fileManager.friendsConfig.clearFriends() //关闭后自动删除所有之前添加过的(当然觉得不需要的吧这代码去了就行)
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is S02PacketChat) {
            atext = text1.get()
            atext2 = text2.get()
            atext3 = text3.get()
            val matcher = Pattern.compile("(.*?) (.*?) (.*?) ? $atext").matcher(packet.chatComponent.unformattedText)
            val matcher2 = Pattern.compile("(.*?) (.*?) (.*?) ? $atext2").matcher(packet.chatComponent.unformattedText)
            val matcher3 = Pattern.compile("(.*?) (.*?) (.*?) ? $atext3").matcher(packet.chatComponent.unformattedText)
            if (text1s.get()) {
                if (matcher.find()) {
                    if (!playerName.contains(matcher.group(2).trim())){ //判断是否已经添加过自己人
                    playerName.add(matcher.group(2).trim())
                    LiquidBounce.fileManager.friendsConfig.addFriend(matcher.group(2).trim())
                    if(savefriend.get()) LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.friendsConfig)
                    if(tipsadd.get()) ClientUtils.displayChatMessage("§8[§c§lCoolSense提醒您§8]§c§d检测到同款用户ID已经添加白名单：" + matcher.group(2).trim())
                    }
                }
            }
            if (text2s.get()) {
                if (matcher2.find()) {
                    if (!playerName.contains(matcher2.group(2).trim())){ //判断是否已经添加过自己人
                    playerName.add(matcher2.group(2).trim())
                    LiquidBounce.fileManager.friendsConfig.addFriend(matcher2.group(2).trim())
                    if(savefriend.get()) LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.friendsConfig)
                    if(tipsadd.get()) ClientUtils.displayChatMessage("§8[§c§lCoolSense提醒您§8]§c§d检测到同款用户ID已经添加白名单：" + matcher2.group(2).trim())
                    }
                }
            }
            if (text3s.get()) {
                if (matcher3.find()) {
                    if (!playerName.contains(matcher3.group(2).trim())){ //判断是否已经添加过自己人
                    playerName.add(matcher3.group(2).trim())
                    LiquidBounce.fileManager.friendsConfig.addFriend(matcher3.group(2).trim())
                    if(savefriend.get()) LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.friendsConfig)
                    if(tipsadd.get()) ClientUtils.displayChatMessage("§8[§c§lCoolSense提醒您§8]§c§d检测到同款用户ID已经添加白名单：" + matcher3.group(2).trim())
                    }
                }
            }
        }
    }
}