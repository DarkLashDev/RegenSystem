package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Util;
import org.bukkit.entity.Player;

import java.util.List;

public class MenuCommand implements SubCommand {

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getPermission() {
        return "regensystem.menu";
    }

    @Override
    public String getUsage() {
        return "/regen menu";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            Util.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        RegenSystem.getInstance().getMenuManager().open(player);
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
