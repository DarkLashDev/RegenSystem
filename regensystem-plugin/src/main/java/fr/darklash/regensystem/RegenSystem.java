package fr.darklash.regensystem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.darklash.regensystem.api.RegenSystemProvider;
import fr.darklash.regensystem.command.Regen;
import fr.darklash.regensystem.listener.Menu;
import fr.darklash.regensystem.listener.Session;
import fr.darklash.regensystem.listener.ZoneSelector;
import fr.darklash.regensystem.manager.DatabaseManager;
import fr.darklash.regensystem.manager.FileManager;
import fr.darklash.regensystem.manager.MenuManager;
import fr.darklash.regensystem.manager.ZoneManager;
import fr.darklash.regensystem.util.RegenPlaceholder;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
public final class RegenSystem extends JavaPlugin {

    @Getter
    private static RegenSystem instance;

    private FileManager fileManager;
    private ZoneManager zoneManager;
    private MenuManager menuManager;
    private DatabaseManager databaseManager;
    private ZoneSelector zoneSelectorListener;
    private Menu menu;
    private RegenPlaceholder regenPlaceholder;

    private String latestVersionString = null;
    private Instant lastChecked = null;
    private final File versionCacheFile = new File(getDataFolder(), "version.cache");

    @Override
    public void onEnable() {
        instance = this;

        initManagers();
        connectDatabase();
        zoneManager.loadZones();
        registerCommands();
        registerListeners(getServer().getPluginManager());
        registerPlaceholders();

        RegenSystemProvider.set(this.zoneManager);

        checkUpdate();

        int pluginID = 26569;
        Metrics metrics = new Metrics(this, pluginID);

        getLogger().info("  _   __ ");
        getLogger().info(" |_) (_  " + getDescription().getName() + " v" + getDescription().getVersion());
        getLogger().info(" | \\ __) Running on Bukkit - " + getServer().getName());
        getLogger().info("         ");

        getLogger().info("✅ RegenSystem activated.");
    }

    @Override
    public void onDisable() {
        disconnectDatabase();
        getLogger().info("⛔ RegenSystem deactivated.");
    }

    private void initManagers() {
        this.fileManager = new FileManager(this);
        this.zoneManager = new ZoneManager();
        this.menuManager = new MenuManager();
        this.databaseManager = new DatabaseManager();
    }

    private void connectDatabase() {
        try {
            this.databaseManager.connect();
        } catch (SQLException e) {
            getLogger().severe("❌ Unable to connect to SQLite database!");
            logException(e);
        }
    }

    private void disconnectDatabase() {
        try {
            if (databaseManager != null) {
                databaseManager.disconnect();
            }
        } catch (SQLException e) {
            getLogger().severe("❌ Error while disconnecting database!");
            logException(e);
        }
    }

    private void registerCommands() {
        Map<String, CommandExecutor> commands = Map.of(
                "regen", new Regen()
        );

        commands.forEach((name, executor) -> {
            PluginCommand command = getCommand(name);
            if (command != null) {
                command.setExecutor(executor);
                command.setTabCompleter((TabCompleter) executor);
            } else {
                throw new IllegalStateException("❌ Command '" + name + "' is not registered in plugin.yml!");
            }
        });
    }

    private void registerListeners(PluginManager pm) {
        this.zoneSelectorListener = new ZoneSelector();
        this.menu = new Menu(menuManager);

        List<Listener> events = List.of(
                zoneSelectorListener,
                menu,
                new Session()
        );

        events.forEach(listener -> pm.registerEvents(listener, this));
    }

    private void checkUpdate() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                if (loadCache()) {
                    getLogger().info("✅ Cached version check: " + latestVersionString);
                    return;
                }

