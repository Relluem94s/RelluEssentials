package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionLockEntry;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProtectionMapperTest {

    @Test
    void constructor_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> {
            var constructor = ProtectionMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void mapProtection_withNullFlagsAndNullRights_returnsEmptyJsonObjects() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt(FIELD_ID)).thenReturn(1);
        when(rs.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_CREATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_UPDATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_UPDATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_DELETED)).thenReturn(null);
        when(rs.getInt(FIELD_DELETEDBY)).thenReturn(0);
        when(rs.getString(FIELD_FLAGS)).thenReturn(null);
        when(rs.getString(FIELD_RIGHTS)).thenReturn(null);
        when(rs.getString(FIELD_MATERIAL_NAME)).thenReturn("CHEST");

        ProtectionEntry result = ProtectionMapper.mapProtection(rs);

        assertNotNull(result.getFlags());
        assertTrue(result.getFlags().isEmpty());
        assertNotNull(result.getRights());
        assertTrue(result.getRights().isEmpty());
    }

    @Test
    void mapProtection_withNonNullFlags_returnsParsedFlagsJson() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt(FIELD_ID)).thenReturn(1);
        when(rs.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_CREATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_UPDATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_UPDATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_DELETED)).thenReturn(null);
        when(rs.getInt(FIELD_DELETEDBY)).thenReturn(0);
        when(rs.getString(FIELD_FLAGS)).thenReturn("{\"hopperEnabled\":true}");
        when(rs.getString(FIELD_RIGHTS)).thenReturn(null);
        when(rs.getString(FIELD_MATERIAL_NAME)).thenReturn("CHEST");

        ProtectionEntry result = ProtectionMapper.mapProtection(rs);

        assertNotNull(result.getFlags());
        assertTrue(result.getFlags().has("hopperEnabled"));
        assertTrue(result.getFlags().getBoolean("hopperEnabled"));
    }

    @Test
    void mapProtection_withNonNullRights_returnsParsedRightsJson() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt(FIELD_ID)).thenReturn(1);
        when(rs.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_CREATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_UPDATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_UPDATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_DELETED)).thenReturn(null);
        when(rs.getInt(FIELD_DELETEDBY)).thenReturn(0);
        when(rs.getString(FIELD_FLAGS)).thenReturn(null);
        when(rs.getString(FIELD_RIGHTS)).thenReturn("{\"player123\":\"READ\"}");
        when(rs.getString(FIELD_MATERIAL_NAME)).thenReturn("CHEST");

        ProtectionEntry result = ProtectionMapper.mapProtection(rs);

        assertNotNull(result.getRights());
        assertTrue(result.getRights().has("player123"));
        assertEquals("READ", result.getRights().getString("player123"));
    }

    @Test
    void mapProtection_withNonNullFlagsAndNonNullRights_returnsBothParsed() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt(FIELD_ID)).thenReturn(1);
        when(rs.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_CREATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_UPDATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_UPDATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_DELETED)).thenReturn(null);
        when(rs.getInt(FIELD_DELETEDBY)).thenReturn(0);
        when(rs.getString(FIELD_FLAGS)).thenReturn("{\"hopperEnabled\":false}");
        when(rs.getString(FIELD_RIGHTS)).thenReturn("{\"player456\":\"WRITE\"}");
        when(rs.getString(FIELD_MATERIAL_NAME)).thenReturn("CHEST");

        ProtectionEntry result = ProtectionMapper.mapProtection(rs);

        assertNotNull(result.getFlags());
        assertFalse(result.getFlags().getBoolean("hopperEnabled"));
        assertNotNull(result.getRights());
        assertEquals("WRITE", result.getRights().getString("player456"));
    }

    @Test
    void mapProtectionLock_withValidMaterial_returnsMappedEntry() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt(FIELD_ID)).thenReturn(1);
        when(rs.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_CREATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_UPDATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_UPDATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_DELETED)).thenReturn(null);
        when(rs.getInt(FIELD_DELETEDBY)).thenReturn(0);
        when(rs.getString(FIELD_VALUE)).thenReturn("CHEST");

        ProtectionLockEntry result = ProtectionMapper.mapProtectionLock(rs);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(Material.CHEST, result.getValue());
    }

    @Test
    void mapProtectionLock_withInvalidMaterial_returnsNullMaterial() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt(FIELD_ID)).thenReturn(2);
        when(rs.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_CREATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_UPDATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_UPDATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_DELETED)).thenReturn(null);
        when(rs.getInt(FIELD_DELETEDBY)).thenReturn(0);
        when(rs.getString(FIELD_VALUE)).thenReturn("INVALID_MATERIAL");

        ProtectionLockEntry result = ProtectionMapper.mapProtectionLock(rs);

        assertNotNull(result);
        assertNull(result.getValue());
    }

    @Test
    void mapProtectionLock_withNullMaterial_returnsNullMaterial() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt(FIELD_ID)).thenReturn(3);
        when(rs.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_CREATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_UPDATED)).thenReturn("2024-01-01");
        when(rs.getInt(FIELD_UPDATEDBY)).thenReturn(1);
        when(rs.getString(FIELD_DELETED)).thenReturn(null);
        when(rs.getInt(FIELD_DELETEDBY)).thenReturn(0);
        when(rs.getString(FIELD_VALUE)).thenReturn(null);

        ProtectionLockEntry result = ProtectionMapper.mapProtectionLock(rs);

        assertNotNull(result);
        assertNull(result.getValue());
    }
}