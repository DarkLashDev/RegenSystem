package fr.darklash.regensystem.api.zone;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

import java.time.Duration;
import java.util.Map;

/**
 * Represents a regenerable zone in the world.
 * <p>
 * A zone is defined by two corners and contains block data,
 * flags, and regeneration logic. Developers can attach custom
 * conditions and actions to zones using the API.
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

    /**
     * Teleports all players currently inside this zone to a safe location outside of it.
     */
    void teleportPlayersOut();

    /**
     * Removes all entities of the specified types within this zone.
     *
     * @param types the entity types to remove (e.g., EntityType.ZOMBIE)
     */
    void clearEntities(EntityType... types);

    /**
     * Displays the borders of this zone using particles for a specified duration.
     *
     * @param particle the type of particle to display
     * @param duration how long to show the particle effect
     */
    void highlight(Particle particle, Duration duration);

    /**
     * Counts the number of players currently inside this zone.
     *
     * @return the number of players inside the zone
     */
    int countPlayersInside();

    /**
     * Converts this zone into a JSON string.
     *
     * @return the JSON representation of this zone
     */
    String toJson();

    /**
     * Creates a zone from a JSON string.
     *
     * @param json the JSON representation of a zone
     * @return a new RegenZone instance
     */
    static RegenZone fromJson(String json) {
        throw new UnsupportedOperationException("Implement in concrete class");
    }

    /**
     * Adds a condition to this zone.
     * <p>
     * Conditions are tested when players interact with or enter the zone.
     * If a condition returns false, associated actions may not execute.
     *
     * @param condition the {@link RegenZoneCondition} to add
     */
    void addCondition(RegenZoneCondition condition);

    /**
     * Adds an action to this zone.
     * <p>
     * Actions are executed when conditions are satisfied or specific events occur.
     *
     * @param action the {@link RegenZoneAction} to add
     */
    void addAction(RegenZoneAction action);

    /**
     * Removes a previously added condition from this zone.
     *
     * @param condition the {@link RegenZoneCondition} to remove
     */
    void removeCondition(RegenZoneCondition condition);

    /**
     * Removes a previously added action from this zone.
     *
     * @param action the {@link RegenZoneAction} to remove
     */
    void removeAction(RegenZoneAction action);
}
