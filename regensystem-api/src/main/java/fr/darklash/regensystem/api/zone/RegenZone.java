package fr.darklash.regensystem.api.zone;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import java.util.Map;

public interface RegenZone {

    String getName();

    Location getCorner1();

    Location getCorner2();

    Location getCenter();

    boolean isEnabled();

    boolean contains(Location location);

    void regenerate();

    void captureState();

    void save();

    void load();

    Map<String, BlockData> getOriginalBlocks();

    Map<String, String> getOriginalBlockData();
}
