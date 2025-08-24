package fr.darklash.regensystem.manager;

import fr.darklash.regensystem.RegenSystem;
import fr.darklash.regensystem.util.Key;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private final RegenSystem pl;
    private final Map<String, FileConfiguration> configs = new HashMap<>();

    public FileManager(RegenSystem pl) {
        this.pl = pl;
        loadAll();
    }

    public void load(String fileName, String subFolder) {
        if (isValidFile(fileName)) return;

        File configFile = getFile(fileName, subFolder);

        if (!configFile.exists()) {
            String resourcePath = (subFolder == null || subFolder.isEmpty()) ? fileName : subFolder + java.io.File.separator + fileName;
            pl.saveResource(resourcePath, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(fileName, config);

        update(fileName, subFolder);
    }

    public void load(String fileName) {
        load(fileName, null);
    }

    public void save(String fileName, String subFolder) {
        if (isValidFile(fileName)) return;

        FileConfiguration config = configs.get(fileName);
        if (config == null) {
            pl.getLogger().severe("The configuration file " + fileName + " has not been loaded.");
            return;
        }

        File configFile = getFile(fileName, subFolder);

        try {
            config.save(configFile);
        } catch (IOException e) {
            pl.getLogger().severe("Unable to save : " + fileName);
        }
    }

    public void save(String fileName) {
        save(fileName, null);
    }

    public void update(String fileName, String subFolder) {
        if (isValidFile(fileName)) return;

        File configFile = getFile(fileName, subFolder);

        // Charger la config existante (celle sur le disque)
        FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(configFile);

        // Charger la config par défaut (depuis le .jar)
        String resourcePath = (subFolder == null || subFolder.isEmpty()) ? fileName : subFolder + "/" + fileName;
        InputStream defaultConfigStream = pl.getResource(resourcePath);
        if (defaultConfigStream == null) {
            pl.getLogger().warning("Default resource not found for : " + fileName);
            return;
        }

        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));

        // Si non versionné, on ne touche pas
        if (!defaultConfig.contains("version")) return;

        int currentVersion = existingConfig.getInt("version", -1);
        int defaultVersion = defaultConfig.getInt("version", -1);

        if (defaultVersion > currentVersion) {
            pl.getLogger().warning("File '" + fileName + "' is outdated (v" + currentVersion + " < v" + defaultVersion + "). Updating...");

            // Stocker les anciennes valeurs valides (présentes dans la nouvelle version)
            Map<String, Object> preservedValues = new HashMap<>();
            for (String key : defaultConfig.getKeys(true)) {
                if (existingConfig.contains(key)) {
                    preservedValues.put(key, existingConfig.get(key));
                }
            }

            // Écraser le fichier avec la version par défaut (qui contient les commentaires à jour)
            pl.saveResource(resourcePath, true);

            // Recharger la version écrasée
            FileConfiguration reloadedConfig = YamlConfiguration.loadConfiguration(configFile);

            // Réinjecter les anciennes valeurs valides
            for (Map.Entry<String, Object> entry : preservedValues.entrySet()) {
                reloadedConfig.set(entry.getKey(), entry.getValue());
            }

            // Mettre à jour la version
            reloadedConfig.set("version", defaultVersion);

            try {
                reloadedConfig.save(configFile);
                configs.put(fileName, reloadedConfig);
                pl.getLogger().info("File '" + fileName + "' has been updated with custom values and comments.");
            } catch (IOException e) {
                pl.getLogger().severe("Error saving file '" + fileName + "' : " + e.getMessage());
            }

        } else {
            // Ajout des clés manquantes (si version actuelle à jour)
            boolean updated = false;
            for (String key : defaultConfig.getKeys(true)) {
                if (!existingConfig.contains(key)) {
                    existingConfig.set(key, defaultConfig.get(key));
                    updated = true;
                }
            }

            if (updated) {
                try {
                    existingConfig.save(configFile);
                    configs.put(fileName, existingConfig);
                    pl.getLogger().info("File '" + fileName + "' updated with the new missing keys.");
                } catch (IOException e) {
                    pl.getLogger().severe("Error updating file '" + fileName + "' : " + e.getMessage());
                }
            }
        }
    }

    public void forceUpdateAllVersionedFiles() {
        for (Map.Entry<String, FileConfiguration> entry : configs.entrySet()) {
            String fileName = entry.getKey();
            forceUpdate(fileName, null);
        }
    }

    public void forceUpdate(String fileName, String subFolder) {
        if (isValidFile(fileName)) return;

        File configFile = getFile(fileName, subFolder);

        String resourcePath = (subFolder == null || subFolder.isEmpty()) ? fileName : subFolder + "/" + fileName;
        InputStream defaultConfigStream = pl.getResource(resourcePath);
        if (defaultConfigStream == null) return;

        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));

        if (!defaultConfig.contains("version")) return;

        int defaultVersion = defaultConfig.getInt("version", -1);

        pl.getLogger().info("Forced file update '" + fileName + "'...");

        // Sauvegarder les valeurs personnalisées valides
        FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(configFile);
        Map<String, Object> preservedValues = new HashMap<>();
        for (String key : defaultConfig.getKeys(true)) {
            if (existingConfig.contains(key)) {
                preservedValues.put(key, existingConfig.get(key));
            }
        }

        // Remplacer par la ressource par défaut
        pl.saveResource(resourcePath, true);

        // Recharger le fichier
        FileConfiguration reloadedConfig = YamlConfiguration.loadConfiguration(configFile);

        // Réinjecter les anciennes valeurs valides
        for (Map.Entry<String, Object> entry : preservedValues.entrySet()) {
            reloadedConfig.set(entry.getKey(), entry.getValue());
        }

        // Mettre à jour la version
        reloadedConfig.set("version", defaultVersion);

        try {
            reloadedConfig.save(configFile);
            configs.put(fileName, reloadedConfig);
            pl.getLogger().info("File '" + fileName + "' successfully forced.");
        } catch (IOException e) {
            pl.getLogger().severe("Error during forced file update '" + fileName + "' : " + e.getMessage());
        }
    }

    public void loadAll() {
        load(Key.File.CONFIG);
        load(Key.File.ZONE);
    }

    public void saveAll() {
        save(Key.File.CONFIG);
        save(Key.File.ZONE);
    }

    public FileConfiguration get(String fileName) {
        return configs.get(fileName);
    }

    private boolean isValidFile(String fileName) {
        return fileName == null || fileName.trim().isEmpty();
    }

    private File getFile(String fileName, String subFolder) {
        return (subFolder == null || subFolder.isEmpty())
                ? new File(pl.getDataFolder(), fileName)
                : new File(pl.getDataFolder(), subFolder + java.io.File.separator + fileName);
    }
}
