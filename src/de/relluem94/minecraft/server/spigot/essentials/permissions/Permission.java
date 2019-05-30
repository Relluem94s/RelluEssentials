package de.relluem94.minecraft.server.spigot.essentials.permissions;

import org.bukkit.entity.Player;

public class Permission {
	public static boolean isAuthorized(Player p, long group) {
		long id = User.getUserByPlayerName(p.getName()).getGroup().getId();
		if(id >= group) {
			return true;
		}
		else {
			return false;
		}
	}
}
