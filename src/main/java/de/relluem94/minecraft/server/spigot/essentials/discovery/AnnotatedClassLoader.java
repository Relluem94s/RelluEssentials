package de.relluem94.minecraft.server.spigot.essentials.discovery;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for discovering and instantiating annotated classes from a given package.
 *
 * <p>Provides static factory methods to load {@link ListenerConstruct} and
 * {@link CommandConstruct} implementations by scanning a package for classes
 * annotated with {@link ListenerName} or {@link CommandName} respectively.
 * </p>
 *
 * @author relluem94
 */
public class AnnotatedClassLoader {

  private AnnotatedClassLoader() {}

  /**
   * Loads all {@link ListenerConstruct} implementations from the given package.
   *
   * <p>Scans the specified package for classes annotated with {@link ListenerName}
   * and instantiates each discovered class.
   * </p>
   *
   * @param packageName the fully qualified package name to scan for annotated listener classes
   * @param classLoader the class loader used to discover classes within the package
   * @return a list of instantiated {@link ListenerConstruct} implementations
   */
  public static List<ListenerConstruct> loadListeners(String packageName, ClassLoader classLoader) {
    return load(packageName, ListenerName.class, ListenerConstruct.class, classLoader);
  }

  /**
   * Loads all {@link CommandConstruct} implementations from the given package.
   *
   * <p>Scans the specified package for classes annotated with {@link CommandName}
   * and instantiates each discovered class.
   * </p>
   *
   * @param packageName the fully qualified package name to scan for annotated command classes
   * @param classLoader the class loader used to discover classes within the package
   * @return a list of instantiated {@link CommandConstruct} implementations
   */
  public static List<CommandConstruct> loadCommands(String packageName, ClassLoader classLoader) {
    return load(packageName, CommandName.class, CommandConstruct.class, classLoader);
  }

  private static <T> List<T> load(
      String packageName,
      Class<? extends Annotation> annotation,
      Class<T> targetType,
      ClassLoader classLoader
  ) {
    return ClassDiscoveryHelper.findAnnotatedClasses(
            packageName,
            annotation,
            targetType,
            classLoader
        )
        .stream()
        .map(clazz -> instantiate(clazz, targetType))
        .flatMap(Optional::stream)
        .toList();
  }

  private static <T> Optional<T> instantiate(Class<? extends T> clazz, Class<T> targetType) {
    try {
      return Optional.of(clazz.getDeclaredConstructor().newInstance());
    } catch (Exception e) {
      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          "Failed to instantiate " + targetType.getSimpleName() + ": " + clazz.getSimpleName());
      return Optional.empty();
    }
  }
}