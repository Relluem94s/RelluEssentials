package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.permissions.groups.AdminGroup;
import de.relluem94.minecraft.server.spigot.essentials.permissions.groups.ModGroup;
import de.relluem94.minecraft.server.spigot.essentials.permissions.groups.UserGroup;
import de.relluem94.minecraft.server.spigot.essentials.permissions.groups.VipGroup;

public class Group {
	
	private String name;
	private long group_id;
	private String prefix;
	
	public Group(String name, long group_id, String prefix) {
		this.name = name;
		this.group_id = group_id;
		this.prefix = prefix;
		
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
}
