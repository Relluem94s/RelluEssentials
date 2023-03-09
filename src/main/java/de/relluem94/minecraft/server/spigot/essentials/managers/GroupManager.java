package de.relluem94.minecraft.server.spigot.essentials.managers;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.User;

public class GroupManager implements IManager{
    
    @Override
    public void manage() {
        List<PlayerEntry> pel = RelluEssentials.dBH.getPlayers();
        pel.forEach(p -> {
            PlayerAPI.putPlayerEntry(UUID.fromString(p.getUUID()), p);
        });

        Bukkit.getOnlinePlayers().forEach(p -> {
            User u = new User(p);
            PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
            u.setGroup(pe.getGroup());
        });
    }
}
