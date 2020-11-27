package de.relluem94.minecraft.server.spigot.essentials.permissions.groups;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;

public class AdminGroup extends Group{

	public AdminGroup() {
		super("Admin", 8, "§5");
		getTeam().setCanSeeFriendlyInvisibles(true);
	}
}
