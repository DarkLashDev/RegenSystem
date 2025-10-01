package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.manager.MessageManager;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Placeholders;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ReloadCommand implements SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "regensystem.reload";
    }

    @Override
    public String getUsage() {
        return "/regen reload [zone]";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            MessageManager.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        if (args.length >= 2) {
            String zoneName = args[1];
            if (!RegenSystem.getInstance().getFileManager().get(Key.File.ZONE).contains("zones." + zoneName)) {
                MessageManager.send(player, Key.Message.ZONE_NOT_FOUND, Placeholders.of("name", zoneName).asMap());
                return true;
            }
            RegenSystemAPI.get().reloadZone(zoneName);
            MessageManager.send(player, Key.Message.ZONE_RELOADED, Placeholders.of("name", zoneName).asMap());
        } else {
            RegenSystemAPI.get().loadZones();
            MessageManager.send(player, Key.Message.ALL_ZONES_RELOADED);
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
