package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.Zone;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>ZonePreRegenEvent</h1>
 *
 * <p>
 * Called immediately <b>before</b> a {@link Zone} starts regenerating its blocks.
 * </p>
 *
 * <p>
 * This event is fired right before any block changes occur, making it the
 * ideal place to perform checks or prevent a regeneration under specific
 * conditions.
 * </p>
 *
 * <h2>Cancellability</h2>
 *
 * <p>
 * This event is <b>cancellable</b>.
 * Cancelling it will completely prevent the regeneration from happening.
 * </p>
 *
 * <h2>Typical Use Cases</h2>
 *
 * <ul>
 *   <li>Prevent regeneration if players are inside the zone</li>
 *   <li>Pause regeneration during events or maintenance</li>
 *   <li>Apply cooldowns or custom conditions</li>
 *   <li>Integrate economy or permission checks</li>
 *   <li>Log or audit regeneration attempts</li>
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
 * public void onZonePreRegen(ZonePreRegenEvent event) {
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
public class ZonePreRegenEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone that is about to regenerate.
     *
     * <p>
     * At this stage, no blocks have been modified yet.
     * </p>
     */
    @Getter
    private final Zone zone;

    private boolean cancelled;

    /**
     * Creates a new {@link ZonePreRegenEvent}.
     *
     * @param zone the zone that is about to regenerate
     * @throws IllegalArgumentException if {@code zone} is {@code null}
     */
    public ZonePreRegenEvent(Zone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("zone cannot be null");
        }
        this.zone = zone;
    }

    /**
     * Checks whether this event has been cancelled.
     *
     * @return {@code true} if regeneration is cancelled, {@code false} otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled.
     *
     * <p>
     * Cancelling this event will prevent the zone from regenerating.
     * </p>
     *
     * @param cancel {@code true} to cancel the regeneration,
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
