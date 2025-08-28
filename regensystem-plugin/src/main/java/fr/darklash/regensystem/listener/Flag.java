package fr.darklash.regensystem.listener;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.api.zone.RegenZone;
import fr.darklash.regensystem.manager.ZoneManager;
import fr.darklash.regensystem.util.Util;
import fr.darklash.regensystem.api.zone.RegenZoneFlag;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Flag implements Listener {

    private final ZoneManager zoneManager;

    public Flag() {
        this.zoneManager = RegenSystem.getInstance().getZoneManager();
    }

    private RegenZone getZoneAt(Location loc) {
        for (RegenZone zone : zoneManager.getZones()) {
            if (zone.contains(loc)) return zone;
        }
        return null;
    }

    private boolean disallowed(RegenZone zone, RegenZoneFlag flag) {
        return zone != null && !zone.hasFlag(flag);
    }

    // --- PVP & MOB_DAMAGE ---
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        if (!(victim instanceof Player playerVictim)) return;

        // Attaquant = joueur (ou projectile tiré par un joueur) -> PVP
        Player attackingPlayer = null;
        if (event.getDamager() instanceof Player p) {
            attackingPlayer = p;
        } else if (event.getDamager() instanceof Projectile proj && proj.getShooter() instanceof Player p) {
            attackingPlayer = p;
        }

        RegenZone zone = getZoneAt(playerVictim.getLocation());

        if (attackingPlayer != null) {
            if (disallowed(zone, RegenZoneFlag.PVP)) {
                event.setCancelled(true);
                Util.send(attackingPlayer, "&cPVP is disabled in this zone!");
            }
            return;
        }

        // Sinon, c'est un mob (ou autre entité non-joueur) -> MOB_DAMAGE
        if (disallowed(zone, RegenZoneFlag.MOB_DAMAGE)) {
            event.setCancelled(true);
            // message au joueur touché (optionnel pour éviter le spam)
            Util.send(playerVictim, "&aMob damage is disabled in this zone.");
        }
    }

    // --- Block Break ---
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        RegenZone zone = getZoneAt(event.getBlock().getLocation());
        // BUILD agit comme master switch + cas spécifique BLOCK_BREAK
        if (disallowed(zone, RegenZoneFlag.BLOCK_BREAK)) {
            event.setCancelled(true);
            Util.send(event.getPlayer(), "&cYou cannot break blocks in this zone!");
        }
    }

    // --- Block Place ---
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        RegenZone zone = getZoneAt(event.getBlock().getLocation());
        // BUILD agit comme master switch + cas spécifique BLOCK_PLACE
        if (disallowed(zone, RegenZoneFlag.BLOCK_PLACE)) {
            event.setCancelled(true);
            Util.send(event.getPlayer(), "&cYou cannot place blocks in this zone!");
        }
    }

    // --- Item Drop ---
    @EventHandler(ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        RegenZone zone = getZoneAt(event.getPlayer().getLocation());
        if (disallowed(zone, RegenZoneFlag.ITEM_DROP)) {
            event.setCancelled(true);
            Util.send(event.getPlayer(), "&cYou cannot drop items in this zone!");
        }
    }

    // --- Item Pickup ---
    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        RegenZone zone = getZoneAt(player.getLocation());
        if (zone != null && !zone.hasFlag(RegenZoneFlag.ITEM_PICKUP)) {
            event.setCancelled(true);
            Util.send(player, "&cYou cannot pick up items in this zone!");
        }
    }

    // --- Mob Spawn ---
    @EventHandler(ignoreCancelled = true)
    public void onMobSpawn(EntitySpawnEvent event) {
        RegenZone zone = getZoneAt(event.getLocation());
        if (disallowed(zone, RegenZoneFlag.MOB_SPAWN)) {
            event.setCancelled(true);
        }
    }

    // --- Explosion ---
    @EventHandler(ignoreCancelled = true)
    public void onExplosion(EntityExplodeEvent event) {
        RegenZone zone = getZoneAt(event.getLocation());
        if (disallowed(zone, RegenZoneFlag.EXPLOSION)) {
            // Empêche la destruction de blocs (on peut choisir de cancel l'event si on veut tout bloquer)
            event.blockList().clear();
        }
    }
}
