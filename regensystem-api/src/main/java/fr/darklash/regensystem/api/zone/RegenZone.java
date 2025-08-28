package fr.darklash.regensystem.api.zone;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import java.util.Map;

/**
 * Represents a regenerable zone in the world.
 * <p>
 * A zone is defined by two corners and contains block data,
 * flags, and regeneration logic.
 */
public interface RegenZone {

    /**
     * Gets the unique name of this zone.
     *
     * @return the zone name
     */
    String getName();

    /**
     * Gets the first corner of the zone.
     *
     * @return a {@link Location} representing one corner
     */
    Location getCorner1();

    /**
     * Gets the second corner of the zone.
     *
     * @return a {@link Location} representing the opposite corner
     */
    Location getCorner2();

    /**
     * Gets the central location of the zone.
     *
     * @return the center {@link Location}
     */
    Location getCenter();

    /**
     * Checks whether this zone is currently enabled.
     *
     * @return true if the zone is enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Checks whether a location is contained within this zone.
     *
     * @param location the location to check
     * @return true if the location is inside the zone, false otherwise
     */
    boolean contains(Location location);

    /**
     * Regenerates all blocks in the zone to their original state.
     * <p>
     * Fires {@link fr.darklash.regensystem.api.event.ZonePreRegenEvent}
     * before regeneration and {@link fr.darklash.regensystem.api.event.ZonePostRegenEvent} after.
     */
    void regenerate();

    /**
     * Captures the current state of all blocks in the zone.
     * <p>
     * This is used to save the original state for future regeneration.
     */
    void captureState();

    /**
     * Saves the zone configuration and captured state to disk.
     */
    void save();

    /**
     * Loads the zone configuration and state from disk.
     */
    void load();

    /**
     * Saves the flags of this zone.
     */
    void saveFlags();

    /**
     * Loads the flags of this zone.
     */
    void loadFlags();

    /**
     * Checks if the zone has a specific flag enabled.
     *
     * @param flag the {@link RegenZoneFlag} to check
     * @return true if the flag is enabled, false otherwise
     */
    boolean hasFlag(RegenZoneFlag flag);

    /**
     * Sets a specific flag for this zone.
     *
     * @param flag  the {@link RegenZoneFlag} to set
     * @param value true to enable the flag, false to disable
     */
    void setFlag(RegenZoneFlag flag, boolean value);

    /**
     * Gets all flags and their current state for this zone.
     *
     * @return a map of {@link RegenZoneFlag} to boolean indicating enabled/disabled
     */
    Map<RegenZoneFlag, Boolean> getFlags();

    /**
     * Gets a map of all original blocks in the zone.
     * <p>
     * The key is a string representing the block location (e.g., "x:y:z").
     *
     * @return a map of location strings to {@link BlockData}
     */
    Map<String, BlockData> getOriginalBlocks();

    /**
     * Gets a map of serialized block data for all original blocks in the zone.
     * <p>
     * This is useful for saving to configuration files.
     *
     * @return a map of location strings to serialized block data strings
     */
    Map<String, String> getOriginalBlockData();
}
