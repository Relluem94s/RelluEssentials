package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import de.relluem94.minecraft.server.spigot.essentials.SubCommandRegistry;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.Optional;

public interface CommandConstruct extends CommandExecutor, TabCompleter {
    CommandsEnum[] getCommands();

    default Optional<SubCommandRegistry<?>> getSubCommandRegistry() {
        return Optional.empty();
    }
}
