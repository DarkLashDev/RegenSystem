package fr.darklash.regensystem.api.event;

import fr.darklash.regensystem.api.zone.RegenZone;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called immediately after a zone has regenerated its blocks.
 * <p>
 * This event is <b>not cancellable</b> since the regeneration has already occurred.
 * Listeners can use this event to perform actions after a zone has been restored,
 * such as updating GUIs, notifying players, or triggering additional effects.
 */
@Getter
public class ZonePostRegenEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The zone that has just been regenerated.
     */
    private final RegenZone zone;

    /**
     * Creates a new ZonePostRegenEvent.
     *
     * @param zone the zone that has just regenerated
     */
    public ZonePostRegenEvent(RegenZone zone) {
        this.zone = zone;
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
