package fr.darklash.regensystem.api.zone;

import lombok.Getter;

/**
 * Represents a configurable flag that can be applied to a {@link RegenZone}.
 * <p>
 * Flags are used to enable or disable specific behaviors inside a zone
 * such as PVP, block breaking, explosions, mob spawning, etc.
 *
 * <p>
 * Each flag defines:
 * <ul>
 *   <li>a default value (used when a zone is first created)</li>
 *   <li>one or more aliases (used for commands and parsing)</li>
 *   <li>a human-readable description</li>
 * </ul>
 */
public enum RegenZoneFlag {

    /** Allows player versus player combat inside the zone */
    PVP(true, "pvp", "Allows player vs player combat"),

    /** Allows explosions to affect the zone */
    EXPLOSION(true, "explosion", "Explosions are allowed"),

    /** Allows blocks to be broken */
    BLOCK_BREAK(true, "break", "blockbreak", "bb", "Allows breaking blocks"),

    /** Allows blocks to be placed */
    BLOCK_PLACE(true, "place", "blockplace", "bp", "Allows placing blocks"),

    /** Allows players to drop items */
    ITEM_DROP(true, "drop", "Allows dropping items"),

    /** Allows players to pick up items */
    ITEM_PICKUP(true, "pickup", "Allows picking up items"),

    /** Allows mobs to spawn */
    MOB_SPAWN(true, "spawn", "mobspawn", "Allows mob spawning"),

    /** Allows mobs to deal damage to players */
    MOB_DAMAGE(true, "mobdamage", "damage", "Allows mobs to deal damage");

    /**
     * Default value for this flag.
     * <p>
     * This value is used when a zone is created and no explicit value
     * is provided by the user.
     */
    private final boolean defaultValue;

    /**
     * Alternative names for this flag.
     * <p>
     * Used for command parsing (e.g. "bb" for BLOCK_BREAK).
     */
    private final String[] aliases;

    /**
     * Human-readable description of the flag.
     * <p>
     * Intended to be displayed in commands, menus, or documentation.
     */
    @Getter
    private final String description;

    /**
     * Creates a new {@link RegenZoneFlag}.
     *
     * @param defaultValue the default enabled/disabled state
     * @param args aliases followed by the description (last argument)
     */
    RegenZoneFlag(boolean defaultValue, String... args) {
        this.defaultValue = defaultValue;

        // If arguments are provided, all except the last one are aliases
        // and the last argument is the description.
        if (args.length > 1) {
            this.aliases = new String[args.length - 1];
            System.arraycopy(args, 0, this.aliases, 0, args.length - 1);
            this.description = args[args.length - 1];
        } else {
            this.aliases = new String[0];
            this.description = "";
        }
    }

    /**
     * Returns the default value of this flag.
     *
     * @return {@code true} if enabled by default, {@code false} otherwise
     */
    public boolean getDefaultValue() {
        return defaultValue;
    }

    /**
     * Attempts to resolve a {@link RegenZoneFlag} from a string.
     * <p>
     * The input may match either:
     * <ul>
     *   <li>the enum name (case-insensitive)</li>
     *   <li>one of the defined aliases</li>
     * </ul>
     *
     * <p>
     * This method is primarily used by commands and API consumers
     * to safely parse user input.
     *
     * @param s the input string
     * @return the matching {@link RegenZoneFlag}, or {@code null} if none matches
     */
    public static RegenZoneFlag fromString(String s) {
        if (s == null) return null;

        for (RegenZoneFlag flag : values()) {

            // Match enum name (e.g. "PVP")
            if (flag.name().equalsIgnoreCase(s)) {
                return flag;
            }

            // Match aliases (e.g. "bb", "blockbreak")
            for (String alias : flag.aliases) {
                if (alias.equalsIgnoreCase(s)) {
                    return flag;
                }
            }
        }
        return null;
    }
}
