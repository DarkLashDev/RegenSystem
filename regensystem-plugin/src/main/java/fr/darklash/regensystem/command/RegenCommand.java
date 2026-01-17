package fr.darklash.regensystem.command;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.command.subcommand.*;
import fr.darklash.regensystem.listener.Selector;
import fr.darklash.regensystem.manager.MessageManager;
import fr.darklash.regensystem.util.*;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RegenCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public RegenCommand() {
        ZoneService service = new ZoneService(RegenSystem.getInstance());

        Selector selector = RegenSystem.getInstance().getSelectorListener();

        register(new Pos1Command(selector));
        register(new Pos2Command(selector));
        register(new SaveCommand(selector));
        register(new ReloadCommand());
        register(new DeleteCommand(service));
        register(new SnapshotCommand(service));
        register(new WandCommand());
        register(new EnableCommand(service));
        register(new DisableCommand(service));
        register(new MenuCommand());
        register(new HelpCommand());
        register(new FlagCommand());
        register(new LangCommand());
    }

    private void register(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageManager.send((Player) sender, Key.Message.ONLY_PLAYERS);
            return true;
        }

        if (args.length == 0) {
            MessageManager.send(player, Key.Message.REGEN_CMD_USAGE);
            return true;
        }

        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub == null) {
            MessageManager.send(player, Key.Message.UNKNOWN_SUBCOMMAND);
            return true;
        }

        return sub.execute(player, args);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
        if (!(sender instanceof Player player)) return Collections.emptyList();

        if (args.length == 1) {
            return subCommands.keySet().stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub != null) {
            return sub.tabComplete(player, args);
        }

        return Collections.emptyList();
    }
}
