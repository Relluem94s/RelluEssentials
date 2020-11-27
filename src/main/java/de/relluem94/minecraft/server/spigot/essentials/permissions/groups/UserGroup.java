package main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Group;

public class UserGroup extends Group{

	public UserGroup() {
		super("User", 1, "§8");
		getTeam().setCanSeeFriendlyInvisibles(false);
	}

}
