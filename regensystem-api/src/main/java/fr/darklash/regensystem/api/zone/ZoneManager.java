package fr.darklash.regensystem.api.zone;

import org.bukkit.Location;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * <h1>ZoneManager</h1>
 *
 * <p>
 * Public API interface used to <b>query and access regenerable zones</b>
 * managed by the RegenSystem plugin.
 * </p>
 *
 * <p>
 * This interface is exposed through {@link fr.darklash.regensystem.api.RegenSystemAPI#getZones()}
 * and must be used by external plugins to interact with zones.
 * </p>
 *
 * <h2>Responsibilities</h2>
 *
 * <ul>
 *   <li>Retrieve zones by name</li>
 *   <li>List all registered zones</li>
 *   <li>Query zones by location, distance, or flags</li>
 *   <li>Access regeneration timers</li>
 * </ul>
 *
 * <h2>Design Notes</h2>
 *
 * <ul>
 *   <li>This interface is <b>read-oriented</b>: it does not allow creating or deleting zones</li>
 *   <li>All returned zones are safe API abstractions</li>
 *   <li>Implementations are managed internally by RegenSystem</li>
 * </ul>
 *
 * <h2>Thread Safety</h2>
 *
 * <p>
 * All methods must be called from the server thread.
 * Internal scheduling and async handling are managed by RegenSystem itself.
 * </p>
 *
 * <h2>Example</h2>
 *
 * <pre>{@code
 * ZoneManager manager = RegenSystemAPI.getZones();
 *
 * Optional<Zone> zone = manager.getZone("mine");
 * zone.ifPresent(z -> {
 *     if (z.contains(player.getLocation())) {
 *         // player is inside the zone
 *     }
 * });
 * }</pre>
 *
 * @author DarkLash
 * @since API 1.0.0
 */
public interface ZoneManager {

    /**
     * Retrieves a zone by its unique name.
     *
     * <p>
     * Zone names are case-sensitive and correspond to the identifiers
     * defined in the configuration files.
     * </p>
     *
     * @param name the zone name
     * @return an {@link Optional} containing the zone if found, otherwise empty
     */
    Optional<Zone> getZone(String name);

    /**
     * Returns all registered zones.
     *
     * <p>
     * The returned collection is a snapshot and should not be modified.
     * </p>
     *
     * @return a collection of all registered zones
     */
    Collection<Zone> getZones();

    /**
     * Returns the names of all registered zones.
     *
     * <p>
     * This method is useful for tab-completion, GUIs, or diagnostics.
     * </p>
     *
     * @return a set containing all zone names
     */
    Set<String> getZoneNames();

    /**
     * Checks whether a zone with the given name exists.
     *
     * @param name the zone name
     * @return {@code true} if the zone exists, {@code false} otherwise
     */
    boolean isRegistered(String name);

    /**
     * Returns the remaining time before the next regeneration
     * of the specified zone.
     *
     * <p>
     * The value is expressed in <b>seconds</b>.
     * </p>
     *
     * @param zoneName the zone name
     * @return remaining time in seconds, or {@code -1} if unavailable
     */
    int getTimeLeft(String zoneName);

    /**
     * Returns all zones that contain the given location.
     *
     * <p>
     * This method supports overlapping zones.
     * </p>
     *
     * @param location the location to test
     * @return a collection of zones containing the location
     */
    Collection<Zone> getZonesContaining(Location location);

    /**
     * Returns all zones that have the given flag enabled.
     *
     * <p>
     * Example use cases:
     * </p>
     * <ul>
     *   <li>Find all PvP-enabled zones</li>
     *   <li>Disable certain behaviors globally</li>
     * </ul>
     *
     * @param flag the {@link ZoneFlag} to check
     * @return a collection of zones where the flag is enabled
     */
    Collection<Zone> getZonesByFlag(ZoneFlag flag);

    /**
     * Returns all zones whose center is within a given radius
     * of the specified location.
     *
     * <p>
     * Distance is calculated using standard Bukkit distance rules
     * and is world-specific.
     * </p>
     *
     * @param location the reference location
     * @param radius the search radius (in blocks)
     * @return a collection of nearby zones
     */
    Collection<Zone> getZonesNear(Location location, int radius);
}
