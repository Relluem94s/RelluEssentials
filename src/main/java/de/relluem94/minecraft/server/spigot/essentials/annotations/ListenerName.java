package de.relluem94.minecraft.server.spigot.essentials.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to assign a human-readable name to a listener class.
 *
 * <p>The assigned name is used to identify the listener at runtime,
 * for example for logging or registration purposes.
 *
 * <p>This annotation must be applied at the class level and is retained at runtime.
 *
 * @author rellu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ListenerName {
  /**
   * Returns the human-readable name of the annotated listener.
   *
   * @return the name of the listener
   */
  String value();
}