package fr.darklash.regensystem.api.listener;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class RegenZoneEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String zoneId;

    public RegenZoneEvent(String zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
