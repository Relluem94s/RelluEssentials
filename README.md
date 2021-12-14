![Rellu Essentials](https://img.relluem94.de/logos/app/relluessentials.png)

### a Spigot Plugin compatible with Spigot 1.16.4

## Development / Build
1. ```shell
   git clone https://github.com/Relluem94/RelluEssentials.git
   ```

1. ```shell
   mvn clean install
   ```

   

## First Steps
1. Copy the jar into the `plugin` Directory
1. Start and Stop the Server
1. Set User and Password of your MySQL Server in the config.yml
1. Start your Server again
1. Enjoy

## Commands
* Flymode (`/fly`)
* Gamemode (`/0` `/1 `/`2` `/3`)
* Portable Craftingbench (`/craft`)
* Gift Cookies (`/cookie`)
* Change Weather to Sun (`/sun`)
* Change Weather to Rain (`/rain`)
* Change Weather to Storm (`/storm`)
* Change time to Day (`/day`)
* Change time to Night (`/night`)
* Teleport to Worldspawn (`/spawn`)
* Open the Enderchest (`/enderchest`)
* Open the Inventory (`/inv`)
* Set Permission Group (`/setGroup`)
* Nick Player (`/nick`)
* Kill yourself (`/suicide`)
* Get More Stuff (`/more`)
* Gives a Player Head (`/head`)
* ClearChat (`/cc`)
* Vanishes Player (`/vanish`)
* Does a rollback of a player (`/rollback`)
* Repair your favorite Tools (`/repair`)
* Teleport to your Bed spawn location or set list tp to homes that get saved in players.yml (`/home`)
* Save, Reload your config or more (`/rellu`)
* Play as god (`/god`)
* Heal yourself if you low on health (`/heal`)
* Away from Keyboard (`/afk`)
* Message other Players (`/msg`)
* Send a Title to a Player (`/title`)
* Broadcasts Message to online Players (`/broadcast`)
* Show where a Player is (`/where`)
* Print Message in Chat in Player / Commandblock Name (`/print`)
* Rename item in your Hand (`/rename`)
* Vanish you from other Players (`/vanish`)
* Clear Chat from unwanted stuff (`/cc`)
* Poke a Player if he does not respond (`/poke`)
* Show Worlds or Teleport to the spawn (`/world`)


## Skills
* Auto Replant Seeds
* Auto Smelt  [small tweaks for attached Blocks needed]
* Telekinesis
* TreeFeller (Fell the whole Tree) [he is dead jim]
* Repair (Repair Tools and Armor) [the lost son]
* Salvage (Salvage Tools and Armor and get back resources and enchantments) [needs some love]


## Events
* Better Chat Format (Player >> Message)
    * Color Codes for VIP and higher
* Better Player Join Message (Shows Custom Join Message)
  * Sets Custom Tab Header and Footer (defined in config.yml)
  * Sets Flymode to enabled 
    * if VIP or higer 
    * if in air
    * if flymode enabled in players.yml
* Better Player Quit Message (Shows Custom Leave Message)
* Better Block Drop (Glass etc.)
* Better Mobs (Disable Phantom, more Iron Golem)
* Better (Call) Soil (disables trampling, sneak to disable)
* Better Safety (Auto disable some commands like /pl)
* Player Move is disabled while afk
* Player Head Info
* Cloudsailor (Gliding Feather and Boots)
* Tool Crafting with Rarity, Higher Tier is unbreakable
* No Death Message
  * Save Death Location as Home
  * Show Location in Chat (private)

## Helpers

- [BlockHelper](./docs/helpers/BlockHelper.md)
- [ChatHelper](./docs/helpers/ChatHelper.md)
- [ConfigHelper](./docs/helpers/ConfigHelper.md)
- DatabaseHelper
- [InventoryHelper](./docs/helpers/InventoryHelper.md)
- [ItemHelper](./docs/helpers/ItemHelper.md)
- [MobHelper](./docs/helpers/MobHelper.md)
- [PlayerHelper](./docs/helpers/PlayerHelper.md)
- [SignHelper](./docs/helpers/SignHelper.md)
- [StringHelper](./docs/helpers/StringHelper.md)
- [TypeHelper](./docs/helpers/TypeHelper.md)
- [Vector2Location](./docs/helpers/Vector2Location.md)
- [WorldHelper](./docs/helpers/WorldHelper.md)


## Database Model
![Database Model](https://raw.githubusercontent.com/Relluem94/RelluEssentials/RE-14/db_model.png)

## TODOS
* BlockHistory save materials into DB (50%)
* Small / Easy Worldedit & World Guard
* Netherstar for Selection
* Manual selection with pos
* set command (material)
* extend command
* region protection with permissions for player and groups with flags
* flags -> build, enter, entry/leave-message, interact, harvest
* chest / falldoor / door / fence-gate protection with permissions for player and groups with flags
* flags -> cant-open-message (standard message or custom), open-on-day, open-on-night
* MCMMO Skill Level System
* fast pickaxe mode
* fast shovel mode
* Stick to Rotate Stairs and convert Upper/lower slaps
* a permission system for external plugins (maybe)
* Multi Language resources (from Database)
* Skill System with listener on blockbreak etc.
* Fix some bugs with the treefeller (activate only sometimes pair with skill system)
* sudo command and sudo chat player 
* log Playtime of Players
* statistics command to show player stats like playtime, first and last played, avg playtime or others
* check inv for incomplete stack of item for telekinesis event (drops item if no space is left)
* the rest from the snippet textfile











[![Build Status](https://build.relluem94.de/buildStatus/icon?job=RelluEssentials)](https://build.relluem94.de/view/Minecraft%20Stuff/job/RelluEssentials/)