package net.ccbluex.liquidbounce.event;

public class EventShader extends Event {
    private final boolean bloom;
    private final boolean blur;

    public EventShader(final boolean bloom, final boolean blur) {
        this.bloom = bloom;
        this.blur = blur;
    }

    public boolean onBloom() {
        return bloom;
    }

    public boolean onBlur() {
        return blur;
    }
}
