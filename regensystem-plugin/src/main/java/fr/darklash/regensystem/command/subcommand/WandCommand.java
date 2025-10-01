package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.manager.MessageManager;
import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class WandCommand implements SubCommand {

    @Override
    public String getName() {
        return "wand";
    }

    @Override
    public String getPermission() {
        return "regensystem.wand";
    }

    @Override
    public String getUsage() {
        return "/regen wand";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission(getPermission())) {
            MessageManager.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta meta = axe.getItemMeta();
        if (meta != null) {
            meta.displayName(Util.legacy("&2Zone selection axe"));
            meta.lore(Util.legacy(List.of("&eLeft-click = Pos1", "&eRight-click = Pos2")));
            meta.setCustomModelData(44);
            axe.setItemMeta(meta);
        }
        player.getInventory().addItem(axe);
        MessageManager.send(player, Key.Message.WAND_RECEIVED);
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
