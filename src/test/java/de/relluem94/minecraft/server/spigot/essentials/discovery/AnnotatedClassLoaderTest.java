package de.relluem94.minecraft.server.spigot.essentials.discovery;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class AnnotatedClassLoaderTest {

  private static final String LISTENER_FIXTURES_PACKAGE =
      "de.relluem94.minecraft.server.spigot.essentials.discovery.listenerfixtures";

  private static final String COMMAND_FIXTURES_PACKAGE =
      "de.relluem94.minecraft.server.spigot.essentials.discovery.commandfixtures";

  private static final String NONEXISTENT_PACKAGE = "de.nonexistent.package";

  private static final String EMPTY_FIXTURES_PACKAGE =
      "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures";

  @Test
  void privateConstructorThrowsIllegalAccessException() throws Exception {
    Constructor<AnnotatedClassLoader> constructor = AnnotatedClassLoader.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    assertThrows(InvocationTargetException.class, constructor::newInstance);
  }

  @Test
  void privateConstructorCauseIsIllegalStateException() throws Exception {
    Constructor<AnnotatedClassLoader> constructor = AnnotatedClassLoader.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }

  @Test
  void loadListenersReturnsEmptyListWhenPackageDoesNotExist() {
    ClassLoader classLoader = new URLClassLoader(new URL[0], null);

    List<ListenerConstruct> result = AnnotatedClassLoader.loadListeners(NONEXISTENT_PACKAGE, classLoader);

    assertTrue(result.isEmpty());
  }

  @Test
  void loadCommandsReturnsEmptyListWhenPackageDoesNotExist() {
    ClassLoader classLoader = new URLClassLoader(new URL[0], null);

    List<CommandConstruct> result = AnnotatedClassLoader.loadCommands(NONEXISTENT_PACKAGE, classLoader);

    assertTrue(result.isEmpty());
  }

  @Test
  void loadListenersReturnsEmptyListWhenNoAnnotatedClassesPresent() {
    ClassLoader classLoader = AnnotatedClassLoaderTest.class.getClassLoader();

    List<ListenerConstruct> result = AnnotatedClassLoader.loadListeners(EMPTY_FIXTURES_PACKAGE, classLoader);

    assertTrue(result.isEmpty());
  }

  @Test
  void loadCommandsReturnsEmptyListWhenNoAnnotatedClassesPresent() {
    ClassLoader classLoader = AnnotatedClassLoaderTest.class.getClassLoader();

    List<CommandConstruct> result = AnnotatedClassLoader.loadCommands(EMPTY_FIXTURES_PACKAGE, classLoader);

    assertTrue(result.isEmpty());
  }

  @Test
  void loadListenersReturnsInstantiatedListeners() {
    Logger silentLogger = Logger.getLogger(AnnotatedClassLoader.class.getName());
    silentLogger.setLevel(Level.OFF);

    ClassLoader classLoader = AnnotatedClassLoaderTest.class.getClassLoader();

    List<ListenerConstruct> result = AnnotatedClassLoader.loadListeners(LISTENER_FIXTURES_PACKAGE, classLoader);

    assertFalse(result.isEmpty());
    assertTrue(result.stream().allMatch(Objects::nonNull));
  }

  @Test
  void loadCommandsReturnsInstantiatedCommands() {
    Logger silentLogger = Logger.getLogger(AnnotatedClassLoader.class.getName());
    silentLogger.setLevel(Level.OFF);

    ClassLoader classLoader = AnnotatedClassLoaderTest.class.getClassLoader();

    List<CommandConstruct> result = AnnotatedClassLoader.loadCommands(COMMAND_FIXTURES_PACKAGE, classLoader);

    assertFalse(result.isEmpty());
    assertTrue(result.stream().allMatch(Objects::nonNull));
  }

  @Test
  void loadListenersSkipsClassesThatCannotBeInstantiated() {
    Logger silentLogger = Logger.getLogger(AnnotatedClassLoader.class.getName());
    silentLogger.setLevel(Level.OFF);

    ClassLoader classLoader = AnnotatedClassLoaderTest.class.getClassLoader();

    List<ListenerConstruct> result = AnnotatedClassLoader.loadListeners(LISTENER_FIXTURES_PACKAGE, classLoader);

    assertTrue(result.stream().noneMatch(
        l -> l.getClass().getSimpleName().equals("UninstantiableListenerFixture")));
  }

  @Test
  void loadCommandsSkipsClassesThatCannotBeInstantiated() {
    Logger silentLogger = Logger.getLogger(AnnotatedClassLoader.class.getName());
    silentLogger.setLevel(Level.OFF);

    ClassLoader classLoader = AnnotatedClassLoaderTest.class.getClassLoader();

    List<CommandConstruct> result = AnnotatedClassLoader.loadCommands(COMMAND_FIXTURES_PACKAGE, classLoader);

    assertTrue(result.stream().noneMatch(
        c -> c.getClass().getSimpleName().equals("UninstantiableCommandFixture")));
  }
}