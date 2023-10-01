package net.ccbluex.liquidbounce.utils.Skid;

import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public abstract class AbstractEntity extends MinecraftInstance {
    /**
     * Position of the entity.
     */
    private float positionX, positionY;
    private float prevDragX = 0, prevDragY = 0;
    private long prevClickTime = System.nanoTime();
    private boolean mouseClicked = false, valueLoaded = false, rightMouseClicked = false;
    public boolean isDragging = false, isHovering = false;
    private float scale = 1F;

    /**
     * Draw the entity.
     */
    protected abstract Border drawElement();

    public AbstractEntity(final float positionX, final float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * @return Create or do nothing.
     */
    public boolean onCreated() {
        return true;
    }

    /**
     * @return PositionX
     */
    public final float getPositionX() {
        return positionX;
    }

    /**
     * @return PositionY
     */
    public final float getPositionY() {
        return positionY;
    }

    /**
     * @param positionX The 'X' Of the entity's position
     */
    public final void setPositionX(final float positionX) {
        this.positionX = positionX;
    }

    /**
     * @param positionY The 'Y' Of the entity's position
     */
    public final void setPositionY(final float positionY) {
        this.positionY = positionY;
    }

    /**
     * @return PrvClickTime
     */
    public final long getPrevClickTime() {
        return prevClickTime;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(final float scale) {
        this.scale = scale;
    }
}
