package fr.darklash.regensystem.api;

import fr.darklash.regensystem.api.zone.RegenZoneManager;

/**
 * Main access point to the RegenSystem API.
 *
 * This class provides access to the loaded implementation of {@link RegenZoneManager}.
 * It is intended to be used by other plugins that depend on RegenSystem.
 */
public final class RegenSystemAPI {

    private static RegenZoneManager instance;

    /**
     * Returns the current instance of the RegenSystem API.
     *
     * @return the API instance
     * @throws IllegalStateException if the API is not yet loaded
     */
    public static RegenZoneManager get() {
        if (instance == null) {
            throw new IllegalStateException("RegenSystem API has not been loaded yet.");
        }
        return instance;
    }

    /**
     * Sets the API implementation. This should only be called by the plugin itself.
     *
     * @param implementation the implementation of the API
     * @throws IllegalStateException if the API has already been initialized
     */
    public static void init(RegenZoneManager implementation) {
        if (instance != null) {
            throw new IllegalStateException("RegenSystem API has already been initialized.");
        }
        instance = implementation;
    }

    private RegenSystemAPI() {
        // Prevent instantiation
    }
}
