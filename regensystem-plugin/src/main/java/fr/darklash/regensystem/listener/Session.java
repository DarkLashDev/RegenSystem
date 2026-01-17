package fr.darklash.regensystem.listener;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.util.Util;
import fr.darklash.regensystem.util.scheduler.RegenScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Session implements Listener {

    private final RegenScheduler scheduler = RegenSystem.getInstance().getScheduler();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String latest = RegenSystem.getInstance().getLatestVersionString();
        String current = RegenSystem.getInstance().getDescription().getVersion();

        if (latest == null || RegenSystem.getInstance().compareVersions(latest, current) <= 0) return;

        if (player.isOp() || player.hasPermission("regensystem.update")) {
            scheduler.runLater(40L, () -> {
                Util.send(
                        player,
                        "&eA new version is available : &a" + latest +
                                "&e (you are using &c" + current + "&e)."
                );

                Component clickable =
                        Component.text("→ Click here to download : ")
                                .color(net.kyori.adventure.text.format.NamedTextColor.GRAY)
                                .append(
                                        Component.text("https://modrinth.com/plugin/regensystem")
                                                .color(net.kyori.adventure.text.format.NamedTextColor.AQUA)
                                                .clickEvent(ClickEvent.openUrl(
                                                        "https://modrinth.com/plugin/regensystem"))
                                                .hoverEvent(HoverEvent.showText(
                                                        Util.legacy("&7Click to open Modrinth in your browser")))
                                );

                Util.sendPrefixed(player, clickable);
            });
        }
    }
}
