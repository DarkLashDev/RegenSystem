package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.RegenSystemAPI;
import fr.darklash.regensystem.api.zone.RegenZoneFlag;
import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.manager.MessageManager;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Placeholders;
import fr.darklash.regensystem.util.ZoneService;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaveCommand implements SubCommand {

    private final Map<Player, Location[]> selections;
    private final ZoneService service;

    public SaveCommand(ZoneService service, Map<Player, Location[]> selections) {
        this.selections = selections;
        this.service = service;
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getPermission() {
        return "regensystem.save";
    }

    @Override
    public String getUsage() {
        return "/regen save <name> [delay] [f:flag=value]...";
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

        Location[] locsCommand = selections.get(player);
        Location[] locsHache = RegenSystem.getInstance().getSelectorListener().getSelections().get(player);

        Location pos1 = null;
        Location pos2 = null;

        if (locsCommand != null && locsCommand[0] != null) pos1 = locsCommand[0];
        else if (locsHache != null && locsHache[0] != null) pos1 = locsHache[0];

        if (locsCommand != null && locsCommand[1] != null) pos2 = locsCommand[1];
        else if (locsHache != null && locsHache[1] != null) pos2 = locsHache[1];

        if (pos1 == null || pos2 == null) {
            MessageManager.send(player, Key.Message.POSITIONS_NOT_SET);
            return true;
        }

        String zoneName = args[1];

        int delay = 60;
        int argIndex = 2;

        if (args.length > 2) {
            try {
                delay = Integer.parseInt(args[2]);
                if (delay <= 0) {
                    MessageManager.send(player, Key.Message.DELAY_MUST_BE_POSITIVE);
                    return true;
                }
                argIndex = 3;
            } catch (NumberFormatException ignored) {

            }
        }

        service.createZone(zoneName, pos1, pos2, delay);

        for (int i = argIndex; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("f:")) {
                String[] split = arg.substring(2).split("=");
                if (split.length == 2) {
                    RegenZoneFlag flag = RegenZoneFlag.fromString(split[0]);
                    if (flag == null) {
                        MessageManager.send(player, Key.Message.UNKNOWN_FLAG, Placeholders.of("flag", split[0]).asMap());
                        continue;
                    }

                    boolean value;
                    if (split[1].equalsIgnoreCase("true") || split[1].equalsIgnoreCase("on")) {
                        value = true;
                    } else if (split[1].equalsIgnoreCase("false") || split[1].equalsIgnoreCase("off")) {
                        value = false;
                    } else {
                        MessageManager.send(player, Key.Message.INVALID_FLAG_VALUE, Placeholders.of("flag", split[0]).asMap());
                        continue;
                    }

                    RegenSystemAPI.get().getZone(zoneName).setFlag(flag, value);
                    MessageManager.send(player, Key.Message.FLAG_SET, Placeholders.of("flag", flag.name()).add("value", value).asMap());
                }
            }
        }

        MessageManager.send(player, Key.Message.ZONE_SAVED_DELAY, Placeholders.of("name", zoneName).add("delay", delay).asMap());

        selections.remove(player);
        RegenSystem.getInstance().getSelectorListener().getSelections().remove(player);
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 2) {
            Collection<String> existing = RegenSystemAPI.get().getZoneNames();
            return getSuggestedZoneNames(args[1], existing);
        }

        if (args.length >= 3) {
            String lastArg = args[args.length - 1];

            if (lastArg.startsWith("f:") && !lastArg.contains("=")) {
                String prefix = lastArg.toLowerCase();
                return Arrays.stream(RegenZoneFlag.values())
                        .map(flag -> "f:" + flag.name() + "=")
                        .filter(s -> s.toLowerCase().startsWith(prefix))
                        .collect(Collectors.toList());
            }

            if (lastArg.startsWith("f:") && lastArg.contains("=")) {
                String prefix = lastArg.substring(0, lastArg.indexOf('=') + 1);
                String afterEqual = lastArg.substring(lastArg.indexOf('=') + 1).toLowerCase();

                return Stream.of("true", "false", "on", "off")
                        .filter(v -> v.startsWith(afterEqual))
                        .map(v -> prefix + v)
                        .collect(Collectors.toList());
            }

            if (!lastArg.startsWith("f:")) {
                return List.of("f:");
            }
        }

        return Collections.emptyList();
    }

    private List<String> getSuggestedZoneNames(String arg, Collection<String> existing) {
        String prefix = arg == null ? "" : arg.toLowerCase();
        Set<String> lowerExisting = existing.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String candidate = "zone" + i;
            if (!lowerExisting.contains(candidate) && candidate.startsWith(prefix)) {
                suggestions.add(candidate);
            }
        }
        return suggestions;
    }
}
