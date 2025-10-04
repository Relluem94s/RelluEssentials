package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotationHelper {
    public static <T extends CommandConstruct> @Nullable String getCommandName(@NotNull Class<T> clazz) {
        CommandName annotation = clazz.getAnnotation(CommandName.class);
        return annotation != null ? annotation.value() : null;
    }
}
