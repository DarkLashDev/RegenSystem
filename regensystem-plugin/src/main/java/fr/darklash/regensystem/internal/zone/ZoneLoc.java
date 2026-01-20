package fr.darklash.regensystem.internal.zone;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ZoneLoc {

    public static String toString(Location loc) {
        if (loc == null || loc.getWorld() == null) return "null";
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public static Location fromString(String s) {
        if (s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null")) return null;

        String[] parts = s.split(",");
        if (parts.length != 4) return null;

        World world = Bukkit.getWorld(parts[0]);
        if (world == null) return null;

        try {
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new org.bukkit.Location(world, x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
