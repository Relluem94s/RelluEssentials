package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;

public class AnnotationHelper {
    public static <T extends CommandConstruct> String getCommandName(Class<T> clazz) {
        CommandName annotation = clazz.getAnnotation(CommandName.class);
        return annotation != null ? annotation.value() : null;
    }
}
