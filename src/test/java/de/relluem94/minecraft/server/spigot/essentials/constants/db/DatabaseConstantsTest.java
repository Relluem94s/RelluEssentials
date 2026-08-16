package de.relluem94.minecraft.server.spigot.essentials.constants.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseConstantsTest {

  @Test
  public void constructorThrowsIllegalStateException() throws Exception {
    Constructor<DatabaseConstants> constructor = DatabaseConstants.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    InvocationTargetException thrown = Assertions.assertThrows(
        InvocationTargetException.class,
        () -> constructor.newInstance()
    );

    Assertions.assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }

  @Test
  public void pluginDatabaseNameHasCorrectValue() {
    Assertions.assertEquals("rellu_essentials", DatabaseConstants.PLUGIN_DATABASE_NAME);
  }

  @Test
  public void pluginDatabaseNameIsNotEmpty() {
    Assertions.assertFalse(DatabaseConstants.PLUGIN_DATABASE_NAME.isEmpty());
  }

  @Test
  public void pluginDatabaseNameContainsNoWhitespace() {
    Assertions.assertFalse(DatabaseConstants.PLUGIN_DATABASE_NAME.contains(" "));
  }
}