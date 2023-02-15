package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.groupEntryList;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;

/**
 *
 * @author rellu
 */
public class Groups {

    /**
     * Returns a GroupEntry if a Group is found by name<br>
     * Returns null if no Group is found
     *
     * @param name String
     * @return GroupEntry
     */
    public static GroupEntry getGroup(String name) {
        for (GroupEntry ge : RelluEssentials.groupEntryList) {
            if (ge.getName().equalsIgnoreCase(name)) {
                return ge;
            }
        }
        return null;
    }

    /**
     * Returns a GroupEntry if a Group is found by id<br>
     * Returns null if no Group is found
     *
     * @param id int
     * @return GroupEntry
     */
    public static GroupEntry getGroup(int id) {
        for (GroupEntry ge : RelluEssentials.groupEntryList) {
            if (ge.getId() == id) {
                return ge;
            }
        }
        return null;
    }

    /**
     * Returns true if GroupEntry is in Database else false
     *
     * @param groupEntry GroupEntry
     * @return boolean
     */
    public static boolean addGroup(GroupEntry groupEntry) {
        if (getGroup(groupEntry.getName()) == null) {
            dBH.insertGroup(groupEntry);
            groupEntryList.addAll(dBH.getGroups());
            return true;
        } else {
            return false;
        }
    }
}