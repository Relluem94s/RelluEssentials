package de.relluem94.minecraft.server.spigot.essentials.permissions.groups;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;

public class VipGroup extends Group{

	public VipGroup() {
		super("VIP", 2, "§a");
		getTeam().setCanSeeFriendlyInvisibles(false);
	}

}
