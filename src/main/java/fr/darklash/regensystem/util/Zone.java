package fr.darklash.regensystem.util;

import fr.darklash.regensystem.RegenSystem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Zone {

    @Getter
    private final String name;
    private final Location corner1;
    private final Location corner2;
    private final Map<String, BlockData> originalBlocks = new HashMap<>();

    public Zone(String name, Location corner1, Location corner2) {
        this.name = name;
        this.corner1 = corner1;
        this.corner2 = corner2;
        captureState();
    }

    public void captureState() {
        originalBlocks.clear();
        for (int x = Math.min(corner1.getBlockX(), corner2.getBlockX()); x <= Math.max(corner1.getBlockX(), corner2.getBlockX()); x++) {
            for (int y = Math.min(corner1.getBlockY(), corner2.getBlockY()); y <= Math.max(corner1.getBlockY(), corner2.getBlockY()); y++) {
                for (int z = Math.min(corner1.getBlockZ(), corner2.getBlockZ()); z <= Math.max(corner1.getBlockZ(), corner2.getBlockZ()); z++) {
                    Location loc = new Location(corner1.getWorld(), x, y, z).toBlockLocation();
                    loc.getWorld().getChunkAt(loc).load(); // facultatif, mais utile
                    String key = x + "," + y + "," + z;
                    originalBlocks.put(key, loc.getBlock().getBlockData().clone());
                }
            }
        }
    }

    public void regenerate() {
        World world = corner1.getWorld();
        if (world == null) return;

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        // Étape 1 : expulser les joueurs
        world.getPlayers().stream()
                .filter(player -> {
                    Location loc = player.getLocation();
                    return loc.getWorld().equals(world)
                            && loc.getX() >= minX && loc.getX() <= maxX
                            && loc.getY() >= minY && loc.getY() <= maxY
                            && loc.getZ() >= minZ && loc.getZ() <= maxZ;
                })
                .forEach(player -> {
                    Location safeLoc = findSafeLocationNearZone(world, minX, maxX, minY, maxY, minZ, maxZ);
                    if (safeLoc != null) {
                        player.teleport(safeLoc);
                        Util.send(player, "&eYou've been moved out of a regenerating zone.");
                    } else {
                        Util.send(player, "&cNo safe position found outside the zone !");
                    }
                });

        // Chargement des chunks nécessaires (une seule fois par chunk)
        int chunkMinX = minX >> 4;
        int chunkMaxX = maxX >> 4;
        int chunkMinZ = minZ >> 4;
        int chunkMaxZ = maxZ >> 4;

        for (int cx = chunkMinX; cx <= chunkMaxX; cx++) {
            for (int cz = chunkMinZ; cz <= chunkMaxZ; cz++) {
                if (!world.getChunkAt(cx, cz).isLoaded()) {
                    world.getChunkAt(cx, cz).load();
                }
            }
        }

        int changedBlocks = 0;

        for (Map.Entry<String, BlockData> entry : originalBlocks.entrySet()) {
            String[] parts = entry.getKey().split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            Location loc = new Location(world, x, y, z);

            BlockData originalBlockData = entry.getValue();
            BlockData currentBlockData = loc.getBlock().getBlockData();

            // Ne pas toucher si le bloc est déjà à l'état voulu
            if (currentBlockData.equals(originalBlockData)) continue;

            // Ne pas remplacer un bloc d'air (tu peux changer la condition si besoin)
            if (originalBlockData.getMaterial().isAir()) continue;

            loc.getBlock().setBlockData(originalBlockData, false);
            changedBlocks++;
        }
    }

    public void load() {
        originalBlocks.clear();
        try (Connection conn = RegenSystem.getInstance().getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT x, y, z, world, block_data FROM zone_blocks WHERE zone_name = ?")) {
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int x = rs.getInt("x");
                    int y = rs.getInt("y");
                    int z = rs.getInt("z");
                    String worldName = rs.getString("world");
                    String blockDataStr = rs.getString("block_data");

                    World world = Bukkit.getWorld(worldName);
                    if (world == null) continue;

                    Location loc = new Location(world, x, y, z);
                    BlockData blockData = Bukkit.createBlockData(blockDataStr);

                    String key = x + "," + y + "," + z;
                    originalBlocks.put(key, blockData);
                }
            }
        } catch (SQLException e) {
            RegenSystem.getInstance().getLogger().severe("❌ Unable to connect to SQLite database!");
            RegenSystem.getInstance().logException(e);
        }
    }

    public void save() {
        try (Connection conn = RegenSystem.getInstance().getDatabaseManager().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM zone_blocks WHERE zone_name = ?")) {
                deleteStmt.setString(1, name);
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO zone_blocks(zone_name, x, y, z, world, block_data) VALUES (?, ?, ?, ?, ?, ?)")) {

                for (Map.Entry<String, BlockData> entry : originalBlocks.entrySet()) {
                    String[] coords = entry.getKey().split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    int z = Integer.parseInt(coords[2]);
                    BlockData blockData = entry.getValue();

                    insertStmt.setString(1, name);
                    insertStmt.setInt(2, x);
                    insertStmt.setInt(3, y);
                    insertStmt.setInt(4, z);
                    insertStmt.setString(5, corner1.getWorld().getName()); // Supposé même monde
                    insertStmt.setString(6, blockData.getAsString());

                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            RegenSystem.getInstance().getLogger().severe("❌ Unable to connect to SQLite database!");
            RegenSystem.getInstance().logException(e);
        }
    }

    public boolean isEnabled() {
        FileConfiguration config = RegenSystem.getInstance().getFileManager().get(Key.File.ZONE);
        return config.getBoolean("zones." + name + ".enabled", true);
    }

    private Location findSafeLocationNearZone(World world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        int radius = 5; // Distance autour de la zone pour chercher
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                // Éloigner du centre
                int x = (dx < 0) ? minX + dx - 1 : maxX + dx + 1;
                int z = (dz < 0) ? minZ + dz - 1 : maxZ + dz + 1;

                // Scanner de haut en bas pour trouver un sol
                for (int y = maxY + 3; y >= minY - 5; y--) {
                    Location base = new Location(world, x, y, z);
                    Location feet = base.clone();
                    Location head = base.clone().add(0, 1, 0);
                    Location below = base.clone().add(0, -1, 0);

                    if (below.getBlock().getType().isSolid()
                            && feet.getBlock().getType().isAir()
                            && head.getBlock().getType().isAir()) {
                        return base.add(0.5, 0, 0.5); // Centrer sur le bloc
                    }
                }
            }
        }
        return null; // Aucun endroit sûr trouvé
    }

    public Location getCenter() {
        return corner1.clone().add(corner2).multiply(0.5);
    }
}
