package fr.darklash.regensystem.command;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemProvider;
import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Util;
import fr.darklash.regensystem.util.Zone;
import fr.darklash.regensystem.util.RegenLocation;
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

public class Regen implements CommandExecutor, TabCompleter {

    private final Map<Player, org.bukkit.Location[]> selections = new HashMap<>();
    private static final List<String> SUBCOMMANDS = List.of(
            "pos1", "pos2", "save", "reload", "delete", "snapshot", "wand",
            "enable", "disable", "menu"
    );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

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
                Location[] locsHache = RegenSystem.getInstance().getZoneSelectorListener().getSelections().get(player);

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

                FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);
                config.set("zones." + zoneName + ".pos1", RegenLocation.toString(pos1));
                config.set("zones." + zoneName + ".pos2", RegenLocation.toString(pos2));
                config.set("zones." + zoneName + ".regenDelay", delay);
                config.set("zones." + zoneName + ".enabled", true);
                RegenSystem.getInstance().getFileManager().save(Key.File.ZONE);

                RegenZone zone = new Zone(zoneName, pos1, pos2);
                zone.save();
                RegenSystemProvider.get().addZone(zone);

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
                    RegenSystemProvider.get().reloadZone(zoneName);
                    Util.send(player, "&bZone '" + zoneName + "' reloaded!");
                } else {
                    RegenSystemProvider.get().loadZones();
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
                    RegenSystem.getInstance().getFileManager().get(Key.File.ZONE).set("zones." + zoneName, null);
                    RegenSystem.getInstance().getFileManager().save(Key.File.ZONE);

                    RegenSystemProvider.get().deleteZone(zoneName);

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
                RegenZone zone = RegenSystemProvider.get().getZone(zoneName);

                if (zone == null) {
                    Util.send(player, "&cZone &4'" + zoneName + "' &cnot found !");
                    return true;
                }

                zone.captureState();
                zone.save();
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
                    config.set("global.regen-enabled", true);
                    RegenSystem.getInstance().getFileManager().save(Key.File.ZONE);
                    Util.send(player, "&aRegeneration activated for all zones !");
                    RegenSystemProvider.get().loadZones();
                } else {
                    String zone = args[1];
                    if (!config.contains("zones." + zone)) {
                        Util.send(player, "&cUnknown zone.");
                        return true;
                    }
                    config.set("zones." + zone + ".enabled", true);
                    RegenSystem.getInstance().getFileManager().save(Key.File.ZONE);
                    Util.send(player, "&aZone &2" + zone + " &aactivated !");
                    RegenSystemProvider.get().loadZones();
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
                    config.set("global.regen-enabled", false);
                    RegenSystem.getInstance().getFileManager().save(Key.File.ZONE);
                    Util.send(player, "&cRegeneration disabled for all zones !");
                    RegenSystemProvider.get().loadZones();
                } else {
                    String zone = args[1];
                    if (!config.contains("zones." + zone)) {
                        Util.send(player, "&cUnknow zone.");
                        return true;
                    }
                    config.set("zones." + zone + ".enabled", false);
                    RegenSystem.getInstance().getFileManager().save(Key.File.ZONE);
                    Util.send(player, "&cZone &4" + zone + " &cdisabled !");
                    RegenSystemProvider.get().loadZones();
                }
            }
            case "menu" -> {
                if (!player.hasPermission("regensystem.regensystem.menu")) {
                    Util.send(player, "&cYou don't have permission to use this command.");
                    return true;
                }

                RegenSystem.getInstance().getMenuManager().open(player);
            }
            default -> Util.send(player, "&cUnknown command.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            for (String sub : SUBCOMMANDS) {
                if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(sub);
                }
            }
            return result;
        }

        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            List<String> zones = new ArrayList<>(RegenSystemProvider.get().getZoneNames());

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
