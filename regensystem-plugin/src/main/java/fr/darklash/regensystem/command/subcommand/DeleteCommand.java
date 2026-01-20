package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.placeholder.Placeholders;
import fr.darklash.regensystem.internal.zone.ZoneAdminService;
import fr.darklash.regensystem.util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DeleteCommand implements SubCommand {

    private final ZoneAdminService service;

    public DeleteCommand(ZoneAdminService service) {
        this.service = service;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getPermission() {
        return "regensystem.delete";
    }

    @Override
    public String getUsage() {
        return "/regen delete <name>";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            Util.send(player, Key.Message.USAGE, Placeholders.of("usage", getUsage()).asMap());
            return true;
        }

        if (!player.hasPermission(getPermission())) {
            Util.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        String zoneName = args[1];

        if (RegenSystem.getInstance().getFileManager().get(Key.File.ZONE).contains("zones." + zoneName)) {
            service.deleteZone(zoneName);
            Util.send(player, Key.Message.ZONE_DELETED, Placeholders.of("name", zoneName).asMap());
        } else {
            Util.send(player, Key.Message.ZONE_NOT_FOUND, Placeholders.of("name", zoneName).asMap());
        }
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 2) {
            String input = args[1].toLowerCase();
            Set<String> zones = RegenSystemAPI.getZones().getZoneNames();
            List<String> result = new ArrayList<>();

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
