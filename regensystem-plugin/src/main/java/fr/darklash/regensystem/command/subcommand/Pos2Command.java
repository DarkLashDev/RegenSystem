package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.listener.Selector;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class Pos2Command implements SubCommand {

    private final Selector selector;

    public Pos2Command(Selector selector) {
        this.selector = selector;
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
            Util.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        selector.getSelections().computeIfAbsent(player, p -> new Location[2])[1] = player.getLocation();
        Util.send(player, Key.Message.POSITION2_SET);
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
