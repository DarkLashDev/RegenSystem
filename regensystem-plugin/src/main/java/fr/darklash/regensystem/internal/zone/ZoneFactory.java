package fr.darklash.regensystem.internal.zone;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.zone.ZoneFlag;
import fr.darklash.regensystem.util.Key;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ZoneFactory {

    private String name;
    private Location corner1;
    private Location corner2;
    private int regenDelay = 60;
    private final Map<ZoneFlag, Boolean> flags = new HashMap<>();
    private boolean saveToConfig = false;
    private boolean registerToManager = false;

    public ZoneFactory name(String name) {
        this.name = name;
        return this;
    }

    public ZoneFactory corner1(Location loc) {
        this.corner1 = loc;
        return this;
    }

    public ZoneFactory corner2(Location loc) {
        this.corner2 = loc;
        return this;
    }

    public ZoneFactory delay(int seconds) {
        this.regenDelay = seconds;
        return this;
    }

    public ZoneFactory flag(ZoneFlag flag, boolean value) {
        this.flags.put(flag, value);
        return this;
    }

    public ZoneFactory save(boolean saveToConfig) {
        this.saveToConfig = saveToConfig;
        return this;
    }

    public ZoneFactory register(boolean registerToManager) {
        this.registerToManager = registerToManager;
        return this;
    }

    public ZoneImpl build() {
        if (name == null || name.isBlank())
            throw new IllegalStateException("ZoneImpl name cannot be null or empty");
        if (corner1 == null || corner2 == null)
            throw new IllegalStateException("Both corners must be defined");

        ZoneImpl zone = new ZoneImpl(name, corner1, corner2);

        for (ZoneFlag flag : ZoneFlag.values()) {
            flags.putIfAbsent(flag, flag.getDefaultValue());
        }

        for (Map.Entry<ZoneFlag, Boolean> e : flags.entrySet()) {
            zone.setFlag(e.getKey(), e.getValue());
        }

        if (saveToConfig) {
            FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);
            config.set("zones." + name + ".pos1", ZoneLoc.toString(corner1));
            config.set("zones." + name + ".pos2", ZoneLoc.toString(corner2));
            config.set("zones." + name + ".regenDelay", regenDelay);
            config.set("zones." + name + ".enabled", true);

            // Flags
            for (Map.Entry<ZoneFlag, Boolean> e : flags.entrySet()) {
                config.set("zones." + name + ".flags." + e.getKey().name(), e.getValue());
            }

            RegenSystem.getInstance().getFileManager().save(Key.File.ZONE);
        }

        zone.captureState();
        zone.save();

        if (registerToManager) {
            RegenSystem.getInstance().getZoneManager().addZone(zone);
        }

        RegenSystem.getInstance().getLogger().info("✅ ZoneImpl '" + name + "' created with builder (delay=" + regenDelay + "s)");

        return zone;
    }
}
