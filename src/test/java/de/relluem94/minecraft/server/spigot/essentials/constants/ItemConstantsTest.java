package de.relluem94.minecraft.server.spigot.essentials.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemConstantsTest {
    @Test
    void pluginEolIsNotNull() {
        assertNotNull(ItemConstants.PLUGIN_ITEM_DUMMY);
    }
}