                String projectId = "regensystem"; // Slug Modrinth
                String url = "https://api.modrinth.com/v2/project/" + projectId + "/version";

                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestProperty("User-Agent", "RegenSystem/" + getDescription().getVersion());
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    getLogger().warning("⚠ Unable to check for updates (HTTP " + responseCode + ")");
                    return;
                }

                JsonArray versions = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonArray();
                if (versions.isEmpty()) {
                    getLogger().warning("⚠ No versions found on Modrinth.");
                    return;
                }

                JsonObject latestVersion = getJsonObject(versions);
                if (latestVersion == null) {
                    getLogger().warning("⚠ No release versions found on Modrinth.");
                    return;
                }

                String latest = latestVersion.get("version_number").getAsString();
                String current = getDescription().getVersion();
                this.latestVersionString = latest;
                this.lastChecked = Instant.now();
                saveCache(); // Sauvegarder pour la prochaine fois

                if (compareVersions(latest, current) > 0) {
                    getLogger().warning("⚠ A new version of RegenSystem is available : " + latest + " (you are on " + current + ")");
                    if (latestVersion.has("changelog")) {
                        String changelog = latestVersion.get("changelog").getAsString();
                        getLogger().info("📄 Changelog for " + latest + ":\n" + changelog);
                    }
                } else {
                    getLogger().info("✅ RegenSystem is up to date.");
                }

            } catch (Exception e) {
                getLogger().warning("⚠ Failed to check for updates: " + e.getMessage());
                logException(e);
            }
        });
    }

    private static @Nullable JsonObject getJsonObject(JsonArray versions) {
        JsonObject latestVersion = null;
        Instant latestDate = Instant.EPOCH;

        for (JsonElement element : versions) {
            JsonObject version = element.getAsJsonObject();

            // Ne garder que les releases
            if (!version.has("version_type") ||
                    !"release".equalsIgnoreCase(version.get("version_type").getAsString())) {
                continue;
            }

            Instant publishedDate = Instant.parse(version.get("date_published").getAsString());

            if (publishedDate.isAfter(latestDate)) {
                latestDate = publishedDate;
                latestVersion = version;
            }
        }
        return latestVersion;
    }

    private void registerPlaceholders() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.regenPlaceholder = new RegenPlaceholder(this);
            regenPlaceholder.register();
            getLogger().info("✅ PlaceholderAPI hook registered.");
        } else {
            getLogger().warning("⚠ PlaceholderAPI not found. Placeholders won't work.");
        }
    }

    public void logException(Throwable t) {
        getLogger().warning("⚠ Exception: " + t.getClass().getSimpleName() + ": " + t.getMessage());
        if (isDebug()) {
            for (StackTraceElement trace : t.getStackTrace()) {
                getLogger().warning("  at " + trace);
            }
        }
    }

    public boolean isPlaceholderEnabled() {
        return regenPlaceholder != null;
    }

    public boolean isDebug() {
        return getConfig().getBoolean("debug", false);
    }

    private boolean loadCache() {
        if (!versionCacheFile.exists()) return false;

        try {
            List<String> lines = java.nio.file.Files.readAllLines(versionCacheFile.toPath());
            if (lines.size() < 2) return false;

            Instant timestamp = Instant.parse(lines.get(0));
            if (timestamp.isBefore(Instant.now().minusSeconds(3600))) { // Cache valide 1h
                return false;
            }

            this.latestVersionString = lines.get(1);
            this.lastChecked = timestamp;
            return true;

        } catch (Exception e) {
            getLogger().warning("⚠ Failed to read update cache : " + e.getMessage());
            return false;
        }
    }

    private void saveCache() {
        try {
            List<String> lines = List.of(Instant.now().toString(), latestVersionString);
            java.nio.file.Files.write(versionCacheFile.toPath(), lines);
        } catch (Exception e) {
            getLogger().warning("⚠ Failed to write update cache : " + e.getMessage());
        }
    }

    public int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int p1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int p2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (p1 != p2) {
                return Integer.compare(p1, p2);
            }
        }
        return 0;
    }
}
