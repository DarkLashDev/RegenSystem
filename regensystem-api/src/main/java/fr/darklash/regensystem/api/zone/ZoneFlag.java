package fr.darklash.regensystem.api.zone;

import lombok.Getter;

/**
 * <h1>ZoneFlag</h1>
 *
 * <p>
 * Represents a configurable <b>behavior flag</b> that can be applied to a {@link Zone}.
 * Flags define what actions are allowed or denied inside a zone.
 * </p>
 *
 * <h2>Purpose</h2>
 *
 * <p>
 * Zone flags are used to control gameplay rules inside regenerable zones, such as:
 * </p>
 *
 * <ul>
 *   <li>Player versus player combat</li>
 *   <li>Block breaking and placing</li>
 *   <li>Explosions</li>
 *   <li>Mob spawning and damage</li>
 *   <li>Item pickup and drop</li>
 * </ul>
 *
 * <h2>Design</h2>
 *
 * <p>
 * Each flag defines:
 * </p>
 * <ul>
 *   <li>a <b>default value</b> (applied when a zone is first created)</li>
 *   <li>one or more <b>aliases</b> used for command parsing</li>
 *   <li>a <b>human-readable description</b> for menus and documentation</li>
 * </ul>
 *
 * <h2>Usage</h2>
 *
 * <pre>{@code
 * Zone zone = RegenSystemAPI.getZones().getZone("mine").orElse(null);
 *
 * if (zone != null && zone.hasFlag(ZoneFlag.PVP)) {
 *     // PvP is allowed in this zone
 * }
 *
 * zone.setFlag(ZoneFlag.PVP, false);
 * }</pre>
 *
 * <h2>Parsing</h2>
 *
 * <p>
 * Flags can be resolved from user input using {@link #fromString(String)}.
 * This supports both enum names and aliases.
 * </p>
 *
 * <pre>{@code
 * ZoneFlag flag = ZoneFlag.fromString("bb"); // BLOCK_BREAK
 * }</pre>
 *
 * <h2>API Stability</h2>
 *
 * <ul>
 *   <li>This enum is part of the <b>stable public API</b></li>
 *   <li>New flags may be added in minor versions</li>
 *   <li>Existing flags will not be removed or renamed in the same major version</li>
 * </ul>
 *
 * @author DarkLash
 * @since API 1.0.0
 */
public enum ZoneFlag {

    /** Allows player versus player combat inside the zone */
    PVP(true, "pvp", "Allows player vs player combat"),

    /** Allows explosions to affect the zone */
    EXPLOSION(true, "explosion", "Explosions are allowed"),

    /** Allows blocks to be broken inside the zone */
    BLOCK_BREAK(true, "break", "blockbreak", "bb", "Allows breaking blocks"),

    /** Allows blocks to be placed inside the zone */
    BLOCK_PLACE(true, "place", "blockplace", "bp", "Allows placing blocks"),

    /** Allows players to drop items inside the zone */
    ITEM_DROP(true, "drop", "Allows dropping items"),

    /** Allows players to pick up items inside the zone */
    ITEM_PICKUP(true, "pickup", "Allows picking up items"),

    /** Allows mobs to spawn inside the zone */
    MOB_SPAWN(true, "spawn", "mobspawn", "Allows mob spawning"),

    /** Allows mobs to deal damage to players inside the zone */
    MOB_DAMAGE(true, "mobdamage", "damage", "Allows mobs to deal damage");

    /**
     * The default value of this flag.
     *
     * <p>
     * This value is applied automatically when a zone is created
     * and no explicit value is defined.
     * </p>
     */
    private final boolean defaultValue;

    /**
     * Alternative names for this flag.
     *
     * <p>
     * Aliases are used for command input and parsing convenience.
     * </p>
     */
    private final String[] aliases;

    /**
     * Human-readable description of the flag.
     *
     * <p>
     * Intended for use in GUIs, commands, and documentation.
     * </p>
     */
    @Getter
    private final String description;

    /**
     * Creates a new {@link ZoneFlag}.
     *
     * @param defaultValue the default enabled/disabled state
     * @param args aliases followed by the description (last argument)
     */
    ZoneFlag(boolean defaultValue, String... args) {
        this.defaultValue = defaultValue;

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
     * Attempts to resolve a {@link ZoneFlag} from a string.
     *
     * <p>
     * The input may match:
     * </p>
     * <ul>
     *   <li>the enum name (case-insensitive)</li>
     *   <li>any of the defined aliases</li>
     * </ul>
     *
     * <p>
     * This method is safe to use for user input parsing.
     * </p>
     *
     * @param s the input string
     * @return the matching {@link ZoneFlag}, or {@code null} if none matches
     */
    public static ZoneFlag fromString(String s) {
        if (s == null) return null;

        for (ZoneFlag flag : values()) {

            if (flag.name().equalsIgnoreCase(s)) {
                return flag;
            }

            for (String alias : flag.aliases) {
                if (alias.equalsIgnoreCase(s)) {
                    return flag;
                }
            }
        }
        return null;
    }
}
