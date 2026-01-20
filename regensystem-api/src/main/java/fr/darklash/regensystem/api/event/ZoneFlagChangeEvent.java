package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.Zone;
import fr.darklash.regensystem.api.zone.ZoneFlag;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>ZoneFlagChangeEvent</h1>
 *
 * <p>
 * Called when a {@link ZoneFlag} is about to be changed on a {@link Zone}.
 * </p>
 *
 * <p>
 * This event is fired <b>before</b> the flag value is updated.
 * </p>
 *
 * <h2>Cancellability</h2>
 *
 * <p>
 * This event is <b>cancellable</b>. Cancelling it will prevent the flag
 * value from being modified.
 * </p>
 *
 * <p>
 * Typical use cases include:
 * </p>
 * <ul>
 *   <li>Restricting flag changes based on permissions or roles</li>
 *   <li>Enforcing global server rules (e.g. no PvP anywhere)</li>
 *   <li>Logging or auditing configuration changes</li>
 *   <li>Preventing dangerous flag combinations</li>
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
 * public void onFlagChange(ZoneFlagChangeEvent event) {
 *     if (event.getFlag() == ZoneFlag.PVP) {
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
public class ZoneFlagChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone whose flag is being changed.
     */
    @Getter
    private final Zone zone;

    /**
     * The flag being modified.
     */
    @Getter
    private final ZoneFlag flag;

    /**
     * The previous value of the flag.
     */
    @Getter
    private final boolean oldValue;

    /**
     * The new value the flag is attempting to be set to.
     */
    @Getter
    private final boolean newValue;

    /**
     * Whether the event has been cancelled.
     */
    private boolean cancelled;

    /**
     * Creates a new {@link ZoneFlagChangeEvent}.
     *
     * @param zone     the zone where the flag is changing
     * @param flag     the flag being modified
     * @param oldValue the previous flag value
     * @param newValue the new flag value
     * @throws IllegalArgumentException if {@code zone} or {@code flag} is {@code null}
     */
    public ZoneFlagChangeEvent(Zone zone, ZoneFlag flag, boolean oldValue, boolean newValue) {
        if (zone == null) {
            throw new IllegalArgumentException("zone cannot be null");
        }
        if (flag == null) {
            throw new IllegalArgumentException("flag cannot be null");
        }
        this.zone = zone;
        this.flag = flag;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Checks whether this event is cancelled.
     *
     * @return {@code true} if the flag change is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled.
     *
     * <p>
     * Cancelling this event will prevent the flag value
     * from being updated.
     * </p>
     *
     * @param cancel {@code true} to cancel the flag change
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
