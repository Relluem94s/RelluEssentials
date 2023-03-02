![Rellu Essentials](https://img.relluem94.de/logos/app/relluessentials.png)

### a Spigot Plugin compatible with Spigot 1.16.4

## Development / Build
1. ```shell
   git clone https://github.com/Relluem94/RelluEssentials.git
   ```

1. ```shell
   git checkout $(git describe --tags $(git rev-list --tags --max-count=1))
   ```

1. ```shell
   mvn clean install
   ```

For Development you can use 
```shell
./MinecraftDevelopmentEnvironment.sh
```
it will run a specified Dev-Sever (with docker mysql and phpmyadmin)

## First Steps
1. Copy the jar into the `plugin` Directory
1. Start and Stop the Server
1. Set User and Password of your MySQL Server in the config.yml
1. Start your Server again
1. Enjoy

## Commands
* Admin gives Clearing Chat, Fake AFK, Teleport to highest Block and Custom NPCs  (`/admin`) 
* Away from Keyboard (`/afk`)
* Broadcasts Message to online Players (`/broadcast`)
* Gift Cookies (`/cookie`)
* Change time to Day (`/day`)
* Open the Enderchest (`/enderchest`)
* Flymode (`/fly`)
* Gamemode (`/0` `/1 `/`2` `/3`)
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
* Portable Craftingbench (`/craft`)
* Print Message in Chat in Player / Commandblock Name (`/print`)
* Protect your Chests, Doors and other Blocks with (`/protect`)
* Shows you your Money in your Purse (`/purse`)
* Change Weather to Rain (`/rain`)
* Save, Reload your config or more (`/rellu`)
* Rename item in your Hand (`/rename`)
* Repair your favorite Tools (`/repair`)
* Does a rollback of a player (`/rollback`) (WIP)
* Edit or Copy your Signs (`/sign`)
* Teleport to Worldspawn (`/spawn`)
* Change your Speed with (`/speed`)
* Change Weather to Storm (`/storm`)
* Kill yourself (`/suicide`)
* Change Weather to Sun (`/sun`)
* Send a Title to a Player (`/title`)
* Vanish you from other Players (`/vanish`)
* Show where a Player is (`/where`)
* Show Worlds or Teleport to the spawn (`/world`)


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
    * Musican
    * Shepherd
    * Smith
* NPCs in Game via Code
    * Bag Salesman
    * Banker
    * Beekeeper
    * Enchanter (WIP)

## Functional Block Protections
* Add aditional Blocks via Database or via API

## Skills
* Skills will be reworked. Some as Enchant some as Skill. (WIP)
    * Auto Replant Seeds
    * Auto Smelt  [small tweaks for attached Blocks needed]
    * Telekinesis
    * TreeFeller (Fell the whole Tree) [he is dead jim]
    * Repair (Repair Tools and Armor) [the lost son]
    * Salvage (Salvage Tools and Armor and get back resources and enchantments) [needs some love]


## Events
* Better Chat Format (Player >> Message)
    * Color Codes for VIP and higher
    * Chat Channels Vip, Mod, Admin | #v, #m, #a
* Better Player Join Message (Shows Custom Join Message)
  * Sets Custom Tab Header and Footer (defined in config.yml)
  * Sets Flymode to enabled 
    * if VIP or higer 
    * if in air
    * if flymode enabled in players.yml
* Better Player Quit Message (Shows Custom Leave Message)
* Better Block Drop (Glass etc.)
* Better Mobs (Disable Phantom)
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
![Database Model](https://raw.githubusercontent.com/Relluem94s/RelluEssentials/master/db_model.png)

## TODOS
* [ ] Fix flags and Rights problem on modify (reload that protectionentry)
* [ ] Add Bank on deposit or withdraw show balance
* [ ] Add /bags 1,2,3,4 or /bags miner, farmer, etc 
* [ ] Fix Bambus and Sugarcane drops on telekinesis
* [ ] Add Price for enchantments
* [ ] May Add Toggle for Replenishment and Autosmelt
* [ ] Delicate add torches to non break list
* [ ] /head 3 args to /customheads
* [ ] Head info user
* [ ] Add Death Chests
* [ ] Add Config (DB) for Deathchests, Ore Restore, etc
* [ ] Remove custom crafting custom smelter stuff
* [ ] Add API with methods to access lists etc and use it internally too
* [ ] 
* [ ] 
* [x] Add Protection Command for Chests Doors etc
* [x] chest / falldoor / door / fence-gate protection with permissions for player and groups with flags
* [x] Add Messages for Protection Modify etc
* [x] May add Views to increase performance (View with only location type 5)
* [x] Add Missing Up and Down Sign Action
* [x] Add Sign Edit
* [x] Add Sign Copy / Paste
* [x] Add Fake AFK (/admin Command)
* [x] Add Chat Clear (/admin Command)
* [x] Add Top (/admin Command)
* [x] Add Bank Upgrade
* [x] Add Redstoneless Lights /admin light
* [x] Rewrite NPC Stuff (less hardcoded)
* [x] Add NPCs to Database
* [x] Fixed Playerhead Command 
* [x] Fixed Playerhead Event 
* [x] Add Request to Mojang API to get Textures
* [x] Add CustomHeads (with textures as base64, uuid and name)
* [x] Add Bags (Miner, Farmer) Database based (softcoded) with insert in bag_type new bag
* [x] Add Sign Up and Down Command
* [x] Add Protection Lock Materials / Types in Database
* [x] Add Protection overwrite by admin and mod group
* [x] Add interest for bank every day (on login)
* [x] Add Missing Strings from Admin.java Command
* [x] Add Missing Strings from Worlds.java Command
* [x] Included /rellu ping to /admin ping
* [x] Add Sudo Command to sudo as other Player 
* [x] Add Exit Command to self kick Players. Or Close Server as Console Sender
* [x] Add NPC Builder who sells Buildblocks
* [x] Enchantment System with custom enchantments
* [x] Add Telekinesis
* [x] Add Thunderstroke
* [x] Add Replenishment
* [x] Add Delicate
* [x] Add Auto Smelting


## LONG TERM TODOS
* [ ] statistics command to show player stats like playtime, first and last played, avg playtime or others
* [ ] MCMMO Skill Level System with listener on blockbreak etc.
* [ ] Fix some bugs with the treefeller (activate only sometimes pair with skill system)
* [ ] check inv for incomplete stack of item for telekinesis event (drops item if no space is left)
* [ ] Small / Easy Worldedit & World Guard
* [ ] BlockHistory save materials into DB (50%)
* [ ] Netherstar for Selection
* [ ] Manual selection with pos
* [ ] set command (material)
* [ ] extend command
* [ ] region protection with permissions for player and groups with flags
* [ ] flags -> build, enter, entry/leave-message, interact, harvest
* [ ] flags -> cant-open-message (standard message or custom), open-on-day, open-on-night
* [ ] fast pickaxe mode
* [ ] fast shovel mode
* [ ] Stick to Rotate Stairs and convert Upper/lower slaps
* [ ] Add Permission System as extra for external plugins (maybe)
* [ ] log Playtime of Players
* [ ] Multi Language resources (from Database)
* [ ] the rest from the snippet textfile









[![Build Status](https://build.relluem94.de/buildStatus/icon?job=RelluEssentials)](https://build.relluem94.de/view/Minecraft%20Stuff/job/RelluEssentials/)
