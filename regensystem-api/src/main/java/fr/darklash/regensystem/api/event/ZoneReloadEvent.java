package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.Zone;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>ZoneReloadEvent</h1>
 *
 * <p>
 * Called when a {@link Zone} is about to be reloaded from the configuration.
 * </p>
 *
 * <p>
 * This event is fired before the existing zone instance is replaced by
 * a newly loaded one. At this point, the zone still represents the
 * <b>current active instance</b>.
 * </p>
 *
 * <h2>Cancellability</h2>
 *
 * <p>
 * This event is <b>cancellable</b>.
 * Cancelling it will prevent the zone from being reloaded.
 * </p>
 *
 * <h2>Typical Use Cases</h2>
 *
 * <ul>
 *   <li>Prevent live reloads of critical zones</li>
 *   <li>Validate configuration before applying changes</li>
 *   <li>Pause reloads during events or gameplay phases</li>
 *   <li>Log or audit zone reload operations</li>
 * </ul>
 *
 * <h2>Execution Context</h2>
 *
 * <ul>
 *   <li>Fired synchronously on the Bukkit main thread</li>
 *   <li>Safe to interact with the Bukkit API</li>
 * </ul>
 *
 * <h2>Important Notes</h2>
 *
 * <ul>
 *   <li>If cancelled, the zone will remain unchanged</li>
 *   <li>No regeneration or task reset will occur if cancelled</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * @EventHandler
 * public void onZoneReload(ZoneReloadEvent event) {
 *     Zone zone = event.getZone();
 *
 *     if ("spawn".equalsIgnoreCase(zone.getName())) {
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
public class ZoneReloadEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone being reloaded.
     *
     * <p>
     * This represents the currently active zone instance before reload.
     * </p>
     */
    @Getter
    private final Zone zone;

    private boolean cancelled;

    /**
     * Creates a new {@link ZoneReloadEvent}.
     *
     * @param zone the zone that is about to be reloaded
     * @throws IllegalArgumentException if {@code zone} is {@code null}
     */
    public ZoneReloadEvent(Zone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("zone cannot be null");
        }
        this.zone = zone;
    }

    /**
     * Checks whether this event has been cancelled.
     *
     * @return {@code true} if the reload is cancelled, {@code false} otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled.
     *
     * <p>
     * Cancelling this event will prevent the zone from being reloaded.
     * </p>
     *
     * @param cancel {@code true} to cancel the reload,
     *               {@code false} to allow it
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
