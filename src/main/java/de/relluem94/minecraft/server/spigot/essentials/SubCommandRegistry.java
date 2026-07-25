package de.relluem94.minecraft.server.spigot.essentials;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import lombok.NonNull;

import java.util.List;

public class SubCommandRegistry<T extends SubCommand> {

    private final List<T> subCommands;

    public SubCommandRegistry(List<T> subCommands) {
        this.subCommands = subCommands;
    }

    public T find(@NonNull String[] args) {
        return subCommands.stream()
                .filter(subCommand -> subCommand.matches(args))
                .findFirst()
                .orElse(null);
    }
}