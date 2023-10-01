/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.ghost

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.timer.TimeUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.Block
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.settings.KeyBinding
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.init.Blocks
import net.minecraft.item.ItemAxe
import net.minecraft.item.ItemPickaxe
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.*
import net.minecraft.potion.Potion
import net.minecraft.util.*
import net.minecraft.world.WorldSettings
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.Cylinder
import java.awt.Color
import java.util.*
import kotlin.math.*

// TODO: Recode KillAura mark
@ModuleInfo(name = "LegitAura", category = ModuleCategory.GHOST, description = "1")
class LegitAura : Module() {

    /**
     * OPTIONS
     */

    // CPS - Attack speed
    private val maxCPS: IntegerValue = object : IntegerValue("MaxCPS", 8, 1, 12) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = minCPS.get()
            if (i > newValue) set(i)

            attackDelay = getAttackDelay(minCPS.get(), this.get())
        }
    }

    private val minCPS: IntegerValue = object : IntegerValue("MinCPS", 5, 1, 12) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = maxCPS.get()
            if (i < newValue) set(i)

            attackDelay = getAttackDelay(this.get(), maxCPS.get())
        }
    }

    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val combatDelayValue = BoolValue("1.9CombatDelay", false)

    // Range
    val rangeValue = object : FloatValue("Range", 3.2f, 1f, 4f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val i = discoverRangeValue.get()
            if (i < newValue) set(i)
        }
    }
    private val throughWallsRangeValue = object : FloatValue("ThroughWallsRange", 1.0f, 0f, 2f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val i = rangeValue.get()
            if (i < newValue) set(i)
        }
    }
    private val discoverRangeValue = FloatValue("DiscoverRange", 4f, 0f, 5f)
    private val rangeSprintReducementValue = FloatValue("RangeSprintReducement", 0f, 0f, 0.4f)

    // Modes
    private val priorityValue = ListValue("Priority", arrayOf("Health", "Distance", "Direction", "LivingTime", "Armor"), "Distance")
    private val targetModeValue = ListValue("TargetMode", arrayOf("Single","Multi"), "Single")

    // Bypass
    private val swingValue = ListValue("Swing", arrayOf("Normal"), "Normal")
    private val keepSprintValue = BoolValue("KeepSprint", true)

    // AutoBlock
    private val autoBlockRangeValue = object : FloatValue("AutoBlockRange", 3.0f, 0f, 0f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val i = discoverRangeValue.get()
            if (i < newValue) set(i)
        }
    }
    val autoBlockValue = ListValue("AutoBlockMode", arrayOf("Off","Fake"),"Off")
    val blockPacketValue = ListValue("AutoBlockPacket", arrayOf("Normal"),"Normal")
    private val blockCheckValue = ListValue("BlockCheck", arrayOf("Normal"),"Normal")
    private val interactAutoBlockValue = BoolValue("InteractAutoBlock", true)
    private val blockRate = IntegerValue("BlockRate", 100, 1, 100)

    // Raycast
    private val raycastValue = BoolValue("RayCast", true)
    private val raycastIgnoredValue = BoolValue("RayCastIgnored", false)
    private val livingRaycastValue = BoolValue("LivingRayCast", true)

    // Bypass
    private val aacValue = BoolValue("AAC", true)

    // Turn Speed
    private val maxTurnSpeed: FloatValue = object : FloatValue("MaxTurnSpeed", 180f, 0f, 180f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = minTurnSpeed.get()
            if (v > newValue) set(v)
        }
    }

    private val minTurnSpeed: FloatValue = object : FloatValue("MinTurnSpeed", 180f, 0f, 180f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = maxTurnSpeed.get()
            if (v < newValue) set(v)
        }
    }

    private val silentRotationValue = BoolValue("SilentRotation", false)
    private val rotationStrafeValue = ListValue("Strafe", arrayOf("Strict"), "Strict")
    private val strafeOnlyGroundValue = BoolValue("StrafeOnlyGround",true)
    private val hitableValue = BoolValue("AlwaysHitable",true)
    private val fovValue = FloatValue("FOV", 50f, 0f, 90f)

    // Predict
    private val predictValue = BoolValue("Predict", true)


    private val maxPredictSize: FloatValue = object : FloatValue("MaxPredictSize", 1f, 0.1f, 5f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = minPredictSize.get()
            if (v > newValue) set(v)
        }
    }

    private val minPredictSize: FloatValue = object : FloatValue("MinPredictSize", 1f, 0.1f, 5f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = maxPredictSize.get()
            if (v < newValue) set(v)
        }
    }

    // Bypass
    private val failRateValue = FloatValue("FailRate", 0f, 0f, 100f)
    private val fakeSwingValue = BoolValue("FakeSwing", true)
    private val noInventoryAttackValue = BoolValue("NoInvAttack", false)
    private val noInventoryDelayValue = IntegerValue("NoInvDelay", 200, 0, 500)
    private val switchDelayValue = IntegerValue("SwitchDelay",300 ,1, 2000)
    private val limitedMultiTargetsValue = IntegerValue("LimitedMultiTargets", 0, 0, 50)

    // Visuals
    private val markValue = ListValue("Mark", arrayOf("Liquid","FDP","Block","Jello", "Plat", "Red", "Sims","Mimosa","None"),"Mimosa")
    private val hiteffect = ListValue("HitEffect", arrayOf("Lightningbolt","Criticals","Blood","Fire","Water","Smoke","Flame","Heart","None"),"Always")
    private val lightingSoundValue = BoolValue("LightingSound", true)
    private val fakeSharpValue = BoolValue("FakeSharp", true)
    //Rotation
    private val boundingBoxModeValue = ListValue("LockLocation", arrayOf("Head","Auto"), "Auto")
    private val rotationSmoothValue = FloatValue("CustomSmooth", 2f, 1f, 10f)
    private val rotationRevValue = BoolValue("RotationReverse", false)
    private val rotationRevTickValue = IntegerValue("RotationReverseTick", 5, 1, 20)
    private val keepDirectionValue = BoolValue("KeepDirection", true)
    private val keepDirectionTickValue = IntegerValue("KeepDirectionTick", 15, 1, 20)
    private val rotationSmoothModeValue = ListValue("SmoothMode", arrayOf("Custom", "Line", "Quad", "Sine", "QuadSine"), "Custom")
    private val rotationModeValue = ListValue("RotationMode", arrayOf("None","LiquidBounce", "ForceCenter", "SmoothCenter", "SmoothLiquid", "LockView", "OldMatrix"), "LiquidBounce")
    private val randomCenterModeValue = ListValue("RandomCenter", arrayOf("Off", "Cubic", "Horizonal", "Vertical"), "Off")
    private val randomCenRangeValue = FloatValue("RandomRange", 0.0f, 0.0f, 1.2f)

    /**
     * MODULE
     */

    // Target
    var target: EntityLivingBase? = null
    private val markTimer=MSTimer()
    private var currentTarget: EntityLivingBase? = null
    private var hitable = false
    private val prevTargetEntities = mutableListOf<Int>()
    private val discoveredTargets = mutableListOf<EntityLivingBase>()
    private val inRangeDiscoveredTargets = mutableListOf<EntityLivingBase>()

    // Attack delay
    private val attackTimer = MSTimer()
    private val switchTimer = MSTimer()
    private var attackDelay = 0L
    private var clicks = 0

    // Container Delay
    private var containerOpen = -1L

    // Fake block status
    var blockingStatus = false

    /**
     * Enable kill aura module
     */
    override fun onEnable() {
        mc.thePlayer ?: return
        mc.theWorld ?: return

        updateTarget()
        RenderUtils.HWIDYES()
    }

    /**
     * Disable kill aura module
     */
    override fun onDisable() {
        target = null
        currentTarget = null
        hitable = false
        prevTargetEntities.clear()
        attackTimer.reset()
        clicks = 0

        stopBlocking()
    }

    /**
     * Motion event
     */
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (mc.thePlayer.isRiding)
            return

        if (!event.isPre()) {
            // AutoBlock
            if (!autoBlockValue.get().equals("off",true) && discoveredTargets.isNotEmpty() && (!blockCheckValue.get().equals("Attack",true)||discoveredTargets.filter { mc.thePlayer.getDistanceToEntityBox(it)>maxRange }.isNotEmpty()) && canBlock) {
                val target=discoveredTargets[0]
                if(mc.thePlayer.getDistanceToEntityBox(target) < autoBlockRangeValue.get())
                    startBlocking(target, blockPacketValue.get().equals("Interact",true) && (mc.thePlayer.getDistanceToEntityBox(target)<maxRange))
            }

            target ?: return
            currentTarget ?: return

            // Update hitable
            updateHitable()

            return
        }

        if (rotationStrafeValue.get().equals("Off", true))
            update()

        if (target != null && currentTarget != null) {
            while (clicks > 0) {
                runAttack()
                clicks--
            }
        }
    }

    /**
     * Strafe event
     */
    @EventTarget
    fun onStrafe(event: StrafeEvent) {
        if (rotationStrafeValue.get().equals("Off", true) && !mc.thePlayer.isRiding)
            return

        update()

        if(strafeOnlyGroundValue.get()&&!mc.thePlayer.onGround)
            return

        if (discoveredTargets.isNotEmpty() && RotationUtils.targetRotation != null) {
            when (rotationStrafeValue.get().toLowerCase()) {
                "strict" -> {
                    val (yaw) = RotationUtils.targetRotation ?: return
                    var strafe = event.strafe
                    var forward = event.forward
                    val friction = event.friction

                    var f = strafe * strafe + forward * forward

                    if (f >= 1.0E-4F) {
                        f = MathHelper.sqrt_float(f)

                        if (f < 1.0F)
                            f = 1.0F

                        f = friction / f
                        strafe *= f
                        forward *= f

                        val yawSin = MathHelper.sin((yaw * Math.PI / 180F).toFloat())
                        val yawCos = MathHelper.cos((yaw * Math.PI / 180F).toFloat())

                        mc.thePlayer.motionX += strafe * yawCos - forward * yawSin
                        mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin
                    }
                    event.cancelEvent()
                }
                "silent" -> {
                    update()

                    RotationUtils.targetRotation.applyStrafeToPlayer(event)
                    event.cancelEvent()
                }
            }
        }
    }

    fun update() {
        if (cancelRun || (noInventoryAttackValue.get() && (mc.currentScreen is GuiContainer ||
                    System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())))
            return

        // Update target
        updateTarget()

        if (discoveredTargets.isEmpty()) {
            stopBlocking()
            return
        }

        // Target
        currentTarget = target

        if (!targetModeValue.get().equals("Switch", ignoreCase = true) && EntityUtils.isSelected(currentTarget, true))
            target = currentTarget
    }

    /**
     * Update event
     */
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (cancelRun) {
            target = null
            currentTarget = null
            hitable = false
            stopBlocking()
            discoveredTargets.clear()
            inRangeDiscoveredTargets.clear()
            return
        }

        if (noInventoryAttackValue.get() && (mc.currentScreen is GuiContainer ||
                    System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())) {
            target = null
            currentTarget = null
            hitable = false
            if (mc.currentScreen is GuiContainer) containerOpen = System.currentTimeMillis()
            return
        }

        if (!rotationStrafeValue.get().equals("Off", true) && !mc.thePlayer.isRiding)
            return

        if (mc.thePlayer.isRiding)
            update()

        if (target != null && currentTarget != null) {
            while (clicks > 0) {
                runAttack()
                clicks--
            }
        }
    }

    /**
     * Render event
     */
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (cancelRun) {
            target = null
            currentTarget = null
            hitable = false
            stopBlocking()
            discoveredTargets.clear()
            inRangeDiscoveredTargets.clear()
        }
        if (currentTarget != null && attackTimer.hasTimePassed(attackDelay) && currentTarget!!.hurtTime <= hurtTimeValue.get()) {
            clicks++
            attackTimer.reset()
            attackDelay = getAttackDelay(minCPS.get(), maxCPS.get())
        }

        val ent = EntityLightningBolt(mc.theWorld, target!!.posX, target!!.posY, target!!.posZ)

        when(hiteffect.get().toLowerCase()){
            "lightningbolt" -> {
                mc.theWorld.addEntityToWorld(-1, ent)
                if(lightingSoundValue.get()){
                    mc.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("random.explode"), 1.0f))
                    mc.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("ambient.weather.thunder"), 1.0f))
                }
            }
            "flame" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.FLAME)
            "smoke" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.SMOKE_LARGE)
            "heart" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.HEART)
            "fire" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.LAVA)
            "water" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.WATER_DROP)
            "criticals" -> mc.effectRenderer.emitParticleAtEntity(target!!, EnumParticleTypes.CRIT)
            "blood" ->{
                repeat(10) {
                    mc.effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.particleID, target!!.posX, target!!.posY + target!!.height / 2, target!!.posZ,
                        target!!.motionX + RandomUtils.nextFloat(-0.5f, 0.5f), target!!.motionY + RandomUtils.nextFloat(-0.5f, 0.5f), target!!.motionZ + RandomUtils.nextFloat(-0.5f, 0.5f), Block.getStateId(Blocks.redstone_block.defaultState))
                }
            }
        }


        when(markValue.get().toLowerCase()) {
            "liquid" -> {
                discoveredTargets.forEach {
                    RenderUtils.drawPlatform(
                        it,
                        if (it.hurtTime <= 0) Color(37, 126, 255, 170) else Color(255, 0, 0, 170)
                    )
                }
            }

            "plat" -> RenderUtils.drawPlatform(
                target!!,
                if (hitable) Color(37, 126, 255, 70) else Color(255, 0, 0, 70)
            )

            "block" -> {
                discoveredTargets.forEach {
                    val bb = it.entityBoundingBox
                    it.entityBoundingBox = bb.expand(0.2, 0.2, 0.2)
                    RenderUtils.drawEntityBox(it, if (it.hurtTime <= 0) Color.GREEN else Color.RED, true)
                    it.entityBoundingBox = bb
                }
            }

            "red" -> {
                discoveredTargets.forEach {
                    RenderUtils.drawPlatform(
                        it,
                        if (it.hurtTime <= 0) Color(255, 255, 255, 255) else Color(124, 215, 255, 255)
                    )
                }
            }

            "sims" -> {
                discoveredTargets.forEach {
                    val radius = 0.15f
                    val side = 4
                    GL11.glPushMatrix()
                    GL11.glTranslated(
                        it.lastTickPosX + (it.posX - it.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX,
                        (it.lastTickPosY + (it.posY - it.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY) + it.height * 1.1,
                        it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                    )
                    GL11.glRotatef(-it.width, 0.0f, 1.0f, 0.0f)
                    GL11.glRotatef((mc.thePlayer.ticksExisted + mc.timer.renderPartialTicks) * 5, 0f, 1f, 0f)
                    RenderUtils.glColor(if (it.hurtTime <= 0) Color(80, 255, 80) else Color(255, 0, 0))
                    RenderUtils.enableSmoothLine(1.5F)
                    val c = Cylinder()
                    GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f)
                    c.draw(0F, radius, 0.3f, side, 1)
                    c.drawStyle = 100012
                    GL11.glTranslated(0.0, 0.0, 0.3)
                    c.draw(radius, 0f, 0.3f, side, 1)
                    GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f)
                    GL11.glTranslated(0.0, 0.0, -0.3)
                    c.draw(0F, radius, 0.3f, side, 1)
                    GL11.glTranslated(0.0, 0.0, 0.3)
                    c.draw(radius, 0F, 0.3f, side, 1)
                    RenderUtils.disableSmoothLine()
                    GL11.glPopMatrix()
                }
            }
            "fdp" -> {
                val drawTime = (System.currentTimeMillis() % 1500).toInt()
                val drawMode=drawTime>750
                var drawPercent=drawTime/750.0
                //true when goes up
                if(!drawMode){
                    drawPercent=1-drawPercent
                }else{
                    drawPercent-=1
                }
                drawPercent=EaseUtils.easeInOutQuad(drawPercent)
                discoveredTargets.forEach {
                    GL11.glPushMatrix()
                    GL11.glDisable(3553)
                    GL11.glEnable(2848)
                    GL11.glEnable(2881)
                    GL11.glEnable(2832)
                    GL11.glEnable(3042)
                    GL11.glBlendFunc(770, 771)
                    GL11.glHint(3154, 4354)
                    GL11.glHint(3155, 4354)
                    GL11.glHint(3153, 4354)
                    GL11.glDisable(2929)
                    GL11.glDepthMask(false)

                    val bb=it.entityBoundingBox
                    val radius=(bb.maxX-bb.minX)+0.3
                    val height=bb.maxY-bb.minY
                    val x = it.lastTickPosX + (it.posX - it.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                    val y = (it.lastTickPosY + (it.posY - it.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY) + height * drawPercent
                    val z = it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                    GL11.glLineWidth((radius*5f).toFloat())
                    GL11.glBegin(3)
                    for (i in 0..360) {
                        val rainbow = Color(Color.HSBtoRGB((mc.thePlayer.ticksExisted / 70.0 + sin(i / 50.0 * 1.75)).toFloat() % 1.0f, 0.7f, 1.0f))
                        GL11.glColor3f(rainbow.red / 255.0f, rainbow.green / 255.0f, rainbow.blue / 255.0f)
                        GL11.glVertex3d(x + radius * cos(i * 6.283185307179586 / 45.0), y, z + radius * sin(i * 6.283185307179586 / 45.0))
                    }
                    GL11.glEnd()

                    GL11.glDepthMask(true)
                    GL11.glEnable(2929)
                    GL11.glDisable(2848)
                    GL11.glDisable(2881)
                    GL11.glEnable(2832)
                    GL11.glEnable(3553)
                    GL11.glPopMatrix()
                }
            }
            "jello" -> {
                discoveredTargets.forEach {
                    val drawTime = (System.currentTimeMillis() % 2000).toInt()
                    val drawMode=drawTime>1000
                    var drawPercent=drawTime/1000.0
                    //true when goes up
                    if(!drawMode){
                        drawPercent=1-drawPercent
                    }else{
                        drawPercent-=1
                    }
                    drawPercent=EaseUtils.easeInOutQuad(drawPercent)
                    val points = mutableListOf<Vec3>()
                    val bb=it.entityBoundingBox
                    val radius=bb.maxX-bb.minX
                    val height=bb.maxY-bb.minY
                    val posX = it.lastTickPosX + (it.posX - it.lastTickPosX) * mc.timer.renderPartialTicks
                    var posY = it.lastTickPosY + (it.posY - it.lastTickPosY) * mc.timer.renderPartialTicks
                    if(drawMode){
                        posY-=0.5
                    }else{
                        posY+=0.5
                    }
                    val posZ = it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * mc.timer.renderPartialTicks
                    for(i in 0..360 step 7){
                        points.add(Vec3(posX - sin(i * Math.PI / 180F) * radius,posY+height*drawPercent,posZ + cos(i * Math.PI / 180F) * radius))
                    }
                    points.add(points[0])
                    //draw
                    mc.entityRenderer.disableLightmap()
                    GL11.glPushMatrix()
                    GL11.glDisable(GL11.GL_TEXTURE_2D)
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                    GL11.glEnable(GL11.GL_LINE_SMOOTH)
                    GL11.glEnable(GL11.GL_BLEND)
                    GL11.glDisable(GL11.GL_DEPTH_TEST)
                    GL11.glBegin(GL11.GL_LINE_STRIP)
                    val baseMove=(if(drawPercent>0.5){1-drawPercent}else{drawPercent})*2
                    val min=(height/60)*20*(1-baseMove)*(if(drawMode){-1}else{1})
                    for(i in 0..20) {
                        var moveFace=(height/60F)*i*baseMove
                        if(drawMode){
                            moveFace=-moveFace
                        }
                        val firstPoint=points[0]
                        GL11.glVertex3d(
                            firstPoint.xCoord - mc.renderManager.viewerPosX, firstPoint.yCoord - moveFace - min - mc.renderManager.viewerPosY,
                            firstPoint.zCoord - mc.renderManager.viewerPosZ
                        )
                        GL11.glColor4f(1F, 1F, 1F, 0.7F*(i/20F))
                        for (vec3 in points) {
                            GL11.glVertex3d(
                                vec3.xCoord - mc.renderManager.viewerPosX, vec3.yCoord - moveFace - min - mc.renderManager.viewerPosY,
                                vec3.zCoord - mc.renderManager.viewerPosZ
                            )
                        }
                        GL11.glColor4f(0F,0F,0F,0F)
                    }
                    GL11.glEnd()
                    GL11.glEnable(GL11.GL_DEPTH_TEST)
                    GL11.glDisable(GL11.GL_LINE_SMOOTH)
                    GL11.glDisable(GL11.GL_BLEND)
                    GL11.glEnable(GL11.GL_TEXTURE_2D)
                    GL11.glPopMatrix()
                }
            }
        }
    }

    /**
     * Handle entity move
     */
    @EventTarget
    fun onEntityMove(event: EntityMovementEvent) {
        val movedEntity = event.movedEntity

        if (target == null || movedEntity != currentTarget)
            return

        updateHitable()
    }

    /**
     * Attack enemy
     */
    private fun runAttack() {
        target ?: return
        currentTarget ?: return

        // Settings
        val failRate = failRateValue.get()
        val swing = swingValue.get()
        val openInventory = aacValue.get() && mc.currentScreen is GuiInventory
        val failHit = failRate > 0 && Random().nextInt(100) <= failRate

        // Close inventory when open
        if (openInventory)
            mc.netHandler.addToSendQueue(C0DPacketCloseWindow())

        // Check is not hitable or check failrate
        if (!hitable || failHit) {
            if (!swing.equals("none",true) && (fakeSwingValue.get() || failHit)) {
                if(swing.equals("packet",true)){
                    mc.netHandler.addToSendQueue(C0APacketAnimation())
                }else{
                    mc.thePlayer.swingItem()
                }
            }
        } else {
            // Attack
            if (!targetModeValue.get().equals("Multi", ignoreCase = true)) {
                attackEntity(currentTarget!!)
            } else {
                inRangeDiscoveredTargets.forEachIndexed { index, entity ->
                    if(limitedMultiTargetsValue.get()==0 || index<limitedMultiTargetsValue.get())
                        attackEntity(entity)
                }
            }

            if(targetModeValue.get().equals("Switch", true)){
                if(switchTimer.hasTimePassed(switchDelayValue.get().toLong())){
                    prevTargetEntities.add(if (aacValue.get()) target!!.entityId else currentTarget!!.entityId)
                    switchTimer.reset()
                }
            }else{
                prevTargetEntities.add(if (aacValue.get()) target!!.entityId else currentTarget!!.entityId)
            }

            if (target == currentTarget)
                target = null
        }

        // Open inventory
        if (openInventory)
            mc.netHandler.addToSendQueue(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
    }

    /**
     * Update current target
     */
    private fun updateTarget() {
        // Reset fixed target to null
        target = null

        // Settings
        val hurtTime = hurtTimeValue.get()
        val fov = fovValue.get()
        val switchMode = targetModeValue.get().equals("Switch", ignoreCase = true)

        // Find possible targets
        discoveredTargets.clear()

        for (entity in mc.theWorld.loadedEntityList) {
            if (entity !is EntityLivingBase || !EntityUtils.isSelected(entity, true) || (switchMode && prevTargetEntities.contains(entity.entityId)))
                continue

            val distance = mc.thePlayer.getDistanceToEntityBox(entity)
            val entityFov = RotationUtils.getRotationDifference(entity)

            if (distance <= discoverRangeValue.get() && (fov == 180F || entityFov <= fov) && entity.hurtTime <= hurtTime)
                discoveredTargets.add(entity)
        }

        // Sort targets by priority
        when (priorityValue.get().toLowerCase()) {
            "distance" -> discoveredTargets.sortBy { mc.thePlayer.getDistanceToEntityBox(it) } // Sort by distance
            "health" -> discoveredTargets.sortBy { it.health } // Sort by health
            "direction" -> discoveredTargets.sortBy { RotationUtils.getRotationDifference(it) } // Sort by FOV
            "livingtime" -> discoveredTargets.sortBy { -it.ticksExisted } // Sort by existence
            "armor" -> discoveredTargets.sortBy { it.totalArmorValue } // Sort by armor
        }

        inRangeDiscoveredTargets.clear()
        inRangeDiscoveredTargets.addAll(discoveredTargets.filter { mc.thePlayer.getDistanceToEntityBox(it)<getRange(it) })

        // Cleanup last targets when no targets found and try again
        if (inRangeDiscoveredTargets.isEmpty()&&prevTargetEntities.isNotEmpty()) {
            prevTargetEntities.clear()
            updateTarget()
            return
        }

        // Find best target
        for (entity in discoveredTargets) {
            // Update rotations to current target
            if (!updateRotations(entity)) // when failed then try another target
                continue

            // Set target to current entity
            if(mc.thePlayer.getDistanceToEntityBox(entity) < maxRange)
                target = entity

            return
        }
    }

    /**
     * Attack [entity]
     */
    private fun attackEntity(entity: EntityLivingBase) {
        // Stop blocking
        if (!blockCheckValue.get().equals("Normal",true)&&(mc.thePlayer.isBlocking || blockingStatus)) {
            mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
            blockingStatus = false
        }

        // Call attack event
        LiquidBounce.eventManager.callEvent(AttackEvent(entity))
        markTimer.reset()

        // Attack target
        val swing=swingValue.get()
        if(swing.equals("packet",true)){
            mc.netHandler.addToSendQueue(C0APacketAnimation())
        }else if(swing.equals("normal",true)){
            mc.thePlayer.swingItem()
        }

        mc.netHandler.addToSendQueue(C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK))

        if (keepSprintValue.get()) {
            // Critical Effect
            if (mc.thePlayer.fallDistance > 0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder &&
                !mc.thePlayer.isInWater && !mc.thePlayer.isPotionActive(Potion.blindness) && !mc.thePlayer.isRiding)
                mc.thePlayer.onCriticalHit(entity)

            // Enchant Effect
            if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.heldItem, entity.creatureAttribute) > 0F || fakeSharpValue.get())
                mc.thePlayer.onEnchantmentCritical(entity)
        } else {
            if (mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR)
                mc.thePlayer.attackTargetEntityWithCurrentItem(entity)
        }

        // Start blocking after attack
        if (mc.thePlayer.isBlocking || (!autoBlockValue.get().equals("off",true) && canBlock)) {
            if(blockCheckValue.get().equals("tick",true))
                return

            if (!(blockRate.get() > 0 && Random().nextInt(100) <= blockRate.get()))
                return

            startBlocking(entity, blockPacketValue.get().equals("Interact",true))
        }

        // Blocking after attack
        if (autoBlockValue.get().equals("Packet", true) && (mc.thePlayer.isBlocking || canBlock) || (mc.thePlayer.isBlocking || blockingStatus))
            Blocking(entity, interactAutoBlockValue.get())

        // Blocking after attack 2
        if (mc.thePlayer.isBlocking || (autoBlockValue.get().equals("LiquidBounce",true) && canBlock)) {
            if (!(blockRate.get() > 0 && Random().nextInt(100) <= blockRate.get()))
                return

            startBlocking(entity, interactAutoBlockValue.get())
        }
    }

    private fun Blocking(interactEntity: Entity, interact: Boolean) {
        if (!(blockRate.get() > 0 && Random().nextInt(100) <= blockRate.get()))
            return

        if (interact) {
            val positionEye = mc.renderViewEntity?.getPositionEyes(1F)

            val expandSize = interactEntity.collisionBorderSize.toDouble()
            val boundingBox = interactEntity.entityBoundingBox.expand(expandSize, expandSize, expandSize)

            val (yaw, pitch) = RotationUtils.targetRotation ?: Rotation(mc.thePlayer!!.rotationYaw, mc.thePlayer!!.rotationPitch)
            val yawCos = cos(-yaw * 0.017453292F - Math.PI.toFloat())
            val yawSin = sin(-yaw * 0.017453292F - Math.PI.toFloat())
            val pitchCos = -cos(-pitch * 0.017453292F)
            val pitchSin = sin(-pitch * 0.017453292F)
            val range = min(maxRange.toDouble(), mc.thePlayer!!.getDistanceToEntityBox(interactEntity)) + 1
            val lookAt = positionEye!!.addVector(yawSin * pitchCos * range, pitchSin * range, yawCos * pitchCos * range)

            val movingObject = boundingBox.calculateIntercept(positionEye, lookAt) ?: return
            val hitVec = movingObject.hitVec

            mc.netHandler.addToSendQueue(C02PacketUseEntity(interactEntity, Vec3(
                hitVec.xCoord - interactEntity.posX,
                hitVec.yCoord - interactEntity.posY,
                hitVec.zCoord - interactEntity.posZ)
            ))
            mc.netHandler.addToSendQueue(C02PacketUseEntity(interactEntity, C02PacketUseEntity.Action.INTERACT))
        }

        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1),
            255, mc.thePlayer!!.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F))

        blockingStatus = true
    }

    /**
     * Update killaura rotations to enemy
     */
    private fun updateRotations(entity: Entity): Boolean {
        if (rotationModeValue.equals("None")) {
            return true
        }
        val bb = entity.entityBoundingBox
        val predictSize = if(predictValue.get()) floatArrayOf(minPredictSize.get(),maxPredictSize.get()) else floatArrayOf(0.0F,0.0F)
        val predict = doubleArrayOf(
            (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(predictSize[0], predictSize[1]),
            (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(predictSize[0], predictSize[1]),
            (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(predictSize[0], predictSize[1]))
        val boundingBox = when(boundingBoxModeValue.get()) {
            "Head" -> AxisAlignedBB(max(bb.minX,bb.minX + predict[0]),max(bb.minY,bb.minY + predictSize[1]),max(bb.minZ,bb.minZ + predict[2]),min(bb.maxX,bb.maxX + predict[0]),min(bb.maxY,bb.maxY + predictSize[1]),min(bb.maxZ,bb.maxZ + predict[2]))
            else -> bb.offset(predict[0],predict[1],predict[2])
        }

        val rModes = when (rotationModeValue.get()) {
            "LiquidBounce", "SmoothLiquid", "Derp" -> "LiquidBounce"
            "ForceCenter", "SmoothCenter", "OldMatrix", "Spin", "FastSpin" -> "CenterLine"
            "LockView" -> "CenterSimple"
            "Test" -> "HalfUp"
            else -> "LiquidBounce"
        }

        val (_, directRotation) =
            RotationUtils.calculateCenter(
                rModes,
                randomCenterModeValue.get(),
                (randomCenRangeValue.get()).toDouble(),
                boundingBox,
                predictValue.get() && rotationModeValue.get() != "Test",
                mc.thePlayer.getDistanceToEntityBox(entity) <= throughWallsRangeValue.get()
            ) ?: return false

        if (rotationModeValue.get() == "OldMatrix") directRotation.pitch = (89.9).toFloat()

        var diffAngle = RotationUtils.getRotationDifference(RotationUtils.serverRotation, directRotation)
        if (diffAngle <0) diffAngle = -diffAngle
        if (diffAngle> 180.0) diffAngle = 180.0

        val calculateSpeed = when (rotationSmoothModeValue.get()) {
            "Custom" -> diffAngle / rotationSmoothValue.get()
            "Line" -> (diffAngle / 180) * maxTurnSpeed.get() + (1 - diffAngle / 180) * minTurnSpeed.get()
            //"Quad" -> Math.pow((diffAngle / 180.0), 2.0) * maxTurnSpeedValue.get() + (1 - Math.pow((diffAngle / 180.0), 2.0)) * minTurnSpeedValue.get()
            "Quad" -> (diffAngle / 180.0).pow(2.0) * maxTurnSpeed.get() + (1 - (diffAngle / 180.0).pow(2.0)) * minTurnSpeed.get()
            "Sine" -> (-cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5) * maxTurnSpeed.get() + (cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5) * minTurnSpeed.get()
            //"QuadSine" -> Math.pow(-cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5, 2.0) * maxTurnSpeedValue.get() + (1 - Math.pow(-cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5, 2.0)) * minTurnSpeedValue.get()
            "QuadSine" -> (-cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5).pow(2.0) * maxTurnSpeed.get() + (1 - (-cos(
                diffAngle / 180 * Math.PI
            ) * 0.5 + 0.5).pow(2.0)) * minTurnSpeed.get()
            else -> 180.0
        }

        val rotation = when (rotationModeValue.get()) {
            "LiquidBounce", "ForceCenter" -> RotationUtils.limitAngleChange(RotationUtils.serverRotation, directRotation,
                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())
            "LockView" -> RotationUtils.limitAngleChange(RotationUtils.serverRotation, directRotation, (180.0).toFloat())
            "SmoothCenter", "SmoothLiquid", "OldMatrix" -> RotationUtils.limitAngleChange(RotationUtils.serverRotation, directRotation, (calculateSpeed).toFloat())
            "Test" -> RotationUtils.limitAngleChange(RotationUtils.serverRotation, directRotation, (calculateSpeed).toFloat())
            else -> return true
        }

        if (silentRotationValue.get()) {
            if (rotationRevTickValue.get()> 0 && rotationRevValue.get()) {
                if (keepDirectionValue.get()) {
                    RotationUtils.setTargetRotationReverse(rotation, 0, rotationRevTickValue.get())
                } else {
                    RotationUtils.setTargetRotationReverse(rotation, keepDirectionTickValue.get(), rotationRevTickValue.get())
                }
            } else {
                if (keepDirectionValue.get()) {
                    RotationUtils.setTargetRotation(rotation, keepDirectionTickValue.get())
                } else {
                    RotationUtils.setTargetRotation(rotation, 0)
                }
            }
        } else {
            rotation.toPlayer(mc.thePlayer)
        }
        return true
    }

    /**
     * Check if enemy is hitable with current rotations
     */
    private fun updateHitable() {
        if(hitableValue.get()){
            hitable = true
            return
        }
        // Disable hitable check if turn speed is zero
        if(maxTurnSpeed.get() <= 0F) {
            hitable = true
            return
        }

        val reach = maxRange.toDouble()

        if (raycastValue.get()) {
            val raycastedEntity = RaycastUtils.raycastEntity(reach) {
                (!livingRaycastValue.get() || it is EntityLivingBase && it !is EntityArmorStand) &&
                        (EntityUtils.isSelected(it, true) || raycastIgnoredValue.get() || aacValue.get() && mc.theWorld.getEntitiesWithinAABBExcludingEntity(it, it.entityBoundingBox).isNotEmpty())
            }

            if (raycastValue.get() && raycastedEntity is EntityLivingBase
                && !EntityUtils.isFriend(raycastedEntity))
                currentTarget = raycastedEntity

            hitable = if(maxTurnSpeed.get() > 0F) currentTarget == raycastedEntity else true
        } else
            hitable = RotationUtils.isFaced(currentTarget, reach)
    }

    /**
     * Start blocking
     */
    private fun startBlocking(interactEntity: Entity, interact: Boolean) {
        if (autoBlockValue.get().equals("Normal", true) && mc.thePlayer.getDistanceToEntityBox(interactEntity) > autoBlockRangeValue.get())
            return

        if (blockingStatus)
            return

        if (interact) {
            mc.netHandler.addToSendQueue(C02PacketUseEntity(interactEntity, interactEntity.positionVector))
            mc.netHandler.addToSendQueue(C02PacketUseEntity(interactEntity, C02PacketUseEntity.Action.INTERACT))
        }

        if(blockPacketValue.get().equals("Vanilla", true) || blockPacketValue.get().equals("AfterTick", true) || blockPacketValue.get().equals("AfterAttack", true)) {
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
        }

        if (blockPacketValue.get().equals("NCP", true)) {
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f))
        }

        if (blockPacketValue.get().equals("VulCan", true)) {
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f))
        }

        if (blockPacketValue.get().equals("Hypixel", true)) {
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f))
        }

        if (blockPacketValue.get().equals("KeyBind", true)) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())
        }

        if (blockPacketValue.get().equals("AAC", true)) {
            mc.gameSettings.keyBindUseItem.pressed = true
        }

        if (blockPacketValue.get().equals("Packet", true)) {
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.heldItem))
        }

        if (blockPacketValue.get().equals("AAC5", true)) {

            mc.netHandler.addToSendQueue(
                C08PacketPlayerBlockPlacement(
                    BlockPos(
                        mc.thePlayer.posX,
                        Math.floor(mc.thePlayer.entityBoundingBox.minY),
                        mc.thePlayer.posZ
                    ), 1, mc.thePlayer.inventory.getCurrentItem(), 8F, 16F, 10F
                )
            )
        }

        if (blockPacketValue.get().equals("Normal", true)) {
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
        }

        if (blockPacketValue.get().equals("Other", true)) {
            mc.thePlayer.setItemInUse(mc.thePlayer.inventory.getCurrentItem(), 71999)
        }

        if(blockPacketValue.get().equals("Right", true)) {
            mc.rightClickMouse()
        }

        if (blockPacketValue.get().equals("InteractPacket",true)) {
            mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target)
            PacketUtils.sendPacket(C08PacketPlayerBlockPlacement(mc.thePlayer.heldItem))
        }

        if (blockPacketValue.get().equals("Interact",true)) {
            mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target)
            PacketUtils.sendPacket(C08PacketPlayerBlockPlacement(mc.thePlayer.heldItem))
        }

        blockingStatus = true
    }

    private fun stopBlocking() {
        if (blockingStatus) {

            if (blockPacketValue.get().equals("KeyBind", true)) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false)
                mc.playerController.onStoppedUsingItem(mc.thePlayer)
                mc.thePlayer.itemInUseCount = 0
            }

            if (blockPacketValue.get().equals("AAC", true)) {
                mc.gameSettings.keyBindUseItem.pressed = false
                mc.thePlayer.itemInUseCount = 0
            }

            if (blockPacketValue.get().equals("AAC5", true)) {
                mc.netHandler.addToSendQueue(
                    C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN,
                        EnumFacing.DOWN
                    )
                )
                mc.thePlayer.itemInUseCount = 0
            }

            if (blockPacketValue.get().equals("NCP", true)) {
                mc.netHandler.addToSendQueue(
                    C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN,
                        EnumFacing.DOWN
                    )
                )
            }

            if (blockPacketValue.get().equals("Packet", true)) {
                mc.netHandler.addToSendQueue(C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    if (mc.playerController.sendUseItem(
                            mc.thePlayer, mc.theWorld,
                            mc.thePlayer.inventory.getCurrentItem()
                        )) BlockPos(-1, -1, -1)
                    else BlockPos.ORIGIN,
                    EnumFacing.DOWN
                ))
            }

            if (blockPacketValue.get().equals("Normal", true)) {
                mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
            }

            if (blockPacketValue.get().equals("Other", true)) {
                mc.playerController.onStoppedUsingItem(mc.thePlayer)
            }

            blockingStatus = false

        }
    }

    /**
     * Attack Delay
     */
    private fun getAttackDelay(minCps: Int, maxCps: Int):Long{
        var delay=TimeUtils.randomClickDelay(minCps.coerceAtMost(maxCps), minCps.coerceAtLeast(maxCps))
        if(combatDelayValue.get()){
            var value=4.0
            if(mc.thePlayer.inventory.getCurrentItem()!=null){
                val currentItem=mc.thePlayer.inventory.getCurrentItem().item
                if(currentItem is ItemSword){
                    value-=2.4
                }else if(currentItem is ItemPickaxe){
                    value-=2.8
                }else if(currentItem is ItemAxe){
                    value-=3
                }
            }
            delay=delay.coerceAtLeast((1000 / value).toLong())
        }
        return delay
    }

    /**
     * Check if run should be cancelled
     */
    private val cancelRun: Boolean
        get() = mc.thePlayer.isSpectator || !isAlive(mc.thePlayer)

    /**
     * Check if [entity] is alive
     */
    private fun isAlive(entity: EntityLivingBase) = entity.isEntityAlive && entity.health > 0 ||
            aacValue.get() && entity.hurtTime > 3


    /**
     * Check if player is able to block
     */
    private val canBlock: Boolean
        get() = mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword

    /**
     * Range
     */
    private val maxRange: Float
        get() = max(rangeValue.get(), throughWallsRangeValue.get())

    private fun getRange(entity: Entity) =
        (if (mc.thePlayer.getDistanceToEntityBox(entity) >= throughWallsRangeValue.get()) rangeValue.get() else throughWallsRangeValue.get()) - if (mc.thePlayer.isSprinting) rangeSprintReducementValue.get() else 0F

    /**
     * HUD Tag
     */
    override val tag: String
        get() = targetModeValue.get()
}