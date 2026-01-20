package fr.darklash.regensystem.api.zone;

import fr.darklash.regensystem.api.regen.RegenResult;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.time.Duration;
import java.util.Map;

/**
 * <h1>Zone</h1>
 *
 * <p>
 * Represents a <b>regenerable cuboid zone</b> in the world.
 * A zone is defined by two corners and is capable of storing block data,
 * handling regeneration logic, and enforcing gameplay rules via flags.
 * </p>
 *
 * <h2>Concept</h2>
 *
 * <p>
 * Zones are the core building blocks of RegenSystem. Each zone:
 * </p>
 * <ul>
 *   <li>Is defined by two {@link Location} corners</li>
 *   <li>Stores a snapshot of original blocks</li>
 *   <li>Can regenerate itself on demand or on a timer</li>
 *   <li>Supports fine-grained behavior control using {@link ZoneFlag}s</li>
 * </ul>
 *
 * <h2>Thread & Platform Safety</h2>
 *
 * <p>
 * All zone operations are internally managed to be safe on:
 * </p>
 * <ul>
 *   <li>Paper</li>
 *   <li>Spigot</li>
 *   <li>Folia (region-safe scheduling)</li>
 * </ul>
 *
 * <p>
 * API consumers should <b>not</b> attempt to access or modify blocks manually
 * inside zones; always use the provided methods.
 * </p>
 *
 * <h2>Lifecycle & Events</h2>
 *
 * <p>
 * Regeneration triggers lifecycle events:
 * </p>
 * <ul>
 *   <li>{@link fr.darklash.regensystem.api.event.ZonePreRegenEvent} (cancellable)</li>
 *   <li>{@link fr.darklash.regensystem.api.event.ZonePostRegenEvent}</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 *
 * <pre>{@code
 * Zone zone = RegenSystemAPI.getZones().getZone("mine").orElse(null);
 *
 * if (zone != null && zone.isEnabled()) {
 *     zone.regenerate();
 * }
 * }</pre>
 *
 * <h2>API Stability</h2>
 *
 * <ul>
 *   <li>This interface is part of the <b>stable public API</b></li>
 *   <li>Backward compatible within the same major version</li>
 *   <li>Implementations are internal and must not be cast</li>
 * </ul>
 *
 * @author DarkLash
 * @since API 1.0.0
 */
public interface Zone {

    /**
     * Returns the unique name of this zone.
     *
     * <p>
     * Zone names are case-sensitive and unique across the server.
     * </p>
     *
     * @return the zone name
     */
    String getName();

    /**
     * Returns the first corner of the zone.
     *
     * @return a {@link Location} representing one corner
     */
    Location getCorner1();

    /**
     * Returns the second corner of the zone.
     *
     * @return a {@link Location} representing the opposite corner
     */
    Location getCorner2();

    /**
     * Returns the center location of the zone.
     *
     * <p>
     * This value is computed and may not correspond to a block-aligned position.
     * </p>
     *
     * @return the center {@link Location}
     */
    Location getCenter();

    /**
     * Checks whether this zone is currently enabled.
     *
     * <p>
     * Disabled zones will not regenerate automatically or manually.
     * </p>
     *
     * @return {@code true} if the zone is enabled, {@code false} otherwise
     */
    boolean isEnabled();

    /**
     * Checks whether the given location is contained within this zone.
     *
     * <p>
     * The location must be in the same world as the zone.
     * </p>
     *
     * @param location the location to check
     * @return {@code true} if the location is inside the zone
     */
    boolean contains(Location location);

    /**
     * Regenerates all blocks in this zone to their original stored state.
     *
     * <p>
     * This method:
     * </p>
     * <ul>
     *   <li>Fires {@link fr.darklash.regensystem.api.event.ZonePreRegenEvent}</li>
     *   <li>Restores blocks safely and efficiently</li>
     *   <li>Fires {@link fr.darklash.regensystem.api.event.ZonePostRegenEvent}</li>
     * </ul>
     *
     * <p>
     * The regeneration may be skipped or stopped depending on the zone state
     * or event cancellation.
     * </p>
     *
     * @return the {@link RegenResult} of the regeneration process
     */
    RegenResult regenerate();

    /**
     * Checks whether a specific {@link ZoneFlag} is enabled for this zone.
     *
     * @param flag the flag to check
     * @return {@code true} if the flag is enabled
     */
    boolean hasFlag(ZoneFlag flag);

    /**
     * Sets the value of a {@link ZoneFlag} for this zone.
     *
     * <p>
     * This action triggers a {@link fr.darklash.regensystem.api.event.ZoneFlagChangeEvent}
     * which can be cancelled.
     * </p>
     *
     * @param flag  the flag to modify
     * @param value {@code true} to enable, {@code false} to disable
     */
    void setFlag(ZoneFlag flag, boolean value);

    /**
     * Returns all flags and their current values for this zone.
     *
     * <p>
     * The returned map is a snapshot and modifying it will not affect the zone.
     * </p>
     *
     * @return a map of {@link ZoneFlag} to enabled/disabled state
     */
    Map<ZoneFlag, Boolean> getFlags();

    /**
     * Visually highlights the borders of this zone using particles.
     *
     * <p>
     * This method is intended for debugging, admin tools, or GUIs.
     * </p>
     *
     * @param particle the particle type to display
     * @param duration how long the effect should last
     */
    void highlight(Particle particle, Duration duration);

    /**
     * Counts the number of players currently inside this zone.
     *
     * @return the number of players inside the zone
     */
    int countPlayersInside();

    /**
     * Returns the number of blocks stored in this zone snapshot.
     *
     * <p>
     * This represents the total amount of blocks that will be considered
     * during regeneration.
     * </p>
     *
     * @return the number of stored blocks
     */
    int getBlockCount();
}
