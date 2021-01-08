package de.relluem94.minecraft.server.spigot.essentials.permissions;

import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import de.relluem94.minecraft.server.spigot.essentials.permissions.groups.UserGroup;
import de.relluem94.minecraft.server.spigot.essentials.skills.RepairSkill;
import de.relluem94.minecraft.server.spigot.essentials.skills.SalvageSkill;
import de.relluem94.minecraft.server.spigot.essentials.skills.TreeFellerSkill;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.users;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class User {

    private Player p;
    private Group g;

    public RepairSkill repair = new RepairSkill(RelluEssentials.board);
    public SalvageSkill salvage = new SalvageSkill(RelluEssentials.board);
    public TreeFellerSkill treeFeller = new TreeFellerSkill(RelluEssentials.board);

    public User(Player p, Group g) {
        this.p = p;
        setGroup(g);
        users.add(this);

    }

    public User(Player p, String g) {
        this.p = p;
        setGroup(Group.getGroupFromName(g));
        users.add(this);
    }

    public Group getGroup() {
        return g;
    }

    public Player getPlayer() {
        return p;
    }

    public void setGroup(Group g) {
        this.g = g;
        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
        pe.setGroup(new GroupEntry(g));
        dBH.updatePlayer(pe);
        p.setCustomName(g.getPrefix() + getCustomName(p));
        p.setPlayerListName(p.getCustomName());
        p.setScoreboard(RelluEssentials.board);
        g.getTeam().addEntry(p.getName() + "");
    }

    public static Group getGroup(Player p) {
        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
       
        if (pe != null) {
            return Group.getGroupFromId(pe.getGroup().getId());
        } else {
            return new UserGroup();
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
        if (pe.getCustomname() != null) {
            name = pe.getCustomname();
        } else {
            name = p.getName();
        }

        return name;
    }
}
