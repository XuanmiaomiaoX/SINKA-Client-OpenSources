package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.*
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.InventoryHelper.serverSlot
import net.ccbluex.liquidbounce.utils.MathUtils.toRadians
import net.ccbluex.liquidbounce.utils.PacketUtils.sendPacket
import net.ccbluex.liquidbounce.utils.RenderUtil.drawBorderedRect
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.RotationUtils.*
import net.ccbluex.liquidbounce.utils.block.BlockUtils.canBeClicked
import net.ccbluex.liquidbounce.utils.block.BlockUtils.isReplaceable
import net.ccbluex.liquidbounce.utils.block.PlaceInfo
import net.ccbluex.liquidbounce.utils.misc.RandomUtils.nextFloat
import net.ccbluex.liquidbounce.utils.render.RenderUtils.drawBlockBox
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.timer.TimeUtils.randomClickDelay
import net.ccbluex.liquidbounce.utils.timer.TimeUtils.randomDelay
import net.ccbluex.liquidbounce.value.*
import net.minecraft.block.BlockBush
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager.resetColor
import net.minecraft.client.settings.GameSettings
import net.minecraft.item.ItemBlock
import net.minecraft.network.play.client.*
import net.minecraft.network.play.client.C0BPacketEntityAction.Action.*
import net.minecraft.util.*
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import kotlin.math.*

@ModuleInfo("BlockFly", "Allow you bridging automatically.", ModuleCategory.WORLD)
class BlockFly : Module() {
    private val mode by ListValue("Mode", arrayOf("Normal", "Rewinside", "Expand"), "Normal")

    // Expand
    private val omniDirectionalExpand by BoolValue("OmniDirectionalExpand", false).displayable { mode == "Expand" }
    private val expandLength by IntegerValue("ExpandLength", 1, 1, 6).displayable { mode == "Expand" }

