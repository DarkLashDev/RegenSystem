package fr.darklash.regensystem.util;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.api.zone.RegenZoneManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ZoneService {

    private final RegenSystem plugin;
    private final RegenZoneManager api;

    public ZoneService(RegenSystem plugin) {
        this.plugin = plugin;
        this.api = RegenSystemAPI.get();
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

    public void createZone(String name, Location pos1, Location pos2, int delay) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Zone name cannot be null or blank");
        if (pos1 == null || pos2 == null)
            throw new IllegalArgumentException("Zone positions cannot be null");

        FileConfiguration config = getZoneConfig();
        if (config.contains(zonePath(name))) {
            throw new IllegalStateException("Zone '" + name + "' already exists");
        }

        config.set(zonePath(name) + ".pos1", ZoneLoc.toString(pos1));
        config.set(zonePath(name) + ".pos2", ZoneLoc.toString(pos2));
        config.set(zonePath(name) + ".regenDelay", delay);
        config.set(zonePath(name) + ".enabled", true);

        saveZoneConfig();

        RegenZone zone = new Zone(name, pos1, pos2);
        zone.save();
        zone.saveFlags();
        api.addZone(zone);

        plugin.getLogger().info("Zone '" + name + "' created with delay " + delay);
    }

    public void deleteZone(String name) {
        FileConfiguration config = getZoneConfig();
        config.set(zonePath(name), null);
        saveZoneConfig();
        api.deleteZone(name);
        plugin.getLogger().info("Zone '" + name + "' deleted");
    }

    public void enableZone(String name) {
        setZoneEnabled(name, true);
    }

    public void disableZone(String name) {
        setZoneEnabled(name, false);
    }

    private void setZoneEnabled(String name, boolean enabled) {
        FileConfiguration config = getZoneConfig();
        config.set(zonePath(name) + ".enabled", enabled);
        saveZoneConfig();
        api.loadZones();
        plugin.getLogger().info("Zone '" + name + "' " + (enabled ? "enabled" : "disabled"));
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
        api.loadZones();
        plugin.getLogger().info("All zones " + (enabled ? "enabled" : "disabled"));
    }

    public void snapshotZone(RegenZone zone) {
        zone.captureState();
        zone.save();
        plugin.getLogger().info("Snapshot saved for zone '" + zone.getName() + "'");
    }
}
