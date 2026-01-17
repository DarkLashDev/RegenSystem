package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.manager.MessageManager;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Placeholders;
import fr.darklash.regensystem.util.Util;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand implements SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/regen help [page]";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        int page = 1;
        if (args.length >= 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                MessageManager.send(player, Key.Message.USAGE, Placeholders.of("usage", getUsage()).asMap());
                return true;
            }
        }

        Key.Message key;
        switch (page) {
            case 1 -> key = Key.Message.HELP_PAGE_1;
            case 2 -> key = Key.Message.HELP_PAGE_2;
            case 3 -> key = Key.Message.HELP_PAGE_3;
            default -> {
                Util.send(player, "&cInvalid page. &7(1 - 3)");
                return true;
            }
        }

        String[] lines = MessageManager.getRaw(key, null).split("\n");
        for (String line : lines) {
            Util.sendPrefixed(player, Util.legacy(line));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
