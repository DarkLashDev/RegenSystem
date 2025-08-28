package fr.darklash.regensystem.api.zone;

import lombok.Getter;

/**
 * Flags represent configurable options for {@link RegenZone}.
 * <p>
 * Each flag can be enabled or disabled per zone.
 * Example: Preventing block breaking, mob spawning, or PVP.
 */
public enum RegenZoneFlag {
    PVP("pvp", "Allows player vs player combat"),
    EXPLOSION("explosion", "Explosions are allowed"),
    BLOCK_BREAK("break", "blockbreak", "bb", "Allows breaking blocks"),
    BLOCK_PLACE("place", "blockplace", "bp", "Allows placing blocks"),
    ITEM_DROP("drop", "Allows dropping items"),
    ITEM_PICKUP("pickup", "Allows picking up items"),
    MOB_SPAWN("spawn", "mobspawn", "Allows mob spawning"),
    MOB_DAMAGE("mobdamage", "damage", "Allows mobs to deal damage");

    private final String[] aliases;
    @Getter
    private final String description;

    RegenZoneFlag(String... args) {
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
     * Gets a flag from its name or alias.
     *
     * @param s the input string
     * @return the matching flag, or null if none found
     */
    public static RegenZoneFlag fromString(String s) {
        for (RegenZoneFlag flag : values()) {
            if (flag.name().equalsIgnoreCase(s)) return flag;
            for (String alias : flag.aliases) {
                if (alias.equalsIgnoreCase(s)) return flag;
            }
        }
        return null;
    }
}
