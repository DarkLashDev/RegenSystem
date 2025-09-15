package fr.darklash.regensystem.manager;

import fr.darklash.regensystem.RegenSystem;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private Connection connection;

    public void connect() {
        try {
            File dbFile = new File(RegenSystem.getInstance().getDataFolder(), "zone.db");
            if (!dbFile.exists()) {
                dbFile.getParentFile().mkdirs();
            }

            String url = "jdbc:sqlite:" + dbFile.getCanonicalPath();
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(true);

            initTables();

        } catch (IOException | SQLException e) {
            RegenSystem.getInstance().getLogger().severe("Failed to connect to SQLite database: " + e.getMessage());
            RegenSystem.getInstance().logException(e);
        }
    }

    private void initTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS zone_blocks (
                    zone_name TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    world TEXT NOT NULL,
                    block_data TEXT NOT NULL,
                    PRIMARY KEY (zone_name, x, y, z)
                )
            """);
        }
    }

    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                RegenSystem.getInstance().getLogger().warning("Database connection was closed, reconnecting...");
                connect();
            }
        } catch (SQLException e) {
            RegenSystem.getInstance().getLogger().severe("Failed to reopen database connection: " + e.getMessage());
            RegenSystem.getInstance().logException(e);
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                RegenSystem.getInstance().getLogger().info("Database connection closed.");
            }
        } catch (SQLException e) {
            RegenSystem.getInstance().getLogger().severe("Error while closing database: " + e.getMessage());
            RegenSystem.getInstance().logException(e);
        }
    }
}
