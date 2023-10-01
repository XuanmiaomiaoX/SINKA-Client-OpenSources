package net.ccbluex.liquidbounce.sound

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.sound.utils.FileUtils
import java.io.File

class TipSoundManager {
    var enableSound: TipSoundPlayer
    var disableSound: TipSoundPlayer
    var startup: TipSoundPlayer
    var died: TipSoundPlayer
    var mvp: TipSoundPlayer
    var mvp1: TipSoundPlayer
    var mvp2: TipSoundPlayer
    var mvp3: TipSoundPlayer
    var mvp4: TipSoundPlayer
    var mvp5: TipSoundPlayer
    var drop: TipSoundPlayer
    var stone: TipSoundPlayer
    var sinka: TipSoundPlayer
    var mouse: TipSoundPlayer
    var mvp6: TipSoundPlayer
    var open: TipSoundPlayer
    var omg: TipSoundPlayer

    init {
        val enableSoundFile = File(LiquidBounce.fileManager.soundsDir, "enable.wav")
        val disableSoundFile = File(LiquidBounce.fileManager.soundsDir, "disable.wav")
        val startupFile = File(LiquidBounce.fileManager.soundsDir, "startup.wav")
        val diedFile = File(LiquidBounce.fileManager.soundsDir, "died.wav")
        val omgFile = File(LiquidBounce.fileManager.soundsDir, "omg.wav")
        val mvpFile = File(LiquidBounce.fileManager.soundsDir, "mvp.wav")
        val mvp1File = File(LiquidBounce.fileManager.soundsDir, "mvp1.wav")
        val mvp2File = File(LiquidBounce.fileManager.soundsDir, "goodrage.wav")
        val mvp3File = File(LiquidBounce.fileManager.soundsDir, "mvp3.wav")
        val mvp4File = File(LiquidBounce.fileManager.soundsDir, "mvp4.wav")
        val mvp5File = File(LiquidBounce.fileManager.soundsDir, "OLDGENESIS.wav")
        val dropFile = File(LiquidBounce.fileManager.soundsDir, "drop.wav")
        val stoneFile = File(LiquidBounce.fileManager.soundsDir, "stone.wav")
        val sinkaFile = File(LiquidBounce.fileManager.soundsDir, "sinka.wav")
        val mouseFile = File(LiquidBounce.fileManager.soundsDir, "mouse.wav")
        val mvp6File = File(LiquidBounce.fileManager.soundsDir, "mvp6.wav")
        val openFile = File(LiquidBounce.fileManager.soundsDir,"open1.wav")


        if (!enableSoundFile.exists()) {
            FileUtils.unpackFile(enableSoundFile, "assets/minecraft/fdpclient/sound/enable.wav")
        }
        if (!disableSoundFile.exists()) {
            FileUtils.unpackFile(disableSoundFile, "assets/minecraft/fdpclient/sound/disable.wav")
        }
        if (!startupFile.exists()) {
            FileUtils.unpackFile(startupFile, "assets/minecraft/fdpclient/sound/startup.wav")
        }
        if (!omgFile.exists()) {
            FileUtils.unpackFile(omgFile, "assets/minecraft/fdpclient/sound/omg.wav")
        }
        if (!diedFile.exists()) {
            FileUtils.unpackFile(diedFile, "assets/minecraft/fdpclient/sound/died.wav")
        }
        if (!mvpFile.exists()) {
            FileUtils.unpackFile(mvpFile, "assets/minecraft/fdpclient/sound/mvp.wav")
        }
        if (!mvp1File.exists()) {
            FileUtils.unpackFile(mvp1File, "assets/minecraft/fdpclient/sound/mvp1.wav")
        }
        if (!mvp2File.exists()) {
            FileUtils.unpackFile(mvp2File, "assets/minecraft/fdpclient/sound/goodrage.wav")
        }
        if (!mvp3File.exists()) {
            FileUtils.unpackFile(mvp3File, "assets/minecraft/fdpclient/sound/mvp3.wav")
        }
        if (!mvp4File.exists()) {
            FileUtils.unpackFile(mvp4File, "assets/minecraft/fdpclient/sound/mvp4.wav")
        }
        if (!stoneFile.exists()) {
            FileUtils.unpackFile(stoneFile, "assets/minecraft/fdpclient/sound/stone.wav")
        }
        if (!mvp5File.exists()) {
            FileUtils.unpackFile(mvp5File, "assets/minecraft/fdpclient/sound/OLDGENESIS.wav")
        }
        if (!dropFile.exists()) {
            FileUtils.unpackFile(dropFile, "assets/minecraft/fdpclient/sound/drop.wav")
        }
        if (!sinkaFile.exists()) {
            FileUtils.unpackFile(sinkaFile, "assets/minecraft/fdpclient/sound/sinka.wav")
        }
        if (!mouseFile.exists()) {
            FileUtils.unpackFile(mouseFile, "assets/minecraft/fdpclient/sound/mouse.wav")
        }
        if (!mvp6File.exists()) {
            FileUtils.unpackFile(mvp6File, "assets/minecraft/fdpclient/sound/mvp6.wav")
        }
        if (!openFile.exists()) {
            FileUtils.unpackFile(openFile, "assets/minecraft/fdpclient/sound/open1.wav")
        }

        enableSound = TipSoundPlayer(enableSoundFile)
        disableSound = TipSoundPlayer(disableSoundFile)
        startup = TipSoundPlayer(startupFile)
        died = TipSoundPlayer(diedFile)
        omg = TipSoundPlayer(omgFile)
        mvp = TipSoundPlayer(mvpFile)
        mvp1 = TipSoundPlayer(mvp1File)
        mvp2 = TipSoundPlayer(mvp2File)
        mvp3 = TipSoundPlayer(mvp3File)
        mvp4 = TipSoundPlayer(mvp4File)
        mvp5 = TipSoundPlayer(mvp5File)
        drop = TipSoundPlayer(dropFile)
        stone = TipSoundPlayer(stoneFile)
        sinka = TipSoundPlayer(sinkaFile)
        mouse = TipSoundPlayer(mouseFile)
        mvp6 = TipSoundPlayer(mvp6File)
        open = TipSoundPlayer(openFile)
    }
}