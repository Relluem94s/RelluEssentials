package de.relluem94.minecraft.server.spigot.essentials.constants;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

class SignConstantsTest {

  @Test
  void constructorShouldThrowIllegalStateException() throws NoSuchMethodException {
    Constructor<SignConstants> constructor = SignConstants.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    assertThrows(InvocationTargetException.class, constructor::newInstance,
        "Constructor should throw IllegalStateException when accessed via reflection");
  }
}