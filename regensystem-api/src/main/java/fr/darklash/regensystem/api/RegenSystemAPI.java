package fr.darklash.regensystem.api;

import fr.darklash.regensystem.api.zone.RegenZoneManager;

/**
 * Main access point to the RegenSystem API.
 * <p>
 * Provides access to the loaded {@link RegenZoneManager} implementation.
 * Other plugins can use this API to manage zones or retrieve zone information.
 */
public final class RegenSystemAPI {

    /**
     * Singleton instance of the {@link RegenZoneManager}.
     * <p>
     * This is set internally when the plugin initializes
     * and can be accessed by other plugins through {@link #get()}.
     */
    private static RegenZoneManager instance;

    /**
     * Returns the current instance of the {@link RegenZoneManager}.
     * <p>
     * This method is the main entry point for other plugins
     * to interact with the RegenSystem API.
     *
     * @return the API instance
     * @throws IllegalStateException if the API has not been initialized yet
     */
    public static RegenZoneManager get() {
        if (instance == null) {
            throw new IllegalStateException("RegenSystem API has not been loaded yet.");
        }
        return instance;
    }

    /**
     * Initializes the API with the provided implementation.
     * <p>
     * This method should only be called internally by the RegenSystem
     * plugin itself during startup. Calling this method more than once
     * will throw an exception to prevent accidental re-initialization.
     *
     * @param impl the implementation of the {@link RegenZoneManager}
     * @throws IllegalStateException if the API has already been initialized
     */
    public static void init(RegenZoneManager impl) {
        if (instance != null) {
            throw new IllegalStateException("RegenSystem API has already been initialized.");
        }
        instance = impl;
    }

    /**
     * Checks whether the API has been initialized.
     * <p>
     * This is useful for other plugins that want to verify
     * if the API is available before attempting to use it.
     *
     * @return {@code true} if the API is initialized, {@code false} otherwise
     */
    public static boolean isInitialized() {
        return instance != null;
    }

    /**
     * Returns the current version of the RegenSystem API.
     * <p>
     * This can be used by other plugins to ensure compatibility.
     *
     * @return the API version string (e.g., "0.0.1")
     */
    public static String getVersion() {
        return "0.0.1";
    }

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This class is a static utility holder and should never be instantiated.
     */
    private RegenSystemAPI() {
        // Prevent instantiation
    }
}
