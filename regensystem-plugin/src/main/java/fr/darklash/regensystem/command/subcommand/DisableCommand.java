package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.manager.MessageManager;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Placeholders;
import fr.darklash.regensystem.util.ZoneService;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DisableCommand implements SubCommand {

    private final ZoneService service;

    public DisableCommand(ZoneService service) {
        this.service = service;
    }

    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public String getPermission() {
        return "regensystem.toggle";
    }

    @Override
    public String getUsage() {
        return "/regen disable <all|zone_name>";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            MessageManager.send(player, Key.Message.USAGE, Placeholders.of("usage", getUsage()).asMap());
            return true;
        }

        if (!player.hasPermission(getPermission())) {
            MessageManager.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);

        if (args[1].equalsIgnoreCase("all")) {
            service.disableAllZones();
            MessageManager.send(player, Key.Message.ALL_ZONES_DISABLED);
        } else {
            String zone = args[1];
            if (!config.contains("zones." + zone)) {
                MessageManager.send(player, Key.Message.ZONE_NOT_FOUND, Placeholders.of("name", zone).asMap());
                return true;
            }
            service.disableZone(zone);
            MessageManager.send(player, Key.Message.ZONE_DISABLED, Placeholders.of("name", zone).asMap());
        }
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 2) {
            String input = args[1].toLowerCase();
            Set<String> zones = RegenSystemAPI.get().getZoneNames();
            List<String> result = new ArrayList<>();

            if ("all".startsWith(input)) result.add("all");
            for (String zone : zones) {
                if (zone.toLowerCase().startsWith(input)) {
                    result.add(zone);
                }
            }
            return result;
        }
        return Collections.emptyList();
    }
}
