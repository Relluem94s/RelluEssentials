package de.relluem94.minecraft.server.spigot.essentials.permissions;

import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.users;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class User {

    private final Player p;

    public User(Player p) {
        this.p = p;
        users.add(this);
    }

    public Player getPlayer() {
        return p;
    }

    public void done(){

    }

    public void setGroup(GroupEntry g) {
        p.setCustomName(g.getPrefix() + getCustomName(p));
        p.setPlayerListName(p.getCustomName());
        p.setScoreboard(RelluEssentials.board);
    }

    public void updateGroup(GroupEntry g) {
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p.getUniqueId());
        pe.setGroup(g);
        dBH.updatePlayer(pe);
        setGroup(g);
    }

    public static GroupEntry getGroup(Player p) {
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p.getUniqueId());

        if (pe != null) {
            return pe.getGroup();
        }
        else {
            return Groups.getGroup(1);
        }
    }

    public static User getUserByPlayerName(String name) {
        for (User u : users) {
            if ((u.getPlayer().getName() + "").equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }

    public static void removeUser(String name) {
        users.remove(getUserByPlayerName(name));
    }

    private String getCustomName(Player p) {
        String name;
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p.getUniqueId());
        if (pe.getCustomName() != null && pe.getCustomName() != "null") {
            name = pe.getCustomName();
        } 
        else {
            if (pe.getName() != null && pe.getName() != "null") {
                name = pe.getName();
            } 
            else {
                name = p.getName();
            }
        }

        return name;
    }
}