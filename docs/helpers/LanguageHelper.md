# Documentation

## `public class LanguageHelper`

* **Author:** relluem94

## `public LanguageHelper(JavaPlugin plugin)`

Creates a new LanguageHelper instance for the given plugin

* **Parameters:**
   * `plugin` — JavaPlugin

## `public void loadLanguages()`

Loads all available language files from the plugin JAR into memory

Available languages: `de_DE`, `en_US`, `dk_DK`

## `public void setDefaultLanguage(String language)`

Sets the default language used for message lookups

Falls back to the current default language if the given language is not loaded

* **Parameters:**
   * `language` — String — language code (e.g. `de_DE`, `en_US`)

## `public String get(MessageKey key)`

Returns the message for the given key in the default language

* **Parameters:**
   * `key` — MessageKey
* **Returns:** String — translated message or `§c[MISSING: key]` if not found

## `public String get(MessageKey key, Object... args)`

Returns the formatted message for the given key in the default language

* **Parameters:**
   * `key` — MessageKey
   * `args` — Object... — format arguments applied via `String.format`
* **Returns:** String — formatted and translated message or `§c[MISSING: key]` if not found

## `public String getWithPrefix(MessageKey key)`

Returns the message for the given key with the plugin prefix prepended

* **Parameters:**
   * `key` — MessageKey
* **Returns:** String — prefixed translated message

## `public String getWithPrefix(MessageKey key, Object... args)`

Returns the formatted message for the given key with the plugin prefix prepended

* **Parameters:**
   * `key` — MessageKey
   * `args` — Object... — format arguments applied via `String.format`
* **Returns:** String — prefixed and formatted translated message