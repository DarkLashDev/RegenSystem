package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.api.zone.RegenZoneFlag;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a flag in a {@link RegenZone} is about to be changed.
 * <p>
 * Cancelling this event will prevent the flag from being updated.
 */
public class ZoneFlagChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone whose flag is being changed.
     */
    @Getter
    private final RegenZone zone;

    /**
     * The flag being modified.
     */
    @Getter
    private final RegenZoneFlag flag;

    /**
     * The old value of the flag before the change.
     */
    @Getter
    private final boolean oldValue;

    /**
     * The new value the flag is attempting to be set to.
     */
    @Getter
    private final boolean newValue;

    private boolean cancelled;

    /**
     * Creates a new ZoneFlagChangeEvent.
     *
     * @param zone     the zone where the flag is changing
     * @param flag     the flag being modified
     * @param oldValue the previous value of the flag
     * @param newValue the new value the flag is being set to
     */
    public ZoneFlagChangeEvent(RegenZone zone, RegenZoneFlag flag, boolean oldValue, boolean newValue) {
        this.zone = zone;
        this.flag = flag;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Checks if the event is cancelled.
     * Cancelling will prevent the flag from being updated.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether the event should be cancelled.
     * Cancelling will prevent the flag from being updated.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
