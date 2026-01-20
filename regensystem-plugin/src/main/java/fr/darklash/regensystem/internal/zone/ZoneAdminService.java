package fr.darklash.regensystem.internal.zone;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.util.Key;
import org.bukkit.configuration.file.FileConfiguration;

public class ZoneAdminService {

    private final RegenSystem plugin;
    private final ZoneManagerImpl zoneManager;

    public ZoneAdminService(RegenSystem plugin) {
        this.plugin = plugin;
        this.zoneManager = plugin.getZoneManager();
    }

    private FileConfiguration getZoneConfig() {
        return plugin.getFileManager().get(Key.File.ZONE);
    }

    private void saveZoneConfig() {
        plugin.getFileManager().save(Key.File.ZONE);
    }

    private String zonePath(String name) {
        return "zones." + name;
    }

    public void deleteZone(String name) {
        FileConfiguration config = getZoneConfig();
        config.set(zonePath(name), null);
        saveZoneConfig();

        zoneManager.deleteZone(name);

        plugin.getLogger().info("ZoneImpl '" + name + "' deleted");
    }

    private void setZoneEnabled(String name, boolean enabled) {
        FileConfiguration config = getZoneConfig();
        config.set(zonePath(name) + ".enabled", enabled);
        saveZoneConfig();

        zoneManager.loadZones();

        plugin.getLogger().info("ZoneImpl '" + name + "' " + (enabled ? "enabled" : "disabled"));
    }

    public void enableZone(String name) {
        setZoneEnabled(name, true);
    }

    public void disableZone(String name) {
        setZoneEnabled(name, false);
    }

    public void enableAllZones() {
        setGlobalEnabled(true);
    }

    public void disableAllZones() {
        setGlobalEnabled(false);
    }

    private void setGlobalEnabled(boolean enabled) {
        FileConfiguration config = getZoneConfig();
        config.set("global.regen-enabled", enabled);
        saveZoneConfig();

        zoneManager.loadZones();

        plugin.getLogger().info("All zones " + (enabled ? "enabled" : "disabled"));
    }

    public void snapshotZone(String zoneName) {
        ZoneImpl zone = zoneManager.getZoneInternal(zoneName).orElse(null);

        if (zone == null) {
            throw new IllegalArgumentException("ZoneImpl not found: " + zoneName);
        }

        zone.captureState();
        zone.save();

        plugin.getLogger().info(
                "Snapshot saved for zone '" + zone.getName() + "'"
        );
    }
}
