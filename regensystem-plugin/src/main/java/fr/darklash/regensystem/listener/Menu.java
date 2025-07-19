package fr.darklash.regensystem.listener;

import fr.darklash.regensystem.manager.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Menu implements Listener {

    private final MenuManager regenMenu;

    public Menu(MenuManager regenMenu) {
        this.regenMenu = regenMenu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        regenMenu.handleClick(event);
    }
}
