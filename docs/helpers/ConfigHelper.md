# Documentation

## `public class ConfigHelper`

 * **Author:** rellu

## `public List<PlayerEntry> getPlayers()`

 * **Returns:** Returns List of all Players from config file

## `public List<LocationEntry> getHomes(PlayerEntry p)`

 * **Parameters:** `p` — Player
 * **Returns:** List of Homes as LocationEntry

## `public String getConfigName()`

Returns the Config Name

 * **Returns:** String Config Name

## `public File getFile()`

Returns the File

 * **Returns:** File

## `public YamlConfiguration getConfig()`

Returns the Config

 * **Returns:** YamlConfiguration

## `public void save() throws IOException`

Saves the Config

 * **Exceptions:** `IOException` — 

## `public void reload() throws IOException, FileNotFoundException, InvalidConfigurationException`

Reloads the Config

 * **Exceptions:**
   * `IOException` — 
   * `FileNotFoundException` — 
   * `InvalidConfigurationException` — 

## `public void setLocation(String path, Location l)`

 * **Parameters:**
   * `path` — where the Location gets saved
   * `l` — the Location itself

## `public Location getLocation(String path)`

 * **Parameters:** `path` — where the Location is saved
 * **Returns:** Location from the Config Path
