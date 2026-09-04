package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

/**
 * Service responsible for loading and managing localized messages.
 * It supports multiple languages and provides methods to retrieve formatted strings.
 */
public class TranslationService {
  private final JavaPlugin plugin;
  private final Map<String, Properties> languages = new HashMap<>();
  private String defaultLanguage = "de_DE";

  /**
   * Constructs a new TranslationService.
   *
   * @param plugin The plugin instance used to access resources and logger.
   */
  public TranslationService(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Loads all available language property files from the plugin resources.
   */
  public void loadLanguages() {
    String[] availableLanguages = {"de_DE", "en_US", "da_DK"};
    for (String lang : availableLanguages) {
      String fileName = "lang/" + lang + ".properties";
      try (InputStream is = plugin.getResource(fileName)) {
        if (is != null) {
          Properties props = new Properties();
          props.load(new InputStreamReader(is, StandardCharsets.UTF_8));
          languages.put(lang, props);
          plugin.getLogger().info("Loaded language: " + lang);
        } else {
          plugin.getLogger().warning("Language file not found in JAR: " + fileName);
        }
      } catch (IOException e) {
        plugin.getLogger().log(Level.SEVERE, "Could not load language: " + lang, e);
      }
    }
  }

  /**
   * Sets the default language to be used when no specific language is requested.
   *
   * @param language The language code (e.g., "en_US") to set as default.
   */
  public void setDefaultLanguage(String language) {
    if (languages.containsKey(language)) {
      this.defaultLanguage = language;
    } else {
      plugin.getLogger()
          .warning("Language '" + language + "' not found, keeping: " + defaultLanguage);
    }
  }

  /**
   * Retrieves a translated message using the default language.
   *
   * @param key The message key to look up.
   * @return The translated and color-formatted string.
   */
  public String get(MessageKey key) {
    return get(key, defaultLanguage);
  }

  private @NonNull String get(MessageKey key, String language) {
    Properties props = languages.getOrDefault(language, languages.get(defaultLanguage));

    if (props == null) {
      return "§c[MISSING: " + key.getKey() + "]";
    }

    String value = props.getProperty(key.getKey());
    if (value == null) {
      Properties defaultProps = languages.get(defaultLanguage);
      value = defaultProps.getProperty(key.getKey());
    }

    return value != null ? applyColors(value) : "§c[MISSING: " + key.getKey() + "]";
  }

  /**
   * Retrieves a translated message and formats it with the provided arguments.
   *
   * @param key  The message key to look up.
   * @param args The arguments to format into the message string.
   * @return The formatted and color-formatted string.
   */
  public String get(MessageKey key, Object... args) {
    return String.format(get(key), args);
  }

  /**
   * Retrieves a translated message with the plugin's command prefix.
   *
   * @param key The message key to look up.
   * @return The message string prefixed with the plugin command prefix.
   */
  public String getWithPrefix(MessageKey key) {
    return Constants.PLUGIN_FORMS_COMMAND_PREFIX + get(key);
  }

  /**
   * Retrieves a translated message with the plugin's command prefix and formats it.
   *
   * @param key  The message key to look up.
   * @param args The arguments to format into the message string.
   * @return The formatted and prefixed message string.
   */
  public String getWithPrefix(MessageKey key, Object... args) {
    return Constants.PLUGIN_FORMS_COMMAND_PREFIX + get(key, args);
  }

  private @NonNull String applyColors(@NonNull String text) {
    String colored = text.replaceAll("(%\\d*[sd])",
        Constants.PLUGIN_COLOR_COMMAND_ARG + "$1" + Constants.PLUGIN_COLOR_MESSAGE);
    return colored.replace("&", "§");
  }
}
