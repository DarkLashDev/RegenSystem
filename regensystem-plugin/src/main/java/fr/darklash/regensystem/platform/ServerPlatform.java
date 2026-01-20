package fr.darklash.regensystem.platform;

import org.bukkit.Bukkit;

public final class ServerPlatform {

    private static Platform cached;

    public static Platform detect() {
        if (cached != null) return cached;

        if (isFolia()) {
            cached = Platform.FOLIA;
        } else if (isPaper()) {
            cached = Platform.PAPER;
        } else {
            cached = Platform.SPIGOT;
        }
        return cached;
    }

    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isPaper() {
        // Paper + forks (Purpur, Pufferfish…)
        return Bukkit.getServer().getName().toLowerCase().contains("paper");
    }

    private ServerPlatform() {}
}
