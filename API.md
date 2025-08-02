# RegenSystem API

This document explains how to use the RegenSystem API via JitPack.

## 📦 Getting Started

To use the RegenSystem API in your own plugin, you must declare it as a dependency using [JitPack](https://jitpack.io).

### Step 1 : Add the JitPack repository

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

### Step 2 : Add the API dependency

```groovy
dependencies {
    compileOnly 'com.github.DarkLash1.RegenSystem:regensystem-api:<version>'
}
```

> ⚠️ **Important** :
> 
> Always use `compileOnly` and never `implementation` for plugin APIs like RegenSystem.
> 
> This ensures the classes are not bundled in your plugin jar, and Spigot uses the version provided by the `RegenSystem` plugin at runtime.
> 
> Using `implementation` can cause classloader conflicts and prevent custom events from working properly.

Replace `<version>` with the latest release tag (e.g `v0.1.3`).

## 🧩 Usage Example

### Listen to `ZoneRegenerationStartEvent`

```java
import fr.darklash.regensystem.api.event.ZoneRegenerationStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class MyListener implements Listener {

    @EventHandler
    public void onZoneRegenStart(ZoneRegenerationStartEvent event) {
        String zone = event.getZoneName();
        Player player = event.getPlayer();

        if (player != null) {
            Bukkit.getLogger().info(player.getName() + " triggered regeneration in zone: " + zone);
        } else {
            Bukkit.getLogger().info("Zone " + zone + " is regenerating (no player specified).");
        }
    }
}
```

### Event Methods

| Method                  | Description                           |
|-------------------------|---------------------------------------|
| `getZoneName()`         | Returns the name (ID) of the regenerating zone |
| `getPlayer()`           | Returns the player who triggered the regeneration (nullable) |
| `isCancelled()`         | 	Returns whether the event is currently cancelled |
| `setCancelled(boolean)` | Cancels or uncancels the event        |

## ❗ Notes

- The RegenSystem plugin must be installed and enabled on the server.
- You must declare a `depend` or `softdepend` on `RegenSystem` in your `plugin.yml` :

```yaml
depend: [RegenSystem]
```

## 📮 Need Help ?

Open an issue or discussion on the [GitHub repository](https://github.com/DarkLash1/RegenSystem) if you need help or want to suggest new API features.
