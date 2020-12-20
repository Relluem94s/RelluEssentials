package main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.groups;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Group;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class VipGroup extends Group {

    public VipGroup() {        
        super(Groups.VIP.getName()  , Groups.VIP.getId(), Groups.VIP.getPrefix());
        getTeam().setCanSeeFriendlyInvisibles(false);
    }

}
