package fr.darklash.regensystem.command.subcommand;

import fr.darklash.regensystem.command.SubCommand;
import fr.darklash.regensystem.platform.PlatformHelper;
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
            Util.send(player, Key.Message.NO_PERMISSION);
            return true;
        }

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta meta = axe.getItemMeta();
        if (meta != null) {
            PlatformHelper.setDisplayName(meta, Util.legacy("&2Zone selection axe"));
            PlatformHelper.setLore(meta, Util.legacy(List.of("&eLeft-click = Pos1", "&eRight-click = Pos2")));
            meta.setCustomModelData(44);
            axe.setItemMeta(meta);
        }
        player.getInventory().addItem(axe);
        Util.send(player, Key.Message.WAND_RECEIVED);
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
