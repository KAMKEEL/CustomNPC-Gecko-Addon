package com.goodbird.npcgecko.wrapper;

import com.goodbird.npcgecko.api.IGeckoColor;
import software.bernie.geckolib3.core.util.Color;

/**
 * Wrapper around GeckoLib's {@link Color} that implements {@link IGeckoColor}.
 */
public class GeckoColorWrapper implements IGeckoColor {
    private final Color color;

    public GeckoColorWrapper(Color color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color.getColor();
    }

    @Override
    public int getRed() {
        return color.getRed();
    }

    @Override
    public int getGreen() {
        return color.getGreen();
    }

    @Override
    public int getBlue() {
        return color.getBlue();
    }

    @Override
    public int getAlpha() {
        return color.getAlpha();
    }

    @Override
    public IGeckoColor brighter(double factor) {
        return new GeckoColorWrapper(color.brighter(factor));
    }

    @Override
    public IGeckoColor darker(double factor) {
        return new GeckoColorWrapper(color.darker(factor));
    }

    /**
     * Get the underlying GeckoLib Color.
     *
     * @return The wrapped Color
     */
    public Color getInternalColor() {
        return color;
    }
}