    // Placeable delay
    private val placeDelay by BoolValue("PlaceDelay", true)
    private val maxDelayValue: IntegerValue = object : IntegerValue("MaxDelay", 0, 0, 1000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            set(newValue.coerceAtLeast(minDelay))
        }
    }.displayable { placeDelay } as IntegerValue
    private val maxDelay by maxDelayValue

    private val minDelay by object : IntegerValue("MinDelay", 0, 0, 1000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            set(newValue.coerceAtMost(maxDelay))
        }
    }.displayable { placeDelay } as IntegerValue

    // Extra clicks
    private val extraClicks by BoolValue("DoExtraClicks", false)

    private val extraClickMaxCPSValue: IntegerValue = object : IntegerValue("ExtraClickMaxCPS", 7, 0, 50) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            set(newValue.coerceAtLeast(extraClickMinCPS))
        }
    }.displayable {extraClicks} as IntegerValue
    private val extraClickMaxCPS by extraClickMaxCPSValue

    private val extraClickMinCPS by object : IntegerValue("ExtraClickMinCPS", 3, 0, 50) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            set(newValue.coerceAtMost(extraClickMaxCPS))
        }
    }.displayable {extraClicks} as IntegerValue

    private val placementAttempt by ListValue("PlacementAttempt", arrayOf("Fail", "Independent"), "Fail").displayable { extraClicks }

    // Autoblock
    private val autoBlock by ListValue("AutoBlock", arrayOf("Off", "Pick", "Spoof", "Switch"), "Spoof")

    // Basic stuff
    val sprint by BoolValue("Sprint", false)
    private val swing by BoolValue("Swing", true)
    private val search by BoolValue("Search", true)
    private val down by BoolValue("Down", true)

    // Eagle
    private val eagle by ListValue("Eagle", arrayOf("Normal", "Silent", "Off"), "Normal")
    private val blocksToEagle by IntegerValue("BlocksToEagle", 0, 0, 10).displayable { eagle != "Off" }
    private val edgeDistance by FloatValue("EagleEdgeDistance", 0f, 0f, 0.5f).displayable { eagle != "Off" }

    // Rotation Options
    private val rotations by BoolValue("Rotations", true)
    private val strafe by BoolValue("Strafe", false)
    private val stabilizedRotation by BoolValue("StabilizedRotation", false)
    private val silentRotation by BoolValue("SilentRotation", true)
    private val keepRotation by BoolValue("KeepRotation", true)
    private val keepTicks by object : IntegerValue("KeepTicks", 1, 1,20) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            set(newValue.coerceAtLeast(minimum))
        }
    }

    // Search options
    private val searchMode by ListValue("SearchMode", arrayOf("Area", "Center"), "Area")
    private val minDist by FloatValue("MinDist", 0f, 0f,0.2f)

    // Turn Speed
    private val maxTurnSpeedValue: FloatValue = object : FloatValue("MaxTurnSpeed", 180f, 1f, 180f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            set(newValue.coerceAtLeast(minTurnSpeed))
        }
    }
    private val maxTurnSpeed by maxTurnSpeedValue
    private val minTurnSpeed by object : FloatValue("MinTurnSpeed", 180f, 1f, 180f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            set(newValue.coerceAtMost(maxTurnSpeed))
        }
    }

    private val angleThresholdUntilReset by FloatValue("AngleThresholdUntilReset", 5f, 0.1f, 180f)

    // Zitter
    private val zitterMode by ListValue("Zitter", arrayOf("Off", "Teleport", "Smooth"), "Off")
    private val zitterSpeed by FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f).displayable { zitterMode == "Teleport" }
    private val zitterStrength by FloatValue("ZitterStrength", 0.05f, 0f, 0.2f).displayable { zitterMode == "Teleport" }

    // Game
    private val timer by FloatValue("Timer", 1f, 0.1f, 10f)
    private val speedModifier by FloatValue("SpeedModifier", 1f, 0f, 2f)
    private val keepRotationValue by IntegerValue("keepRotationValue",20,0,20)
    private val slow by BoolValue("Slow", false)
    private val slowSpeed by FloatValue("SlowSpeed", 0.6f, 0.2f, 0.8f).displayable { slow }

    // Safety
    private val sameY by BoolValue("SameY", false)
    private val safeWalk by BoolValue("SafeWalk", true)
    private val airSafe by BoolValue("AirSafe", false).displayable { safeWalk }

    // Visuals
    private val counterDisplay by BoolValue("Counter", true)
    private val mark by BoolValue("Mark", false)

    // Target placement
    private var targetPlace: PlaceInfo? = null

    // Launch position
    private var launchY = 0

    // Zitter Direction
    private var zitterDirection = false

    // Delay
    private val delayTimer = MSTimer()
    private val zitterTimer = MSTimer()
    private var delay = 0

    // Eagle
    private var placedBlocksWithoutEagle = 0
    private var eagleSneaking = false

    // Downwards
    private val shouldGoDown
        get() = down && !sameY && GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && blocksAmount > 1

    // Current rotation
    private val currRotation
        get() = targetRotation ?: serverRotation

    // Extra clicks
    private var extraClick = ExtraClickInfo(randomClickDelay(extraClickMinCPS, extraClickMaxCPS).toInt(), 0L, 0)

    // Enabling module
    override fun onEnable() {
        val player = mc.thePlayer ?: return

        launchY = player.posY.roundToInt()
    }

    // Events
    @EventTarget
    private fun onUpdate(event: UpdateEvent) {
        val player = mc.thePlayer ?: return

        mc.timer.timerSpeed = timer

        if (shouldGoDown) {
            mc.gameSettings.keyBindSneak.pressed = false
        }

        if (slow) {
            player.motionX *= slowSpeed
            player.motionZ *= slowSpeed
        }

        // Eagle
        if (eagle != "Off" && !shouldGoDown) {
            var dif = 0.5
            val blockPos = BlockPos(player).down()

            for (side in EnumFacing.values()) {
                if (side.axis == EnumFacing.Axis.Y) {
                    continue
                }

                val neighbor = blockPos.offset(side)

                if (isReplaceable(neighbor)) {
                    val calcDif = (if (side.axis == EnumFacing.Axis.Z) {
                        abs(neighbor.z + 0.5 - player.posZ)
                    } else {
                        abs(neighbor.x + 0.5 - player.posX)
                    }) - 0.5

                    if (calcDif < dif) {
                        dif = calcDif
                    }
                }
            }

            if (placedBlocksWithoutEagle >= blocksToEagle) {
                val shouldEagle = isReplaceable(blockPos) || dif < edgeDistance
                if (eagle == "Silent") {
                    if (eagleSneaking != shouldEagle) {
                        sendPacket(C0BPacketEntityAction(player, if (shouldEagle) START_SNEAKING else STOP_SNEAKING))
                    }
                    eagleSneaking = shouldEagle
                } else {
                    mc.gameSettings.keyBindSneak.pressed = shouldEagle
                }
                placedBlocksWithoutEagle = 0
            } else {
                placedBlocksWithoutEagle++
            }
        }

        if (player.onGround) {
            if (mode == "Rewinside") {
                MovementUtils.strafe(0.2F)
                player.motionY = 0.0
            }
            when (zitterMode.toLowerCase()) {
                "off" -> {
                    return
                }

                "smooth" -> {
                    if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight)) {
                        mc.gameSettings.keyBindRight.pressed = false
                    }
                    if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft)) {
                        mc.gameSettings.keyBindLeft.pressed = false
                    }

                    if (zitterTimer.hasTimePassed(100)) {
                        zitterDirection = !zitterDirection
                        zitterTimer.reset()
                    }

                    if (zitterDirection) {
                        mc.gameSettings.keyBindRight.pressed = true
                        mc.gameSettings.keyBindLeft.pressed = false
                    } else {
                        mc.gameSettings.keyBindRight.pressed = false
                        mc.gameSettings.keyBindLeft.pressed = true
                    }
                }

                "teleport" -> {
                    MovementUtils.strafe(zitterSpeed)
                    val yaw = (player.rotationYaw + if (zitterDirection) 90.0 else -90.0).toRadians()
                    player.motionX -= sin(yaw) * zitterStrength
                    player.motionZ += cos(yaw) * zitterStrength
                    zitterDirection = !zitterDirection
                }
            }
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val rotation = targetRotation

        if (rotations && keepRotation && rotation != null) {
            setRotation(rotation, 1)
        }

        if (event.eventState == EventState.POST) {
            update()
        }
    }

    @EventTarget
    fun onTick(event: TickEvent) {
        mc.thePlayer ?: return
        mc.theWorld ?: return
        val target = targetPlace

        if (extraClicks) {
            while (extraClick.clicks > 0) {
                extraClick.clicks--

                doPlaceAttempt()
            }
        }

        if (target == null) {
            if (placeDelay) {
                delayTimer.reset()
            }
            return
        }

        val raycastProperly = !(mode == "Expand" && expandLength > 1 || shouldGoDown)

        performBlockRaytrace(currRotation, mc.playerController.blockReachDistance).let {
            if (it != null && it.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && it.blockPos == target.blockPos && (!raycastProperly || it.sideHit == target.enumFacing)) {
                val result = if (raycastProperly) {
                    PlaceInfo(it.blockPos, it.sideHit, it.hitVec)
                } else {
                    target
                }

                place(result)
            }
        }
    }

    fun update() {
        val player = mc.thePlayer ?: return
        val holdingItem = player.heldItem?.item is ItemBlock

        if (!holdingItem && (autoBlock == "Off" || InventoryUtils.findAutoBlockBlock() == -1)) {
            return
        }

        findBlock(mode == "Expand" && expandLength > 1, searchMode == "Area")
    }

    private fun setRotation(rotation: Rotation, ticks: Int) {
        val player = mc.thePlayer ?: return

        if (silentRotation) {
            if(keepRotation){
                setTargetRotation(rotation, keepRotationValue)
            }else{
                setTargetRotation(rotation, ticks)
            }
        } else {
            rotation.toPlayer(player)
        }
    }

    // Search for new target block
    private fun findBlock(expand: Boolean, area: Boolean) {
        val player = mc.thePlayer ?: return

        val blockPosition = if (shouldGoDown) {
            if (player.posY == player.posY.roundToInt() + 0.5) {
                BlockPos(player.posX, player.posY - 0.6, player.posZ)
            } else {
                BlockPos(player.posX, player.posY - 0.6, player.posZ).down()
            }
        } else if (sameY && launchY <= player.posY) {
            BlockPos(player.posX, launchY - 1.0, player.posZ)
        } else if (player.posY == player.posY.roundToInt() + 0.5) {
            BlockPos(player)
        } else {
            BlockPos(player).down()
        }

        if (!expand && (!isReplaceable(blockPosition) || search(blockPosition, !shouldGoDown, area))) {
            return
        }

        if (expand) {
            val yaw = player.rotationYaw.toRadians()
            val x = if (omniDirectionalExpand) -sin(yaw).roundToInt() else player.horizontalFacing.directionVec.x
            val z = if (omniDirectionalExpand) cos(yaw).roundToInt() else player.horizontalFacing.directionVec.z
            for (i in 0 until expandLength) {
                if (search(blockPosition.add(x * i, 0, z * i), false, area)) {
                    return
                }
            }
        } else if (search) {
            for (x in -1..1) {
                for (z in -1..1) {
                    if (search(blockPosition.add(x, 0, z), !shouldGoDown, area)) {
                        return
                    }
                }
            }
        }
    }

    private fun place(placeInfo: PlaceInfo) {
        val player = mc.thePlayer ?: return
        val world = mc.theWorld ?: return

        if (!delayTimer.hasTimePassed(delay.toLong()) || sameY && launchY - 1 != placeInfo.vec3.yCoord.toInt()) {
            return
        }

        var itemStack = player.heldItem
        //TODO: blacklist more blocks than only bushes
        if (itemStack == null || itemStack.item !is ItemBlock || (itemStack.item as ItemBlock).block is BlockBush || player.heldItem.stackSize <= 0) {
            val blockSlot = InventoryUtils.findAutoBlockBlock()
            if (blockSlot == -1) return

            when (autoBlock.toLowerCase()) {
                "off" -> return

                "pick" -> {
                    player.inventory.currentItem = blockSlot - 36
                    mc.playerController.updateController()
                }

                "spoof", "switch" -> {
                    if (blockSlot - 36 != serverSlot) {
                        sendPacket(C09PacketHeldItemChange(blockSlot - 36))
                    }
                }
            }
            itemStack = player.inventoryContainer.getSlot(blockSlot).stack
        }

        if (mc.playerController.onPlayerRightClick(
                player, world, itemStack, placeInfo.blockPos, placeInfo.enumFacing, placeInfo.vec3
            )
        ) {
            delayTimer.reset()
            delay = if (!placeDelay) 0 else randomDelay(minDelay, maxDelay).toInt()

            if (player.onGround) {
                player.motionX *= speedModifier
                player.motionZ *= speedModifier
            }

            if (swing) {
                player.swingItem()
            } else {
                sendPacket(C0APacketAnimation())
            }
        } else {
            if (mc.playerController.sendUseItem(player, world, itemStack)) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress2()
            }
        }

        if (autoBlock == "Switch") {
            if (serverSlot != player.inventory.currentItem) {
                sendPacket(C09PacketHeldItemChange(player.inventory.currentItem))
            }
        }

        targetPlace = null
    }

    private fun doPlaceAttempt() {
        val player = mc.thePlayer ?: return
        val world = mc.theWorld ?: return

        val stack = player.inventoryContainer.getSlot(serverSlot + 36).stack ?: return

        if (stack.item !is ItemBlock || InventoryUtils.BLOCK_BLACKLIST.contains((stack.item as ItemBlock).block)) {
            return
        }

        val block = stack.item as ItemBlock

        val raytrace = performBlockRaytrace(currRotation, mc.playerController.blockReachDistance) ?: return

        val isOnTheSamePos = raytrace.blockPos.x == player.posX.toInt() && raytrace.blockPos.z == player.posZ.toInt()

        val isBlockBelowPlayer = if (sameY) {
            raytrace.blockPos.y == launchY - 1 && !block.canPlaceBlockOnSide(
                world, raytrace.blockPos, EnumFacing.UP, player, stack
            )
        } else {
            raytrace.blockPos.y <= player.posY - 1 && (placementAttempt == "Independent" && isOnTheSamePos || !block.canPlaceBlockOnSide(
                world, raytrace.blockPos, EnumFacing.UP, player, stack
            ))
        }

        val shouldPlace = placementAttempt == "Independent" || !block.canPlaceBlockOnSide(
            world, raytrace.blockPos, raytrace.sideHit, player, stack
        )

        if (raytrace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !isBlockBelowPlayer || !shouldPlace) {
            return
        }

        if (mc.playerController.onPlayerRightClick(
                player, world, stack, raytrace.blockPos, raytrace.sideHit, raytrace.hitVec
            )
        ) {
            if (swing) {
                player.swingItem()
            } else {
                sendPacket(C0APacketAnimation())
            }
        } else {
            if (mc.playerController.sendUseItem(player, world, stack)) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress2()
            }
        }
    }

    // Disabling module
    override fun onDisable() {
        val player = mc.thePlayer ?: return

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
            mc.gameSettings.keyBindSneak.pressed = false
            if (eagleSneaking) {
                sendPacket(C0BPacketEntityAction(player, STOP_SNEAKING))
            }
        }

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight)) {
            mc.gameSettings.keyBindRight.pressed = false
        }
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft)) {
            mc.gameSettings.keyBindLeft.pressed = false
        }

        targetPlace = null
        mc.timer.timerSpeed = 1f

        if (serverSlot != player.inventory.currentItem) {
            sendPacket(C09PacketHeldItemChange(player.inventory.currentItem))
        }
    }

    // Entity movement event
    @EventTarget
    fun onMove(event: MoveEvent) {
        val player = mc.thePlayer ?: return

        if (!safeWalk || shouldGoDown) {
            return
        }

        if (airSafe || player.onGround) {
            event.isSafeWalk = true
        }
    }

    // Scaffold visuals
    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (counterDisplay) {
            glPushMatrix()
            LiquidBounce.moduleManager.getModule(BlockOverlay::class.java)?.let {
                it as BlockOverlay
                if (it.state && it.infoValue.get() && it.currentBlock != null) glTranslatef(0f, 15f, 0f)
            }

            val info = "Blocks: §7$blocksAmount"
            val resolution = ScaledResolution(mc)
            val width = resolution.scaledWidth
            val height = resolution.scaledHeight

            drawBorderedRect(
                width / 2 - 2f,
                height / 2 + 5f,
                width / 2 + Fonts.font40.getStringWidth(info) + 2f,
                height / 2 + 16f,
                3f,
                Color.BLACK.rgb,
                Color.BLACK.rgb
            )

            resetColor()

            Fonts.font40.drawString(
                info, width / 2, height / 2 + 7, Color.WHITE.rgb
            )
            glPopMatrix()
        }
    }

    // Visuals
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        val player = mc.thePlayer ?: return

        val shouldBother =
            !(shouldGoDown || mode == "Expand" && expandLength > 1) && extraClicks && MovementUtils.isMoving()

        if (shouldBother) {
            currRotation.let {
                performBlockRaytrace(it, mc.playerController.blockReachDistance)?.let { raytrace ->
                    val timePassed = System.currentTimeMillis() - extraClick.lastClick >= extraClick.delay

                    if (raytrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && timePassed) {
                        extraClick = ExtraClickInfo(
                            randomClickDelay(extraClickMinCPS, extraClickMaxCPS).toInt(),
                            System.currentTimeMillis(),
                            extraClick.clicks + 1
                        )
                    }
                }
            }
        }

        if (!mark) {
            return
        }

        for (i in 0 until if (mode == "Expand") expandLength + 1 else 2) {
            val yaw = player.rotationYaw.toRadians()
            val x = if (omniDirectionalExpand) -sin(yaw).roundToInt() else player.horizontalFacing.directionVec.x
            val z = if (omniDirectionalExpand) cos(yaw).roundToInt() else player.horizontalFacing.directionVec.z
            val blockPos = BlockPos(
                player.posX + x * i,
                if (sameY && launchY <= player.posY) launchY - 1.0 else player.posY - (if (player.posY == player.posY + 0.5) 0.0 else 1.0) - if (shouldGoDown) 1.0 else 0.0,
                player.posZ + z * i
            )
            val placeInfo = PlaceInfo.get(blockPos)

            if (isReplaceable(blockPos) && placeInfo != null) {
                drawBlockBox(blockPos, Color(68, 117, 255, 100), false)
                break
            }
        }
    }

    /**
     * Search for placeable block
     *
     * @param blockPosition pos
     * @param raycast visible
     * @param area spot
     * @return
     */

    private fun search(blockPosition: BlockPos, raycast: Boolean, area: Boolean): Boolean {
        val player = mc.thePlayer ?: return false

        if (!isReplaceable(blockPosition)) {
            return false
        }

        val maxReach = mc.playerController.blockReachDistance

        val eyes = player.getPositionEyes(1f)
        var placeRotation: PlaceRotation? = null

        var currPlaceRotation: PlaceRotation?

        for (side in EnumFacing.values()) {
            val neighbor = blockPosition.offset(side)

            if (!canBeClicked(neighbor)) {
                continue
            }

            if (!area) {
                currPlaceRotation =
                    findTargetPlace(blockPosition, neighbor, Vec3(0.5, 0.5, 0.5), side, eyes, maxReach, raycast)
                        ?: continue

                if (placeRotation == null || getRotationDifference(
                        currPlaceRotation.rotation, currRotation
                    ) < getRotationDifference(placeRotation.rotation, currRotation)
                ) {
                    placeRotation = currPlaceRotation
                }
            } else {
                var x = 0.0
                var y = 0.0
                var z = 0.0
                val maximum = 0.9
                while (x < maximum) {
                    x += 0.1
                    while (y < maximum) {
                        y += 0.1
                        while (z < maximum) {
                            z += 0.1
                            currPlaceRotation =
                                findTargetPlace(blockPosition, neighbor, Vec3(x, y, z), side, eyes, maxReach, raycast)
                                    ?: continue

                            if (placeRotation == null ||
                                getRotationDifference(currPlaceRotation.rotation, currRotation)
                                < getRotationDifference(placeRotation.rotation, currRotation)
                            ) placeRotation = currPlaceRotation
                        }
                    }
                }
            }
        }

        placeRotation ?: return false

        if (rotations) {
            val limitedRotation = limitAngleChange(
                currRotation, placeRotation.rotation, nextFloat(minTurnSpeed, maxTurnSpeed)
            )

            setRotation(limitedRotation, keepTicks)
        }
        targetPlace = placeRotation.placeInfo
        return true
    }

    /**
     * For expand scaffold, fixes vector values that should match according to direction vector
     */
    private fun modifyVec(original: Vec3, direction: EnumFacing, pos: Vec3, shouldModify: Boolean): Vec3 {
        if (!shouldModify) {
            return original
        }

        val x = original.xCoord
        val y = original.yCoord
        val z = original.zCoord

        val side = direction.opposite

        return when (side.axis ?: return original) {
            EnumFacing.Axis.Y -> Vec3(x, pos.yCoord + side.directionVec.y.coerceAtLeast(0), z)
            EnumFacing.Axis.X -> Vec3(pos.xCoord + side.directionVec.x.coerceAtLeast(0), y, z)
            EnumFacing.Axis.Z -> Vec3(x, y, pos.zCoord + side.directionVec.z.coerceAtLeast(0))
        }

    }

    private fun findTargetPlace(
        pos: BlockPos, offsetPos: BlockPos, vec3: Vec3, side: EnumFacing, eyes: Vec3, maxReach: Float, raycast: Boolean
    ): PlaceRotation? {
        val world = mc.theWorld ?: return null

        val vec = (Vec3(pos) + vec3).addVector(
            side.directionVec.x * vec3.xCoord, side.directionVec.y * vec3.yCoord, side.directionVec.z * vec3.zCoord
        )

        val distance = eyes.distanceTo(vec)

        if (raycast && (distance > maxReach || world.rayTraceBlocks(eyes, vec, false, true, false) != null)) {
            return null
        }

        val diff = vec - eyes

        if (side.axis != EnumFacing.Axis.Y) {
            val dist = abs((if (side.axis == EnumFacing.Axis.Z) diff.zCoord else diff.xCoord))

            if (dist < minDist) {
                return null
            }
        }

        var rotation = toRotation(vec, false)

        rotation = if (stabilizedRotation) {
            Rotation(round(rotation.yaw / 45f) * 45f, rotation.pitch)
        } else {
            rotation
        }

        // If the current rotation already looks at the target block and side, then return right here
        performBlockRaytrace(currRotation, maxReach)?.let { raytrace ->
            if (raytrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && raytrace.blockPos == offsetPos && (!raycast || raytrace.sideHit == side.opposite)) {
                return PlaceRotation(
                    PlaceInfo(
                        raytrace.blockPos, side.opposite, modifyVec(raytrace.hitVec, side, Vec3(offsetPos), !raycast)
                    ), currRotation
                )
            }
        }

        val raytrace = performBlockRaytrace(rotation, maxReach) ?: return null

        if (raytrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && raytrace.blockPos == offsetPos && (!raycast || raytrace.sideHit == side.opposite)) {
            return PlaceRotation(
                PlaceInfo(
                    raytrace.blockPos, side.opposite, modifyVec(raytrace.hitVec, side, Vec3(offsetPos), !raycast)
                ), rotation
            )
        }

        return null
    }

    private fun performBlockRaytrace(rotation: Rotation, maxReach: Float): MovingObjectPosition? {
        val player = mc.thePlayer ?: return null
        val world = mc.theWorld ?: return null

        val eyes = player.getPositionEyes(1f)
        val rotationVec = getVectorForRotation(rotation)

        val reach = eyes + (rotationVec * maxReach.toDouble())

        return world.rayTraceBlocks(eyes, reach, false, false, true)
    }

    /**
     * Returns the amount of blocks
     */
    private val blocksAmount: Int
        get() {
            var amount = 0
            for (i in 36..44) {
                val stack = mc.thePlayer.inventoryContainer.getSlot(i).stack ?: continue
                val item = stack.item
                if (item is ItemBlock) {
                    val block = item.block
                    val heldItem = mc.thePlayer.heldItem
                    if (heldItem != null && heldItem == stack || block !in InventoryUtils.BLOCK_BLACKLIST && block !is BlockBush) {
                        amount += stack.stackSize
                    }
                }
            }
            return amount
        }
    override val tag
        get() = mode

    data class ExtraClickInfo(val delay: Int, val lastClick: Long, var clicks: Int)
}

private operator fun Vec3.times(d: Double): Vec3 {
    return Vec3(this.xCoord * d, this.yCoord * d, this.zCoord * d)
}

private operator fun Vec3.minus(vec3: Vec3): Vec3 {
    return this.addVector(-vec3.xCoord, -vec3.yCoord, -vec3.zCoord)
}

private operator fun Vec3.plus(vec3: Vec3): Vec3 {
    return this.add(vec3)
}
