package fr.darklash.regensystem.manager;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.zone.RegenZoneManager;
import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Zone;
import fr.darklash.regensystem.util.RegenLocation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ZoneManager implements RegenZoneManager {

    private final Map<String, RegenZone> zones = new HashMap<>();
    private final Map<String, BukkitTask> scheduledTasks = new HashMap<>();
    private final Map<String, Integer> zoneTimers = new HashMap<>();
    private final Map<String, BukkitTask> timerTasks = new HashMap<>();

    @Override
    public void loadZones() {
        // Annule les anciennes tâches
        for (BukkitTask task : scheduledTasks.values()) {
            task.cancel();
        }
        scheduledTasks.clear();
        for (BukkitTask task : timerTasks.values()) {
            task.cancel();
        }
        timerTasks.clear();
        zones.clear();

        FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);
        ConfigurationSection section = config.getConfigurationSection("zones");
        if (section == null) return;

        for (String zoneName : section.getKeys(false)) {
            ConfigurationSection zoneSec = section.getConfigurationSection(zoneName);
            if (zoneSec == null) continue; // <--- Important !

            boolean globalEnabled = config.getBoolean("global.regen-enabled", true);
            boolean zoneEnabled = zoneSec.getBoolean("enabled", true);

            String rawPos1 = zoneSec.getString("pos1");
            String rawPos2 = zoneSec.getString("pos2");

            if (rawPos1 == null || rawPos2 == null) {
                RegenSystem.getInstance().getLogger().warning("Zone '" + zoneName + "' is missing pos1 or pos2!");
                continue;
            }

            Zone zone = new Zone(
                    zoneName,
                    RegenLocation.fromString(rawPos1),
                    RegenLocation.fromString(rawPos2)
            );
            zone.load();

            zones.put(zoneName, zone);

            if (globalEnabled && zoneEnabled) {
                int delay = zoneSec.getInt("regenDelay", 60);
                startZoneTask(zone, delay);
            }
        }
    }

    private void startZoneTask(RegenZone zone, int delay) {
        String zoneName = zone.getName();
        zoneTimers.put(zoneName, delay);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(RegenSystem.getInstance(), () -> {
            int timeLeft = zoneTimers.get(zoneName);

            if (timeLeft <= 0) {
                zone.regenerate();
                zoneTimers.put(zoneName, delay);
            } else {
                zoneTimers.put(zoneName, timeLeft - 1);
            }
        }, 20L, 20L);

        scheduledTasks.put(zoneName, task);
        timerTasks.put(zoneName, task);
    }

    @Override
    public void reloadZone(String name) {
        FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);
        ConfigurationSection section = config.getConfigurationSection("zones." + name);
        if (section == null) return;

        String rawPos1 = section.getString("pos1");
        String rawPos2 = section.getString("pos2");

        if (rawPos1 == null || rawPos2 == null) return;

        // Stop l'ancienne tâche si elle existe
        BukkitTask oldTask = scheduledTasks.remove(name);
        if (oldTask != null) oldTask.cancel();
        BukkitTask oldTimer = timerTasks.remove(name);
        if (oldTimer != null) oldTimer.cancel();

        Zone zone = new Zone(name, RegenLocation.fromString(rawPos1), RegenLocation.fromString(rawPos2));
        zone.load();

        zones.put(name, zone);

        if (!config.getBoolean("global.regen-enabled", true)) return;
        if (!config.getBoolean("zones." + name + ".enabled", true)) return;

        int delay = section.getInt("regenDelay", 60);
        startZoneTask(zone, delay);
    }

    @Override
    public void deleteZone(String name) {
        RegenZone zone = zones.remove(name);
        if (zone != null) {
            BukkitTask task = scheduledTasks.remove(name);
            if (task != null) task.cancel();
            BukkitTask timer = timerTasks.remove(name);
            if (timer != null) timer.cancel();
            zoneTimers.remove(name);
        }
    }

    @Override
    public void addZone(RegenZone zone) {
        zones.put(zone.getName(), zone);

        FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);

        if (!config.getBoolean("global.regen-enabled", true)) return;
        if (!config.getBoolean("zones." + zone.getName() + ".enabled", true)) return;

        int delay = config.getInt("zones." + zone.getName() + ".regenDelay", 60);
        startZoneTask(zone, delay);
    }

    @Override
    public Collection<RegenZone> getZones() {
        return new ArrayList<>(zones.values());
    }

    @Override
    public boolean isZoneRegistered(String name) {
        return zones.containsKey(name);
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
    public int getTimeLeft(String zoneName) {
        return zoneTimers.getOrDefault(zoneName, -1);
    }
}
