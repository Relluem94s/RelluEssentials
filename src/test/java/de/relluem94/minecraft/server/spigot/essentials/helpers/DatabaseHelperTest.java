package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.loader.SqlResourceLoader;
import de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces.IPatchHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DatabaseHelperTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private DataSource dataSourceNoSchema;

    @Mock
    private SqlResourceLoader sqlResourceLoader;

    @Mock
    private IPatchHelper patchHelper;

    @Mock
    private Connection connection;

    @Mock
    private Connection connectionNoSchema;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private PreparedStatement preparedStatementLocation;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSet resultSetLocation;

    @Mock
    private World world;

    @Mock
    private Server server;

    private DatabaseHelper databaseHelper;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        RelluEssentials fakeInstance = mock(RelluEssentials.class);

        Field instanceField = RelluEssentials.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, fakeInstance);

        Field groupListField = RelluEssentials.class.getDeclaredField("groupEntryList");
        groupListField.setAccessible(true);
        groupListField.set(fakeInstance, new ArrayList<>());

        Field locationTypeEntryField = RelluEssentials.class.getDeclaredField("locationTypeEntryList");
        locationTypeEntryField.setAccessible(true);
        locationTypeEntryField.set(fakeInstance, new ArrayList<>());

        databaseHelper = new DatabaseHelper(dataSource, dataSourceNoSchema, sqlResourceLoader);
        databaseHelper.setPatchHelper(patchHelper);
    }



    private void stubBukkitServer() throws NoSuchFieldException, IllegalAccessException {
        Field serverField = Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        serverField.set(null, null);

        when(server.getLogger()).thenReturn(Logger.getLogger("test"));
        when(server.getWorld(anyString())).thenReturn(world);
        Bukkit.setServer(server);
    }


    private void stubConnectionWithResultSet() throws SQLException, FileNotFoundException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(dataSource.getConnection()).thenReturn(connection);
        when(sqlResourceLoader.load(anyString())).thenReturn("SELECT 1");
    }

    private void stubConnectionForExecution() throws SQLException, FileNotFoundException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(sqlResourceLoader.load(anyString())).thenReturn("SELECT 1");
    }

    @Test
    void initCallsPatchHelperWithCurrentDbVersion() throws SQLException, FileNotFoundException {
        when(dataSourceNoSchema.getConnection()).thenReturn(connectionNoSchema);
        when(connectionNoSchema.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        doReturn("SELECT 1").when(sqlResourceLoader).load(anyString());
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(anyString())).thenReturn("some-value");
        when(resultSet.getInt(anyString())).thenReturn(5);

        try (MockedStatic<ChatHelper> _ = mockStatic(ChatHelper.class)) {
            databaseHelper.init();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        verify(patchHelper).applyPatch(5);
    }

    @Test
    void getPluginInformationReturnsVersionMinusOneWhenDatabaseNotReachable() throws SQLException {
        when(dataSourceNoSchema.getConnection()).thenThrow(new SQLException("Connection failed"));

        Logger databaseHelperLogger = Logger.getLogger(DatabaseHelper.class.getName());
        Level originalLevel = databaseHelperLogger.getLevel();
        databaseHelperLogger.setLevel(Level.OFF);

        try (MockedStatic<ChatHelper> _ = mockStatic(ChatHelper.class)) {
            PluginInformationEntry result = databaseHelper.getPluginInformation();
            assertEquals(-1, result.getDbVersion());
        } finally {
            databaseHelperLogger.setLevel(originalLevel);
        }
    }

    @Test
    void getPlayerReturnsNullWhenPlayerNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        PlayerEntry result = databaseHelper.getPlayer("non-existing-uuid");

        assertNull(result);
    }

    @Test
    void getPlayerReturnsMappedPlayerWhenFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(true, false, false, false, false);
        when(resultSet.getString("uuid")).thenReturn("test-uuid");
        when(resultSet.getString("name")).thenReturn("TestPlayer");
        when(resultSet.getString("customname")).thenReturn("Custom");
        when(resultSet.getString("created")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("createdby")).thenReturn(1);
        when(resultSet.getString("updated")).thenReturn("2024-04-03 10:30:56");
        when(resultSet.getInt("updatedby")).thenReturn(1);
        when(resultSet.getString("deleted")).thenReturn(null);
        when(resultSet.getInt("deletedby")).thenReturn(1);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getDouble("purse")).thenReturn(100.0);
        when(resultSet.getBoolean("afk")).thenReturn(false);
        when(resultSet.getBoolean("fly")).thenReturn(false);
        when(resultSet.getInt("group_fk")).thenReturn(1);

        PlayerEntry result = databaseHelper.getPlayer("test-uuid");

        assertNotNull(result);
        assertEquals("test-uuid", result.getUuid());
        assertEquals("TestPlayer", result.getName());
    }

    @Test
    void getWarpsReturnsEmptyListWhenNoWarpsExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getWarps();

        assertTrue(result.isEmpty());
    }

    @Test
    void getGroupsReturnsEmptyListWhenNoGroupsExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getGroups();

        assertTrue(result.isEmpty());
    }

    @Test
    void getProtectionsReturnsEmptyMapWhenNoProtectionsExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getProtections();

        assertTrue(result.isEmpty());
    }

    @Test
    void getLocationByIdReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        LocationEntry result = databaseHelper.getLocation(99);

        assertNull(result);
    }

    @Test
    void getLocationByLocationReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);
        Location location = new Location(world, 1.0, 64.0, 1.0);

        LocationEntry result = databaseHelper.getLocation(location, 1);

        assertNull(result);
    }

    @Test
    void getBankTierReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        BankTierEntry result = databaseHelper.getBankTier(1);

        assertNull(result);
    }

    @Test
    void getBankTiersReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getBankTiers();

        assertTrue(result.isEmpty());
    }

    @Test
    void getPlayerBankAccountReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        BankAccountEntry result = databaseHelper.getPlayerBankAccount(1);

        assertNull(result);
    }

    @Test
    void getBagTypesReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getBagTypes();

        assertTrue(result.isEmpty());
    }

    @Test
    void getBagTypeByIdReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        BagTypeEntry result = databaseHelper.getBagType(1);

        assertNull(result);
    }

    @Test
    void getBagReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        BagEntry result = databaseHelper.getBag(1, 1);

        assertNull(result);
    }

    @Test
    void getBagsReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getBags();

        assertTrue(result.isEmpty());
    }

    @Test
    void getTransactionsToBankFromPlayerReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getTransactionsToBankFromPlayer(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void getLocationsReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getLocations(1, 1);

        assertTrue(result.isEmpty());
    }

    @Test
    void getPlayersReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getPlayers();

        assertTrue(result.isEmpty());
    }

    @Test
    void getCropsReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getCrops();

        assertTrue(result.isEmpty());
    }

    @Test
    void getDropsReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getDrops();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllSettingsReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getAllSettings();

        assertTrue(result.isEmpty());
    }

    @Test
    void getLocationTypesReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getLocationTypes();

        assertTrue(result.isEmpty());
    }

    @Test
    void getNPCsReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getNPCs();

        assertTrue(result.isEmpty());
    }

    @Test
    void getProtectionLocksReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getProtectionLocks();

        assertTrue(result.isEmpty());
    }

    @Test
    void insertPlayerExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        GroupEntry group = new GroupEntry();
        group.setId(1);

        PlayerEntry player = new PlayerEntry();
        player.setCreatedBy(1);
        player.setUuid("test-uuid");
        player.setName("TestPlayer");
        player.setCustomName("Custom");
        player.setGroup(group);

        databaseHelper.insertPlayer(player);

        verify(preparedStatement).execute();
    }

    @Test
    void updatePlayerExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        GroupEntry group = new GroupEntry();
        group.setId(1);

        PlayerEntry player = new PlayerEntry();
        player.setId(1);
        player.setGroup(group);
        player.setAfk(false);
        player.setFlying(false);
        player.setName("TestPlayer");
        player.setCustomName("Custom");
        player.setPurse(100.0);
        player.setUuid("test-uuid");

        databaseHelper.updatePlayer(player);

        verify(preparedStatement).execute();
    }

    @Test
    void insertBankAccountExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        BankTierEntry tier = new BankTierEntry();
        tier.setId(1);

        BankAccountEntry bankAccount = new BankAccountEntry();
        bankAccount.setPlayerId(1);
        bankAccount.setValue(500.0);
        bankAccount.setTier(tier);

        databaseHelper.insertBankAccount(bankAccount);

        verify(preparedStatement).execute();
    }

    @Test
    void deleteLocationExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        LocationEntry locationEntry = new LocationEntry();
        locationEntry.setId(1);
        locationEntry.setPlayerId(1);

        databaseHelper.deleteLocation(locationEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void cleanupLocationsReturnsAffectedRowCount() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        when(preparedStatement.executeUpdate()).thenReturn(3);

        int result = databaseHelper.cleanupLocations();

        assertEquals(3, result);
    }

    @Test
    void cleanupProtectionsReturnsAffectedRowCount() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        when(preparedStatement.executeUpdate()).thenReturn(2);

        int result = databaseHelper.cleanupProtections();

        assertEquals(2, result);
    }

    @Test
    void cleanupLocationsReturnsZeroOnSqlException() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error"));


        Logger databaseHelperLogger = Logger.getLogger(DatabaseHelper.class.getName());
        Level originalLevel = databaseHelperLogger.getLevel();
        databaseHelperLogger.setLevel(Level.OFF);

        try {
            int result = databaseHelper.cleanupLocations();
            assertEquals(0, result);
        } finally {
            databaseHelperLogger.setLevel(originalLevel);
        }
    }

    @Test
    void getPlayerPartnerReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);


        PlayerPartnerEntry result = databaseHelper.getPlayerPartner(1);

        assertNull(result);
    }

    @Test
    void insertPlayerPartnerExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        PlayerPartnerEntry partnerEntry = new PlayerPartnerEntry();
        partnerEntry.setCreatedBy(1);
        partnerEntry.setFirstPartnerId(1);
        partnerEntry.setSecondPartnerId(2);
        partnerEntry.setShareProtections(false);

        databaseHelper.insertPlayerPartner(partnerEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void deletePlayerPartnerExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        PlayerPartnerEntry partnerEntry = new PlayerPartnerEntry();
        partnerEntry.setId(1);
        partnerEntry.setDeletedBy(1);

        databaseHelper.deletePlayerPartner(partnerEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void insertBagExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        databaseHelper.insertBag(1, 42);

        verify(preparedStatement).execute();
    }

    @Test
    void updateBagEntryExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        BagTypeEntry bagType = new BagTypeEntry();
        bagType.setId(1);

        BagEntry bagEntry = new BagEntry();
        bagEntry.setId(1);
        bagEntry.setPlayerId(1);
        bagEntry.setBagType(bagType);

        databaseHelper.updateBagEntry(bagEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void insertGroupExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        GroupEntry groupEntry = new GroupEntry();
        groupEntry.setId(1);
        groupEntry.setName("admin");
        groupEntry.setPrefix("§4[Admin]");

        databaseHelper.insertGroup(groupEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void deleteProtectionExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        LocationEntry locationEntry = new LocationEntry();
        locationEntry.setId(1);
        locationEntry.setPlayerId(1);

        ProtectionEntry protectionEntry = new ProtectionEntry();
        protectionEntry.setId(1);
        protectionEntry.setLocationEntry(locationEntry);

        databaseHelper.deleteProtection(protectionEntry);

        verify(preparedStatement, times(2)).execute();
    }

    @Test
    void getProtectionByLocationReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);
        when(world.getName()).thenReturn("world");
        Location location = new Location(world, 1.0, 64.0, 1.0);

        ProtectionEntry result = databaseHelper.getProtectionByLocation(location);

        assertNull(result);
    }

    @Test
    void updateBankAccountExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        databaseHelper.updateBankAccount(1, 100.0, 400.0, 1);

        verify(preparedStatement).execute();
    }

    @Test
    void addTransactionToBankExecutesPreparedStatementTwice() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        databaseHelper.addTransactionToBank(1, 1, 50.0, 450.0, 1);

        verify(preparedStatement, times(2)).execute();
    }

    @Test
    void getWorldGroupReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        WorldGroupEntry result = databaseHelper.getWorldGroup("non-existing-group");

        assertNull(result);
    }

    @Test
    void insertWorldGroupExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
        worldGroupEntry.setCreatedBy(1);
        worldGroupEntry.setName("survival");

        databaseHelper.insertWorldGroup(worldGroupEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void insertWorldExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();
        WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
        worldGroupEntry.setId(1);

        GroupEntry groupEntry = new GroupEntry();
        groupEntry.setId(1);

        WorldEntry worldEntry = new WorldEntry();
        worldEntry.setCreatedBy(1);
        worldEntry.setName("world_nether");
        worldEntry.setWorldGroupEntry(worldGroupEntry);
        worldEntry.setGroupEntry(groupEntry);

        databaseHelper.insertWorld(worldEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void getWorldGroupInventoryReturnsNullWhenNotFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
        worldGroupEntry.setId(1);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(1);

        WorldGroupInventoryEntry result = databaseHelper.getWorldGroupInventory(playerEntry, worldGroupEntry);

        assertNull(result);
    }

    @Test
    void insertWorldGroupInventoryExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
        worldGroupEntry.setId(1);

        WorldGroupInventoryEntry inventoryEntry = new WorldGroupInventoryEntry();
        inventoryEntry.setPlayerId(1);
        inventoryEntry.setWorldGroupEntry(worldGroupEntry);
        inventoryEntry.setHealth(20.0);
        inventoryEntry.setFoodLevel(20);
        inventoryEntry.setTotalExperience(0);
        inventoryEntry.setInventory(new org.json.JSONObject());

        databaseHelper.insertWorldGroupInventory(inventoryEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void OupdateWorldGroupInventoryExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
        worldGroupEntry.setId(1);

        WorldGroupInventoryEntry inventoryEntry = new WorldGroupInventoryEntry();
        inventoryEntry.setUpdatedBy(1);
        inventoryEntry.setPlayerId(1);
        inventoryEntry.setWorldGroupEntry(worldGroupEntry);
        inventoryEntry.setHealth(20.0);
        inventoryEntry.setFoodLevel(20);
        inventoryEntry.setTotalExperience(0);
        inventoryEntry.setInventory(new org.json.JSONObject());

        databaseHelper.updateWorldGroupInventory(inventoryEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void getWorldGroupsReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getWorldGroups();

        assertTrue(result.isEmpty());
    }

    @Test
    void updatePlayerPartnerExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        PlayerPartnerEntry partnerEntry = new PlayerPartnerEntry();
        partnerEntry.setId(1);
        partnerEntry.setUpdatedBy(1);
        partnerEntry.setShareProtections(true);

        databaseHelper.updatePlayerPartner(partnerEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void updateProtectionRightExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        LocationEntry locationEntry = new LocationEntry();
        locationEntry.setPlayerId(1);

        ProtectionEntry protectionEntry = new ProtectionEntry();
        protectionEntry.setId(1);
        protectionEntry.setLocationEntry(locationEntry);
        protectionEntry.setRights(new org.json.JSONObject());

        databaseHelper.updateProtectionRight(protectionEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void updateProtectionFlagExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        LocationEntry locationEntry = new LocationEntry();
        locationEntry.setPlayerId(1);

        ProtectionEntry protectionEntry = new ProtectionEntry();
        protectionEntry.setId(1);
        protectionEntry.setLocationEntry(locationEntry);
        protectionEntry.setFlags(new org.json.JSONObject());

        databaseHelper.updateProtectionFlag(protectionEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void insertProtectionExecutesPreparedStatement() throws SQLException, FileNotFoundException {
        stubConnectionForExecution();

        LocationEntry locationEntry = new LocationEntry();
        locationEntry.setId(1);
        locationEntry.setPlayerId(1);

        ProtectionEntry protectionEntry = new ProtectionEntry();
        protectionEntry.setLocationEntry(locationEntry);
        protectionEntry.setMaterialName("CHEST");
        protectionEntry.setRights(new org.json.JSONObject());
        protectionEntry.setFlags(new org.json.JSONObject());

        databaseHelper.insertProtection(protectionEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void insertLocationExecutesPreparedStatement() throws SQLException, FileNotFoundException, NoSuchFieldException, IllegalAccessException {
        stubConnectionForExecution();
        stubBukkitServer();
        when(world.getName()).thenReturn("world");

        LocationTypeEntry locationType = new LocationTypeEntry();
        locationType.setId(1);

        Location location = new Location(world, 1.0, 64.0, 1.0);

        LocationEntry locationEntry = new LocationEntry();
        locationEntry.setPlayerId(1);
        locationEntry.setLocation(location);
        locationEntry.setLocationName("home");
        locationEntry.setLocationType(locationType);

        databaseHelper.insertLocation(locationEntry);

        verify(preparedStatement).execute();
    }

    @Test
    void getAllWorldGroupSettingsReturnsEmptyListWhenNoneExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        var result = databaseHelper.getAllWorldGroupSettings();

        assertTrue(result.isEmpty());
    }

    @Test
    void getWorldByGroupReturnsEmptyListWhenNoWorldsExist() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(false);

        WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
        worldGroupEntry.setId(1);

        var result = databaseHelper.getWorldByGroup(worldGroupEntry);

        assertTrue(result.isEmpty());
    }

    @Test
    void getBagReturnsMappedBagWhenFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(true, false, false);
        when(resultSet.getInt("id")).thenReturn(10);
        when(resultSet.getInt("player_fk")).thenReturn(42);
        when(resultSet.getInt("bag_type_fk")).thenReturn(1);
        when(resultSet.getString("created")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("createdby")).thenReturn(1);
        when(resultSet.getString("updated")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("updatedby")).thenReturn(1);
        when(resultSet.getString("deleted")).thenReturn(null);
        when(resultSet.getInt("deletedby")).thenReturn(0);

        BagEntry result = databaseHelper.getBag(1, 42);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals(42, result.getPlayerId());
    }

    @Test
    void getBagsReturnsMappedBagsWhenFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(true, false, false);
        when(resultSet.getInt("id")).thenReturn(5);
        when(resultSet.getInt("player_fk")).thenReturn(7);
        when(resultSet.getInt("bag_type_fk")).thenReturn(2);
        when(resultSet.getString("created")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("createdby")).thenReturn(1);
        when(resultSet.getString("updated")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("updatedby")).thenReturn(1);
        when(resultSet.getString("deleted")).thenReturn(null);
        when(resultSet.getInt("deletedby")).thenReturn(0);

        var result = databaseHelper.getBags();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(5, result.getFirst().getId());
        assertEquals(7, result.getFirst().getPlayerId());
    }

    @Test
    void getPlayersReturnsMappedPlayersWhenFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(true, false, false, false, false);
        when(resultSet.getString("uuid")).thenReturn("test-uuid");
        when(resultSet.getString("name")).thenReturn("TestPlayer");
        when(resultSet.getString("customname")).thenReturn("Custom");
        when(resultSet.getString("created")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("createdby")).thenReturn(1);
        when(resultSet.getString("updated")).thenReturn("2024-04-03 10:30:56");
        when(resultSet.getInt("updatedby")).thenReturn(1);
        when(resultSet.getString("deleted")).thenReturn(null);
        when(resultSet.getInt("deletedby")).thenReturn(1);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getDouble("purse")).thenReturn(100.0);
        when(resultSet.getBoolean("afk")).thenReturn(false);
        when(resultSet.getBoolean("fly")).thenReturn(false);
        when(resultSet.getInt("group_fk")).thenReturn(1);

        var result = databaseHelper.getPlayers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("test-uuid", result.getFirst().getUuid());
        assertEquals("TestPlayer", result.getFirst().getName());
    }

    @Test
    void getProtectionByLocationReturnsMappedProtectionWhenFound() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();
        when(resultSet.next()).thenReturn(true, false, false);
        when(world.getName()).thenReturn("world");

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("material_name")).thenReturn("CHEST");
        when(resultSet.getString("flags")).thenReturn("{}");
        when(resultSet.getString("rights")).thenReturn("{}");
        when(resultSet.getString("created")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("createdby")).thenReturn(1);
        when(resultSet.getString("updated")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("updatedby")).thenReturn(1);
        when(resultSet.getString("deleted")).thenReturn(null);
        when(resultSet.getInt("deletedby")).thenReturn(0);

        Location location = new Location(world, 1.0, 64.0, 1.0);

        ProtectionEntry result = databaseHelper.getProtectionByLocation(location);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("CHEST", result.getMaterialName());
    }



    @Test
    void getProtectionsReturnsMappedProtectionWhenLocationExists() throws SQLException, FileNotFoundException, NoSuchFieldException, IllegalAccessException {
        stubBukkitServer();
        when(dataSource.getConnection()).thenReturn(connection);
        when(sqlResourceLoader.load(anyString())).thenReturn("SELECT 1");

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement)
                .thenReturn(preparedStatementLocation);

        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(preparedStatementLocation.getResultSet()).thenReturn(resultSetLocation);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("location_fk")).thenReturn(1);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("material_name")).thenReturn("CHEST");
        when(resultSet.getString("flags")).thenReturn("{}");
        when(resultSet.getString("rights")).thenReturn("{}");
        when(resultSet.getString("created")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("createdby")).thenReturn(1);
        when(resultSet.getString("updated")).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt("updatedby")).thenReturn(1);
        when(resultSet.getString("deleted")).thenReturn(null);
        when(resultSet.getInt("deletedby")).thenReturn(0);

        when(resultSetLocation.next()).thenReturn(true, false);
        when(resultSetLocation.getFloat("x")).thenReturn(1.0f);
        when(resultSetLocation.getFloat("y")).thenReturn(64.0f);
        when(resultSetLocation.getFloat("z")).thenReturn(1.0f);
        when(resultSetLocation.getFloat("yaw")).thenReturn(0.0f);
        when(resultSetLocation.getFloat("pitch")).thenReturn(0.0f);
        when(resultSetLocation.getString("world")).thenReturn("world");
        when(resultSetLocation.getString("location_name")).thenReturn("home");
        when(resultSetLocation.getInt("player_fk")).thenReturn(1);
        when(resultSetLocation.getInt("id")).thenReturn(1);

        var result = databaseHelper.getProtections();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getProtectionsLogsErrorWhenLocationEntryIsMissing() throws SQLException, FileNotFoundException {
        stubConnectionWithResultSet();

        when(resultSet.next()).thenReturn(true, false, false);
        when(resultSet.getInt("location_fk")).thenReturn(999);
        when(resultSet.getInt("id")).thenReturn(42);

        var result = databaseHelper.getProtections();

        assertTrue(result.isEmpty());
    }
}