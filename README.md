![Rellu Essentials](https://static.relluem94.de/logos/app/relluessentials.png)

### a Spigot Plugin compatible with Spigot 1.21.9

# First Steps
Find out how to get the plugin and how to use it.

## Usage of Plugin
1. [Download](https://github.com/Relluem94s/RelluEssentials/packages) or [Build](https://github.com/Relluem94s/RelluEssentials#build) the plugin jar 
2. Copy the jar into the `plugin` Directory
3. Start and Stop the Server
4. Set Host, User and Password of your MySQL Server in the config.yml
5. Start your Server again
6. Use (`/setGroup`) in your Server Console to grant permission to your player
7. Enjoy

# Functionality 
What does the plugin include?

## Commands
* Admin gives AdminTools, Clearing Chat, Fake AFK, Teleport to the highest Block and Custom NPCs and others (`/admin`) 
* Away from Keyboard (`/afk`)
* Change your own Weather (vip) (`/playerweather`)
* Teleports to the latest Checkpoint (on spawn, teleport, world, warp and home) (`/back`)
* Opens Bag Pack Menu (`/bags`)
* Broadcasts Message to online Players (`/broadcast`)
* Gift Cookies (`/cookie`)
* Change time to Day (`/day`)
* Open the Enderchest (`/enderchest`)
* Linux Like (`/exit`)
* Fly mode (`/fly`)
* Game mode (`/0` `/1 `/`2` `/3`)
* List all Gamerules for the current World (`/gamerules`)
* Play as god (`/god`)
* Gives a Player Head (`/head`)
* Heal yourself if you low on health (`/heal`)
* Teleport to your Bed spawn location or set list tp to homes that get saved in players.yml (`/home`)
* Open the Inventory (`/inv`)
* Message other Players (`/msg`)
* Get More Stuff (`/more`)
* Nick Player (`/nick`)
* Change time to Night (`/night`)
* Set Permission Group (`/setGroup`)
* Poke a Player if he does not respond (`/poke`)
* Portable Crafting Bench (`/craft`)
* Print Message in Chat in Player / Command Block Name (`/print`)
* Protect your Chests, Doors and other Blocks with (`/protect`)
* Shows you your Money in your Purse (`/purse`)
* Change Weather to Rain (`/rain`)
* Save, Reload your config or more (`/rellu`)
* Rename item in your Hand (`/rename`)
* Repair your favorite Tools (`/repair`)
* Does a rollback of a player (`/rollback`) (WIP)
* Edit or Copy your Signs (`/sign`)
* Teleport to World Spawn (`/spawn`)
* Change your Speed with (`/speed`)
* Change Weather to Storm (`/storm`)
* Linux Like (`/sudo`)
* Kill yourself (`/suicide`)
* Change Weather to Sun (`/sun`)
* Teleport to other Players (`/tp`)
* Teleport to or a Player (`/teleport`)
* Shows all Team Members (mod and higher) (`/team`)
* Send a Title to a Player (`/title`)
* Vanish you from other Players (`/vanish`)
* Warps you to a defined Point (`/warp`)
* Show where a Player is (`/where`)
* Show Worlds or Teleport to the spawn (`/world`)
* Set Positions for Modify Command (`/postion`)
* Modify selected Blocks (`/modify`)


## NPCs
* Custom NPC via Database or via API
* NPCs in Game via Database
    * Adventurer
    * Baker
    * Butcher
    * Farmer
    * Fisher
    * Florist
    * Lumberjack
    * Miner
    * Musician
    * Shepherd
    * Smith
    * Builder
* NPCs in Game via Code
    * Bag Salesman
    * Banker
    * Beekeeper
    * Enchanter (WIP)


## Bags
* Custom Bags via Database
    * Mining Bag
    * Farming Bag
    * Monster Bag
    * Shepherd Bag
    * Lumberjack Bag
    * Nether Bag


## Functional Block Protections
* Add additional Blocks via Database or via API


## Enchantments
* Replenishment (Auto Replant Seeds)
* AutoSmelt  (Smelts all Blocks)
* Telekinesis (Teleports item into Inventory)
* Delicate (Prevents breaking immature Crops and Stems)
* ThunderStrike (Strikes Entity with a Lightning Bolt)


## Skills
* Skills will be reworked. Some as Enchant some as Skill. (WIP)
    * TreeFeller (Fell the whole Tree) [he is dead jim]
    * Repair (Repair Tools and Armor) [the lost son]
    * Salvage (Salvage Tools and Armor and get back resources and enchantments) [needs some love]


## Events
* Better Chat Format (Player >> Message)
    * Color Codes for VIP and higher
    * Chat Channels Vip, Mod, Admin | #v, #m, #a
* Better Player Join Message (Shows Custom Join Message)
  * Sets Custom Tab Header and Footer (Database)
  * Sets Fly mode to enabled 
    * if VIP or higher 
    * if in air
    * if fly mode enabled in playerEntry (Database)
* Better Player Quit Message (Shows Custom Leave Message)
* Better Block Drop (Glass etc.)
* Better Mobs (Disable Phantom)
* Better (Call) Soil (disables trampling, sneak to disable)
* Better Safety (Auto disable some commands like /pl)
* Player Move is disabled while afk
* Player Head Info
* CloudSailor (Gliding Feather and Boots)
* Tool Crafting with Rarity, Higher Tier is unbreakable
* Position Highlighter shows your Selections
* No Death Message
  * Save Death Location as Home
  * Show Location in Chat (private)

## Build
1. ```shell
   git clone https://github.com/Relluem94/RelluEssentials.git
   ```

2. ```shell
   git checkout $(git describe --tags $(git rev-list --tags --max-count=1))
   ```

3. ```shell
   mvn clean install -Pship
   ```


## Development
```shell
./RelluBash-Script-Collection/dev/MinecraftDevelopmentEnvironment.sh
```
it will run a specified Dev-Sever (with docker mysql and phpmyadmin) check [RelluBash-Script-Collection](https://github.com/Relluem94s/RelluBash-Script-Collection)


# Documentation
How it is working? What's under the hood?

## Helpers
- AttributeHelper
- BagHelper
- BankerHelper
- [BlockHelper](./docs/helpers/BlockHelper.md)
- [ChatHelper](./docs/helpers/ChatHelper.md)
- [ConfigHelper](./docs/helpers/ConfigHelper.md)
- DatabaseHelper
- EnchantmentHelper
- ExperienceHelper
- [InventoryHelper](./docs/helpers/InventoryHelper.md)
- [ItemHelper](./docs/helpers/ItemHelper.md)
- [MobHelper](./docs/helpers/MobHelper.md)
- NPCHelper
- PlayerHeadHelper
- [PlayerHelper](./docs/helpers/PlayerHelper.md)
- [ProtectionHelper](./docs/helpers/ProtectionHelper.md)
- [RecipeHelper](./docs/helpers/RecipeHelper.md)
- [SignHelper](./docs/helpers/SignHelper.md)
- [StringHelper](./docs/helpers/StringHelper.md)
- [TypeHelper](./docs/helpers/TypeHelper.md)
- [UUIDHelper](./docs/helpers/UUIDHelper.md)
- [WorldHelper](./docs/helpers/WorldHelper.md)
- others
  - [Vector2Location](./docs/helpers/Vector2Location.md)


## API
- BagAPI
- BankAPI
- NPCAPI
- PlayerAPI
- ProtectionAPI
- WarpAPI


## Database Model
![Database Model](https://raw.githubusercontent.com/Relluem94s/RelluEssentials/master/db_model.svg)


## Build & Code Coverage

[![Java CI with Maven](https://github.com/Relluem94s/RelluEssentials/actions/workflows/maven.yml/badge.svg)](https://github.com/Relluem94s/RelluEssentials/actions/workflows/maven.yml)
[![Dependency review](https://github.com/Relluem94s/RelluEssentials/actions/workflows/dependency-review.yml/badge.svg)](https://github.com/Relluem94s/RelluEssentials/actions/workflows/dependency-review.yml) 
![Coverage](https://raw.githubusercontent.com/Relluem94s/RelluEssentials/refs/heads/badges/.github/badges/jacoco.svg)
![Branches](https://raw.githubusercontent.com/Relluem94s/RelluEssentials/refs/heads/badges/.github/badges/branches.svg)
