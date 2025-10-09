package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotationHelper {
    @SuppressWarnings("unchecked")
    public static <T extends CommandConstruct> @Nullable String getCommandName(@NotNull Class<T> clazz) {
        try {
            Class<?> annotationClass = clazz.getClassLoader().loadClass("de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName");
            Object annotation = clazz.getAnnotation((Class<? extends java.lang.annotation.Annotation>) annotationClass);
            if (annotation == null) return null;
            return (String) annotationClass.getMethod("value").invoke(annotation);
        } catch (Exception e) {
            return null;
        }
    }
}
