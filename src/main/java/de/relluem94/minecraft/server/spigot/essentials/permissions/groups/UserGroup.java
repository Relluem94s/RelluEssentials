package de.relluem94.minecraft.server.spigot.essentials.permissions.groups;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;
import de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class UserGroup extends Group {

    public UserGroup() {
        super(Groups.USER.getName(), Groups.USER.getId(), Groups.USER.getPrefix());
        getTeam().setCanSeeFriendlyInvisibles(false);
    }

}
