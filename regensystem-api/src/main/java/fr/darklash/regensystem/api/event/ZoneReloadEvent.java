package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.RegenZone;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a zone is reloaded from the configuration file.
 * <p>
 * This event is cancellable. Cancelling it will prevent the zone from being reloaded.
 * <p>
 * You can use this to intercept reloads, for example to prevent
 * certain zones from being updated at runtime.
 */
public class ZoneReloadEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone being reloaded.
     */
    @Getter
    private final RegenZone zone;

    private boolean cancelled;

    /**
     * Creates a new ZoneReloadEvent.
     *
     * @param zone the zone that is being reloaded
     */
    public ZoneReloadEvent(RegenZone zone) {
        this.zone = zone;
    }

    /**
     * Checks if the event is cancelled.
     *
     * @return true if the reload is cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether the event should be cancelled.
     * Cancelling the event will prevent the zone reload.
     *
     * @param cancel true to cancel, false to allow
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the handlers list for Bukkit events.
     *
     * @return the handler list
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the static handler list for Bukkit events.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
