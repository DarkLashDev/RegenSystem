package fr.darklash.regensystem.manager;

import fr.darklash.regensystem.util.Key;
import fr.darklash.regensystem.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MenuManager {

    private final Inventory menu;
    private static final Component TITLE = Util.legacy("RegenSystem");

    public MenuManager() {
        menu = Bukkit.createInventory(null, 27, TITLE);
        initMenuItems();
    }

    private void initMenuItems() {
        menu.clear();

        ItemStack glassPane = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPane.getItemMeta();
        if (glassMeta != null) {
            glassMeta.displayName(Component.empty());
            glassMeta.lore(List.of());
            glassPane.setItemMeta(glassMeta);
        }
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, glassPane);
        }

        menu.setItem(10, createItem(Material.RED_WOOL, "&cSet Position 1", List.of("&7Set first corner of the zone")));
        menu.setItem(11, createItem(Material.GREEN_WOOL, "&aSet Position 2", List.of("&7Set second corner of the zone")));
        menu.setItem(12, createItem(Material.PAPER, "&eReload Zones", List.of("&7Reload all zones configuration")));
        menu.setItem(14, createItem(Material.DIAMOND_AXE, "&bGet Selection Wand", List.of("&7Gives you the zone selection axe")));
        menu.setItem(15, createItem(Material.LIME_CONCRETE, "&aEnable All Zones", List.of("&7Enable regeneration on all zones")));
        menu.setItem(16, createItem(Material.RED_CONCRETE, "&cDisable All Zones", List.of("&7Disable regeneration on all zones")));
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Util.legacy(name));
            meta.lore(Util.legacy(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    public void open(Player player) {
        player.openInventory(menu);
    }

    public void handleClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!event.getView().title().equals(TITLE)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        Player player = (Player) event.getWhoClicked();
        Material type = clicked.getType();

        switch (type) {
            case RED_WOOL -> {
                player.performCommand("regen pos1");
                player.closeInventory();
            }
            case GREEN_WOOL -> {
                player.performCommand("regen pos2");
                player.closeInventory();
            }
            case PAPER -> {
                player.performCommand("regen reload");
                player.closeInventory();
            }
            case DIAMOND_AXE -> {
                player.performCommand("regen wand");
                player.closeInventory();
            }
            case LIME_CONCRETE -> {
                player.performCommand("regen enable all");
                player.closeInventory();
            }
            case RED_CONCRETE -> {
                player.performCommand("regen disable all");
                player.closeInventory();
            }
            case YELLOW_STAINED_GLASS_PANE -> {

            }
            default -> MessageManager.send(player, Key.Message.UNKNOWN_ACTION);
        }
    }
}
