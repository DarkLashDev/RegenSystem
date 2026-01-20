package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.Zone;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>ZoneDeleteEvent</h1>
 *
 * <p>
 * Called when a {@link Zone} is about to be deleted from the RegenSystem.
 * </p>
 *
 * <p>
 * This event is fired <b>before</b> the zone is removed from the internal
 * registry and before any associated data (timers, regeneration tasks,
 * persistence) is cleaned up.
 * </p>
 *
 * <h2>Cancellability</h2>
 *
 * <p>
 * This event is <b>cancellable</b>. Cancelling it will prevent the zone
 * from being deleted.
 * </p>
 *
 * <p>
 * Typical use cases include:
 * </p>
 * <ul>
 *   <li>Preventing deletion of protected or critical zones</li>
 *   <li>Warning or relocating players before deletion</li>
 *   <li>Auditing or logging zone removals</li>
 *   <li>Blocking deletion while a zone is in use</li>
 * </ul>
 *
 * <h2>Execution Context</h2>
 *
 * <ul>
 *   <li>Fired synchronously on the Bukkit main thread</li>
 *   <li>Safe to interact with Bukkit API</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * @EventHandler
 * public void onZoneDelete(ZoneDeleteEvent event) {
 *     Zone zone = event.getZone();
 *
 *     if (zone.countPlayersInside() > 0) {
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
public class ZoneDeleteEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone that is about to be deleted.
     *
     * <p>
     * The zone is still fully accessible at this stage and may be inspected
     * or used to decide whether deletion should proceed.
     * </p>
     */
    @Getter
    private final Zone zone;

    /**
     * Whether the event has been cancelled.
     */
    private boolean cancelled;

    /**
     * Creates a new {@link ZoneDeleteEvent}.
     *
     * @param zone the zone that is about to be deleted
     * @throws IllegalArgumentException if {@code zone} is {@code null}
     */
    public ZoneDeleteEvent(Zone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("zone cannot be null");
        }
        this.zone = zone;
    }

    /**
     * Checks whether this event is cancelled.
     *
     * @return {@code true} if the zone deletion is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled.
     *
     * <p>
     * Cancelling this event will prevent the zone from being removed
     * from the registry.
     * </p>
     *
     * @param cancel {@code true} to cancel the deletion
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
