package fr.darklash.regensystem.internal.zone;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.event.ZoneCreateEvent;
import fr.darklash.regensystem.api.event.ZoneDeleteEvent;
import fr.darklash.regensystem.api.event.ZoneReloadEvent;
import fr.darklash.regensystem.api.regen.RegenResult;
import fr.darklash.regensystem.api.zone.ZoneFlag;
import fr.darklash.regensystem.api.zone.Zone;
import fr.darklash.regensystem.api.zone.ZoneManager;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.platform.scheduler.RegenScheduler;
import fr.darklash.regensystem.platform.scheduler.RegenTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ZoneManagerImpl implements ZoneManager {

    private final Map<String, ZoneImpl> zones = new HashMap<>();
    private final Map<String, RegenTask> zoneTasks = new HashMap<>();
    private final Map<String, Integer> zoneTimers = new HashMap<>();

    private final RegenScheduler scheduler = RegenSystem.getInstance().getScheduler();

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
                RegenSystem.getInstance().getLogger().warning("ZoneImpl '" + zoneName + "' is missing pos1 or pos2!");
                continue;
            }

            ZoneImpl zone = new ZoneImpl(
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

    private void startZoneTimer(Zone zone, int delaySeconds) {
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

    private void tickZone(Zone zone, int delaySeconds) {
        String name = zone.getName();
        int remaining = zoneTimers.getOrDefault(name, delaySeconds);

        if (remaining <= 0) {
            RegenResult result = zone.regenerate();

            if (result == RegenResult.SUCCESS || result == RegenResult.SKIP) {
                zoneTimers.put(name, delaySeconds);
            } else if (result == RegenResult.STOP) {
                // on retente plus tard sans reset
                zoneTimers.put(name, 1);
            }

            return;
        }

        zoneTimers.put(name, remaining - 1);
    }

    public void reloadZone(String name) {
        Zone oldZone = zones.get(name);

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

        ZoneImpl newZone = new ZoneImpl(
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

    public void deleteZone(String name) {
        Zone zone = zones.get(name);
        if (zone == null) return;

        ZoneDeleteEvent event = new ZoneDeleteEvent(zone);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        zones.remove(name);

        RegenTask task = zoneTasks.remove(name);
        if (task != null) task.cancel();

        zoneTimers.remove(name);
    }

    public void addZone(ZoneImpl zone) {
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
    public Collection<Zone> getZones() {
        return new ArrayList<>(zones.values());
    }

    @Override
    public Optional<Zone> getZone(String name) {
        return Optional.ofNullable(zones.get(name));
    }

    public Optional<ZoneImpl> getZoneInternal(String name) {
        return Optional.ofNullable(zones.get(name));
    }

    @Override
    public Set<String> getZoneNames() {
        return zones.keySet();
    }

    @Override
    public boolean isRegistered(String name) {
        return zones.containsKey(name);
    }

    @Override
    public int getTimeLeft(String zoneName) {
        return zoneTimers.getOrDefault(zoneName, -1);
    }

    @Override
    public Collection<Zone> getZonesContaining(Location loc) {
        List<Zone> result = new ArrayList<>();
        for (Zone zone : zones.values()) {
            if (zone.contains(loc)) result.add(zone);
        }
        return result;
    }

    @Override
    public Collection<Zone> getZonesByFlag(ZoneFlag flag) {
        List<Zone> result = new ArrayList<>();
        for (Zone zone : zones.values()) {
            if (zone.hasFlag(flag)) result.add(zone);
        }
        return result;
    }

    @Override
    public Collection<Zone> getZonesNear(Location loc, int radius) {
        List<Zone> result = new ArrayList<>();
        for (Zone zone : zones.values()) {
            Location center = zone.getCenter();
            if (center.getWorld().equals(loc.getWorld())
                    && center.distance(loc) <= radius) {
                result.add(zone);
            }
        }
        return result;
    }
}
