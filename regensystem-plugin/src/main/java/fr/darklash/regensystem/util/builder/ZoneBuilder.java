package fr.darklash.regensystem.util.builder;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.api.zone.RegenZoneFlag;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Zone;
import fr.darklash.regensystem.util.ZoneLoc;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ZoneBuilder {

    private String name;
    private Location corner1;
    private Location corner2;
    private int regenDelay = 60;
    private final Map<RegenZoneFlag, Boolean> flags = new HashMap<>();
    private boolean saveToConfig = false;
    private boolean registerToManager = false;

    public ZoneBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ZoneBuilder corner1(Location loc) {
        this.corner1 = loc;
        return this;
    }

    public ZoneBuilder corner2(Location loc) {
        this.corner2 = loc;
        return this;
    }

    public ZoneBuilder delay(int seconds) {
        this.regenDelay = seconds;
        return this;
    }

    public ZoneBuilder flag(RegenZoneFlag flag, boolean value) {
        this.flags.put(flag, value);
        return this;
    }

    public ZoneBuilder save(boolean saveToConfig) {
        this.saveToConfig = saveToConfig;
        return this;
    }

    public ZoneBuilder register(boolean registerToManager) {
        this.registerToManager = registerToManager;
        return this;
    }

    public RegenZone build() {
        if (name == null || name.isBlank())
            throw new IllegalStateException("Zone name cannot be null or empty");
        if (corner1 == null || corner2 == null)
            throw new IllegalStateException("Both corners must be defined");

        RegenZone zone = new Zone(name, corner1, corner2);

        for (RegenZoneFlag flag : RegenZoneFlag.values()) {
            flags.putIfAbsent(flag, flag.getDefaultValue());
        }

        for (Map.Entry<RegenZoneFlag, Boolean> e : flags.entrySet()) {
            zone.setFlag(e.getKey(), e.getValue());
        }

        if (saveToConfig) {
            FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);
            config.set("zones." + name + ".pos1", ZoneLoc.toString(corner1));
            config.set("zones." + name + ".pos2", ZoneLoc.toString(corner2));
            config.set("zones." + name + ".regenDelay", regenDelay);
            config.set("zones." + name + ".enabled", true);

            // Flags
            for (Map.Entry<RegenZoneFlag, Boolean> e : flags.entrySet()) {
                config.set("zones." + name + ".flags." + e.getKey().name(), e.getValue());
            }

            RegenSystem.getInstance().getFileManager().save(Key.File.ZONE);
        }

        zone.captureState();
        zone.save();

        if (registerToManager) {
            RegenSystemAPI.get().addZone(zone);
        }

        RegenSystem.getInstance().getLogger().info("✅ Zone '" + name + "' created with builder (delay=" + regenDelay + "s)");

        return zone;
    }
}
