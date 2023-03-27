package de.relluem94.minecraft.server.spigot.essentials.managers;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class GroupManager implements IEnable{
    
    @Override
    public void enable() {
        List<PlayerEntry> pel = RelluEssentials.getInstance().getDatabaseHelper().getPlayers();
        pel.forEach(p -> RelluEssentials.getInstance().getPlayerAPI().putPlayerEntry(UUID.fromString(p.getUUID()), p));

        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            PlayerHelper.setGroup(p, pe.getGroup());
        });
    }
}
