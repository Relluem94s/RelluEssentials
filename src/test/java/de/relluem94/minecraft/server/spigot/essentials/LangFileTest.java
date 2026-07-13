package de.relluem94.minecraft.server.spigot.essentials;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LangFileTest {

    @ParameterizedTest(name = "Checking lang file: {0}")
    @ValueSource(strings = {
            "lang/de_DE.properties",
            "lang/dk_DK.properties",
            "lang/en_US.properties"
    })
    @DisplayName("All MessageKeys should be present in lang files")
    void testLangFileCompleteness(String langFile) throws Exception {
        // Datei laden
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(langFile)) {
            assertNotNull(is, "Lang file not found: " + langFile);
            properties.load(is);
        }

        List<String> missingKeys = new ArrayList<>();
        for (MessageKey key : MessageKey.values()) {
            if (!properties.containsKey(key.getKey())) {
                missingKeys.add(key.getKey());
            }
        }

        assertTrue(missingKeys.isEmpty(),
                "Missing keys in " + langFile + ":\n" + String.join("\n", missingKeys)
        );
    }

}
