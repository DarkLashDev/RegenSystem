package fr.darklash.regensystem.util;

import fr.darklash.regensystem.RegenSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Util {

    private static final MiniMessage MINI = MiniMessage.miniMessage();
    private static final Map<String, String> CODES = new LinkedHashMap<>();

    static {
        // Colors
        CODES.put("&0", "<black>");
        CODES.put("&1", "<dark_blue>");
        CODES.put("&2", "<dark_green>");
        CODES.put("&3", "<dark_aqua>");
        CODES.put("&4", "<dark_red>");
        CODES.put("&5", "<dark_purple>");
        CODES.put("&6", "<gold>");
        CODES.put("&7", "<gray>");
        CODES.put("&8", "<dark_gray>");
        CODES.put("&9", "<blue>");
        CODES.put("&a", "<green>");
        CODES.put("&b", "<aqua>");
        CODES.put("&c", "<red>");
        CODES.put("&d", "<light_purple>");
        CODES.put("&e", "<yellow>");
        CODES.put("&f", "<white>");

        // Styles
        CODES.put("&k", "<obfuscated>");
        CODES.put("&l", "<bold>");
        CODES.put("&m", "<strikethrough>");
        CODES.put("&n", "<underlined>");
        CODES.put("&o", "<italic>");
        CODES.put("&r", "</reset>");
    }

    private static String convertToMiniMessage(String legacy) {
        if (legacy == null) return "";
        String converted = legacy;
        for (Map.Entry<String, String> entry : CODES.entrySet()) {
            converted = converted.replace(entry.getKey(), entry.getValue());
        }
        return converted;
    }

    public static Component miniItalic(String legacy) {
        return MINI.deserialize(convertToMiniMessage(legacy));
    }

    public static Component miniNoItalic(String legacy) {
        return MINI.deserialize(convertToMiniMessage(legacy))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static void send(Player player, String message) {
        player.sendMessage(getPrefix().append(miniNoItalic(message)));
    }

    public static void sendRaw(Player player, String message) {
        player.sendMessage(miniNoItalic(message));
    }

    public static Component legacy(String legacy) {
        return miniNoItalic(legacy);
    }

    public static List<Component> legacy(List<String> lines) {
        return lines.stream().map(Util::legacy).toList();
    }

    public static String stripColor(String input) {
        if (input == null) return "";
        return input.replaceAll("(?i)(&[0-9a-fk-or])|(§[0-9a-fk-or])", "");
    }

    public static Component getPrefix() {
        String rawPrefix = RegenSystem.getInstance().getFileManager().get(Key.File.CONFIG).getString("prefix", "&6[RegenSystem] &r");
        return miniNoItalic(rawPrefix);
    }
}
