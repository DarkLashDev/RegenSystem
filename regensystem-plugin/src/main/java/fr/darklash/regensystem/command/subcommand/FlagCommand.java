package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.api.zone.Zone;
import fr.darklash.regensystem.api.zone.ZoneFlag;
import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.placeholder.Placeholders;
import fr.darklash.regensystem.util.Util;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
            Util.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        if (args.length == 1) {
            Util.send(player, Key.Message.FLAG_LIST_HEADER);
            for (ZoneFlag flag : ZoneFlag.values()) {
                Util.sendPrefixed(player, Util.legacy(" &e" + flag.name() + " &7- &f" + flag.getDescription()));
            }
            return true;
        }

        String zoneName = args[1];
        Zone zone = RegenSystemAPI.getZones().getZone(zoneName).orElse(null);

        if (zone == null) {
            Util.send(player, Key.Message.ZONE_NOT_FOUND, Placeholders.of("name", zoneName).asMap());
            return true;
        }

        if (args.length == 2) {
            // Affiche tous les flags de la zone avec leur état et description
            Util.send(player, Key.Message.FLAGS_FOR_ZONE, Placeholders.of("zone", zoneName).asMap());
            for (ZoneFlag flag : ZoneFlag.values()) {
                boolean value = false;
                value = zone.hasFlag(flag);
                String status = value ? "&a✅" : "&c❌";
                Util.sendPrefixed(player, Util.legacy(" &e" + flag.name() + " &7- " + status + " &f" + flag.getDescription()));
            }
            return true;
        }

        if (args.length < 4) {
            Util.send(player, Key.Message.USAGE, Placeholders.of("usage", getUsage()).asMap());
            return true;
        }

        ZoneFlag flag = ZoneFlag.fromString(args[2]);
        if (flag == null) {
            Util.send(player, Key.Message.UNKNOWN_FLAG, Placeholders.of("flag", args[2]).asMap());
            return true;
        }

        boolean value;
        if (args[3].equalsIgnoreCase("on") || args[3].equalsIgnoreCase("true")) value = true;
        else if (args[3].equalsIgnoreCase("off") || args[3].equalsIgnoreCase("false")) value = false;
        else {
            Util.send(player, Key.Message.INVALID_FLAG_VALUE, Placeholders.of("flag", flag.name()).asMap());
            return true;
        }

        zone.setFlag(flag, value);
        Util.send(player, Key.Message.FLAG_SET, Placeholders.of("flag", flag.name()).add("zone", zoneName).add("value", String.valueOf(value)).asMap());
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {

        if (args.length == 2) {
            return RegenSystemAPI.getZones().getZoneNames().stream()
                    .filter(z -> z.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        if (args.length == 3) {
            return Arrays.stream(ZoneFlag.values())
                    .map(Enum::name)
                    .filter(f -> f.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        }

        if (args.length == 4) {
            return Stream.of("on", "off")
                    .filter(v -> v.startsWith(args[3].toLowerCase()))
                    .toList();
        }

        return List.of();
    }
}
