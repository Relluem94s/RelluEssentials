package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.groupEntryList;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;

/**
 *
 * @author rellu
 */
public class Groups {

    private Groups() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

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

            RelluEssentials.getInstance().getDatabaseHelper().insertGroup(groupEntry);
            groupEntryList.addAll(RelluEssentials.getInstance().getDatabaseHelper().getGroups());
            return true;
        } else {
            return false;
        }
    }
}