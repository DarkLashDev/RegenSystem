package fr.darklash.regensystem.manager;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.event.ZoneCreateEvent;
import fr.darklash.regensystem.api.event.ZoneDeleteEvent;
import fr.darklash.regensystem.api.event.ZoneReloadEvent;
import fr.darklash.regensystem.api.zone.RegenZoneFlag;
import fr.darklash.regensystem.api.zone.RegenZoneManager;
import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Zone;
import fr.darklash.regensystem.util.ZoneLoc;
import fr.darklash.regensystem.util.scheduler.RegenScheduler;
import fr.darklash.regensystem.util.scheduler.RegenTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ZoneManager implements RegenZoneManager {

    private final Map<String, RegenZone> zones = new HashMap<>();
    private final Map<String, RegenTask> zoneTasks = new HashMap<>();
    private final Map<String, Integer> zoneTimers = new HashMap<>();

    private final RegenScheduler scheduler = RegenSystem.getInstance().getScheduler();

    @Override
    public void loadZones() {
        zoneTasks.values().forEach(RegenTask::cancel);
        zoneTasks.clear();
        zoneTimers.clear();
        zones.clear();

        FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);
        ConfigurationSection section = config.getConfigurationSection("zones");
        if (section == null) return;

        for (String zoneName : section.getKeys(false)) {
            ConfigurationSection zoneSec = section.getConfigurationSection(zoneName);
            if (zoneSec == null) continue;

            boolean globalEnabled = config.getBoolean("global.regen-enabled", true);
            boolean zoneEnabled = zoneSec.getBoolean("enabled", true);

            String rawPos1 = zoneSec.getString("pos1");
            String rawPos2 = zoneSec.getString("pos2");

            if (rawPos1 == null || rawPos2 == null) {
                RegenSystem.getInstance().getLogger().warning("Zone '" + zoneName + "' is missing pos1 or pos2!");
                continue;
            }

            RegenZone zone = new Zone(
                    zoneName,
                    ZoneLoc.fromString(rawPos1),
                    ZoneLoc.fromString(rawPos2)
            );
            zone.load();
            zone.loadFlags();

            zones.put(zoneName, zone);

            if (globalEnabled && zoneEnabled) {
                int delay = zoneSec.getInt("regenDelay", 60);
                startZoneTimer(zone, delay);
            }
        }
    }

    private void startZoneTimer(RegenZone zone, int delaySeconds) {
        String name = zone.getName();
        zoneTimers.put(name, delaySeconds);

        RegenTask task = scheduler.runRepeating(
                zone.getCenter(),
                20L,
                20L,
                () -> tickZone(zone, delaySeconds)
        );

        zoneTasks.put(name, task);
    }

    private void tickZone(RegenZone zone, int delaySeconds) {
        String name = zone.getName();
        int remaining = zoneTimers.getOrDefault(name, delaySeconds);

        if (remaining <= 0) {
            zone.regenerate();

            zoneTimers.put(name, delaySeconds);
            return;
        }

        zoneTimers.put(name, remaining - 1);
    }

    @Override
    public void reloadZone(String name) {
        RegenZone oldZone = zones.get(name);

        ZoneReloadEvent event = new ZoneReloadEvent(oldZone);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        // Supprimer l’ancienne zone + tâche
        RegenTask oldTask = zoneTasks.remove(name);
        if (oldTask != null) oldTask.cancel();
        zoneTimers.remove(name);
        zones.remove(name);

        // Recharger depuis la config
        FileConfiguration config =
                RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);
        ConfigurationSection section =
                config.getConfigurationSection("zones." + name);
        if (section == null) return;

        String rawPos1 = section.getString("pos1");
        String rawPos2 = section.getString("pos2");
        if (rawPos1 == null || rawPos2 == null) return;

        RegenZone newZone = new Zone(
                name,
                ZoneLoc.fromString(rawPos1),
                ZoneLoc.fromString(rawPos2)
        );
        newZone.load();
        newZone.loadFlags();

        zones.put(name, newZone);

        if (!config.getBoolean("global.regen-enabled", true)) return;
        if (!section.getBoolean("enabled", true)) return;

        int delay = section.getInt("regenDelay", 60);
        startZoneTimer(newZone, delay);
    }

    @Override
    public void deleteZone(String name) {
        RegenZone zone = zones.get(name);
        if (zone == null) return;

        ZoneDeleteEvent event = new ZoneDeleteEvent(zone);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        zones.remove(name);

        RegenTask task = zoneTasks.remove(name);
        if (task != null) task.cancel();

        zoneTimers.remove(name);
    }

    @Override
    public void addZone(RegenZone zone) {
        ZoneCreateEvent event = new ZoneCreateEvent(zone);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        zones.put(zone.getName(), zone);

        FileConfiguration config =
                RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);

        if (!config.getBoolean("global.regen-enabled", true)) return;
        if (!config.getBoolean("zones." + zone.getName() + ".enabled", true)) return;

        int delay = config.getInt(
                "zones." + zone.getName() + ".regenDelay", 60
        );

        startZoneTimer(zone, delay);
    }

    @Override
    public Collection<RegenZone> getZones() {
        return new ArrayList<>(zones.values());
    }

    @Override
    public RegenZone getZone(String name) {
        return zones.get(name);
    }

    @Override
    public Set<String> getZoneNames() {
        return zones.keySet();
    }

    @Override
    public boolean isZoneRegistered(String name) {
        return zones.containsKey(name);
    }

    @Override
    public int getTimeLeft(String zoneName) {
        return zoneTimers.getOrDefault(zoneName, -1);
    }

    @Override
    public Collection<RegenZone> getZonesContaining(Location loc) {
        List<RegenZone> result = new ArrayList<>();
        for (RegenZone zone : zones.values()) {
            if (zone.contains(loc)) result.add(zone);
        }
        return result;
    }

    @Override
    public Collection<RegenZone> getZonesByFlag(RegenZoneFlag flag) {
        List<RegenZone> result = new ArrayList<>();
        for (RegenZone zone : zones.values()) {
            if (zone.hasFlag(flag)) result.add(zone);
        }
        return result;
    }

    @Override
    public Collection<RegenZone> getZonesNear(Location loc, int radius) {
        List<RegenZone> result = new ArrayList<>();
        for (RegenZone zone : zones.values()) {
            Location center = zone.getCenter();
            if (center.getWorld().equals(loc.getWorld())
                    && center.distance(loc) <= radius) {
                result.add(zone);
            }
        }
        return result;
    }
}
