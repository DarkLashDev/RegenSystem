package fr.darklash.regensystem.api.zone;

import org.bukkit.entity.Player;

/**
 * Represents a condition that must be met for a player
 * to interact with a zone or trigger zone-specific actions.
 * <p>
 * This is a functional interface, so it can be implemented
 * using a lambda expression or method reference.
 * <p>
 * Example usage:
 * <pre>
 * zone.addCondition(player -> player.hasPermission("zone.bypass"));
 * </pre>
 */
@FunctionalInterface
public interface RegenZoneCondition {

    /**
     * Tests whether the condition is satisfied for the given player.
     *
     * @param player the player to test
     * @return true if the condition is met, false otherwise
     */
    boolean test(Player player);
}
