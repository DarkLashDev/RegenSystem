package fr.darklash.regensystem.command;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.api.zone.RegenZoneFlag;
import fr.darklash.regensystem.util.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class Regen implements CommandExecutor, TabCompleter {

    private final Map<Player, org.bukkit.Location[]> selections = new HashMap<>();
    private static final List<String> SUBCOMMANDS = List.of(
            "pos1", "pos2", "save", "reload", "delete", "snapshot", "wand",
            "enable", "disable", "menu", "help", "flag"
    );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        ZoneService service = new ZoneService(RegenSystem.getInstance());

        if (!(sender instanceof Player player)) {
            Util.send((Player) sender, "Order reserved for players.");
            return true;
        }

        if (args.length == 0) {
            Util.send(player, "&cUse : /regen <pos1|pos2|save|reload|delete|snapshot|wand|enable|disable|menu>");
            return true;
        }

        if (!player.hasPermission("regensystem.command")) {
            Util.send(player, "&cYou don't have permission to use this command.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "pos1" -> {
                if (!player.hasPermission("regensystem.pos")) {
                    Util.send(player, "&cYou don't have permission to use this command.");
                    return true;
                }

                selections.computeIfAbsent(player, p -> new Location[2])[0] = player.getLocation();
                Util.send(player, "&aPosition 1 set !");
            }
            case "pos2" -> {
                if (!player.hasPermission("regensystem.pos")) {
                    Util.send(player, "&cYou don't have permission to use this command.");
                    return true;
                }

                selections.computeIfAbsent(player, p -> new Location[2])[1] = player.getLocation();
                Util.send(player, "&aPosition 2 set !");
            }
            case "save" -> {
                if (args.length < 2) {
                    Util.send(player, "&cUse : /regen save <name> [delay]");
                    return true;
                }

                if (!player.hasPermission("regensystem.save")) {
                    Util.send(player, "&cYou don't have permission to register a zone.");
                    return true;
                }

                Location[] locsCommand = selections.get(player);
                Location[] locsHache = RegenSystem.getInstance().getSelectorListener().getSelections().get(player);

                Location pos1 = null;
                Location pos2 = null;

                if (locsCommand != null && locsCommand[0] != null) pos1 = locsCommand[0];
                else if (locsHache != null && locsHache[0] != null) pos1 = locsHache[0];

                if (locsCommand != null && locsCommand[1] != null) pos2 = locsCommand[1];
                else if (locsHache != null && locsHache[1] != null) pos2 = locsHache[1];

                if (pos1 == null || pos2 == null) {
                    Util.send(player, "&cYou have to define pos1 and pos2 first (via command or axe) !");
                    return true;
                }

                String zoneName = args[1];

                int delay = 60;
                if (args.length >= 3) {
                    try {
                        delay = Integer.parseInt(args[2]);
                        if (delay <= 0) {
                            Util.send(player, "&cThe delay must be a positive integer.");
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        Util.send(player, "&cInvalid delay, must be an integer.");
                        return true;
                    }
                }

                service.createZone(zoneName, pos1, pos2, delay);

                Util.send(player, "&eZone &6" + zoneName + " &esaved with a regeneration delay of &b" + delay + " &esec.");
            }
            case "reload" -> {
                if (!player.hasPermission("regensystem.reload")) {
                    Util.send(player, "&cYou are not allowed to reload zones.");
                    return true;
                }

                if (args.length >= 2) {
                    String zoneName = args[1];
                    if (!RegenSystem.getInstance().getFileManager().get(Key.File.ZONE).contains("zones." + zoneName)) {
                        Util.send(player, "&cZone &4'" + zoneName + "' &cnot found.");
                        return true;
                    }
                    RegenSystemAPI.get().reloadZone(zoneName);
                    Util.send(player, "&bZone '" + zoneName + "' reloaded!");
                } else {
                    RegenSystemAPI.get().loadZones();
                    Util.send(player, "&bAll zones reloaded!");
                }
            }
            case "delete" -> {
                if (args.length < 2) {
                    Util.send(player, "&cUse : /regen delete <name>");
                    return true;
                }

                if (!player.hasPermission("regensystem.delete")) {
                    Util.send(player, "&cYou don't have permission to delete a zone.");
                    return true;
                }

                String zoneName = args[1];

                if (RegenSystem.getInstance().getFileManager().get(Key.File.ZONE).contains("zones." + zoneName)) {

                    service.deleteZone(zoneName);

                    Util.send(player, "&eZone &6'" + zoneName + "' &edeleted !");
                } else {
                    Util.send(player, "&cZone &4'" + zoneName + "' &cnot found !");
                }
            }
            case "snapshot" -> {
                if (args.length < 2) {
                    Util.send(player, "&cUse : /regen snapshot <name>");
                    return true;
                }

                if (!player.hasPermission("regensystem.snapshot")) {
                    Util.send(player, "&cYou don't have permission to make a snapshot.");
                    return true;
                }

                String zoneName = args[1];
                RegenZone zone = RegenSystemAPI.get().getZone(zoneName);

                if (zone == null) {
                    Util.send(player, "&cZone &4'" + zoneName + "' &cnot found !");
                    return true;
                }

                service.snapshotZone(zone);

                Util.send(player, "&aSnapshot of recorded zone '&2" + zoneName + "&a'");
            }
            case "wand" -> {
                if (!player.hasPermission("regensystem.wand")) {
                    Util.send(player, "&cYou don't have permission to receive the axe.");
                    return true;
                }

                ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
                ItemMeta meta = axe.getItemMeta();
                if (meta != null) {
                    meta.displayName(Util.legacy("&2Zone selection axe"));
                    meta.lore(Util.legacy(List.of("&eLeft-click = Pos1", "&eRight-click = Pos2")));
                    meta.setCustomModelData(44);
                    axe.setItemMeta(meta);
                }
                player.getInventory().addItem(axe);
                Util.send(player, "&bYou've got the selection axe !");
                return true;
            }
            case "enable" -> {
                if (args.length < 2) {
                    Util.send(player, "&cUse : /regen enable <all|zone_name>");
                    return true;
                }

                if (!player.hasPermission("regensystem.toggle")) {
                    Util.send(player, "&cYou do not have permission to change the status of zones.");
                    return true;
                }

                FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);

                if (args[1].equalsIgnoreCase("all")) {
                    service.enableAllZones();
                    Util.send(player, "&aRegeneration activated for all zones !");
                } else {
                    String zone = args[1];
                    if (!config.contains("zones." + zone)) {
                        Util.send(player, "&cUnknown zone.");
                        return true;
                    }
                    service.enableZone(zone);
                    Util.send(player, "&aZone &2" + zone + " &aactivated !");
                }
            }
            case "disable" -> {
                if (args.length < 2) {
                    Util.send(player, "&cUse : /regen disable <all|zone_name>");
                    return true;
                }

                if (!player.hasPermission("regensystem.toggle")) {
                    Util.send(player, "&cYou do not have permission to change the status of zones.");
                    return true;
                }

                FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);

                if (args[1].equalsIgnoreCase("all")) {
                    service.disableAllZones();
                    Util.send(player, "&cRegeneration disabled for all zones !");
                } else {
                    String zone = args[1];
                    if (!config.contains("zones." + zone)) {
                        Util.send(player, "&cUnknow zone.");
                        return true;
                    }
                    service.disableZone(zone);
                    Util.send(player, "&cZone &4" + zone + " &cdisabled !");
                }
            }
            case "menu" -> {
                if (!player.hasPermission("regensystem.menu")) {
                    Util.send(player, "&cYou don't have permission to use this command.");
                    return true;
                }

                RegenSystem.getInstance().getMenuManager().open(player);
            }
            case "help" -> {
                int page = 1;
                if (args.length >= 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ignored) {
                        Util.send(player, "&cInvalid page number.");
                        return true;
                    }
                }

                // List of help pages (EN only)
                List<String[]> helpPages = List.of(
                        new String[] {
                                "&8&l&m--------------------------",
                                "&6&lRegenSystem &7» &fHelp (1/3)",
                                "",
                                "&e/regen pos1 &7- Set the first corner of the zone at your location.",
                                "&e/regen pos2 &7- Set the second corner of the zone at your location.",
                                "&e/regen save <name> [delay] &7- Save a new zone with an optional delay.",
                                "&e/regen reload [zone] &7- Reload one or all zones.",
                                "&e/regen delete <name> &7- Delete a zone and its stored data.",
                                "&8&l&m--------------------------"
                        },
                        new String[] {
                                "&8&l&m--------------------------",
                                "&6&lRegenSystem &7» &fHelp (2/3)",
                                "",
                                "&e/regen snapshot <name> &7- Update the stored state of a zone.",
                                "&e/regen wand &7- Receive a diamond axe for selection.",
                                "&e/regen enable <all|zone> &7- Enable regen globally or for a zone.",
                                "&e/regen disable <all|zone> &7- Disable regen globally or for a zone.",
                                "&e/regen menu &7- Open the interactive menu.",
                                "&e/regen help [page] &7- Show this help menu.",
                                "&8&l&m--------------------------"
                        },
                        new String[] {
                                "&8&l&m--------------------------",
                                "&6&lRegenSystem &7» &fHelp (3/3)",
                                "",
                                "&e/regen flag <zone> <flag> <on|off> &7- Enables or disables flags for a zone.",
                                "&8&l&m--------------------------"
                        }
                );

                if (page <= 0 || page > helpPages.size()) {
                    Util.send(player, "&cInvalid page. &7(1 - " + helpPages.size() + ")");
                    return true;
                }

                for (String line : helpPages.get(page - 1)) {
                    player.sendMessage(Util.getPrefix().append(Util.legacy(line)));
                }
            }
            case "flag" -> {
                if (!player.hasPermission("regensystem.flag")) {
                    Util.send(player, "&cYou don't have permission to manage flags.");
                    return true;
                }

                if (args.length == 1) {
                    // Affiche tous les flags avec leur description (sans zone)
                    player.sendMessage(Util.getPrefix().append(Util.legacy("&6&lList of available flags :")));
                    for (RegenZoneFlag flag : RegenZoneFlag.values()) {
                        player.sendMessage(Util.getPrefix().append(
                                Util.legacy(" &e" + flag.name() + " &7- &f" + flag.getDescription())
                        ));
                    }
                    return true;
                }

                String zoneName = args[1];
                RegenZone zone = RegenSystemAPI.get().getZone(zoneName);

                if (zone == null) {
                    Util.send(player, "&cZone &4'" + zoneName + "' &cnot found.");
                    return true;
                }

                if (args.length == 2) {
                    // Affiche tous les flags de la zone avec leur état et description
                    player.sendMessage(Util.getPrefix().append(Util.legacy("&6&lFlags for zone &b" + zoneName)));
                    for (RegenZoneFlag flag : RegenZoneFlag.values()) {
                        boolean value = false;
                        value = zone.hasFlag(flag);
                        String status = value ? "&a✅" : "&c❌";
                        player.sendMessage(Util.getPrefix().append(
                                Util.legacy(" &e" + flag.name() + " &7- " + status + " &f" + flag.getDescription())
                        ));
                    }
                    return true;
                }

                if (args.length < 4) {
                    Util.send(player, "&cUse : /regen flag <zone> <flag> <on|off>");
                    return true;
                }

                RegenZoneFlag flag = RegenZoneFlag.fromString(args[2]);
                if (flag == null) {
                    Util.send(player, "&cUnknown flag : &4" + args[2]);
                    return true;
                }

                boolean value;
                if (args[3].equalsIgnoreCase("on") || args[3].equalsIgnoreCase("true")) value = true;
                else if (args[3].equalsIgnoreCase("off") || args[3].equalsIgnoreCase("false")) value = false;
                else {
                    Util.send(player, "&cInvalid value : use &6on/off &cor &6true/false");
                    return true;
                }

                zone.setFlag(flag, value);
                Util.send(player, "&eFlag &6" + flag.name() + " &efor zone &b" + zoneName + " &eset to &a" + value);
            }
            default -> Util.send(player, "&cUnknown command.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return SUBCOMMANDS.stream()
                    .filter(sub -> sub.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("flag")) {
            return RegenSystemAPI.get().getZoneNames().stream()
                    .filter(z -> z.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("flag")) {
            return Arrays.stream(RegenZoneFlag.values())
                    .map(Enum::name)
                    .filter(f -> f.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("flag")) {
            return Stream.of("on", "off")
                    .filter(v -> v.startsWith(args[3].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            List<String> zones = new ArrayList<>(RegenSystemAPI.get().getZoneNames());

            return switch (sub) {
                case "delete", "snapshot", "enable", "disable", "reload" -> {
                    List<String> result = new ArrayList<>();
                    if ("all".startsWith(args[1].toLowerCase())) result.add("all");
                    for (String zone : zones) {
                        if (zone.toLowerCase().startsWith(args[1].toLowerCase())) {
                            result.add(zone);
                        }
                    }
                    yield result;
                }
                case "save" -> getSuggestedZoneNames(args[1], zones);
                default -> Collections.emptyList();
            };
        }

        return Collections.emptyList();
    }

    private @NotNull List<String> getSuggestedZoneNames(String arg, List<String> existing) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String candidate = "zone" + i;
            if (!existing.contains(candidate) && candidate.startsWith(arg.toLowerCase())) {
                suggestions.add(candidate);
            }
        }
        return suggestions;
    }
}
