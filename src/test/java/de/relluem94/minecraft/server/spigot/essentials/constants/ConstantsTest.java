package de.relluem94.minecraft.server.spigot.essentials.constants;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

class ConstantsTest {

  @Test
  void constructorShouldThrowIllegalStateException() throws NoSuchMethodException {
    Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    assertThrows(InvocationTargetException.class, constructor::newInstance,
        "Constructor should throw IllegalStateException when accessed via reflection");
  }

  @Test
  void pluginEolIsNotNull() {
    assertNotNull(Constants.PLUGIN_EOL);
  }
}