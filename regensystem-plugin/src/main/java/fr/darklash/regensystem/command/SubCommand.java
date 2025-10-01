package fr.darklash.regensystem.command;

import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommand {

    String getName();
    String getPermission();
    String getUsage();
    boolean execute(Player player, String[] args);
    List<String> tabComplete(Player player, String[] args);
}
