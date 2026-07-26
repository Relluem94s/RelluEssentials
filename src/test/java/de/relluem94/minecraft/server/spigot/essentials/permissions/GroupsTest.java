package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupsTest {

    private static final GroupEntry USER_GROUP = new GroupEntry(1, "user", "§8");
    private static final GroupEntry MOD_GROUP = new GroupEntry(2, "mod", "§9");
    private static final GroupEntry ADMIN_GROUP = new GroupEntry(3, "admin", "§c");
    private List<GroupEntry> mutableGroupList;

    @BeforeEach
    void injectTestGroups() {
        mutableGroupList = new ArrayList<>(List.of(USER_GROUP, MOD_GROUP, ADMIN_GROUP));
        Groups.injectGroupEntries(mutableGroupList);
    }

    @AfterEach
    void resetInjectedGroups() {
        Groups.injectGroupEntries(null);
        Groups.injectGroupRepository(null);
    }


    @Test
    void getGroupByNameReturnsCorrectGroup() {
        GroupEntry result = Groups.getGroup("mod");
        assertEquals(MOD_GROUP, result);
    }

    @Test
    void getGroupByNameIsCaseInsensitive() {
        GroupEntry result = Groups.getGroup("ADMIN");
        assertEquals(ADMIN_GROUP, result);
    }

    @Test
    void getGroupByNameFallsBackToUserGroupWhenNotFound() {
        GroupEntry result = Groups.getGroup("nonexistent");
        assertEquals(USER_GROUP, result);
    }

    @Test
    void getGroupByNameReturnsFallbackWhenNoUserGroupExists() {
        Groups.injectGroupEntries(List.of(ADMIN_GROUP));
        GroupEntry result = Groups.getGroup("nonexistent");
        assertEquals("user", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    void getGroupByIdReturnsCorrectGroup() {
        GroupEntry result = Groups.getGroup(2);
        assertEquals(MOD_GROUP, result);
    }

    @Test
    void getGroupByIdReturnsNullWhenNotFound() {
        GroupEntry result = Groups.getGroup(99);
        assertNull(result);
    }

    @Test
    void groupExistsReturnsTrueForExistingGroup() {
        assertTrue(Groups.groupExists("admin"));
    }

    @Test
    void groupExistsReturnsFalseForMissingGroup() {
        assertFalse(Groups.groupExists("superadmin"));
    }

    @Test
    void groupExistsIsCaseInsensitive() {
        assertTrue(Groups.groupExists("MOD"));
    }

    @Test
    void checkForUtilityClass(){
        Assertions.assertThrows(IllegalStateException.class, Groups::new);
    }

    @Test
    void addGroupReturnsTrueAndInsertsNewGroup() {
        GroupEntry newGroup = new GroupEntry(4, "vip", "§6");
        List<GroupEntry> insertedGroups = new ArrayList<>();

        Groups.injectGroupRepository(new GroupRepository() {
            @Override
            public void insertGroup(GroupEntry groupEntry) {
                insertedGroups.add(groupEntry);
            }

            @Override
            public List<GroupEntry> getGroups() {
                return List.of(newGroup);
            }

            @Override
            public void addAllGroups(List<GroupEntry> groups) {
                mutableGroupList.addAll(groups);
            }
        });

        boolean result = Groups.addGroup(newGroup);

        assertTrue(result);
        assertTrue(insertedGroups.contains(newGroup));
    }

    @Test
    void addGroupReturnsFalseWhenGroupAlreadyExists() {
        List<GroupEntry> insertedGroups = new ArrayList<>();

        Groups.injectGroupRepository(new GroupRepository() {
            @Override
            public void insertGroup(GroupEntry groupEntry) {
                insertedGroups.add(groupEntry);
            }

            @Override
            public List<GroupEntry> getGroups() {
                return List.of();
            }

            @Override
            public void addAllGroups(List<GroupEntry> groups) {}
        });

        boolean result = Groups.addGroup(ADMIN_GROUP);

        assertFalse(result);
        assertTrue(insertedGroups.isEmpty());
    }
}