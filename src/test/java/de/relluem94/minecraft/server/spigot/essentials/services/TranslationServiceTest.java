package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

  @Mock
  private JavaPlugin plugin;

  private TranslationService translationService;

  private static final String VALID_PROPERTIES_CONTENT = "test.key=Hello World\ntest.format=Hello %s\n";
  private static final String GERMAN_PROPERTIES_CONTENT = "test.key=Hallo Welt\ntest.format=Hallo %s\n";

  @BeforeEach
  void setUp() {
    Logger logger = Logger.getLogger("TestLogger");
    logger.setLevel(Level.OFF);
    when(plugin.getLogger()).thenReturn(logger);
    translationService = new TranslationService(plugin);
  }

  private InputStream toInputStream(String content) {
    return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
  }

  private MessageKey buildMessageKey(String key) {
    MessageKey messageKey = mock(MessageKey.class);
    when(messageKey.getKey()).thenReturn(key);
    return messageKey;
  }

  @Test
  void loadLanguagesLoadsAvailableLanguageFiles() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(GERMAN_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.key");

    assertAll(
        () -> assertEquals("Hallo Welt", translationService.get(key)),
        () -> assertFalse(translationService.get(key).isEmpty())
    );
  }

  @Test
  void loadLanguagesHandlesMissingLanguageFileGracefully() {
    when(plugin.getResource(anyString())).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.key");
    String result = translationService.get(key);

    assertTrue(result.contains("MISSING"));
  }

  @Test
  void getReturnsTranslatedMessageForDefaultLanguage() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(GERMAN_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.key");
    String result = translationService.get(key);

    assertEquals("Hallo Welt", result);
  }

  @Test
  void getReturnsMissingPlaceholderWhenKeyNotFound() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(GERMAN_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("nonexistent.key");
    String result = translationService.get(key);

    assertAll(
        () -> assertTrue(result.contains("MISSING")),
        () -> assertTrue(result.contains("nonexistent.key"))
    );
  }

  @Test
  void getReturnsMissingPlaceholderWhenNoLanguagesLoaded() {
    when(plugin.getResource(anyString())).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.key");
    String result = translationService.get(key);

    assertAll(
        () -> assertTrue(result.contains("MISSING")),
        () -> assertTrue(result.contains("test.key"))
    );
  }

  @Test
  void getWithArgsFormatsMessageCorrectly() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(GERMAN_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.format");
    String result = translationService.get(key, "Welt");

    assertTrue(result.contains("Welt"));
  }

  @Test
  void getWithArgsPropagatesMissingKeyPlaceholder() {
    when(plugin.getResource(anyString())).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("nonexistent.key");
    String result = translationService.get(key, "arg1");

    assertAll(
        () -> assertTrue(result.contains("MISSING")),
        () -> assertTrue(result.contains("nonexistent.key"))
    );
  }

  @Test
  void getWithPrefixReturnsPrefixedMessage() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(GERMAN_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.key");
    String result = translationService.getWithPrefix(key);

    assertAll(
        () -> assertTrue(result.startsWith(Constants.PLUGIN_FORMS_COMMAND_PREFIX)),
        () -> assertTrue(result.contains("Hallo Welt"))
    );
  }

  @Test
  void getWithPrefixAndArgsReturnsPrefixedFormattedMessage() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(GERMAN_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.format");
    String result = translationService.getWithPrefix(key, "Welt");

    assertAll(
        () -> assertTrue(result.startsWith(Constants.PLUGIN_FORMS_COMMAND_PREFIX)),
        () -> assertTrue(result.contains("Welt"))
    );
  }

  @Test
  void setDefaultLanguageSwitchesToValidLanguage() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(GERMAN_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();
    translationService.setDefaultLanguage("en_US");

    MessageKey key = buildMessageKey("test.key");
    String result = translationService.get(key);

    assertEquals("Hello World", result);
  }

  @Test
  void setDefaultLanguageKeepsCurrentLanguageWhenInvalidLanguageProvided() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(GERMAN_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();
    translationService.setDefaultLanguage("xx_XX");

    MessageKey key = buildMessageKey("test.key");
    String result = translationService.get(key);

    assertEquals("Hallo Welt", result);
  }

  @Test
  void applyColorsConvertsAmpersandToSectionSign() {
    String propertiesWithColor = "test.key=&aGreen Text\n";
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(propertiesWithColor));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.key");
    String result = translationService.get(key);

    assertTrue(result.contains("§a"));
  }

  @Test
  void applyColorsWrapsFormatArgumentsWithColorCodes() {
    String propertiesWithFormat = "test.format=Hello %s World\n";
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(
        toInputStream(propertiesWithFormat));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();

    MessageKey key = buildMessageKey("test.format");
    String rawTranslation = translationService.get(key);

    assertAll(
        () -> assertTrue(rawTranslation.contains(Constants.PLUGIN_COLOR_COMMAND_ARG)),
        () -> assertTrue(rawTranslation.contains(Constants.PLUGIN_COLOR_MESSAGE))
    );
  }

  @Test
  void getFallsBackToDefaultLanguageWhenKeyMissingInRequestedLanguage() {
    String germanOnly = "test.key=Hallo Welt\ntest.german.only=Nur Deutsch\n";
    String englishPartial = "test.key=Hello World\n";

    when(plugin.getResource("lang/de_DE.properties")).thenReturn(toInputStream(germanOnly));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(toInputStream(englishPartial));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();
    translationService.setDefaultLanguage("en_US");

    MessageKey key = buildMessageKey("test.german.only");
    String result = translationService.get(key);

    assertTrue(result.contains("MISSING"));
  }

  @Test
  void getReturnsMissingPlaceholderWhenDefaultLanguageNotLoaded() {
    when(plugin.getResource("lang/de_DE.properties")).thenReturn(null);
    when(plugin.getResource("lang/en_US.properties")).thenReturn(
        toInputStream(VALID_PROPERTIES_CONTENT));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();
    translationService.setDefaultLanguage("en_US");

    MessageKey key = buildMessageKey("nonexistent.key");
    String result = translationService.get(key);

    assertAll(
        () -> assertTrue(result.contains("MISSING")),
        () -> assertTrue(result.contains("nonexistent.key"))
    );
  }

  @Test
  void loadLanguagesLogsErrorWhenLanguageFileThrowsIOException() throws IOException {
    InputStream brokenStream = mock(InputStream.class);
    when(brokenStream.read(any(), anyInt(), anyInt())).thenThrow(
        new IOException("Simulated read error"));

    when(plugin.getResource("lang/de_DE.properties")).thenReturn(brokenStream);
    when(plugin.getResource("lang/en_US.properties")).thenReturn(null);
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    Logger spyLogger = spy(Logger.getLogger("TestLogger"));
    when(plugin.getLogger()).thenReturn(spyLogger);

    translationService.loadLanguages();

    verify(spyLogger).log(eq(Level.SEVERE), contains("de_DE"), any(IOException.class));
  }

  @Test
  void getReturnsFallbackValueFromDefaultLanguageWhenKeyMissingInCurrentLanguage() {
    String germanContent = "test.key=Hallo Welt\ntest.german.only=Nur Deutsch\n";
    String englishContent = "test.key=Hello World\n";

    when(plugin.getResource("lang/de_DE.properties")).thenReturn(toInputStream(germanContent));
    when(plugin.getResource("lang/en_US.properties")).thenReturn(toInputStream(englishContent));
    when(plugin.getResource("lang/da_DK.properties")).thenReturn(null);

    translationService.loadLanguages();
    translationService.setDefaultLanguage("de_DE");

    MessageKey key = buildMessageKey("test.german.only");
    String result = translationService.get(key, "en_US");

    assertEquals("Nur Deutsch", result);
  }
}