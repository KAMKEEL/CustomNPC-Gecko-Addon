package com.goodbird.npcgecko.api;

/**
 * Read-only interface for querying the runtime state of a Gecko model.
 * Allows scripts to inspect model bones and their current transforms.
 *
 * <p>This interface provides access to the model's bone hierarchy after
 * animations have been applied, useful for attaching effects or
 * querying positions of specific model parts.</p>
 */
public interface IGeckoModel {

    /**
     * Get a bone by name.
     *
     * @param name The bone name as defined in the model file
     * @return The bone, or null if not found
     */
    IGeckoBone getBone(String name);

    /**
     * Get the names of all bones in this model.
     *
     * @return Array of bone names
     */
    String[] getBoneNames();
}
