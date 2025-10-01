package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.api.zone.RegenZoneFlag;
import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.manager.MessageManager;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Placeholders;
import fr.darklash.regensystem.util.Util;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class FlagCommand implements SubCommand {

    @Override
    public String getName() {
        return "flag";
    }

    @Override
    public String getPermission() {
        return "regensystem.flag";
    }

    @Override
    public String getUsage() {
        return "/regen flag <zone> <flag> <on|off>";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            MessageManager.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        if (args.length == 1) {
            MessageManager.send(player, Key.Message.FLAG_LIST_HEADER);
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
            MessageManager.send(player, Key.Message.ZONE_NOT_FOUND, Placeholders.of("name", zoneName).asMap());
            return true;
        }

        if (args.length == 2) {
            // Affiche tous les flags de la zone avec leur état et description
            MessageManager.send(player, Key.Message.FLAGS_FOR_ZONE, Placeholders.of("zone", zoneName).asMap());
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
            MessageManager.send(player, Key.Message.USAGE, Placeholders.of("usage", getUsage()).asMap());
            return true;
        }

        RegenZoneFlag flag = RegenZoneFlag.fromString(args[2]);
        if (flag == null) {
            MessageManager.send(player, Key.Message.UNKNOWN_FLAG, Placeholders.of("flag", args[2]).asMap());
            return true;
        }

        boolean value;
        if (args[3].equalsIgnoreCase("on") || args[3].equalsIgnoreCase("true")) value = true;
        else if (args[3].equalsIgnoreCase("off") || args[3].equalsIgnoreCase("false")) value = false;
        else {
            MessageManager.send(player, Key.Message.INVALID_FLAG_VALUE, Placeholders.of("flag", flag.name()).asMap());
            return true;
        }

        zone.setFlag(flag, value);
        MessageManager.send(player, Key.Message.FLAG_SET, Placeholders.of("flag", flag.name()).add("zone", zoneName).add("value", String.valueOf(value)).asMap());
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 2) {
            return RegenSystemAPI.get().getZoneNames().stream()
                    .filter(z -> z.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        if (args.length == 3) {
            return Arrays.stream(RegenZoneFlag.values())
                    .map(Enum::name)
                    .filter(f -> f.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        }

        if (args.length == 4) {
            return List.of("on", "off").stream()
                    .filter(v -> v.startsWith(args[3].toLowerCase()))
                    .toList();
        }

        return List.of();
    }
}
