package fr.darklash.regensystem.api.zone;

import org.bukkit.entity.Player;

/**
 * Represents an action that can be executed when a player
 * interacts with a zone, enters it, or when a specific zone event occurs.
 * <p>
 * This is a functional interface, so it can be implemented
 * using a lambda expression or method reference.
 * <p>
 * Example usage:
 * <pre>
 * zone.addAction((player, zone) -> player.sendMessage("You entered " + zone.getName()));
 * </pre>
 */
@FunctionalInterface
public interface RegenZoneAction {

    /**
     * Executes this action for the given player and zone.
     *
     * @param player the player on whom the action is performed
     * @param zone   the zone where the action occurs
     */
    void execute(Player player, RegenZone zone);
}
