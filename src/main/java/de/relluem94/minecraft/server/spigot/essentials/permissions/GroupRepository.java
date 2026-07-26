package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;

import java.util.List;
public interface GroupRepository {
    void insertGroup(GroupEntry groupEntry);
    List<GroupEntry> getGroups();
    void addAllGroups(List<GroupEntry> groups);
}