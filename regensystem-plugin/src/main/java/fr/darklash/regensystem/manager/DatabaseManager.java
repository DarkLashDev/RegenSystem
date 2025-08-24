package fr.darklash.regensystem.manager;

import fr.darklash.regensystem.RegenSystem;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private Connection connection;

    public void connect() throws SQLException {
        File dbFile = new File(RegenSystem.getInstance().getDataFolder(), "zone.db");
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);

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

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            RegenSystem.getInstance().getLogger().warning("Reopening closed database connection...");
            connect();
        }
        return connection;
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
