package fr.darklash.regensystem.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegenZoneEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final String zoneId;
    private final Player player; // peut être null si event global
    private boolean cancelled;

    /**
     * Constructeur avec joueur (non null)
     */
    public RegenZoneEvent(@NotNull String zoneId, @NotNull Player player) {
        this.zoneId = zoneId;
        this.player = player;
        this.cancelled = false;
    }

    /**
     * Constructeur sans joueur (player = null)
     */
    public RegenZoneEvent(@NotNull String zoneId) {
        this.zoneId = zoneId;
        this.player = null;
        this.cancelled = false;
    }

    public String getZoneId() {
        return zoneId;
    }

    /**
     * Peut retourner null si événement global
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
