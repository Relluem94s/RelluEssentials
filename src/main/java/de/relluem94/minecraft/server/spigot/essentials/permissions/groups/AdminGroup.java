package main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Group;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class AdminGroup extends Group {

    public AdminGroup() {
        super(Groups.ADMIN.getName(), Groups.ADMIN.getId(), Groups.ADMIN.getPrefix());
        getTeam().setCanSeeFriendlyInvisibles(true);
    }
}
