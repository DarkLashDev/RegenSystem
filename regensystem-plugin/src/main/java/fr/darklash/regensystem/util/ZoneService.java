package fr.darklash.regensystem.util;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.api.zone.RegenZone;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ZoneService {

    private final RegenSystem plugin;

    public ZoneService(RegenSystem plugin) {
        this.plugin = plugin;
    }

    public void createZone(String name, Location pos1, Location pos2, int delay) {
        FileConfiguration config = plugin.getFileManager().get(Key.File.ZONE);

        config.set("zones." + name + ".pos1", ZoneLoc.toString(pos1));
        config.set("zones." + name + ".pos2", ZoneLoc.toString(pos2));
        config.set("zones." + name + ".regenDelay", delay);
        config.set("zones." + name + ".enabled", true);

        plugin.getFileManager().save(Key.File.ZONE);

        RegenZone zone = new Zone(name, pos1, pos2);
        zone.save();
        zone.saveFlags();
        RegenSystemAPI.get().addZone(zone);
    }

    public void deleteZone(String name) {
        FileConfiguration config = plugin.getFileManager().get(Key.File.ZONE);
        config.set("zones." + name, null);
        plugin.getFileManager().save(Key.File.ZONE);
        RegenSystemAPI.get().deleteZone(name);
    }

    public void enableZone(String name) {
        FileConfiguration config = plugin.getFileManager().get(Key.File.ZONE);
        config.set("zones." + name + ".enabled", true);
        plugin.getFileManager().save(Key.File.ZONE);
        RegenSystemAPI.get().loadZones();
    }

    public void disableZone(String name) {
        FileConfiguration config = plugin.getFileManager().get(Key.File.ZONE);
        config.set("zones." + name + ".enabled", false);
        plugin.getFileManager().save(Key.File.ZONE);
        RegenSystemAPI.get().loadZones();
    }

    public void enableAllZones() {
        FileConfiguration config = plugin.getFileManager().get(Key.File.ZONE);
        config.set("global.regen-enabled", true);
        plugin.getFileManager().save(Key.File.ZONE);
        RegenSystemAPI.get().loadZones();
    }

    public void disableAllZones() {
        FileConfiguration config = plugin.getFileManager().get(Key.File.ZONE);
        config.set("global.regen-enabled", false);
        plugin.getFileManager().save(Key.File.ZONE);
        RegenSystemAPI.get().loadZones();
    }

    public void snapshotZone(RegenZone zone) {
        zone.captureState();
        zone.save();
    }
}
