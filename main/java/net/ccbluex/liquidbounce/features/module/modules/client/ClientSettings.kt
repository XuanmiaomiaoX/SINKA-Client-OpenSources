//Mimosa
package net.ccbluex.liquidbounce.features.module.modules.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.features.module.modules.movement.NewStrafe
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.features.module.modules.movement.VulCanSpeed
import net.ccbluex.liquidbounce.features.module.modules.render.Animations
import net.ccbluex.liquidbounce.features.module.modules.render.Particles
import net.ccbluex.liquidbounce.ui.client.clickgui.ClientInfoGUI
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.ServerUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.network.play.server.S45PacketTitle


@ModuleInfo(name = "ClientSettings", description = "Client Settings", category = ModuleCategory.CLIENT)
class ClientSettings : Module(){
    init {
        state = true
        array = false
    }
    var kill=0
    var syncEntity: EntityLivingBase? = null
    val modeValue= ListValue("Server", arrayOf("BlocksMC","Hypixel","None"), "BlocksMC")
    val MusicValue= ListValue("MvpMusic", arrayOf("On-My-Own", "Liquid","Disorder","dashstar", "GOODRAGE","Chronomia","OLDGENESIS","Drop"), "Liquid")
    val moduleNoti = BoolValue("Module State", false)
    private val Kill = BoolValue("Kill", true)
    private val killWarn = BoolValue("Killwarn", false)
    var Music = BoolValue("Music", true)

    var win = false
    val sound = BoolValue("Sound", true)
    val isInfo = BoolValue("NoInfo",false)
    private val Win = BoolValue("WinWarn", true)
    private val died = BoolValue("Died and Sound", true)
    private val Play = BoolValue("Play", true)
    var yanZhen = BoolValue("AutoServerMode", true)
    private val autofalse = BoolValue("AutoDisable", true)
    private val autoclear = BoolValue("Auto Clear Kill", true)
    private val autoSinka = BoolValue("AutoSinkaADV", false)
    var bgValue = IntegerValue("BackGround", 1, 1, 4)
    var isNoti = false
    var isGui = false

    private var canWarn = true
    private var canWarn1 = true

    override fun onDisable() {
        canWarn = true
        canWarn1 = true
    }
    var i = 0
    //低血量警告and died
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if(yanZhen.get()){
            if(ServerUtils.getRemoteIp().equals("hypixel.net")){
                modeValue.value="Hypixel"
            }else if(ServerUtils.getRemoteIp().equals("blocksmc.com")){
                modeValue.value="BlocksMC"
            }else{
                modeValue.value="None"
            }
        }
        //Draw InfoGUI
        if (ClientColor.ColorMode.get() == "White" && ClientColor.rColorMode.get() == "White") {
            ClientColor.rColorMode.value = "Black"
        }
        if (ClientColor.ColorMode.get() == "White" && ClientColor.clickMode.get() == "White") {
            ClientColor.clickMode.value = "Grey"
        }
        if(!isInfo.get()){
            if(!isGui){
                mc.displayGuiScreen(ClientInfoGUI())
                isGui=true
            }
        }

