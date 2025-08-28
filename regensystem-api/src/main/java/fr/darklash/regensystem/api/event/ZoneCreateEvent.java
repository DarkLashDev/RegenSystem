package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.RegenZone;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a new zone is being created.
 * <p>
 * This event is <b>cancellable</b>. Cancelling it will prevent the zone from being registered.
 * Listeners can use this event to validate the zone creation,
 * prevent overlapping zones, or log the creation.
 */
public class ZoneCreateEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone that is being created.
     */
    @Getter
    private final RegenZone zone;

    /**
     * Whether the event is cancelled.
     */
    private boolean cancelled;

    /**
     * Creates a new ZoneCreateEvent.
     *
     * @param zone the zone that is being created
     */
    public ZoneCreateEvent(RegenZone zone) {
        this.zone = zone;
    }

    /**
     * Checks whether this event is cancelled.
     *
     * @return true if the creation is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled.
     *
     * @param cancel true to cancel the creation
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
