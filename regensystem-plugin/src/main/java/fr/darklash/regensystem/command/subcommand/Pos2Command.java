package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.manager.MessageManager;
import fr.darklash.regensystem.util.Key;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class Pos2Command implements SubCommand {

    private final Map<Player, Location[]> selections;

    public Pos2Command(Map<Player, Location[]> selections) {
        this.selections = selections;
    }

    @Override
    public String getName() {
        return "pos2";
    }

    @Override
    public String getPermission() {
        return "regensystem.pos";
    }

    @Override
    public String getUsage() {
        return "/regen pos2";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            MessageManager.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        selections.computeIfAbsent(player, p -> new Location[2])[1] = player.getLocation();
        MessageManager.send(player, Key.Message.POSITION2_SET);
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
