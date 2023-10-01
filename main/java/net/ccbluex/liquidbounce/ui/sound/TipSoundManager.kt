package net.ccbluex.liquidbounce.ui.sound

import net.ccbluex.liquidbounce.LiquidBounce
import me.HXC.Utils.FileUtils
import java.io.File

class TipSoundManager {
    var enableSound: TipSoundPlayer
    var disableSound: TipSoundPlayer

    init {
        val enableSoundFile = File(LiquidBounce.fileManager.soundsDir, "enable.wav")
        val disableSoundFile = File(LiquidBounce.fileManager.soundsDir, "disable.wav")
//SoundModule.playSound(true)
        if (!enableSoundFile.exists()) {
            FileUtils.unpackFile(enableSoundFile, "assets/minecraft/liquidbounce/sound/win.wav")
        }
        if (!disableSoundFile.exists()) {
            FileUtils.unpackFile(disableSoundFile, "assets/minecraft/liquidbounce/sound/lost.wav")
        }

        enableSound = TipSoundPlayer(enableSoundFile)
        disableSound = TipSoundPlayer(disableSoundFile)
    }
}