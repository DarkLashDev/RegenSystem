package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.placeholder.Placeholders;
import fr.darklash.regensystem.util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LangCommand implements SubCommand {

    @Override
    public String getName() {
        return "lang";
    }

    @Override
    public String getPermission() {
        return "regensystem.lang";
    }

    @Override
    public String getUsage() {
        return "/regen lang <language>";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            Util.send(player, Key.Message.USAGE, Placeholders.of("usage", getUsage()).asMap());
            return true;
        }

        String input = args[1].toUpperCase();

        try {
            Key.Lang lang = Key.Lang.valueOf(input);
            Util.setLang(lang);

            Util.send(player, Key.Message.LANGUAGE_SET, Placeholders.of("lang", lang.name()).asMap());
        } catch (IllegalArgumentException e) {
            Util.send(player, Key.Message.UNKNOWN_LANGUAGE);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 2) {
            String input = args[1].toUpperCase();
            List<String> result = new ArrayList<>();

            for (Key.Lang lang : Key.Lang.values()) {
                if (lang.name().startsWith(input)) {
                    result.add(lang.name());
                }
            }
            return result;
        }
        return Collections.emptyList();
    }
}
