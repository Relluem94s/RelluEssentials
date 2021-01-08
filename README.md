![Rellu Essentials](https://img.relluem94.de/logos/relluessentials.png)

### a Spigot Plugin compatible with Spigot 1.16.4

## Commands
* Flymode (/fly)
* Gamemode (/0,/1,/2,/3)
* Portable Craftingbench (/craft)
* Gift Cookies (/cookie)
* Change Weather to Sun (/sun)
* Change Weather to Rain (/rain)
* Change Weather to Storm (/storm)
* Change time to Day (/day)
* Change time to Night (/night)
* Teleport to Worldspawn (/spawn)
* Open the Enderchest (/enderchest)
* Open the Inventory (/inv)
* Set Permission Group (/setGroup)
* Nick Player (/nick)
* Kill yourself (/suicide)
* Get More Stuff (/more)
* Repair your favorite Tools (/repair)
* Teleport to your Bed spawn location or set list tp to homes that get saved in players.yml (/home)
* Save, Reload your config or more (/rellu)
* Play as god (/god)
* Heal yourself if you low on health (/heal)
* Away from Keyboard (/afk)
* Message other Players (/msg)
* Send a Title to a Player (/title)
* Show where a Player is (/where)
* Print Message in Chat in Player / Commandblock Name (/print)
* Rename item in your Hand (/rename)

## Skills
* TreeFeller (Fell the whole Tree) [he is dead jim]
* Repair (Repair Tools and Armor) [the lost son]
* Salvage (Salvage Tools and Armor and get back resources and enchantments) [needs some love]
* Auto Replant Seeds
* Auto Smelt  [small tweaks]
* Telekenesis [half finished]

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
* No Death Message
  * Save Death Location as Home
  * Show Location in Chat (private)




![Database Model](https://raw.githubusercontent.com/Relluem94/RelluEssentials/RE-14/db_model.png)

## TODOS
* BlockHistory save materials into DB
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
* Multi Language ressources (from Database)
* Skill System with listener on blockbreak etc.
* Fix some bugs with the treefeller (activate only sometimes pair with skill system)
* the rest from the snippet textfile

