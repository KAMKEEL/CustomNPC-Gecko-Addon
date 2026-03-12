package com.goodbird.npcgecko.api;

/**
 * Read-only interface for querying the runtime state of a GeckoLib model.
 * Provides access to the model's bone hierarchy after animations have been applied.
 *
 * <p>Useful for inspecting bone transforms, attaching effects to specific
 * model parts, or querying positions of bones at runtime.</p>
 *
 * @see IGeckoBone
 */
public interface IGeckoModel {

    /**
     * Get a bone by its name as defined in the model file.
     *
     * @param name the bone name
     * @return the bone interface, or null if no bone with that name exists
     */
    IGeckoBone getBone(String name);

    /**
     * Get the names of all bones in this model.
     *
     * @return array of all bone names
     */
    String[] getBoneNames();
}
