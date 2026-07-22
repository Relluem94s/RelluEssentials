package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillsEntryTest {

    private SkillsEntry skillsEntry;

    @BeforeEach
    void setUp() {
        skillsEntry = new SkillsEntry();
    }

    @Test
    void shouldSetAndGetId() {
        skillsEntry.setId(1);
        assertEquals(1, skillsEntry.getId());
    }

    @Test
    void shouldSetAndGetName() {
        skillsEntry.setName("mining");
        assertEquals("mining", skillsEntry.getName());
    }

    @Test
    void shouldSetAndGetDisplayName() {
        skillsEntry.setDisplayName("Mining");
        assertEquals("Mining", skillsEntry.getDisplayName());
    }

    @Test
    void shouldSetAndGetMaxLevel() {
        skillsEntry.setMaxLevel(100);
        assertEquals(100, skillsEntry.getMaxLevel());
    }
}