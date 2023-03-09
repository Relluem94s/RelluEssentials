package de.relluem94.minecraft.server.spigot.essentials.managers;

import java.util.UUID;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;

public class SudoManager implements IDisable {

    @Override
    public void disable() {
        for(UUID uuid: RelluEssentials.sudoers.keySet()){
            Sudo.exitSudo(Bukkit.getPlayer(uuid));
        }
    }
}