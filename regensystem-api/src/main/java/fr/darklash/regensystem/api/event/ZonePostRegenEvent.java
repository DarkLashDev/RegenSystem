package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.Zone;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>ZonePostRegenEvent</h1>
 *
 * <p>
 * Called immediately after a {@link Zone} has successfully regenerated
 * its blocks.
 * </p>
 *
 * <p>
 * This event is fired <b>after</b> all blocks have been restored to their
 * original state and after all internal regeneration logic has completed.
 * </p>
 *
 * <h2>Cancellability</h2>
 *
 * <p>
 * This event is <b>not cancellable</b>, as the regeneration has already
 * occurred when the event is fired.
 * </p>
 *
 * <h2>Typical Use Cases</h2>
 *
 * <ul>
 *   <li>Updating GUIs or scoreboards</li>
 *   <li>Notifying players that a zone has regenerated</li>
 *   <li>Triggering visual or sound effects</li>
 *   <li>Running custom post-regeneration logic</li>
 *   <li>Logging or analytics</li>
 * </ul>
 *
 * <h2>Execution Context</h2>
 *
 * <ul>
 *   <li>Fired synchronously on the Bukkit main thread</li>
 *   <li>Safe to interact with the Bukkit API</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * @EventHandler
 * public void onZonePostRegen(ZonePostRegenEvent event) {
 *     Zone zone = event.getZone();
 *
 *     Bukkit.broadcastMessage(
 *         "Zone '" + zone.getName() + "' has regenerated!"
 *     );
 * }
 * }</pre>
 *
 * <h2>API Stability</h2>
 *
 * <ul>
 *   <li>Part of the <b>stable public API</b></li>
 *   <li>Backward compatible within the same major API version</li>
 * </ul>
 *
 * @since API 1.0.0
 */
@ApiStatus.NonExtendable
@Getter
public class ZonePostRegenEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone that has just been regenerated.
     *
     * <p>
     * At this stage, the zone is fully restored and reflects
     * its post-regeneration state.
     * </p>
     */
    private final Zone zone;

    /**
     * Creates a new {@link ZonePostRegenEvent}.
     *
     * @param zone the zone that has just regenerated
     * @throws IllegalArgumentException if {@code zone} is {@code null}
     */
    public ZonePostRegenEvent(Zone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("zone cannot be null");
        }
        this.zone = zone;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return the handler list
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the static handler list for this event.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
