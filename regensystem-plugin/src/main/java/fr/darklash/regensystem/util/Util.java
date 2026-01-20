package fr.darklash.regensystem.util;

import fr.darklash.regensystem.RegenSystem;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Util {

    /* ===================== TEXT / FORMAT ===================== */

    private static final MiniMessage MINI = MiniMessage.miniMessage();
    private static final Map<String, String> CODES = new LinkedHashMap<>();

    private static BukkitAudiences audiences() {
        return RegenSystem.getInstance().getAudiences();
    }

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
        for (var entry : CODES.entrySet()) {
            converted = converted.replace(entry.getKey(), entry.getValue());
        }
        return converted;
    }

    public static Component legacy(String legacy) {
        return MINI.deserialize(convertToMiniMessage(legacy))
                .decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> legacy(List<String> lines) {
        if (lines == null) return List.of();
        return lines.stream().map(Util::legacy).toList();
    }

    public static String toLegacyString(Component component) {
        if (component == null) return "";
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static Component getPrefix() {
        String raw = RegenSystem.getInstance()
                .getFileManager()
                .get(Key.File.CONFIG)
                .getString("prefix", "&6[RegenSystem] &r");
        return legacy(raw);
    }

    /* ===================== LANG / MESSAGES ===================== */

    private static final Map<Key.Lang, Map<Key.Message, String>> TRANSLATIONS =
            new EnumMap<>(Key.Lang.class);

    private static Key.Lang CURRENT_LANG = Key.Lang.EN;

    static {
        TRANSLATIONS.put(Key.Lang.EN, Messages.EN);
        TRANSLATIONS.put(Key.Lang.FR, Messages.FR);
    }

    public static void setLang(Key.Lang lang) {
        CURRENT_LANG = lang;
    }

    public static String getRaw(Key.Message key, Map<String, String> placeholders) {
        String raw = TRANSLATIONS.getOrDefault(CURRENT_LANG, Map.of()).get(key);
        if (raw == null) return "";

        if (placeholders != null) {
            for (var entry : placeholders.entrySet()) {
                raw = raw.replace("<" + entry.getKey() + ">", entry.getValue());
            }
        }
        return raw;
    }

    public static Component message(Key.Message key, Map<String, String> placeholders) {
        return legacy(getRaw(key, placeholders));
    }

    /* ===================== SEND ===================== */

    public static void send(Player player, String message) {
        audiences().player(player)
                .sendMessage(getPrefix().append(legacy(message)));
    }

    public static void send(Player player, Component component) {
        audiences().player(player)
                .sendMessage(component);
    }

    public static void sendRaw(Player player, String message) {
        audiences().player(player)
                .sendMessage(legacy(message));
    }

    public static void sendPrefixed(Player player, Component component) {
        audiences().player(player)
                .sendMessage(getPrefix().append(component));
    }

    public static void send(Player player, Key.Message key) {
        send(player, key, null);
    }

    public static void send(Player player, Key.Message key, Map<String, String> placeholders) {
        send(player, message(key, placeholders));
    }
}
