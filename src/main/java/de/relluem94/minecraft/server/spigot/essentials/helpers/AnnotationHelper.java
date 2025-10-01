package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;

public class AnnotationHelper {
    public static String getCommandName(Class<?> clazz) {
        CommandName annotation = clazz.getAnnotation(CommandName.class);
        return annotation != null ? annotation.value() : null;
    }
}
