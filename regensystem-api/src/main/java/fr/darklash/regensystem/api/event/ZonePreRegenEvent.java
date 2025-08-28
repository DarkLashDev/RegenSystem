package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.RegenZone;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called immediately before a zone regenerates its blocks.
 * <p>
 * This event is cancellable. Cancelling it will prevent the regeneration
 * from occurring. This can be useful if you want to temporarily stop
 * certain zones from regenerating under specific conditions.
 */
public class ZonePreRegenEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone that is about to regenerate.
     */
    @Getter
    private final RegenZone zone;

    private boolean cancelled;

    /**
     * Creates a new ZonePreRegenEvent.
     *
     * @param zone the zone that is about to regenerate
     */
    public ZonePreRegenEvent(RegenZone zone) {
        this.zone = zone;
    }

    /**
     * Checks if the event is cancelled.
     *
     * @return true if the regeneration is cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether the event should be cancelled.
     * Cancelling the event will prevent the zone from regenerating.
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
