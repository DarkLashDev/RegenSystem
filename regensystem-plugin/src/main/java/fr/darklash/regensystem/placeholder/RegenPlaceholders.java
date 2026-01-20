package fr.darklash.regensystem.placeholder;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.api.zone.Zone;
import fr.darklash.regensystem.internal.zone.ZoneLoc;
import fr.darklash.regensystem.util.Key;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class RegenPlaceholders extends PlaceholderExpansion {

    private final RegenSystem plugin;

    public RegenPlaceholders(RegenSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "regen"; // %regen_...%
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        FileConfiguration config = plugin.getFileManager().get(Key.File.ZONE);

        // %regen_zone_count%
        if (params.equalsIgnoreCase("zone_count")) {
            return String.valueOf(RegenSystemAPI.getZones().getZones().size());
        }

        // %regen_regen_enabled%
        if (params.equalsIgnoreCase("regen_enabled")) {
            boolean enabled = config.getBoolean("global.regen-enabled", false);
            return enabled ? "enabled" : "disabled";
        }

        // %regen_enabled_<zone>%
        if (params.startsWith("enabled_")) {
            String zoneName = params.substring("enabled_".length());
            boolean enabled = config.getBoolean("zones." + zoneName + ".enabled", false);
            return enabled ? "true" : "false";
        }

        // %regen_delay_<zone>%
        if (params.startsWith("delay_")) {
            String zoneName = params.substring("delay_".length());
            int delay = config.getInt("zones." + zoneName + ".regenDelay", -1);
            return delay == -1 ? "unknown" : String.valueOf(delay);
        }

        // %regen_exists_<zone>%
        if (params.startsWith("exists_")) {
            String zoneName = params.substring("exists_".length());
            return plugin.getZoneManager().getZoneNames().contains(zoneName) ? "true" : "false";
        }

        // %regen_block_count_<zone>%
        if (params.startsWith("block_count_")) {
            String zoneName = params.substring("block_count_".length());
            Zone zone = RegenSystemAPI
                    .getZones()
                    .getZone(zoneName)
                    .orElse(null);
            if (zone == null) return "unknown";
            return String.valueOf(zone.getBlockCount());
        }

        // %regen_corner1_<zone>%
        if (params.startsWith("corner1_")) {
            String zoneName = params.substring("corner1_".length());
            Zone zone = RegenSystemAPI
                    .getZones()
                    .getZone(zoneName)
                    .orElse(null);
            if (zone == null) return "unknown";
            return ZoneLoc.toString(zone.getCorner1());
        }

        // %regen_corner2_<zone>%
        if (params.startsWith("corner2_")) {
            String zoneName = params.substring("corner2_".length());
            Zone zone = RegenSystemAPI
                    .getZones()
                    .getZone(zoneName)
                    .orElse(null);
            if (zone == null) return "unknown";
            return ZoneLoc.toString(zone.getCorner2());
        }

        // %regen_timer_<zone>%
        if (params.startsWith("timer_")) {
            String zoneName = params.substring("timer_".length());
            int seconds = plugin.getZoneManager().getTimeLeft(zoneName);

            if (seconds == -1) return "unknown";

            int minutes = seconds / 60;
            int secs = seconds % 60;
            return String.format("%02d:%02d", minutes, secs);
        }

        // %regen_name_<zone>%
        if (params.startsWith("name_")) {
            String zoneName = params.substring("name_".length());
            Zone zone = RegenSystemAPI
                    .getZones()
                    .getZone(zoneName)
                    .orElse(null);
            if (zone == null) return "unknown";
            return zone.getName();
        }

        return null;
    }
}
