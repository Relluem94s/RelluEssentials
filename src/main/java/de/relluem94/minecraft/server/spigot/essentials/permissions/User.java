package de.relluem94.minecraft.server.spigot.essentials.permissions;

import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import de.relluem94.minecraft.server.spigot.essentials.skills.RepairSkill;
import de.relluem94.minecraft.server.spigot.essentials.skills.SalvageSkill;
import de.relluem94.minecraft.server.spigot.essentials.skills.TreeFellerSkill;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.users;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class User {

    private final Player p;

    public RepairSkill repair = new RepairSkill(RelluEssentials.board);
    public SalvageSkill salvage = new SalvageSkill(RelluEssentials.board);
    public TreeFellerSkill treeFeller = new TreeFellerSkill(RelluEssentials.board);

    public User(Player p) {
        this.p = p;
        users.add(this);
    }

    public Player getPlayer() {
        return p;
    }

    public void setGroup(GroupEntry g) {
        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
        pe.setGroup(g);
        dBH.updatePlayer(pe);
        p.setCustomName(g.getPrefix() + getCustomName(p));
        p.setPlayerListName(p.getCustomName());
        p.setScoreboard(RelluEssentials.board);
    }

    public static GroupEntry getGroup(Player p) {
        PlayerEntry pe = playerEntryList.get(p.getUniqueId());

        if (pe != null) {
            return pe.getGroup();
        } else {
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
        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
        if (pe.getCustomName() != null) {
            name = pe.getCustomName();
        } else {
            name = p.getName();
        }

        return name;
    }
}