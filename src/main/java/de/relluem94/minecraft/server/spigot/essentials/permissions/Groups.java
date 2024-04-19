package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;

/**
 *
 * @author rellu
 */
public class Groups {

    private Groups() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    /**
     * Returns a GroupEntry if a Group is found by name else User Group is used<br>
     *
     * @param name String
     * @return GroupEntry
     */
    public static GroupEntry getGroup(String name) {
        for (GroupEntry ge : RelluEssentials.getInstance().groupEntryList) {
            if (ge.getName().equalsIgnoreCase(name)) {
                return ge;
            }
        }
        return getGroup("user");
    }

    public static boolean groupExists(String name) {
        for (GroupEntry ge : RelluEssentials.getInstance().groupEntryList) {
            if (ge.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Returns a GroupEntry if a Group is found by id<br>
     * Returns null if no Group is found
     *
     * @param id int
     * @return GroupEntry
     */
    public static GroupEntry getGroup(int id) {
        for (GroupEntry ge : RelluEssentials.getInstance().groupEntryList) {
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
    @SuppressWarnings("unused")
    public static boolean addGroup(GroupEntry groupEntry) {
        if (!groupExists(groupEntry.getName())) {
            RelluEssentials.getInstance().getDatabaseHelper().insertGroup(groupEntry);
            RelluEssentials.getInstance().groupEntryList.addAll(RelluEssentials.getInstance().getDatabaseHelper().getGroups());
            return true;
        } else {
            return false;
        }
    }
}