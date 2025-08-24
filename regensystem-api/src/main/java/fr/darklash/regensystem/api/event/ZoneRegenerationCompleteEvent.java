package fr.darklash.regensystem.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ZoneRegenerationCompleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String zoneName;

    public ZoneRegenerationCompleteEvent(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneName() {
        return zoneName;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
