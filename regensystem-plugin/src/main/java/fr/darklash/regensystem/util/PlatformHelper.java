package fr.darklash.regensystem.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;

public class PlatformHelper {

    private static Method TELEPORT_ASYNC;

    static {
        if (ServerPlatform.isFolia()) {
            try {
                TELEPORT_ASYNC = Player.class.getMethod("teleportAsync", Location.class);
            } catch (NoSuchMethodException ignored) {
            }
        }
    }

    public static void setDisplayName(ItemMeta meta, Component name) {
        try {
            meta.displayName(name);
        } catch (NoSuchMethodError e) {
            meta.setDisplayName(Util.toLegacyString(name));
        }
    }

    public static void setLore(ItemMeta meta, List<Component> lore) {
        try {
            meta.lore(lore);
        } catch (NoSuchMethodError e) {
            meta.setLore(
                    lore.stream()
                            .map(Util::toLegacyString)
                            .toList()
            );
        }
    }

    public static void teleport(Player player, Location location) {
        if (player == null || location == null) return;

        if (ServerPlatform.isFolia() && TELEPORT_ASYNC != null) {
            try {
                TELEPORT_ASYNC.invoke(player, location);
                return;
            } catch (Throwable ignored) {
                // fallback
            }
        }

        player.teleport(location);
    }
}
