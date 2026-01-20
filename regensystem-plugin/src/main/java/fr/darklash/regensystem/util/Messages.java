package fr.darklash.regensystem.util;

import java.util.EnumMap;
import java.util.Map;

public class Messages {

    static final Map<Key.Message, String> EN = new EnumMap<>(Key.Message.class);
    static final Map<Key.Message, String> FR = new EnumMap<>(Key.Message.class);

    static {
        // === ENGLISH ===
        EN.put(Key.Message.ONLY_PLAYERS, "&cOnly players can use this command.");
        EN.put(Key.Message.UNKNOWN_SUBCOMMAND, "&cUnknown subcommand.");
        EN.put(Key.Message.REGEN_CMD_USAGE, "&cUse : /regen <subcommand>");
        EN.put(Key.Message.USAGE, "&cUse: <usage>");
        EN.put(Key.Message.POSITION1_SET, "&aPosition 1 set!");
        EN.put(Key.Message.POSITION2_SET, "&aPosition 2 set!");
        EN.put(Key.Message.ZONE_SAVED, "&aZone <name> saved successfully!");
        EN.put(Key.Message.ZONE_DELETED, "&cZone <name> deleted.");
        EN.put(Key.Message.ZONE_NOT_FOUND, "&cZone <name> not found.");
        EN.put(Key.Message.NO_PERMISSION, "&cYou don't have permission to do this.");
        EN.put(Key.Message.RELOADED, "&aRegenSystem reloaded!");
        EN.put(Key.Message.POSITIONS_NOT_SET, "&cYou must define pos1 and pos2 first (via command or axe)!");
        EN.put(Key.Message.DELAY_MUST_BE_POSITIVE, "&cThe delay must be a positive integer.");
        EN.put(Key.Message.UNKNOWN_FLAG, "&cUnknown flag: &4<flag>");
        EN.put(Key.Message.INVALID_FLAG_VALUE, "&cInvalid flag value for &6<flag> &c: use true/false or on/off");
        EN.put(Key.Message.FLAG_SET, "&eFlag &6<flag> &eset to &a<value>");
        EN.put(Key.Message.ZONE_SAVED_DELAY, "&eZone &6<name> &esaved with a regeneration delay of &b<delay> &esec.");
        EN.put(Key.Message.SNAPSHOT_CREATED, "&aSnapshot of recorded zone '&2<name>&a' created!");
        EN.put(Key.Message.WAND_RECEIVED, "&bYou've got the selection axe!");
        EN.put(Key.Message.ZONE_RELOADED, "&bZone '<name>' reloaded!");
        EN.put(Key.Message.ALL_ZONES_RELOADED, "&bAll zones reloaded!");
        EN.put(Key.Message.HELP_PAGE_1, String.join("\n",
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
        EN.put(Key.Message.HELP_PAGE_2, String.join("\n",
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
        EN.put(Key.Message.HELP_PAGE_3, String.join("\n",
                "&8&l&m--------------------------",
                "&6&lRegenSystem &7» &fHelp (3/3)",
                "",
                "&e/regen flag <zone> <flag> <on|off> &7- Enables or disables flags for a zone.",
                "&e/regen lang <language> &7- Set the plugin language.",
                "&8&l&m--------------------------"
        ));
        EN.put(Key.Message.FLAG_LIST_HEADER, "&6&lList of available flags:");
        EN.put(Key.Message.FLAGS_FOR_ZONE, "&6&lFlags for zone &b<zone>");
        EN.put(Key.Message.ZONE_ENABLED, "&aZone <name> activated!");
        EN.put(Key.Message.ALL_ZONES_ENABLED, "&aRegeneration activated for all zones!");
        EN.put(Key.Message.ZONE_DISABLED, "&cZone <name> disabled!");
        EN.put(Key.Message.ALL_ZONES_DISABLED, "&cRegeneration disabled for all zones!");
        EN.put(Key.Message.LANGUAGE_SET, "&aLanguage set to : &e<lang>");
        EN.put(Key.Message.UNKNOWN_LANGUAGE, "&cUnknown language. Available : EN, FR");
        EN.put(Key.Message.UNKNOWN_ACTION, "Unknown action.");
        EN.put(Key.Message.INTERNAL_ERROR, "&cAn internal error has occurred.");

        // === FRENCH ===
        FR.put(Key.Message.ONLY_PLAYERS, "&cSeuls les joueurs peuvent utiliser cette commande.");
        FR.put(Key.Message.UNKNOWN_SUBCOMMAND, "&cSous-commande inconnue.");
        FR.put(Key.Message.REGEN_CMD_USAGE, "&cUtilisation : /regen <sous-commande>");
        FR.put(Key.Message.USAGE, "&cUtilisation : <usage>");
        FR.put(Key.Message.POSITION1_SET, "&aPosition 1 définie !");
        FR.put(Key.Message.POSITION2_SET, "&aPosition 2 définie !");
        FR.put(Key.Message.ZONE_SAVED, "&aZone <name> sauvegardée avec succès !");
        FR.put(Key.Message.ZONE_DELETED, "&cZone <name> supprimée.");
        FR.put(Key.Message.ZONE_NOT_FOUND, "&cZone <name> introuvable.");
        FR.put(Key.Message.NO_PERMISSION, "&cVous n’avez pas la permission pour faire ça.");
        FR.put(Key.Message.RELOADED, "&aRegenSystem rechargé !");
        FR.put(Key.Message.POSITIONS_NOT_SET, "&cVous devez définir pos1 et pos2 d'abord (via commande ou hache) !");
        FR.put(Key.Message.DELAY_MUST_BE_POSITIVE, "&cLe délai doit être un entier positif.");
        FR.put(Key.Message.UNKNOWN_FLAG, "&cFlag inconnu : &4<flag>");
        FR.put(Key.Message.INVALID_FLAG_VALUE, "&cValeur de flag invalide pour &6<flag> &c: utilisez true/false ou on/off");
        FR.put(Key.Message.FLAG_SET, "&eFlag &6<flag> &aest défini sur &a<value>");
        FR.put(Key.Message.ZONE_SAVED_DELAY, "&eZone &6<name> &asauvegardée avec un délai de régénération de &b<delay> &esecondes.");
        FR.put(Key.Message.SNAPSHOT_CREATED, "&aSnapshot de la zone '&2<name>&a' créé !");
        FR.put(Key.Message.WAND_RECEIVED, "&bVous avez reçu la hache de sélection !");
        FR.put(Key.Message.ZONE_RELOADED, "&bZone '<name>' rechargée !");
        FR.put(Key.Message.ALL_ZONES_RELOADED, "&bToutes les zones ont été rechargées !");
        FR.put(Key.Message.HELP_PAGE_1, String.join("\n",
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
        FR.put(Key.Message.HELP_PAGE_2, String.join("\n",
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
        FR.put(Key.Message.HELP_PAGE_3, String.join("\n",
                "&8&l&m--------------------------",
                "&6&lRegenSystem &7» &fAide (3/3)",
                "",
                "&e/regen flag <zone> <flag> <on|off> &7- Active ou désactive les flags d’une zone.",
                "&e/regen lang <langage> &7- Définit la langue du plugin.",
                "&8&l&m--------------------------"
        ));
        FR.put(Key.Message.FLAG_LIST_HEADER, "&6&lListe des flags disponibles :");
        FR.put(Key.Message.FLAGS_FOR_ZONE, "&6&lFlags de la zone &b<zone>");
        FR.put(Key.Message.ZONE_ENABLED, "&aZone <name> activée !");
        FR.put(Key.Message.ALL_ZONES_ENABLED, "&aRégénération activée pour toutes les zones !");
        FR.put(Key.Message.ZONE_DISABLED, "&cZone <name> désactivée !");
        FR.put(Key.Message.ALL_ZONES_DISABLED, "&cRégénération désactivée pour toutes les zones !");
        FR.put(Key.Message.LANGUAGE_SET, "&aLangue définie sur : &e<lang>");
        FR.put(Key.Message.UNKNOWN_LANGUAGE, "&cLangue inconnue. Disponibles : EN, FR");
        FR.put(Key.Message.UNKNOWN_ACTION, "Action inconnue.");
        FR.put(Key.Message.INTERNAL_ERROR, "&cUne erreur interne est survenue.");
    }

    private Messages() {}
}
