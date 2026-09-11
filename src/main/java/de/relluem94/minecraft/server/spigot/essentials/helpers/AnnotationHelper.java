package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for resolving annotation metadata from command classes at runtime.
 *
 * <p>Uses reflection-based class loading to avoid compile-time dependencies
 * on annotation types, allowing dynamic resolution of command metadata.</p>
 *
 * @author rellu
 */
public class AnnotationHelper {

  /**
   * Resolves the command name from a {@code @CommandName} annotation present on the given class.
   *
   * <p>The annotation is loaded dynamically via the class loader of the provided class
   * to avoid a hard compile-time dependency. If the annotation is absent or any
   * reflective operation fails, {@code null} is returned.</p>
   *
   * @param <T>   the type of {@link CommandConstruct} to inspect
   * @param clazz the class to inspect for the {@code @CommandName} annotation
   * @return the command name defined by the annotation, or {@code null} if the annotation
   *         is not present or cannot be resolved
   */
  @SuppressWarnings("unchecked")
  public static <T extends CommandConstruct> @Nullable String getCommandName(
      @NotNull Class<T> clazz) {
    try {
      Class<?> annotationClass = clazz.getClassLoader()
          .loadClass("de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName");
      Object annotation = clazz.getAnnotation(
          (Class<? extends java.lang.annotation.Annotation>) annotationClass);
      if (annotation == null) {
        return null;
      }
      return (String) annotationClass.getMethod("value").invoke(annotation);
    } catch (Exception e) {
      return null;
    }
  }
}
