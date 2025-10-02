package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import org.bukkit.command.CommandExecutor;

public interface CommandConstruct extends CommandExecutor {
    CommandsEnum[] getCommands();
}