        if(!isNoti&&!(LiquidBounce.moduleManager.getModule(ClientSettings::class.java)as ClientSettings).moduleNoti.get()){
            LiquidBounce.hud.addNotification(Notification("Module State:Off","You won't receive module prompts!",NotifyType.INFO,10000))
            isNoti=true
        }
        if(isNoti&&(LiquidBounce.moduleManager.getModule(ClientSettings::class.java)as ClientSettings).moduleNoti.get()){
            LiquidBounce.hud.addNotification(Notification("Module State:On","You will receive a module prompt!",NotifyType.INFO,10000))
            isNoti=false
        }
//        Minecraft.getMinecraft().itemRenderer.resetEquippedProgress()
        //itemAnim
      if(died.get()){
           if (mc.thePlayer.isDead) {

               if (canWarn1) {
                   LiquidBounce.hud.addNotification(
                       Notification("Warning", "You Died!", NotifyType.DROP, 5000))
                   canWarn1 = false
               }
           } else {
               canWarn1 = true
           }
        }
        when (ClientColor.rAlphaMode.get()) {
            "100" -> ClientColor.air1 = 255
            "70" -> ClientColor.air1 = 190
            "50" -> ClientColor.air1 = 150
            "25" -> ClientColor.air1 = 50
        }
        when (ClientColor.clickMode.get()) {
            "White" -> {
                ClientColor.red2 = 250
                ClientColor.green2 = 250
                ClientColor.blue2 = 250
                ClientColor.red3 = 255
                ClientColor.green3 = 255
                ClientColor.blue3 = 255
                ClientColor.red4 = 80
                ClientColor.green4 = 80
                ClientColor.blue4 = 80
            }

            "Grey" -> {
                ClientColor.red2 = 80
                ClientColor.green2 = 80
                ClientColor.blue2 = 80
                ClientColor.red3 = 75
                ClientColor.green3 = 75
                ClientColor.blue3 = 75
                ClientColor.red4 = 250
                ClientColor.green4 = 250
                ClientColor.blue4 = 250
            }
        }
        when (ClientColor.rColorMode.get()) {
            "White" -> {
                ClientColor.red = 255
                ClientColor.green = 255
                ClientColor.blue = 255
                ClientColor.red1 = 70
                ClientColor.green1 = 70
                ClientColor.blue1 = 70
                ClientColor.air2 = 180
                ClientColor.WB = false
            }

            "Black" -> {
                ClientColor.red = 0
                ClientColor.green = 0
                ClientColor.blue = 0
                ClientColor.red1 = 250
                ClientColor.green1 = 250
                ClientColor.blue1 = 250
                ClientColor.air2 = 255
                ClientColor.WB = true
            }

            "Grey" -> {
                ClientColor.red = 40
                ClientColor.green = 40
                ClientColor.blue = 40
                ClientColor.red1 = 250
                ClientColor.green1 = 250
                ClientColor.blue1 = 250
                ClientColor.air2 = 255
                ClientColor.WB = true
            }
        }
        when (ClientColor.ColorMode.get()) {
            "Pink-Blue" -> {
                ClientColor.colorRedValue.value = 255
                ClientColor.colorGreenValue.value = 160
                ClientColor.colorBlueValue.value = 200
                ClientColor.colorRedValue2.value = 117
                ClientColor.colorGreenValue2.value = 224
                ClientColor.colorBlueValue2.value = 255
            }

            "Purple-Blue" -> {
                ClientColor.colorRedValue.value = 150
                ClientColor.colorGreenValue.value = 243
                ClientColor.colorBlueValue.value = 255
                ClientColor.colorRedValue2.value = 215
                ClientColor.colorGreenValue2.value = 161
                ClientColor.colorBlueValue2.value = 255
            }

            "Yellow-Blue" -> {
                ClientColor.colorRedValue.value = 113
                ClientColor.colorGreenValue.value = 255
                ClientColor.colorBlueValue.value = 253
                ClientColor.colorRedValue2.value = 252
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 0
            }

            "Green-Blue" -> {
                ClientColor.colorRedValue.value = 202
                ClientColor.colorGreenValue.value = 255
                ClientColor.colorBlueValue.value = 153
                ClientColor.colorRedValue2.value = 100
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 231
            }

            "RedPink" -> {
                ClientColor.colorRedValue.value = 255
                ClientColor.colorGreenValue.value = 103
                ClientColor.colorBlueValue.value = 141
                ClientColor.colorRedValue2.value = 255
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 255
            }

            "Pink" -> {
                ClientColor.colorRedValue.value = 255
                ClientColor.colorGreenValue.value = 160
                ClientColor.colorBlueValue.value = 200
                ClientColor.colorRedValue2.value = 255
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 255
            }

            "Blue" -> {
                ClientColor.colorRedValue.value = 52
                ClientColor.colorGreenValue.value = 243
                ClientColor.colorBlueValue.value = 250
                ClientColor.colorRedValue2.value = 255
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 255
            }

            "Green" -> {
                ClientColor.colorRedValue.value = 200
                ClientColor.colorGreenValue.value = 255
                ClientColor.colorBlueValue.value = 153
                ClientColor.colorRedValue2.value = 255
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 255
            }

            "Red" -> {
                ClientColor.colorRedValue.value = 255
                ClientColor.colorGreenValue.value = 2
                ClientColor.colorBlueValue.value = 5
                ClientColor.colorRedValue2.value = 255
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 255
            }

            "Purple" -> {
                ClientColor.colorRedValue.value = 195
                ClientColor.colorGreenValue.value = 141
                ClientColor.colorBlueValue.value = 255
                ClientColor.colorRedValue2.value = 255
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 255
            }

            "White" -> {
                ClientColor.colorRedValue.value = 255
                ClientColor.colorGreenValue.value = 255
                ClientColor.colorBlueValue.value = 255
                ClientColor.colorRedValue2.value = 255
                ClientColor.colorGreenValue2.value = 255
                ClientColor.colorBlueValue2.value = 255
            }

            "custom" -> {}
        }
        var killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
        if(Animations.isSlide.get()&&killAura.target!!.hurtTime==10){
            Minecraft.getMinecraft().itemRenderer.resetEquippedProgress()
        }
     }
    //((ClientSettings) LiquidBounce.INSTANCE.moduleManager.get(ClientSettings.class)).getBgValue()

    @EventTarget
    fun onPacket(event: PacketEvent) {
        //clear
        if (autoclear.get()) {
            if (kill >99) {
                kill = 0
                LiquidBounce.tipSoundManager.enableSound.asyncPlay()
                LiquidBounce.hud.addNotification(Notification("Cleared!", "The score has been cleared", NotifyType.INFO, 9000))
            }
        }
        if(killWarn.get()){
            if(kill==3){
                kill=4
                LiquidBounce.hud.addNotification(Notification("3Kill!", "You killed 3 players", NotifyType.INFO, 9000))

            }
            if(kill==6){
                kill=7
                LiquidBounce.hud.addNotification(Notification("5Kill!", "You killed 5 players", NotifyType.INFO, 9000))

            }
            if(kill==12){
                kill=13
                LiquidBounce.hud.addNotification(Notification("10Kill!", "You killed 10 players", NotifyType.INFO, 9000))

            }
            if(kill==22){
                kill=23
                LiquidBounce.tipSoundManager.enableSound.asyncPlay()
                LiquidBounce.hud.addNotification(Notification("20Kill!", "You killed 20 players", NotifyType.INFO, 9000))

            }
            if(kill==50){
                kill=51
                LiquidBounce.tipSoundManager.enableSound.asyncPlay()
                LiquidBounce.hud.addNotification(Notification("50Kill!", "You killed 50 players", NotifyType.INFO, 9000))

            }
        }
        //kill
        if(Kill.get()){
            if (syncEntity != null && syncEntity!!.isDead ) {
                kill++
                if(sound.get()){
                    LiquidBounce.tipSoundManager.startup.asyncPlay()
                }
                LiquidBounce.hud.addNotification(Notification("Kill!", "You killed a player.", NotifyType.KILL, 4000))
                syncEntity = null
            }
        }
        //AutoDisable
        if(autofalse.get()){
            val packet = event.packet
            if(packet is S08PacketPlayerPosLook) {
                LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state = false
                LiquidBounce.moduleManager.getModule(VulCanSpeed::class.java)!!.state = false
                LiquidBounce.moduleManager.getModule(NewStrafe::class.java)!!.state = false
                LiquidBounce.moduleManager.getModule(Fly::class.java)!!.state = false
            }
        }
        val packet = event.packet
        if(packet is S08PacketPlayerPosLook) {
           win=false
        }

        when(modeValue.get()) {
            "None" -> {

            }
            "BlocksMC" -> {
                if (Win.get()) {
                    if (event.packet is S02PacketChat && event.packet.chatComponent.unformattedText.contains("winning")) {
                        if (autofalse.get()) {
                            LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                        }//win and died
                        win = true
                        music()
                        LiquidBounce.tipSoundManager.startup.asyncPlay()
                        LiquidBounce.hud.addNotification(
                            Notification(
                                "Victory!",
                                "You won this competition!You killed " + kill + " Player",
                                NotifyType.ALL,
                                9000
                            )
                        )
                        kill = 0
                        if (autoSinka.get()) {
                            mc.thePlayer.sendChatMessage("[SINKA] Keep trying!" + LiquidBounce.CLIENTQQ)
                        }
                    } else {
                        val packet = event.packet
                        if (packet is S45PacketTitle) {
                            val title = packet.message.formattedText
                            if (title.contains("You are now a spectator")) {
                                if (autofalse.get()) {
                                    LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                                }
                                if (sound.get()) {
                                    LiquidBounce.tipSoundManager.died.asyncPlay()
                                }
                                if (autoSinka.get()) {
                                    mc.thePlayer.sendChatMessage("[SINKA] O!I'm dead" + LiquidBounce.CLIENTQQ)
                                }
                                LiquidBounce.hud.addNotification(
                                    Notification(
                                        "Keep trying",
                                        "You killed " + kill + " Player",
                                        NotifyType.LOST,
                                        9000
                                    )
                                )
                                kill = 0
                            }
                        }
                    }
                }
                //PlayGame
                if (Play.get()) {
                    if (event.packet is S02PacketChat && event.packet.chatComponent.unformattedText.contains(mc.thePlayer.name) && event.packet.chatComponent.unformattedText.contains(
                            "wants"
                        )
                    ) {
                        if (autofalse.get()) {
                            LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                        }
                        kill = 0
                        LiquidBounce.hud.addNotification(
                            Notification(
                                "Play..",
                                "You are the Top, Defeat all.",
                                NotifyType.PLAY,
                                9000
                            )
                        )
                        if (autoSinka.get()) {
                            mc.thePlayer.sendChatMessage("[SINKA] I like this!" + LiquidBounce.CLIENTQQ)
                        }
                    }
                    if (event.packet is S02PacketChat && event.packet.chatComponent.unformattedText.contains(mc.thePlayer.name) && event.packet.chatComponent.unformattedText.contains(
                            "join"
                        )
                    ) {
                        if (autofalse.get()) {
                            LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                        }
                        kill = 0
                        LiquidBounce.hud.addNotification(
                            Notification(
                                "Play..",
                                "You are the Top, Defeat all.",
                                NotifyType.PLAY,
                                9000
                            )
                        )
                        if (autoSinka.get()) {
                            mc.thePlayer.sendChatMessage("[SINKA] I like this!" + LiquidBounce.CLIENTQQ)
                        }
                    }
                }
            }

            "Hypixel" -> {
                if (Win.get()) {
                    if (event.packet is S02PacketChat && event.packet.chatComponent.unformattedText.contains("Winner")) {
                        if (autofalse.get()) {
                            LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                        }
                        win = true
                        music()
                        LiquidBounce.tipSoundManager.startup.asyncPlay()
                        LiquidBounce.hud.addNotification(
                            Notification(
                                "Victory!",
                                "You killed " + kill + " Player",
                                NotifyType.ALL,
                                9000
                            )
                        )
                        kill = 0
                        if (autoSinka.get()) {
                            mc.thePlayer.sendChatMessage("[SINKA] Keep trying!" + LiquidBounce.CLIENTQQ)
                        }
                    } else {
                        val packet = event.packet
                        if (packet is S45PacketTitle) {
                            val title = packet.message.formattedText
                            if (title.contains("You are now a spectator")) {
                                if (autofalse.get()) {
                                    LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                                }
                                if (autoSinka.get()) {
                                    mc.thePlayer.sendChatMessage("[SINKA] O!I'm dead" + LiquidBounce.CLIENTQQ)
                                }
                                LiquidBounce.hud.addNotification(
                                    Notification(
                                        "Keep trying",
                                        "You killed " + kill + " Player",
                                        NotifyType.LOST,
                                        9000
                                    )
                                )
                                kill = 0
                            }
                        }
                    }
                }
                //PlayGame
                if (Play.get()) {
                    if (event.packet is S02PacketChat && event.packet.chatComponent.unformattedText.contains(mc.thePlayer.name) && event.packet.chatComponent.unformattedText.contains(
                            "wants"
                        )
                    ) {
                        if (autofalse.get()) {
                            LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                        }
                        kill = 0
                        LiquidBounce.hud.addNotification(
                            Notification(
                                "Play..",
                                "You are the Top, Defeat all.",
                                NotifyType.PLAY,
                                9000
                            )
                        )
                        if (autoSinka.get()) {
                            mc.thePlayer.sendChatMessage("[SINKA] I like this!" + LiquidBounce.CLIENTQQ)
                        }
                    }
                    if (event.packet is S02PacketChat && event.packet.chatComponent.unformattedText.contains(mc.thePlayer.name) && event.packet.chatComponent.unformattedText.contains(
                            "join"
                        )
                    ) {
                        if (autofalse.get()) {
                            LiquidBounce.moduleManager.getModule(KillAura::class.java)!!.state = false
                        }
                        kill = 0
                        LiquidBounce.hud.addNotification(
                            Notification(
                                "Play..",
                                "You are the Top, Defeat all.",
                                NotifyType.PLAY,
                                9000
                            )
                        )
                        if (autoSinka.get()) {
                            mc.thePlayer.sendChatMessage("[SINKA] I like this!" + LiquidBounce.CLIENTQQ)
                        }
                    }
                }
            }
        }
    }
    //判断攻击目标死亡
    @EventTarget
    private fun onAttack(event: AttackEvent) { syncEntity = event.targetEntity as EntityLivingBase?
    }
    private fun music() {
        if (Music.get()) {
            when(MusicValue.get()){
                "On-My-Own" -> {
                    LiquidBounce.tipSoundManager.mvp.asyncPlay()
                }
                "Liquid" -> {
                    LiquidBounce.tipSoundManager.mvp1.asyncPlay()
                }
                "GOODRAGE" -> {
                    LiquidBounce.tipSoundManager.mvp2.asyncPlay()
                }
                "dashstar" -> {
                    LiquidBounce.tipSoundManager.mvp3.asyncPlay()
                }
                "Chronomia" -> {
                    LiquidBounce.tipSoundManager.mvp4.asyncPlay()
                }
                "OLDGENESIS" -> {
                    LiquidBounce.tipSoundManager.mvp5.asyncPlay()
                }
                "Drop" -> {
                    LiquidBounce.tipSoundManager.drop.asyncPlay()
                }
                "Disorder" -> {
                    LiquidBounce.tipSoundManager.mvp6.asyncPlay()
                }
            }
        }
    }
}