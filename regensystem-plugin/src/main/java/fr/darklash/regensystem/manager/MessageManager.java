package fr.darklash.regensystem.manager;

import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class MessageManager {

    private static final Map<Key.Lang, Map<Key.Message, String>> translations = new EnumMap<>(Key.Lang.class);
    private static Key.Lang currentLang = Key.Lang.EN;

    static {
        // === ENGLISH ===
        Map<Key.Message, String> en = new EnumMap<>(Key.Message.class);
        en.put(Key.Message.ONLY_PLAYERS, "&cOnly players can use this command.");
        en.put(Key.Message.UNKNOWN_SUBCOMMAND, "&cUnknown subcommand.");
        en.put(Key.Message.REGEN_CMD_USAGE, "&cUse : /regen <subcommand>");
        en.put(Key.Message.USAGE, "&cUse: <usage>");
        en.put(Key.Message.POSITION1_SET, "&aPosition 1 set!");
        en.put(Key.Message.POSITION2_SET, "&aPosition 2 set!");
        en.put(Key.Message.ZONE_SAVED, "&aZone <name> saved successfully!");
        en.put(Key.Message.ZONE_DELETED, "&cZone <name> deleted.");
        en.put(Key.Message.ZONE_NOT_FOUND, "&cZone <name> not found.");
        en.put(Key.Message.NO_PERMISSION, "&cYou don't have permission to do this.");
        en.put(Key.Message.RELOADED, "&aRegenSystem reloaded!");
        en.put(Key.Message.POSITIONS_NOT_SET, "&cYou must define pos1 and pos2 first (via command or axe)!");
        en.put(Key.Message.DELAY_MUST_BE_POSITIVE, "&cThe delay must be a positive integer.");
        en.put(Key.Message.UNKNOWN_FLAG, "&cUnknown flag: &4<flag>");
        en.put(Key.Message.INVALID_FLAG_VALUE, "&cInvalid flag value for &6<flag> &c: use true/false or on/off");
        en.put(Key.Message.FLAG_SET, "&eFlag &6<flag> &eset to &a<value>");
        en.put(Key.Message.ZONE_SAVED_DELAY, "&eZone &6<name> &esaved with a regeneration delay of &b<delay> &esec.");
        en.put(Key.Message.SNAPSHOT_CREATED, "&aSnapshot of recorded zone '&2<name>&a' created!");
        en.put(Key.Message.WAND_RECEIVED, "&bYou've got the selection axe!");
        en.put(Key.Message.ZONE_RELOADED, "&bZone '<name>' reloaded!");
        en.put(Key.Message.ALL_ZONES_RELOADED, "&bAll zones reloaded!");
        en.put(Key.Message.HELP_PAGE_1, String.join("\n",
                "&8&l&m--------------------------",
                "&6&lRegenSystem &7» &fHelp (1/3)",
                "",
                "&e/regen pos1 &7- Set the first corner of the zone at your location.",
                "&e/regen pos2 &7- Set the second corner of the zone at your location.",
                "&e/regen save <name> [delay] [f:flag=value]... &7- Save a new zone with an optional delay.",
                "&e/regen reload [zone] &7- Reload one or all zones.",
                "&e/regen delete <name> &7- Delete a zone and its stored data.",
                "&8&l&m--------------------------"
        ));
        en.put(Key.Message.HELP_PAGE_2, String.join("\n",
                "&8&l&m--------------------------",
                "&6&lRegenSystem &7» &fHelp (2/3)",
                "",
                "&e/regen snapshot <name> &7- Update the stored state of a zone.",
                "&e/regen wand &7- Receive a diamond axe for selection.",
                "&e/regen enable <all|name> &7- Enable regen globally or for a zone.",
                "&e/regen disable <all|name> &7- Disable regen globally or for a zone.",
                "&e/regen menu &7- Open the interactive menu.",
                "&e/regen help [page] &7- Show this help menu.",
                "&8&l&m--------------------------"
        ));
        en.put(Key.Message.HELP_PAGE_3, String.join("\n",
                "&8&l&m--------------------------",
                "&6&lRegenSystem &7» &fHelp (3/3)",
                "",
                "&e/regen flag <zone> <flag> <on|off> &7- Enables or disables flags for a zone.",
                "&e/regen lang <language> &7- Set the plugin language.",
                "&8&l&m--------------------------"
        ));
        en.put(Key.Message.FLAG_LIST_HEADER, "&6&lList of available flags:");
        en.put(Key.Message.FLAGS_FOR_ZONE, "&6&lFlags for zone &b<zone>");
        en.put(Key.Message.ZONE_ENABLED, "&aZone <name> activated!");
        en.put(Key.Message.ALL_ZONES_ENABLED, "&aRegeneration activated for all zones!");
        en.put(Key.Message.ZONE_DISABLED, "&cZone <name> disabled!");
        en.put(Key.Message.ALL_ZONES_DISABLED, "&cRegeneration disabled for all zones!");
        en.put(Key.Message.LANGUAGE_SET, "&aLanguage set to : &e<lang>");
        en.put(Key.Message.UNKNOWN_LANGUAGE, "&cUnknown language. Available : EN, FR");
        en.put(Key.Message.UNKNOWN_ACTION, "Unknown action.");
        en.put(Key.Message.INTERNAL_ERROR, "&cAn internal error has occurred.");
        translations.put(Key.Lang.EN, en);

        // === FRENCH ===
        Map<Key.Message, String> fr = new EnumMap<>(Key.Message.class);
        fr.put(Key.Message.ONLY_PLAYERS, "&cSeuls les joueurs peuvent utiliser cette commande.");
        fr.put(Key.Message.UNKNOWN_SUBCOMMAND, "&cSous-commande inconnue.");
        fr.put(Key.Message.REGEN_CMD_USAGE, "&cUtilisation : /regen <sous-commande>");
        fr.put(Key.Message.USAGE, "&cUtilisation : <usage>");
        fr.put(Key.Message.POSITION1_SET, "&aPosition 1 définie !");
        fr.put(Key.Message.POSITION2_SET, "&aPosition 2 définie !");
        fr.put(Key.Message.ZONE_SAVED, "&aZone <name> sauvegardée avec succès !");
        fr.put(Key.Message.ZONE_DELETED, "&cZone <name> supprimée.");
        fr.put(Key.Message.ZONE_NOT_FOUND, "&cZone <name> introuvable.");
        fr.put(Key.Message.NO_PERMISSION, "&cVous n’avez pas la permission pour faire ça.");
        fr.put(Key.Message.RELOADED, "&aRegenSystem rechargé !");
        fr.put(Key.Message.POSITIONS_NOT_SET, "&cVous devez définir pos1 et pos2 d'abord (via commande ou hache) !");
        fr.put(Key.Message.DELAY_MUST_BE_POSITIVE, "&cLe délai doit être un entier positif.");
        fr.put(Key.Message.UNKNOWN_FLAG, "&cFlag inconnu : &4<flag>");
        fr.put(Key.Message.INVALID_FLAG_VALUE, "&cValeur de flag invalide pour &6<flag> &c: utilisez true/false ou on/off");
        fr.put(Key.Message.FLAG_SET, "&eFlag &6<flag> &aest défini sur &a<value>");
        fr.put(Key.Message.ZONE_SAVED_DELAY, "&eZone &6<name> &asauvegardée avec un délai de régénération de &b<delay> &esecondes.");
        fr.put(Key.Message.SNAPSHOT_CREATED, "&aSnapshot de la zone '&2<name>&a' créé !");
        fr.put(Key.Message.WAND_RECEIVED, "&bVous avez reçu la hache de sélection !");
        fr.put(Key.Message.ZONE_RELOADED, "&bZone '<name>' rechargée !");
        fr.put(Key.Message.ALL_ZONES_RELOADED, "&bToutes les zones ont été rechargées !");
        fr.put(Key.Message.HELP_PAGE_1, String.join("\n",
                "&8&l&m--------------------------",
                "&6&lRegenSystem &7» &fAide (1/3)",
                "",
                "&e/regen pos1 &7- Définit le premier coin de la zone à votre position.",
                "&e/regen pos2 &7- Définit le deuxième coin de la zone à votre position.",
                "&e/regen save <nom> [délai] &7- Sauvegarde une nouvelle zone avec un délai optionnel.",
                "&e/regen reload [zone] &7- Recharge une ou toutes les zones.",
                "&e/regen delete <nom> &7- Supprime une zone et ses données.",
                "&8&l&m--------------------------"
        ));
        fr.put(Key.Message.HELP_PAGE_2, String.join("\n",
                "&8&l&m--------------------------",
                "&6&lRegenSystem &7» &fAide (2/3)",
                "",
                "&e/regen snapshot <nom> &7- Met à jour l’état enregistré d’une zone.",
                "&e/regen wand &7- Donne une hache de sélection.",
                "&e/regen enable <all|zone> &7- Active la régénération globalement ou pour une zone.",
                "&e/regen disable <all|zone> &7- Désactive la régénération globalement ou pour une zone.",
                "&e/regen menu &7- Ouvre le menu interactif.",
                "&e/regen help [page] &7- Affiche ce menu d’aide.",
                "&8&l&m--------------------------"
        ));
        fr.put(Key.Message.HELP_PAGE_3, String.join("\n",
                "&8&l&m--------------------------",
                "&6&lRegenSystem &7» &fAide (3/3)",
                "",
                "&e/regen flag <zone> <flag> <on|off> &7- Active ou désactive les flags d’une zone.",
                "&e/regen lang <langage> &7- Définit la langue du plugin.",
                "&8&l&m--------------------------"
        ));
        fr.put(Key.Message.FLAG_LIST_HEADER, "&6&lListe des flags disponibles :");
        fr.put(Key.Message.FLAGS_FOR_ZONE, "&6&lFlags de la zone &b<zone>");
        fr.put(Key.Message.ZONE_ENABLED, "&aZone <name> activée !");
        fr.put(Key.Message.ALL_ZONES_ENABLED, "&aRégénération activée pour toutes les zones !");
        fr.put(Key.Message.ZONE_DISABLED, "&cZone <name> désactivée !");
        fr.put(Key.Message.ALL_ZONES_DISABLED, "&cRégénération désactivée pour toutes les zones !");
        fr.put(Key.Message.LANGUAGE_SET, "&aLangue définie sur : &e<lang>");
        fr.put(Key.Message.UNKNOWN_LANGUAGE, "&cLangue inconnue. Disponibles : EN, FR");
        fr.put(Key.Message.UNKNOWN_ACTION, "Action inconnue.");
        fr.put(Key.Message.INTERNAL_ERROR, "&cUne erreur interne est survenue.");
        translations.put(Key.Lang.FR, fr);
    }

    public static void setLang(Key.Lang lang) {
        currentLang = lang;
    }

    public static String getRaw(Key.Message key, Map<String, String> placeholders) {
        String raw = translations.get(currentLang).get(key);
        if (raw == null) return "";

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                raw = raw.replace("<" + entry.getKey() + ">", entry.getValue());
            }
        }

        return raw;
    }

    public static Component asComponent(Key.Message key, Map<String, String> placeholders) {
        return Util.legacy(getRaw(key, placeholders));
    }

    public static void send(Player player, Key.Message key, Map<String, String> placeholders) {
        Util.send(player, getRaw(key, placeholders));
    }

    public static void send(Player player, Key.Message key) {
        send(player, key, null);
    }
}
