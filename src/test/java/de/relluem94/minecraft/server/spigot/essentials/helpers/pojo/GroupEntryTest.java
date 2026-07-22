package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GroupEntryTest {

    @Test
    void shouldCreateGroupEntryWithDefaultConstructor() {
        GroupEntry groupEntry = new GroupEntry();
        assertEquals(0, groupEntry.getId());
        assertNull(groupEntry.getName());
        assertNull(groupEntry.getPrefix());
    }

    @Test
    void shouldCreateGroupEntryWithParameterizedConstructor() {
        GroupEntry groupEntry = new GroupEntry(1, "Admin", "[A]");
        assertEquals(1, groupEntry.getId());
        assertEquals("Admin", groupEntry.getName());
        assertEquals("[A]", groupEntry.getPrefix());
    }

    @Test
    void shouldSetAndGetId() {
        GroupEntry groupEntry = new GroupEntry();
        groupEntry.setId(10);
        assertEquals(10, groupEntry.getId());
    }

    @Test
    void shouldSetAndGetName() {
        GroupEntry groupEntry = new GroupEntry();
        groupEntry.setName("Moderator");
        assertEquals("Moderator", groupEntry.getName());
    }

    @Test
    void shouldSetAndGetPrefix() {
        GroupEntry groupEntry = new GroupEntry();
        groupEntry.setPrefix("[M]");
        assertEquals("[M]", groupEntry.getPrefix());
    }

    @Test
    void shouldAllowNullName() {
        GroupEntry groupEntry = new GroupEntry(1, null, "[A]");
        assertNull(groupEntry.getName());
    }

    @Test
    void shouldAllowNullPrefix() {
        GroupEntry groupEntry = new GroupEntry(1, "Admin", null);
        assertNull(groupEntry.getPrefix());
    }

    @Test
    void shouldOverwriteExistingValues() {
        GroupEntry groupEntry = new GroupEntry(1, "Admin", "[A]");
        groupEntry.setId(2);
        groupEntry.setName("Moderator");
        groupEntry.setPrefix("[M]");

        assertEquals(2, groupEntry.getId());
        assertEquals("Moderator", groupEntry.getName());
        assertEquals("[M]", groupEntry.getPrefix());
    }
}