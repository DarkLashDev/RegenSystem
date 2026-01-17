package fr.darklash.regensystem.listener;

import fr.darklash.regensystem.util.Util;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Selector implements Listener {

    private final Map<Player, Location[]> selections = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getHand() != EquipmentSlot.HAND) return;

        if (item.getType() != Material.DIAMOND_AXE) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasCustomModelData()) return;

        if (meta.getCustomModelData() != 44) return;

        if (!event.hasBlock()) return;

        event.setCancelled(true);

        if (event.getClickedBlock() != null) {
            Location clickedBlockLocation = event.getClickedBlock().getLocation();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                selections.computeIfAbsent(player, p -> new Location[2])[1] = clickedBlockLocation;
                Util.send(player, "&2Position 2 defined at " + formatLocation(clickedBlockLocation));
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                selections.computeIfAbsent(player, p -> new Location[2])[0] = clickedBlockLocation;
                Util.send(player, "&2Position 1 defined at " + formatLocation(clickedBlockLocation));
            }
        }
    }

    private String formatLocation(Location loc) {
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }
}
