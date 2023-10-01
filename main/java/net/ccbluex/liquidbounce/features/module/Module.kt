/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.features.module.modules.client.ClientSettings
import net.ccbluex.liquidbounce.script.api.module.ScriptModule
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.Skid.AnimationHelper
import net.ccbluex.liquidbounce.utils.Skid.Translate
import net.ccbluex.liquidbounce.utils.render.ColorUtils.stripColor
import net.ccbluex.liquidbounce.value.*
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard

import java.util.*

@SideOnly(Side.CLIENT)
open class Module : MinecraftInstance(), Listenable {

    // Module information
    // TODO: Remove ModuleInfo and change to constructor (#Kotlin)
    var fakeName: String
    var name: String
    var description: String
    var category: ModuleCategory
    val translate = Translate(0F, 0F)
    val valueTranslate = Translate(0F, 0F)
    val moduleTranslate = Translate(0F, 0F)
    @JvmField
    var showSettings = false
    @JvmField
    var yPos = 30F
    val animationHelper: AnimationHelper
    var keyBind = Keyboard.CHAR_NONE
        set(keyBind) {
            field = keyBind

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }
    var array = true
        set(array) {
            field = array

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }
    private val canEnable: Boolean

    var slideStep = 0F
    var openList = false

    init {
        val moduleInfo = javaClass.getAnnotation(ModuleInfo::class.java)!!
        fakeName = if(moduleInfo.fakeName.isEmpty() || this is ScriptModule) moduleInfo.name else moduleInfo.fakeName
        name = moduleInfo.name
        description = moduleInfo.description
        category = moduleInfo.category
        keyBind = moduleInfo.keyBind
        array = moduleInfo.array
        canEnable = moduleInfo.canEnable
        animationHelper = AnimationHelper(this)
    }

    // Current state of module
    var state = false
        set(value) {
            if (field == value) return

            // Call toggle
            onToggle(value)

            // Play sound and add notification
            if (!LiquidBounce.isStarting) {
                if((LiquidBounce.moduleManager[ClientSettings::class.java] as ClientSettings).sound.get()){
                    LiquidBounce.tipSoundManager.disableSound.asyncPlay()
                }
                if((LiquidBounce.moduleManager.getModule(ClientSettings::class.java)as ClientSettings).moduleNoti.get()){
                    LiquidBounce.hud.addNotification(Notification("Module","${if (value) "Enabled " else "Disabled "}$name",if(value) NotifyType.SUCCESS else NotifyType.ERROR))
                }
            }

            // Call on enabled or disabled
            if (value) {
                onEnable()

                if (canEnable)
                    field = true
            } else {
                onDisable()
                field = false
            }

            // Save module state
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }


    // HUD
    val hue = Math.random().toFloat()
    var slide = 0F
    var BreakName : Boolean = false
    var higt = 0F

    // Tag
    open val tag: String?
        get() = null

    val tagName: String
        get() = "$name${if (tag == null) "" else " §7$tag"}"

    val colorlessTagName: String
        get() = "$name${if (tag == null) "" else " " + stripColor(tag)}"

    /**
     * Toggle module
     */
    fun toggle() {
        state = !state
    }

    /**
     * Called when module toggled
     */
    open fun onToggle(state: Boolean) {}

    /**
     * Called when module enabled
     */
    open fun onEnable() {}

    /**
     * Called when module disabled
     */
    open fun onDisable() {}

    /**
     * Get module by [valueName]
     */
    open fun getValue(valueName: String) = values.find { it.name.equals(valueName, ignoreCase = true) }

    /**
     * Get all values of module
     */
//    open val values: List<Value<*>>
//        get() = javaClass.declaredFields.map { valueField ->
//            valueField.isAccessible = true
//            valueField[this]
//        }.filterIsInstance<Value<*>>()
    open val values: List<Value<*>>
        get() = getValuesMethod()

    private fun getValuesMethod(): List<Value<*>> {
        val optionTreeSet = TreeSet<Value<*>> { value, value2 -> value.name.compareTo(value2.name) }
        javaClass.declaredFields.forEach { valueField ->
            valueField.isAccessible = true
            val value = valueField[this]
            if(value is Value<*>)
                optionTreeSet.add(value)
        }
        val optionList = ArrayList<Value<*>>()
        optionTreeSet.filter { option -> option is IntegerValue || option is FloatValue }
            .forEach { optionList.add(it) }
        optionTreeSet.filterIsInstance<ListValue>()
            .forEach { optionList.add(it) }
        optionTreeSet.filterIsInstance<BoolValue>()
            .forEach { optionList.add(it) }
        optionTreeSet.filterIsInstance<TextValue>()
            .forEach { optionList.add(it) }
        optionTreeSet.filterIsInstance<BlockValue>()
            .forEach { optionList.add(it) }
        optionTreeSet.filterIsInstance<FontValue>()
            .forEach { optionList.add(it) }
        return optionList
    }

    protected fun alert(msg: String) = ClientUtils.displayChatMessage(msg)

    val numberValues: List<Value<*>>
        get() = values.filter { it is IntegerValue || it is FloatValue }

    val booleanValues: List<BoolValue>
        get() = values.filterIsInstance<BoolValue>()

    val listValues: List<ListValue>
        get() = values.filterIsInstance<ListValue>()

    fun getBreakName(breakValue: Boolean): String {
        val stringBuilder = StringBuilder()
        if(!breakValue)
            return name
        if(name == "AutoGG&Play")
            return "AutoGG & Play"
        for(i in name.indices) {
            if(i + 2 < name.length && i > 1)
                if(Character.isUpperCase(name[i + 2]) && Character.isUpperCase(name[i])) {
                    stringBuilder.append(" ${name[i]}")
                    continue
                }
            if(i + 1 < name.length)
                if (!Character.isUpperCase(name[i + 1]))
                    stringBuilder.append(if (Character.isUpperCase(name[i]) && i > 0) " ${name[i]}" else name[i])
                else
                    stringBuilder.append(name[i])
            else
                stringBuilder.append(name[i])
        }
        return stringBuilder.toString()
    }


    open fun tagName(nameBreak: Boolean) : String {
        if(nameBreak && !name.contains("ScriptModule") && !fakeName.contains("ScriptModule")) {
            return "$fakeName${if (tag == null) "" else "§7 [$tag]"}"
        }
        return "$name${if (tag == null) "" else "§7 [$tag]"}"
    }


    fun colorlessTagName(nameBreak: Boolean) : String {
        if(nameBreak) {
            return "$fakeName${if (tag == null) "" else "[" + stripColor(tag)}"
        }
        return "$name${if (tag == null) "" else "§[" + stripColor(tag)}"
    }

    fun fakeName(nameBreak: Boolean) : String {
        if(nameBreak && !name.contains("ScriptModule") && !fakeName.contains("ScriptModule")) {
            return fakeName
        }
        return name
    }



    /**
     * Events should be handled when module is enabled
     */
    override fun handleEvents() = state
}