package de.relluem94.minecraft.server.spigot.essentials.constants;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExceptionConstantsTest {

  @Test
  void constructorThrowsIllegalStateException() throws Exception {
    Constructor<ExceptionConstants> constructor = ExceptionConstants.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }
}