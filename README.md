# RegenSystem 

[![](https://img.shields.io/modrinth/dt/regensystem?label=Downloads&color=brightgreen&style=flat-square)](https://modrinth.com/plugin/regensystem)
[![Modrinth](https://img.shields.io/modrinth/v/regensystem?color=green&label=Modrinth&logo=modrinth)](https://modrinth.com/plugin/regensystem)
[![License: DL Group Public License](https://img.shields.io/badge/license-DLGPL--RegenSystem-orange.svg)](https://github.com/DarkLash1/RegenSystem/blob/main/LICENSE)

A powerful, flexible block regeneration system designed for PvP Box, survival zones, or mining zones. Define cuboid areas and let them regenerate automatically over time – all with a single command.

---

### ✅ Server Compatibility

- ✅ Fully compatible with Paper — RegenSystem uses modern Adventure components that are not supported on Spigot and only partially supported on Bukkit.
- ❌ Spigot is no longer supported — the plugin will not work correctly due to missing Adventure support.
- ⚠️ Not tested on Purpur or Folia — compatibility may vary.

---

### ✅ RegenSystem API

**🔗 Full documentation available here :**
[RegenSystem API](https://github.com/DarkLash1/RegenSystem/wiki/RegenSystem-API)

Learn how to interact with the plugin through custom events and clean interfaces for building compatible extensions or plugins.

---

### ✅ Commands

| Command                        | Description                                                                 |
|-------------------------------|-----------------------------------------------------------------------------|
| `/regen pos1`                 | Set the first corner of the zone at your current location.                 |
| `/regen pos2`                 | Set the second corner of the zone at your current location.                |
| `/regen save <name> [delay]`  | Save a new zone with an optional regen delay in seconds.                   |
| `/regen reload`               | Reload all zones and restart regen tasks.                                  |
| `/regen delete <name>`        | Delete a zone and its stored data.                                         |
| `/regen snapshot <name>`      | Update the stored state of a zone with the current blocks.                 |
| `/regen wand`                 | Receive a diamond axe to select pos1/pos2 with left/right click.           |
| `/regen enable <all\name>`   | Enable regen globally or for a specific zone.                              |
| `/regen disable <all\name>`  | Disable regen globally or for a specific zone.                             |
| `/regen menu`                | Opens the menu to modify options.                                    
|
| `/regen help`                | Show the help menu.
|
| `/regen flag <zone> <flag> <on\off>` | Enables or disables flags for a zone.
|

---

### ✅ Permissions

| Permission             | Description                                       | Default |
| ---------------------- | ------------------------------------------------- | ------- |
| `regensystem.*`        | Grants all permissions above                      | OP      |
| `regensystem.command`  | Use the base `/regen` command                     | OP      |
| `regensystem.pos`      | Set pos1 and pos2 using `/regen pos1/pos2`        | OP      |
| `regensystem.save`     | Save zones via `/regen save`                      | OP      |
| `regensystem.reload`   | Reload zones via `/regen reload`                  | OP      |
| `regensystem.delete`   | Delete zones via `/regen delete`                  | OP      |
| `regensystem.snapshot` | Update zone data with `/regen snapshot`           | OP      |
| `regensystem.wand`     | Give yourself the selection axe via `/regen wand` | OP      |
| `regensystem.toggle`   | Enable/disable regen with `/regen enable/disable` | OP      |
| `regensystem.menu`     | Open menu via `/regen menu`                       | OP      |
| `regensystem.update`   | Receive an update message on connection           | OP      |
| `regensystem.flag`     | Allows you to manage flags with `/regen flag`     | OP      |

💡 Use `regensystem.*` to quickly give full access to the plugin.

---

### 🔁 Dependencies

- ✅ PlaceholderAPI _(soft-depend, optional)_

To enable placeholders, simply install PlaceholderAPI – the plugin will auto-detect it.

---

### ✅ Placeholders (via PlaceholderAPI)

If [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) is installed, you can use the following placeholders :

| Placeholder                         | Description                                         |
|-------------------------------------|-----------------------------------------------------|
| `%regen_zone_count%`               | Total number of zones defined                      |
| `%regen_regen_enabled%`            | Global regen status (`enabled` or `disabled`)      |
| `%regen_enabled_<zone>%`           | If regen is enabled for a specific zone            |
| `%regen_delay_<zone>%`             | Regen delay (in seconds) for a specific zone       |
| `%regen_exists_<zone>%`            | Returns "true" if the zone exists                  |
| `%regen_block_count_<zone>%`       | Number of original blocks in zone                  |
| `%regen_corner1_<zone>%`           | Coordinates of the first corner of the zone        |
| `%regen_corner2_<zone>%`           | Coordinates of the second corner of the zone       |
| `%regen_timer_<zone>%`             | Time remaining before next regeneration (mm:ss)    |
| `%regen_name_<zone>%`              | Official zone name                                 |

⚠️ Placeholders are auto-registered if PlaceholderAPI is present. No extra config needed.

---

### ✅ bStats

RegenSystem uses [bStats](https://bstats.org/plugin/bukkit/RegenSystem) to collect basic stats like server count and version. This helps the developer improve the plugin over time.

---

### ✅ File Structure

```bash
plugins/
└── RegenSystem/
    ├── config.yml     # Plugin messages & prefix (do NOT edit version !)
    ├── zone.yml       # Zones definition : positions, delays, enabled flags
    ├── version.cache  # Don't touch this file
    └── zone.db        # 🔥 Block data storage (SQLite, auto-managed)
```

---

### ✅ zone.yml

> This file contains the definitions for your zones : their position, delay, and status.

```yaml
global:
  regen-enabled: true      # Enable or disable regeneration globally

zones:
  zone1:                   # Replace with your own zone names
    pos1: world,100,64,100 # First corner of the cuboid (x, y, z)
    pos2: world,110,70,110 # Second corner of the cuboid (x, y, z)
    regenDelay: 60         # Delay in seconds
    enabled: true          # If false, the zone won't regenerate
    flags:                 # Zone-specific flags
      BLOCK_BREAK: true
      ITEM_DROP: true
      MOB_SPAWN: true
      PVP: true
      BLOCK_PLACE: true
      MOB_DAMAGE: true
      EXPLOSION: true
      ITEM_PICKUP: true
```

### ✅ config.yml

> ⚠️ Do not edit the version field – it’s used internally for updates.

```yaml
version: 2                    # ⚠️ Do not modify!
debug: false                  # For more information in the logs
prefix: "&6[RegenSystem] &r"  # Prefix used in plugin messages
updates:               
  check-interval: 12          # In hours
  notify-admins: true         # To notify the admins
```

### ✅ zone.db (SQLite)

> Stores block snapshots for each zone – fast, optimized, and safe.
> Do **not** edit manually – managed automatically by the plugin.

---

### 📌 Installation

1. Drop `RegenSystem-VERSION.jar` into your `plugins/` folder.

2. (Optional) Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) for placeholder support.

3. Start your server.

4. Use `/regen wand` to define zones and `/regen save <name>` to activate.

---
