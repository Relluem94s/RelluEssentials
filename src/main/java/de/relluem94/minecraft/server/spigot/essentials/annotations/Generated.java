package de.relluem94.minecraft.server.spigot.essentials.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class or method as generated code.
 *
 * <p>Elements annotated with this annotation are excluded from code coverage analysis
 * and similar quality checks, as their content is not manually written.</p>
 *
 * @author rellu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Generated {}