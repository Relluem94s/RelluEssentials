package de.relluem94.minecraft.server.spigot.essentials.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class SudoManager implements IDisable {

    public static final Map<UUID, PlayerEntry> sudoers = new HashMap<>();

    @Override
    public void disable() {
        for(UUID uuid: sudoers.keySet()){
            Sudo.exitSudo(Bukkit.getPlayer(uuid));
        }
    }
}