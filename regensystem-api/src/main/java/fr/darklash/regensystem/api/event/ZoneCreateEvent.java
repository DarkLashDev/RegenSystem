package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.Zone;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>ZoneCreateEvent</h1>
 *
 * <p>
 * Called when a new {@link Zone} is about to be created and registered
 * in the RegenSystem zone registry.
 * </p>
 *
 * <p>
 * This event is fired <b>before</b> the zone becomes active and visible
 * through the public API.
 * </p>
 *
 * <h2>Cancellability</h2>
 *
 * <p>
 * This event is <b>cancellable</b>. Cancelling it will prevent the zone
 * from being registered and fully created.
 * </p>
 *
 * <p>
 * Typical use cases include:
 * </p>
 * <ul>
 *   <li>Preventing overlapping zones</li>
 *   <li>Restricting zone creation based on permissions or rules</li>
 *   <li>Validating zone size, world, or configuration</li>
 *   <li>Logging or auditing zone creation attempts</li>
 * </ul>
 *
 * <h2>Execution Context</h2>
 *
 * <ul>
 *   <li>Fired synchronously on the Bukkit main thread</li>
 *   <li>Safe to access Bukkit API</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * @EventHandler
 * public void onZoneCreate(ZoneCreateEvent event) {
 *     Zone zone = event.getZone();
 *
 *     if (zone.getBlockCount() > 50_000) {
 *         event.setCancelled(true);
 *     }
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
public class ZoneCreateEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone that is being created.
     *
     * <p>
     * This zone is not yet registered in the {@link fr.darklash.regensystem.api.zone.ZoneManager}
     * and may be safely inspected or rejected.
     * </p>
     */
    @Getter
    private final Zone zone;

    /**
     * Whether the event has been cancelled.
     */
    private boolean cancelled;

    /**
     * Creates a new {@link ZoneCreateEvent}.
     *
     * @param zone the zone that is about to be created
     * @throws IllegalArgumentException if {@code zone} is {@code null}
     */
    public ZoneCreateEvent(Zone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("zone cannot be null");
        }
        this.zone = zone;
    }

    /**
     * Checks whether this event is cancelled.
     *
     * @return {@code true} if the zone creation is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled.
     *
     * <p>
     * Cancelling this event will prevent the zone from being registered
     * and activated.
     * </p>
     *
     * @param cancel {@code true} to cancel the zone creation
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
