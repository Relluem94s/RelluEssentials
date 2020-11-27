package de.relluem94.minecraft.server.spigot.essentials.permissions.groups;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;

public class ModGroup extends Group{

	public ModGroup() {
		super("Mod", 4, "§6");
		getTeam().setCanSeeFriendlyInvisibles(true);
	}

}
