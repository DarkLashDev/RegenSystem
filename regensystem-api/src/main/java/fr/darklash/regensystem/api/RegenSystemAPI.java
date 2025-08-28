package fr.darklash.regensystem.api;

import fr.darklash.regensystem.api.zone.RegenZoneManager;

/**
 * Main access point to the RegenSystem API.
 * <p>
 * Provides access to the loaded {@link RegenZoneManager} implementation.
 * Other plugins can use this API to manage zones or retrieve zone information.
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
     * Sets the API implementation. This should only be called internally by RegenSystem.
     *
     * @param impl the implementation of the API
     * @throws IllegalStateException if the API has already been initialized
     */
    public static void init(RegenZoneManager impl) {
        if (instance != null) {
            throw new IllegalStateException("RegenSystem API has already been initialized.");
        }
        instance = impl;
    }

    private RegenSystemAPI() {
        // Prevent instantiation
    }
}
