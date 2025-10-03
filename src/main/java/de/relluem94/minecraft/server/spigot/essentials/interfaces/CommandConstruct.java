package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public interface CommandConstruct extends CommandExecutor, TabCompleter {
    CommandsEnum[] getCommands();
}
