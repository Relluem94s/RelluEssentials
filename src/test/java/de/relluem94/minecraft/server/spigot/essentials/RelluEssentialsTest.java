package de.relluem94.minecraft.server.spigot.essentials;


import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
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

public class RelluEssentialsTest {

    private static ServerMock server;
    private static RelluEssentials plugin;
    private static DB db;

    @BeforeClass
    public static void setUpDB() throws ManagedProcessException {
        Logger.getLogger(RelluEssentialsTest.class.getName()).severe("StartUp Database");
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


        
        Logger.getLogger(RelluEssentialsTest.class.getName()).severe("StartUp Plugin");
        server = MockBukkit.mock();
        plugin = MockBukkit.load(RelluEssentials.class);
    }

    @AfterClass
    public static void tearDownDB() throws ManagedProcessException{
        Logger.getLogger(RelluEssentialsTest.class.getName()).severe("Tear Down Plugin");
        MockBukkit.unmock();

        Logger.getLogger(RelluEssentialsTest.class.getName()).severe("Tear Down Database");
        db.stop();
    }


    @Test
    @DisplayName("Test Plugin 1")
    public void test(){
        System.out.println("Test1 >> 1");
        Assert.assertTrue(plugin.isEnabled());
        System.out.println("Test1 >> 2");
        Assert.assertTrue(plugin.isUnitTest());
        System.out.println("Test1 >> 3");

    }


    
    @Test
    @DisplayName("Test Plugin 2")
    public void test2(){
        System.out.println("Test2 >> 1");
        server.addSimpleWorld(Strings.PLUGIN_WORLD_LOBBY);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD_NETHER);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD_THE_END);

        System.out.println("Test2 >> 2");

        PlayerMock player = server.addPlayer();
        PlayerMock player2 = server.addPlayer();

        System.out.println("Test2 >> 3");

        player.assertGameMode(GameMode.SURVIVAL); // TODO Hangs here!
        player2.assertGameMode(GameMode.CREATIVE);

/*
 * 
 * 
Running de.relluem94.minecraft.server.spigot.essentials.RelluEssentialsTest
Mar 28, 2023 11:25:42 AM de.relluem94.minecraft.server.spigot.essentials.RelluEssentialsTest setUpDB
SEVERE: StartUp Database
Mar 28, 2023 11:25:45 AM de.relluem94.minecraft.server.spigot.essentials.RelluEssentialsTest setUpDB
SEVERE: StartUp Plugin
Test1 >> 1
Test1 >> 2
Test1 >> 3
Test2 >> 1
Test2 >> 2
Test2 >> 3
[11:25:46 SEVERE]: Tear Down Plugin
 * 
 */

        System.out.println("Test2 >> 4");

        player.simulateBlockBreak(server.getWorld(Strings.PLUGIN_WORLD_LOBBY).getBlockAt(1, 1, 1));
        player2.simulateBlockBreak(server.getWorld(Strings.PLUGIN_WORLD_LOBBY).getBlockAt(1, 1, 2));

        System.out.println("Test2 >> 5");

        server.execute("poke", player, player2.getName());

        System.out.println("Test2 >> 6");

        Assertions.assertTrue(server.getOnlinePlayers().isEmpty());
        Assert.assertTrue(server.getOnlinePlayers().isEmpty());

        System.out.println("Test2 >> 8");
    }
}