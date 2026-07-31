package de.relluem94.minecraft.server.spigot.essentials.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupRegistryTest {

    private static final GroupEntry USER_GROUP = new GroupEntry(1, "user", "§8");
    private static final GroupEntry MOD_GROUP = new GroupEntry(2, "mod", "§9");
    private static final GroupEntry ADMIN_GROUP = new GroupEntry(3, "admin", "§c");
    private List<GroupEntry> mutableGroupList;

    @BeforeEach
    void injectTestGroups() {
        mutableGroupList = new ArrayList<>(List.of(USER_GROUP, MOD_GROUP, ADMIN_GROUP));
        GroupRegistry.injectGroupEntries(mutableGroupList);
    }

    @AfterEach
    void resetInjectedGroups() {
        GroupRegistry.injectGroupEntries(null);
        GroupService.injectGroupRepository(null);
    }


    @Test
    void getGroupByNameReturnsCorrectGroup() {
        GroupEntry result = GroupRegistry.getGroup("mod");
        assertEquals(MOD_GROUP, result);
    }

    @Test
    void getGroupByNameIsCaseInsensitive() {
        GroupEntry result = GroupRegistry.getGroup("ADMIN");
        assertEquals(ADMIN_GROUP, result);
    }

    @Test
    void getGroupByNameFallsBackToUserGroupWhenNotFound() {
        GroupEntry result = GroupRegistry.getGroup("nonexistent");
        assertEquals(USER_GROUP, result);
    }

    @Test
    void getGroupByNameReturnsFallbackWhenNoUserGroupExists() {
        GroupRegistry.injectGroupEntries(List.of(ADMIN_GROUP));
        GroupEntry result = GroupRegistry.getGroup("nonexistent");
        assertEquals("user", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    void getGroupByIdReturnsCorrectGroup() {
        GroupEntry result = GroupRegistry.getGroup(2);
        assertEquals(MOD_GROUP, result);
    }

    @Test
    void getGroupByIdReturnsNullWhenNotFound() {
        GroupEntry result = GroupRegistry.getGroup(99);
        assertNull(result);
    }

    @Test
    void groupExistsReturnsTrueForExistingGroup() {
        assertTrue(GroupRegistry.groupExists("admin"));
    }

    @Test
    void groupExistsReturnsFalseForMissingGroup() {
        assertFalse(GroupRegistry.groupExists("superadmin"));
    }

    @Test
    void groupExistsIsCaseInsensitive() {
        assertTrue(GroupRegistry.groupExists("MOD"));
    }

    @Test
    void checkForUtilityClass(){
        Assertions.assertThrows(IllegalStateException.class, GroupService::new);
    }

    @Test
    void addGroupReturnsTrueAndInsertsNewGroup() {
        GroupEntry newGroup = new GroupEntry(4, "vip", "§6");
        List<GroupEntry> insertedGroups = new ArrayList<>();

        GroupService.injectGroupRepository(new GroupRepository() {
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

        boolean result = GroupService.addGroup(newGroup);

        assertTrue(result);
        assertTrue(insertedGroups.contains(newGroup));
    }

    @Test
    void addGroupReturnsFalseWhenGroupAlreadyExists() {
        List<GroupEntry> insertedGroups = new ArrayList<>();

        GroupService.injectGroupRepository(new GroupRepository() {
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

        boolean result = GroupService.addGroup(ADMIN_GROUP);

        assertFalse(result);
        assertTrue(insertedGroups.isEmpty());
    }
}