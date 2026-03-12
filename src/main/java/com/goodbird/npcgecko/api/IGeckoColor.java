package com.goodbird.npcgecko.api;

/**
 * Interface for color manipulation with RGBA components.
 *
 * <p>Create colors using the factory methods on {@link AbstractGeckoAPI}:</p>
 * <pre>
 * var red = gecko.colorOfRGB(255, 0, 0);
 * var semiTransparent = gecko.colorOfRGBA(255, 0, 0, 128);
 * </pre>
 *
 * @see AbstractGeckoAPI#colorOfRGB
 * @see AbstractGeckoAPI#colorOfRGBA
 */
public interface IGeckoColor {

    /**
     * Get the packed ARGB color value as a single integer.
     *
     * @return the packed ARGB color integer
     */
    int getColor();

    /**
     * Get the red component.
     *
     * @return the red component (0-255)
     */
    int getRed();

    /**
     * Get the green component.
     *
     * @return the green component (0-255)
     */
    int getGreen();

    /**
     * Get the blue component.
     *
     * @return the blue component (0-255)
     */
    int getBlue();

    /**
     * Get the alpha (transparency) component.
     *
     * @return the alpha component (0-255, where 255 is fully opaque)
     */
    int getAlpha();

    /**
     * Create a brighter version of this color.
     *
     * @param factor brightness factor (0.0 = no change, 1.0 = white)
     * @return a new brighter color
     */
    IGeckoColor brighter(double factor);

    /**
     * Create a darker version of this color.
     *
     * @param factor darkness factor (0.0 = no change, 1.0 = black)
     * @return a new darker color
     */
    IGeckoColor darker(double factor);
}
