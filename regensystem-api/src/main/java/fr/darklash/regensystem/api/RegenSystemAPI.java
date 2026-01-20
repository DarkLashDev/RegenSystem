package fr.darklash.regensystem.api;

import fr.darklash.regensystem.api.zone.ZoneManager;
import org.jetbrains.annotations.ApiStatus;

/**
 * <h1>RegenSystemAPI</h1>
 *
 * <p>
 * Central entry point of the <b>RegenSystem public API</b>.
 * This class provides access to all regenerable zones managed by the plugin
 * and exposes versioning information for compatibility checks.
 * </p>
 *
 * <p>
 * External plugins must use this class to interact with RegenSystem.
 * Direct access to internal or core classes is strictly forbidden.
 * </p>
 *
 * <h2>Usage</h2>
 *
 * <pre>{@code
 * if (!RegenSystemAPI.isInitialized()) {
 *     throw new IllegalStateException("RegenSystem is not available");
 * }
 *
 * ZoneManager zones = RegenSystemAPI.getZones();
 * }</pre>
 *
 * <h2>API Versioning</h2>
 *
 * <p>
 * The API follows <b>Semantic Versioning</b>:
 * </p>
 *
 * <ul>
 *   <li><b>MAJOR</b> – Breaking changes</li>
 *   <li><b>MINOR</b> – Backward-compatible additions</li>
 *   <li><b>PATCH</b> – Bug fixes and internal improvements</li>
 * </ul>
 *
 * <p>
 * External plugins are encouraged to check the API version
 * to ensure compatibility.
 * </p>
 *
 * <h2>Thread Safety</h2>
 *
 * <p>
 * All methods exposed by the API are safe to call from the server thread.
 * Any asynchronous behavior is handled internally by RegenSystem.
 * </p>
 *
 * <h2>Important Notes</h2>
 *
 * <ul>
 *   <li>This class cannot be instantiated</li>
 *   <li>The API is initialized automatically by the RegenSystem plugin</li>
 *   <li>Calling {@link #getZones()} before initialization will throw an exception</li>
 * </ul>
 *
 * @author DarkLash
 * @since API 1.0.0
 */
public final class RegenSystemAPI {

    /**
     * Full API version string.
     * <p>
     * Example: {@code "1.0.0"}
     */
    public static final String API_VERSION = "1.0.0";

    /**
     * API major version.
     * <p>
     * Incremented when breaking changes are introduced.
     */
    public static final int API_MAJOR = 1;

    /**
     * API minor version.
     * <p>
     * Incremented when new backward-compatible features are added.
     */
    public static final int API_MINOR = 0;

    /**
     * API patch version.
     * <p>
     * Incremented for bug fixes and internal improvements.
     */
    public static final int API_PATCH = 0;

    /**
     * Internal reference to the zone manager implementation.
     * <p>
     * This field is initialized by the RegenSystem plugin during startup.
     */
    private static ZoneManager manager;

    /**
     * Returns the {@link ZoneManager} used to access and query all registered zones.
     *
     * <p>
     * This is the primary method used by external plugins to interact
     * with RegenSystem zones.
     * </p>
     *
     * <p>
     * If the API has not yet been initialized, this method will throw
     * an {@link IllegalStateException}.
     * </p>
     *
     * @return the public {@link ZoneManager} instance
     * @throws IllegalStateException if the API is not initialized
     */
    public static ZoneManager getZones() {
        if (manager == null) {
            throw new IllegalStateException("RegenSystem API has not been initialized yet.");
        }
        return manager;
    }

    /**
     * Initializes the RegenSystem API.
     *
     * <p>
     * <b>This method is for internal use only</b> and is called automatically
     * by the RegenSystem plugin during its startup phase.
     * </p>
     *
     * <p>
     * External plugins must never call this method.
     * </p>
     *
     * @param impl the internal {@link ZoneManager} implementation
     * @throws IllegalStateException if the API is already initialized
     */
    @ApiStatus.Internal
    public static void init(ZoneManager impl) {
        if (manager != null) {
            throw new IllegalStateException("RegenSystem API already initialized.");
        }
        manager = impl;
    }

    /**
     * Checks whether the RegenSystem API has been initialized.
     *
     * <p>
     * This method can be safely used by external plugins
     * to verify that RegenSystem is loaded and ready.
     * </p>
     *
     * @return {@code true} if the API is initialized, {@code false} otherwise
     */
    public static boolean isInitialized() {
        return manager != null;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private RegenSystemAPI() {}
}
