# RegenSystem API

This document explains how to use the RegenSystem API via JitPack.

## 📦 Getting Started

To use the RegenSystem API in your own plugin, you must declare it as a dependency using [JitPack](https://jitpack.io).

### Step 1: Add the JitPack repository

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

### Step 2: Add the API dependency

```groovy
dependencies {
    implementation 'com.github.DarkLash1.RegenSystem:regensystem-api:<version>'
}
```

Replace `<version>` with the latest release tag (e.g. `v0.0.9`).

## 🧩 Usage Example

### Listen to `RegenZoneEvent`

```java
import fr.darklash.regensystem.api.event.RegenZoneEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class MyListener implements Listener {

    @EventHandler
    public void onRegenZone(RegenZoneEvent event) {
        String zone = event.getZoneId();
        Player player = event.getPlayer();
        if (player != null) {
            Bukkit.getLogger().info(player.getName() + " triggered regen in zone: " + zone);
        } else {
            Bukkit.getLogger().info("Zone " + zone + " is regenerating (no player specified).");
        }
    }
}
```

### Event Methods

| Permission              | Description                           |
|-------------------------|---------------------------------------|
| `getZoneId()`           | Returns the ID of the regen zone      |
| `getPlayer()`           | Returns the player triggering it      |
| `isCancelled()`         | Returns whether the event is canceled |
| `setCancelled(boolean)` | Cancel the event                      |

## ❗ Notes

- The RegenSystem plugin must be installed and enabled on the server.
- You must declare a `depend` or `softdepend` on `RegenSystem` in your `plugin.yml` :

```yaml
depend: [RegenSystem]
```

## 📮 Need Help ?

Open an issue or discussion on the [GitHub repository](https://github.com/DarkLash1/RegenSystem) if you need help or want to suggest new API features.
