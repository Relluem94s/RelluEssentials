package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Utility class for managing permission groups.
 *
 * <p>Provides static methods to retrieve, check and add permission groups.
 * Groups are stored in a list managed by {@link RelluEssentials} and persisted
 * via the database helper.</p>
 *
 * <p>This class cannot be instantiated.</p>
 *
 * @author rellu
 */
public class Groups {

    private static List<GroupEntry> injectedGroupEntries = null;

    private Groups() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static void injectGroupEntries(List<GroupEntry> groupEntries) {
        injectedGroupEntries = groupEntries;
    }

    private static List<GroupEntry> resolveGroupEntries() {
        if (injectedGroupEntries != null) {
            return injectedGroupEntries;
        }
        return RelluEssentials.getInstance().getGroupEntryList();
    }

    /**
     * Returns a GroupEntry if a Group is found by name else User Group is used<br>
     *
     * @param name String
     * @return GroupEntry
     */
    public static @NotNull GroupEntry getGroup(String name) {
        return resolveGroupEntries().stream()
                .filter(ge -> ge.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> resolveGroupEntries().stream()
                        .filter(ge -> ge.getName().equalsIgnoreCase("user"))
                        .findFirst()
                        .orElse(new GroupEntry(1, "user", "§8")));
    }

    /**
     * Returns {@code true} if a group with the given name exists, {@code false} otherwise.
     *
     * @param name the name of the group to check
     * @return {@code true} if the group exists, {@code false} otherwise
     */
    public static boolean groupExists(String name) {
        return resolveGroupEntries().stream()
                .anyMatch(ge -> ge.getName().equalsIgnoreCase(name));
    }

    /**
     * Returns a GroupEntry if a Group is found by id<br>
     * Returns null if no Group is found
     *
     * @param id int
     * @return GroupEntry
     */
    public static @Nullable GroupEntry getGroup(int id) {
        return resolveGroupEntries().stream()
                .filter(ge -> ge.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns true if GroupEntry is in Database else false
     *
     * @param groupEntry GroupEntry
     * @return boolean
     */
    @SuppressWarnings("unused")
    public static boolean addGroup(@NotNull GroupEntry groupEntry) {
        if (!groupExists(groupEntry.getName())) {
            RelluEssentials.getInstance().getDatabaseHelper().insertGroup(groupEntry);
            RelluEssentials.getInstance().getGroupEntryList().addAll(RelluEssentials.getInstance().getDatabaseHelper().getGroups());
            return true;
        } else {
            return false;
        }
    }
}