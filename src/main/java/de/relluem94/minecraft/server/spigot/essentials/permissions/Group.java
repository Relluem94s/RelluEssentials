package main.java.de.relluem94.minecraft.server.spigot.essentials.permissions;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups.AdminGroup;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups.ModGroup;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups.UserGroup;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups.VipGroup;


public class Group {
	
	private final String name;
	private final long group_id;
	private final String prefix;
	private Team team;
	
	public Group(String name, long group_id, String prefix) {
		this.name = name;
		this.group_id = group_id;
		this.prefix = prefix;
		addGroup();
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return group_id;
	}

	public String getPrefix() {
		return prefix;
	}
	
	public static Group getGroupFromName(String groupName) {
		Group g;
		switch(groupName.toLowerCase()) {
			case "user": g = new UserGroup(); break;
			case "vip": g = new VipGroup(); break;
			case "mod": g = new ModGroup(); break;
			case "admin": g = new AdminGroup(); break;
			default: g = new UserGroup(); break;
		}
		return g;
	}
	
	private void addGroup() {
		if(RelluEssentials.board.getTeam(name) == null) {
			team = RelluEssentials.board.registerNewTeam(name);
			ChatColor cc = ChatColor.getByChar(prefix.charAt(1));
			team.setColor(cc);
		}
		else {
			team = RelluEssentials.board.getTeam(name);
		}
	}
	
	public Team getTeam() {
		return team;
	}
}
