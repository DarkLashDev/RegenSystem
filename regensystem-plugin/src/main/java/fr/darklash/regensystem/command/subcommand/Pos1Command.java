package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.listener.Selector;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Pos1Command implements SubCommand {

    private final Selector selector;

    public Pos1Command(Selector selector) {
        this.selector = selector;
    }

    @Override
    public String getName() {
        return "pos1";
    }

    @Override
    public String getPermission() {
        return "regensystem.pos";
    }

    @Override
    public String getUsage() {
        return "/regen pos1";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            Util.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        selector.getSelections().computeIfAbsent(player, p -> new Location[2])[0] = player.getLocation();
        Util.send(player, Key.Message.POSITION1_SET);
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }
}
