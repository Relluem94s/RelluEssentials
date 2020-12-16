package main.java.de.relluem94.minecraft.server.spigot.essentials.permissions;

import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups.UserGroup;
import main.java.de.relluem94.minecraft.server.spigot.essentials.skills.RepairSkill;
import main.java.de.relluem94.minecraft.server.spigot.essentials.skills.SalvageSkill;
import main.java.de.relluem94.minecraft.server.spigot.essentials.skills.TreeFellerSkill;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.users;

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
		players.getConfig().set("player." + p.getUniqueId() + ".group" , g.getName());
		p.setCustomName(g.getPrefix() +  getCustomName(p));
		p.setScoreboard(RelluEssentials.board);
		g.getTeam().addEntry(p.getName());
	}
	
	public static Group getGroup(Player p) {
		if(players.getConfig().getString("player." + p.getUniqueId() + ".group") != null) {
			return  Group.getGroupFromName(players.getConfig().getString("player." + p.getUniqueId() + ".group"));
		}
		else {
			return new UserGroup();
		}
	}
	
	public static User getUserByPlayerName(String name) {
		for(User u: users) {
			if(u.getPlayer().getName().equalsIgnoreCase(name)) {
				return u;
			}
		}
		return null;
	}
	
	public static void removeUser(String name) {
		users.remove(getUserByPlayerName(name));
	}
	
	private String getCustomName(Player p) {
		String name = "";
		if(players.getConfig().get("player." + p.getUniqueId() + ".customname") != null) {
			name += players.getConfig().get("player." + p.getUniqueId() + ".customname");
		}
		else {
			name += p.getName();
		}
		
		return name;
	}
}
