# ⛏️ MineAlert: The Advanced Anti-X-Ray Solution

**MineAlert** is a highly efficient and fully customizable anti-cheat plugin designed to ensure fair play on your Minecraft server by rigorously monitoring and flagging suspicious **X-Ray** activities. Protect your server's economy and player experience from the abuse of resource-finding cheats.

***

## ✨ Features at a Glance

* **Advanced X-Ray Detection:** The core function of MineAlert is to track the frequency of rare ore (Diamond, Ancient Debris, etc.) breakage. It uses configurable, threshold-based alerts to flag players who find too many high-value minerals within a defined timeframe.
* **Minimal Server Impact:** Engineered for performance, MineAlert utilizes highly optimized methods to minimize server lag and operates cleanly and efficiently in the background.
* **Database Flexibility:** Supports high-performance data storage to manage player logs and infractions reliably:
    * **SQLite** (Default, zero configuration)
    * **MySQL**
    * **MariaDB**
* **Full Customization:** The entire plugin is fully customizable and translatable, allowing you to tailor messages and settings to perfectly fit your server environment.
* **Multi-Language Support:** Ships with automatic translation support for:
    * **English (EN)**
    * **Italian (IT)**

***

## 🛠️ Commands and Usage

All commands are executed using the primary alias `/minealert` (or the shortened form, if available).

| Command | Description | Required Permission                                 |
| :--- | :--- |:----------------------------------------------------|
| `/minealert checklogs [player]` | Displays a comprehensive log of a player's mining statistics and recorded cheating infractions. | `minealert.command.checklogs`                       |
| `/minealert reload` | Reloads the plugin's configuration file (`settings.yml`) without restarting the server. | `minealert.command.reload`                          |
| `/minealert ?` | Shows a list of all available commands for which the player has permission. | *Requires permission for at least one subcommand*   |

***

## ⚙️ Configuration & Permissions

### Permissions Structure

MineAlert provides granular control over who receives alerts and who is immune to detection (for staff testing).

* **Notify Permissions:** The permission required to receive in-game notifications when an alert is triggered is fully **customizable** in the configuration file.
* **Bypass Permissions:** The permission required for a player to be **ignored** by the alert system (staff/admin bypass) is also fully **customizable**.
* **Specific Command Permissions:**
    * Access to `/minealert reload` requires the permission: **`minealert.command.reload`**
    * Access to `/minealert ?` requires the player to have permission for at least one subcommand (e.g., `minealert.command.reload` or the `checklogs` permission).

### Alert Conditions (Example from `settings.yml`)

The heart of the detection system is easily configured within your `settings.yml` file.

| Setting | Default Value | Description |
| :--- | :--- | :--- |
| `max-blocks` | 10 | The maximum number of monitored rare blocks (like Diamonds) a player can break within the time window before an alert is triggered. |
| `time-window-ms` | 120000 | The time window (in milliseconds) used to track the `max-blocks` count. (120000 ms = 2 minutes) |

***

## 🚀 Installation

1.  Place the `MineAlert.jar` file into your server's `/plugins` folder.
2.  Restartyour server.
3.  (Optional but Recommended): Configure your desired database connection (MySQL/MariaDB) in the generated configuration files to ensure data persistence and performance.
4.  Join the server and start monitoring!