package fr.darklash.regensystem.api.zone;

import java.util.Collection;
import java.util.Set;

/**
 * Manager for all {@link RegenZone} instances.
 * <p>
 * This interface is the main entry point for managing zones:
 * - Registering / deleting zones
 * - Accessing zone data
 * - Loading / reloading zones
 * - Checking time left before regeneration
 */
public interface RegenZoneManager {

    /**
     * Gets a zone by its name.
     *
     * @param name the zone name (case-sensitive)
     * @return the zone, or {@code null} if it does not exist
     */
    RegenZone getZone(String name);

    /**
     * Returns all registered zones.
     *
     * @return a collection of zones
     */
    Collection<RegenZone> getZones();

    /**
     * Gets all registered zone names.
     *
     * @return a set of zone names
     */
    Set<String> getZoneNames();

    /**
     * Deletes a zone and removes it from memory.
     *
     * @param name the zone name
     */
    void deleteZone(String name);

    /**
     * Adds a new zone.
     *
     * @param zone the zone instance
     */
    void addZone(RegenZone zone);

    /**
     * Reloads a zone (flags, blocks, configuration...).
     *
     * @param name the zone name
     */
    void reloadZone(String name);

    /**
     * Loads all zones from persistent storage.
     */
    void loadZones();

    /**
     * Gets the remaining time before a zone regenerates.
     *
     * @param zoneName the zone name
     * @return time left in seconds, or -1 if not applicable
     */
    int getTimeLeft(String zoneName);

    /**
     * Checks if a zone is registered.
     *
     * @param name the zone name
     * @return true if registered, false otherwise
     */
    boolean isZoneRegistered(String name);
}
