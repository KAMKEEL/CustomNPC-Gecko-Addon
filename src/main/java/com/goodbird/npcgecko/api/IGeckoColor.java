package com.goodbird.npcgecko.api;

/**
 * Interface for color manipulation. Provides access to RGBA color components
 * and common color operations.
 *
 * <p>Create colors using the factory methods on {@link AbstractGeckoAPI}:</p>
 * <pre>
 * var color = GeckoAPI.Instance().colorOfRGB(255, 0, 0); // Red
 * var alpha = GeckoAPI.Instance().colorOfRGBA(255, 0, 0, 128); // Semi-transparent red
 * </pre>
 */
public interface IGeckoColor {

    /**
     * Get the packed ARGB color value.
     *
     * @return The packed color as an integer
     */
    int getColor();

    /**
     * @return The red component (0-255)
     */
    int getRed();

    /**
     * @return The green component (0-255)
     */
    int getGreen();

    /**
     * @return The blue component (0-255)
     */
    int getBlue();

    /**
     * @return The alpha component (0-255)
     */
    int getAlpha();

    /**
     * Create a brighter version of this color.
     *
     * @param factor How much brighter (0.0 = no change, 1.0 = white)
     * @return A new brighter color
     */
    IGeckoColor brighter(double factor);

    /**
     * Create a darker version of this color.
     *
     * @param factor How much darker (0.0 = no change, 1.0 = black)
     * @return A new darker color
     */
    IGeckoColor darker(double factor);
}
