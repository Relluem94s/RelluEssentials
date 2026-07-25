package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import org.bukkit.entity.Player;

public interface SubCommand {
    void execute(Player player, String[] args);
    boolean matches(String[] args);
}
