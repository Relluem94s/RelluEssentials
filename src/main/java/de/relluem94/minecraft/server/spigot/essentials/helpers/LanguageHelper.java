package de.relluem94.minecraft.server.spigot.essentials.helpers;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

public class LanguageHelper {
        private final JavaPlugin plugin;
        private final Map<String, Properties> languages = new HashMap<>();
        private String defaultLanguage = "de_DE";

        public LanguageHelper(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        public void loadLanguages() {
            String[] availableLanguages = {"de_DE", "en_US"};

            for (String lang : availableLanguages) {
                String fileName = "lang/" + lang + ".properties";

                plugin.saveResource(fileName, false);

                File langFile = new File(plugin.getDataFolder(), fileName);

                Properties props = new Properties();
                try (InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(langFile), StandardCharsets.UTF_8)) {
                    props.load(reader);
                    languages.put(lang, props);
                    plugin.getLogger().info("Loaded language: " + lang);
                } catch (IOException e) {
                    plugin.getLogger().log(Level.WARNING, "Could not load language file: " + fileName, e);
                }
            }

            if (languages.isEmpty()) {
                loadFromJar("en_US");
                loadFromJar("de_DE");
            }
        }

        private void loadFromJar(String lang) {
            String fileName = "lang/" + lang + ".properties";
            try (InputStream is = plugin.getResource(fileName)) {
                if (is != null) {
                    Properties props = new Properties();
                    props.load(new InputStreamReader(is, StandardCharsets.UTF_8));
                    languages.put(lang, props);
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not load fallback language!", e);
            }
        }

        public void setDefaultLanguage(String language) {
            if (languages.containsKey(language)) {
                this.defaultLanguage = language;
            } else {
                plugin.getLogger().warning("Language '" + language + "' not found, keeping: " + defaultLanguage);
            }
        }

        public String get(MessageKey key) {
            return get(key, defaultLanguage);
        }

        public String get(MessageKey key, String language) {
            Properties props = languages.getOrDefault(language, languages.get(defaultLanguage));

            if (props == null) {
                return "§c[MISSING: " + key.getKey() + "]";
            }

            String value = props.getProperty(key.getKey());
            if (value == null) {
                Properties defaultProps = languages.get(defaultLanguage);
                if (defaultProps != null) {
                    value = defaultProps.getProperty(key.getKey());
                }
            }

            return value != null ? applyColors(value) : "§c[MISSING: " + key.getKey() + "]";
        }

        public String get(MessageKey key, Object... args) {
            return String.format(applyColors(get(key)), args);
        }

        public String get(MessageKey key, String language, Object... args) {
            return String.format(get(key, language), args);
        }

        public String getWithPrefix(MessageKey key) {
            return Constants.PLUGIN_FORMS_COMMAND_PREFIX + get(key);
        }

        public String getWithPrefix(MessageKey key, Object... args) {
            return Constants.PLUGIN_FORMS_COMMAND_PREFIX + get(key, args);
        }

        private @NonNull String applyColors(@NonNull String text) {
            String colored = text.replaceAll("(%\\d*[sd])",
                    Constants.PLUGIN_COLOR_COMMAND_ARG + "$1" + Constants.PLUGIN_COLOR_MESSAGE);
            return colored.replace("&", "§");
        }
}