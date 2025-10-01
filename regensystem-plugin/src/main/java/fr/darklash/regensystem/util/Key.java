package fr.darklash.regensystem.util;

public class Key {

    public static class File {
        private File() {};

        public static final String CONFIG = "config.yml";
        public static final String ZONE = "zone.yml";
    }

    public enum Lang {
        EN, FR
    }

    public enum Message {

        ONLY_PLAYERS,
        UNKNOWN_SUBCOMMAND,
        REGEN_CMD_USAGE,
        USAGE,
        POSITION1_SET,
        POSITION2_SET,
        ZONE_SAVED,
        ZONE_DELETED,
        ZONE_NOT_FOUND,
        NO_PERMISSION,
        RELOADED,
        POSITIONS_NOT_SET,
        DELAY_MUST_BE_POSITIVE,
        UNKNOWN_FLAG,
        INVALID_FLAG_VALUE,
        FLAG_SET,
        ZONE_SAVED_DELAY,
        SNAPSHOT_CREATED,
        WAND_RECEIVED,
        ZONE_RELOADED,
        ALL_ZONES_RELOADED,
        HELP_PAGE_1,
        HELP_PAGE_2,
        HELP_PAGE_3,
        FLAG_LIST_HEADER,
        FLAGS_FOR_ZONE,
        ZONE_ENABLED,
        ALL_ZONES_ENABLED,
        ZONE_DISABLED,
        ALL_ZONES_DISABLED,
        LANGUAGE_SET,
        UNKNOWN_LANGUAGE,
        UNKNOWN_ACTION
    }
}
