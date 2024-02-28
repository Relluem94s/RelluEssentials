package de.relluem94.minecraft.server.spigot.essentials;

import java.io.OutputStream;
import java.io.PrintStream;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

public class RelluEssentialsTest {

    private static ServerMock server;
    private static RelluEssentials plugin;
    private static DB db;
    private static PlayerMock player1;
    private static PlayerMock player2;
    private static PlayerMock player3;


    @BeforeClass
    public static void setUp() throws ManagedProcessException {
        PrintStream sysOut = System.out;

        PrintStream noOut = new PrintStream(new OutputStream(){
            public void write(int b) {}
        });

        System.setOut(noOut); 

        db = DB.newEmbeddedDB(DatabaseHelper.DB_TEST_PORT);
        db.start();
        System.setOut(sysOut);

        db.createDB(DatabaseConstants.PLUGIN_DATABASE_NAME);
        db.source("de/relluem94/minecraft/server/spigot/essentials/rellu_essentials.sql");

        server = MockBukkit.mock();
        plugin = MockBukkit.load(RelluEssentials.class);

        server.addSimpleWorld(Strings.PLUGIN_WORLD_LOBBY);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD_NETHER);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD_THE_END);

        server.setOfflinePlayers(20);

        player1 = server.addPlayer("PlayerOne");
        player2 = server.addPlayer("PlayerTwo");
        player3 = server.addPlayer("PlayerThree");

        addPlayerToDB(player1, "admin");
        addPlayerToDB(player2, "vip");
        addPlayerToDB(player3, "user");

        server.getScheduler().performTicks(100L);
    }

    /**
     * Only needed due to Could not pass event AsyncPlayerPreLoginEvent to RelluEssentials v3.0
     */
    private static void addPlayerToDB(PlayerMock p, String groupName){
        PlayerEntry pe = new PlayerEntry();
        pe.setUuid(p.getUniqueId().toString());
        pe.setName(p.getName());
        pe.setCustomName(p.getName());
        pe.setGroup(Groups.getGroup(groupName));

        plugin.getDatabaseHelper().insertPlayer(pe);

        pe = plugin.getDatabaseHelper().getPlayer(p.getUniqueId().toString());

        plugin.getPlayerAPI().putPlayerEntry(p.getUniqueId(), pe);
    }

    @AfterClass
    public static void tearDown() throws ManagedProcessException{
        MockBukkit.getMock().getScheduler().shutdown();
        MockBukkit.unmock();
        db.stop();
    }


    @Test
    @DisplayName("Plugin Enabled")
    public void testPluginEnabled(){
        Assert.assertTrue(plugin.isEnabled());

    }

    @Test
    @DisplayName("Plugin Unit Test")
    public void testUnitTest(){
        Assert.assertTrue(plugin.isUnitTest());
    }

    @Test
    @DisplayName("Plugin World loaded Lobby")
    public void testWorldLobbyLoaded(){
        Assert.assertNotNull(server.getWorld(Strings.PLUGIN_WORLD_LOBBY));
    }

    @Test
    @DisplayName("Plugin World Loading World")
    public void testWorldWorldLoaded(){
        Assert.assertNotNull(server.getWorld(Strings.PLUGIN_WORLD_WORLD));
    }

    @Test
    @DisplayName("Plugin World Loading World Nether")
    public void testWorldWorldNetherLoaded(){
        Assert.assertNotNull(server.getWorld(Strings.PLUGIN_WORLD_WORLD_THE_END));
    }

    @Test
    @DisplayName("Plugin World Loading World End")
    public void testWorldWorldEndLoaded(){
        Assert.assertNotNull(server.getWorld(Strings.PLUGIN_WORLD_WORLD_THE_END));
    }

    @Test
    @DisplayName("Plugin Offline Player #1")
    public void testPlayer1Online(){
        Assert.assertTrue(player1.isOnline());
    }

    @Test
    @DisplayName("Plugin Offline Player #2")
    public void testPlayer2Online(){
        Assert.assertTrue(player2.isOnline());
    }

    @Test
    @DisplayName("Plugin Offline Player #3")
    public void testPlayer3Offline(){
        Assert.assertTrue(player3.isOnline());
    }
    
    @Test
    @DisplayName("Test Gamemmode Player #1")
    public void testGameModePlayer1(){
        Assert.assertEquals(GameMode.SURVIVAL, player1.getGameMode()); 
    }

    @Test
    @DisplayName("Test Gamemmode Player #2")
    public void testGameModePlayer2(){
        Assert.assertEquals(GameMode.SURVIVAL, player2.getGameMode());
    }

    @Test
    @DisplayName("Test Gamemmode Player #3")
    public void testGameModePlayer3(){
        Assert.assertEquals(GameMode.SURVIVAL, player3.getGameMode());
    }

    @Test
    @DisplayName("Test BlockBreak Player #1")
    public void testSimulateBlockBreakPlayer1(){
        Block b = server.getWorld(Strings.PLUGIN_WORLD_LOBBY).getBlockAt(1, 1, 1);
        Assertions.assertNotEquals(Material.AIR, b.getType());
        player1.simulateBlockBreak(b);
        Assert.assertEquals(Material.AIR, b.getType());
    }

    @Test
    @DisplayName("Test BlockBreak Player #2")
    public void testSimulateBlockBreakPlayer2(){
        Block b = server.getWorld(Strings.PLUGIN_WORLD_LOBBY).getBlockAt(1, 1, 2);
        Assertions.assertNotEquals(Material.AIR, b.getType());
        player2.simulateBlockBreak(b);
        Assert.assertEquals(Material.GRASS, b.getType());
    }

    @Test
    @DisplayName("Test BlockBreak Player #3")
    public void testSimulateBlockBreakPlayer3(){
        Block b = server.getWorld(Strings.PLUGIN_WORLD_LOBBY).getBlockAt(1, 1, 3);
        Assertions.assertNotEquals(Material.AIR, b.getType());
        player2.simulateBlockBreak(b);
        Assert.assertEquals(Material.GRASS, b.getType());
    }

    @Test
    @DisplayName("Test Plugin 2")
    public void test2(){

        server.execute("poke", player1, player2.getName());
        Assert.assertFalse(server.getOnlinePlayers().isEmpty());
    }
}